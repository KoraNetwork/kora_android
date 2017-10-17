package com.kora.android.di.component;

import com.kora.android.di.annotation.PerFragment;
import com.kora.android.di.module.FragmentModule;
import com.kora.android.presentation.ui.main.fragments.home.HomeFragment;
import com.kora.android.presentation.ui.main.fragments.profile.ProfileFragment;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(HomeFragment homeFragment);
    void inject(ProfileFragment profileFragment);
}