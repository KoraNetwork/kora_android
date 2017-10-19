package com.kora.android.domain.base;

import io.reactivex.annotations.NonNull;

import android.support.annotation.CallSuper;

public abstract class DefaultWeb3jSubscriber<T> extends DefaultDisposableObserver<T> {

    @CallSuper
    @Override
    public void onError(@NonNull final Throwable throwable) {
        final Exception exception = (Exception) throwable;
        handleWeb3jError(exception.getMessage());
    }

    public void handleWeb3jError(final String message) {

    }
}