package com.kora.android.presentation.ui.borrow;

import android.support.annotation.NonNull;

import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.borrow.AddBorrowRequestUseCase;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Action;

@ConfigPersistent
public class BorrowMoneyPresenter extends BasePresenter<BorrowMoneyView> {

    private final ConvertAmountUseCase mConvertAmountUseCase;
    private final GetUserDataUseCase mGetUserDataUseCase;
    private final AddBorrowRequestUseCase mAddBorrowRequestUseCase;

    private UserEntity mSender;
    private UserEntity mReceiver;

    private BorrowEntity mBorrowRequest;

    @Inject
    public BorrowMoneyPresenter(final GetUserDataUseCase getUserDataUseCase,
                                final ConvertAmountUseCase convertAmountUseCase,
                                final AddBorrowRequestUseCase addBorrowRequestUseCase) {
        mConvertAmountUseCase = convertAmountUseCase;
        mGetUserDataUseCase = getUserDataUseCase;
        mAddBorrowRequestUseCase = addBorrowRequestUseCase;
    }

    public void setSender(UserEntity user) {
        mSender = user;
    }

    public UserEntity getSender() {
        return mSender;
    }

    public void setBorrow(BorrowEntity borrow) {
        mBorrowRequest = borrow;
    }

    public BorrowEntity getBorrow() {
        return mBorrowRequest;
    }

    public void setReceiver(UserEntity user) {
        mReceiver = user;
    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public void loadCurrentUser() {
        mGetUserDataUseCase.setData(false);
        mGetUserDataUseCase.execute(new GetUserSubscriber());
    }


    public void convertIfNeed(Double val) {
        if (mReceiver == null || mSender == null) return;
        if (mReceiver.getCurrency().equals(mSender.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(val);
            return;
        }
        mConvertAmountUseCase.setData(val, mSender.getCurrency(), mReceiver.getCurrency());
        mConvertAmountUseCase.execute(new ConvertSubscriber());
    }


    public void onCreateBorrowRequestClicked(List<UserEntity> guaranters,
                                             double amount, double convertedAmount,
                                             String rate,
                                             String startDate, String maturityDate,
                                             String note) {
        boolean isValid = validateForm(guaranters, amount, convertedAmount, rate, startDate, maturityDate);
        if (isValid) {
            mAddBorrowRequestUseCase.setData(mReceiver, guaranters, amount, convertedAmount, Integer.valueOf(rate), startDate, maturityDate, note);
            mAddBorrowRequestUseCase.execute(new AddBorrowRequestSubscriber());
        }

    }

    private boolean validateForm(List<UserEntity> guaranters, double amount, double convertedAmount,
                                 String rate, String startDate, String maturityDate) {
        if (guaranters == null || guaranters.size() == 0) {
            if (!isViewAttached()) return false;
            getView().showNoGuarantersError();
            return false;
        }
        if (!Validator.isValidPrice(amount)) {
            if (!isViewAttached()) return false;
            getView().showInvalidAmountError();
            return false;
        }

        if (!Validator.isValidPrice(convertedAmount)) {
            if (!isViewAttached()) return false;
            getView().showInvalidConvertedAmountError();
            return false;
        }

        if (Validator.isEmpty(rate)) {
            if (!isViewAttached()) return false;
            getView().showEmptyRateError();
            return false;
        }

        if (Validator.isEmpty(startDate)) {
            if (!isViewAttached()) return false;
            getView().showEmptyStartDateError();
            return false;
        }

        if (!Validator.isFutureDate(startDate, DateUtils.PRETTY_DATE_PATTERN)) {
            if (!isViewAttached()) return false;
            getView().showPastStartDateError();
            return false;
        }

        if (Validator.isEmpty(maturityDate)) {
            if (!isViewAttached()) return false;
            getView().showEmptyMaturityDateError();
            return false;
        }

        if (!Validator.isBefore(startDate, maturityDate, DateUtils.PRETTY_DATE_PATTERN)) {
            if (!isViewAttached()) return false;
            getView().showPastMaturityDateError();
            return false;
        }

        return true;
    }

    private Action mGetCurrentUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUserDataUseCase.execute(new GetUserSubscriber());
        }
    };

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
            getView().showCurrentUser(userEntity);
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

    private Action mConvertAmountAction = new Action() {
        @Override
        public void run() throws Exception {
            mConvertAmountUseCase.execute(new ConvertSubscriber());
        }
    };


    private class ConvertSubscriber extends DefaultInternetSubscriber<Double> {

        @Override
        public void onNext(Double amount) {
            if (!isViewAttached()) return;
            getView().showConvertedCurrency(amount);

        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mConvertAmountAction));
        }
    }

    private Action mAddBorrowRequestAction = new Action() {
        @Override
        public void run() throws Exception {
            mAddBorrowRequestUseCase.execute(new AddBorrowRequestSubscriber());
        }
    };

    private class AddBorrowRequestSubscriber extends DefaultInternetSubscriber<BorrowEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(BorrowEntity borrowEntity) {
            if (!isViewAttached()) return;
            getView().onBorrowRequestAdded(borrowEntity);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(Throwable throwable) {
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
            getView().showErrorWithRetry(new RetryAction(mAddBorrowRequestAction));
        }
    }

    @Override
    public void onDetachView() {
        mConvertAmountUseCase.dispose();
        mGetUserDataUseCase.dispose();
        mAddBorrowRequestUseCase.dispose();
    }
}
