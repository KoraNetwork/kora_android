package com.kora.android.presentation.ui.transactions;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.transaction.GetTransactionUseCase;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class TransactionDetailsPresenter extends BasePresenter<TransactionDetailsView> {

    private final GetTransactionUseCase mGetTransactionUseCase;

    private String mTransactionId;
    private TransactionEntity mTransactionEntity;

    @Inject
    public TransactionDetailsPresenter(final GetTransactionUseCase getTransactionUseCase) {
        mGetTransactionUseCase = getTransactionUseCase;
    }

    public String getTransactionId() {
        return mTransactionId;
    }

    public void setTransactionId(final String transactionId) {
        mTransactionId = transactionId;
    }

    public TransactionEntity getTransactionEntity() {
        return mTransactionEntity;
    }

    public void setTransactionEntity(final TransactionEntity transactionEntity) {
        mTransactionEntity = transactionEntity;
    }

    public void getTransaction() {
        if (mTransactionId == null) return;
        mGetTransactionUseCase.setData(mTransactionId);
        mGetTransactionUseCase.execute(new GetTransactionSubscriber());
    }

    private Action mGetTransactionAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetTransactionUseCase.execute(new GetTransactionSubscriber());
        }
    };

    private class GetTransactionSubscriber extends DefaultInternetSubscriber<TransactionEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(final TransactionEntity transactionEntity) {
            if (!isViewAttached()) return;
            mTransactionEntity = transactionEntity;
            getView().showTransaction(transactionEntity);
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetTransactionAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetTransactionUseCase.dispose();
    }
}
