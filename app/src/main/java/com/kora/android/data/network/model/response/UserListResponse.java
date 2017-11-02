package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class UserListResponse {

    @JsonField(name = "recents")
    private List<UserResponse> mRecents;
    @JsonField(name = "contacts")
    private List<UserResponse> mContacts;

    public List<UserResponse> getRecents() {
        return mRecents;
    }

    public void setRecents(List<UserResponse> recents) {
        mRecents = recents;
    }

    public List<UserResponse> getContacts() {
        return mContacts;
    }

    public void setContacts(List<UserResponse> contacts) {
        mContacts = contacts;
    }
}
