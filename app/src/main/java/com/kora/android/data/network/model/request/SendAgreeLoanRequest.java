package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SendAgreeLoanRequest {

    @JsonField(name = "rawAgreeLoan")
    private String mRawAgreeLoan;

    public SendAgreeLoanRequest addRawCreateLoan(final String rawAgreeLoan) {
        mRawAgreeLoan = rawAgreeLoan;
        return this;
    }

    public String getRawAgreeLoan() {
        return mRawAgreeLoan;
    }

    public void setRawAgreeLoan(String mRawAgreeLoan) {
        this.mRawAgreeLoan = mRawAgreeLoan;
    }
}
