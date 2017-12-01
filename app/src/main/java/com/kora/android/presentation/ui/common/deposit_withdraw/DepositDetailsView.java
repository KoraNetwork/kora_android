package com.kora.android.presentation.ui.common.deposit_withdraw;

import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface DepositDetailsView extends BaseView<DepositDetailsPresenter> {

    void retrieveSender(final UserEntity sender);

    void retrieveReceiver(final UserEntity receiver);

    void showConvertedCurrency(final Double value,
                               final String currency);

    void emptySenderAmountError();

    void emptyReceiverAmountError();

    void openPinScreen(final UserEntity receiver,
                       final double fromAmount,
                       final double toAmount,
                       final DepositEntity depositEntity);

    void onUserRejected(final DepositEntity depositEntity);

    void onDepositSent(DepositEntity depositEntity);
}
