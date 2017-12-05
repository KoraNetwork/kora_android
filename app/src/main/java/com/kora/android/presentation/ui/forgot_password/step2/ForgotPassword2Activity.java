package com.kora.android.presentation.ui.forgot_password.step1;

import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;

public class ForgotPassword1Activity extends ToolbarActivity<ForgotPassword1Presenter> implements ForgotPassword1View {

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_forgot_password_1;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    protected int getTitleRes() {
        return 0;
    }
}
