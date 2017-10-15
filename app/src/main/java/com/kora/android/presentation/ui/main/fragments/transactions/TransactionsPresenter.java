package com.kora.android.presentation.ui.main.fragments.transactions;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
class TransactionsPresenter extends BasePresenter<TransactionsView> {

    @Inject
    public TransactionsPresenter() {

    }
}
