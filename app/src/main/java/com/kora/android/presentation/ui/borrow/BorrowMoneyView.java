package com.kora.android.presentation.ui.borrow;

import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface BorrowMoneyView extends BaseView<BorrowMoneyPresenter> {

    String test = "";

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

    void onBorrowRequestUpdated(BorrowEntity borrowEntity);

    void setupGuarantor(UserEntity user);

    void showEnterPinScreen(final BorrowEntity borrowEntity, final ActionType actionType);
}
