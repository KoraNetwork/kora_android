package com.kora.android.presentation.ui.common.deposit_withdraw;

import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.deposit.AddDepositUseCase;
import com.kora.android.domain.usecase.deposit.UpdateDepositUseCase;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.common.send_request.RequestDetailsPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

@ConfigPersistent
public class DepositDetailsPresenter extends BasePresenter<DepositDetailsView> {

    private final GetUserDataUseCase mGetUserDataUseCase;
    private final ConvertAmountUseCase mConvertAmountUseCase;
    private final AddDepositUseCase mAddDepositUseCase;
    private final UpdateDepositUseCase mUpdateDepositUseCase;

    private ActionType mActionType;
    private DepositEntity mDepositEntity;
    private UserEntity mSender;
    private UserEntity mReceiver;

    private int mAmountEditTextWidth = 0;

    @Inject
    public DepositDetailsPresenter(final GetUserDataUseCase getUserDataUseCase,
                                   final ConvertAmountUseCase convertAmountUseCase,
                                   final AddDepositUseCase addDepositUseCase,
                                   final UpdateDepositUseCase updateDepositUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mConvertAmountUseCase = convertAmountUseCase;
        mAddDepositUseCase = addDepositUseCase;
        mUpdateDepositUseCase = updateDepositUseCase;
    }

    public void getCurrentUser() {
        mGetUserDataUseCase.setData(false);
        mGetUserDataUseCase.execute(new GetCurrentUserSubscriber());
    }

    private Action mGetCurrentUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUserDataUseCase.execute(new GetCurrentUserSubscriber());
        }
    };

    private class GetCurrentUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

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

    public void setReceiver(UserEntity receiver) {
        mReceiver = receiver;
        if (!isViewAttached()) return;
        if (receiver == null) return;
        getView().retrieveReceiver(receiver);
    }

    public void setSender(UserEntity sender) {
        mSender = sender;
    }

    public void setDepositEntity(DepositEntity depositEntity) {
        mDepositEntity = depositEntity;
        if (depositEntity == null) return;

        if (mDepositEntity.getDirection() == Direction.FROM) {
            setSender(mDepositEntity.getFrom());
            setReceiver(mDepositEntity.getTo());
        } else {
            setSender(mDepositEntity.getTo());
            setReceiver(mDepositEntity.getFrom());
        }
    }

    public void convertIfNeed(Double value) {
        if (mSender == null || mReceiver == null) return;
        if (mSender.getCurrency().equals(mReceiver.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(value, mReceiver.getCurrency());
            return;
        }
        mConvertAmountUseCase.setData(value, mSender.getCurrency(), mReceiver.getCurrency());
        mConvertAmountUseCase.execute(new ConvertSubscriber());
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

    private boolean validateForm(double senderAmount, double receiverAmount) {
        if (!Validator.isValidPrice(senderAmount)) {
            if (!isViewAttached()) return false;
            getView().emptySenderAmountError();
            return false;
        }
        if (!Validator.isValidPrice(receiverAmount)) {
            if (!isViewAttached()) return false;
            getView().emptyReceiverAmountError();
            return false;
        }
        return true;
    }

    public void sendDeposit(final double senderAmount, final double receiverAmount, final int interestRate) {
        if (!validateForm(senderAmount, receiverAmount)) return;

        mAddDepositUseCase.setData(
                mReceiver.getId(),
                senderAmount,
                receiverAmount,
                interestRate);
        mAddDepositUseCase.execute(new AddDepositSubscriber());
    }

    private Action mAddDepositAction = new Action() {
        @Override
        public void run() throws Exception {
            mAddDepositUseCase.execute(new AddDepositSubscriber());
        }
    };

    private class AddDepositSubscriber extends DefaultInternetSubscriber<DepositEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final DepositEntity depositEntity) {
            if (!isViewAttached()) return;
            getView().onDepositSent(depositEntity);
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
            getView().showErrorWithRetry(new RetryAction(mAddDepositAction));
        }
    }

    public void onConfirmClicked() {
        if (mDepositEntity == null) return;
        if (!isViewAttached()) return;

//        if (mDepositEntity.getDirection().equals(Direction.TO))
//            getView().openPinScreen(mDepositEntity.getFrom(), mDepositEntity.getToAmount(), mDepositEntity.getFromAmount(), mDepositEntity);
//        else if (mDepositEntity.getDirection().equals(Direction.FROM))

            getView().openPinScreen(mDepositEntity.getFrom(), mDepositEntity.getToAmount(), mDepositEntity.getFromAmount(), mDepositEntity);
    }

    public void onRejectClicked() {
        if (mDepositEntity == null) return;
        mUpdateDepositUseCase.setData(mDepositEntity.getId());
        mUpdateDepositUseCase.execute(new UpdateDepositSubscriber());
    }

    private Action mUpdateDepositAction = new Action() {
        @Override
        public void run() throws Exception {
            mUpdateDepositUseCase.execute(new UpdateDepositSubscriber());
        }
    };

    private class UpdateDepositSubscriber extends DefaultInternetSubscriber<DepositEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final DepositEntity depositEntity) {
            if (!isViewAttached()) return;
            getView().onUserRejected(depositEntity);
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
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mUpdateDepositAction));
        }
    }

    public ActionType getActionType() {
        return mActionType;
    }

    public void setActionType(ActionType mActionType) {
        this.mActionType = mActionType;
    }

    public UserEntity getSender() {
        return mSender;
    }

    public DepositEntity getDepositEntity() {
        return mDepositEntity;
    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public int getAmountEditTextWidth() {
        return mAmountEditTextWidth;
    }

    public void setAmountEditTextWidth(int mAmountEditTextWidth) {
        this.mAmountEditTextWidth = mAmountEditTextWidth;
    }

    @Override
    public void onDetachView() {
        mGetUserDataUseCase.dispose();
        mConvertAmountUseCase.dispose();
        mAddDepositUseCase.dispose();
        mUpdateDepositUseCase.dispose();
    }
}
