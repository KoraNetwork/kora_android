package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryEntity implements Parcelable {

    private String mCountryCode;
    private String mName;
    private String mCurrency;
    private String mCurrencyNameFull;
    private String mERC20Token;
    private String mPhoneCode;
    private String mFlag;

    public CountryEntity addCountryCode(final String countryCode) {
        mCountryCode = countryCode;
        return this;
    }

    public CountryEntity addName(final String name) {
        mName = name;
        return this;
    }

    public CountryEntity addCurrency(final String currency) {
        mCurrency = currency;
        return this;
    }

    public CountryEntity addCurrencyNameFull(final String currencyNameFull) {
        mCurrencyNameFull = currencyNameFull;
        return this;
    }

    public CountryEntity addERC20Token(final String ERC20Token) {
        mERC20Token = ERC20Token;
        return this;
    }

    public CountryEntity addPhoneCode(final String phoneCode) {
        mPhoneCode = phoneCode;
        return this;
    }

    public CountryEntity addFlag(final String flag) {
        mFlag = flag;
        return this;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        this.mCurrency = currency;
    }

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.mPhoneCode = phoneCode;
    }

    public String getFlag() {
        return mFlag;
    }

    public void setFlag(String flag) {
        this.mFlag = flag;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        this.mCountryCode = countryCode;
    }

    public String getCurrencyFullName() {
        return mCurrencyNameFull;
    }

    public void setCurrencyFullName(String currencyFullName) {
        this.mCurrencyNameFull = currencyFullName;
    }

    public String getERC20Token() {
        return mERC20Token;
    }

    public void setERC20Token(String ERC20Token) {
        this.mERC20Token = ERC20Token;
    }

    @Override
    public String toString() {
        return "CountryEntity{" + "\n" +
                "mCountryCode=" + mCountryCode + "\n" +
                "mName=" + mName + "\n" +
                "mCurrency=" + mCurrency + "\n" +
                "mCurrencyNameFull=" + mCurrencyNameFull + "\n" +
                "mERC20Token=" + mERC20Token + "\n" +
                "mPhoneCode=" + mPhoneCode + "\n" +
                "mFlag=" + mFlag + "\n" +
                "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCountryCode);
        dest.writeString(this.mName);
        dest.writeString(this.mCurrency);
        dest.writeString(this.mCurrencyNameFull);
        dest.writeString(this.mERC20Token);
        dest.writeString(this.mPhoneCode);
        dest.writeString(this.mFlag);
    }

    public CountryEntity() {
    }

    protected CountryEntity(Parcel in) {
        this.mCountryCode = in.readString();
        this.mName = in.readString();
        this.mCurrency = in.readString();
        this.mCurrencyNameFull = in.readString();
        this.mERC20Token = in.readString();
        this.mPhoneCode = in.readString();
        this.mFlag = in.readString();
    }

    public static final Creator<CountryEntity> CREATOR = new Creator<CountryEntity>() {
        @Override
        public CountryEntity createFromParcel(Parcel source) {
            return new CountryEntity(source);
        }

        @Override
        public CountryEntity[] newArray(int size) {
            return new CountryEntity[size];
        }
    };
}
