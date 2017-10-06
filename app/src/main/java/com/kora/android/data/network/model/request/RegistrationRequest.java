package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class RegistrationRequest {

    @JsonField(name = "phone")
    String mPhoneNumber;
    @JsonField(name = "identity")
    String mIdentity;
    @JsonField(name = "creator")
    String mCreator;
    @JsonField(name = "recoveryKey")
    String mRecoveryKey;
    @JsonField(name = "owner")
    String mOwner;
    @JsonField(name = "userName")
    String mUserName;
    @JsonField(name = "legalName")
    String mLegalName;
    @JsonField(name = "email")
    String mEmail;
    @JsonField(name = "dateOfBirth")
    String mDateOfBirth;
    @JsonField(name = "currency")
    String mCurrency;
    @JsonField(name = "postalCode")
    String mPostalCode;
    @JsonField(name = "address")
    String mAddress;
    @JsonField(name = "password")
    String mPassword;

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
}
