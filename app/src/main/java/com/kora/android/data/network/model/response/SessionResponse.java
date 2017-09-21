package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SessionResponse {

    @JsonField(name = "session_token")
    String mSessionToken;

    public SessionResponse() {

    }

    public String getSessionToken() {
        return mSessionToken;
    }
}
