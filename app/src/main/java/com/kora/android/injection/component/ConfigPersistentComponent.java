package com.kora.android.injection.component;

import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.injection.module.ActivityModule;
import com.kora.android.injection.module.FragmentModule;

import dagger.Component;

@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(final ActivityModule activityModule);

    FragmentComponent fragmentComponent(final FragmentModule fragmentModule);
}
