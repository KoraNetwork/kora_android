package com.kora.android.presentation.ui.base.presenter;

import com.kora.android.presentation.ui.base.view.BaseView;

public abstract class BasePresenter<V extends BaseView> implements Presenter<V> {

    private V mMvpView;

    @Override
    public void attachView(final V view) {
        mMvpView = view;
    }

    @Override
    public V getView() {
        return mMvpView;
    }

    @Override
    public boolean isViewAttached() {
        return mMvpView != null;
    }

    @Override
    public void detachView() {
        onDetachView();
        if (mMvpView != null)
            mMvpView = null;
    }

    @Override
    public void onDetachView() {

    }
}