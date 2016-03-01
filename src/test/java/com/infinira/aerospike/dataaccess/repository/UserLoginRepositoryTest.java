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

import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.infinira.aerospike.dataaccess.model.UserLogin;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.testng.Assert.*;


public class UserLoginRepositoryTest {
    String accountNumberPrefix =  "8383-3939-39393";
    AerospikeRepository<UserLogin> userLoginAerospikeRepository;
    ArrayList<UserLogin> userLoginArrayList = new ArrayList<UserLogin>();
    String setName = "UserLogin";
    int noActiveUsers = 0;
    UserLogin userLogin;
    int userIdSeq = 1000;
    int passcode = 2828;
    int password = 3737272;

    public String getNewUserId()
    {
        return "User_" + userIdSeq++;
    }

    public String getPasscode()
    {
        return String.valueOf((int)(Math.random()*passcode++));
    }

    public String getPassword()
    {
        return String.valueOf((int)(Math.random()*password++));
    }

    public boolean getRandomBool()
    {
        return Math.random() > 0.5;
    }

    public UserLoginRepositoryTest() throws InstantiationException, IllegalAccessException {
        userLoginAerospikeRepository = RepositoryFactory.getUserLoginRepository();
    }

    public UserLogin createUserLogin(String accountNumber) throws UnknownHostException {
        UserLogin userLogin = new UserLogin();
        userLogin.setAccountNumber(accountNumber);
        userLogin.setUserId(getNewUserId());
        userLogin.setCurrentPassword(getPassword());
        userLogin.setCurrentPasscode(getPasscode());
        userLogin.setPasswordHint("Random generated password hint!");
        userLogin.setSystem(getRandomBool());
        userLogin.setEnabled(getRandomBool());
        userLogin.setHasLoggedOut(getRandomBool());
        userLogin.setRequirePasswordChange(getRandomBool());
        userLogin.setLastTimezone("PST");
        userLogin.setDisabledDatetime(new Timestamp(82828282));
        userLogin.setSuccessiveFailedLogins(1);
        userLogin.setModifiedOn(new Timestamp(93939331));
        userLogin.setCreatedOn(new Timestamp(93939331));
        if (userLogin.isEnabled()) noActiveUsers++;
        return userLogin;
    }

    @BeforeClass
    public void setUp() throws Exception {
        userLoginAerospikeRepository.dropIndex("UserLoginEnabled");
        SetDelete.clearSet("test",setName);
        // Create 100 objects
        for (int i = 0; i < 100 ; i++) {
            userLoginArrayList.add(createUserLogin(accountNumberPrefix + "-"+i));
        }
        userLogin = createUserLogin(accountNumberPrefix);
        //Insert the obects in the database
        userLoginAerospikeRepository.insert(userLogin);
        userLoginAerospikeRepository.insertAll(userLoginArrayList);
        userLoginAerospikeRepository.createIndex("UserLoginEnabled","enabled",IndexType.STRING);
    }

    @AfterClass
    public void tearDown() throws Exception {
        userLogin= null;
        userLoginArrayList = null;
        //SetDelete.clearSet("test",setName);
    }

    @Test (priority=1)
    public void testIndex() throws Exception {
        userLoginAerospikeRepository.createIndex("ActNoIndx",UserLogin.ACCOUNT_NUMBER, IndexType.STRING);
        //userLoginRepository.dropIndex("UserIdIndex");
    }

    @Test (priority=1)
    public void testUpdate() throws Exception {
        userLogin.setCurrentPassword("NEW PASSWORD");
        userLogin.setCurrentPasscode("NEW 1234");
        userLoginAerospikeRepository.update(userLogin);
        UserLogin ul = userLoginAerospikeRepository.findOne((String) userLogin.getKey());
        Assert.assertEquals(ul.getCurrentPassword(),"NEW PASSWORD");
        Assert.assertEquals(ul.getCurrentPasscode(),"NEW 1234");
    }

    @Test (priority=1)
    public void testSave() throws Exception {
        // Create a new field which is  not defined and save it.
        userLogin.setValue("NewBin","NewBin_Value");
        userLoginAerospikeRepository.save(userLogin); // Save this new bin value.
        UserLogin ul = userLoginAerospikeRepository.findOne((String) userLogin.getKey());
        Assert.assertEquals(ul.getValue("NewBin"),"NewBin_Value");
    }


    @Test (priority=2)
    public void testFindOne() throws Exception {
        UserLogin ul = userLoginAerospikeRepository.findOne(userLogin.getAccountNumber());
       // TestUtil.PropertyValuesAreEquals(userIdSeq,ul);
    }

    @Test (priority=2)
    public void testExists() throws Exception {
        assertTrue(userLoginAerospikeRepository.exists(userLogin.getAccountNumber()));
    }

    @Test (priority=2)
    public void testFindAll() throws Exception {
        ArrayList<UserLogin> userLoginList = userLoginAerospikeRepository.findAll();
        assertEquals(userLoginList.size(),101);
    }

    @Test (priority=2)
    public void testFindAll1() throws Exception {
        ArrayList<UserLogin> userLoginList = userLoginAerospikeRepository.findAll();
        assertEquals(userLoginList.size(),101);
    }

    @Test (priority=2)
    public void testFindAll2() throws Exception {
        String[] ids = new String[25];
        for (int i = 0; i < 25; i++) {
            ids[i] =  userLoginArrayList.get(i+10).getAccountNumber();
        }
        ArrayList<UserLogin> result = userLoginAerospikeRepository.findAll(ids);
        for (UserLogin ul:result) {
            System.out.println(ul.toJSONString());
        }
        Assert.assertEquals(result.size(),25);
    }

    @Test (priority=2)
    public void testFindAllUsingQuery() throws Exception {
        //userLoginRepository.createIndex("UserLoginEnabled","enabled",IndexType.STRING);
        Filter filter = Filter.equal("enabled","1");
        ArrayList<UserLogin> result = userLoginAerospikeRepository.findAll(filter);
        Assert.assertEquals(result.size(),noActiveUsers);
    }

    @Test (priority=1)
    public void testCount() throws Exception {
        long count = userLoginAerospikeRepository.count();
        Assert.assertEquals(count, userLoginArrayList.size()+1);
    }


    @Test (priority=4)
    public void testDelete() throws Exception {
        userLoginAerospikeRepository.delete(userLogin.getAccountNumber());
        // Try to get the UserLogin
        UserLogin tempEvent = userLoginAerospikeRepository.findOne(userLogin.getAccountNumber());
        assertNull(tempEvent);
    }

    @Test (priority=4)
    public void testDelete1() throws Exception {
        String[] eventIds = new String[5];
        for (int i = 0; i <eventIds.length ; i++) {
            eventIds[i] = userLoginArrayList.get(i).getAccountNumber();
        }
        userLoginAerospikeRepository.delete(eventIds);
        // Try to get the event
        for (int i = 0; i <eventIds.length ; i++) {
            UserLogin tempEvent = userLoginAerospikeRepository.findOne(userLoginArrayList.get(i).getAccountNumber());
            assertNull(tempEvent);
        }
    }

    @Test (priority=5)
    public void testDeleteAll() throws Exception {
        userLoginAerospikeRepository.deleteAll();
        // Try to get the userLogin records
        for (UserLogin ul1: userLoginArrayList) {
            UserLogin ul = userLoginAerospikeRepository.findOne(ul1.getAccountNumber());
            assertNull(ul);
        }
    }
}