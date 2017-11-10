package com.kora.android.domain.base;

import io.reactivex.annotations.NonNull;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.kora.android.R;

import org.web3j.crypto.CipherException;

import java.io.FileNotFoundException;

public abstract class DefaultWeb3jSubscriber<T> extends DefaultDisposableObserver<T> {

    @CallSuper
    @Override
    public void onError(@NonNull final Throwable throwable) {
        final Exception exception = (Exception) throwable;

        if (exception instanceof FileNotFoundException) {
            handleWalletError((FileNotFoundException) exception);
        } else if (exception instanceof CipherException) {
            handlePinError((CipherException) exception);
        } else {
            handleWeb3jError(exception.getMessage());
        }
    }

    public void handleWalletError(final FileNotFoundException fileNotFoundException) {

    }

    public void handlePinError(final CipherException cipherException) {

    }

    public void handleWeb3jError(final String message) {

    }
}