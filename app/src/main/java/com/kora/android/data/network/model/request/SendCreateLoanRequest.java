package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SendCreateLoanRequest {

    @JsonField(name = "rawCreateLoan")
    private String mRawCreateLoan;

    public SendCreateLoanRequest addRawCreateLoan(final String rawCreateLoan) {
        mRawCreateLoan = rawCreateLoan;
        return this;
    }

    public String getRawCreateLoan() {
        return mRawCreateLoan;
    }

    public void setRawCreateLoan(String mRawCreateLoan) {
        this.mRawCreateLoan = mRawCreateLoan;
    }
}
