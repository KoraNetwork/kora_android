package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class DepositWithdrawListResponse {

    @JsonField(name = "data")
    private List<DepositWithdrawResponse> mResponseList = new ArrayList<>();

    public List<DepositWithdrawResponse> getResponseList() {
        return mResponseList;
    }

    public void setResponseList(List<DepositWithdrawResponse> responseList) {
        mResponseList = responseList;
    }
}
