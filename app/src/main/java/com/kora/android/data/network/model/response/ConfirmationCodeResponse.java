package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ConfirmationCodeResponse {

    @JsonField(name = "confirmed")
    boolean mConfirmed;

    public ConfirmationCodeResponse() {

    }

    public boolean isConfirmed() {
        return mConfirmed;
    }
}
