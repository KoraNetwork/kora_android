package com.kora.android.presentation.ui.splash;

import android.os.Bundle;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.home.HomeActivity;
import com.kora.android.presentation.ui.login.LoginActivity;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        getPresenter().startSplashTask();
    }

    @Override
    public void showNextScreen(final boolean isSessionTokenEmpty) {
//        if (isSessionTokenEmpty)
//            startActivity(WelcomeActivity.getLaunchIntent(this));
//        else
//        startActivity(MainActivity.getLaunchIntent(this));

//        startActivity(FirstStepActivity.getLaunchIntent(this));

//        startActivity(ThirdStepActivity.getLaunchIntent(this));

//        startActivity(FourthStepActivity.getLaunchIntent(this));

        startActivity(LoginActivity.getLaunchIntent(this));

//        startActivity(HomeActivity.getLaunchIntent(this));
    }
}
