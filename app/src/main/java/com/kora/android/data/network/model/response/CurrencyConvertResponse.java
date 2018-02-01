package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CurrencyConvertResponse {

    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "exchangeRate")
    private double mExchangeRate;

    public double getFromAmount() {
        return mFromAmount;
    }

    public void setFromAmount(double mFromAmount) {
        this.mFromAmount = mFromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double mToAmount) {
        this.mToAmount = mToAmount;
    }

    public double getExchangeRate() {
        return mExchangeRate;
    }

    public void setExchangeRate(double mExchangeRate) {
        this.mExchangeRate = mExchangeRate;
    }
}
