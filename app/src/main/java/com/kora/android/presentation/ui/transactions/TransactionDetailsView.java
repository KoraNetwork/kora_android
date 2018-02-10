package com.kora.android.presentation.ui.transactions;

import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface TransactionDetailsView extends BaseView<TransactionDetailsPresenter> {

    void showTransaction(final TransactionEntity transactionEntity);

    void enableAndShowRefreshIndicator(final boolean enableIndicator,
                                       final boolean showIndicator);
}
