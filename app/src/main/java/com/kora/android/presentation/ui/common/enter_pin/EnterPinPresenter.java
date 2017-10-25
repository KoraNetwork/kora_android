package com.kora.android.presentation.ui.common.enter_pin;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.transaction.AddToTransactionsUseCase;
import com.kora.android.domain.usecase.transaction.SendTransactionUseCase;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class EnterPinPresenter extends BasePresenter<EnterPinView> {

    private final SendTransactionUseCase mSendTransactionUseCase;
    private final AddToTransactionsUseCase mAddToTransactionsUseCase;

    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;

    @Inject
    public EnterPinPresenter(final SendTransactionUseCase sendTransactionUseCase,
                             final AddToTransactionsUseCase addToTransactionsUseCase) {
        mSendTransactionUseCase = sendTransactionUseCase;
        mAddToTransactionsUseCase = addToTransactionsUseCase;
    }

    public void startSendTransactionTask(final String pinCode, ActionType actionType) {
        if (pinCode == null || pinCode.isEmpty()) {
            getView().showEmptyPinCode();
            return;
        }
        if (!StringUtils.isPinCodeLongEnough(pinCode)) {
            getView().showTooShortPinCode();
            return;
        }

        mSendTransactionUseCase.setData(pinCode, mReceiver, mSenderAmount, mReceiverAmount);
        mSendTransactionUseCase.execute(new SendTransactionSubscriber(actionType));
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

        private ActionType mActionType;

        public SendTransactionSubscriber(ActionType actionType) {
            mActionType = actionType;
        }

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true, false, R.string.enter_pin_sending_transaction_wait);
        }

        @Override
        public void onNext(@NonNull final List<String> transactionHashList) {
            if(!isViewAttached()) return;
            startAddToTransactionsTask(transactionHashList, mActionType);
        }
//
//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

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

    public void startAddToTransactionsTask(final List<String> transactionHash, ActionType actionType) {
        mAddToTransactionsUseCase.setData(
                getTransactionTypeByAction(actionType),
                mReceiver.getId(),
                mSenderAmount,
                mReceiverAmount,
                transactionHash);
        mAddToTransactionsUseCase.execute(new AddToTransactionsSubscriber());
    }

    private TransactionType getTransactionTypeByAction(ActionType actionType) {
        switch (actionType) {
            case CREATE_REQUEST:
                return TransactionType.REQUEST;
            case SEND_MONEY:
                return TransactionType.SEND;
        }
        return null;
    }

    private Action mAddToTransactionsAction = new Action() {
        @Override
        public void run() throws Exception {
            mAddToTransactionsUseCase.execute(new AddToTransactionsSubscriber());
        }
    };

    private class AddToTransactionsSubscriber extends DefaultInternetSubscriber<TransactionEntity> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final TransactionEntity transactionEntity) {
            if (!isViewAttached()) return;
            getView().showNextScreen();
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
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mAddToTransactionsAction));
        }
    }

    @Override
    public void onDetachView() {
        mSendTransactionUseCase.dispose();
        mAddToTransactionsUseCase.dispose();
    }
}
