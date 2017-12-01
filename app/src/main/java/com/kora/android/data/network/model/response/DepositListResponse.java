package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class DepositListResponse {

    @JsonField(name = "data")
    private List<DepositResponse> mDepositResponses = new ArrayList<>();

    public List<DepositResponse> getDepositResponses() {
        return mDepositResponses;
    }

    public void setDepositResponses(List<DepositResponse> depositResponses) {
        mDepositResponses = depositResponses;
    }
}
