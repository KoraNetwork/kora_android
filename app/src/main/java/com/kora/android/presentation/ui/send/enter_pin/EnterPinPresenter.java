package com.kora.android.presentation.ui.send.enter_pin;

import android.util.Log;

import com.kora.android.common.utils.StringUtils;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.transaction.SendTransactionUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ConfigPersistent
public class EnterPinPresenter extends BasePresenter<EnterPinView> {

    private final SendTransactionUseCase mSendTransactionUseCase;

    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;

    @Inject
    public EnterPinPresenter(final SendTransactionUseCase sendTransactionUseCas) {
        mSendTransactionUseCase = sendTransactionUseCas;
    }

    public void startSendTransactionTask(final String pinCode) {
        if (pinCode == null || pinCode.isEmpty()) {
            getView().showEmptyPinCode();
            return;
        }
        if (!StringUtils.isPinCodeLongEnough(pinCode)) {
            getView().showTooShortPinCode();
            return;
        }

        mSendTransactionUseCase.setData(pinCode, mReceiver, mSenderAmount, mReceiverAmount);
        mSendTransactionUseCase.execute(new SendTransactionSubscriber());
    }

    public void setReceiver(final UserEntity receiver) {
        mReceiver = receiver;
    }

    public void setSenderAmount(final double senderAmount) {
        mSenderAmount = senderAmount;
    }

    public void setReceiverAmount(final double receiverAmount) {
        mReceiverAmount = receiverAmount;
    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public double getSenderAmount() {
        return mSenderAmount;
    }

    public double getReceiverAmount() {
        return mReceiverAmount;
    }

    private class SendTransactionSubscriber extends DefaultWeb3jSubscriber<List<String>> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final List<String> transactionHashList) {
            if(!isViewAttached()) return;
            Log.e("_____", transactionHashList.toString());
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }

        @Override
        public void handleWeb3jError(final String message) {
            if(!isViewAttached()) return;
            getView().showError(message);
        }
    }

    @Override
    public void onDetachView() {
        mSendTransactionUseCase.dispose();
    }
}
