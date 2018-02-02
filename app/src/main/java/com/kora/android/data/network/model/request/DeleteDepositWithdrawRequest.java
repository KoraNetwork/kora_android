package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class DeleteDepositWithdrawRequest {

    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "rawTransaction")
    private String mRawTransaction;

    public DeleteDepositWithdrawRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DeleteDepositWithdrawRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DeleteDepositWithdrawRequest addRawTransaction(final String rawTransaction) {
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

    public void setRawTransaction(String rawTransaction) {
        this.mRawTransaction = rawTransaction;
    }
}
