package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;

@ConfigPersistent
public class RegisterUseCase extends AsyncUseCase<DisposableCompletableObserver, Completable> {

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
    protected Completable buildTask() {
        return mRegistrationRepository.register(mUserEntity);
    }
}
