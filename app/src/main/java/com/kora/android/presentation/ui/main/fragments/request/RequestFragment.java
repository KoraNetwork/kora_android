package com.kora.android.presentation.ui.main.fragments.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.adapter.filter.OnFilterListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.get_contact.GetContactActivity;
import com.kora.android.presentation.ui.send_request.SendRequestDetailsActivity;
import com.kora.android.presentation.ui.main.fragments.request.adapter.RequestAdapter;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterDialog;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;
import com.kora.android.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class RequestFragment extends StackFragment<RequestPresenter> implements RequestView,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnFilterListener<RequestFilterModel> {

    private static final int REQUEST_DETAILS = 521;
    private static final int REQUEST_CREATE = 522;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.request_list) RecyclerView mRequestList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.relative_layout_placeholder) RelativeLayout mRlPlaceholder;

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
            final ArrayList<RequestEntity> requests = savedInstanceState.getParcelableArrayList(Keys.Args.REQUEST_LIST);
            showRequests(requests);
            final RequestFilterModel filter = savedInstanceState.getParcelable(Keys.Args.REQUEST_FILTER);
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
        startActivityForResult(
                GetContactActivity.getLaunchIntent(getBaseActivity(), getString(R.string.request_money_title), false),
                REQUEST_CREATE);
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
        } else if (mRequestFilterDialog.isShowing()) {
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
        if (requestEntities.isEmpty() && mRequestAdapter.getItemCount() == 0) {
            mRlPlaceholder.setVisibility(View.VISIBLE);
            mRequestList.setVisibility(View.GONE);
        } else {
            mRlPlaceholder.setVisibility(View.GONE);
            mRequestList.setVisibility(View.VISIBLE);
            mRequestAdapter.addItems(requestEntities);
        }
    }

    @Override
    public void onItemClicked(int position) {
        RequestEntity request = mRequestAdapter.getItemByPosition(position);
        startActivityForResult(SendRequestDetailsActivity.getLaunchIntent(getBaseActivity(), request), REQUEST_DETAILS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Action action = (Action) data.getSerializableExtra(Keys.Extras.EXTRA_ACTION);
        switch (requestCode) {
            case REQUEST_DETAILS:
                if (action == Action.UPDATE) {
                    RequestEntity request = data.getParcelableExtra(Keys.Extras.EXTRA_REQUEST_ENTITY);
                    mRequestAdapter.changeItemState(request);
                } else if (action == Action.DELETE) {
                    RequestEntity request = data.getParcelableExtra(Keys.Extras.EXTRA_REQUEST_ENTITY);
                    request.setState(RequestState.INPROGRESS);
                    if (mRequestAdapter.getItemCount() == 0)
                        mRlPlaceholder.setVisibility(View.GONE);
                    mRequestAdapter.addItem(request);
                    mRequestList.scrollToPosition(0);
                }
                break;

            case REQUEST_CREATE:
                if (action == Action.CREATE) {
                    UserEntity user = data.getParcelableExtra(Keys.Extras.EXTRA_USER);
                    startActivityForResult(
                            SendRequestDetailsActivity.getLaunchIntent(getBaseActivity(), user, ActionType.CREATE_REQUEST),
                            REQUEST_DETAILS);
                }
                break;
        }
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
        mScrollListener.resetParams();
        getPresenter().retrieveRequestList(requestFilterModel, 0);
    }
}
