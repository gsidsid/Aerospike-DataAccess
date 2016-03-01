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

import com.infinira.aerospike.dataaccess.model.UserProfile;
import com.infinira.aerospike.dataaccess.util.Utils;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by babu on 12/16/2015.
 */
public class UserProfileRepositoryTest {

    public UserProfile createUserInfo(String accountNumber)
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setAccountNumber(accountNumber);
        userProfile.setUserId("Siddharth");
        userProfile.setFirstName("Siddharth");
        userProfile.setMiddleName(null);
        userProfile.setLastName("Garimella");
        userProfile.setPersonalTitle("Mr.");
        userProfile.setSuffix(null);
        userProfile.setNickname("Siddu");
        userProfile.setFirstNameLocal(null);
        userProfile.setMiddleNameLocal(null);
        userProfile.setLastNameLocal(null);
        userProfile.setOtherLocal(null);
        userProfile.setGender("MALE");
        userProfile.setBirthDate(new Timestamp(8383838l));
        userProfile.setMothersMaidenName("MaidenName");
        userProfile.setMaritalStatus("UNMARRIED");
        userProfile.setSocial_SecurityNumber("123-45-6789");
        userProfile.setEmail("gsidsid@gmail.com");
        userProfile.setProfession("Student");
        userProfile.setTitle("Junior Student");
        userProfile.setEmploymentStatus("NOT_EMPLOYED");
        userProfile.setResidenceStatus("RENTING");
        userProfile.setLanguageId("EN");
        userProfile.setCurrencyId("USD");
        userProfile.setCurrentPhoto("http://tinyurl/photo");
        userProfile.setPrevPhoto("http://tinyurl/photo_prev");
        userProfile.setPhotoUpdateOn(new Timestamp(8123324l));
        userProfile.setMphoneCountryCode(1);
        userProfile.setMphoneAreaCode(408);
        userProfile.setMphoneNumber("123-4567");
        userProfile.setAddressLine1("ABC ROAD");
        userProfile.setAddressLine2(null);
        userProfile.setCity("GREAT HILLS");
        userProfile.setState("CA");
        userProfile.setCountryCode("USA");
        userProfile.setPostalCode("94024");
        userProfile.setCreatedOn(Utils.now());
        userProfile.setModifiedOn(Utils.now());
        userProfile.setModifiedBy("Siddu");
        HashMap<String,Object> userAttrs = new HashMap<String,Object>();
        userAttrs.put("HEIGHT",5.10);
        userProfile.setUserAttrs(userAttrs);
        userProfile.setUserAttr("WIGHT",120.00);
        return userProfile;
    }

    @Test
    public void testUserInfo() throws Exception {
        SetDelete.clearSet("test", UserProfile.class.getSimpleName());
        UserProfile userProfile =  createUserInfo("9393-30393");
        userProfile.setUserAttr("replenish_limit",50);
        userProfile.setUserAttr("notifications",true);
        userProfile.setUserAttr("security_level","high");
        RepositoryFactory.getUserProfileRepository().insert(userProfile);
        UserProfile ui =RepositoryFactory.getUserProfileRepository().findOne(userProfile.getAccountNumber());
        assertThat(userProfile).isLenientEqualsToByIgnoringNullFields(ui);
        System.out.println(userProfile.toJSONString());
        userProfile = createUserInfo("9393-30393");
        HashMap<String,Object> userMap = new HashMap<String,Object>();
        userMap.put(UserProfile.ACCOUNT_NUMBER,"92393-393939");
        userMap.put(UserProfile.USER_ID,"Siddu");
        userMap.put(UserProfile.COUNTRY_CODE,"India");
        UserProfile userProfile2 = new UserProfile(userMap,0,0);
        System.out.println(userProfile2.toString());
    }
}