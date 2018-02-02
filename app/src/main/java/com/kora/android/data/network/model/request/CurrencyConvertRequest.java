package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.kora.android.data.network.model.converter.TransactionTypeConverter;
import com.kora.android.presentation.enums.TransactionType;

@JsonObject
public class CurrencyConvertRequest {

    @JsonField(name = "to")
    private String mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "type", typeConverter = TransactionTypeConverter.class)
    private TransactionType mType;

    public CurrencyConvertRequest addTo(final String to) {
        mTo = to;
        return this;
    }

    public CurrencyConvertRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public CurrencyConvertRequest addType(TransactionType type) {
        mType = type;
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

    public TransactionType getType() {
        return mType;
    }

    public void setType(TransactionType mType) {
        this.mType = mType;
    }
}
