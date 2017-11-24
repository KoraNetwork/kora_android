package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class SendPayBackLoanRequest {

    @JsonField(name = "rawApproves")
    private List<String> mRawApproves;
    @JsonField(name = "rawPayBackLoan")
    private String mRawPayBackLoan;

    public SendPayBackLoanRequest addRawApproves(final List<String> rawApproves) {
        mRawApproves = rawApproves;
        return this;
    }

    public SendPayBackLoanRequest addRawPayBackLoan(final String rawPayBackLoan) {
        mRawPayBackLoan = rawPayBackLoan;
        return this;
    }

    public List<String> getRawApproves() {
        return mRawApproves;
    }

    public void setRawApproves(List<String> mRawApproves) {
        this.mRawApproves = mRawApproves;
    }

    public String getRawPayBackLoan() {
        return mRawPayBackLoan;
    }

    public void setRawPayBackLoan(String mRawPayBackLoan) {
        this.mRawPayBackLoan = mRawPayBackLoan;
    }
}
