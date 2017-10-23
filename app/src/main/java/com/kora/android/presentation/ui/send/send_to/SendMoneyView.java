package com.kora.android.presentation.ui.send.send_to;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface SendMoneyView extends BaseView<SendMoneyPresenter> {
    void retrieveSender(UserEntity userEntity);

    void retrieveReceiver(UserEntity user);

    void showConvertedCurrency(Double amount, String currency);

    void emptySenderAmountError();

    void emptyReceiverAmountError();

    void openPinScreen(UserEntity receiver, Double sAmount, Double rAmount);
}
