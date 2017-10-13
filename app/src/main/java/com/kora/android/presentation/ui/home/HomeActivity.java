package com.kora.android.presentation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_view_flag)
    ImageView mIvFlag;
    @BindView(R.id.text_view_currency_balance)
    TextView mTvCurrencyBalance;
    @BindView(R.id.text_view_currency_name)
    TextView mTvCurrencyName;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_menu_white);

        getPresenter().startGetBalanceTask();
    }

    @Override
    public void showFlag(final String flagLink) {
        Glide.with(this)
                .load(API_BASE_URL + flagLink)
                .into(mIvFlag);
    }

    @Override
    public void showBalance(final String balance) {
        mTvCurrencyBalance.setText(balance);
    }

    @Override
    public void showCurrencyName(final String currencyName) {
        mTvCurrencyName.setText(currencyName);
    }
}