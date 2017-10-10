package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {

    private String mName;
    private String mCurrency;
    private String mPhoneCode;
    private String mFlag;

    public Country addName(final String name) {
        mName = name;
        return this;
    }

    public Country addCurrency(final String currency) {
        mCurrency = currency;
        return this;
    }

    public Country addPhoneCode(final String phoneCode) {
        mPhoneCode = phoneCode;
        return this;
    }

    public Country addFlag(final String flag) {
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
        return "Country{" + "\n" +
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

    public Country() {
    }

    protected Country(Parcel in) {
        this.mName = in.readString();
        this.mCurrency = in.readString();
        this.mPhoneCode = in.readString();
        this.mFlag = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
