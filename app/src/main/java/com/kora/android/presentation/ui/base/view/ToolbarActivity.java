package com.kora.android.presentation.ui.base.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

public abstract class ToolbarActivity<P extends BasePresenter> extends BaseActivity<P> implements BaseActivityView<P> {

    private Toolbar mToolbar;

    @CallSuper
    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mToolbar = getToolbar();
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(getNavigationIcon());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle();
        }
    }

    private void setTitle() {
        int titleRes = getTitleRes();
        if (titleRes <= 0) titleRes = R.string.empty_string;
        setTitle(titleRes);

    }

    @Override
    public void setTitle(@StringRes int titleRes) {
        if (titleRes == -1) titleRes = R.string.empty_string;
        super.setTitle(titleRes);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title == null) title = "";
        super.setTitle(title);
    }

    protected abstract Toolbar getToolbar();

    @StringRes
    protected abstract int getTitleRes();

    @DrawableRes
    protected int getNavigationIcon() {
        return R.drawable.ic_back_white;
    }
}