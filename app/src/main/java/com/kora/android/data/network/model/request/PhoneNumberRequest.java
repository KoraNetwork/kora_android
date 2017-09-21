package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class PhoneNumberRequest {

    @JsonField(name = "phoneNumber")
    String mPhoneNumber;

    public PhoneNumberRequest() {

    }

    public PhoneNumberRequest(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public PhoneNumberRequest addPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
        return this;
    }
}
