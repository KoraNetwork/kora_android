package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ForgotPasswordRequest {

    @JsonField(name = "email")
    private String mEmail;

    public ForgotPasswordRequest addEmail(final String email) {
        mEmail = email;
        return this;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
