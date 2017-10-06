package com.kora.android.data.web3j.model.response;

import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;

import org.web3j.abi.datatypes.Address;

public class IdentityCreatedResponse {

    private String mIdentity;
    private String mCreator;
    private String mRecoveryKey;
    private String mOwner;

    public IdentityCreatedResponse(final MetaIdentityManager.IdentityCreatedEventResponse identityCreatedEventResponse) {
        mIdentity = identityCreatedEventResponse.identity.toString();
        mCreator = identityCreatedEventResponse.creator.toString();
        mRecoveryKey = identityCreatedEventResponse.recoveryKey.toString();
        mOwner = identityCreatedEventResponse.owner.toString();
    }

    public String getIdentity() {
        return mIdentity;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getRecoveryKey() {
        return mRecoveryKey;
    }

    public String getOwner() {
        return mOwner;
    }

    @Override
    public String toString() {
        return "IdentityCreatedResponse{" + "\n" +
                "mIdentity=" + mIdentity + "\n" +
                "mCreator=" + mCreator + "\n" +
                "mRecoveryKey=" + mRecoveryKey + "\n" +
                "mOwner=" + mOwner + "\n" +
                '}';
    }
}