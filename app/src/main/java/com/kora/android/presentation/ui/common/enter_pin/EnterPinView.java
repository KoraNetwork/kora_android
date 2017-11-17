package com.kora.android.presentation.ui.common.enter_pin;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface EnterPinView extends BaseView<EnterPinPresenter> {

    void showEmptyPinCode();

    void showTooShortPinCode();

    void showTransactionScreen();

    void showBorrowScreen();
}
