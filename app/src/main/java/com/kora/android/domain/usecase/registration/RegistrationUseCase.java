package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class RegistrationUseCase extends AsyncUseCase {

    private final RegistrationRepository mRegistrationRepository;

    private UserEntity mUser;

    @Inject
    public RegistrationUseCase(final RegistrationRepository registrationRepository) {
        this.mRegistrationRepository = registrationRepository;
    }

    public void setData(final UserEntity user) {
       mUser = user;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRegistrationRepository.register(mUser);
    }
}
