// Copyright (c)  WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/http;
import org.wso2.carbon.apimgt.gateway.constants as constants;
import ballerina/time;
import org.wso2.carbon.apimgt.gateway.dto as dto;
import org.wso2.carbon.apimgt.gateway.throttling as throttling;
import ballerina/io;

map blockConditions;
map throttleDataMap;
public stream<dto:RequestStream> requestStream;
public stream<dto:GlobalThrottleStream> globalThrottleStream;
future ftr = start initializeThrottleSubscription();

public function isBlockConditionExist(string key) returns (boolean) {
    if (blockConditions.hasKey(key)){
        boolean status = check <boolean>blockConditions[key];
        return status;
    }
    return false;
}
public function putBlockCondition(string key, string value) {
    blockConditions[key] = value;
}

public function isThrottled(string key) returns (boolean) {
    boolean isThrottled = throttleDataMap.hasKey(key);
    if (isThrottled){
        int currentTime = time:currentTime().time;
        int timeStamp = check <int>throttleDataMap[key];
        if (timeStamp >= currentTime) {
            return isThrottled;
        } else {
            boolean status = throttleDataMap.remove(key);
            return false;
        }
    }
    return isThrottled;
}
public function getClientIp(http:Request request) returns (string) {
    string clientIp;
    string header = "";
    try {
        header = request.getHeader(constants:X_FORWARD_FOR_HEADER);
    }catch(error e){
        log:printError("Error occurred when getting X_FORWARD_FOR_HEADER: ");
    }
    clientIp = header;
    int idx = header.indexOf(",");
    if (idx > -1) {
        clientIp = clientIp.substring(0, idx);
    }
    return clientIp;
}

public function publishNonThrottleEvent(dto:RequestStream request) {
    requestStream.publish(request);
}
function initializeThrottleSubscription() {
    globalThrottleStream.subscribe(onReceiveThrottleEvent);
    requestStream.subscribe(throttling:startToPublish);
}
public function onReceiveThrottleEvent(dto:GlobalThrottleStream throttleEvent) {

    if(throttleEvent.isThrottled){
        throttleDataMap[throttleEvent.throttleKey]=throttleEvent.expiryTimeStamp;
    }

}