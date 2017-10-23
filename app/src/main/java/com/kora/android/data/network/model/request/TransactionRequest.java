package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class TransactionRequest {

    @JsonField(name = "type")
    private String mType;
    @JsonField(name = "to")
    private String mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "transactionHash")
    private List<String> mTransactionHash;

    public TransactionRequest addType(final String type) {
        mType = type;
        return this;
    }

    public TransactionRequest addTo(final String to) {
        mTo = to;
        return this;
    }

    public TransactionRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public TransactionRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public TransactionRequest addTransactionHash(final List<String> transactionHash) {
        mTransactionHash = transactionHash;
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

    public double getmToAmount() {
        return mToAmount;
    }

    public void setToAmount(double mToAmount) {
        this.mToAmount = mToAmount;
    }

    public List<String> getTransactionHash() {
        return mTransactionHash;
    }

    public void setTransactionHash(List<String> mTransactionHash) {
        this.mTransactionHash = mTransactionHash;
    }
}
