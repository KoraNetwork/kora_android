package com.kora.android.presentation.ui.registration.countries;

import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.registration.GetCountriesUseCase;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class CountriesPresenter extends BasePresenter<CountriesView> {

    private final GetCountriesUseCase mGetCountriesUseCase;

    @Inject
    public CountriesPresenter(final GetCountriesUseCase getCountriesUseCase) {
        mGetCountriesUseCase = getCountriesUseCase;

    }

    public void startGetCountriesTask() {
        mGetCountriesUseCase.execute(new GetCountriesObserver());
    }

    private Action mGetPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetCountriesUseCase.execute(new GetCountriesObserver());
        }
    };

    private class GetCountriesObserver extends DefaultInternetSubscriber<List<Country>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final List<Country> countryList) {
            if (!isViewAttached()) return;
            getView().showCountries(countryList);
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
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mGetPhoneNumberAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetCountriesUseCase.dispose();
    }
}
