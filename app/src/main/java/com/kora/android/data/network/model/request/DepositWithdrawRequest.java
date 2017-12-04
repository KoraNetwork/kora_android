package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class DepositWithdrawRequest {

    @JsonField(name = "to")
    private String mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "interestRate")
    private int mInterestRate;

    public DepositWithdrawRequest addTo(final String to) {
        mTo = to;
        return this;
    }

    public DepositWithdrawRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DepositWithdrawRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DepositWithdrawRequest addInterestRate(final int interestRate) {
        mInterestRate = interestRate;
        return this;
    }

    public String getTo() {
        return mTo;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public int getInterestRate() {
        return mInterestRate;
    }

    public void setTo(String mTo) {
        this.mTo = mTo;
    }

    public void setFromAmount(double mFromAmount) {
        this.mFromAmount = mFromAmount;
    }

    public void setToAmount(double mToAmount) {
        this.mToAmount = mToAmount;
    }

    public void setInterestRate(int mInterestRate) {
        this.mInterestRate = mInterestRate;
    }
}
