package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ConfirmationCodeRequest {

    @JsonField(name = "phoneNumber")
    private String mPhoneNumber;
    @JsonField(name = "verificationCode")
    private String mConfirmationCode;

    public ConfirmationCodeRequest addPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
        return this;
    }

    public ConfirmationCodeRequest addConfirmationCode(final String confirmationCode) {
        mConfirmationCode = confirmationCode;
        return this;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getConfirmationCode() {
        return mConfirmationCode;
    }

    public void setConfirmationCode(String mConfirmationCode) {
        this.mConfirmationCode = mConfirmationCode;
    }
}
