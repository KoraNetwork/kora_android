package com.kora.android.presentation.ui.common.recent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.common.add_contact.AddContactActivity;
import com.kora.android.presentation.ui.common.send_to.SendMoneyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.TRANSACTION_TYPE;
import static com.kora.android.presentation.enums.TransactionType.REQUEST;

public class RecentActivity extends BaseActivity<RecentPresenter> implements RecentView,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_view_toolbar_title)
    TextView mTvToolbarTitle;

    @BindView(R.id.user_list)
    RecyclerView mUserList;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.text_add_new_contact)
    TextView mAddContactButton;

    private UserAdapter mUserAdapter;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final TransactionType transactionType) {
        final Intent intent = new Intent(baseActivity, RecentActivity.class);
        intent.putExtra(TRANSACTION_TYPE, transactionType.toString());
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_recent;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_white);

        initArguments(savedInstanceState);
        initUI();

        if (savedInstanceState == null) {
            getPresenter().getUserList();
        } else {
            ArrayList<UserEntity> users = savedInstanceState.getParcelableArrayList(Keys.Args.USER_LIST);
            mUserAdapter.addUsers(users);
        }
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(TRANSACTION_TYPE))
                getPresenter().setTransactionType(bundle.getString(TRANSACTION_TYPE));
        }
        if (getIntent() != null) {
            getPresenter().setTransactionType(getIntent().getStringExtra(TRANSACTION_TYPE));
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TRANSACTION_TYPE, getPresenter().getTransactionType().toString());
        outState.putParcelableArrayList(Keys.Args.USER_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    private void initUI() {
        if (getPresenter().getTransactionType().equals(REQUEST))
            mTvToolbarTitle.setText(R.string.request_money_request_money);

        mUserAdapter = new UserAdapter();
        mUserAdapter.setOnUserClickListener(this);

        mUserList.setLayoutManager(new LinearLayoutManager(this));
        mUserList.setAdapter(mUserAdapter);
        mUserList.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showUserList(List<UserEntity> users) {
        Collections.sort(users, (o1, o2) -> o1.getFullName().compareTo(o2.getFullName()));
        mUserAdapter.addUsers(users);
        mUserList.invalidate();
    }

    @Override
    public void onItemClicked(int position) {
        startActivity(SendMoneyActivity.getLaunchIntent(this,
                mUserAdapter.getItem(position),
                getPresenter().getTransactionType()));
    }

    @OnClick(R.id.text_add_new_contact)
    public void onAddContactClicked() {
        startActivity(AddContactActivity.getLaunchIntent(this, getPresenter().getTransactionType()));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        mUserAdapter.clearAll();
        getPresenter().getUserList();
    }
}
