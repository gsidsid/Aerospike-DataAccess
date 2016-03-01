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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserLogin extends Entity {
    public final static String ACCOUNT_NUMBER = "account_number";
    public final static String USER_ID = "user_id";
    public final static String CURRENT_PASSWORD = "c_password";
    public final static String PASSWORD_HINT = "pwd_hint";
    public final static String CURRENT_PASSCODE = "c_passcode";
    public final static String ACTIVE_TOKEN = "active_token";
    public final static String IS_SYSTEM = "is_system";
    public final static String ENABLED = "enabled";
    public final static String HAS_LOGGED_OUT = "has_logged_out";
    public final static String REQUIRE_PASSWORD_CHANGE = "req_pwd_change";
    public final static String LAST_CURRENCY_ID = "last_currency_id";
    public final static String LAST_LOCALE = "last_locale";
    public final static String LAST_TIME_ZONE = "last_time_zone";
    public final static String DISABLED_DATE_TIME = "dsbld_datetime";
    public final static String SUCCESSIVE_FAILED_LOGINS = "failed_logins";
    public final static String MODIFIED_ON = "updated_stamp";
    public final static String CREATED_ON = "created_stamp";
    private final static ArrayList<String> fieldNames = new ArrayList<String>();
    private final static ArrayList<String> mandatoryFieldNames = new ArrayList<String>();
    private final static String keyName = ACCOUNT_NUMBER;

    static {
        fieldNames.add(ACCOUNT_NUMBER);           // Unique user identifier.
        fieldNames.add(USER_ID);                  // User identifier that is user friendly to login.
        fieldNames.add(CURRENT_PASSWORD);         // Current user password.
        fieldNames.add(PASSWORD_HINT);            // Password hint statement
        fieldNames.add(CURRENT_PASSCODE);         // Current passcode (4 digit code)
        fieldNames.add(ACTIVE_TOKEN);             // Active token
        fieldNames.add(IS_SYSTEM);                // Is system using this identifier
        fieldNames.add(ENABLED);                  // Is this user account enabled
        fieldNames.add(HAS_LOGGED_OUT);           // Has the user no actively logged in
        fieldNames.add(REQUIRE_PASSWORD_CHANGE);  // Does the user require password change
        fieldNames.add(LAST_TIME_ZONE);           // Last login timezone. (useful for fraud detection_
        fieldNames.add(DISABLED_DATE_TIME);       // Date/time when user was marked as disabled.
        fieldNames.add(SUCCESSIVE_FAILED_LOGINS); // Successive failed logins. If it exceeds a limit, he needs to re-initiate clearing
        fieldNames.add(MODIFIED_ON);       // Last record updated timestamp
        fieldNames.add(CREATED_ON);            // Record creation timestamp.

        // Mandatory fields
        mandatoryFieldNames.add(ACCOUNT_NUMBER);           // Unique user identifier
        mandatoryFieldNames.add(USER_ID);                  // User identifier that is user friendly to login
        mandatoryFieldNames.add(CURRENT_PASSWORD);         // Current user password
        mandatoryFieldNames.add(PASSWORD_HINT);            // Password hint statement
        mandatoryFieldNames.add(CURRENT_PASSCODE);         // Current passcode (4 digit code)
        mandatoryFieldNames.add(ACTIVE_TOKEN);             // Active token. This gets replaced when logged in again with a new token.
        mandatoryFieldNames.add(ENABLED);                  // Is this user account enabled
    }

    public UserLogin() {
        super(new HashMap<String, Object>(), 0, 0);
    }


    public UserLogin(Map<String, Object> bins, int generation, int expiration) {
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

    public String getCurrentPassword() {
        return getString(CURRENT_PASSWORD);
    }

    public void setCurrentPassword(String currentPassword) {
        setValue(CURRENT_PASSWORD, currentPassword);
    }

    public String getPasswordHint() {
        return getString(PASSWORD_HINT);
    }

    public void setPasswordHint(String passwordHint) {
        setValue(PASSWORD_HINT, passwordHint);
    }

    public String getCurrentPasscode() {
        return getString(CURRENT_PASSCODE);
    }

    public void setCurrentPasscode(String currentPasscode) {
        setValue(CURRENT_PASSCODE, currentPasscode);
    }

    public String getActiveToken() {
        return getString(ACTIVE_TOKEN);
    }

    public void setActiveToken(String activeToken) {
        setValue(ACTIVE_TOKEN, activeToken);
    }

    public Boolean isSystem() {
        return getBoolean(IS_SYSTEM);
    }

    public void setSystem(boolean system) {
        setValue(IS_SYSTEM, system);
    }

    public Boolean isEnabled() {
        return getBoolean(ENABLED);
    }

    public void setEnabled(boolean enabled) {
        setValue(ENABLED, new Boolean(enabled));
    }

    public Boolean isHasLoggedOut() {
        return getBoolean(HAS_LOGGED_OUT);
    }

    public void setHasLoggedOut(boolean hasLoggedOut) {
        setValue(HAS_LOGGED_OUT, hasLoggedOut);
    }

    public Boolean isRequirePasswordChange() {
        return getBoolean(REQUIRE_PASSWORD_CHANGE);
    }

    public void setRequirePasswordChange(boolean requirePasswordChange) {
        setValue(REQUIRE_PASSWORD_CHANGE, requirePasswordChange);
    }

    public String getLastTimezone() {
        return getString(LAST_TIME_ZONE);
    }

    public void setLastTimezone(String lastTimezone) {
        setValue(LAST_TIME_ZONE, lastTimezone);
    }

    public Timestamp getDisabledDatetime() {
        return getTimestamp(DISABLED_DATE_TIME);
    }

    public void setDisabledDatetime(Timestamp disabledDatetime) {
        // Store long value.
        setValue(DISABLED_DATE_TIME, disabledDatetime.getTime());
    }

    public int getSuccessiveFailedLogins() {
        return getInteger(SUCCESSIVE_FAILED_LOGINS);
    }

    public void setSuccessiveFailedLogins(int successiveFailedLogins) {
        setValue(SUCCESSIVE_FAILED_LOGINS, successiveFailedLogins);
    }

    public Timestamp getModifiedOn() {
        return getTimestamp(MODIFIED_ON);
    }

    public void setModifiedOn(Timestamp lastUpdatedStamp) {
        setValue(MODIFIED_ON, lastUpdatedStamp);
    }

    public Timestamp getCreatedOn() {
        return getTimestamp(CREATED_ON);
    }

    public void setCreatedOn(Timestamp createdStamp) {
        setValue(CREATED_ON, createdStamp.getTime());
    }

}
