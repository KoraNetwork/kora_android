package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

@ConfigPersistent
public class GetCountriesUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final RegistrationRepository mRegistrationRepository;

    @Inject
    public GetCountriesUseCase(final RegistrationRepository registrationRepository) {
        mRegistrationRepository = registrationRepository;
    }

    @Override
    protected Single buildTask() {
        return mRegistrationRepository.getCountries();
    }
}
