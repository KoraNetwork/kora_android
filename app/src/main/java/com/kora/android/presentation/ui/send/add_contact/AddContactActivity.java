package com.kora.android.presentation.ui.send.add_contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.countries.adapter.CountryAdapter;
import com.kora.android.presentation.ui.send.add_contact.adapter.UserAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AddContactActivity extends BaseActivity<AddContactPresenter> implements AddContactView, UserAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
        getPresenter().startGetUsersTask();
    }

    private void initUI() {
        mRvUsers.setHasFixedSize(true);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
        mRvUsers.setAdapter(mUserAdapter);

        mUserAdapter.setOnItemClickListener(this);
    }

    @Override
    public void showUsers(final List<UserEntity> userEntityList) {
        mUserAdapter.addAll(userEntityList);

        for (int i = 0; i < userEntityList.size(); i++)
            Log.e("_____", userEntityList.get(i).toString());
    }

    @Override
    public void onClickSelectUser(final UserEntity userEntity, final int position) {
        Log.e("_____", userEntity.toString());
    }
}
