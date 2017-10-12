package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.AuthRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class RegisterUseCase extends AsyncUseCase {

    private final AuthRepository mAuthRepository;

    private UserEntity mUserEntity;

    @Inject
    public RegisterUseCase(final AuthRepository authRepository) {
        this.mAuthRepository = authRepository;
    }

    public void setData(final UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    @Override
    protected Observable buildObservableTask() {
        return mAuthRepository.register(mUserEntity);
    }
}
