package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class UserListResponse {

    @JsonField(name = "total")
    private int mTotal;
    @JsonField(name = "data")
    private List<UserResponse> mData;

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int mTotal) {
        this.mTotal = mTotal;
    }

    public List<UserResponse> getData() {
        return mData;
    }

    public void setData(List<UserResponse> mUserResponseList) {
        this.mData = mUserResponseList;
    }

    @Override
    public String toString() {
        return "UserListResponse{" + "\n" +
                "mTotal=" + mTotal + "\n" +
                "mData=" + mData + "\n" +
                "}";
    }
}
