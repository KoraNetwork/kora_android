package com.kora.android.presentation.ui.main.fragments.home;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.balance.GetBalanceUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeView> {

    private final GetUserDataUseCase mUserDataUseCase;
    private final GetBalanceUseCase mGetBalanceUseCase;

    private UserEntity mUserEntity;

    @Inject
    public HomePresenter(final GetUserDataUseCase userDataUseCase,
                         final GetBalanceUseCase getBalanceUseCase) {
        mUserDataUseCase = userDataUseCase;
        mGetBalanceUseCase = getBalanceUseCase;
    }

    public void startGetUserTask() {
        mUserDataUseCase.setData(false);
        mUserDataUseCase.execute(new GetUserSubscriber());
    }

    private Action mGetUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mUserDataUseCase.execute(new GetUserSubscriber());
        }
    };

    public void startGetBalanceTask() {
        mGetBalanceUseCase.setData(mUserEntity.getIdentity(), mUserEntity.getERC20Token());
        mGetBalanceUseCase.execute(new GetBalanceSubscriber());
    }

    private Action mGetBalanceAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetBalanceUseCase.execute(new GetBalanceSubscriber());
        }
    };

    private class GetUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final UserEntity userEntity) {
            if (!isViewAttached()) return;
            mUserEntity = userEntity;
            getView().showFlag(userEntity.getFlag());
            startGetBalanceTask();
        }

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
        }

        @Override
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetUserAction));
        }
    }

    private class GetBalanceSubscriber extends DefaultInternetSubscriber<String> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final String balance) {
            if(!isViewAttached()) return;
            getView().showBalance(balance + " " + mUserEntity.getCurrency());
            getView().showCurrencyName(mUserEntity.getCurrencyNameFull());
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
            if(!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetBalanceAction));
        }
    }

    @Override
    public void onDetachView() {
        mUserDataUseCase.dispose();
        mGetBalanceUseCase.dispose();
    }
}
