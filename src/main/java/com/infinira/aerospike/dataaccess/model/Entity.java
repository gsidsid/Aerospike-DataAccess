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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinira.aerospike.dataaccess.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

//import org.springframework.util.CollectionUtils;

//Todo: Add parameter validations, logging statements, Documentation.

public abstract class Entity<T> implements Serializable, Comparable<Entity>, Cloneable {

    protected final static Logger logger = LoggerFactory.getLogger(Entity.class);

    public final static String FIELDS = "bins";
    public final static String FIELD_NAMES = "fieldNames";
    public final static String PERSISTED = "persisted";

    // No need to serialize these attributes (transient)
    protected transient boolean persisted = false;
    protected transient boolean modified = false;
    protected transient boolean mutable = true;
    protected transient boolean isFromDb = false;
    protected transient int generation;
    protected transient int expiration;

    // Hashmap that maintains all attributes and value pairs.
    @JsonIgnore
    protected Map<String, Object> bins;
    // Field value change status helps in updating an object without requiring to externally keep track of changes.
    @JsonIgnore
    protected transient HashSet<String> valueChangeStatus = new HashSet<String>();

    /**
     * Default  Entity constructor with map <fields, values>
     */
    protected Entity() {
        this(new HashMap<String, Object>(), 0, 0);
    }

    /**
     * Entity constructor.
     *
     * @param data:       Hashmap of fields and values.
     * @param generation: Aerospike record/entity generation.
     * @param expiration: Record expirtation time.
     */
    public Entity(Map<String, Object> data, int generation, int expiration) {
        if (this.bins == null)
            this.bins = new HashMap<String, Object>();
        // Iterate the entries in bins structure and check if there are any data type conversions needed.
        if (data != null && data.size() > 0) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if(value != null) {
                    if (value instanceof Boolean) {
                        // Convert boolean value into 0 or 1 string. Takes less storage space than long (64 bits) and is indexable.
                        String boolStr = "0";
                        if ((Boolean) value) boolStr = "1";
                        this.bins.put(name, boolStr);
                    } else if (value instanceof Float) {
                        // Get a string from the value and store the value instead of Float serialized object.
                        this.bins.put(name, ((Float) value).toString());
                    } else if (value instanceof Integer) {
                        // Convert integer into long value
                        this.bins.put(name, ((Integer) value).longValue());
                    } else if (value instanceof Short) {
                        // Convert short into long value
                        this.bins.put(name, ((Short) value).longValue());
                    } else if (value instanceof Double) {
                        // Just keep Double object. Repository will take care of storing double instead of serailzed object
                        this.bins.put(name, value);//((Double)value).doubleValue());
                    } else if (value instanceof BigDecimal) {
                        // Get a string from the value and store the value instead of BigDecimal serialized object.
                        this.bins.put(name, ((BigDecimal) value).toPlainString());
                    } else if (value instanceof Timestamp) {
                        // Get timestamp time value
                        this.bins.put(name, ((Timestamp) value).getTime());
                    } else {
                        this.bins.put(name, value);
                    }
                } else  this.bins.put(name, value);
            }
        }
        this.generation = generation;
        this.expiration = expiration;
        this.isFromDb = !(generation == 0 && expiration == 0);
    }

    /**
     * Constructor with mutable flag.
     * @param bins
     * @param generation
     * @param expiration
     * @param mutable    : If the object is not mutable (read-only), set this flag to false. By default, mutable is true.
     */
    public Entity(Map<String, Object> bins, int generation, int expiration, Boolean mutable) {
        this(bins, generation, expiration);
        this.mutable = mutable;
    }

    /**
     * Set if this entity is persisted in the database
     * @param persisted  flag
     */
    public void withPersisted(boolean persisted) {
        this.persisted = persisted;
    }


    /**
     * Internal method to obtain HashMap and directly store values to database
     * @return bins (attribute, value) pairs.
     */
    @JsonIgnore
    public Map<String, Object> getAllFieldValues() {
        return bins;
    }

    /**
     * Get list of attributes that have changed values.
     * @return
     */
    @JsonIgnore
    public HashSet<String> getValueChangeStatus() {
        return valueChangeStatus;
    }

    /**
     * Clears the value change statuses
     */
    @JsonIgnore
    public void clearValueChangeStatus() {
        valueChangeStatus.clear();
    }


    // Gets the value of a given field name.

    /**
     * Gets the value of a given field name.
     * @param name field name
     * @return  Value object
     */
    public Object getValue(String name) {
        return this.bins == null ? null : this.bins.get(name);
    }

    // Getter methods for generic types.

    /**
     * Get String for a given field name
     * @param name  field name
     * @return String value
     */
    protected String getString(String name) {
        return (String) this.getValue(name);
    }

    /**
     * Get Double object for a given field value
     * @param name  field name
     * @return  Double value
     */
    protected Double getDouble(String name) {
        Object value = this.getValue(name);
        if (value == null) return null;
        return (value instanceof Double) ? (Double) value : Double.longBitsToDouble((Long) value);
    }

    /**
     * Get BigDecimal object for a given field value. BigDecimal is converted into string for storage and it is converted back into BigDecimal.
     * @param name  field name
     * @return  BigDecimal value
     */
    protected BigDecimal getBigDecimal(String name) {
        String value = this.getString(name);
        return value != null ? new BigDecimal(value) : null;
    }

    /**
     * Get Float object for a given field value
     * @param name  field name
     * @return  Float value
     */
    protected Float getFloat(String name) {
        String value = this.getString(name);
        return value != null ? new Float(value) : null;
    }

    /**
     * Get Integer object for a given field value
     * @param name  field name
     * @return  Integer value
     */
    protected Integer getInteger(String name) {
        Long value = this.getLong(name);
        return value != null ? value.intValue() : null;
    }

    /**
     * Get Long object for a given field value
     * @param name  field name
     * @return  Long value
     */
    protected Long getLong(String name) {
        Object value = this.getValue(name);
        return value != null ? (Long) value : null;
    }

    /**
     * Get Short object for a given field value
     * @param name  field name
     * @return  Short value
     */
    protected Short getShort(String name) {
        Long value = this.getLong(name);
        return value != null ? value.shortValue() : null;
    }

    /**
     * Get Boolean object for a given field value
     * @param name  field name
     * @return  Boolean value
     */
    protected Boolean getBoolean(String name) {
        String value = this.getString(name);
        return value != null ? value.equals("1") : null;
    }

    /**
     * Get Timestamp object for a given field value
     * @param name  field name
     * @return  Timestamp value
     */
    protected Timestamp getTimestamp(String name) {
        Long value = this.getLong(name);
        return value != null ? new Timestamp(value) : null;
    }

    /**
     * Get List object for a given field value. Note lists are stored as serialzed objects in the database.
     * @param name  field name
     * @return  List value
     */
    protected List<?> getList(String name) {
        return (List) this.getValue(name);
    }

    /**
     * Get Map object for a given field value. Note maps are stored as serialzed objects in the database.
     * @param name  field name
     * @return  Map value
     */
    protected Map<?, ?> getMap(String name) {
        return (Map<?, ?>) this.getValue(name);
    }

    /**
     * Get GeoJSON object for a given field value.
     * @param name  field name
     * @return  Geo JSON String value
     */
    public String getGeoJSON(String name) {
        return (String) this.getValue(name);
    }

    /**
     * Get Java Object for a given field value. Note that object must of serializable.
     * @param name  field name
     * @return  Object value
     */
    public Object getObject(String name) {
        return (bins == null) ? null : bins.get(name);
    }

    /**
     * Get the time to live value for this object.
     * @return
     */
    @JsonIgnore
    protected int getTimeToLive() {
        if (this.expiration == 0) {
            return -1;
        } else {
            int now = (int) ((System.currentTimeMillis() - 1262304000000L) / 1000L);
            return this.expiration >= 0 && this.expiration <= now ? 1 : this.expiration - now;
        }
    }

    /**
     * Sets value of an attribute.
     * @param name Name of the attribute
     * @param value Value of the attribute
     */
    public void setValue(String name, Object value) {
        // If the object is not mutable, log message and throw an exception.
        if (!mutable) {
            logger.error("Object is an immutable object. Updates are not allowed.");
            return;
        }
        boolean containsKey = bins.containsKey(name);

        // Get the current value.
        Object currentValue = this.getValue(name);

        // If the field is already available.
        if (containsKey) {
            // If both current and new value are null, return.
            if (currentValue == null && value == null)
                return;

            // If both current and new values are equal (plain string comparison), return.
            if (currentValue != null && value != null && currentValue.toString().equals(value.toString()))
                return;

            // If generation is greater than 0, set the valueChangeStatus to true;
            if (generation > 0)
                valueChangeStatus.add(name); // Update the change status.
        }

        // Store the value.
        storeInBin(name, value);
    }

    private void storeInBin(String name, Object value) {
        if (value == null) {
            bins.put(name, value);
        } else {
            if (value instanceof Boolean) {
                // Convert boolean value into 0 or 1 string
                String boolStr = "0";
                if ((Boolean) value) boolStr = "1";
                bins.put(name, boolStr);
            } else if (value instanceof Float) {
                // Get a string from the value and store the value instead of Float serialized object.
                bins.put(name, ((Float) value).toString());
            } else if (value instanceof Integer) {
                // Convert integer into long value
                bins.put(name, ((Integer) value).longValue());
            } else if (value instanceof Short) {
                // Convert short into long value
                bins.put(name, ((Short) value).longValue());
            } else if (value instanceof BigDecimal) {
                // Get a string from the value and store the value instead of BigDecimal serialized object.
                bins.put(name, ((BigDecimal) value).toPlainString());
            } else if (value instanceof Double) {
                // Get a string from the value and store the value instead of Double serialized object.
                bins.put(name, ((Double)value).doubleValue());
            } else if (value instanceof Timestamp) {
                // Get timestamp time value
                bins.put(name, ((Timestamp) value).getTime());
            } else {
                bins.put(name, value);
            }
        }
        if(!modified)
            modified = true; // Entity changed.
    }

    /**
     *  Clear the object and reset it.
     */
    public void clear() {
        bins.clear();
        valueChangeStatus.clear();
        isFromDb = false;
        generation = 0;
        expiration = 0;
        modified = false;
        mutable = true;
    }

    /**
     * Get the set of Keys for this object.
     * @return  Set of keys.
     */
    public Set<String> keySet() {
        return Collections.unmodifiableSet(bins.keySet());
    }

    /**
     * Object entry set for all (key, value) pairs.
     * @return
     */
    public Set<Map.Entry<String, Object>> entrySet() {
        return Collections.unmodifiableSet(bins.entrySet());
    }

    /**
     * Compare two entities. Subclass should implement this if required.
     * @param entity
     * @return -1 if current object is less than input object, 0 if equal and 1 if current object is greater.
     */
    public int compareTo(Entity entity) {
        return 0;
    }


    @Override
    public String toString() {
        String jsonInString = null;
        try {
            jsonInString = Utils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(jsonInString);
        return jsonInString;
    }

    // JSON onvenience methods
    /**
     * Converts this object to JSON string
     * @return JSON string
     * @throws JsonProcessingException
     */
    public String toJSONString() throws JsonProcessingException {
        return Utils.getObjectMapper().writeValueAsString(this);
    }

    // From JSON string to object.

    /**
     * Creates an object from the JSON String.
     * @param jsonString
     * @param modelType
     * @return
     * @throws IOException
     */
    public static Object toObject(String jsonString, Class<?> modelType) throws IOException {
        return Utils.getObjectMapper().readValue(jsonString, modelType);
    }
}
