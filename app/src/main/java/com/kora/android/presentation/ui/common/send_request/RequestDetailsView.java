package com.kora.android.presentation.ui.common.send_request;

import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface RequestDetailsView extends BaseView<RequestDetailsPresenter> {
    void retrieveSender(UserEntity userEntity);

    void retrieveReceiver(UserEntity user);

    void showConvertedCurrency(Double amount, String currency);

    void emptySenderAmountError();

    void emptyReceiverAmountError();

    void openPinScreen(UserEntity receiver, Double sAmount, Double rAmount, RequestEntity request);

    void onConfirmClicked();

    void onUserRejected(RequestEntity requestEntity);

    void onRequestSend(RequestEntity requestEntity);
}
