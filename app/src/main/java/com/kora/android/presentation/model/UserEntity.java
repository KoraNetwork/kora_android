package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEntity implements Parcelable {

    private String mAvatar;
    private String mPhoneNumber;
    private String mIdentity;
    private String mCreator;
    private String mRecoveryKey;
    private String mOwner;
    private String mUserName;
    private String mLegalName;
    private String mEmail;
    private String mDateOfBirth;
    private String mCurrency;
    private String mPostalCode;
    private String mAddress;

    private String mPassword;
    private String mCountryCode;

    private String mERC20Token;
    private String mFlag;
    private String mCurrencyNameFull;

    public UserEntity addAvatar(final String avatar) {
        mAvatar = avatar;
        return this;
    }

    public UserEntity addPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
        return this;
    }

    public UserEntity addIdentity(final String identity) {
        mIdentity = identity;
        return this;
    }

    public UserEntity addCreator(final String creator) {
        mCreator = creator;
        return this;
    }

    public UserEntity addRecoveryKey(final String recoveryKey) {
        mRecoveryKey = recoveryKey;
        return this;
    }

    public UserEntity addOwner(final String owner) {
        mOwner = owner;
        return this;
    }

    public UserEntity addUserName(final String userName) {
        mUserName = userName;
        return this;
    }

    public UserEntity addLegalName(final String legalName) {
        mLegalName = legalName;
        return this;
    }

    public UserEntity addEmail(final String email) {
        mEmail = email;
        return this;
    }

    public UserEntity addDateOfBirth(final String dateOfBirth) {
        mDateOfBirth = dateOfBirth;
        return this;
    }

    public UserEntity addCurrency(final String currency) {
        mCurrency = currency;
        return this;
    }

    public UserEntity addPostalCode(final String postalCode) {
        mPostalCode = postalCode;
        return this;
    }

    public UserEntity addAddress(final String address) {
        mAddress = address;
        return this;
    }

    public UserEntity addERC20Token(final String ERC20Token) {
        mERC20Token = ERC20Token;
        return this;
    }

    public UserEntity addFlag(final String flag) {
        mFlag = flag;
        return this;
    }

    public UserEntity addCurrencyNameFull(final String currencyNameFull) {
        mCurrencyNameFull = currencyNameFull;
        return this;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
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

    public String getDateOfBirth() {
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

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getERC20Token() {
        return mERC20Token;
    }

    public void setERC20Token(String mERC20Token) {
        this.mERC20Token = mERC20Token;
    }

    public String getFlag() {
        return mFlag;
    }

    public void setFlag(String mFlag) {
        this.mFlag = mFlag;
    }

    public String getCurrencyNameFull() {
        return mCurrencyNameFull;
    }

    public void setCurrencyNameFull(String mCurrencyNameFull) {
        this.mCurrencyNameFull = mCurrencyNameFull;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "\n" +
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
                "mPassword=" + mPassword + "\n" +
                "mCountryCode=" + mCountryCode + "\n" +
                "mERC20Token=" + mERC20Token + "\n" +
                "mFlag=" + mFlag + "\n" +
                "mCurrencyNameFull=" + mCurrencyNameFull + "\n" +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAvatar);
        dest.writeString(this.mPhoneNumber);
        dest.writeString(this.mIdentity);
        dest.writeString(this.mCreator);
        dest.writeString(this.mRecoveryKey);
        dest.writeString(this.mOwner);
        dest.writeString(this.mUserName);
        dest.writeString(this.mLegalName);
        dest.writeString(this.mEmail);
        dest.writeString(this.mDateOfBirth);
        dest.writeString(this.mCurrency);
        dest.writeString(this.mPostalCode);
        dest.writeString(this.mAddress);
        dest.writeString(this.mPassword);
        dest.writeString(this.mCountryCode);
        dest.writeString(this.mERC20Token);
        dest.writeString(this.mFlag);
        dest.writeString(this.mCurrencyNameFull);
    }

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        this.mAvatar = in.readString();
        this.mPhoneNumber = in.readString();
        this.mIdentity = in.readString();
        this.mCreator = in.readString();
        this.mRecoveryKey = in.readString();
        this.mOwner = in.readString();
        this.mUserName = in.readString();
        this.mLegalName = in.readString();
        this.mEmail = in.readString();
        this.mDateOfBirth = in.readString();
        this.mCurrency = in.readString();
        this.mPostalCode = in.readString();
        this.mAddress = in.readString();
        this.mPassword = in.readString();
        this.mCountryCode = in.readString();
        this.mERC20Token = in.readString();
        this.mFlag = in.readString();
        this.mCurrencyNameFull = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}