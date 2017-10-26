package com.kora.android.presentation.ui.borrow;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class BorrowMoneyPresenter extends BasePresenter<BorrowMoneyView> {

    private UserEntity mUserEntity;

    @Inject
    public BorrowMoneyPresenter() {

    }

    public UserEntity getUserEntity() {
        return mUserEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.mUserEntity = userEntity;
    }
}
