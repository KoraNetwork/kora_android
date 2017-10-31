package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class RawTransactionRequest {

    @JsonField(name = "type")
    private String mType;
    @JsonField(name = "to")
    private String mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "rawTransactions")
    private List<String> mRawTransactions;

    public RawTransactionRequest addType(final String type) {
        mType = type;
        return this;
    }

    public RawTransactionRequest addTo(final String to) {
        mTo = to;
        return this;
    }

    public RawTransactionRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public RawTransactionRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public RawTransactionRequest addRawTransactions(final List<String> rawTransactions) {
        mRawTransactions = rawTransactions;
        return this;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
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

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double mToAmount) {
        this.mToAmount = mToAmount;
    }

    public List<String> getRawTransactions() {
        return mRawTransactions;
    }

    public void setRawTransactions(List<String> rawTransactions) {
        this.mRawTransactions = rawTransactions;
    }
}
