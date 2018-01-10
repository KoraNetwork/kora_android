package com.kora.android.presentation.ui.main.fragments.send;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.base.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.common.send_request.RequestDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTextChanged;

public class SendFragment extends StackFragment<SendPresenter> implements SendView,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    AppCompatEditText mEtSearch;
    @BindView(R.id.recycler_view_users)
    RecyclerView mRvUsers;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSrlRefresh;

    private UserAdapter mUserAdapter;
    private Handler mHandler = new Handler();

    public static BaseFragment getNewInstance() {
        return new SendFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_get_contact;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitle() {
        return R.string.send_money_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        initUI();

        if (savedInstanceState == null) {
            getPresenter().getUsers();
        } else {
            final List<UserEntity> users = savedInstanceState.getParcelableArrayList(Keys.Args.USER_LIST);
            mUserAdapter.addUsers(users);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Keys.Args.USER_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    private void initUI() {
        mUserAdapter = new UserAdapter();
        mUserAdapter.setOnUserClickListener(this);

        mSrlRefresh.setOnRefreshListener(this);
        mRvUsers.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
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
                mUserAdapter.addUser(new UserEntity.Section(getString(R.string.add_contact_all_contacts_title)));
            mUserAdapter.addUsers(pair.second);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable, 500);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mRvUsers.addOnScrollListener(mRecyclerViewScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRvUsers.removeOnScrollListener(mRecyclerViewScrollListener);
    }

    private final RecyclerViewScrollListener mRecyclerViewScrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore(final int totalItemsCount) {
            getPresenter().getUsers(mUserAdapter.getRawItemsCount());
        }
    };

    private Runnable runnable = () -> {
        mRecyclerViewScrollListener.resetParams();
        final String search = mEtSearch.getText().toString().trim();
        getPresenter().setSearch(search);
        mUserAdapter.clearAll();
        getPresenter().getUsers();
    };

    @Override
    public void onItemClicked(final int position) {
        getPresenter().setAsRecent(mUserAdapter.getItem(position));
        startActivity(RequestDetailsActivity.getLaunchIntent(getBaseActivity(),
                mUserAdapter.getItem(position),
                ActionType.SEND_MONEY));
    }

    @Override
    public void onRefresh() {
        mUserAdapter.clearAll();
        mSrlRefresh.setRefreshing(false);
        getPresenter().getUsers();
    }
}
