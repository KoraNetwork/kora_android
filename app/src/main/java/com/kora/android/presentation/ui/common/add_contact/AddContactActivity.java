package com.kora.android.presentation.ui.common.add_contact;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.common.send_to.RequestDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Args.ACTION_TYPE;

public class AddContactActivity extends ToolbarActivity<AddContactPresenter> implements AddContactView,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    AppCompatEditText mEtSearch;
    @BindView(R.id.recycler_view_users)
    RecyclerView mRvUsers;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    public UserAdapter mUserAdapter;

    private ActionType mActionType;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, AddContactActivity.class);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_contact;
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
            getPresenter().startGetUsersTask(0, true);
        } else {
            ArrayList<UserEntity> users = savedInstanceState.getParcelableArrayList(Keys.Args.USER_LIST);
            mUserAdapter.addUsers(users);
        }
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(ACTION_TYPE))
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
        mUserAdapter = new UserAdapter();
        mUserAdapter.setOnUserClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
        mRvUsers.setAdapter(mUserAdapter);
        mRvUsers.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showUsers(final List<UserEntity> userEntityList, final boolean clearList) {
        if (clearList)
            mUserAdapter.clearAll();
        mUserAdapter.addUsers(userEntityList);
        mRvUsers.invalidate();
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
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().startGetUsersTask(mUserAdapter.getItemCount(), true);
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
        getPresenter().startGetUsersTask(mUserAdapter.getItemCount(), false);
    };

    @Override
    public void onItemClicked(int position) {
        startActivity(RequestDetailsActivity.getLaunchIntent(this,
                mUserAdapter.getItem(position), mActionType));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getPresenter().startGetUsersTask(0, false);
    }
}
