package com.kora.android.presentation.ui.main.fragments.deposit_withdraw;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.deposit.GetDepositListUseCase;
import com.kora.android.presentation.enums.DepositWithdrawRole;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter.DepositWithdrawFilterModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class DepositWithdrawPresenter extends BasePresenter<DepositWithdrawView> {

    private final GetDepositListUseCase mGetDepositWithdrawListUseCase;

    private DepositWithdrawRole mDepositWithdrawRole;
    private DepositWithdrawFilterModel mDepositWithdrawFilterModel = new DepositWithdrawFilterModel();

    @Inject
    public DepositWithdrawPresenter(final GetDepositListUseCase getDepositListUseCase) {
        mGetDepositWithdrawListUseCase = getDepositListUseCase;
    }

    public void retrieveDepositListWithFilter(final int itemCount) {
        retrieveDepositList(mDepositWithdrawFilterModel, itemCount);
    }

    public void retrieveDepositList(final DepositWithdrawFilterModel depositWithdrawFilterModel, final int skip) {
        Direction direction = null;
        boolean isDeposit = true;
        switch (mDepositWithdrawRole) {
            case DEPOSIT_USER:
                direction = Direction.FROM;
                isDeposit = true;
                break;
            case DEPOSIT_AGENT:
                direction = Direction.TO;
                isDeposit = true;
                break;
            case WITHDRAW_USER:
                direction = Direction.TO;
                isDeposit = false;
                break;
            case WITHDRAW_AGENT:
                direction = Direction.FROM;
                isDeposit = false;
                break;
        }
        depositWithdrawFilterModel.setDirection(direction);
        mDepositWithdrawFilterModel = depositWithdrawFilterModel;
        mGetDepositWithdrawListUseCase.setData(depositWithdrawFilterModel, skip, isDeposit);
        mGetDepositWithdrawListUseCase.execute(new GetDepositWithdrawListSubscriber());
    }

    private Action mGetDepositListAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetDepositWithdrawListUseCase.execute(new GetDepositWithdrawListSubscriber());
        }
    };

    private class GetDepositWithdrawListSubscriber extends DefaultInternetSubscriber<List<DepositWithdrawEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(List<DepositWithdrawEntity> depositWithdrawEntityList) {
            if (!isViewAttached()) return;
            getView().showDepositWithdrawList(depositWithdrawEntityList);
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

    public DepositWithdrawRole getRole() {
        return mDepositWithdrawRole;
    }

    public void setRole(final DepositWithdrawRole depositWithdrawRole) {
        mDepositWithdrawRole = depositWithdrawRole;
    }

    public DepositWithdrawFilterModel getFilter() {
        return mDepositWithdrawFilterModel;
    }

    public void setFilter(final DepositWithdrawFilterModel depositWithdrawFilterModel) {
        mDepositWithdrawFilterModel = depositWithdrawFilterModel;
    }

    @Override
    public void onDetachView() {
        mGetDepositWithdrawListUseCase.dispose();
    }
}
