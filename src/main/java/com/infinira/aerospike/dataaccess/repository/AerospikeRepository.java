
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

/* Some of the implementation of this class is taken from Aerospike Springdata implementation.
   If you are looking for pure Springdata implementation, please see:
   https://github.com/spring-projects/spring-data-aerospike/tree/master/src/main/java/org/springframework/data/aerospike
 */

package com.infinira.aerospike.dataaccess.repository;

import com.aerospike.client.*;
import com.aerospike.client.cluster.Node;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.KeyRecord;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;
import com.aerospike.helper.query.KeyRecordIterator;
import com.aerospike.helper.query.Qualifier;
import com.infinira.aerospike.dataaccess.model.Entity;
import com.infinira.aerospike.dataaccess.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

// Generic repository implementation
public class AerospikeRepository<T extends Entity> implements Repository<T> {
    protected final static Logger logger = LoggerFactory.getLogger(Utils.class);
    //Aerospike client object
    private final static AerospikeClient aerospikeClient = AerospikeClientUtil.getClient();
    //Default name space from configuration file.
    private final static String DEFAULT_NAMESPACE = AerospikeClientUtil.getDefaultNamespace();

    // Domain class
    private final Class<T> domainType;
    // Name space
    private final String namespace;
    // Set name (User class name as setname)
    private final String setName;


    /**
     * Default constructor.
     * @param domainType
     */
    public AerospikeRepository(Class<T> domainType) {
       this(DEFAULT_NAMESPACE,domainType);
    }

    /**
     * Constructor
     * @param namespace Name space
     * @param domainType Domain type (T.class)
     */
    public AerospikeRepository(String namespace, Class<T> domainType) {
       this(namespace,domainType,null);
    }

    /**
     * Constructor
     * @param namespace name space for this object
     * @param domainType class domain type
     * @param setName set name. Default is simple class name.
     */
    public AerospikeRepository(String namespace, Class<T> domainType, String setName) {
        Assert.notNull(domainType, "Domain type cannot be null");
        this.domainType = domainType;
        // If name space is null or empty, use default name space.
        if(namespace == null || namespace.length() == 0)
            this.namespace = AerospikeClientUtil.getDefaultNamespace();
        else this.namespace = namespace;
        if(setName != null && setName.length()>0)
            this.setName = setName;
        else
            // Set setName as Class simplename.
            this.setName = domainType.getSimpleName();
    }

    /**
     * Remove all records associated with his set.
     * @throws Exception
     */
    @Override
    public void clearSet() throws Exception {
        SetDelete.clearSet(namespace,setName);
    }

    /**
     * Create a secondary index on a given field (binName).
     * @param indexName Index name
     * @param binName bin or field name
     * @param indexType Index type
     */
    @Override
    public void createIndex(String indexName, String binName, IndexType indexType) {
        Assert.notNull(indexName);
        Assert.notNull(binName);
        Assert.notNull(indexType);
        IndexTask task = aerospikeClient.createIndex(null, namespace, setName, indexName, binName, indexType);
        task.waitTillComplete();
    }

    /**
     * Drop index
     * @param indexName  index name.
     */
    @Override
    public void dropIndex(String indexName) {
        aerospikeClient.dropIndex(null, namespace, setName, indexName);
    }

    //Insert/Save/Update a new record in the Aerospike db.
    private void change(String opType, T object) {
        //Entity entity = object;
        // Use reflection to get key, bins and valueChangeStatus
        String keyName =  Utils.get(object, "keyName");
        HashMap<String,Object> bins =  Utils.get(object, "bins");
        HashSet<String> changedFields =  Utils.get(object, "valueChangeStatus");
        int generation =   Utils.get(object, "generation");
        // Get the keyvalue
        String keyValue = (String)bins.get(keyName);
        // Create a Key. Key maps to unique event id
        Key key = new Key(namespace, setName, keyValue);
        // Find fields to save.
        Set<String> fieldNames;
        if(opType.equals("UPDATE") && generation > 0)
            fieldNames = changedFields;             //It is enough to update only changed fields, not all.
        else
            fieldNames = bins.keySet();             //For insert and update

        // Create Bin array
        Bin[] binArray = new Bin[fieldNames.size()];
        //Only store columns that are not null.
        int i = 0;
        for (Object fieldName : fieldNames) {
            if (fieldName != null) {
                Object obj = object.getValue((String) fieldName);
                // Special consideration for double to store it as double instead of serialized object.
                if(obj instanceof  Double) {
                    //System.out.println("Debug for double:" + obj);
                    double dbl = ((Double) obj).doubleValue();
                    binArray[i++] = new Bin((String) fieldName, dbl);//object.getValue((String) fieldName));
                } else
                    binArray[i++] = new Bin((String) fieldName, object.getValue((String) fieldName));
            }
        }
        // Check the op type and follow
        if (opType.equals("INSERT"))        // CREATE_ONLY :  Create only if it does not exists
            aerospikeClient.put(AerospikeClientUtil.getInsertPolicy(), key, binArray);
        else if (opType.equals("SAVE"))     // UPDATE      :  Create or update record
            aerospikeClient.put(null, key, binArray);
        else if (opType.equals("UPDATE"))   // UPDATE_ONLY :  Update record only.
            aerospikeClient.put(AerospikeClientUtil.getUpdatePolicy(), key, binArray);
    }

    // Insert a new record
    @Override
    public void insert(T entity) {
        change("INSERT", entity);
    }

    // Save a record
    @Override
    public void save(T entity) {
        change("SAVE", entity);
    }

    // Update a record
    @Override
    public void update(T entity) {
        change("UPDATE", entity);
    }

    //InsertAll events in the list
    @Override
    public void insertAll(Collection<T> objectsToSave) {
        for (T element : objectsToSave) {
            if (element == null) {
                continue;
            }
            save(element);
        }
    }

    /**
     * Record to Entity conversion method
     * @param record Record from database
     * @return Return entity
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private T record2Entity(Record record) throws IllegalAccessException, InstantiationException {
        if (record != null) {
            // Create an object instance
            T mObject = domainType.newInstance();
            //String keyName = Util.get(mObject, "keyName"); Util.set(mObject,keyName,keyValue); --> Keyname is already in the bins.
            // Set bins, generation, expiration and isFromDB values.
            Utils.set(mObject, "bins", record.bins);
            Utils.set(mObject, "generation", record.generation);
            Utils.set(mObject, "expiration", record.expiration);
            Utils.set(mObject, "isFromDb", !(record.generation == 0 && record.expiration == 0));
            Utils.set(record, "bins", null);// clear bins reference for letting record garbage collection
            return mObject;
        }
        return null;
    }

    /**
     * Gets a given field value
     * @param keyValue Primary key value.
     * @param fieldName Field name
     * @return
     */
    @Override
    public Object get(String keyValue, String fieldName) {
        if (keyValue == null)
            return null;
        // Create a Key. Key maps to unique event id
        Key key = new Key(namespace, setName, keyValue);
        Record record = aerospikeClient.get(null, key, fieldName);
        if (record != null)
            return record.getValue(fieldName);
        return null;
    }


    /**
     * Gets a list of fields
     * @param keyValue Primary Keyvalue
     * @param fieldNames list of field names to retrieve.
     * @return a Map of field names and field values.
     */
    @Override
    public Map<String, Object> get(String keyValue, String... fieldNames) {
        if (keyValue == null)
            return null;
        // Create a Key. Key maps to unique event id
        Key key = new Key(namespace, setName, keyValue);
        Record record = aerospikeClient.get(null, key, fieldNames);
        if(record != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            for (String fieldName : fieldNames) {
                result.put(fieldName, record.getValue(fieldName));
            }
            return result;
        } else return null;
    }

    /**
     * Finds an entity for a given key value
     * @param keyValue  key value
     * @return entity if exists else returns null
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public T findOne(String keyValue) throws InstantiationException, IllegalAccessException {
        if (keyValue == null)
            return null;
        // Create a Key. Key maps to unique event id
        Key key = new Key(namespace, setName, keyValue);
        Record record = aerospikeClient.get(null, key);
        Entity entity = record2Entity(record);
        //noinspection unchecked
        return (T) entity;
    }

    /**
     * Checks if an entity exists corresponding to a given key value
     * @param keyValue primary key value.
     * @return
     */
    @Override
    public boolean exists(String keyValue) {
        if (keyValue == null)
            return false;
        // Create a Key. Key maps to unique event id
        Key key = new Key(namespace, setName, keyValue);
        return aerospikeClient.exists(null, key);
    }

    /**
     * Find all entities in the database
     * @return ArrayList of entity objects
     */
    @Override
    public ArrayList<T> findAll() {
        final ArrayList<T> scanList = new ArrayList<T>();
        Iterable<T> results = findAllUsingQuery(null);
        Iterator<T> iterator = results.iterator();
        try {
            while (iterator.hasNext()) {
                scanList.add(iterator.next());
                //System.out.println(iterator.next());
            }
        } finally {
            ((EntityIterator<T>) iterator).close();
        }
        return scanList;
    }

    /**
     * Find all entities that match a given filter and qualifiers.
     * @param filter Filter on fields
     * @param qualifiers Qualifiers
     * @return ArrayList of entities
     */
    @Override
    public ArrayList<T> findAll(Filter filter, Qualifier... qualifiers) {
        final ArrayList<T> scanList = new ArrayList<T>();
        Iterable<T> results = findAllUsingQuery(filter, qualifiers);
        Iterator<T> iterator = results.iterator();
        try {
            while (iterator.hasNext()) {
                scanList.add(iterator.next());
                //System.out.println(iterator.next());
            }
        } finally {
            ((EntityIterator<T>) iterator).close();
        }
        return scanList;
    }

    /**
     * Find all entities for a given set of keyValues
     * @param keyValues List of key value strings
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public ArrayList<T> findAll(String[] keyValues) throws InstantiationException, IllegalAccessException {
        // If empty list
        if (keyValues.length == 0)
            return null;
        Key[] keys = new Key[keyValues.length];
        // create an array of keys
        for (int i = 0; i < keyValues.length; i++) {
            if (keyValues[i] != null && keyValues[i].trim().length() != 0) {
                keys[i] = new Key(namespace, setName, keyValues[i]);
            }
        }
        // Return list.
        ArrayList<T> entityList = new ArrayList<T>();
        // Use batch operation to get all corresponding records
        Record[] records = aerospikeClient.get(AerospikeClientUtil.getBatchPolicy(), keys);
        for (int i = 0; i < records.length; i++) {
            if (records[i] != null) {
                Key key = keys[i];
                entityList.add(record2Entity(records[i]));
            }

        }
        return entityList;
    }

    // Find all using query

    /**
     * Find all entities for a given filter and qualifiers
     * @param filter input filter
     * @param qualifiers qualifiers
     * @return Iterable list of entities.
     */
    private Iterable<T> findAllUsingQuery(Filter filter, Qualifier... qualifiers) {
        Statement stmt = new Statement();
        stmt.setNamespace(namespace);
        stmt.setSetName(setName);
        Iterable<T> results;
        final KeyRecordIterator recIterator = AerospikeClientUtil.getQueryEngine().select(namespace, setName, filter, qualifiers);
        results = new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new EntityIterator<T>(recIterator);
            }
        };
        return results;
    }

    /**
     * Entity iterator
     * @param <T>
     */
    public class EntityIterator<T> implements CloseableIterator<T> {
        private final KeyRecordIterator keyRecordIterator;

        public EntityIterator(KeyRecordIterator keyRecordIterator) {
            this.keyRecordIterator = keyRecordIterator;
        }

        @Override
        public boolean hasNext() {
            return this.keyRecordIterator.hasNext();
        }

        @Override
        public T next() {
            KeyRecord keyRecord = this.keyRecordIterator.next();
            try {
                //noinspection unchecked
                return (T) record2Entity(keyRecord.record);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void close() {
            try {
                keyRecordIterator.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void remove() {
        }
    }

    /**
     * Gets the number of records in the set
     * @return the number of records.
     */
    @Override
    public long count() {
        Node[] nodes = aerospikeClient.getNodes();
        int replicationCount = 2;
        int nodeCount = nodes.length;
        int noObjects = 0;
        for (Node node : nodes) {
            String infoString = Info.request(node, "sets/" + namespace + "/" + setName);
            String noObjectsStr = infoString.substring(infoString.indexOf("=") + 1, infoString.indexOf(":"));
            noObjects = Integer.parseInt(noObjectsStr);
        }
        // System.out.println(String.format("Total Master and Replica objects %d", noObjects));
        // System.out.println(String.format("Total Master objects %d", (nodeCount > 1) ? noObjects/replicationCount : noObjects));
        return (nodeCount > 1) ? noObjects / replicationCount : noObjects;
    }

    // operate on a bin value.
    // opType: APPEND, PREPEND, ADD
    private T operate(String opType, T object, String fieldName, Object value) throws InstantiationException, IllegalAccessException {
        Assert.notNull(object, "Object to prepend to must not be null!");
        T result;
        try {
            Record record = null;
            String keyName = Utils.get(object,"keyName");
            HashMap<String,Object> bins = Utils.get(object,"bins");
            String keyValue = (String)bins.get(keyName);
            Key key = new Key(namespace, setName, keyValue);
            if (opType.equals("PREPEND"))
                record = aerospikeClient.operate(null, key, Operation.prepend(new Bin(fieldName, value)), Operation.get(fieldName));
            else if (opType.equals("APPEND"))
                record = aerospikeClient.operate(null, key, Operation.append(new Bin(fieldName, value)), Operation.get(fieldName));
            else if (opType.equals("ADD"))
                record = aerospikeClient.operate(null, key, Operation.add(new Bin(fieldName, value)), Operation.get(fieldName));
            result = record2Entity(record);
        } catch (AerospikeException ae) {
            logger.error(ae.getMessage());
            throw ae;
        }
        return result;
    }

    // opType: APPEND, PREPEND, ADD
    private T operate(String opType, T object, Map<String, Object> values) throws InstantiationException, IllegalAccessException {
        Assert.notNull(object, "Object to prepend to must not be null!");
        T result = null;
        try {
            Operation[] operations = new Operation[values.size() + 1];
            int i = 0;
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                Bin newBin = new Bin(entry.getKey(), entry.getValue());
                if (opType.equals("PREPEND"))
                    operations[i] = Operation.prepend(newBin);
                else if (opType.equals("APPEND"))
                    operations[i] = Operation.append(newBin);
                else if (opType.equals("ADD"))
                    operations[i] = Operation.add(newBin);
                i++;
            }
            // Last operation is to get the record to return.
            operations[i] = Operation.get();
            String keyName = Utils.get(object,"keyName");
            HashMap<String,Object> bins = Utils.get(object,"bins");
            String keyValue = (String)bins.get(keyName);
            Key key = new Key(namespace, setName, keyValue);
            Record record = aerospikeClient.operate(null, key, operations);
            result = record2Entity(record);
        } catch (AerospikeException ae) {
            logger.error(ae.getMessage());
            throw ae;
        }
        return result;
    }

    /**
     * Prepend value to an existing field's value.
     * @param object Input object
     * @param fieldName Field name
     * @param value Value to be prepended.
     * @return Updated object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public T prepend(T object, String fieldName, String value) throws IllegalAccessException, InstantiationException {
        return operate("PREPEND", object, fieldName, value);
    }

    // Append value to an existing field's value.

    /**
     * Append value to an existing field's value.
     * @param object Input object
     * @param fieldName Field name
     * @param value Value to be prepended.
     * @return Updated object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public T append(T object, String fieldName, String value) throws IllegalAccessException, InstantiationException {
        return operate("APPEND", object, fieldName, value);
    }

    /**
     * Add integer value to an existing field's value.
     * @param object Input object
     * @param fieldName Field name
     * @param value Value to be prepended.
     * @return Updated object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public T add(T object, String fieldName, int value) throws IllegalAccessException, InstantiationException {
        return operate("ADD", object, fieldName, value);
    }

    // Add integer value to an existing field's value.
    // Todo: Need to implement, add based on primary key.
    /*
    public Integer add(String keyName, String keyValue, String fieldName, int value) throws IllegalAccessException, InstantiationException {
        Bin newBin = new Bin(fieldName, value);
        // Last operation is to get the record to return.
        operations[i] = Operation.get();
        String keyName = Utils.get(object,"keyName");
        HashMap<String,Object> bins = Utils.get(object,"bins");
        String keyValue = (String)bins.get(keyName);
        Key key = new Key(namespace, setName, keyValue);
        Record record = aerospikeClient.operate(null, key, operations);
        return operate("ADD", object, fieldName, value);
    }
    */

    /**
     * Prepend value to a set of fields.
     * @param object Input object
     * @param values map of fields and values.
     * @return Updated object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public T prepend(T object, Map<String, Object> values) throws IllegalAccessException, InstantiationException {
        return operate("PREPEND", object, values);
    }

    /**
     * Appends values to all given fields.
     * @param object Input object
     * @param values map of fields and values.
     * @return Updated object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public T append(T object, Map<String, Object> values) throws IllegalAccessException, InstantiationException {
        return operate("APPEND", object, values);
    }

    /**
     * Add integer bin values to existing record bin values. The policy specifies the transaction timeout
     * record expiration and how the transaction is handled when the record already exists.
     * This call only works for integer values. Object in the map must be Integer object.
     * @param object Input object
     * @param values map of fields and values.
     * @return Updated object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public T add(T object, Map<String, Object> values) throws IllegalAccessException, InstantiationException {
        return operate("ADD", object, values);
    }

    /**
     * Deletes entity
     * @param keyValue  Primary key value
     */
    @Override
    public void delete(String keyValue) {
        if (keyValue != null && keyValue.trim().length() != 0) {
            Key key = new Key(namespace, setName, keyValue);
            aerospikeClient.delete(null, key);
        }
    }

    /**
     * Deletes all entities associated with keyvalues
     * @param keyValues
     */
    @Override
    public void delete(String[] keyValues) {
        // Iterate the ids and delete each of the records. There is no batch operation for this yet. Lua procedure can help.
        for (String keyValue : keyValues) {
            delete(keyValue);
        }
    }

    /**
     * Delete all records.
     * @return the number of deleted records.
     * @throws Exception
     */
    @Override
    public long deleteAll() throws Exception {
        final int[] count = {0};
        try {
            // Scan all records and delete them. As there is no immediate physical delete, performance may not be bad.
            aerospikeClient.scanAll(null, namespace, setName, new ScanCallback() {
                public void scanCallback(Key key, Record record) throws AerospikeException {
                    aerospikeClient.delete(null, key);
                    count[0]++;
                }
            });
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
        return count[0];
    }
}
