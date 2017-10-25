package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class RequestListResponse {

    @JsonField(name = "data")
    private List<RequestResponse> mRequestResponses = new ArrayList<>();

    public List<RequestResponse> getRequestResponses() {
        return mRequestResponses;
    }

    public void setRequestResponses(List<RequestResponse> requestResponses) {
        mRequestResponses = requestResponses;
    }
}
