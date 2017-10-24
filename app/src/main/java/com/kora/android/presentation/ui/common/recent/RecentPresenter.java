package com.kora.android.presentation.ui.common.recent;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.GetRecentUsersUseCase;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class RecentPresenter extends BasePresenter<RecentView> {

    private final GetRecentUsersUseCase mGetRecentUsersUseCase;

    private TransactionType mTransactionType;

    @Inject
    public RecentPresenter(final GetRecentUsersUseCase getRecentUsersUseCase) {
        mGetRecentUsersUseCase = getRecentUsersUseCase;
    }

    public void setTransactionType(final String transactionType) {
        mTransactionType = TransactionType.valueOf(transactionType);
    }

    public TransactionType getTransactionType() {
        return mTransactionType;
    }

    public void getUserList() {
        mGetRecentUsersUseCase.execute(new GetRecentUserSubscriber());
    }

    @Override
    public void onDetachView() {
        mGetRecentUsersUseCase.dispose();
    }

    private Action mGetRecentUsersAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetRecentUsersUseCase.execute(new GetRecentUserSubscriber());
        }
    };

    private class GetRecentUserSubscriber extends DefaultInternetSubscriber<List<UserEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(List<UserEntity> users) {
            if (!isViewAttached()) return;
            getView().showUserList(users);
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
            getView().showErrorWithRetry(new RetryAction(mGetRecentUsersAction));
        }
    }
}
