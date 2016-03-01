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
import com.aerospike.helper.query.Qualifier;
import com.infinira.aerospike.dataaccess.model.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by babu on 2/19/2016.
 */
public interface Repository<T extends Entity> {

    void clearSet() throws Exception;

    // Create index
    void createIndex(String indexName, String binName, IndexType indexType);

    // Drop index
    void dropIndex(String indexName);

    // Insert a new record
    void insert(T entity);

    // Save a record
    void save(T entity);

    // Update a record
    void update(T entity);

    //InsertAll events in the list
    void insertAll(Collection<T> objectsToSave);

    // Find a field value
    Object get(String keyValue, String fieldName);

    // Find an event for a given keyValue
    Map<String, Object> get(String keyValue, String... fieldNames);

    // Find an event for a given keyValue
    T findOne(String keyValue) throws InstantiationException, IllegalAccessException;

    // Check if record exists
    boolean exists(String keyValue);

    ArrayList<T> findAll();

    ArrayList<T> findAll(Filter filter, Qualifier... qualifiers);

    // Find all event for a given array of event ids.
    ArrayList<T> findAll(String[] keyValues) throws InstantiationException, IllegalAccessException;

    // Get count of all CPEvents in the database. Taken from example code.
    long count();

    // Prepend value to an existing field's value.
    T prepend(T object, String fieldName, String value) throws IllegalAccessException, InstantiationException;

    // Append value to an existing field's value.
    T append(T object, String fieldName, String value) throws IllegalAccessException, InstantiationException;

    // Add integer value to an existing field's value.
    T add(T object, String fieldName, int value) throws IllegalAccessException, InstantiationException;

    // Prepends values to all given fields
    T prepend(T object, Map<String, Object> values) throws IllegalAccessException, InstantiationException;

    // Appends values to all given fields.
    T append(T object, Map<String, Object> values) throws IllegalAccessException, InstantiationException;

    // Add integer bin values to existing record bin values. The policy specifies the transaction timeout,
    // record expiration and how the transaction is handled when the record already exists.
    // This call only works for integer values. Object in the map must be Integer object.
    T add(T object, Map<String, Object> values) throws IllegalAccessException, InstantiationException;

    void delete(String keyValue);

    void delete(String[] keyValues);

    // Delete all records.
    long deleteAll() throws Exception;
}
