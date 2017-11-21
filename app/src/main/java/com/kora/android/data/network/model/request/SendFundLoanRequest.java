package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class SendFundLoanRequest {

    @JsonField(name = "rawApproves")
    private List<String> mRawApproves;
    @JsonField(name = "rawFundLoan")
    private String mRawFundLoan;

    public SendFundLoanRequest addRawApproves(final List<String> rawApproves) {
        mRawApproves = rawApproves;
        return this;
    }

    public SendFundLoanRequest addRawFundLoan(final String rawFundLoan) {
        mRawFundLoan = rawFundLoan;
        return this;
    }

    public List<String> getRawApproves() {
        return mRawApproves;
    }

    public void setRawApproves(List<String> mRawApproves) {
        this.mRawApproves = mRawApproves;
    }

    public String getRawFundLoan() {
        return mRawFundLoan;
    }

    public void setRawFundLoan(String mRawFundLoan) {
        this.mRawFundLoan = mRawFundLoan;
    }
}
