package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SetAsRecentUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    private UserEntity mUserEntity;

    @Inject
    public SetAsRecentUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.addToRecent(mUserEntity);
    }
}
