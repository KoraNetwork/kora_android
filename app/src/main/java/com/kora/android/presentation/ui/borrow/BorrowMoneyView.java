package com.kora.android.presentation.ui.borrow;

import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface BorrowMoneyView extends BaseView<BorrowMoneyPresenter> {

    void showCurrentUser(UserEntity userEntity);

    void showConvertedCurrency(Double amount);

    void showNoGuarantersError();

    void showInvalidAmountError();

    void showEmptyRateError();

    void showInvalidConvertedAmountError();

    void showEmptyStartDateError();

    void showPastStartDateError();

    void showEmptyMaturityDateError();

    void showPastMaturityDateError();

    void onBorrowRequestAdded(BorrowEntity borrowEntity);


//    void showSender(UserEntity lender);
//
//    void showReceiver(UserEntity lender);
//
//    void showConvertedCurrency(double amount);
//
//    void showNoGuarantersError();
//
//    void showInvalidAmountError();
//
//    void showInvalidConvertedAmountError();
//
//    void showEmptyRateError();
//
//    void showEmptyStartDateError();
//
//    void showEmptyMaturityDateError();
//
//    void onBorrowRequestAdded(BorrowEntity borrowEntity);
//
//    void showPastStartDateError();
//
//    void showPastMaturityDateError();
//
//    void showBorrowRequest(BorrowEntity borrowEntity);
}
