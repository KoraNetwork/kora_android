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
import com.kora.android.domain.usecase.transaction.CreateRawTransactionUseCase;
import com.kora.android.domain.usecase.transaction.SendRawTransactionUseCase;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import org.web3j.crypto.CipherException;

import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class EnterPinPresenter extends BasePresenter<EnterPinView> {

    private final CreateRawTransactionUseCase mCreateRawTransactionUseCase;
    private final SendRawTransactionUseCase mSendRawTransactionUseCase;
    private final DeleteRequestUseCase mDeleteRequestUseCase;

    private ActionType mActionType;
    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;
    private RequestEntity mRequest;

    @Inject
    public EnterPinPresenter(final CreateRawTransactionUseCase createRawTransactionUseCase,
                             final SendRawTransactionUseCase sendRawTransactionUseCase,
                             final DeleteRequestUseCase deleteRequestUseCase) {
        mCreateRawTransactionUseCase = createRawTransactionUseCase;
        mSendRawTransactionUseCase = sendRawTransactionUseCase;
        mDeleteRequestUseCase = deleteRequestUseCase;
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

    public RequestEntity getRequest() {
        return mRequest;
    }

    public void setRequest(RequestEntity requestId) {
        this.mRequest = requestId;
    }

    private TransactionType getTransactionTypeByAction(ActionType actionType) {
        switch (actionType) {
            case CREATE_REQUEST:
            case SHOW_REQUEST:
                return TransactionType.REQUEST;
            case SEND_MONEY:
                return TransactionType.SEND;
        }
        return null;
    }

    public void startCreateRawTransactionTask(final String pinCode) {
        if (pinCode == null || pinCode.isEmpty()) {
            getView().showEmptyPinCode();
            return;
        }
        if (!StringUtils.isPinCodeLongEnough(pinCode)) {
            getView().showTooShortPinCode();
            return;
        }

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
            if (mActionType.equals(ActionType.SEND_MONEY))
                startSendRawTransactionTask(rawTransactions);
            else if (mActionType.equals(ActionType.SHOW_REQUEST))
                startDeleteRequestTask(rawTransactions);
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();

            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWalletError(final FileNotFoundException fileNotFoundException) {
            getView().showError(R.string.web3j_error_message_no_wallet);
        }

        @Override
        public void handlePinError(final CipherException cipherException) {
            getView().showError(R.string.web3j_error_message_wrong_password);
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            getView().showError(R.string.web3j_error_message_network_problems);
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

    private Action mSendRawTransactionAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendRawTransactionUseCase.execute(new SendRawTransactionSubscriber());
        }
    };

    private class SendRawTransactionSubscriber extends DefaultInternetSubscriber<TransactionEntity> {

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
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mSendRawTransactionAction));
        }
    }

    public void startDeleteRequestTask(final List<String> rawTransactions) {
        if (mRequest.getDirection().equals(Direction.TO)) {
            mDeleteRequestUseCase.setData(
                    mRequest.getId(),
                    mReceiverAmount,
                    mSenderAmount,
                    rawTransactions);
        } else if (mRequest.getDirection().equals(Direction.FROM)) {
            mDeleteRequestUseCase.setData(
                    mRequest.getId(),
                    mSenderAmount,
                    mReceiverAmount,
                    rawTransactions);
        }
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

    @Override
    public void onDetachView() {
        mDeleteRequestUseCase.dispose();
        mCreateRawTransactionUseCase.dispose();
        mSendRawTransactionUseCase.dispose();
    }
}
