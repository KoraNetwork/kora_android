package com.kora.android.di.component;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.di.module.ActivityModule;
import com.kora.android.di.module.FragmentModule;

import dagger.Component;

@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(final ActivityModule activityModule);

    FragmentComponent fragmentComponent(final FragmentModule fragmentModule);
}
