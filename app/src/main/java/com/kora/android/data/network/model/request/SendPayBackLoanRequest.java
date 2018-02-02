package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SendPayBackLoanRequest {

    @JsonField(name = "rawApprove")
    private String mRawApprove;
    @JsonField(name = "rawPayBackLoan")
    private String mRawPayBackLoan;

    public SendPayBackLoanRequest addRawApprove(final String rawApprove) {
        mRawApprove = rawApprove;
        return this;
    }

    public SendPayBackLoanRequest addRawPayBackLoan(final String rawPayBackLoan) {
        mRawPayBackLoan = rawPayBackLoan;
        return this;
    }

    public String getRawApprove() {
        return mRawApprove;
    }

    public void setRawApprove(String mRawApprove) {
        this.mRawApprove = mRawApprove;
    }

    public String getRawPayBackLoan() {
        return mRawPayBackLoan;
    }

    public void setRawPayBackLoan(String mRawPayBackLoan) {
        this.mRawPayBackLoan = mRawPayBackLoan;
    }
}
