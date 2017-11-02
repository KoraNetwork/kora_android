package com.kora.android.presentation.ui.borrow;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class BorrowMoneyPresenter extends BasePresenter<BorrowMoneyView> {

    private final ConvertAmountUseCase mConvertAmountUseCase;
    private final GetUserDataUseCase mGetUserDataUseCase;

    private UserEntity mLender;
    private UserEntity mSender;

    @Inject
    public BorrowMoneyPresenter(final GetUserDataUseCase getUserDataUseCase,
                                final ConvertAmountUseCase convertAmountUseCase) {
        mConvertAmountUseCase = convertAmountUseCase;
        mGetUserDataUseCase = getUserDataUseCase;
    }

    public UserEntity getLender() {
        return mLender;
    }

    public void getCurrentUser() {
        mGetUserDataUseCase.setData(false);
        mGetUserDataUseCase.execute(new GetUserSubscriber());
    }

    public void setLender(UserEntity userEntity) {
        this.mLender = userEntity;
        if (!isViewAttached()) return;
        getView().showLender(mLender);
    }

    public void convertIfNeed(double amount) {
        if (mLender == null || mSender == null) return;

        if (mLender.getCurrency().equals(mSender.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(amount, mSender.getCurrency());
            return;
        }
        mConvertAmountUseCase.setData(amount, mSender.getCurrency(), mLender.getCurrency());
        mConvertAmountUseCase.execute(new ConvertSubscriber());
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
            getView().retrieveSenderCurrency(userEntity);
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
            getView().showConvertedCurrency(amount, mSender.getCurrency());

        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
        }
    }

    @Override
    public void onDetachView() {
        mConvertAmountUseCase.dispose();
        mGetUserDataUseCase.dispose();
    }
}
