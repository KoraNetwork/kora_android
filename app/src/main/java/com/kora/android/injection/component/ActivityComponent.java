package com.kora.android.injection.component;

import com.kora.android.injection.annotation.PerActivity;
import com.kora.android.injection.module.ActivityModule;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.registration.step1.FirstStepActivity;
import com.kora.android.presentation.ui.registration.step2.SecondStepActivity;
import com.kora.android.presentation.ui.splash.SplashActivity;
import com.kora.android.presentation.ui.welcome.WelcomeActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);
    void inject(WelcomeActivity welcomeActivity);
    void inject(MainActivity mainActivity);

    void inject(FirstStepActivity firstStepActivity);
    void inject(SecondStepActivity secondStepActivity);
}
