package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class LoginRequest {

    @JsonField(name = "identifier")
    private String mIdentifier;
    @JsonField(name = "password")
    private String mPassword;

    public LoginRequest addIdentifier(final String identifier) {
        mIdentifier = identifier;
        return this;
    }

    public LoginRequest addPassword(final String password) {
        mPassword = password;
        return this;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String mIdentifier) {
        this.mIdentifier = mIdentifier;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
