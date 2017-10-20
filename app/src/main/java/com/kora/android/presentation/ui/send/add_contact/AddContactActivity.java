package com.kora.android.presentation.ui.send.add_contact;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.send.add_contact.adapter.RecyclerViewScrollListener;
import com.kora.android.presentation.ui.send.add_contact.adapter.UserAdapter;
import com.kora.android.presentation.ui.send.send.SendMoneyActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AddContactActivity extends BaseActivity<AddContactPresenter> implements AddContactView, UserAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    EditText mEtSearch;
    @BindView(R.id.recycler_view_users)
    RecyclerView mRvUsers;

    @Inject
    public UserAdapter mUserAdapter;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, AddContactActivity.class);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_contact ;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_white);

        initUI();
        getPresenter().startGetUsersTask(mUserAdapter.getItemCount(), true);
    }

    private void initUI() {
        mRvUsers.setHasFixedSize(true);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
        mRvUsers.setAdapter(mUserAdapter);

        mUserAdapter.setOnItemClickListener(this);

        mEtSearch.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void showUsers(final List<UserEntity> userEntityList, final boolean clearList) {
        if (clearList)
            mUserAdapter.clearAll();
        mUserAdapter.addAll(userEntityList);
    }

    @Override
    public void onClickSelectUser(final UserEntity userEntity, final int position) {
        startActivity(SendMoneyActivity.getLaunchIntent(this, userEntity));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRvUsers.setOnScrollListener(mRecyclerViewScrollListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRvUsers.setOnScrollListener(null);
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

    private TextWatcher mTextWatcher = new TextWatcher() {

        private Handler handler = new Handler();
        private Runnable runnable;

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            handler.removeCallbacks(runnable);
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            runnable = () -> {
                mRecyclerViewScrollListener.resetParams();
                final String search = editable.toString().trim();
                getPresenter().setSearch(search);
                getPresenter().startGetUsersTask(mUserAdapter.getItemCount(), false);
            };
            handler.postDelayed(runnable, 500);
        }
    };
}
