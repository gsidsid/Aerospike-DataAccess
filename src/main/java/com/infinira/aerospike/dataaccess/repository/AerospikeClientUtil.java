/*
 * Copyright 2016 Infinira Software Private Limited.
 *
 * Portions may be licensed to Infinira Software Private Limited. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.infinira.aerospike.dataaccess.repository;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Value;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.helper.query.QueryEngine;
import com.infinira.aerospike.dataaccess.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Client utility class
 */
public class AerospikeClientUtil {
    private final Logger logger = LoggerFactory.getLogger(AerospikeClientUtil.class);
    private static AerospikeConfig aerospikeConfig;

    // Aerospike client and queryEngine variables.
    private static AerospikeClient client = null;
    private static QueryEngine queryEngine = null;

    // Insert, Update and Batch policies.
    private static WritePolicy insertPolicy;
    private static WritePolicy updatePolicy;
    private static BatchPolicy batchPolicy;
    private static Boolean lockObject=true;

    /**
     * Read configuration file (JSON format)
     * @throws IOException
     */
    private static void readConfigFile() throws IOException {
        String configJson = Utils.readTextFile("./target/classes/AerospikeConfig.json");
        aerospikeConfig = (AerospikeConfig) Utils.toObject(configJson, AerospikeConfig.class);
    }

    /**
     * Get cached Aerospike client
     * @return client
     */
    public static AerospikeClient getClient() {
        // If client is already created, return client.
        if (client != null) return client;
        synchronized(lockObject) {
            try {
                // Check if the config is already set. If not, read from file.
                // For programatic control, use set API to initialize configuration object.
                if (aerospikeConfig == null)
                    readConfigFile();


                // Create a new client object
                client = new AerospikeClient(aerospikeConfig.getClientPolicy(), aerospikeConfig.getHosts());
            /*  RecordExistsAction options
                CREATE_ONLY :  Create only.
                REPLACE     :  Create or replace record.
                REPLACE_ONLY:  Replace record only.
                UPDATE      :  Create or update record.
                UPDATE_ONLY :  Update record only.
            */
                // Initialize insert, update and batch policies
                insertPolicy = new WritePolicy(client.writePolicyDefault);
                insertPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
                // Set send key to true as we want to store key value as well.
                insertPolicy.sendKey = true;
                updatePolicy = new WritePolicy(client.writePolicyDefault);
                updatePolicy.recordExistsAction = RecordExistsAction.UPDATE_ONLY;
                batchPolicy = client.batchPolicyDefault;

                // Set the UseDoubleType by default. Note that this only works for Aerospike 3.6.0 onwards.
                Value.UseDoubleType = true;

                return client;
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        return null;
    }

    // Set of getter methods.

    public static String getDefaultNamespace() {
        if (aerospikeConfig != null) return aerospikeConfig.getDefaultNameSpace();
        else return null;
    }

    public static String getDbUser() {
        if (aerospikeConfig != null) return aerospikeConfig.getClientPolicy().user;
        else return null;
    }

    public static String getDbUserPassword() {
        if (aerospikeConfig != null) return aerospikeConfig.getClientPolicy().password;
        else return null;
    }

    public static ClientPolicy getPolicy() {
        if (aerospikeConfig != null) return aerospikeConfig.getClientPolicy();
        else return null;
    }

    public static QueryEngine getQueryEngine() {
        if (queryEngine == null)
            queryEngine = new QueryEngine(client);
        return queryEngine;
    }

    public static WritePolicy getInsertPolicy() {
        return insertPolicy;
    }

    public static WritePolicy getUpdatePolicy() {
        return updatePolicy;
    }

    public static BatchPolicy getBatchPolicy() {
        return batchPolicy;
    }

    /**
     * Set configuration programatically instead of loading from a file.
     * @param aerospikeConfig
     */
    public static void setAerospikeConfig(AerospikeConfig aerospikeConfig) {
        AerospikeClientUtil.aerospikeConfig = aerospikeConfig;
    }

    /**
     * Closes the client.
     */
    public static void closeClient() {
        client.close();
    }
}
