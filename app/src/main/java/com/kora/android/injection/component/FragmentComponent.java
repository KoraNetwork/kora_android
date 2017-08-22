package com.kora.android.injection.component;

import com.kora.android.injection.annotation.PerFragment;
import com.kora.android.injection.module.FragmentModule;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}