package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class DeleteRequestRequest {

    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "rawTransactions")
    private List<String> mRawTransactions;

    public DeleteRequestRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DeleteRequestRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DeleteRequestRequest addRawTransactions(final List<String> rawTransactions) {
        mRawTransactions = rawTransactions;
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

    public List<String> getRawTransactions() {
        return mRawTransactions;
    }

    public void setRawTransactions(List<String> rawTransactions) {
        this.mRawTransactions = rawTransactions;
    }
}
