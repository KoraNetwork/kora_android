package com.kora.android.di.module;

import com.kora.android.presentation.service.BaseService;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private final BaseService mService;

    public ServiceModule(final BaseService service) {
        mService = service;
    }

    @Provides
    BaseService provideService() {
        return mService;
    }
}
