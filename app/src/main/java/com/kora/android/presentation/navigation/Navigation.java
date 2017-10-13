package com.kora.android.presentation.navigation;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;

import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface Navigation {
    void showFragment(final BaseFragment fragment);

    void startActivity(final Intent launchIntent);

    void startActivity(final Intent launchIntent, final ActivityOptionsCompat activityOptions);

    void startActivity(final Intent launchIntent, final boolean finishCurrent);

    void startActivityForResult(final Intent launchIntent, final int requestCode);

    void startActivityForResult(final Intent launchIntent, ActivityOptionsCompat activityOptions, final int requestCode);

    void startActivityForResult(final BaseView baseView, final Intent launchIntent, final int requestCode);

    void startActivityForResult(final BaseView baseView, final Intent launchIntent, final ActivityOptionsCompat activityOptions, int requestCode);
}
