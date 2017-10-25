package com.kora.android.presentation.ui.main.fragments.transactions;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.transaction.GetTransactionsUseCase;
import com.kora.android.presentation.ui.main.fragments.transactions.filter.TransactionFilterModel;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
class TransactionsPresenter extends BasePresenter<TransactionsView> {

    private final GetTransactionsUseCase mGetTransactionsUseCase;

    private TransactionFilterModel mTransactionFilterModel = new TransactionFilterModel();

    @Inject
    public TransactionsPresenter(final GetTransactionsUseCase getTransactionsUseCase) {
        mGetTransactionsUseCase = getTransactionsUseCase;
    }

    public void retrieveTransactions(final TransactionFilterModel transactionFilterModel, int itemCount) {
        mTransactionFilterModel = transactionFilterModel;
        mGetTransactionsUseCase.setData(transactionFilterModel, itemCount);
        mGetTransactionsUseCase.execute(new GetTransactionsSubscriber());
    }

    public void onFilterClicked() {
        if (!isViewAttached()) return;
        getView().openFilterDialog();
    }

    public void retrieveTransactionsWithFilter(int itemCount) {
        retrieveTransactions(mTransactionFilterModel, itemCount);
    }

    public TransactionFilterModel getFilter() {
        return mTransactionFilterModel;
    }

    public void setFilter(TransactionFilterModel filter) {
        mTransactionFilterModel = filter;
    }

    private Action mGetTransactionListAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetTransactionsUseCase.execute(new GetTransactionsSubscriber());
        }
    };

    private class GetTransactionsSubscriber extends DefaultInternetSubscriber<List<TransactionEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(List<TransactionEntity> transactionEntities) {
            if (!isViewAttached()) return;
            getView().showTransactions(transactionEntities);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetTransactionListAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetTransactionsUseCase.dispose();
    }
}
