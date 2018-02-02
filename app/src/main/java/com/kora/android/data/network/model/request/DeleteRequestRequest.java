package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class DeleteRequestRequest {

    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "rawTransaction")
    private String mRawTransaction;

    public DeleteRequestRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DeleteRequestRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DeleteRequestRequest addRawTransaction(final String rawTransaction) {
        mRawTransaction = rawTransaction;
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

    public String getRawTransaction() {
        return mRawTransaction;
    }

    public void setRawTransaction(String rawTransactions) {
        this.mRawTransaction = rawTransactions;
    }
}
