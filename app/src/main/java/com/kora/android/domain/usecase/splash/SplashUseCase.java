package com.kora.android.domain.usecase.splash;

import com.kora.android.domain.base.AsyncUseCase;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SplashUseCase extends AsyncUseCase {

    private final static int DELAY = 2000;

    @Inject
    public SplashUseCase() {

    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.timer(DELAY, TimeUnit.MILLISECONDS);
    }
}
