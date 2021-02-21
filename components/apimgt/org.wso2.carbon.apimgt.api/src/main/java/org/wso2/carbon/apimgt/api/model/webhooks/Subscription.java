/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.api.model.webhooks;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents the model for webhook subscriptions
 */
public class Subscription implements Serializable {
    private String apiKey;
    private String tenantDomain;
    private String apiUuid;
    private String appID;
    private String callback;
    private String topic;
    private String secret;
    private Date updatedTime;
    private long expiryTime;
    private Date lastDelivery;
    private int lastDeliveryState;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Date getUpdatedTime() {
        return updatedTime != null ? new Date(updatedTime.getTime()) : null;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime != null ? new Date(updatedTime.getTime()) : null;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getTenantDomain() {
        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {

        this.tenantDomain = tenantDomain;

    }
    public Date getLastDelivery() {

        return lastDelivery;
    }

    public void setLastDelivery(Date lastDelivery) {

        this.lastDelivery = lastDelivery;
    }

    public int getLastDeliveryState() {

        return lastDeliveryState;
    }

    public void setLastDeliveryState(int lastDeliveryState) {

        this.lastDeliveryState = lastDeliveryState;
    }

    public String getApiUuid() {

        return apiUuid;
    }

    public void setApiUuid(String apiUuid) {
        this.apiUuid = apiUuid;
    }

    public String getApiKey() {

        return apiKey;
    }

    public void setApiKey(String apiKey) {

        this.apiKey = apiKey;
    }
}
