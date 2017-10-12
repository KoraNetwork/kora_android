package com.kora.android.di.module;

import com.kora.android.presentation.ui.base.view.BaseActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity mActivity;

    public ActivityModule(final BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    BaseActivity provideActivity() {
        return mActivity;
    }
}