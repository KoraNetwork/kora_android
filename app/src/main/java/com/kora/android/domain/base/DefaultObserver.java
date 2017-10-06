package com.kora.android.domain.base;

import com.kora.android.data.network.exception.RetrofitException;

import io.reactivex.observers.DisposableObserver;

public class DefaultObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(final T t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(final Throwable throwable) {
        if (!(throwable instanceof RetrofitException)) {
            throwable.printStackTrace();
        }
    }
}
