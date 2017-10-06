package com.kora.android.domain.base;

import com.kora.android.data.network.enumclass.Kind;
import com.kora.android.data.network.exception.RetrofitException;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;

public class DefaultCompletableObserver extends DisposableCompletableObserver {

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        if (throwable instanceof RetrofitException) {
            RetrofitException retrofitException = (RetrofitException) throwable;
            if (retrofitException.getKind() == Kind.NETWORK) {
                handleNetworkError(retrofitException);
            } else if (retrofitException.getKind() == Kind.HTTP) {
                handleHttpError(retrofitException);
            } else if (retrofitException.getKind() == Kind.UNEXPECTED) {
                handleUnexpectedError(retrofitException);
            }
        }
    }

    private void handleHttpError(final RetrofitException retrofitException) {
        switch (retrofitException.getResponse().code()) {
            case 401:
                handleUnauthorizedException();
                break;
            case 422:
                handleValidationException(retrofitException);
                break;
            case 504:
                handleNetworkError(retrofitException);
                break;
            case 500:
                break;
        }
    }

    public void handleValidationException(final RetrofitException retrofitException) {

    }

    public void handleNetworkError(final RetrofitException retrofitException) {

    }

    public void handleUnauthorizedException() {

    }

    public void handleUnexpectedError(final RetrofitException retrofitException) {

    }
}