package com.kora.android.presentation.ui.main.fragments.deposit;

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

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.DepositState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.adapter.filter.OnFilterListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.common.add_contact.GetContactActivity;
import com.kora.android.presentation.ui.common.deposit_withdraw.DepositDetailsActivity;
import com.kora.android.presentation.ui.main.fragments.deposit.adapter.DepositAdapter;
import com.kora.android.presentation.ui.main.fragments.deposit.filter.DepositFilterDialog;
import com.kora.android.presentation.ui.main.fragments.deposit.filter.DepositFilterModel;
import com.kora.android.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.kora.android.common.Keys.Args.DEPOSIT_DIRECTION;
import static com.kora.android.common.Keys.Args.DEPOSIT_FILTER;
import static com.kora.android.common.Keys.Args.DEPOSIT_LIST;
import static com.kora.android.common.Keys.Extras.EXTRA_ACTION;
import static com.kora.android.common.Keys.Extras.EXTRA_DEPOSIT_ENTITY;

public class DepositFragment extends StackFragment<DepositPresenter> implements DepositView,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnFilterListener<DepositFilterModel> {

    public static final int DEPOSIT_CREATE = 888;
    public static final int DEPOSIT_DETAILS = 999;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_deposits)
    RecyclerView mRvDeposits;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.floating_button_create_deposit)
    FloatingActionButton fabCreateDeposit;

    private DepositAdapter mDepositAdapter;
    private DepositFilterDialog mDepositFilterDialog;

    public static BaseFragment getNewInstance(final Direction direction) {
        final DepositFragment depositFragment = new DepositFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(DEPOSIT_DIRECTION, direction);
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
        return getPresenter().getDirection() == Direction.FROM
                ? R.string.deposit_user_title
                : R.string.deposit_agent_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        initArguments(savedInstanceState);
        initUI();

        if (savedInstanceState == null) {
            getPresenter().retrieveDepositListWithFilter(0);
        } else {
            final List<DepositEntity> depositEntityList = savedInstanceState.getParcelableArrayList(DEPOSIT_LIST);
            mDepositAdapter.addItems(depositEntityList);
            final DepositFilterModel depositFilterModel = savedInstanceState.getParcelable(DEPOSIT_FILTER);
            getPresenter().setFilter(depositFilterModel);
        }
    }

    private void initArguments(final Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().containsKey(DEPOSIT_DIRECTION))
                getPresenter().setDirection((Direction) getArguments().getSerializable(DEPOSIT_DIRECTION));
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(DEPOSIT_DIRECTION))
                getPresenter().setDirection((Direction) getArguments().getSerializable(DEPOSIT_DIRECTION));
        }
    }

    private void initUI() {
        mDepositAdapter = new DepositAdapter(this);
        mRvDeposits.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        mRvDeposits.setAdapter(mDepositAdapter);
        mRvDeposits.setItemAnimator(new DefaultItemAnimator());
        mRvDeposits.addItemDecoration(new DividerItemDecoration(mRvDeposits.getContext(), R.drawable.list_divider));

        srlRefresh.setOnRefreshListener(this);

        fabCreateDeposit.setVisibility(getPresenter().getDirection() == Direction.FROM
                ? View.VISIBLE
                : View.GONE);
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

    @OnClick(R.id.floating_button_create_deposit)
    public void onClickCreateDeposit() {
        startActivityForResult(
                GetContactActivity.getLaunchIntent(getBaseActivity(), getString(R.string.deposit_user_title),true),
                DEPOSIT_CREATE);
    }

    @Override
    public void onRefresh() {
        mDepositAdapter.clearAll();
        mScrollListener.resetParams();
        getPresenter().retrieveDepositListWithFilter(0);
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DEPOSIT_DIRECTION, getPresenter().getDirection());
        outState.putParcelableArrayList(DEPOSIT_LIST, (ArrayList<DepositEntity>) mDepositAdapter.getItems());
        outState.putParcelable(DEPOSIT_FILTER, getPresenter().getFilter());
    }

    @Override
    public void enableAndShowRefreshIndicator(boolean enableIndicator, boolean showIndicator) {
        srlRefresh.setEnabled(enableIndicator);
        srlRefresh.setRefreshing(showIndicator);
    }

    @Override
    public void openFilterDialog() {
        if (mDepositFilterDialog == null) {
            mDepositFilterDialog = DepositFilterDialog.newInstance(new DepositFilterModel());
            mDepositFilterDialog.setOnFilterListener(this);
        } else if (mDepositFilterDialog.isShowing()) {
            mDepositFilterDialog.dismiss();
        }
        mDepositFilterDialog.show(getBaseActivity().getSupportFragmentManager());
    }

    private final RecyclerViewScrollListener mScrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().retrieveDepositListWithFilter(mDepositAdapter.getItemCount());
        }
    };

    @Override
    public void showDepositList(final List<DepositEntity> depositEntityList) {
        mDepositAdapter.addItems(depositEntityList);
    }

    @Override
    public void onItemClicked(final int position) {
        final DepositEntity depositEntity = mDepositAdapter.getItemByPosition(position);
        startActivityForResult(
                DepositDetailsActivity.getLaunchIntent(getBaseActivity(), depositEntity),
                DEPOSIT_DETAILS);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        final Action action = (Action) data.getSerializableExtra(EXTRA_ACTION);
        switch (requestCode) {
            case DEPOSIT_DETAILS:
                if (action == Action.UPDATE) {
                    final DepositEntity depositEntity = data.getParcelableExtra(EXTRA_DEPOSIT_ENTITY);
                    mDepositAdapter.changeItemState(depositEntity);
                } else if (action == Action.DELETE) {
                    final DepositEntity depositEntity = data.getParcelableExtra(EXTRA_DEPOSIT_ENTITY);
                    depositEntity.setState(DepositState.INPROGRESS);
                    mDepositAdapter.addItem(depositEntity);
                    mRvDeposits.scrollToPosition(0);
                }
                break;
            case DEPOSIT_CREATE:
                if (action == Action.CREATE) {
                    final UserEntity userEntity = data.getParcelableExtra(Keys.Extras.EXTRA_USER);
                    startActivityForResult(
                            DepositDetailsActivity.getLaunchIntent(getBaseActivity(), userEntity, ActionType.CREATE_DEPOSIT),
                            DEPOSIT_DETAILS);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRvDeposits.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRvDeposits.removeOnScrollListener(mScrollListener);
    }

    @Override
    public void onCancelFilter() {

    }

    @Override
    public void onFilterChanged(final DepositFilterModel depositFilterModel) {
        mDepositAdapter.clearAll();
        mScrollListener.resetParams();
        getPresenter().retrieveDepositList(depositFilterModel, 0);
    }
}
