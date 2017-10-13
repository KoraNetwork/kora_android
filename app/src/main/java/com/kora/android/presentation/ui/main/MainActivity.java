package com.kora.android.presentation.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    public static final int TAB_INVALID_POSITION = -1;
    public static final int TAB_HOME_POSITION = 0;
    public static final int TAB_SEND_MONEY_POSITION = 1;
    public static final int TAB_REQUEST_MONEY_POSITION = 2;
    public static final int TAB_BORROW_MONEY_POSITION = 3;
    public static final int TAB_DEPOSIT_POSITION = 4;
    public static final int TAB_WITHDRAWAL_POSITION = 5;
    public static final int TAB_TRANSACTIONS_HISTORY_POSITION = 6;
    public static final int TAB_USER_PROFILE_POSITION = 7;
    public static final int TAB_SEND_A_FEEDBACK_POSITION = 8;

    public static Intent getLaunchIntent(final Context context) {
        return getLaunchIntent(context, TAB_HOME_POSITION);
    }

    public static Intent getLaunchIntent(final Context context,
                                         final int currentTab) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Keys.Extras.EXTRA_CURRENT_TAB, currentTab);
        return intent;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public static Intent getLaunchIntent(final BaseActivity context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        setupDrawer();

    }

    private void setupDrawer() {

        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mFilterNavigationView);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                getPresenter().loadCounters();
            }
        });

        NavigationMenuView navMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}