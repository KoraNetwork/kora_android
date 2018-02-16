package com.kora.android.presentation.ui.deposit_withdraw;

import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface DepositWithdrawDetailsView extends BaseView<DepositWithdrawDetailsPresenter> {

    void retrieveSender(final UserEntity sender);

    void retrieveReceiver(final UserEntity receiver);

    void showConvertedCurrency(final Double value,
                               final String currency);

    void emptySenderAmountError();

    void emptyReceiverAmountError();

    void showIncorrectInterestRate();

    void openEnterPinScreen(final UserEntity receiver,
                            final double fromAmount,
                            final double toAmount,
                            final DepositWithdrawEntity depositWithdrawEntity);

    void onUserRejected(final DepositWithdrawEntity depositWithdrawEntity);

    void onDepositWithdrawSent(final DepositWithdrawEntity depositWithdrawEntity);
}
