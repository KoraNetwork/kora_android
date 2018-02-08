package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class RegistrationRequest {

    @JsonField(name = "phone")
    private String mPhoneNumber;
    @JsonField(name = "identity")
    private String mIdentity;
    @JsonField(name = "creator")
    private String mCreator;
    @JsonField(name = "recoveryKey")
    private String mRecoveryKey;
    @JsonField(name = "owner")
    private String mOwner;
    @JsonField(name = "userName")
    private String mUserName;
    @JsonField(name = "legalName")
    private String mLegalName;
    @JsonField(name = "email")
    private String mEmail;
    @JsonField(name = "dateOfBirth")
    private String mDateOfBirth;
    @JsonField(name = "currency")
    private String mCurrency;
    @JsonField(name = "postalCode")
    private String mPostalCode;
    @JsonField(name = "address")
    private String mAddress;
    @JsonField(name = "password")
    private String mPassword;

    public RegistrationRequest addPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
        return this;
    }

    public RegistrationRequest addIdentity(final String identity) {
        mIdentity = identity;
        return this;
    }

    public RegistrationRequest addCreator(final String creator) {
        mCreator = creator;
        return this;
    }

    public RegistrationRequest addRecoveryKey(final String recoveryKey) {
        mRecoveryKey = recoveryKey;
        return this;
    }

    public RegistrationRequest addOwner(final String owner) {
        mOwner = owner;
        return this;
    }

    public RegistrationRequest addUserName(final String userName) {
        mUserName = userName;
        return this;
    }

    public RegistrationRequest addLegalName(final String legalName) {
        mLegalName = legalName;
        return this;
    }

    public RegistrationRequest addEmail(final String email) {
        mEmail = email;
        return this;
    }

    public RegistrationRequest addDateOfBirth(final String dateOfBirth) {
        mDateOfBirth = dateOfBirth;
        return this;
    }

    public RegistrationRequest addCurrency(final String currency) {
        mCurrency = currency;
        return this;
    }

    public RegistrationRequest addPostalCode(final String postalCode) {
        mPostalCode = postalCode;
        return this;
    }

    public RegistrationRequest addAddress(final String address) {
        mAddress = address;
        return this;
    }

    public RegistrationRequest addPassword(final String password) {
        mPassword = password;
        return this;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getIdentity() {
        return mIdentity;
    }

    public void setIdentity(String mIdentity) {
        this.mIdentity = mIdentity;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String mCreator) {
        this.mCreator = mCreator;
    }

    public String getRecoveryKey() {
        return mRecoveryKey;
    }

    public void setRecoveryKey(String mRecoveryKey) {
        this.mRecoveryKey = mRecoveryKey;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getLegalName() {
        return mLegalName;
    }

    public void setLegalName(String mLegalName) {
        this.mLegalName = mLegalName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(String mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String mPostalCode) {
        this.mPostalCode = mPostalCode;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
