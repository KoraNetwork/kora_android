package com.kora.android.presentation.ui.common.send_to;

import android.util.Log;

import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultDisposableObserver;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.request.AddToRequestsUseCase;
import com.kora.android.domain.usecase.transaction.AddToTransactionsUseCase;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.domain.usecase.user.SetAsRecentUseCase;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.common.enter_pin.EnterPinPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

@ConfigPersistent
public class SendMoneyPresenter extends BasePresenter<SendMoneyView> {

    private final GetUserDataUseCase mGetUserDataUseCase;
    private final ConvertAmountUseCase mConvertAmountUseCase;
    private final SetAsRecentUseCase mSetAsRecentUseCase;
    private final AddToRequestsUseCase mAddToRequestsUseCase;

    private UserEntity mSender;
    private UserEntity mReceiver;

    private TransactionType mTransactionType;

    @Inject
    public SendMoneyPresenter(final GetUserDataUseCase getUserDataUseCase,
                              final ConvertAmountUseCase convertAmountUseCase,
                              final SetAsRecentUseCase setAsRecentUseCase,
                              final AddToRequestsUseCase addToRequestsUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mConvertAmountUseCase = convertAmountUseCase;
        mSetAsRecentUseCase = setAsRecentUseCase;
        mAddToRequestsUseCase = addToRequestsUseCase;
    }

    public void setTransactionType(final String transactionType) {
        mTransactionType = TransactionType.valueOf(transactionType);
    }

    public TransactionType getTransactionType() {
        return mTransactionType;
    }

    public void getCurrentUser() {
        mGetUserDataUseCase.setData(false);
        mGetUserDataUseCase.execute(new GetUserSubscriber());
    }

    private Action mGetCurrentUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUserDataUseCase.execute(new GetUserSubscriber());
        }
    };

    public void setReceiver(UserEntity receiver) {
        mReceiver = receiver;
        if (!isViewAttached()) return;
        getView().retrieveReceiver(receiver);

    }

    public void setSender(UserEntity sender) {
        mSender = sender;
        if (!isViewAttached()) return;
        getView().retrieveSender(sender);
    }

    public void convertIfNeed(String amountString) {
        double amount = Double.valueOf(amountString);
        if (mSender.getCurrency().equals(mReceiver.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(amount, mReceiver.getCurrency());
            return;
        }
        mConvertAmountUseCase.setData(amount, mSender.getCurrency(), mReceiver.getCurrency());
        mConvertAmountUseCase.execute(new ConvertSubscriber());
    }

    public void sendOrRequest(String senderAmount, String receiverAmount, String additionalNote) {

        if (Validator.isEmpty(senderAmount)) {
            if (getView() == null) return;
            getView().emptySenderAmountError();
            return;
        }
        if (Validator.isEmpty(receiverAmount)) {
            if (getView() == null) return;
            getView().emptyReceiverAmountError();
            return;
        }

        Double sAmount = Double.valueOf(senderAmount);
        Double rAmount = Double.valueOf(receiverAmount);

        if (mTransactionType.equals(TransactionType.SEND)) {
            getView().openPinScreen(mReceiver, sAmount, rAmount);
        } else if (mTransactionType.equals(TransactionType.REQUEST)) {
            mAddToRequestsUseCase.setData(
                    mReceiver.getId(),
                    sAmount,
                    rAmount,
                    additionalNote);
            mAddToRequestsUseCase.execute(new AddToRequestsSubscriber());
        }
    }

    private Action mAddToRequestsAction = new Action() {
        @Override
        public void run() throws Exception {
            mAddToRequestsUseCase.execute(new AddToRequestsSubscriber());
        }
    };

    private class AddToRequestsSubscriber extends DefaultInternetSubscriber<RequestEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final RequestEntity requestEntity) {
            if (!isViewAttached()) return;

            Log.e("_____", requestEntity.toString());
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
            getView().showErrorWithRetry(new RetryAction(mAddToRequestsAction));
        }
    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public UserEntity getSender() {
        return mSender;
    }

    public void setAsResent() {
        mSetAsRecentUseCase.setData(mReceiver);
        mSetAsRecentUseCase.execute(new DefaultDisposableObserver());
    }

    private class GetUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(UserEntity userEntity) {
            mSender = userEntity;
            if (!isViewAttached()) return;
            getView().retrieveSender(userEntity);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetCurrentUserAction));
        }
    }

    private class ConvertSubscriber extends DefaultInternetSubscriber<Double> {

        @Override
        public void onNext(Double amount) {
            if (!isViewAttached()) return;
            getView().showConvertedCurrency(amount, mReceiver.getCurrency());

        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
        }
    }

    @Override
    public void onDetachView() {
        mGetUserDataUseCase.dispose();
        mConvertAmountUseCase.dispose();
        mSetAsRecentUseCase.dispose();
        mAddToRequestsUseCase.dispose();
    }
}
