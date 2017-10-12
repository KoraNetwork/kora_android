package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class LoginResponse {

    @JsonField(name = "sessionToken")
    String mSessionToken;
    @JsonField(name = "user")
    UserResponse mUserResponse;

    public LoginResponse() {

    }

    public String getSessionToken() {
        return mSessionToken;
    }

    public UserResponse getUserResponse() {
        return mUserResponse;
    }

    @Override
    public String toString() {
        return "LoginResponse{" + "\n" +
                "mSessionToken=" + mSessionToken + "\n" +
                "mUserResponse=" + mUserResponse + "\n" +
                '}';
    }
}

