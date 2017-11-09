package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class BorrowAgreedRequest {

    @JsonField(name = "agree") private boolean mAgreed;

    public BorrowAgreedRequest() {
    }

    public BorrowAgreedRequest(boolean agreed) {
        mAgreed = agreed;
    }

    public boolean isAgreed() {
        return mAgreed;
    }

    public void setAgreed(boolean agreed) {
        mAgreed = agreed;
    }
}
