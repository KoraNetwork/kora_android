package com.kora.android.presentation.ui.borrow;

import android.support.annotation.NonNull;

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


//    public void convertIfNeed(double amount) {
//        if (mLender == null || mSender == null) return;
//
//        if (mLender.getCurrency().equals(mSender.getCurrency())) {
//            if (getView() == null) return;
//            getView().showConvertedCurrency(amount);
//            return;
//        }
//        mConvertAmountUseCase.setData(amount, mSender.getCurrency(), mLender.getCurrency());
//        mConvertAmountUseCase.execute(new ConvertSubscriber());
//    }

//    public void onBorrowClicked(List<UserEntity> guaranters,
//                                double amount, double convertedAmount,
//                                String rate,
//                                String startDate, String maturityDate,
//                                String note) {
//
//        if (guaranters == null || guaranters.size() == 0) {
//            if (!isViewAttached()) return;
//            getView().showNoGuarantersError();
//            return;
//        }
//        if (!Validator.isValidPrice(amount)) {
//            if (!isViewAttached()) return;
//            getView().showInvalidAmountError();
//            return;
//        }
//
//        if (!Validator.isValidPrice(convertedAmount)) {
//            if (!isViewAttached()) return;
//            getView().showInvalidConvertedAmountError();
//            return;
//        }
//
//        if (Validator.isEmpty(rate)) {
//            if (!isViewAttached()) return;
//            getView().showEmptyRateError();
//            return;
//        }
//
//        if (Validator.isEmpty(startDate)) {
//            if (!isViewAttached()) return;
//            getView().showEmptyStartDateError();
//            return;
//        }
//
//        if (!Validator.isFutureDate(startDate, DateUtils.PRETTY_DATE_PATTERN)) {
//            if (!isViewAttached()) return;
//            getView().showPastStartDateError();
//            return;
//        }
//
//        if (Validator.isEmpty(maturityDate)) {
//            if (!isViewAttached()) return;
//            getView().showEmptyMaturityDateError();
//            return;
//        }
//
//        if (!Validator.isBefore(startDate, maturityDate, DateUtils.PRETTY_DATE_PATTERN)) {
//            if (!isViewAttached()) return;
//            getView().showPastMaturityDateError();
//            return;
//        }
//
//        mAddBorrowRequestUseCase.setData(mSender, guaranters, amount, convertedAmount, Integer.valueOf(rate), startDate, maturityDate, note);
//        mAddBorrowRequestUseCase.execute(new AddBorrowRequestSubscriber());
//
//    }

//    public void setBorrow(BorrowEntity borrow) {
//        mBorrowRequest = borrow;
//    }
//
//    public void loadBorrowData() {
//        if (mBorrowRequest == null) return;
//        if (!isViewAttached()) return;
//        getView().showBorrowRequest(mBorrowRequest);
//    }

//    public void loadLenderData() {
//        if (mLender == null) return;
//        if (!isViewAttached()) return;
//        getView().showSender(mLender);
//    }


//    private Action mConvertAmountAction = new Action() {
//        @Override
//        public void run() throws Exception {
//            mConvertAmountUseCase.execute(new ConvertSubscriber());
//        }
//    };
//
//    private class ConvertSubscriber extends DefaultInternetSubscriber<Double> {
//
//        @Override
//        public void onNext(Double amount) {
//            if (!isViewAttached()) return;
//            getView().showConvertedCurrency(amount);
//
//        }
//
//        @Override
//        public void onError(@NonNull Throwable throwable) {
//            super.onError(throwable);
//            if (!isViewAttached()) return;
//        }
//
//        @Override
//        public void handleNetworkError(RetrofitException retrofitException) {
//            if (!isViewAttached()) return;
//            getView().showErrorWithRetry(new RetryAction(mConvertAmountAction));
//        }
//    }
//
//    private Action mAddBorrowRequestAction = new Action() {
//        @Override
//        public void run() throws Exception {
//            mAddBorrowRequestUseCase.execute(new AddBorrowRequestSubscriber());
//        }
//    };
//
//    private class AddBorrowRequestSubscriber extends DefaultInternetSubscriber<BorrowEntity> {
//
//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }
//
//        @Override
//        public void onNext(BorrowEntity borrowEntity) {
//            if (!isViewAttached()) return;
//            getView().onBorrowRequestAdded(borrowEntity);
//        }
//
//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }
//
//        @Override
//        public void onError(Throwable throwable) {
//            super.onError(throwable);
//
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }
//
//        @Override
//        public void handleUnprocessableEntity(ErrorModel errorModel) {
//            if (!isViewAttached()) return;
//            getView().showError(errorModel.getError());
//        }
//
//        @Override
//        public void handleNetworkError(RetrofitException retrofitException) {
//            if (!isViewAttached()) return;
//            getView().showErrorWithRetry(new RetryAction(mAddBorrowRequestAction));
//        }
//    }

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


    @Override
    public void onDetachView() {
        mConvertAmountUseCase.dispose();
        mGetUserDataUseCase.dispose();
        mAddBorrowRequestUseCase.dispose();
    }
}
