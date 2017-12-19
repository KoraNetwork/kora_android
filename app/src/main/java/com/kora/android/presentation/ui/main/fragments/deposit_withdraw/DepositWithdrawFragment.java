package com.kora.android.presentation.ui.main.fragments.deposit_withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.kora.android.presentation.enums.DepositWithdrawRole;
import com.kora.android.presentation.enums.DepositWithdrawState;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.adapter.filter.OnFilterListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.common.add_contact.GetContactActivity;
import com.kora.android.presentation.ui.common.deposit_withdraw.DepositWithdrawDetailsActivity;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.adapter.DepositWithdrawAdapter;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter.DepositWithdrawFilterDialog;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter.DepositWithdrawFilterModel;
import com.kora.android.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.kora.android.common.Keys.Args.DEPOSIT_WITHDRAW_ROLE;
import static com.kora.android.common.Keys.Args.DEPOSIT_FILTER;
import static com.kora.android.common.Keys.Args.DEPOSIT_LIST;
import static com.kora.android.common.Keys.Extras.EXTRA_ACTION;
import static com.kora.android.common.Keys.Extras.EXTRA_DEPOSIT_ENTITY;

public class DepositWithdrawFragment extends StackFragment<DepositWithdrawPresenter> implements DepositWithdrawView,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnFilterListener<DepositWithdrawFilterModel> {

    public static final int DEPOSIT_WITHDRAW_CREATE = 888;
    public static final int DEPOSIT_WITHDRAW_DETAILS = 999;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_deposit_withraw_list)
    RecyclerView mRvDepositWithdrawList;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.floating_button_create_deposit_withdraw)
    FloatingActionButton fabCreateDepositWithdraw;
    @BindView(R.id.relative_layout_placeholder)
    RelativeLayout mRlPlaceholder;


    private DepositWithdrawAdapter mDepositWithdrawAdapter;
    private DepositWithdrawFilterDialog mDepositWithdrawFilterDialog;

    public static BaseFragment getNewInstance(final DepositWithdrawRole depositWithdrawRole) {
        final DepositWithdrawFragment depositFragment = new DepositWithdrawFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(DEPOSIT_WITHDRAW_ROLE, depositWithdrawRole);
        depositFragment.setArguments(bundle);
        return depositFragment;
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_deposit;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitle() {
        switch (getPresenter().getRole()) {
            case DEPOSIT_USER:
                return R.string.deposit_user_title;
            case DEPOSIT_AGENT:
                return R.string.deposit_agent_title;
            case WITHDRAW_USER:
                return R.string.withdraw_user_title;
            case WITHDRAW_AGENT:
                return R.string.withdraw_agent_title;
        }
        return -1;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        initArguments(savedInstanceState);
        initUI();

        if (savedInstanceState == null) {
            getPresenter().retrieveDepositListWithFilter(0);
        } else {
            final List<DepositWithdrawEntity> depositWithdrawEntityList = savedInstanceState.getParcelableArrayList(DEPOSIT_LIST);
            mDepositWithdrawAdapter.addItems(depositWithdrawEntityList);
            final DepositWithdrawFilterModel depositWithdrawFilterModel = savedInstanceState.getParcelable(DEPOSIT_FILTER);
            getPresenter().setFilter(depositWithdrawFilterModel);
        }
    }

    private void initArguments(final Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().containsKey(DEPOSIT_WITHDRAW_ROLE))
                getPresenter().setRole((DepositWithdrawRole) getArguments().getSerializable(DEPOSIT_WITHDRAW_ROLE));
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(DEPOSIT_WITHDRAW_ROLE))
                getPresenter().setRole((DepositWithdrawRole) getArguments().getSerializable(DEPOSIT_WITHDRAW_ROLE));
        }
    }

    private void initUI() {
        mDepositWithdrawAdapter = new DepositWithdrawAdapter(this);
        mRvDepositWithdrawList.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        mRvDepositWithdrawList.setAdapter(mDepositWithdrawAdapter);
        mRvDepositWithdrawList.setItemAnimator(new DefaultItemAnimator());
        mRvDepositWithdrawList.addItemDecoration(new DividerItemDecoration(mRvDepositWithdrawList.getContext(), R.drawable.list_divider));

        srlRefresh.setOnRefreshListener(this);

        switch (getPresenter().getRole()) {
            case DEPOSIT_USER:
            case WITHDRAW_AGENT:
                fabCreateDepositWithdraw.setVisibility(View.VISIBLE);
                break;
            case DEPOSIT_AGENT:
            case WITHDRAW_USER:
                fabCreateDepositWithdraw.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_deposit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                getPresenter().onFilterClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.floating_button_create_deposit_withdraw)
    public void onClickCreateDeposit() {
        switch (getPresenter().getRole()) {
            case DEPOSIT_USER:
                startActivityForResult(
                        GetContactActivity.getLaunchIntent(
                                getBaseActivity(),
                                getString(R.string.deposit_user_title),
                                true),
                        DEPOSIT_WITHDRAW_CREATE);
                break;
            case WITHDRAW_AGENT:
                startActivityForResult(
                        GetContactActivity.getLaunchIntent(
                                getBaseActivity(),
                                getString(R.string.withdraw_agent_title),
                                false),
                        DEPOSIT_WITHDRAW_CREATE);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mDepositWithdrawAdapter.clearAll();
        mScrollListener.resetParams();
        getPresenter().retrieveDepositListWithFilter(0);
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DEPOSIT_WITHDRAW_ROLE, getPresenter().getRole());
        outState.putParcelableArrayList(DEPOSIT_LIST, (ArrayList<DepositWithdrawEntity>) mDepositWithdrawAdapter.getItems());
        outState.putParcelable(DEPOSIT_FILTER, getPresenter().getFilter());
    }

    @Override
    public void enableAndShowRefreshIndicator(boolean enableIndicator, boolean showIndicator) {
        srlRefresh.setEnabled(enableIndicator);
        srlRefresh.setRefreshing(showIndicator);
    }

    @Override
    public void openFilterDialog() {
        if (mDepositWithdrawFilterDialog == null) {
            mDepositWithdrawFilterDialog = DepositWithdrawFilterDialog.newInstance(new DepositWithdrawFilterModel());
            mDepositWithdrawFilterDialog.setOnFilterListener(this);
        } else if (mDepositWithdrawFilterDialog.isShowing()) {
            mDepositWithdrawFilterDialog.dismiss();
        }
        mDepositWithdrawFilterDialog.show(getBaseActivity().getSupportFragmentManager());
    }

    private final RecyclerViewScrollListener mScrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().retrieveDepositListWithFilter(mDepositWithdrawAdapter.getItemCount());
        }
    };

    @Override
    public void showDepositWithdrawList(final List<DepositWithdrawEntity> depositWithdrawEntityList) {
        if (depositWithdrawEntityList.isEmpty() && mDepositWithdrawAdapter.getItemCount() == 0) {
            mRlPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mRlPlaceholder.setVisibility(View.GONE);
            mDepositWithdrawAdapter.addItems(depositWithdrawEntityList);
        }
    }

    @Override
    public void onItemClicked(final int position) {
        final DepositWithdrawEntity depositWithdrawEntity = mDepositWithdrawAdapter.getItemByPosition(position);
        switch (getPresenter().getRole()) {
            case DEPOSIT_USER:
            case DEPOSIT_AGENT:
                startActivityForResult(
                        DepositWithdrawDetailsActivity.getLaunchIntent(
                                getBaseActivity(),
                                depositWithdrawEntity,
                                ActionType.SHOW_DEPOSIT),
                        DEPOSIT_WITHDRAW_DETAILS);
                break;
            case WITHDRAW_USER:
            case WITHDRAW_AGENT:
                startActivityForResult(
                        DepositWithdrawDetailsActivity.getLaunchIntent(
                                getBaseActivity(),
                                depositWithdrawEntity,
                                ActionType.SHOW_WITHDRAW),
                        DEPOSIT_WITHDRAW_DETAILS);
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        final Action action = (Action) data.getSerializableExtra(EXTRA_ACTION);
        switch (requestCode) {
            case DEPOSIT_WITHDRAW_DETAILS:
                if (action == Action.UPDATE) {
                    final DepositWithdrawEntity depositWithdrawEntity = data.getParcelableExtra(EXTRA_DEPOSIT_ENTITY);
                    mDepositWithdrawAdapter.changeItemState(depositWithdrawEntity);
                } else if (action == Action.DELETE) {
                    final DepositWithdrawEntity depositWithdrawEntity = data.getParcelableExtra(EXTRA_DEPOSIT_ENTITY);
                    depositWithdrawEntity.setState(DepositWithdrawState.INPROGRESS);
                    if (mDepositWithdrawAdapter.getItemCount() == 0)
                        mRlPlaceholder.setVisibility(View.GONE);
                    mDepositWithdrawAdapter.addItem(depositWithdrawEntity);
                    mRvDepositWithdrawList.scrollToPosition(0);
                }
                break;
            case DEPOSIT_WITHDRAW_CREATE:
                if (action == Action.CREATE) {
                    final UserEntity userEntity = data.getParcelableExtra(Keys.Extras.EXTRA_USER);
                    switch (getPresenter().getRole()) {
                        case DEPOSIT_USER:
                            startActivityForResult(
                                    DepositWithdrawDetailsActivity.getLaunchIntent(
                                            getBaseActivity(),
                                            userEntity,
                                            ActionType.CREATE_DEPOSIT),
                                    DEPOSIT_WITHDRAW_DETAILS);
                            break;
                        case WITHDRAW_AGENT:
                            startActivityForResult(
                                    DepositWithdrawDetailsActivity.getLaunchIntent(
                                            getBaseActivity(),
                                            userEntity,
                                            ActionType.CREATE_WITHDRAW),
                                    DEPOSIT_WITHDRAW_DETAILS);
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRvDepositWithdrawList.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRvDepositWithdrawList.removeOnScrollListener(mScrollListener);
    }

    @Override
    public void onCancelFilter() {

    }

    @Override
    public void onFilterChanged(final DepositWithdrawFilterModel depositWithdrawFilterModel) {
        mDepositWithdrawAdapter.clearAll();
        mScrollListener.resetParams();
        getPresenter().retrieveDepositList(depositWithdrawFilterModel, 0);
    }
}
