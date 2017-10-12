package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryEntity implements Parcelable {

    private String mName;
    private String mCurrency;
    private String mPhoneCode;
    private String mFlag;

    public CountryEntity addName(final String name) {
        mName = name;
        return this;
    }

    public CountryEntity addCurrency(final String currency) {
        mCurrency = currency;
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

    @Override
    public String toString() {
        return "CountryEntity{" + "\n" +
                "mName=" + mName + "\n" +
                "mCurrency=" + mCurrency + "\n" +
                "mPhoneCode=" + mPhoneCode + "\n" +
                "mFlag=" + mFlag + "\n" +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mCurrency);
        dest.writeString(this.mPhoneCode);
        dest.writeString(this.mFlag);
    }

    public CountryEntity() {

    }

    protected CountryEntity(Parcel in) {
        this.mName = in.readString();
        this.mCurrency = in.readString();
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
