package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class RegisterUseCase extends AsyncUseCase {

    private final RegistrationRepository mRegistrationRepository;

    private UserEntity mUserEntity;

    @Inject
    public RegisterUseCase(final RegistrationRepository registrationRepository) {
        this.mRegistrationRepository = registrationRepository;
    }

    public void setData(final UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRegistrationRepository.register(mUserEntity);
    }
}
