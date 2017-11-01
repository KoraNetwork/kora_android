package com.kora.android.presentation.ui.borrow;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface BorrowMoneyView extends BaseView<BorrowMoneyPresenter> {

    void showLender(UserEntity lender);

    void showConvertedCurrency(double amount, String currency);

    void retrieveSenderCurrency(UserEntity userEntity);
}