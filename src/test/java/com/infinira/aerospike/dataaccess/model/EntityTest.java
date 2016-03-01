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

package com.infinira.aerospike.dataaccess.model;

import com.aerospike.client.Value;
import com.infinira.aerospike.dataaccess.repository.AerospikeRepository;
import com.infinira.aerospike.dataaccess.repository.SetDelete;
import com.infinira.aerospike.dataaccess.util.Utils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Siddharth Garimella on 1/5/2016.
 */
public class EntityTest {
    public static AerospikeRepository<GeneralEntity> myEntityAerospikeRepository = new AerospikeRepository<GeneralEntity>(GeneralEntity.class);

    @BeforeClass
    public void setUp() throws Exception {
        SetDelete.clearSet("test","GeneralEntity");

    }
    // Test the creation of an entity with all supported data types and check.
    @Test
    public void testEntityClassPersist() throws Exception {
        System.out.println("user double type:" + Value.UseDoubleType);
        GeneralEntity myEntity = new GeneralEntity();
        myEntity.setVarKey("101");                       // String
        myEntity.setVarInteger(12);                      // Integer
        myEntity.setVarString("Test string");            // String
        myEntity.setVarDouble(3.142D);                   // Double number
        myEntity.setVarBoolean(true);                    // Boolean
        Timestamp now = Utils.now();
        myEntity.setVarTimestamp(now);           // Timestamp
        myEntity.setVarBigdecimal(new BigDecimal(43.45));// BigDecimal
        myEntity.setVarFloat(new Float(3.21F));          // Float
        myEntity.setVarLong(9393393L);                   // Long
        myEntity.setVarShort(new Short((short)93));      // Short
        ArrayList<String> al = new ArrayList<String>();  // ArrayList<?>
        al.add("FirstItem");
        al.add("SecondItem");
        myEntity.setVarList(al);
        HashMap<String, Object> hm = new HashMap<String, Object>(); // HashMap
        hm.put("TEST", "test");
        myEntity.setVarMap(hm);

        // Test getter methods
        assertThat(myEntity.getVarKey()).isEqualTo("101");                       // String
        assertThat(myEntity.getVarInteger()).isEqualTo(12);                      // Integer
        assertThat(myEntity.getVarString()).isEqualTo("Test string");            // String
        assertThat(myEntity.getVarDouble()).isEqualTo(3.142D);                   // Double number
        assertThat(myEntity.getVarBoolean()).isEqualTo(true);                    // Boolean
        assertThat(myEntity.getVarTimestamp()).isEqualTo(now);           // Timestamp
        assertThat(myEntity.getVarBigdecimal()).isEqualTo(new BigDecimal(43.45));// BigDecimal
        assertThat(myEntity.getVarFloat()).isEqualTo(new Float(3.21F));          // Float
        assertThat(myEntity.getVarLong()).isEqualTo(9393393L);                   // Long
        assertThat(myEntity.getVarShort()).isEqualTo(new Short((short)93));      // Short
        assertThat(myEntity.getVarList().size()).isEqualTo(2);
        assertThat(myEntity.getVarList().get(0)).isEqualTo("FirstItem");
        assertThat(myEntity.getVarMap().get("TEST")).isEqualTo("test");

        //myEntity.setVarByte((byte)0xd8);
        myEntityAerospikeRepository.save(myEntity);               // Save entity in test tablespace.
        GeneralEntity result = myEntityAerospikeRepository.findOne("101");
        System.out.println(result.toJSONString());
        System.out.println(myEntity.getTimeToLive());
        assertThat(result).isEqualsToByComparingFields(myEntity);
    }

    // Test big decimal with mathcontext.
    @Test
    public void testBigDecimalClass() throws Exception {
        // Case 1: BigDecimal with MathContext.
        GeneralEntity myEntity = new GeneralEntity();
        myEntity.setVarKey("102");                       // String
        // BigDecimal is use for currency calculations. It is important to set MathContext or Scale.
        // Otherwise, you will see really long number in the database wasting space.
        // For example BigDecimal value of 43.45, turn out to be 43.4500000000000028421709430404007434844970703125 when converted into string.
        // Case 1: with mathcontext of 11, it truncates the number to 2893939.9394 for the following.
        BigDecimal bgdeci = new BigDecimal(2893939.939367, new MathContext(11));
        myEntity.setVarBigdecimal(bgdeci);// BigDecimal
        myEntityAerospikeRepository.save(myEntity);               // Save entity in test tablespace.
        GeneralEntity result = myEntityAerospikeRepository.findOne("102");
        System.out.println(result.toJSONString());
        System.out.println(myEntity.getTimeToLive());
        //result.setVarBigdecimal(new BigDecimal(29.29));
        assertThat(result).isEqualsToByComparingFields(myEntity);
    }

    // Test incremental updates.
    @Test
    public void testUpdate() throws Exception {
        System.out.println("user double type:" + Value.UseDoubleType);
        GeneralEntity myEntity = new GeneralEntity();
        myEntity.setVarKey("101");                       // String
        myEntity.setVarInteger(12);                      // Integer
        myEntity.setVarString("Test string");            // String
        myEntity.setVarDouble(3.142D);                   // Double number
        myEntity.setVarBoolean(true);                    // Boolean
        myEntity.setVarTimestamp(Utils.now());           // Timestamp
        myEntity.setVarBigdecimal(new BigDecimal(43.45));// BigDecimal
        myEntity.setVarFloat(new Float(3.21F));          // Float
        myEntity.setVarLong(9393393L);                   // Long
        myEntity.setVarShort(new Short((short)93));      // Short
        ArrayList<String> al = new ArrayList<String>();  // ArrayList<?>
        al.add("FirstItem");
        al.add("SecondItem");
        myEntity.setVarList(al);
        HashMap<String, Object> hm = new HashMap<String, Object>(); // HashMap
        hm.put("TEST", "test");
        myEntity.setVarMap(hm);
        //myEntity.setVarByte((byte)0xd8);
        myEntityAerospikeRepository.save(myEntity);               // Save entity in test tablespace.


        GeneralEntity result = myEntityAerospikeRepository.findOne("101");
        // Update the result
        result.setVarInteger(13);                      // Integer
        result.setVarString("New Test string");        // String
        result.setVarDouble(6.231D);                   // Double number
        result.setVarBoolean(false);                   // Boolean
        Timestamp now = Utils.now();
        result.setVarTimestamp(now);           // Timestamp
        BigDecimal bg = new BigDecimal(99.99,new MathContext(5));
        result.setVarBigdecimal(bg);// BigDecimal
        result.setVarFloat(new Float(6.42F));          // Float
        // Keep the long same. If the value is same, it should not be updated.
        result.setVarLong(9393393L);                   // Long
        // Clear short value.
        result.setVarShort(null);                       // Short
        System.out.println(result.getValueChangeStatus());

        // Check if long is marked as a field for change.
        assertThat(result.getValueChangeStatus().contains(GeneralEntity.VAR_LONG)).isFalse();
        // Short should be marked for update.
        assertThat(result.getValueChangeStatus().contains(GeneralEntity.VAR_SHORT)).isTrue();
        myEntityAerospikeRepository.update(result);

        // Retrieve from database
        GeneralEntity result1 = myEntityAerospikeRepository.findOne("101");
        assertThat(result1.getVarInteger()).isEqualTo(13);
        assertThat(result1.getVarString()).isEqualTo("New Test string");
        assertThat(result1.getVarDouble()).isEqualTo(6.231D);
        assertThat(result1.getVarBoolean()).isEqualTo(false);
        assertThat(result1.getVarTimestamp().getTime()).isEqualTo(now.getTime());
        assertThat(result1.getVarBigdecimal()).isEqualTo(bg);
        assertThat(result1.getVarFloat()).isEqualTo(6.42F);
        assertThat(result1.getVarShort()).isNull();
        assertThat(result1.getVarLong()).isEqualTo(9393393L);
        System.out.println(result.toJSONString());
    }


    @Test
    public void testEntityClassWithMapConstructor() throws Exception {
        HashMap<String,Object> genEntityMap = new HashMap<String, Object>();
        genEntityMap.put(GeneralEntity.VAR_KEY,"104");
        genEntityMap.put(GeneralEntity.VAR_INTEGER,12);
        genEntityMap.put(GeneralEntity.VAR_STRING,"Test string");
        genEntityMap.put(GeneralEntity.VAR_DOUBLE,3.142D);
        Timestamp now = Utils.now();
        genEntityMap.put(GeneralEntity.VAR_TIMESTAMP,now);
        genEntityMap.put(GeneralEntity.VAR_BIGDECIMAL,new BigDecimal(43.45));
        genEntityMap.put(GeneralEntity.VAR_FLOAT,3.21F);
        genEntityMap.put(GeneralEntity.VAR_LONG,9393393L);
        genEntityMap.put(GeneralEntity.VAR_SHORT,93);
        genEntityMap.put(GeneralEntity.VAR_BOOLEAN,true);
        HashMap<String, Object> hm = new HashMap<String, Object>(); // HashMap
        hm.put("TEST", "test");
        genEntityMap.put(GeneralEntity.VAR_MAP,hm);
        ArrayList<String> al = new ArrayList<String>();  // ArrayList<?>
        al.add("FirstItem");
        al.add("SecondItem");
        genEntityMap.put(GeneralEntity.VAR_LIST,al);

        // Create an entity with a map structure
        GeneralEntity myEntity = new GeneralEntity(genEntityMap);

        // Test getter methods
        assertThat(myEntity.getVarKey()).isEqualTo("104");                       // String
        assertThat(myEntity.getVarInteger()).isEqualTo(12);                      // Integer
        assertThat(myEntity.getVarString()).isEqualTo("Test string");            // String
        assertThat(myEntity.getVarDouble()).isEqualTo(3.142D);                   // Double number
        assertThat(myEntity.getVarBoolean()).isEqualTo(true);                    // Boolean
        assertThat(myEntity.getVarTimestamp()).isEqualTo(now);           // Timestamp
        assertThat(myEntity.getVarBigdecimal()).isEqualTo(new BigDecimal(43.45));// BigDecimal
        assertThat(myEntity.getVarFloat()).isEqualTo(new Float(3.21F));          // Float
        assertThat(myEntity.getVarLong()).isEqualTo(9393393L);                   // Long
        assertThat(myEntity.getVarShort()).isEqualTo(new Short((short)93));      // Short
        assertThat(myEntity.getVarList().size()).isEqualTo(2);
        assertThat(myEntity.getVarList().get(0)).isEqualTo("FirstItem");
        assertThat(myEntity.getVarMap().get("TEST")).isEqualTo("test");

        //myEntity.setVarByte((byte)0xd8);
        myEntityAerospikeRepository.save(myEntity);               // Save entity in test tablespace.
        GeneralEntity result = myEntityAerospikeRepository.findOne("104");
        System.out.println(result.toJSONString());
        System.out.println(myEntity.getTimeToLive());
        assertThat(result).isEqualsToByComparingFields(myEntity);
    }

    @Test
    public void testEntityClassWithMapObjectsConstructor() throws Exception {
        // Test passing Integer, Float, Double, Short, Boolean as objects.
        HashMap<String,Object> genEntityMap = new HashMap<String, Object>();
        genEntityMap.put(GeneralEntity.VAR_KEY,new String("105"));
        genEntityMap.put(GeneralEntity.VAR_INTEGER,new Integer(12));
        genEntityMap.put(GeneralEntity.VAR_STRING,new String("Test string"));
        genEntityMap.put(GeneralEntity.VAR_DOUBLE,new Double(3.142D));
        Timestamp now = Utils.now();
        genEntityMap.put(GeneralEntity.VAR_TIMESTAMP,now);
        genEntityMap.put(GeneralEntity.VAR_BIGDECIMAL,new BigDecimal(43.45));
        genEntityMap.put(GeneralEntity.VAR_FLOAT,new Float(3.21F));
        genEntityMap.put(GeneralEntity.VAR_LONG,new Long(9393393L));
        genEntityMap.put(GeneralEntity.VAR_SHORT,new Short((short)93));
        genEntityMap.put(GeneralEntity.VAR_BOOLEAN,new Boolean(true));
        HashMap<String, Object> hm = new HashMap<String, Object>(); // HashMap
        hm.put("TEST", "test");
        genEntityMap.put(GeneralEntity.VAR_MAP,hm);
        ArrayList<String> al = new ArrayList<String>();  // ArrayList<?>
        al.add("FirstItem");
        al.add("SecondItem");
        genEntityMap.put(GeneralEntity.VAR_LIST,al);

        // Create an entity with a map structure
        GeneralEntity myEntity = new GeneralEntity(genEntityMap);

        // Test getter methods
        assertThat(myEntity.getVarKey()).isEqualTo("105");                       // String
        assertThat(myEntity.getVarInteger()).isEqualTo(12);                      // Integer
        assertThat(myEntity.getVarString()).isEqualTo("Test string");            // String
        assertThat(myEntity.getVarDouble()).isEqualTo(3.142D);                   // Double number
        assertThat(myEntity.getVarBoolean()).isEqualTo(true);                    // Boolean
        assertThat(myEntity.getVarTimestamp()).isEqualTo(now);           // Timestamp
        assertThat(myEntity.getVarBigdecimal()).isEqualTo(new BigDecimal(43.45));// BigDecimal
        assertThat(myEntity.getVarFloat()).isEqualTo(new Float(3.21F));          // Float
        assertThat(myEntity.getVarLong()).isEqualTo(9393393L);                   // Long
        assertThat(myEntity.getVarShort()).isEqualTo(new Short((short)93));      // Short
        assertThat(myEntity.getVarList().size()).isEqualTo(2);
        assertThat(myEntity.getVarList().get(0)).isEqualTo("FirstItem");
        assertThat(myEntity.getVarMap().get("TEST")).isEqualTo("test");

        //myEntity.setVarByte((byte)0xd8);
        myEntityAerospikeRepository.save(myEntity);               // Save entity in test tablespace.
        GeneralEntity result = myEntityAerospikeRepository.findOne("105");
        System.out.println(result.toJSONString());
        System.out.println(myEntity.getTimeToLive());
        assertThat(result).isEqualsToByComparingFields(myEntity);
    }


}