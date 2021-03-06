package com.kora.android.presentation.ui.main;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.login.LogoutUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainView> {

    private final GetUserDataUseCase mGetUserDataUseCase;
    private final LogoutUseCase mLogoutUseCase;

    private UserEntity mUserEntity;

    @Inject
    public MainPresenter(final GetUserDataUseCase getUserDataUseCase,
                         final LogoutUseCase logoutUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mLogoutUseCase = logoutUseCase;
    }

    public UserEntity getUserEntity() {
        return mUserEntity;
    }

    public void setUserEntity(final UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    public void loadUserData(final boolean fromNetwork) {
        mGetUserDataUseCase.setData(fromNetwork);
        mGetUserDataUseCase.execute(new GetUserSubscriber(fromNetwork));
    }

    private Action mGetUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUserDataUseCase.execute(new GetUserSubscriber(true));
        }
    };

    private class GetUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        private boolean mFromNetwork;

        public GetUserSubscriber(boolean fromNetwork) {
            mFromNetwork = fromNetwork;
        }

        @Override
        protected void onStart() {
           if (!isViewAttached()) return;
           if (mFromNetwork)
                getView().showProgress(true);
        }

        @Override
        public void onNext(UserEntity userEntity) {
            if (!isViewAttached()) return;
            mUserEntity = userEntity;
            if (mFromNetwork) {
                getView().showEmailConfirmed(userEntity);
            } else {
                getView().onUserDataLoaded(userEntity);
            }
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            if (mFromNetwork)
                getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            if (mFromNetwork)
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
            getView().showErrorWithRetry(new RetryAction(mGetUserAction));
        }
    }

    public void logout() {
        mLogoutUseCase.setData(true);
        mLogoutUseCase.execute(new LogoutSubscriber());
    }

    private Action mLogoutAction = new Action() {
        @Override
        public void run() throws Exception {
            mLogoutUseCase.execute(new LogoutSubscriber());
        }
    };

    private class LogoutSubscriber extends DefaultInternetSubscriber<Object> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final Object o) {
            if (!isViewAttached()) return;
            getView().showLoginScreen();
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
            getView().showErrorWithRetry(new RetryAction(mLogoutAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetUserDataUseCase.dispose();
        mLogoutUseCase.dispose();
    }
}