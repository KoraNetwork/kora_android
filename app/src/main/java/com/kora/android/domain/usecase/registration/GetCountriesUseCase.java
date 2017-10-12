package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.di.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetCountriesUseCase extends AsyncUseCase {

    private final RegistrationRepository mRegistrationRepository;

    @Inject
    public GetCountriesUseCase(final RegistrationRepository registrationRepository) {
        mRegistrationRepository = registrationRepository;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRegistrationRepository.getCountries();
    }
}
