package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ConfirmationCodeRequest {

    @JsonField(name = "phoneNumber")
    String mPhoneNumber;
    @JsonField(name = "verificationCode")
    String mConfirmationCode;

    public ConfirmationCodeRequest() {

    }

    public ConfirmationCodeRequest addPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
        return this;
    }

    public ConfirmationCodeRequest addConfirmationCode(final String confirmationCode) {
        mConfirmationCode = confirmationCode;
        return this;
    }
}
