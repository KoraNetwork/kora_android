package com.kora.android.domain.base;

import io.reactivex.annotations.NonNull;

import android.support.annotation.CallSuper;

import org.web3j.crypto.CipherException;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public abstract class DefaultWeb3jSubscriber<T> extends DefaultDisposableObserver<T> {

    @CallSuper
    @Override
    public void onError(@NonNull final Throwable throwable) {
        if (isThrowableNetworkException(throwable)) {
            handleNetworkError(throwable);
        } else if (throwable instanceof FileNotFoundException) {
            handleWalletError((FileNotFoundException) throwable);
        } else if (throwable instanceof CipherException) {
            handlePinError((CipherException) throwable);
        } else {
            handleWeb3jError(throwable.getMessage());
        }
    }

    public void handleNetworkError(final Throwable throwable) {

    }

    public void handleWalletError(final FileNotFoundException fileNotFoundException) {

    }

    public void handlePinError(final CipherException cipherException) {

    }

    public void handleWeb3jError(final String message) {

    }

    private boolean isThrowableNetworkException(final Throwable throwable) {
        return (throwable instanceof RuntimeException && throwable.getMessage().contains("java.net.UnknownHostException")) ||
                throwable instanceof TimeoutException ||
                throwable instanceof ConnectException ||
                throwable instanceof SocketTimeoutException ||
                throwable instanceof UnknownHostException;
    }
}