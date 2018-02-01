package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CreateIdentityRequest {

    @JsonField(name = "owner")
    private String mOwner;
    @JsonField(name = "recoveryKey")
    private String mRecovery;

    public CreateIdentityRequest addOwner(final String owner) {
        mOwner = owner;
        return this;
    }

    public CreateIdentityRequest addRecovery(final String recovery) {
        mRecovery = recovery;
        return this;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getRecovery() {
        return mRecovery;
    }

    public void setRecovery(String mRecovery) {
        this.mRecovery = mRecovery;
    }
}
