package com.kora.android.presentation.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements WelcomeView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent getLaunchIntent(BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_black);
    }
}