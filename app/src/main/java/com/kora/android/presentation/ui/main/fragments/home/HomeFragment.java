package com.kora.android.presentation.ui.main.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;

import butterknife.BindView;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class HomeFragment extends StackFragment<HomePresenter> implements HomeView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_view_flag)
    ImageView mIvFlag;
    @BindView(R.id.text_view_currency_balance)
    TextView mTvCurrencyBalance;
    @BindView(R.id.text_view_currency_name)
    TextView mTvCurrencyName;

    public static BaseFragment getNewInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void injectToComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {

        getPresenter().startGetUserTask();
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