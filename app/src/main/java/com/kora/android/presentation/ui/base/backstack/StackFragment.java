package com.kora.android.presentation.ui.base.backstack;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kora.android.R;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.MainActivity;

public abstract class StackFragment<P extends BasePresenter> extends BaseFragment<P> {

    private Toolbar mToolbar;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mToolbar = getToolbar();
        if (mToolbar != null) {
            setTitle();
            if (getBaseActivity() instanceof MainActivity) {
                mDrawerLayout = ((MainActivity) getBaseActivity()).getDrawerLayout();
            }
            setupDrawerToggle();
        }
    }

    private void setTitle() {
        int titleRes = getTitle();
        if (titleRes == -1) titleRes = R.string.empty_string;
        getBaseActivity().setTitle(titleRes);
    }

    protected void setTitle(final String title) {
        getBaseActivity().setTitle(title);
    }

    protected void setTitle(@StringRes final int titleRes) {
        getBaseActivity().setTitle(titleRes);
    }

    @StringRes
    protected int getTitle() {
        return -1;
    }

    private void setupDrawerToggle() {
        getBaseActivity().setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(getBaseActivity(), mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerToggle.setToolbarNavigationClickListener(v -> {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mDrawerToggle != null)
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    protected Toolbar getToolbar() {
        return null;
    }

}
