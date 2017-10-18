package com.kora.android.domain.usecase.user;

import com.kora.android.common.Keys;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetRecentUsersUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    @Inject
    public GetRecentUsersUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.getRecentUsers();
    }
}
