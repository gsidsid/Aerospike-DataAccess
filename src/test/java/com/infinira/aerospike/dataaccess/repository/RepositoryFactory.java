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


import com.infinira.aerospike.dataaccess.model.UserLogin;
import com.infinira.aerospike.dataaccess.model.UserProfile;

/**
 * Created by Siddharth Garimella on 12/16/2015.
 */
public final class RepositoryFactory {

    // Aerospike repositories
    private static AerospikeRepository<UserLogin> userLoginRepository;
    private static AerospikeRepository<UserProfile> userProfileRepository;

    public RepositoryFactory() {
    }

    public static AerospikeRepository<UserLogin> getUserLoginRepository() throws InstantiationException, IllegalAccessException {
        if (userLoginRepository == null)
            userLoginRepository = new AerospikeRepository<UserLogin>(UserLogin.class);
        return userLoginRepository;
    }

    public static AerospikeRepository<UserProfile> getUserProfileRepository() throws InstantiationException, IllegalAccessException {
        if (userProfileRepository == null)
            userProfileRepository = new AerospikeRepository<UserProfile>(UserProfile.class);
        return userProfileRepository;
    }
}
