package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CountryResponse {

    @JsonField(name = "countryCode")
    String mCountryCode;
    @JsonField(name = "name")
    String mName;
    @JsonField(name = "currency")
    String mCurrency;
    @JsonField(name = "currencyNameFull")
    String mCurrencyNameFull;
    @JsonField(name = "ERC20Token")
    String mERC20Token;
    @JsonField(name = "phoneCode")
    String mPhoneCode;
    @JsonField(name = "flag")
    String mFlag;

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
}
