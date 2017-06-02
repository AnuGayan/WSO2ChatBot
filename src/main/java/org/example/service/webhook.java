/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.service;

import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;
import org.example.service.utils.CommonLogger;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;


/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
//@Path("/service")
@Path("/webhook")
public class webhook {
    private HashMap payload = new HashMap();
    private String parameter1;
    private String parameter2;
    private String parameter3;
    private AIResponse response1;
    Result result;
    Fulfillment fulfillment;
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Fulfillment post(AIResponse response) {
        CommonLogger.log(webhook.class,"info","[httpPOST Invoked]");

        if (!response.getResult().isActionIncomplete()){
            payload = response.getResult().getParameters();
            String[] keyset = (String[]) payload.keySet().toArray();
            parameter1= payload.get(keyset[0]).toString();
            parameter2= payload.get(keyset[1]).toString();
            parameter3= payload.get(keyset[2]).toString();


        }
        String res = "";
        fulfillment.setDisplayText(res);


        // TODO: Implementation for HTTP POST request
        System.out.println("POST invoked");
        return fulfillment;
    }

    public webhook() {


    }

}
