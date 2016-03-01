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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeneralEntity extends Entity<GeneralEntity> {
    public final static String VAR_KEY = "var_key";
    public final static String VAR_INTEGER = "var_integer";
    public final static String VAR_STRING = "var_string";
    public final static String VAR_DOUBLE = "var_double";
    public final static String VAR_BOOLEAN = "var_boolean";
    public final static String VAR_TIMESTAMP = "var_timestamp";
    public final static String VAR_BIGDECIMAL = "var_bigdecimal";
    public final static String VAR_FLOAT = "var_float";
    public final static String VAR_LONG = "var_long";
    public final static String VAR_SHORT = "var_short";
    public final static String VAR_LIST = "var_list";
    public final static String VAR_MAP = "var_map";
    //public final static String VAR_BYTE = "var_byte";
    private static ArrayList<String> fieldNames = new ArrayList<String>();
    private static String keyName = VAR_KEY;

    static {
        fieldNames.add(VAR_KEY);
        fieldNames.add(VAR_INTEGER);
        fieldNames.add(VAR_STRING);
        fieldNames.add(VAR_DOUBLE);
        fieldNames.add(VAR_BOOLEAN);
        fieldNames.add(VAR_TIMESTAMP);
        fieldNames.add(VAR_BIGDECIMAL);
        fieldNames.add(VAR_FLOAT);
        fieldNames.add(VAR_LONG);
        fieldNames.add(VAR_SHORT);
        fieldNames.add(VAR_LIST);
        fieldNames.add(VAR_MAP);
        //fieldNames.add(VAR_BYTE);
    }

    public GeneralEntity() {
        super(new HashMap<String, Object>(), 0, 0);
    }

    public GeneralEntity(Map<String, Object> bins) {
        super(bins, 0, 0);
    }

    public GeneralEntity(Map<String, Object> bins, int generation, int expiration) {
        super(bins, generation, expiration);
    }

    public GeneralEntity(Map<String, Object> bins, int generation, int expiration, Boolean mutable) {
        super(bins, generation, expiration, mutable);
    }

    public String getVarKey() {
        return getString(VAR_KEY);
    }

    public void setVarKey(String varKey) {
        setValue(VAR_KEY, varKey);
    }

    public Integer getVarInteger() {
        return getInteger(VAR_INTEGER);
    }

    public void setVarInteger(Integer varInteger) {
        setValue(VAR_INTEGER, varInteger);
    }

    public String getVarString() {
        return getString(VAR_STRING);
    }

    public void setVarString(String varString) {
        setValue(VAR_STRING, varString);
    }

    public Double getVarDouble() {
        return getDouble(VAR_DOUBLE);
    }

    public void setVarDouble(Double varDouble) {
        setValue(VAR_DOUBLE, varDouble);
    }

    public Boolean getVarBoolean() {
        return getBoolean(VAR_BOOLEAN);
    }

    public void setVarBoolean(Boolean varBoolean) {
        setValue(VAR_BOOLEAN, varBoolean);
    }

    public Timestamp getVarTimestamp() {
        return getTimestamp(VAR_TIMESTAMP);
    }

    public void setVarTimestamp(Timestamp varTimestamp) {
        setValue(VAR_TIMESTAMP, varTimestamp);
    }

    public BigDecimal getVarBigdecimal() {
        return getBigDecimal(VAR_BIGDECIMAL);
    }

    public void setVarBigdecimal(BigDecimal varBigdecimal) {
        setValue(VAR_BIGDECIMAL, varBigdecimal);
    }

    public Float getVarFloat() {
        return getFloat(VAR_FLOAT);
    }

    public void setVarFloat(Float varFloat) {
        setValue(VAR_FLOAT, varFloat);
    }

    public Long getVarLong() {
        return getLong(VAR_LONG);
    }

    public void setVarLong(Long varLong) {
        setValue(VAR_LONG, varLong);
    }

    public ArrayList<String> getVarList() {
        return (ArrayList<String>) getList(VAR_LIST);
    }

    public void setVarList(ArrayList<String> varList) {
        setValue(VAR_LIST, varList);
    }

    public HashMap<String, Object> getVarMap() {
        return (HashMap<String, Object>) getMap(VAR_MAP);
    }

    public void setVarMap(HashMap<String, Object> varMap) {
        setValue(VAR_MAP, varMap);
    }

    public Short getVarShort() {
        return getShort(VAR_SHORT);
    }

    public void setVarShort(Short varShort) {
        setValue(VAR_SHORT, varShort);
    }

}