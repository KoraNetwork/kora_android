package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class RestorePasswordRequest {

    @JsonField(name = "token")
    private String mToken;
    @JsonField(name = "newPassword")
    private String mNewPassword;

    public RestorePasswordRequest addToken(final String token) {
        mToken = token;
        return this;
    }

    public RestorePasswordRequest addNewPassword(final String newPassword) {
        mNewPassword = newPassword;
        return this;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String mNewPassword) {
        this.mNewPassword = mNewPassword;
    }
}
