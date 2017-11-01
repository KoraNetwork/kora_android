package com.kora.android.presentation.service;

public class BaseServicePresenter<V extends BaseServiceContractor> {

    private V mMvpService;

    public void attachService(final V service) {
        mMvpService = service;
    }

    public V getService() {
        return mMvpService;
    }

    public boolean isServiceAttached() {
        return mMvpService != null;
    }

    public void detachService() {
        onDetachService();
        if (mMvpService != null)
            mMvpService = null;
    }

    public void onDetachService() {

    }
}
