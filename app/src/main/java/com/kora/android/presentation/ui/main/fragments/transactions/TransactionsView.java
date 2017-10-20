package com.kora.android.presentation.ui.main.fragments.transactions;

import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface TransactionsView extends BaseView<TransactionsPresenter> {

    void showTransactions(List<TransactionEntity> transactionEntities);
}
