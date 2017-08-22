package com.kora.android.injection.module;

import com.kora.android.presentation.ui.base.view.BaseActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity mActivity;

    public ActivityModule(final BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    BaseActivity provideActivity() {
        return mActivity;
    }
}