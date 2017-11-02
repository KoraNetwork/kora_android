package com.kora.android.presentation.ui.main.fragments.home;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.web3j.GetBalanceUseCase;
import com.kora.android.domain.usecase.transaction.GetTransactionsUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.main.fragments.transactions.filter.TransactionFilterModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeView> {

    private static int ITEMS_PER_PAGE = 5;

    private final GetUserDataUseCase mUserDataUseCase;
    private final GetBalanceUseCase mGetBalanceUseCase;
    private final GetTransactionsUseCase mGetTransactionsUseCase;

    private UserEntity mUserEntity;

    @Inject
    public HomePresenter(final GetUserDataUseCase userDataUseCase,
                         final GetBalanceUseCase getBalanceUseCase,
                         final GetTransactionsUseCase getTransactionsUseCase) {
        mUserDataUseCase = userDataUseCase;
        mGetBalanceUseCase = getBalanceUseCase;
        mGetTransactionsUseCase = getTransactionsUseCase;
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

    private class GetUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(final UserEntity userEntity) {
            if (!isViewAttached()) return;
            getView().showFlag(userEntity.getFlag());

            mUserEntity = userEntity;

            if (userEntity.getIdentity() != null && !userEntity.getIdentity().isEmpty()) {
                mGetBalanceUseCase.setData(userEntity.getIdentity(), userEntity.getERC20Token());
                mGetBalanceUseCase.execute(new GetBalanceSubscriber());
            } else {
                if (!isViewAttached()) return;
                getView().showProgress(false);
                getView().enableAndShowRefreshIndicator(true, false);
            }
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
            getView().enableAndShowRefreshIndicator(true, false);
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

    private Action mGetBalanceAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetBalanceUseCase.execute(new GetBalanceSubscriber());
        }
    };

    private class GetBalanceSubscriber extends DefaultWeb3jSubscriber<String> {

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

            mGetTransactionsUseCase.setData(
                    new TransactionFilterModel(),
                    ITEMS_PER_PAGE,
                    0);
            mGetTransactionsUseCase.execute(new GetTransactionsSubscriber());
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
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void handleWeb3jError(String message) {
            if(!isViewAttached()) return;
            getView().showError(message);
        }
    }

    private Action mGetTransactionsAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetTransactionsUseCase.execute(new GetTransactionsSubscriber());
        }
    };

    private class GetTransactionsSubscriber extends DefaultInternetSubscriber<List<TransactionEntity>> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final List<TransactionEntity> transactionEntityList) {
            if (!isViewAttached()) return;
            getView().showTransactions(transactionEntityList);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mGetTransactionsAction));
        }
    }

    @Override
    public void onDetachView() {
        mUserDataUseCase.dispose();
        mGetBalanceUseCase.dispose();
        mGetTransactionsUseCase.dispose();
    }
}
