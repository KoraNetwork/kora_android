package com.kora.android.domain.base;

import com.kora.android.data.network.exception.RetrofitException;

import io.reactivex.observers.DisposableObserver;

public class DefaultDisposableObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (!(e instanceof RetrofitException)) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }
}
