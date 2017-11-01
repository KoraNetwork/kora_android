package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class BorrowListResponse {

    @JsonField(name = "data")
    List<BorrowResponse> mList;

    public List<BorrowResponse> getList() {
        return mList;
    }

    public void setList(List<BorrowResponse> list) {
        mList = list;
    }
}
