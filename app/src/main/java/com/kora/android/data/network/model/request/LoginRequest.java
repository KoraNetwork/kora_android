package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class LoginRequest {

    @JsonField(name = "identifier")
    String mIdentifier;
    @JsonField(name = "password")
    String mPassword;

    public LoginRequest addIdentifier(final String identifier) {
        mIdentifier = identifier;
        return this;
    }

    public LoginRequest addPassword(final String password) {
        mPassword = password;
        return this;
    }
}
