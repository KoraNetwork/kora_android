package com.kora.android.presentation.ui.main.fragments.borrow.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.fragments.borrow.fragment.adapter.BorrowAdapter;
import com.kora.android.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BorrowFragment extends BaseFragment<BorrowPresenter> implements BorrowView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.borrow_list) RecyclerView mBorrowList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;

    private BorrowAdapter mBorrowAdapter;

    public static BaseFragment getNewInstance(final BorrowType borrowType) {
        final Bundle args = new Bundle();
        args.putSerializable(Keys.Args.ARG_BORROW_TYPE, borrowType);
        final BorrowFragment borrowFragment = new BorrowFragment();
        borrowFragment.setArguments(args);
        return borrowFragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_borrow;
    }

    @Override
    public void injectToComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        initArguments();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBorrowAdapter = new BorrowAdapter(null);
        mBorrowList.setLayoutManager(layoutManager);
        mBorrowList.setAdapter(mBorrowAdapter);
        mBorrowList.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBorrowList.getContext(), R.drawable.list_divider);
        mBorrowList.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            getPresenter().retrieveBorrowList();
        } else {
            ArrayList<BorrowEntity> entities = savedInstanceState.getParcelableArrayList(Keys.Args.BORROW_LIST);
            mBorrowAdapter.addItems(entities);
            BorrowType type = (BorrowType) savedInstanceState.getSerializable(Keys.Args.BORROW_TYPE);
            getPresenter().setBorrowType(type);
        }

    }

    private void initArguments() {
        final Bundle arguments = getArguments();
        if (arguments == null) return;
        BorrowType borrowType = (BorrowType) arguments.getSerializable(Keys.Args.ARG_BORROW_TYPE);
        getPresenter().setBorrowType(borrowType);
    }

    @Override
    public void onRefresh() {
        mBorrowAdapter.clearAll();
        mScrollListener.resetParams();
        getPresenter().retrieveBorrowList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Keys.Args.BORROW_LIST, (ArrayList<BorrowEntity>) mBorrowAdapter.getItems());
        outState.putSerializable(Keys.Args.BORROW_TYPE, getPresenter().getBorrowType());
    }

    @Override
    public void enableAndShowRefreshIndicator(boolean enableIndicator, boolean showIndicator) {
        swipeRefreshLayout.setEnabled(enableIndicator);
        swipeRefreshLayout.setRefreshing(showIndicator);
    }

    @Override
    public void showData(List<BorrowEntity> entities) {
        mBorrowAdapter.addItems(entities);
    }

    private final RecyclerViewScrollListener mScrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().retrieveBorrowList(mBorrowAdapter.getItemCount());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mBorrowList.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBorrowList.removeOnScrollListener(mScrollListener);
    }
}
