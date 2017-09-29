package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class PhoneNumberResponse {

    @JsonField(name = "sent")
    boolean mSent;

    public PhoneNumberResponse() {

    }

    public boolean isSent() {
        return mSent;
    }
}
