package com.kora.android.presentation.ui.base.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import butterknife.BindView;

public abstract class ToolbarActivity<P extends BasePresenter> extends BaseActivity<P> implements BaseActivityView<P> {

    private Toolbar mToolbar;

    @Nullable
    @BindView(R.id.title)
    TextView mTitle;

    @CallSuper
    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mToolbar = getToolbar();
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setNavigationIcon(getNavigationIcon());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("");
            setTitle();
        }
    }

    private void setTitle() {
        int titleRes = getTitleRes();
        if (titleRes <= 0) titleRes = R.string.empty_string;
        if (mTitle != null)
            mTitle.setText(titleRes);

    }

    @Override
    public void setTitle(@StringRes int titleRes) {
        if (titleRes == -1) titleRes = R.string.empty_string;
        if (mTitle != null)
            mTitle.setText(titleRes);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title == null) title = "";
        if (mTitle != null)
            mTitle.setText(title);
    }

    protected abstract Toolbar getToolbar();

    @StringRes
    protected abstract int getTitleRes();

    @DrawableRes
    protected int getNavigationIcon() {
        return R.drawable.ic_back_white;
    }
}