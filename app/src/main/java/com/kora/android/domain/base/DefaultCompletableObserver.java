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
    public void onError(@NonNull final Throwable e) {
        if (e instanceof RetrofitException) {
            RetrofitException retrofitException = (RetrofitException) e;
            if (retrofitException.getKind() == Kind.NETWORK) {
                handleNetworkError(retrofitException);
            } else if (retrofitException.getKind() == Kind.HTTP) {
                handleHttpError(retrofitException);
            } else if (retrofitException.getKind() == Kind.UNEXPECTED) {
                handleUnexpectedError(retrofitException);
            }
        }
    }

    private void handleHttpError(final RetrofitException error) {
        switch (error.getResponse().code()) {
            case 401:
                handleUnauthorizedException();
                break;
            case 504:
                handleNetworkError(error);
                break;
            case 500:
                break;
        }
    }

    public void handleNetworkError(RetrofitException retrofitException) {

    }

    public void handleUnauthorizedException() {
        // TODO: redirect to start screen and clear session (mPreferencesHelper)
    }

    public void handleUnexpectedError(final RetrofitException exception) {
        // TODO: Something went wrong...
    }
}