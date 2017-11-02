package com.kora.android.presentation.ui.common.add_contact;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Extras.TITLE;

public class GetContactActivity extends ToolbarActivity<GetContactPresenter> implements GetContactView,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edit_text_search) AppCompatEditText mEtSearch;
    @BindView(R.id.recycler_view_users) RecyclerView mRvUsers;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;

    public UserAdapter mUserAdapter;

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final String title) {
        final Intent intent = new Intent(baseActivity, GetContactActivity.class);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_get_contact;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return R.string.add_contact_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        initArguments(savedInstanceState);
        initUI();

        if (savedInstanceState == null) {
            getPresenter().getUsers();
        } else {
            List users = savedInstanceState.getParcelableArrayList(Keys.Args.USER_LIST);
            mUserAdapter.addUsers(users);
        }
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null && bundle.containsKey(TITLE)) {
            setTitle(bundle.getString(TITLE));
        }
        if (getIntent() != null) {
            setTitle(getIntent().getStringExtra(TITLE));
        }

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, getTitle().toString());
        outState.putParcelableArrayList(Keys.Args.USER_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    private void initUI() {
        mUserAdapter = new UserAdapter();
        mUserAdapter.setOnUserClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
        mRvUsers.setAdapter(mUserAdapter);
        mRvUsers.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showUsers(final Pair<List<UserEntity>, List<UserEntity>> pair) {
        if (pair.first != null && pair.first.size() > 0) {
            mUserAdapter.addUser(new UserEntity.Section(getString(R.string.add_contact_recent_title)));
            mUserAdapter.addUsers(pair.first);
        }
        if (pair.second != null && pair.second.size() > 0) {
            int size = pair.first == null ? 0 : pair.first.size();
            if (mUserAdapter.getRawItemsCount() == size)
                mUserAdapter.addUser(new UserEntity.Section(getString(R.string.add_contact_all_title)));
            mUserAdapter.addUsers(pair.second);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRvUsers.addOnScrollListener(mRecyclerViewScrollListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRvUsers.removeOnScrollListener(mRecyclerViewScrollListener);
    }

    private final RecyclerViewScrollListener mRecyclerViewScrollListener = new RecyclerViewScrollListener() {

        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().getUsers(mUserAdapter.getRawItemsCount());
        }
    };

    private Handler handler = new Handler();

    @OnTextChanged(R.id.edit_text_search)
    public void onSearchKeyChanged() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 500);
    }

    private Runnable runnable = () -> {
        mRecyclerViewScrollListener.resetParams();
        final String search = mEtSearch.getText().toString().trim();
        getPresenter().setSearch(search);
        mUserAdapter.clearAll();
        getPresenter().getUsers();
    };

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent();
        intent.putExtra(Keys.Extras.EXTRA_ACTION, Action.CREATE);
        intent.putExtra(Keys.Extras.EXTRA_USER, mUserAdapter.getItem(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRefresh() {
        mUserAdapter.clearAll();
        swipeRefreshLayout.setRefreshing(false);
        getPresenter().getUsers();
    }
}
