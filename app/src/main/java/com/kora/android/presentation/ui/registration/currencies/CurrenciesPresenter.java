package com.kora.android.presentation.ui.registration.currencies;

import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.registration.GetCountriesUseCase;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.usecase.registration.GetCurrenciesUseCase;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class CurrenciesPresenter extends BasePresenter<CurrenciesView> {

    private final GetCurrenciesUseCase mGetCurrenciesUseCase;

    @Inject
    public CurrenciesPresenter(final GetCurrenciesUseCase getCurrenciesUseCase) {
        mGetCurrenciesUseCase = getCurrenciesUseCase;
    }

    public void startGetCountriesTask() {
        mGetCurrenciesUseCase.execute(new GetCurrenciesObserver());
    }

    private Action mGetCurrenciesAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetCurrenciesUseCase.execute(new GetCurrenciesObserver());
        }
    };

    private class GetCurrenciesObserver extends DefaultInternetSubscriber<List<CountryEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(List<CountryEntity> countries) {
            if (!isViewAttached()) return;
            getView().showCurrencies(countries);
        }

        @Override
        public void onComplete() {
            super.onComplete();
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if(!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetCurrenciesAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetCurrenciesUseCase.dispose();
    }
}
