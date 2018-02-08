package com.kora.android.presentation.ui.main.fragments.home;

import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface HomeView extends BaseView<HomePresenter> {

    void showEmailConfirmation(final boolean isEmailConfirmed);

    void showFlag(final String flagLink);
    void showBalance(final String balance);
    void showCurrencyName(final String currencyName);

    void showTransactions(final List<TransactionEntity> transactionEntityList);

    void enableAndShowRefreshIndicator(final boolean enableIndicator,
                                       final boolean showIndicator);
}
