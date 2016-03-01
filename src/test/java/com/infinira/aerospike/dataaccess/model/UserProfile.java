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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
@JsonPropertyOrder({"accountNumber", "userId", "firstName", "middleName", "lastName", "personalTitle", "suffix",
        "nickname", "firstNameLocal", "middleNameLocal", "lastNameLocal", "otherLocal", "gender", "birthDate",
        "mothersMaidenName", "maritalStatus", "socialSecurityNumber", "email", "profession", "title",
        "employmentStatus", "residenceStatus", "languageId", "currencyId", "currentPhoto", "prevPhoto",
        "photoUpdatedOn", "mphoneCountryCode", "mphoneAreaCode", "mphoneNumber", "addressLine1",
        "addressLine2", "city", "state", "country", "postalCode", "latitude", "longitude", "elevation",
        "description", "createdOn", "modifiedOn", "modifiedBy", "createdOn", "userAttrs"})
public class UserProfile extends Entity {
    public final static String ACCOUNT_NUMBER = "account_number";
    public final static String USER_ID = "user_id";
    public final static String FIRST_NAME = "first_name";
    public final static String MIDDLE_NAME = "middle_name";
    public final static String LAST_NAME = "last_name";
    public final static String PERSONAL_TITLE = "personal_title";
    public final static String SUFFIX = "suffix";
    public final static String NICKNAME = "nick_name";
    public final static String FIRST_NAME_LOCAL = "first_name_l";
    public final static String MIDDLE_NAME_LOCAL = "middle_name_l";
    public final static String LAST_NAME_LOCAL = "last_name_l";
    public final static String OTHER_LOCAL = "other_loc";
    public final static String GENDER = "gender";
    public final static String BIRTH_DATE = "birth_date";
    public final static String MOTHERS_MAIDEN_NAME = "maiden_name";
    public final static String MARITAL_STATUS = "marital_status";
    public final static String SOCIAL_SECURITY_NUMBER = "ssn_4";
    public final static String EMAIL = "email";
    public final static String PROFESSION = "profession";
    public final static String TITLE = "title";
    public final static String EMPLOYMENT_STATUS = "emp_status";
    public final static String RESIDENCE_STATUS = "res_status";
    public final static String CURRENT_PHOTO = "current_photo";
    public final static String PREV_PHOTO = "prev_photo";
    public final static String PHOTO_UPDATED_ON = "photo_updt_on";
    public final static String MPHONE_COUNTRY_CODE = "m_country_code"; // country code
    public final static String MPHONE_AREA_CODE = "m_area_code";// area code
    public final static String MPHONE_NUMBER = "m_number";
    public final static String ADDRESS_LINE1 = "addr_line1";
    public final static String ADDRESS_LINE2 = "addr_line2";
    public final static String CITY = "addr_city";
    public final static String STATE = "addr_state";
    public final static String POSTAL_CODE = "addr_post_code";
    public final static String COUNTRY_CODE = "addr_country";
    public final static String LANGUAGE_ID = "language_id";
    public final static String CURRENCY_ID = "currency_id";
    public final static String LOCATION = "location";
    public final static String DESCRIPTION = "description";
    public final static String MODIFIED_ON = "modified_on";
    public final static String MODIFIED_BY = "modified_by";
    public final static String CREATED_ON = "created_on";
    public final static String USER_ATTRS = "user_attrs";
    private static final ArrayList<String> fieldNames = new ArrayList<String>();
    private static final ArrayList<String> mandatoryFieldNames = new ArrayList<String>();
    private static String keyName = ACCOUNT_NUMBER;


    static {
        fieldNames.add(ACCOUNT_NUMBER);
        fieldNames.add(USER_ID);
        fieldNames.add(FIRST_NAME);
        fieldNames.add(MIDDLE_NAME);
        fieldNames.add(LAST_NAME);
        fieldNames.add(PERSONAL_TITLE);
        fieldNames.add(SUFFIX);
        fieldNames.add(NICKNAME);
        fieldNames.add(FIRST_NAME_LOCAL);
        fieldNames.add(MIDDLE_NAME_LOCAL);
        fieldNames.add(LAST_NAME_LOCAL);
        fieldNames.add(OTHER_LOCAL);
        fieldNames.add(GENDER);
        fieldNames.add(BIRTH_DATE);
        fieldNames.add(MOTHERS_MAIDEN_NAME);
        fieldNames.add(MARITAL_STATUS);
        fieldNames.add(SOCIAL_SECURITY_NUMBER);
        fieldNames.add(EMAIL);
        fieldNames.add(PROFESSION);
        fieldNames.add(TITLE);
        fieldNames.add(EMPLOYMENT_STATUS);
        fieldNames.add(RESIDENCE_STATUS);
        fieldNames.add(LANGUAGE_ID);
        fieldNames.add(CURRENCY_ID);
        fieldNames.add(CURRENT_PHOTO);
        fieldNames.add(PREV_PHOTO);
        fieldNames.add(PHOTO_UPDATED_ON);
        fieldNames.add(MPHONE_COUNTRY_CODE);
        fieldNames.add(MPHONE_AREA_CODE);
        fieldNames.add(MPHONE_NUMBER);
        fieldNames.add(ADDRESS_LINE1);
        fieldNames.add(ADDRESS_LINE2);
        fieldNames.add(CITY);
        fieldNames.add(STATE);
        fieldNames.add(COUNTRY_CODE);
        fieldNames.add(POSTAL_CODE);
        fieldNames.add(LOCATION);
        fieldNames.add(DESCRIPTION);
        fieldNames.add(CREATED_ON);
        fieldNames.add(MODIFIED_ON);
        fieldNames.add(MODIFIED_BY);
        fieldNames.add(USER_ATTRS);
        
        // Mandatory field names
        mandatoryFieldNames.add(ACCOUNT_NUMBER);
        mandatoryFieldNames.add(USER_ID);
        mandatoryFieldNames.add(FIRST_NAME);
        mandatoryFieldNames.add(LAST_NAME);
        mandatoryFieldNames.add(EMAIL);
        mandatoryFieldNames.add(MPHONE_COUNTRY_CODE);
        mandatoryFieldNames.add(MPHONE_AREA_CODE);
        mandatoryFieldNames.add(MPHONE_NUMBER);
        mandatoryFieldNames.add(COUNTRY_CODE);
    }

    public UserProfile() {
        super(new HashMap<String, Object>(), 0, 0);
    }

    public UserProfile(Map<String, Object> bins, int generation, int expiration) {
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

    public String getAccountNumber() {
        return getString(ACCOUNT_NUMBER);
    }

    public void setAccountNumber(String actNumber) {
        setValue(ACCOUNT_NUMBER, actNumber);
    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public void setUserId(String userId) {
        setValue(USER_ID, userId);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        setValue(FIRST_NAME, firstName);
    }

    public String getMiddleName() {
        return getString(MIDDLE_NAME);
    }

    public void setMiddleName(String middleName) {
        setValue(MIDDLE_NAME, middleName);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public void setLastName(String lastName) {
        setValue(LAST_NAME, lastName);
    }

    public String getPersonalTitle() {
        return getString(PERSONAL_TITLE);
    }

    public void setPersonalTitle(String personalTitle) {
        setValue(PERSONAL_TITLE, personalTitle);
    }

    public String getSuffix() {
        return getString(SUFFIX);
    }

    public void setSuffix(String suffix) {
        setValue(SUFFIX, suffix);
    }

    public String getNickname() {
        return getString(NICKNAME);
    }

    public void setNickname(String nickname) {
        setValue(NICKNAME, nickname);
    }

    public String getFirstNameLocal() {
        return getString(FIRST_NAME_LOCAL);
    }

    public void setFirstNameLocal(String firstNameLocal) {
        setValue(FIRST_NAME_LOCAL, firstNameLocal);
    }

    public String getMiddleNameLocal() {
        return getString(MIDDLE_NAME_LOCAL);
    }

    public void setMiddleNameLocal(String middleNameLocal) {
        setValue(MIDDLE_NAME_LOCAL, middleNameLocal);
    }

    public String getLastNameLocal() {
        return getString(LAST_NAME_LOCAL);
    }

    public void setLastNameLocal(String lastNameLocal) {
        setValue(LAST_NAME_LOCAL, lastNameLocal);
    }

    public String getOtherLocal() {
        return getString(OTHER_LOCAL);
    }

    public void setOtherLocal(String otherLocal) {
        setValue(OTHER_LOCAL, otherLocal);
    }

    public String getGender() {
        return getString(GENDER);
    }

    public void setGender(String gender) {
        setValue(GENDER, gender);
    }

    public Timestamp getBirthDate() {
        return getTimestamp(BIRTH_DATE);
    }

    public void setBirthDate(Timestamp birthDate) {
        setValue(BIRTH_DATE, birthDate);
    }

    public String getMothersMaidenName() {
        return getString(MOTHERS_MAIDEN_NAME);
    }

    public void setMothersMaidenName(String mothersMaidenName) {
        setValue(MOTHERS_MAIDEN_NAME, mothersMaidenName);
    }

    public String getMaritalStatus() {
        return getString(MARITAL_STATUS);
    }

    public void setMaritalStatus(String maritalStatus) {
        setValue(MARITAL_STATUS, maritalStatus);
    }

    public String getSocialSecurityNumber() {
        return getString(SOCIAL_SECURITY_NUMBER);
    }

    public void setSocial_SecurityNumber(String socialSecurityNumber) {
        setValue(SOCIAL_SECURITY_NUMBER, socialSecurityNumber);
    }

    public String getEmail() {
        return getString(EMAIL);
    }

    public void setEmail(String email) {
        setValue(EMAIL, email);
    }

    public String getProfession() {
        return getString(PROFESSION);
    }

    public void setProfession(String profession) {
        setValue(PROFESSION, profession);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public void setTitle(String title) {
        setValue(TITLE, title);
    }

    public String getEmploymentStatus() {
        return getString(EMPLOYMENT_STATUS);
    }

    public void setEmploymentStatus(String employmentStatus) {
        setValue(EMPLOYMENT_STATUS, employmentStatus);
    }

    public String getResidenceStatus() {
        return getString(RESIDENCE_STATUS);
    }

    public void setResidenceStatus(String residenceStatus) {
        setValue(RESIDENCE_STATUS, residenceStatus);
    }

    public String getLanguageId() {
        return getString(LANGUAGE_ID);
    }

    public void setLanguageId(String languageId) {
        setValue(LANGUAGE_ID, languageId);
    }

    public String getCurrencyId() {
        return getString(CURRENCY_ID);
    }

    public void setCurrencyId(String currencyId) {
        setValue(CURRENCY_ID, currencyId);
    }

    public String getCurrentPhoto() {
        return getString(CURRENT_PHOTO);
    }

    public void setCurrentPhoto(String currentPhoto) {
        setValue(CURRENT_PHOTO, currentPhoto);
    }

    public String getPrevPhoto() {
        return getString(PREV_PHOTO);
    }

    public void setPrevPhoto(String prevPhoto) {
        setValue(PREV_PHOTO, prevPhoto);
    }

    public Timestamp getPhotoUpdateOn() {
        return getTimestamp(PHOTO_UPDATED_ON);
    }

    public void setPhotoUpdateOn(Timestamp photoUpdateOn) {
        setValue(PHOTO_UPDATED_ON, photoUpdateOn);
    }

    public Integer getMphoneCountryCode() {
        return getInteger(MPHONE_COUNTRY_CODE);
    }

    public void setMphoneCountryCode(Integer mphone_CountryCode) {
        setValue(MPHONE_COUNTRY_CODE, mphone_CountryCode);
    }

    public Integer getMphoneAreaCode() {
        return getInteger(MPHONE_AREA_CODE);
    }

    public void setMphoneAreaCode(Integer mphoneAreaCode) {
        setValue(MPHONE_AREA_CODE, mphoneAreaCode);
    }

    public String getMphoneNumber() {
        return getString(MPHONE_NUMBER);
    }

    public void setMphoneNumber(String mphoneNumber) {
        setValue(MPHONE_NUMBER, mphoneNumber);
    }

    public String getAddressLine1() {
        return getString(ADDRESS_LINE1);
    }

    public void setAddressLine1(String addressLine1) {
        setValue(ADDRESS_LINE1, addressLine1);
    }

    public String getAddressLine2() {
        return getString(ADDRESS_LINE2);
    }

    public void setAddressLine2(String addressLine2) {
        setValue(ADDRESS_LINE2, addressLine2);
    }

    public String getCity() {
        return getString(CITY);
    }

    public void setCity(String city) {
        setValue(CITY, city);
    }

    public String getState() {
        return getString(STATE);
    }

    public void setState(String state) {
        setValue(STATE, state);
    }

    public String getCountryCode() {
        return getString(COUNTRY_CODE);
    }

    public void setCountryCode(String countryCode) {
        setValue(COUNTRY_CODE, countryCode);
    }

    public String getPostalCode() {
        return getString(POSTAL_CODE);
    }

    public void setPostalCode(String postalCode) {
        setValue(POSTAL_CODE, postalCode);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public void setDescription(String description) {
        setValue(DESCRIPTION, description);
    }

    public Timestamp getCreatedOn() {
        return getTimestamp(CREATED_ON);
    }

    public void setCreatedOn(Timestamp createdOn) {
        setValue(CREATED_ON, createdOn);
    }

    public Timestamp getModifiedOn() {
        return getTimestamp(MODIFIED_ON);
    }

    public void setModifiedOn(Timestamp modifiedOn) {
        setValue(MODIFIED_ON, modifiedOn);
    }

    public String getModifiedBy() {
        return getString(MODIFIED_BY);
    }

    public void setModifiedBy(String modifiedBy) {
        setValue(MODIFIED_BY, modifiedBy);
    }

    public Map<String, Object> getUserAttrs() {
        return getMap(USER_ATTRS);
    }

    public Object getUserAttr(String attributeName) {
        if (getUserAttrs() == null)
            return null;
        else return getUserAttrs().get(attributeName);
    }

    public void setUserAttrs(HashMap<String, Object> user_attrs) {
        setValue(USER_ATTRS, user_attrs);
    }

    public void setUserAttr(String attributeName, Object value) {
        if (getUserAttrs() == null) {
            setUserAttrs(new HashMap<String, Object>());
        }
        getUserAttrs().put(attributeName, value);
    }
}
