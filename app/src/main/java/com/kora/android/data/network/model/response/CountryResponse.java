package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CountryResponse {

    @JsonField(name = "countryCode")
    private String mCountryCode;
    @JsonField(name = "name")
    private String mName;
    @JsonField(name = "currency")
    private String mCurrency;
    @JsonField(name = "currencyNameFull")
    private String mCurrencyNameFull;
    @JsonField(name = "ERC20Token")
    private String mERC20Token;
    @JsonField(name = "phoneCode")
    private String mPhoneCode;
    @JsonField(name = "flag")
    private String mFlag;

    public String getCountryCode() {
        return mCountryCode;
    }

    public String getName() {
        return mName;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getCurrencyNameFull() {
        return mCurrencyNameFull;
    }

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public String getFlag() {
        return mFlag;
    }

    public String getERC20Token() {
        return mERC20Token;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public void setCurrencyNameFull(String mCurrencyNameFull) {
        this.mCurrencyNameFull = mCurrencyNameFull;
    }

    public void setERC20Token(String mERC20Token) {
        this.mERC20Token = mERC20Token;
    }

    public void setPhoneCode(String mPhoneCode) {
        this.mPhoneCode = mPhoneCode;
    }

    public void setFlag(String mFlag) {
        this.mFlag = mFlag;
    }
}
