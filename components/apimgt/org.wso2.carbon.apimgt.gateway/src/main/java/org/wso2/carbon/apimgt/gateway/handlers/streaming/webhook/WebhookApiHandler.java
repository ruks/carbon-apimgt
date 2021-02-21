/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.gateway.handlers.streaming.webhook;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.api.ApiUtils;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.rest.RESTConstants;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.wso2.carbon.apimgt.gateway.handlers.Utils;
import org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.impl.APIConstants;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.apache.axis2.Constants.Configuration.HTTP_METHOD;

/**
 * Handler used for web hook apis. This handler retrieves the topic name, to which subscription request is coming and
 * will set it to the synapse msg context.
 * <pre>
 * {@code
 * <handler class="org.wso2.carbon.apimgt.gateway.handlers.streaming.webhook.WebhookApiHandler">
 *     <property name="eventReceiverResourcePath" value="/webhooks_events_receiver_resource"/>
 *     <property name="topicQueryParamName" value="hub.topic"/>
 * </handler>
 * }
 * </pre>
 */
public class WebhookApiHandler extends APIAuthenticationHandler {

    private static final Log log = LogFactory.getLog(WebhookApiHandler.class);
    private static final String EMPTY_STRING = "";

    private String eventReceiverResourcePath = APIConstants.WebHookProperties.DEFAULT_SUBSCRIPTION_RESOURCE_PATH;
    private String topicQueryParamName = APIConstants.WebHookProperties.DEFAULT_TOPIC_QUERY_PARAM_NAME;

    @Override
    public boolean handleRequest(MessageContext synCtx) {

        String requestSubPath = getRequestSubPath(synCtx);
        // all other requests are assumed to be for subscription as there will be only 2 resources for web hook api
        if (!requestSubPath.startsWith(eventReceiverResourcePath)) {
            String topicName = getTopicName(ApiUtils.getFullRequestPath(synCtx));
            if (topicName.isEmpty()) {
                handleFailure(synCtx, "Topic name not found for web hook subscription request");
                return false;
            }
            org.apache.axis2.context.MessageContext axisCtx = ((Axis2MessageContext) synCtx).getAxis2MessageContext();
            Object httpVerb = axisCtx.getProperty(HTTP_METHOD);
            axisCtx.setProperty(HTTP_METHOD, APIConstants.SubscriptionCreatedStatus.SUBSCRIBE);
            synCtx.setProperty(APIConstants.API_TYPE, APIConstants.API_TYPE_WEBSUB);
            synCtx.setProperty(APIConstants.API_ELECTED_RESOURCE, topicName);
            boolean authenticationResolved = super.handleRequest(synCtx);
            ((Axis2MessageContext) synCtx).getAxis2MessageContext().
                    setProperty(Constants.Configuration.HTTP_METHOD, httpVerb);
            return authenticationResolved;
        } else {
            org.apache.axis2.context.MessageContext axisMsgContext = ((Axis2MessageContext) synCtx).
                    getAxis2MessageContext();
            try {
                RelayUtils.buildMessage(axisMsgContext);
                String payload;
                if (JsonUtil.hasAJsonPayload(axisMsgContext)) {
                    payload = JsonUtil.jsonPayloadToString(axisMsgContext);
                } else {
                    payload = synCtx.getEnvelope().getBody().getFirstElement().toString();
                }
                synCtx.setProperty(APIConstants.Webhooks.PAYLOAD_PROPERTY, payload);
                return true;
            } catch (IOException | XMLStreamException e) {
                log.error("Error while building the message", e);
                return false;
            }
        }
    }

    private String getRequestSubPath(MessageContext synCtx) {

        Object requestSubPath = synCtx.getProperty(RESTConstants.REST_SUB_REQUEST_PATH);
        if (requestSubPath != null) {
            return requestSubPath.toString();
        }
        return Utils.getSubRequestPath(Utils.getSelectedAPI(synCtx), synCtx);
    }

    /**
     * Retrieves the name of the topic from the query param to which the subscription request is entering for the
     * web hook api.
     *
     * @param url request url
     * @return topic name
     */
    private String getTopicName(String url) {

        int queryIndex = url.indexOf('?');
        if (queryIndex != -1 && url.contains(topicQueryParamName)) {
            String query = url.substring(queryIndex + 1);
            String[] entries = query.split(RESTConstants.QUERY_PARAM_DELIMITER);
            String name;
            for (String entry : entries) {
                int index = entry.indexOf('=');
                if (index != -1) {
                    try {
                        name = entry.substring(0, index);
                        if (name.equalsIgnoreCase(topicQueryParamName)) {
                            return URLDecoder.decode(entry.substring(index + 1), RESTConstants.DEFAULT_ENCODING);
                        }
                    } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                        log.error("Error extracting topic name from query param", e);
                        return EMPTY_STRING;
                    }
                }
            }
        }
        return EMPTY_STRING;
    }

    /**
     * This method handle the failure
     *
     * @param messageContext   message context of the request
     * @param errorDescription description of the error
     */
    private void handleFailure(MessageContext messageContext, String errorDescription) {

        OMElement payload = getFaultPayload(errorDescription);
        Utils.setFaultPayload(messageContext, payload);
        Mediator sequence =
                messageContext.getSequence(APIConstants.WebHookProperties.WEB_HOOK_SUBSCRIPTION_FAILURE_HANDLER);
        if (sequence != null && !sequence.mediate(messageContext)) {
            return;
        }
        Utils.sendFault(messageContext, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    /**
     * @param description description of the error
     * @return the OMElement
     */
    private OMElement getFaultPayload(String description) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(APISecurityConstants.API_SECURITY_NS,
                                               APISecurityConstants.API_SECURITY_NS_PREFIX);
        OMElement payload = fac.createOMElement("fault", ns);
        OMElement error = fac.createOMElement("error", ns);
        error.setText(description);
        payload.addChild(error);
        return payload;
    }

    public void setEventReceiverResourcePath(String eventReceiverResourcePath) {
        this.eventReceiverResourcePath = eventReceiverResourcePath;
    }

    public void setTopicQueryParamName(String topicQueryParamName) {
        this.topicQueryParamName = topicQueryParamName;
    }
}
