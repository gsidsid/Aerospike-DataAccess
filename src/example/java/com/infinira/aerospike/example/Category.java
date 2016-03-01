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

package com.infinira.aerospike.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infinira.aerospike.dataaccess.model.Entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Siddharth Garimella on 2/19/2016.
 */

/**
 * This is product category class.
 */
public class Category extends Entity<Category> {
    public final static String CATEGORY_ID = "category_id";                         // Unique category identifier
    public final static String PARENT_ID = "parent_id";                             // Parent category identifier if exists
    public final static String CATEGORY_PATH = "category_path";                     // Complete Category path such as 123/456/2314/29292
    public final static String NAME = "name";                                       // Category name
    public final static String PRODUCTS = "products";                               // Products in this category
    public final static String SOURCE = "source";                                   // Category source
    public final static String COVERAGE = "coverage";                               // Category coverage such as Americas, Europe, Asia, etc.
    public final static String CREATOR = "creator";                                 // Creator of this category
    public final static String PUBLISHER = "publisher";                             // Publisher of this category
    public final static String CONTRIBUTOR = "contributor";                         // Person who contributed to this category.
    public final static String IMG_TYPE = "img_type";                               // Type of category image (PNG, TIFF, GIF, etc.)
    public final static String IMG_NAME = "img_name";                               // Image name.
    public final static String IMG_URL = "img_url";                                 // Image URL
    public final static String IMG_ALT_TEXT = "img_alt_text";                       // Alt text
    public final static String SHORT_DESC = "short_desc";                           // Short description
    public final static String LONG_DESC = "long_desc";                             // Long description
    public final static String CREATED_DATE = "created_date";                       // Created date
    public final static String MODIFIED_DATE = "modified_date";                     // Modified date
    public final static String CUSTOM_PROP = "custom_prop";                         // Custom properties (Map structure with K,V pairs)
    private final static ArrayList<String> fieldNames = new ArrayList<String>();
    private final static ArrayList<String> mandatoryFieldNames = new ArrayList<String>();
    private final static String keyName = CATEGORY_ID;

    static {
        // Field Names
        fieldNames.add(CATEGORY_ID);
        fieldNames.add(PARENT_ID);
        fieldNames.add(CATEGORY_PATH);
        fieldNames.add(NAME);
        fieldNames.add(PRODUCTS);
        fieldNames.add(SOURCE);
        fieldNames.add(COVERAGE);
        fieldNames.add(CREATOR);
        fieldNames.add(PUBLISHER);
        fieldNames.add(CONTRIBUTOR);
        fieldNames.add(IMG_TYPE);
        fieldNames.add(IMG_NAME);
        fieldNames.add(IMG_URL);
        fieldNames.add(IMG_ALT_TEXT);
        fieldNames.add(SHORT_DESC);
        fieldNames.add(LONG_DESC);
        fieldNames.add(CREATED_DATE);
        fieldNames.add(MODIFIED_DATE);
        fieldNames.add(CUSTOM_PROP);

        // Mandatory Fields. These can be used as required.
        mandatoryFieldNames.add(CATEGORY_ID);
        mandatoryFieldNames.add(PARENT_ID);
        mandatoryFieldNames.add(CATEGORY_PATH);
        mandatoryFieldNames.add(NAME);
        mandatoryFieldNames.add(SHORT_DESC);
    }

    public Category() {
        super(new HashMap<String, Object>(), 0, 0);
    }


    public Category(Map<String, Object> bins, int generation, int expiration) {
        super(bins, generation, expiration);
    }

    // Sets the key value
    @JsonIgnore
    public void setKey(Object key) {
        setValue(keyName, key);
    }

    // Gets the key value
    @JsonIgnore
    public Object getKey() {
        return getValue(keyName);
    }

    // Gets all the field names
    @JsonIgnore
    public static ArrayList<String> getFieldNames() {
        return fieldNames;
    }

    // Gets all mandatory field names
    @JsonIgnore
    public static ArrayList<String> getMandatoryFieldNames() {
        return mandatoryFieldNames;
    }


    public String getCategoryId() {
        return getString(CATEGORY_ID);
    }

    public void setCategoryId(String categoryId) {
        setValue(CATEGORY_ID, categoryId);
    }

    public String getCategoryPath() {
        return getString(CATEGORY_PATH);
    }

    public void setCategoryPath(String categoryPath) {
        setValue(CATEGORY_PATH, categoryPath);
    }

    public String getParentId() {
        return getString(PARENT_ID);
    }

    public void setParentId(String parentId) {
        setValue(PARENT_ID, parentId);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        setValue(NAME, name);
    }

    public List<String> getProducts() {
        return (List<String>) getList(PRODUCTS);
    }

    public void setProducts(List<String> products) {
        setValue(PRODUCTS, products);
    }

    public String getSource() {
        return getString(SOURCE);
    }

    public void setSource(String source) {
        setValue(SOURCE, source);
    }

    public String getCoverage() {
        return getString(COVERAGE);
    }

    public void setCoverage(String coverage) {
        setValue(COVERAGE, coverage);
    }

    public String getCreator() {
        return getString(CREATOR);
    }

    public void setCreator(String creator) {
        setValue(CREATOR, creator);
    }

    public String getPublisher() {
        return getString(PUBLISHER);
    }

    public void setPublisher(String publisher) {
        setValue(PUBLISHER, publisher);
    }

    public String getContributor() {
        return getString(CONTRIBUTOR);
    }

    public void setContributor(String contributor) {
        setValue(CONTRIBUTOR, contributor);
    }

    public Timestamp getCreationDate() {
        return getTimestamp(CREATED_DATE);
    }

    public void setCreationDate(Timestamp creationDate) {
        setValue(CREATED_DATE, creationDate);
    }

    public Timestamp getModifiedDate() {
        return getTimestamp(MODIFIED_DATE);
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        setValue(MODIFIED_DATE, modifiedDate);
    }

    public String getImgType() {
        return getString(IMG_TYPE);
    }

    public void setImgType(String imgType) {
        setValue(IMG_TYPE, imgType);
    }

    public String getImgName() {
        return getString(IMG_NAME);
    }

    public void setImgName(String imgName) {
        setValue(IMG_NAME, imgName);
    }

    public String getImgUrl() {
        return getString(IMG_URL);
    }

    public void setImgUrl(String imgUrl) {
        setValue(IMG_URL, imgUrl);
    }

    public String getImgAltText() {
        return getString(IMG_ALT_TEXT);
    }

    public void setImgAltText(String imgAltText) {
        setValue(IMG_ALT_TEXT, imgAltText);
    }

    public String getShortDesc() {
        return getString(SHORT_DESC);
    }

    public void setShortDesc(String shortDesc) {
        setValue(SHORT_DESC, shortDesc);
    }

    public String getLongDesc() {
        return getString(LONG_DESC);
    }

    public void setLongDesc(String longDesc) {
        setValue(LONG_DESC, longDesc);
    }

    public HashMap<String, Object> getCustomProp() {
        return (HashMap<String, Object>) getMap(CUSTOM_PROP);
    }

    public void setCustomProp(HashMap<String, Object> customProp) {
        setValue(CUSTOM_PROP, customProp);
    }


}
