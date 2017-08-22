package com.kora.android.domain.usecase.splash;

import com.kora.android.domain.base.AsyncUseCase;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;

public class SplashUseCase extends AsyncUseCase<DisposableCompletableObserver, Completable> {

    private final static int DELAY = 1000;

    @Inject
    public SplashUseCase() {

    }

    @Override
    protected Completable buildTask() {
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS);
    }
}
