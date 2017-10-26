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
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.common.add_contact.AddContactActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Extras.TITLE;

public class RecentActivity extends ToolbarActivity<RecentPresenter> implements RecentView,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.user_list) RecyclerView mUserList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.text_add_new_contact) TextView mAddContactButton;

    private UserAdapter mUserAdapter;

    private String mTitle;

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return R.string.request_money_title;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final String title) {
        final Intent intent = new Intent(baseActivity, RecentActivity.class);
        intent.putExtra(TITLE, title);
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
        if (bundle != null && bundle.containsKey(TITLE)) {
//            setTitle(R.string.request_money_title);
            mTitle = bundle.getString(TITLE);
        }
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra(TITLE);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TITLE, mTitle);
        outState.putParcelableArrayList(Keys.Args.USER_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    private void initUI() {
        setTitle(mTitle);


        mUserAdapter = new UserAdapter();
        mUserAdapter.setOnUserClickListener(this);

        mUserList.setLayoutManager(new LinearLayoutManager(this));
        mUserList.setAdapter(mUserAdapter);
        mUserList.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showUserList(List<UserEntity> users) {
        mUserAdapter.addUsers(users);
        mUserList.invalidate();
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent();
        intent.putExtra(Keys.Extras.EXTRA_ACTION, Action.CREATE);
        intent.putExtra(Keys.Extras.EXTRA_USER, mUserAdapter.getItem(position));
        setResult(RESULT_OK, intent);
        finish();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) finish();
//    }

    @OnClick(R.id.text_add_new_contact)
    public void onAddContactClicked() {
        Intent launchIntent = AddContactActivity.getLaunchIntent(this);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(launchIntent);
        finish();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        mUserAdapter.clearAll();
        getPresenter().getUserList();
    }
}
