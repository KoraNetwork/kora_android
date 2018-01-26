package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.AuthRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetCurrenciesUseCase extends AsyncUseCase {

    private final AuthRepository mAuthRepository;

    @Inject
    public GetCurrenciesUseCase(final AuthRepository authRepository) {
        mAuthRepository = authRepository;
    }

    @Override
    protected Observable buildObservableTask() {
        return mAuthRepository.getCurrencies();
    }
}
