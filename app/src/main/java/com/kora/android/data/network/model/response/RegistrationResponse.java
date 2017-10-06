package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class RegistrationResponse {

    @JsonField(name = "sessionToken")
    String mSessionToken;
    @JsonField(name = "user")
    UserResponse mUserResponse;

    public RegistrationResponse() {

    }

    public String getSessionToken() {
        return mSessionToken;
    }

    public UserResponse getUserResponse() {
        return mUserResponse;
    }

    @Override
    public String toString() {
        return "RegistrationResponse{" + "\n" +
                "mSessionToken=" + mSessionToken + "\n" +
                "mUserResponse=" + mUserResponse + "\n" +
                '}';
    }
}
