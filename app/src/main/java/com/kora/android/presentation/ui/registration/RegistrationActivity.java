package com.kora.android.presentation.ui.registration;

import android.content.Intent;
import android.os.Bundle;

import com.kora.android.R;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

public class RegistrationActivity extends BaseActivity<RegistrationPresenter> implements RegistrationView {

    public static Intent getLaunchIntent(BaseActivity baseActivity) {
        return new Intent(baseActivity, RegistrationActivity.class);
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

    }
}
