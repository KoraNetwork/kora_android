package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CountryResponse {

    @JsonField(name = "name")
    String mName;
    @JsonField(name = "currency")
    String mCurrency;
    @JsonField(name = "phoneCode")
    String mPhoneCode;
    @JsonField(name = "flag")
    String mFlag;

    public String getName() {
        return mName;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public String getFlag() {
        return mFlag;
    }
}
