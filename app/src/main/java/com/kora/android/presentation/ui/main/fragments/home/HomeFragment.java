package com.kora.android.presentation.ui.main.fragments.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.adapter.TransactionAdapter;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.main.fragments.transactions.TransactionsFragment;
import com.kora.android.views.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class HomeFragment extends StackFragment<HomePresenter> implements HomeView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_view_flag)
    ImageView mIvFlag;
    @BindView(R.id.text_view_currency_balance)
    TextView mTvCurrencyBalance;
    @BindView(R.id.text_view_currency_name)
    TextView mTvCurrencyName;
    @BindView(R.id.recycler_view_transactions)
    RecyclerView mRvTransactions;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSlRefresh;
    @BindView(R.id.card_view_show_all_transactions)
    CardView mCvShowTransactions;

    private TransactionAdapter mTransactionAdapter;

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

        initUI();
        getPresenter().startGetUserTask();
    }

    private void initUI() {
        mTransactionAdapter = new TransactionAdapter(null);

        mRvTransactions.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        mRvTransactions.setAdapter(mTransactionAdapter);
        mRvTransactions.setItemAnimator(new DefaultItemAnimator());
        mRvTransactions.addItemDecoration(new DividerItemDecoration(
                mRvTransactions.getContext(),
                R.drawable.list_divider));

        mSlRefresh.setOnRefreshListener(this);
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

    @Override
    public void showTransactions(final List<TransactionEntity> transactionEntityList) {
        if (transactionEntityList != null && !transactionEntityList.isEmpty()) {
            mRvTransactions.setVisibility(View.VISIBLE);
            mTransactionAdapter.clearAll();
            mTransactionAdapter.addItems(transactionEntityList);
        }
    }

    @Override
    public void enableAndShowRefreshIndicator(final boolean enableIndicator,
                                              final boolean showIndicator) {
        mSlRefresh.setEnabled(enableIndicator);
        mSlRefresh.setRefreshing(showIndicator);
    }

    @Override
    public void onRefresh() {
        getPresenter().startGetUserTask();
    }

    @OnClick(R.id.card_view_show_all_transactions)
    public void onClickShowTransactions() {
        getNavigator().showFragment(TransactionsFragment.getNewInstance(), MainActivity.TAB_TRANSACTIONS_HISTORY_POSITION);
        selectHostById(MainActivity.TAB_TRANSACTIONS_HISTORY_POSITION);
    }
}