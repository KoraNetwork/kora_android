package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SendPayBackLoanRequest {

    @JsonField(name = "rawPayBackLoan")
    private String mRawPayBackLoan;

    public SendPayBackLoanRequest addRawPayBackLoan(final String rawPayBackLoan) {
        mRawPayBackLoan = rawPayBackLoan;
        return this;
    }

    public String getRawPayBackLoan() {
        return mRawPayBackLoan;
    }

    public void setRawPayBackLoan(String mRawPayBackLoan) {
        this.mRawPayBackLoan = mRawPayBackLoan;
    }
}
