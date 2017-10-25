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
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.common.add_contact.AddContactActivity;
import com.kora.android.presentation.ui.common.send_to.SendMoneyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ACTION_TYPE;

public class RecentActivity extends ToolbarActivity<RecentPresenter> implements RecentView,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.user_list) RecyclerView mUserList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.text_add_new_contact) TextView mAddContactButton;

    private UserAdapter mUserAdapter;

    private ActionType mActionType;

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return R.string.request_money_title;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, RecentActivity.class);
        intent.putExtra(ACTION_TYPE, actionType);
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
        super.onViewReady(savedInstanceState);

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
        if (bundle != null && bundle.containsKey(ACTION_TYPE)) {
            mActionType = (ActionType) bundle.getSerializable(ACTION_TYPE);
        }
        if (getIntent() != null) {
            mActionType = (ActionType) getIntent().getSerializableExtra(ACTION_TYPE);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ACTION_TYPE, mActionType);
        outState.putParcelableArrayList(Keys.Args.USER_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    private void initUI() {
        if (mActionType.equals(ActionType.CREATE_REQUEST))
            setTitle(R.string.request_money_title);

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
                mActionType));
    }

    @OnClick(R.id.text_add_new_contact)
    public void onAddContactClicked() {
        startActivity(AddContactActivity.getLaunchIntent(this, mActionType));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        mUserAdapter.clearAll();
        getPresenter().getUserList();
    }
}
