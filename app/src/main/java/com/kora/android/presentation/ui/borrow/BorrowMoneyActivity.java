package com.kora.android.presentation.ui.borrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;

import static com.kora.android.common.Keys.Args.USER_ENTITY;

public class BorrowMoneyActivity extends BaseActivity<BorrowMoneyPresenter> implements BorrowMoneyView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity userEntity) {
        final Intent intent = new Intent(baseActivity, BorrowMoneyActivity.class);
        intent.putExtra(USER_ENTITY, userEntity);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_borrow_money;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_white);

        initArguments(savedInstanceState);
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(USER_ENTITY))
                getPresenter().setUserEntity(bundle.getParcelable(USER_ENTITY));
        }
        if (getIntent() != null) {
            getPresenter().setUserEntity(getIntent().getParcelableExtra(USER_ENTITY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_ENTITY, getPresenter().getUserEntity());
    }
}
