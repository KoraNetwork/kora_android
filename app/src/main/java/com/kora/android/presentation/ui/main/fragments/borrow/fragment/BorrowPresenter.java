package com.kora.android.presentation.ui.main.fragments.borrow.fragment;

import android.util.Log;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.borrow.GetBorrowUseCase;
import com.kora.android.presentation.enums.BorrowListType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class BorrowPresenter extends BasePresenter<BorrowView> {

    private final GetBorrowUseCase mGetBorrowUseCase;
    private BorrowListType mBorrowListType;

    @Inject
    public BorrowPresenter(final GetBorrowUseCase getBorrowUseCase) {
        mGetBorrowUseCase = getBorrowUseCase;

    }

    public void retrieveBorrowList() {
        retrieveBorrowList(0);
    }

    public void retrieveBorrowList(int skip) {
        mGetBorrowUseCase.setData(skip, mBorrowListType);
        mGetBorrowUseCase.execute(new GetBorrowsSubscriber());
    }

    public void setBorrowType(BorrowListType borrowListType) {
        mBorrowListType = borrowListType;
    }

    public BorrowListType getBorrowType() {
        return mBorrowListType;
    }

    private Action mGetBorrowsListAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetBorrowUseCase.execute(new GetBorrowsSubscriber());
        }
    };

    private class GetBorrowsSubscriber extends DefaultInternetSubscriber<List<BorrowEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(List<BorrowEntity> borrowEntities) {
            if (!isViewAttached()) return;
            getView().showData(borrowEntities);
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
            getView().showErrorWithRetry(new RetryAction(mGetBorrowsListAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetBorrowUseCase.dispose();
    }
}
