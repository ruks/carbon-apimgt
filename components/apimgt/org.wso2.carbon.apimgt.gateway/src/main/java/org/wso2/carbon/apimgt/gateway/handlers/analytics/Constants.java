/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.wso2.carbon.apimgt.gateway.handlers.analytics;

/**
 * analytics related gateway constants
 */
public class Constants {
    public static final String REQUEST_START_TIME_PROPERTY = "apim.analytics.request.start.time";
    public static final String BACKEND_START_TIME_PROPERTY = "apim.analytics.backend.start.time";
    public static final String BACKEND_END_TIME_PROPERTY = "apim.analytics.backend.end.time";
    public static final String BACKEND_RESPONSE_CODE = "api.analytics.backend.response_code";
    public static final String USER_AGENT_PROPERTY = "api.analytics.user.agent";
    public static final String CACHED_RESPONSE_KEY = "CachableResponse";
    public static final String SKIP_DEFAULT_METRICS_PUBLISHING = "skip_default_metrics_publishing";

    public static final String REGION_ID = "asia";
    public static final String DEPLOYMENT_ID = "prod";
    public static final String SUCCESS_EVENT_TYPE = "response";
    public static final String FAULTY_EVENT_TYPE = "fault";
    public static final String UNKNOWN_VALUE = "UNKNOWN";
    public static final int UNKNOWN_INT_VALUE = -1;
    public static final String ANONYMOUS_VALUE = "anonymous";

    public static final class ERROR_CODE_RANGES {
        public static final int AUTH_FAILURE_START = 900900;
        public static final int AUTH_FAILURE__END = 901000;

        public static final int THROTTLED_FAILURE_START = 900800;
        public static final int THROTTLED_FAILURE__END = 900900;

        public static final int TARGET_FAILURE_START = 101500;
        public static final int TARGET_FAILURE__END = 101600;
    }

    public static final int RESOURCE_NOT_FOUND_ERROR_CODE = 404;
    public static final int METHOD_NOT_ALLOWED_ERROR_CODE = 405;
    public static final int ENDPOINT_SUSPENDED_ERROR_CODE = 303001;
}
