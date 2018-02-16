package com.kora.android.presentation.ui.deposit_withdraw;

import com.kora.android.common.utils.StringUtils;
import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.convert.GetConvertedAmountUseCase;
import com.kora.android.domain.usecase.deposit.AddDepositWithdrawUseCase;
import com.kora.android.domain.usecase.deposit.UpdateDepositUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class DepositWithdrawDetailsPresenter extends BasePresenter<DepositWithdrawDetailsView> {

    private final GetUserDataUseCase mGetUserDataUseCase;
    private final GetConvertedAmountUseCase mGetConvertedAmountUseCase;
    private final AddDepositWithdrawUseCase mAddDepositWithdrawUseCase;
    private final UpdateDepositUseCase mUpdateDepositWithdrawUseCase;

    private ActionType mActionType;
    private DepositWithdrawEntity mDepositWithdrawEntity;
    private UserEntity mSender;
    private UserEntity mReceiver;

    private int mAmountEditTextWidth = 0;

    @Inject
    public DepositWithdrawDetailsPresenter(final GetUserDataUseCase getUserDataUseCase,
                                           final GetConvertedAmountUseCase getConvertedAmountUseCase,
                                           final AddDepositWithdrawUseCase addDepositWithdrawUseCase,
                                           final UpdateDepositUseCase updateDepositUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mGetConvertedAmountUseCase = getConvertedAmountUseCase;
        mAddDepositWithdrawUseCase = addDepositWithdrawUseCase;
        mUpdateDepositWithdrawUseCase = updateDepositUseCase;
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

    public void setDepositEntity(DepositWithdrawEntity depositWithdrawEntity) {
        mDepositWithdrawEntity = depositWithdrawEntity;
        if (depositWithdrawEntity == null) return;

        if (mDepositWithdrawEntity.getDirection() == Direction.FROM) {
            setSender(mDepositWithdrawEntity.getFrom());
            setReceiver(mDepositWithdrawEntity.getTo());
        } else {
            setSender(mDepositWithdrawEntity.getTo());
            setReceiver(mDepositWithdrawEntity.getFrom());
        }
    }

    public void convertIfNeed(final Double value) {
        if (mSender == null || mReceiver == null) return;
        if (mSender.getCurrency().equals(mReceiver.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(value, mReceiver.getCurrency());
            return;
        }
        if (mActionType == ActionType.CREATE_DEPOSIT) {
            mGetConvertedAmountUseCase.setData(mReceiver.getId(), value, TransactionType.DEPOSIT);
        } else  if (mActionType == ActionType.CREATE_WITHDRAW) {
            mGetConvertedAmountUseCase.setData(mReceiver.getId(), value, TransactionType.WITHDRAW);
        }
        mGetConvertedAmountUseCase.execute(new ConvertSubscriber());
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

    private boolean validateForm(double senderAmount, double receiverAmount, String interestRate) {
        if (interestRate == null || interestRate.isEmpty()) {
            getView().showIncorrectInterestRate();
            return false;
        }
        if (!StringUtils.isInterestRateValid(interestRate)) {
            getView().showIncorrectInterestRate();
            return false;
        }
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

    public void sendDepositWithdraw(final double senderAmount, final double receiverAmount, final String interestRate) {
        if (!validateForm(senderAmount, receiverAmount, interestRate)) return;

        boolean isDeposit = true;
        if (mActionType == ActionType.CREATE_DEPOSIT) {
            isDeposit = true;
        } else if (mActionType == ActionType.CREATE_WITHDRAW) {
            isDeposit = false;
        }
        mAddDepositWithdrawUseCase.setData(
                mReceiver.getId(),
                senderAmount,
                receiverAmount,
                Integer.parseInt(interestRate),
                isDeposit);
        mAddDepositWithdrawUseCase.execute(new AddDepositWithdrawSubscriber());
    }

    private Action mAddDepositAction = new Action() {
        @Override
        public void run() throws Exception {
            mAddDepositWithdrawUseCase.execute(new AddDepositWithdrawSubscriber());
        }
    };

    private class AddDepositWithdrawSubscriber extends DefaultInternetSubscriber<DepositWithdrawEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final DepositWithdrawEntity depositWithdrawEntity) {
            if (!isViewAttached()) return;
            getView().onDepositWithdrawSent(depositWithdrawEntity);
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
        if (mDepositWithdrawEntity == null) return;
        if (!isViewAttached()) return;
        getView().openEnterPinScreen(mDepositWithdrawEntity.getFrom(), mDepositWithdrawEntity.getToAmount(), mDepositWithdrawEntity.getFromAmount(), mDepositWithdrawEntity);
    }

    public void onRejectClicked() {
        if (mDepositWithdrawEntity == null) return;

        boolean isDeposit = true;
        if (mActionType == ActionType.SHOW_DEPOSIT) {
            isDeposit = true;
        } else if (mActionType == ActionType.SHOW_WITHDRAW) {
            isDeposit = false;
        }
        mUpdateDepositWithdrawUseCase.setData(mDepositWithdrawEntity.getId(), isDeposit);
        mUpdateDepositWithdrawUseCase.execute(new UpdateDepositSubscriber());
    }

    private Action mUpdateDepositAction = new Action() {
        @Override
        public void run() throws Exception {
            mUpdateDepositWithdrawUseCase.execute(new UpdateDepositSubscriber());
        }
    };

    private class UpdateDepositSubscriber extends DefaultInternetSubscriber<DepositWithdrawEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final DepositWithdrawEntity depositWithdrawEntity) {
            if (!isViewAttached()) return;
            getView().onUserRejected(depositWithdrawEntity);
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

    public DepositWithdrawEntity getDepositEntity() {
        return mDepositWithdrawEntity;
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
        mGetConvertedAmountUseCase.dispose();
        mAddDepositWithdrawUseCase.dispose();
        mUpdateDepositWithdrawUseCase.dispose();
    }
}
