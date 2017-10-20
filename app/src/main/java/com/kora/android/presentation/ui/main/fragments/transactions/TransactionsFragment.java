package com.kora.android.presentation.ui.main.fragments.transactions;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.fragments.transactions.adapter.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TransactionsFragment extends StackFragment<TransactionsPresenter>
        implements TransactionsView, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.transactions_list) RecyclerView mTransactionsList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;

    private TransactionAdapter mTransactionAdapter;

    public static BaseFragment getNewInstance() {
        return new TransactionsFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_transactions;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        mTransactionAdapter = new TransactionAdapter(this);
        mTransactionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTransactionsList.setAdapter(mTransactionAdapter);
        mTransactionsList.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            getPresenter().retrieveTransactions();
        } else {
            ArrayList<TransactionEntity> transactions = savedInstanceState.getParcelableArrayList(Keys.Args.TRANSACTION_LIST);
            mTransactionAdapter.addItems(transactions);
        }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Keys.Args.TRANSACTION_LIST, (ArrayList<TransactionEntity>) mTransactionAdapter.getItems());
    }

    @Override
    public void showTransactions(List<TransactionEntity> transactionEntities) {
        mTransactionAdapter.addItems(transactionEntities);
    }
}
