package com.infinira.aerospike.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinira.aerospike.dataaccess.model.Entity;
import com.infinira.aerospike.dataaccess.util.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Siddharth Garimella on 2/23/2016.
 */

/**
 * Product class which extends Entity class.
 */
public class Product extends Entity<Product> {

    // List of product attribute definition. Aerospike supports only 14 chars for bin name. With this approach,
    // getter and setter methods can be of any length. Only attribute/bin names can be restricted to 14 characters.
    public final static String PRODUCT_ID = "product_id";                     // Product SKU
    public final static String SKU = "sku";                                   // Product SKU
    public final static String NAME = "name";                                 // Product Name
    public final static String CODE = "code";                                 // Product Code
    public final static String CATEGORIES = "categories";                     // List of categories that contain this product.
    public final static String IN_STOCK = "in_stock";                         // Is in stock
    public final static String VISIBLE = "visible";                           // Is visible
    public final static String TAX_ID = "tax_id";                             // Tax id
    public final static String TYPE = "type";                                 // Product Type
    public final static String MSRP_CURRENCY = "msrp_currency";               // MSRP Currency Code
    public final static String MSRP_AMOUNT = "msrp_amount";                   // MSRP amount
    public final static String PRICE_CURRENCY = "price_currency";             // Price Currency Code
    public final static String PRICE_AMOUNT = "price_amount";                 // Price amount
    public final static String NOTES = "notes";                               // Notes
    public final static String SHORT_DESC = "short_desc";                     // Short description
    public final static String LONG_DESC = "long_desc";                       // Long description
    public final static String IMG_TYPE = "img_type";                         // Image type (PNNG/JPEG/..
    public final static String IMG_NAME = "img_name";                         // Image name
    public final static String IMG_URL = "img_url";                           // Image URL
    public final static String IMG_ALT_TEXT = "img_alt_text";                 // Image alt text
    public final static String CREATED_BY = "created_by";                     // Created by
    public final static String CREATED_DATE = "created_date";                 // Created date
    public final static String MODIFIED_DATE = "modified_date";               // Image type (PNNG/JPEG/..
    public final static String CUSTOM_PROP = "Custom_Prop";                   // Custom properties {Name, Value} pairs in a hashMap. Thet can be serialized into JSON for

    // List of filed name used by Product. These are helpful to iterate over all attributes and obtain them
    // dynamically.
    private final static ArrayList<String> fieldNames = new ArrayList<String>();
    // List of mandatory attributes to create a product object. This can be useful for form/method validations without requiring
    // external file.
    private final static ArrayList<String> mandatoryFieldNames = new ArrayList<String>();
    // Product record keyname. This is used a primary key by Aerospike to uniquely identify the product object.
    private final static String keyName = PRODUCT_ID;

    //
    static {
        // All available fields.
        fieldNames.add(PRODUCT_ID);
        fieldNames.add(SKU);
        fieldNames.add(NAME);
        fieldNames.add(CODE);
        fieldNames.add(IN_STOCK);
        fieldNames.add(VISIBLE);
        fieldNames.add(TAX_ID);
        fieldNames.add(TYPE);
        fieldNames.add(MSRP_CURRENCY);
        fieldNames.add(MSRP_AMOUNT);
        fieldNames.add(PRICE_CURRENCY);
        fieldNames.add(PRICE_AMOUNT);
        fieldNames.add(NOTES);
        fieldNames.add(SHORT_DESC);
        fieldNames.add(LONG_DESC);
        fieldNames.add(IMG_TYPE);
        fieldNames.add(IMG_NAME);
        fieldNames.add(IMG_URL);
        fieldNames.add(IMG_ALT_TEXT);
        fieldNames.add(CREATED_BY);
        fieldNames.add(CREATED_DATE);
        fieldNames.add(MODIFIED_DATE);
        fieldNames.add(CUSTOM_PROP);

        // Mandatory Fileds
        mandatoryFieldNames.add(PRODUCT_ID);
        mandatoryFieldNames.add(SKU);
        mandatoryFieldNames.add(NAME);
        mandatoryFieldNames.add(TYPE);
        mandatoryFieldNames.add(MSRP_CURRENCY);
        mandatoryFieldNames.add(MSRP_AMOUNT);

    }

    // Default constructor
    public Product() {
        super(new HashMap<String, Object>(), 0, 0);
    }

    // Create object with map data structure. If the object is created retrieving from database, corresponding
    // generation and expiration values are specified. If a brand new object is created, set these values to 0 by default.
    public Product(Map<String, Object> bins, int generation, int expiration) {
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

    // Getter and setter methods.

    public String getProductId() {
        return getString(PRODUCT_ID);
    }

    public void setProductId(String productId) {
        setValue(PRODUCT_ID, productId);
    }

    public String getSku() {
        return getString(SKU);
    }

    public void setSku(String sku) {
        setValue(SKU, sku);
    }

    // Category list JSON is retrieved and converted into ArrayList. This is to demonstrate how to use JSON instead
    // of Java object serialization. The disadvantage is incrementally adding categories to a product. Need to get the list,
    // add new category and update the list by invoking a setter method.

    public ArrayList<String> getCategories() throws IOException {
        String categories = getString(CATEGORIES);
        return (ArrayList<String>) Utils.getObjectMapper().readValue(categories, List.class);
    }

    // Note that categories are stored as JSON string in the database.
    public void setCategories(ArrayList<String> categories) {
        try {
            setValue(CATEGORIES, Utils.getObjectMapper().writeValueAsString(categories));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        setValue(NAME, name);
    }

    public String getCode() {
        return getString(CODE);
    }

    public void setCode(String code) {
        setValue(CODE, code);
    }

    public Boolean getInStock() {
        return getBoolean(IN_STOCK);
    }

    public void setInStock(Boolean inStock) {
        setValue(IN_STOCK, inStock);
    }

    public Boolean getVisible() {
        return getBoolean(VISIBLE);
    }

    public void setVisible(Boolean visible) {
        setValue(VISIBLE, visible);
    }

    public String getTaxId() {
        return getString(TAX_ID);
    }

    public void setTaxId(String taxId) {
        setValue(TAX_ID, taxId);
    }

    public String getType() {
        return getString(TYPE);
    }

    public void setType(String type) {
        setValue(TYPE, type);
    }

    public String getMsrpCurrency() {
        return getString(MSRP_CURRENCY);
    }

    public void setMsrpCurrency(String msrpCurrency) {
        setValue(MSRP_CURRENCY, msrpCurrency);
    }

    public BigDecimal getMsrpAmount() {
        return getBigDecimal(MSRP_AMOUNT);
    }

    public void setMsrpAmount(BigDecimal msrpAmount) {
        setValue(MSRP_AMOUNT, msrpAmount);
    }

    public String getPriceCurrency() {
        return getString(PRICE_CURRENCY);
    }

    public void setPriceCurrency(String priceCurrency) {
        setValue(PRICE_CURRENCY, priceCurrency);
    }

    public BigDecimal getPriceAmount() {
        return getBigDecimal(PRICE_AMOUNT);
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        setValue(PRICE_AMOUNT, priceAmount);
    }

    public Timestamp getNotes() {
        return getTimestamp(NOTES);
    }

    public void setNotes(Timestamp notes) {
        setValue(NOTES, notes);
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

    public String getCreatedBy() {
        return getString(CREATED_BY);
    }

    public void setCreatedBy(String createdBy) {
        setValue(CREATED_BY, createdBy);
    }

    public Timestamp getCreatedDate() {
        return getTimestamp(CREATED_DATE);
    }

    public void setCreatedDate(Timestamp createdDate) {
        setValue(CREATED_DATE, createdDate);
    }

    public Timestamp getModifiedDate() {
        return getTimestamp(MODIFIED_DATE);
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        setValue(MODIFIED_DATE, modifiedDate);
    }


    //Store custom properties as JSON string in the database

    public HashMap<String, Object> getCustomProp() throws IOException {
        String customProp = getString(CUSTOM_PROP);
        return (HashMap<String, Object>) Utils.getObjectMapper().readValue(customProp, Map.class);
    }

    public void setCustomProp(HashMap<String, Object> customProp) {
        try {
            setValue(CUSTOM_PROP, Utils.getObjectMapper().writeValueAsString(customProp));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
