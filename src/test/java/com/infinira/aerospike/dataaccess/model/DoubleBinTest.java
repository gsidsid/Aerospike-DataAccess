package com.infinira.aerospike.dataaccess.model;


import com.aerospike.client.*;

// To check UseDoubleType setting. Taken from stackoverflow..
public class DoubleBinTest {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int    port = 3000;
        AerospikeClient client = new AerospikeClient(null, host, port);
        Value.UseDoubleType = true;

        String namespace = "test";
        String set       = "doubleset";
        Key key       = new Key(namespace, set, "doubletest");

        Bin testBin1  = new Bin("Double", new Double(0.5)); // works
        Bin testBin2  = new Bin("double", 0.5);  // works
        client.put(null, key, testBin1, testBin2);

        Record record1 = client.get(null, key);

        Object result1 = record1.getValue("Double");
        System.out.println("Result Double bin value is " + result1);

        Object result2 = record1.getValue("double");
        if (result2 != null) {
            System.out.println("Result double bin type should be java.lang.Double");
            System.out.println("Result double bin type  is " + result2.getClass()); // class java.lang.Long
            System.out.println("Result double bin value is " + result2);            // 4602678819172646912
        }

        //client.delete(null, key);
    }
}
