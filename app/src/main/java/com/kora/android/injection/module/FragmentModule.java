package com.kora.android.injection.module;

import android.support.v4.app.Fragment;

import com.kora.android.presentation.ui.base.view.BaseFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private final BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    Fragment providesFragment() {
        return mFragment;
    }
}
