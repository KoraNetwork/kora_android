package com.kora.android.presentation.ui.common.enter_pin;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.request.DeleteRequestUseCase;
import com.kora.android.domain.usecase.transaction.AddToTransactionsUseCase;
import com.kora.android.domain.usecase.transaction.CreateRawTransactionUseCase;
import com.kora.android.domain.usecase.transaction.SendRawTransactionUseCase;
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
    private final DeleteRequestUseCase mDeleteRequestUseCase;
    private final CreateRawTransactionUseCase mCreateRawTransactionUseCase;
    private final SendRawTransactionUseCase mSendRawTransactionUseCase;

    private ActionType mActionType;
    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;
    private String mRequestId;

    @Inject
    public EnterPinPresenter(final SendTransactionUseCase sendTransactionUseCase,
                             final AddToTransactionsUseCase addToTransactionsUseCase,
                             final DeleteRequestUseCase deleteRequestUseCase,
                             final CreateRawTransactionUseCase createRawTransactionUseCase,
                             final SendRawTransactionUseCase sendRawTransactionUseCase) {
        mSendTransactionUseCase = sendTransactionUseCase;
        mAddToTransactionsUseCase = addToTransactionsUseCase;
        mDeleteRequestUseCase = deleteRequestUseCase;
        mCreateRawTransactionUseCase = createRawTransactionUseCase;
        mSendRawTransactionUseCase = sendRawTransactionUseCase;
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

    public ActionType getActionType() {
        return mActionType;
    }

    public void setActionType(final ActionType actionType) {
        mActionType = actionType;
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

    public String getRequestId() {
        return mRequestId;
    }

    public void setRequestId(String requestId) {
        this.mRequestId = requestId;
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
            if (mActionType.equals(ActionType.SEND_MONEY))
                startAddToTransactionsTask(transactionHashList, mActionType);
            else if (mActionType.equals(ActionType.SHOW_REQUEST))
                startDeleteRequestTask(transactionHashList);
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

    private TransactionType getTransactionTypeByAction(ActionType actionType) {
        switch (actionType) {
            case CREATE_REQUEST:
                return TransactionType.REQUEST;
            case SEND_MONEY:
                return TransactionType.SEND;
        }
        return null;
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

    public void startDeleteRequestTask(final List<String> transactionHash) {
        mDeleteRequestUseCase.setData(
                mRequestId,
                mSenderAmount,
                mReceiverAmount,
                transactionHash);
        mDeleteRequestUseCase.execute(new DeleteRequestSubscriber());
    }

    private Action mDeleteRequestAction = new Action() {
        @Override
        public void run() throws Exception {
            mDeleteRequestUseCase.execute(new DeleteRequestSubscriber());
        }
    };

    private class DeleteRequestSubscriber extends DefaultInternetSubscriber<Object> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final Object object) {
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
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mDeleteRequestAction));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void startCreateRawTransactionTask(final String pinCode) {
        mCreateRawTransactionUseCase.setData(
                mReceiver,
                mSenderAmount,
                mReceiverAmount,
                pinCode);
        mCreateRawTransactionUseCase.execute(new CreateRawTransactionSubscriber());
    }

    private class CreateRawTransactionSubscriber extends DefaultWeb3jSubscriber<List<String>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final List<String> rawTransactions) {
            if (!isViewAttached()) return;
            for (int i = 0; i < rawTransactions.size(); i++) {
                Log.e("_____", rawTransactions.get(i));
            }
            startSendRawTransactionTask(rawTransactions);
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWeb3jError(final String message) {
            getView().showError(message);
        }
    }

    public void startSendRawTransactionTask(final List<String> rawTransactions) {
        mSendRawTransactionUseCase.setData(
                getTransactionTypeByAction(mActionType),
                mReceiver.getId(),
                mSenderAmount,
                mReceiverAmount,
                rawTransactions);
        mSendRawTransactionUseCase.execute(new SendRawTransactionSubscriber());
    }

    private class SendRawTransactionSubscriber extends DefaultInternetSubscriber<TransactionEntity> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final TransactionEntity transactionEntity) {
            if (!isViewAttached()) return;
            Log.e("_____", transactionEntity.toString());
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
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
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
        mDeleteRequestUseCase.dispose();
        mCreateRawTransactionUseCase.dispose();
        mSendRawTransactionUseCase.dispose();
    }
}
