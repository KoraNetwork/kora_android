package com.kora.android.presentation.ui.base.presenter;

import com.kora.android.presentation.ui.base.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView> implements Presenter<V> {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
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
        mCompositeDisposable.clear();
        if (mMvpView != null)
            mMvpView = null;
    }

    protected void addDisposable(Disposable subscription){
        mCompositeDisposable.add(subscription);
    }
}