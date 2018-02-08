package com.kora.android.presentation.ui.main.fragments.borrow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.enums.BorrowListType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.borrow.BorrowMoneyActivity;
import com.kora.android.presentation.ui.main.fragments.borrow.fragment.adapter.BorrowAdapter;
import com.kora.android.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.kora.android.common.Keys.Extras.BORROW_REQUEST_EXTRA;
import static com.kora.android.common.Keys.Extras.EXTRA_ACTION;

public class BorrowFragment extends StackFragment<BorrowPresenter> implements BorrowView,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private static final int BORROW_DETAILS = 225;

    @BindView(R.id.borrow_list) RecyclerView mBorrowList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.relative_layout_placeholder) RelativeLayout mRlPlaceholder;

    private BorrowAdapter mBorrowAdapter;

    public static BaseFragment getNewInstance(final BorrowListType borrowListType) {
        final Bundle args = new Bundle();
        args.putSerializable(Keys.Args.ARG_BORROW_TYPE, borrowListType);
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
        mBorrowAdapter = new BorrowAdapter(this);
        mBorrowList.setLayoutManager(layoutManager);
        mBorrowList.setAdapter(mBorrowAdapter);
        mBorrowList.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBorrowList.getContext(), R.drawable.list_divider);
        mBorrowList.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            getPresenter().retrieveBorrowList();
        } else {
            final ArrayList<BorrowEntity> entities = savedInstanceState.getParcelableArrayList(Keys.Args.BORROW_LIST);
            showData(entities);
            final BorrowListType type = (BorrowListType) savedInstanceState.getSerializable(Keys.Args.BORROW_TYPE);
            getPresenter().setBorrowType(type);
        }
    }

    private void initArguments() {
        final Bundle arguments = getArguments();
        if (arguments == null) return;
        BorrowListType borrowListType = (BorrowListType) arguments.getSerializable(Keys.Args.ARG_BORROW_TYPE);
        getPresenter().setBorrowType(borrowListType);
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
        if (entities.isEmpty() && mBorrowAdapter.getItemCount() == 0) {
            mRlPlaceholder.setVisibility(View.VISIBLE);
            mBorrowList.setVisibility(View.GONE);
        } else {
            mRlPlaceholder.setVisibility(View.GONE);
            mBorrowList.setVisibility(View.VISIBLE);
            mBorrowAdapter.addItems(entities);
        }
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

    public void addRequest(BorrowEntity borrowRequest) {
        if (borrowRequest == null) return;
//        mBorrowAdapter.addItem(0, borrowRequest);
    }

    @Override
    public void onItemClicked(int position) {
        BorrowEntity item = mBorrowAdapter.getItem(position);
        if (item == null) return;
        startActivityForResult(BorrowMoneyActivity.getLaunchIntent(getBaseActivity(), item), BORROW_DETAILS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BORROW_DETAILS && resultCode == RESULT_OK) {
            Action action = (Action) data.getSerializableExtra(EXTRA_ACTION);
            switch (action) {
                case UPDATE:
                    BorrowEntity updatedItem = data.getParcelableExtra(BORROW_REQUEST_EXTRA);
                    mBorrowAdapter.updateItem(updatedItem);
                    break;
                case DELETE:
                    BorrowEntity deletedItem = data.getParcelableExtra(BORROW_REQUEST_EXTRA);
                    mBorrowAdapter.deleteItem(deletedItem);
                    break;
            }
        }
    }
}
