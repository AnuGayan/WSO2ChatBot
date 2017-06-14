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


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.service.models.CsvDB;
import org.example.service.utils.CommonLogger;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */

@Path("/webhook")
public class webhook {


    private CsvDB csvDB = new CsvDB();
    private String from;
    private String to;
    private String timePeriod;
    private static String name;
    private int count;


    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject post(JsonObject response) throws Exception {

        System.out.println("Post invoke");
        CommonLogger.log(webhook.class, "info", "[httpPOST Invoked]");
        System.out.println(response);

        /**
         *
         * check whether the intent is complete.
         *
         * **/
        if (!response.get("result").getAsJsonObject().get("actionIncomplete").getAsBoolean()) {

            String productName;

            /**
             *
             * Purchase count intent hit
             *
             * **/
            if (response.get("result").getAsJsonObject().get("action").getAsString().equals("purchase.count")) {
                productName = response.get("result").getAsJsonObject().get("parameters").
                        getAsJsonObject().get("Products").getAsString();
                name = productName;

                if (response.get("result").getAsJsonObject().get("parameters").getAsJsonObject().
                        get("timeperiod").getAsJsonObject().get("date-period") == null) {
                    if (!response.get("result").getAsJsonObject().get("parameters").getAsJsonObject().
                            get("timeperiod").getAsJsonObject().get("date").toString().isEmpty()) {
                        from = response.get("result").getAsJsonObject().get("parameters").getAsJsonObject().
                                get("timeperiod").getAsJsonObject().get("date").toString();
                        from = from.substring(1, from.length() - 1);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        to = dateFormat.format(date);
                    }
                } else {
                    timePeriod = response.get("result").getAsJsonObject().get("parameters").
                            getAsJsonObject().get("timeperiod").getAsJsonObject().get("date-period").toString();
                    int iend = timePeriod.indexOf("/");
                    if (iend != -1) {
                        from = timePeriod.substring(1, iend);
                        to = timePeriod.substring(iend + 1, timePeriod.length() - 1);
                    }
                }

                CommonLogger.log(CsvDB.class, "info", "ProductName - "
                        + productName + "  timeperiod - " + timePeriod);
                count = csvDB.count(productName, from, to);
                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = "No one has purchase " + productName + " in between " + from + " to " + to;
                } else {
                    payload = "Purchase Count of " + productName + " in between" + from + " to " + to + " is " + count;
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";
                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;
            }
            /**
             *
             * purchase count follow up
             *
             * different product/ different time
             *
             * **/
            else if (response.get("result").getAsJsonObject().get("action").getAsString().
                    equals("Purchasecount.Purchasecount-diffProduct")) {
                productName = response.get("result").getAsJsonObject().get("parameters").
                        getAsJsonObject().get("Products").getAsString();
                timePeriod = response.get("result").getAsJsonObject().get("parameters").
                        getAsJsonObject().get("timeperiod").toString();

                /**
                 *
                 * different time
                 *
                 * **/
                if (productName.equals("")) {
                    productName = name;
                    timePeriod = response.get("result").getAsJsonObject().get("parameters").
                            getAsJsonObject().get("timeperiod").getAsJsonObject().get("date-period").toString();
                    int iend = timePeriod.indexOf("/");
                    if (iend != -1) {
                        from = timePeriod.substring(1, iend);
                        to = timePeriod.substring(iend + 1, timePeriod.length() - 1);
                        count = csvDB.count(productName, from, to);
                    }
                }

                /**
                 *
                 * different product
                 *
                 * **/
                else if (timePeriod.equals("")) {
                    count = csvDB.count(productName);
                }

                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = "No one has purchase " + productName + " in between " + from + " to " + to;
                } else {
                    payload = "Purchase Count of " + productName + " in between" + from + " to " + to + " is " + count;
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";
                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;

            }

            /**
             *
             * Purchase count customer followup intent hit
             *
             * **/

            else if (response.get("result").getAsJsonObject().get("action").getAsString().
                    equals("Purchasecount.Purchasecount-customer")) {
                productName = response.get("result").getAsJsonObject().get("contects").getAsJsonObject().
                        get("parameters").getAsJsonObject().get("Products").getAsString();

                count = csvDB.count(productName);
                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = productName + " doesn't have customers ";
                } else {
                    payload = productName + " has " + count + " customers";
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";
                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;


            }
            /**
             *
             * Customer count intent hit
             *
             * **/

            else if (response.get("result").getAsJsonObject().get("action").getAsString().equals("customer.count")) {
                productName = response.get("result").getAsJsonObject().get("parameters").
                        getAsJsonObject().get("Products").getAsString();
                name = productName;
                count = csvDB.count(productName);
                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = productName + " doesn't have customers ";
                } else {
                    payload = productName + " has " + count + " customers";
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";
                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;
            }
            /** Customer count follow up intent purchase**/
            else if (response.get("result").getAsJsonObject().get("action").getAsString()
                    .equals("CustomerCount.CustomerCount-purchase")) {
                productName = name;
                timePeriod = response.get("result").getAsJsonObject().get("parameters").
                        getAsJsonObject().get("timeperiod").getAsJsonObject().get("date-period").toString();
                int iend = timePeriod.indexOf("/");
                if (iend != -1) {
                    from = timePeriod.substring(1, iend);
                    to = timePeriod.substring(iend + 1, timePeriod.length() - 1);
                }

                count = csvDB.count(productName, from, to);
                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = "No one has purchase " + productName + " in between " + from + " to " + to;
                } else {
                    payload = "Purchase Count of " + productName + " in between" + from + " to " + to + " is " + count;
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";
                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;

            } else if (response.get("result").getAsJsonObject().get("action")
                    .getAsString().equals("product-purchaseCount")) {
                productName = response.get("result").getAsJsonObject().get("contexts")
                        .getAsJsonArray().get(0).getAsJsonObject().get("parameters")
                        .getAsJsonObject().get("Products").toString();
                productName = productName.substring(1,productName.length()-1);

                timePeriod = response.get("result").getAsJsonObject().get("parameters").
                        getAsJsonObject().get("timeperiod").getAsJsonObject().get("date-period").toString();
                int iend = timePeriod.indexOf("/");
                if (iend != -1) {
                    from = timePeriod.substring(1, iend);
                    to = timePeriod.substring(iend + 1, timePeriod.length() - 1);
                }
                count = csvDB.count(productName, from, to);
                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = "No one has purchase "
                            + productName + " in between " + from + " to " + to;
                } else {
                    payload = "Purchase Count of "
                            + productName + " in between " + from + " to " + to + " is " + count;
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";

                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;

            }else if (response.get("result").getAsJsonObject().get("action")
                    .getAsString().equals("product-customers")){

                productName = response.get("result").getAsJsonObject().get("contexts").getAsJsonArray().get(0)
                        .getAsJsonObject().get("parameters").getAsJsonObject().get("Products").toString();
                productName = productName.substring(1,productName.length()-1);
                count = csvDB.count(productName);
                JsonObject object;
                String payload;
                if (count == 0) {
                    payload = productName + " doesn't have customers ";
                } else {
                    payload = productName + " has " + count + " customers";
                }

                String res = "{\n" +
                        "\"speech\": \"" + payload + "\",\n" +
                        "\"displayText\": \"" + payload + "\",\n" +
                        "\"source\": \"WSO2 inc Database\"}";
                JsonParser parser = new JsonParser();
                object = (JsonObject) parser.parse(res);

                return object;
            }
            return null;


        }
        return null;
    }
}




