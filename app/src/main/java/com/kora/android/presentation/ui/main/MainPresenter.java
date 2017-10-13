package com.kora.android.presentation.ui.main;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.UserDataUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainView> {

    private final UserDataUseCase mUserDataUseCase;

    @Inject
    public MainPresenter(final UserDataUseCase userDataUseCase) {
        mUserDataUseCase = userDataUseCase;
    }

    public void loadUserData() {
        mUserDataUseCase.setData(false);
        mUserDataUseCase.execute(new GetUserSubscriber());
    }

    private Action mGetUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mUserDataUseCase.execute(new GetUserSubscriber());
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
            if (!isViewAttached()) return;
            getView().showUserData(userEntity);
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
            getView().showErrorWithRetry(new RetryAction(mGetUserAction));
        }
    }

    @Override
    public void onDetachView() {
        mUserDataUseCase.dispose();
    }
}