package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CurrencyConvertRequest {

    @JsonField(name = "to")
    private String mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;

    public CurrencyConvertRequest addTo(final String to) {
        mTo = to;
        return this;
    }

    public CurrencyConvertRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public String getTo() {
        return mTo;
    }

    public void setTo(String mTo) {
        this.mTo = mTo;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public void setFromAmount(double mFromAmount) {
        this.mFromAmount = mFromAmount;
    }
}
