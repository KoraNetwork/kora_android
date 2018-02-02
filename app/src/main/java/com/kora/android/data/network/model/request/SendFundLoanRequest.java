package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SendFundLoanRequest {

    @JsonField(name = "rawApprove")
    private String mRawApprove;
    @JsonField(name = "rawFundLoan")
    private String mRawFundLoan;

    public SendFundLoanRequest addRawApprove(final String rawApprove) {
        mRawApprove = rawApprove;
        return this;
    }

    public SendFundLoanRequest addRawFundLoan(final String rawFundLoan) {
        mRawFundLoan = rawFundLoan;
        return this;
    }

    public String getRawApprove() {
        return mRawApprove;
    }

    public void setRawApprove(String mRawApprove) {
        this.mRawApprove = mRawApprove;
    }

    public String getRawFundLoan() {
        return mRawFundLoan;
    }

    public void setRawFundLoan(String mRawFundLoan) {
        this.mRawFundLoan = mRawFundLoan;
    }
}
