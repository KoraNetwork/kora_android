package com.kora.android.presentation.ui.main.fragments.borrow;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class BorrowMainPresenter extends BasePresenter<BorrowMainView> {

    @Inject
    public BorrowMainPresenter() {

    }
}
