package com.kora.android.domain.base;

import android.support.annotation.CallSuper;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.enumclass.Kind;
import com.kora.android.data.network.exception.RetrofitException;

import java.io.IOException;

import io.reactivex.annotations.NonNull;

public abstract class DefaultInternetSubscriber<T> extends DefaultDisposableObserver<T> {

    @CallSuper
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
        } else {
            throwable.printStackTrace();
        }
    }

    private void handleHttpError(final RetrofitException retrofitException) {
        ErrorModel parsedError;
        switch (retrofitException.getResponse().code()) {
            case 401:
                handleUnauthorizedException();
                break;
            case 422:
                parsedError = parseError(retrofitException);
                if (parsedError == null) return;
                if (parsedError.getAttributes() != null
                        && parsedError.getAttributes().getAttributeName() != null
                        && parsedError.getAttributes().getAttributeName().size() > 0) {
                    parsedError.setError(parsedError.getAttributes().getAttributeName().get(0).getError());
                }
                handleUnprocessableEntity(parsedError);
                break;
            case 500:
            case 504:
                handleInternalServerError();
                break;
        }
    }

    private ErrorModel parseError(final RetrofitException exception) {
        try {
            return exception.getErrorBodyAs(ErrorModel.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void handleUnprocessableEntity(final ErrorModel errorModel) {
    }

    public void handleNetworkError(final RetrofitException retrofitException) {
    }

    public void handleUnauthorizedException() {
    }

    public void handleUnexpectedError(final RetrofitException retrofitException) {
    }

    public void handleInternalServerError() {
    }
}
