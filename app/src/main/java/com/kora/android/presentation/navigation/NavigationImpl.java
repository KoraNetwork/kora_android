package com.kora.android.presentation.navigation;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;

import com.kora.android.di.annotation.PerActivity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.base.view.BaseView;

import javax.inject.Inject;

@PerActivity
public class NavigationImpl implements Navigation {

    private final BaseActivity mBaseActivity;

    @Inject
    public NavigationImpl(final BaseActivity context) {
        mBaseActivity = context;
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        final int fragmentContainerId = mBaseActivity.getFragmentContainer();

        if (fragmentContainerId == 0)
            throw new RuntimeException("Layout resource id should be provided. Check the method description");

        final FragmentManager fragmentManager = mBaseActivity.getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(fragmentContainerId, fragment)
                .commit();
    }

    @Override
    public void startActivity(Intent launchIntent) {
        mBaseActivity.startActivity(launchIntent);
    }

    @Override
    public void startActivity(Intent launchIntent, ActivityOptionsCompat activityOptions) {
        mBaseActivity.startActivity(launchIntent, activityOptions.toBundle());
    }

    @Override
    public void startActivity(Intent launchIntent, boolean finishCurrent) {
        startActivity(launchIntent);
        mBaseActivity.finish();
    }

    @Override
    public void startActivityForResult(Intent launchIntent, int requestCode) {
        mBaseActivity.startActivityForResult(launchIntent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent launchIntent, ActivityOptionsCompat activityOptions, int requestCode) {
        mBaseActivity.startActivityForResult(launchIntent, requestCode, activityOptions.toBundle());
    }

    @Override
    public void startActivityForResult(BaseView baseView, Intent launchIntent, int requestCode) {
        if (baseView instanceof BaseActivity) {
            ((BaseActivity) baseView).startActivityForResult(launchIntent, requestCode);
        } else if (baseView instanceof BaseFragment) {
            ((BaseFragment) baseView).startActivityForResult(launchIntent, requestCode);
        } else {
            startActivityForResult(launchIntent, requestCode);
        }
    }

    @Override
    public void startActivityForResult(BaseView baseView, Intent launchIntent, ActivityOptionsCompat activityOptions, int requestCode) {
        mBaseActivity.startActivity(launchIntent, activityOptions.toBundle());
        if (baseView instanceof BaseActivity) {
            ((BaseActivity) baseView).startActivityForResult(launchIntent, requestCode, activityOptions.toBundle());
        } else if (baseView instanceof BaseFragment) {
            ((BaseFragment) baseView).startActivityForResult(launchIntent, requestCode, activityOptions.toBundle());
        } else {
            startActivityForResult(launchIntent, activityOptions, requestCode);
        }
    }
}
