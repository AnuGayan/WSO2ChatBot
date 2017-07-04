package org.example.service.models;/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.example.service.utils.CommonLogger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class CsvDB {
    private ArrayList<Product> products = new ArrayList<>();


    private ArrayList<Product> ProductDB() throws FileNotFoundException, ParseException {

        products = new ArrayList<>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("Data.csv");

        Scanner scan = new Scanner(is);

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] lineArray = line.split(",");
            products.add(new Product(lineArray[0], new SimpleDateFormat("dd/MM/yyyy").
                    parse(lineArray[1]), lineArray[2], Integer.parseInt(lineArray[3])));
        }
        System.out.println(products);
        return products;
    }

    private ArrayList<Product> filteredProductArray(String productName) {
        ArrayList<Product> inMemoryFilteredProductList = new ArrayList<>();
        for (Product product : products) {
            if (productName.equals("DAS")) {
                if (product.getProductName().equals("WSO2 Complex Event Processing Server") ||
                        product.getProductName().equals("WSO2 Machine Learner") ||
                        product.getProductName().equals("WSO2 Data Analytics Server")) {
                    CommonLogger.log(CsvDB.class, "info", "ProductName - " +
                            product.getProductName());
                    inMemoryFilteredProductList.add(product);


                }
            }
            if (productName.equals("IS")) {
                if (product.getProductName().equals("WSO2 Identity Server")) {
                    CommonLogger.log(CsvDB.class, "info", "ProductName - " +
                            product.getProductName());
                    inMemoryFilteredProductList.add(product);
                }
            }
            if (productName.equals("IoT Server")) {
                if (product.getProductName().equals("WSO2 IoT Server")) {
                    CommonLogger.log(CsvDB.class, "info", "ProductName - " +
                            product.getProductName());
                    inMemoryFilteredProductList.add(product);

                }
            }
            if (productName.equals("Enterprise Integrator")) {
                if (product.getProductName().equals("WSO2 Enterprise Integrator") ||
                        product.getProductName().equals("WSO2 Enterprise Service Bus") ||
                        product.getProductName().equals("WSO2 Message Broker") ||
                        product.getProductName().equals("WSO2 Application Server") ||
                        product.getProductName().equals("WSO2 Data Services Server") ||
                        product.getProductName().equals("WSO2 Business Process Server")) {
                    CommonLogger.log(CsvDB.class, "info", "ProductName - " +
                            product.getProductName());
                    inMemoryFilteredProductList.add(product);

                }
            }
            if (productName.equals("APIM")) {
                if (product.getProductName().equals("WSO2 API Manager")) {
                    CommonLogger.log(CsvDB.class, "info", "ProductName - " +
                            product.getProductName());
                    inMemoryFilteredProductList.add(product);
                }
            }
            if (productName.equals("All Products")) {
                if (product.getProductName().equals("WSO2 Enterprise Integrator") ||
                        product.getProductName().equals("WSO2 Enterprise Service Bus") ||
                        product.getProductName().equals("WSO2 Message Broker") ||
                        product.getProductName().equals("WSO2 Application Server") ||
                        product.getProductName().equals("WSO2 Data Services Server") ||
                        product.getProductName().equals("WSO2 Business Process Server") ||
                        product.getProductName().equals("WSO2 Complex Event Processing Server") ||
                        product.getProductName().equals("WSO2 Machine Learner") ||
                        product.getProductName().equals("WSO2 Data Analytics Server") ||
                        product.getProductName().equals("WSO2 Identity Server") ||
                        product.getProductName().equals("WSO2 IoT Server")) {
                    CommonLogger.log(CsvDB.class, "info", "ProductName - " +
                            product.getProductName());
                    inMemoryFilteredProductList.add(product);

                }
            }
        }

        return inMemoryFilteredProductList;
    }


    public int count(String productName, String from, String to) throws Exception {
        int count = 0;
        ArrayList<Product> inMemoryFilteredProductList;
        ArrayList<Product> inMemoryFilteredFinalList = new ArrayList<>();
        products = ProductDB();
        Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(from);
        Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(to);

        CommonLogger.log(CsvDB.class, "info", "ProductName - " + productName +
                "  time period From - " + dateFrom + "  To - " + dateTo);

        inMemoryFilteredProductList = filteredProductArray(productName);
        for (Product anInMemoryFilteredProductList : inMemoryFilteredProductList) {


            if (anInMemoryFilteredProductList.getPurchesedDate().after(dateFrom) &&
                    anInMemoryFilteredProductList.getPurchesedDate().before(dateTo)) {
                inMemoryFilteredFinalList.add(anInMemoryFilteredProductList);
//                k++;
            }
            CommonLogger.log(CsvDB.class, "info", "SelectedFinalCount" + inMemoryFilteredFinalList.size());

            count = inMemoryFilteredFinalList.size();

        }
        return count;
    }

    public int count(String productName) throws Exception {
        int count = 0;
        ArrayList<Product> inMemoryFilteredProductList;
        products = ProductDB();
        CommonLogger.log(CsvDB.class, "info", "ProductName - " + productName);

        inMemoryFilteredProductList = filteredProductArray(productName);
        count = inMemoryFilteredProductList.size();
        return count;

    }
}

class Product {
    private String productName;
    private Date purchesedDate;
    private int productInstance;
    private String customerName;

    public int getProductInstance() {
        return productInstance;
    }

    String getProductName() {
        return productName;
    }

    Date getPurchesedDate() {
        return purchesedDate;
    }

    String getCustomerName() {
        return customerName;
    }

    Product(String productName, Date purchesedDate, String customerName, int productInstance) {
        this.customerName = customerName;
        this.productName = productName;
        this.purchesedDate = purchesedDate;
        this.productInstance = productInstance;


    }
}