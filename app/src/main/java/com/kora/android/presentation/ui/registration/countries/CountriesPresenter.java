package com.kora.android.presentation.ui.registration.countries;

import android.util.Log;

import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.GetCountriesUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.main.MainPresenter;

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
        addDisposable(mGetCountriesUseCase.execute(new GetCountriesObserver()));
    }

    private Action mGetPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mGetCountriesUseCase.execute(new GetCountriesObserver()));
        }
    };

    private class GetCountriesObserver extends DefaultSingleObserver<List<Country>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull final List<Country> countryList) {
            if (!isViewAttached()) return;
            getView().showProgress(false);

            getView().showCountries(countryList);
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
}
