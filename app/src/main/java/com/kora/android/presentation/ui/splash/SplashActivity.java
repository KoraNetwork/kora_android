package com.kora.android.presentation.ui.splash;

import android.os.Bundle;

import com.kora.android.R;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.welcome.WelcomeActivity;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().startSplashTask();
    }

    @Override
    public void showNextScreen(boolean isSessionTokenEmpty) {
//        if (isSessionTokenEmpty)
//            startActivity(WelcomeActivity.getLaunchIntent(this));
//        else
            startActivity(MainActivity.getLaunchIntent(this));

    }
}
