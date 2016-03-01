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

import com.aerospike.client.Host;

/**
 * Created by Siddharth Garimella on 2/23/2016.
 */

/**
 * com.aerospike.client.Host class does not have a default constructor. JSON deserailzation is not possible without default constructor.
 * Use this class to capture hostname and port number. Internally, it converts into com.aerospike.client.Host for creating a client.
 */
public class AerospikeHost {
    private String name;
    private int port=-1;

    public AerospikeHost() {
    }

    public AerospikeHost(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Host getHost()
    {
        if(name!=null && port != -1)
            return new Host(name,port);
        else return null;
    }

}
