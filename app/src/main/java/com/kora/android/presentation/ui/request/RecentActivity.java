package com.kora.android.presentation.ui.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;

import static com.kora.android.common.Keys.Args.TRANSACTION_TYPE;
import static com.kora.android.presentation.enums.TransactionType.REQUEST;

public class RecentActivity extends BaseActivity<RecentPresenter> implements RecentView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_view_toolbar_title)
    TextView mTvToolbarTitle;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final TransactionType transactionType) {
        final Intent intent = new Intent(baseActivity, RecentActivity.class);
        intent.putExtra(TRANSACTION_TYPE, transactionType.toString());
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_recent;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_white);

        initArguments(savedInstanceState);

        if (getPresenter().getTransactionType().equals(REQUEST))
            mTvToolbarTitle.setText(R.string.request_money_request_money);
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(TRANSACTION_TYPE))
                getPresenter().setTransactionType(bundle.getString(TRANSACTION_TYPE));
        }
        if (getIntent() != null) {
            getPresenter().setTransactionType(getIntent().getStringExtra(TRANSACTION_TYPE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TRANSACTION_TYPE, getPresenter().getTransactionType().toString());
    }
}
