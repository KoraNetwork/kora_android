package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class DeleteRequestRequest {

    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "transactionHash")
    private List<String> mTransactionHash;

    public DeleteRequestRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DeleteRequestRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DeleteRequestRequest addTransactionHash(final List<String> transactionHash) {
        mTransactionHash = transactionHash;
        return this;
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

    public List<String> getTransactionHash() {
        return mTransactionHash;
    }

    public void setTransactionHash(List<String> mTransactionHash) {
        this.mTransactionHash = mTransactionHash;
    }
}
