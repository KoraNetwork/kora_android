package com.kora.android.presentation.ui.send.enter_pin;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.transaction.AddToTransactionsUseCase;
import com.kora.android.domain.usecase.transaction.SendTransactionUseCase;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.presentation.enums.TransactionType.SEND;

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
            getView().showProgress(true, false, R.string.enter_pin_sending_transaction_wait);
        }

        @Override
        public void onNext(@NonNull final List<String> transactionHashList) {
            if(!isViewAttached()) return;
            startAddToTransactionsTask(transactionHashList);
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

    public void startAddToTransactionsTask(final List<String> transactionHash) {
        mAddToTransactionsUseCase.setData(
                SEND.toString().toLowerCase(),
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
