package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class LoginResponse {

    @JsonField(name = "sessionToken")
    private String mSessionToken;
    @JsonField(name = "user")
    private UserResponse mUserResponse;

    public String getSessionToken() {
        return mSessionToken;
    }

    public UserResponse getUserResponse() {
        return mUserResponse;
    }

    public void setSessionToken(String mSessionToken) {
        this.mSessionToken = mSessionToken;
    }

    public void setUserResponse(UserResponse mUserResponse) {
        this.mUserResponse = mUserResponse;
    }
}

