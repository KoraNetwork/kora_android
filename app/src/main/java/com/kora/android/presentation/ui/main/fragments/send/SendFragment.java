package com.kora.android.presentation.ui.main.fragments.send;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.adapter.UserAdapter;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.send.add_contact.AddContactActivity;
import com.kora.android.views.fastscroll.FastScrollRecyclerView;
import com.kora.android.views.fastscroll.FastScrollRecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SendFragment extends StackFragment<SendPresenter> implements SendView,
        UserAdapter.OnUserClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.user_list) FastScrollRecyclerView mUserList;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.text_add_new_contact) TextView mAddContactButton;

    private UserAdapter mUserAdapter;

    public static BaseFragment getNewInstance() {
        return new SendFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_send;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserAdapter = new UserAdapter();
        mUserAdapter.setOnUserClickListener(this);
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mUserList.setAdapter(mUserAdapter);
            mUserList.addItemDecoration(new FastScrollRecyclerViewItemDecoration(getActivity()));
            mUserList.setItemAnimator(new DefaultItemAnimator());
            swipeRefreshLayout.setOnRefreshListener(this);

            getPresenter().getUserList();
        } else {
            ArrayList<UserEntity> users = savedInstanceState.getParcelableArrayList(Keys.Args.USER_LIST);
            mUserAdapter.addUsers(users);
        }

    }

    @Override
    public void showUserList(List<UserEntity> users) {
        Collections.sort(users, (o1, o2) -> {
            String n1 = o1.getLegalName() == null || o1.getLegalName().length() == 0 ? o1.getUserName() == null || o1.getUserName().length() == 0 ? "" : o1.getUserName() : o1.getLegalName();
            String n2 = o2.getLegalName() == null || o2.getLegalName().length() == 0 ? o2.getUserName() == null || o2.getUserName().length() == 0 ? "" : o2.getUserName() : o2.getLegalName();
            return n1.compareTo(n2);
        });
        mUserAdapter.addUsers(users);
        mUserList.invalidate();
    }

    @Override
    public void onUserClicked(int position) {

    }

    @OnClick(R.id.text_add_new_contact)
    public void onAddContactClicked() {
        startActivity(AddContactActivity.getLaunchIntent(getBaseActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Keys.Args.USER_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        mUserAdapter.clearAll();
        getPresenter().getUserList();
    }
}
