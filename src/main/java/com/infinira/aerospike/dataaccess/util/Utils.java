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

package com.infinira.aerospike.dataaccess.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;

public class Utils {
    protected final static Logger logger = LoggerFactory.getLogger(Utils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //objectMapper.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
    }


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    // To JSON string.
    public static String toJSONString(Object object) throws JsonProcessingException {
        if (object == null)
            return null;
        return Utils.getObjectMapper().writeValueAsString(object);
    }

    // From JSON string to object.
    public static Object toObject(String jsonString, Class<?> modelType) throws IOException {
        if (jsonString == null)
            return null;
        return Utils.getObjectMapper().readValue(jsonString, modelType);
    }

    //--------------------------Java Object reflection methods ----------

    public static boolean set(Object object, String fieldName, Object fieldValue) {
        Assert.notNull(object, "Object cannot be null.");
        Assert.notNull(fieldName, "Fieldname cannot be null.");
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <V> V get(Object object, String fieldName) {
        Assert.notNull(object, "Object cannot be null.");
        Assert.notNull(fieldName, "Fieldname cannot be null.");
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (V) field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    // Utility method to print all setter methods.
    public static void printSetterMethods(String className) throws Throwable {
        try {
            Class c = Class.forName(className);
            Method m[] = c.getMethods();
            for (int i = 0; i < m.length; i++)
                if (m[i].getName().startsWith("set")) {
                    System.out.println("instanceName." + m[i].getName() + "(\"valOne\");");
                }

        } catch (Throwable e) {
            log(e);
            throw e;
        }
    }

    public static void print(String className) throws Throwable {
        try {
            Class c = Class.forName(className);
            Method m[] = c.getMethods();
            for (int i = 0; i < m.length; i++)
                if (m[i].getName().startsWith("set")) {
                    System.out.println("instanceName." + m[i].getName() + "(\"valOne\");");
                }

        } catch (Throwable e) {
            log(e);
            throw e;
        }
    }

    private static void log(Object message) {
        logger.error(String.valueOf(message));//
        // System.out.println(String.valueOf(message));
    }

    // ------------------ File reading utils-----------
    public static String readTextFile(String fileName) throws IOException {
        StringBuffer output = new StringBuffer();
        // This will reference one line at a time
        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
                System.out.println(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            logger.error("Unable to open file '" + fileName + "'");
            throw ex;
        } catch (IOException ex) {
            logger.error("Error reading file '" + fileName + "'");
            throw ex;
        }
        return output.toString();
    }

    //---------------- Timestamp utilities--------------------
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }


}

