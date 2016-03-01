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
import com.aerospike.client.policy.ClientPolicy;

/**
 * Configuration data.
 */

public class AerospikeConfig {
	private String defaultNameSpace="test";
	private AerospikeHost[] aerospikeHosts;
	private ClientPolicy clientPolicy;

	public AerospikeConfig() {
	}

	public AerospikeConfig(String defaultNameSpace, AerospikeHost[] aerospikeHosts, ClientPolicy clientPolicy) {
		this.defaultNameSpace = defaultNameSpace;
		this.aerospikeHosts = aerospikeHosts;
		this.clientPolicy = clientPolicy;
	}

	public String getDefaultNameSpace() {
		return defaultNameSpace;
	}

	public void setDefaultNameSpace(String defaultNameSpace) {
		this.defaultNameSpace = defaultNameSpace;
	}

	public ClientPolicy getClientPolicy() {
		return clientPolicy;
	}

	public void setClientPolicy(ClientPolicy clientPolicy) {
		this.clientPolicy = clientPolicy;
	}

	public AerospikeHost[] getAerospikeHosts() {
		 return aerospikeHosts;
	}

	public void setAerospikeHosts(AerospikeHost[] aerospikeHosts) {
		this.aerospikeHosts = aerospikeHosts;
	}

	public Host[] getHosts() {
		if(aerospikeHosts != null) {
			Host[] hosts = new Host[aerospikeHosts.length];
			for (int i = 0; i < aerospikeHosts.length; i++) {
				hosts[i] = aerospikeHosts[i].getHost();
			}
			return hosts;
		}
		else return null;
	}

}
