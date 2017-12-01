package com.kora.android.presentation.ui.main.fragments.deposit;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.deposit.GetDepositListUseCase;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.main.fragments.deposit.filter.DepositFilterModel;
import com.kora.android.presentation.ui.main.fragments.request.RequestPresenter;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

@ConfigPersistent
public class DepositPresenter extends BasePresenter<DepositView> {

    private final GetDepositListUseCase mGetDepositListUseCase;

    private Direction mDirection;
    private DepositFilterModel mDepositFilterModel = new DepositFilterModel();

    @Inject
    public DepositPresenter(final GetDepositListUseCase getDepositListUseCase) {
        mGetDepositListUseCase = getDepositListUseCase;
    }

    public void retrieveDepositListWithFilter(final int itemCount) {
        retrieveDepositList(mDepositFilterModel, itemCount);
    }

    public void retrieveDepositList(final DepositFilterModel depositFilterModel, final int skip) {
        depositFilterModel.setDirection(mDirection);
        mDepositFilterModel = depositFilterModel;
        mGetDepositListUseCase.setData(depositFilterModel, skip);
        mGetDepositListUseCase.execute(new GetDepositListSubscriber());
    }

    private Action mGetDepositListAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetDepositListUseCase.execute(new GetDepositListSubscriber());
        }
    };

    private class GetDepositListSubscriber extends DefaultInternetSubscriber<List<DepositEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(List<DepositEntity> depositEntityList) {
            if (!isViewAttached()) return;
            getView().showDepositList(depositEntityList);
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
            getView().showErrorWithRetry(new RetryAction(mGetDepositListAction));
        }
    }
    public void onFilterClicked() {
        if (!isViewAttached()) return;
        getView().openFilterDialog();
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(final Direction direction) {
        mDirection = direction;
    }

    public DepositFilterModel getFilter() {
        return mDepositFilterModel;
    }

    public void setFilter(final DepositFilterModel depositFilterModel) {
        mDepositFilterModel = depositFilterModel;
    }

    @Override
    public void onDetachView() {
        mGetDepositListUseCase.dispose();
    }
}
