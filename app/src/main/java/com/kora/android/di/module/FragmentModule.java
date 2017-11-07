package com.kora.android.di.module;

import com.kora.android.presentation.ui.base.view.BaseFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private final BaseFragment mFragment;

    public FragmentModule(final BaseFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    BaseFragment providesFragment() {
        return mFragment;
    }
}
