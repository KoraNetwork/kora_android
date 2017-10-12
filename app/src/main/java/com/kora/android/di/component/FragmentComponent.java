package com.kora.android.di.component;

import com.kora.android.di.annotation.PerFragment;
import com.kora.android.di.module.FragmentModule;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}