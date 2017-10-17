package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

public class UpdateUserUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;
    private UserEntity mUserEntity;

    @Inject
    public UpdateUserUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.updateUser(mUserEntity);
    }
}
