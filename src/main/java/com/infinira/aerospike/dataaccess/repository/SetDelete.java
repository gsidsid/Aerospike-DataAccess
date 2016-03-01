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
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ResultCode;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.policy.ScanPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class has been taken from Aerospike utility code: https://github.com/aerospike/delete-set and licensed under
 * Apache License, Version 2.0.
 */

public class SetDelete {
    private static Logger log = LoggerFactory.getLogger(SetDelete.class);
    static int count = 0;
    public static void clearSet(String namespace, String set) throws Exception {

        try {
            final AerospikeClient client = AerospikeClientUtil.getClient();
            ScanPolicy scanPolicy = new ScanPolicy();
            scanPolicy.includeBinData = false;
			/*
			 * scan the entire Set using scannAll(). This will scan each node
			 * in the cluster and return the record Digest to the call back object
			 */
            log.info("Deleting all records from " + namespace + "." + set);
            client.scanAll(scanPolicy, namespace, set, new ScanCallback() {

                public void scanCallback(Key key, Record record) throws AerospikeException {
					/*
					 * for each Digest returned, delete it using delete()
					 */
                    if (client.delete(null, key))
                        count++;
					/*
					 * after 25,000 records delete, return print the count.
					 */
                    if (count % 25000 == 0){
                        log.info("Deleted "+ count + " records");
                    }
                }
            });
            log.info("Deleted "+ count + " records from set " + set);
        } catch (AerospikeException e) {
            int resultCode = e.getResultCode();
            log.info(ResultCode.getResultString(resultCode));
            log.debug("Error details: ", e);
        }
    }

}

