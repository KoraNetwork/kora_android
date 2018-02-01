package com.kora.android.data.web3j.model.response;


public class CreateWalletsResponse {

    private String mOwner;
    private String mRecovery;

    public CreateWalletsResponse() {

    }

    public CreateWalletsResponse(final String owner,
                                 final String recovery) {
        mOwner = owner;
        mRecovery = recovery;
    }

    public CreateWalletsResponse addOwner(String owner) {
        mOwner = owner;
        return this;
    }

    public CreateWalletsResponse addRecovery(String recovery) {
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
