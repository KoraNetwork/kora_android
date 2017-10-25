package com.kora.android.presentation.ui.main.fragments.request;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.adapter.filter.OnFilterListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.common.recent.RecentActivity;
import com.kora.android.presentation.ui.common.send_to.SendMoneyActivity;
import com.kora.android.presentation.ui.main.fragments.request.adapter.RequestAdapter;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterDialog;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;
import com.kora.android.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RequestFragment extends StackFragment<RequestPresenter> implements RequestView,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnFilterListener<RequestFilterModel> {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.request_list) RecyclerView mRequestList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;

    private RequestAdapter mRequestAdapter;
    private RequestFilterDialog mRequestFilterDialog;

    public static BaseFragment getNewInstance() {
        return new RequestFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_request;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitle() {
        return R.string.request_money_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRequestAdapter = new RequestAdapter(this);
        mRequestList.setLayoutManager(layoutManager);
        mRequestList.setAdapter(mRequestAdapter);
        mRequestList.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRequestList.getContext(), R.drawable.list_divider);
        mRequestList.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            getPresenter().retrieveRequestListWithFilter(0);
        } else {
            ArrayList<RequestEntity> requests = savedInstanceState.getParcelableArrayList(Keys.Args.REQUEST_LIST);
            mRequestAdapter.addItems(requests);
            RequestFilterModel filter = savedInstanceState.getParcelable(Keys.Args.REQUEST_FILTER);
            getPresenter().setFilter(filter);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.request_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                getPresenter().onFilterClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.floating_button_create_request)
    public void onClickCreateRequest() {
        startActivity(RecentActivity.getLaunchIntent(getBaseActivity(), ActionType.CREATE_REQUEST));
    }

    @Override
    public void onRefresh() {
        mRequestAdapter.clearAll();
        mScrollListener.resetParams();
        getPresenter().retrieveRequestListWithFilter(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Keys.Args.REQUEST_LIST, (ArrayList<RequestEntity>) mRequestAdapter.getItems());
        outState.putParcelable(Keys.Args.REQUEST_FILTER, getPresenter().getFilter());
    }

    @Override
    public void enableAndShowRefreshIndicator(boolean enableIndicator, boolean showIndicator) {
        swipeRefreshLayout.setEnabled(enableIndicator);
        swipeRefreshLayout.setRefreshing(showIndicator);
    }

    @Override
    public void openFilterDialog() {
        if (mRequestFilterDialog == null) {
            mRequestFilterDialog = RequestFilterDialog.newInstance(new RequestFilterModel());
            mRequestFilterDialog.setOnFilterListener(this);
        }  else if (mRequestFilterDialog.isShowing()) {
            mRequestFilterDialog.dismiss();
        }
        mRequestFilterDialog.show(getActivity().getSupportFragmentManager());
    }

    private final RecyclerViewScrollListener mScrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().retrieveRequestListWithFilter(mRequestAdapter.getItemCount());
        }
    };

    @Override
    public void showRequests(List<RequestEntity> requestEntities) {
        mRequestAdapter.addItems(requestEntities);
    }

    @Override
    public void onItemClicked(int position) {
        RequestEntity request = mRequestAdapter.getItemByPosition(position);
        startActivity(SendMoneyActivity.getLaunchIntent(getBaseActivity(), request));
    }

    @Override
    public void onResume() {
        super.onResume();
        mRequestList.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRequestList.removeOnScrollListener(mScrollListener);
    }

    @Override
    public void onCancelFilter() {

    }

    @Override
    public void onFilterChanged(RequestFilterModel requestFilterModel) {
        mRequestAdapter.clearAll();
        getPresenter().retrieveRequestList(requestFilterModel, 0);
    }
}
