package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetUserDataUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;
    private boolean mFromNetwork = true;

    @Inject
    public GetUserDataUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final boolean fromNetwork) {
        mFromNetwork = fromNetwork;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.getUserData(mFromNetwork);
    }
}
