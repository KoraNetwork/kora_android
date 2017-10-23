package com.kora.android.presentation.ui.request;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class RecentPresenter extends BasePresenter<RecentView> {

    private TransactionType mTransactionType;

    @Inject
    public RecentPresenter() {

    }

    public void setTransactionType(final String transactionType) {
        mTransactionType = TransactionType.valueOf(transactionType);
    }

    public TransactionType getTransactionType() {
        return mTransactionType;
    }
}
