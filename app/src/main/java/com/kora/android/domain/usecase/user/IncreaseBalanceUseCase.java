package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class IncreaseBalanceUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    @Inject
    public IncreaseBalanceUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.increaseBalance();
    }
}
