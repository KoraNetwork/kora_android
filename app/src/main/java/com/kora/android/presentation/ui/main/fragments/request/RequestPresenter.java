package com.kora.android.presentation.ui.main.fragments.request;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.request.GetRequestListUseCase;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class RequestPresenter extends BasePresenter<RequestView> {

    private final GetRequestListUseCase mGetRequestListUseCase;

    private RequestFilterModel mRequestFilterModel = new RequestFilterModel();

    @Inject
    public RequestPresenter(final GetRequestListUseCase getRequestListUseCase) {
        mGetRequestListUseCase = getRequestListUseCase;

    }

    public void retrieveRequestListWithFilter(int itemCount) {
        retrieveRequestList(mRequestFilterModel, itemCount);
    }

    public void retrieveRequestList(RequestFilterModel requestFilterModel, int skip) {
        mRequestFilterModel = requestFilterModel;
        mGetRequestListUseCase.setData(requestFilterModel, skip);
        mGetRequestListUseCase.execute(new GetRequestsSubscriber());
    }

    private Action mGetRequestListAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetRequestListUseCase.execute(new GetRequestsSubscriber());
        }
    };

    public void onFilterClicked() {
        if (!isViewAttached()) return;
        getView().openFilterDialog();
    }

    public RequestFilterModel getFilter() {
        return mRequestFilterModel;
    }

    public void setFilter(RequestFilterModel filter) {
        mRequestFilterModel = filter;
    }

    private class GetRequestsSubscriber extends DefaultInternetSubscriber<List<RequestEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
            getView().enableAndShowRefreshIndicator(true, false);
        }

        @Override
        public void onNext(List<RequestEntity> requestEntities) {
            if (!isViewAttached()) return;
            getView().showRequests(requestEntities);
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
            getView().showErrorWithRetry(new RetryAction(mGetRequestListAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetRequestListUseCase.dispose();
    }
}
