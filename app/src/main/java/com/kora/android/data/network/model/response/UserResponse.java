package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class UserResponse {

    @JsonField(name = "id")
    String mId;
    @JsonField(name = "avatar")
    String mAvatar;
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
    @JsonField(name = "ERC20Token")
    String mERC20Token;
    @JsonField(name = "flag")
    String mFlag;
    @JsonField(name = "currencyNameFull")
    String mCurrencyNameFull;

    public String getId() {
        return mId;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getIdentity() {
        return mIdentity;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getRecoveryKey() {
        return mRecoveryKey;
    }

    public String getOwner() {
        return mOwner;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getLegalName() {
        return mLegalName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getERC20Token() {
        return mERC20Token;
    }

    public String getFlag() {
        return mFlag;
    }

    public String getCurrencyNameFull() {
        return mCurrencyNameFull;
    }

    @Override
    public String toString() {
        return "UserResponse{" + "\n" +
                "mId=" + mId + "\n" +
                "mAvatar=" + mAvatar + "\n" +
                "mPhoneNumber=" + mPhoneNumber + "\n" +
                "mIdentity=" + mIdentity + "\n" +
                "mCreator=" + mCreator + "\n" +
                "mRecoveryKey=" + mRecoveryKey + "\n" +
                "mOwner=" + mOwner + "\n" +
                "mUserName=" + mUserName + "\n" +
                "mLegalName=" + mLegalName + "\n" +
                "mEmail=" + mEmail + "\n" +
                "mDateOfBirth=" + mDateOfBirth + "\n" +
                "mCurrency=" + mCurrency + "\n" +
                "mPostalCode=" + mPostalCode + "\n" +
                "mAddress=" + mAddress + "\n" +
                "mERC20Token=" + mERC20Token + "\n" +
                "mFlag=" + mFlag + "\n" +
                "mCurrencyNameFull=" + mCurrencyNameFull + "\n" +
                '}';
    }
}
