package com.kora.android.presentation.ui.send.send_to;

import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultDisposableObserver;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.domain.usecase.user.SetAsRecentUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class SendMoneyPresenter extends BasePresenter<SendMoneyView> {

    final GetUserDataUseCase mGetUserDataUseCase;
    final ConvertAmountUseCase mConvertAmountUseCase;
    final SetAsRecentUseCase mSetAsRecentUseCase;

    private UserEntity mSender;
    private UserEntity mReceiver;

    @Inject
    public SendMoneyPresenter(final GetUserDataUseCase getUserDataUseCase,
                              final ConvertAmountUseCase convertAmountUseCase,
                              final SetAsRecentUseCase setAsRecentUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mConvertAmountUseCase = convertAmountUseCase;
        mSetAsRecentUseCase = setAsRecentUseCase;
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

    public void send(String senderAmount, String receiverAmount) {

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

        getView().openPinScreen(mReceiver, sAmount, rAmount);

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
    }
}
