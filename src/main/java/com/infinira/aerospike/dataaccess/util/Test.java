package com.infinira.aerospike.dataaccess.util;

import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.RecordExistsAction;
import com.infinira.aerospike.dataaccess.repository.AerospikeConfig;
import com.infinira.aerospike.dataaccess.repository.AerospikeHost;

import java.io.IOException;

/**
 * Created by Siddharth Garimella on 2/22/2016.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        AerospikeHost[] hosts = new AerospikeHost[1];
        hosts[0] = new AerospikeHost("localhost",3000);
        //hosts[1] = new AerospikeHost("localhost1",3000);
        //hosts[2] = new AerospikeHost("localhost1",3000);
        // Set client policy
        ClientPolicy clientPolicy = new ClientPolicy();
        clientPolicy.failIfNotConnected = true;
        clientPolicy.timeout = 2000;
        clientPolicy.maxThreads = 300;
        clientPolicy.readPolicyDefault.timeout = 500;
        clientPolicy.readPolicyDefault.maxRetries = 4;
        clientPolicy.readPolicyDefault.sleepBetweenRetries = 150;
        clientPolicy.writePolicyDefault.recordExistsAction = RecordExistsAction.UPDATE;
        clientPolicy.queryPolicyDefault.priority = Priority.HIGH;
        clientPolicy.queryPolicyDefault.recordQueueSize = 5000;
        clientPolicy.scanPolicyDefault.concurrentNodes = false;
        clientPolicy.scanPolicyDefault.includeBinData = true;
        clientPolicy.scanPolicyDefault.priority = Priority.HIGH;
        clientPolicy.scanPolicyDefault.timeout = 1500;
        clientPolicy.scanPolicyDefault.scanPercent = 100;
        clientPolicy.writePolicyDefault.sendKey = true;
        clientPolicy.user = "testuser";
        clientPolicy.password = "testpassword";
        AerospikeConfig aerospikeConfig = new AerospikeConfig();
        aerospikeConfig.setDefaultNameSpace("test");
        aerospikeConfig.setClientPolicy(clientPolicy);
        aerospikeConfig.setAerospikeHosts(hosts);
        // export to json
        System.out.println(Utils.toJSONString(aerospikeConfig));

        String configJson = Utils.readTextFile("./target/classes/AerospikeConfig.json");
        aerospikeConfig = (AerospikeConfig) Utils.toObject(configJson, AerospikeConfig.class);
        System.out.println(aerospikeConfig.toString());
    }
}
