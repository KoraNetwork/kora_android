package com.kora.android.presentation.ui.home;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface HomeView extends BaseView<HomePresenter> {

    void showFlag(final String flagLink);
    void showBalance(final String balance);
    void showCurrencyName(final String currencyName);
}
