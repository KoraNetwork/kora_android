package com.kora.android.domain.usecase.login;

import com.kora.android.data.repository.AuthRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class LogoutUseCase extends AsyncUseCase {

    private final AuthRepository mAuthRepository;

    private boolean mFromNetwork;

    @Inject
    public LogoutUseCase(AuthRepository loginRepository) {
        mAuthRepository = loginRepository;
    }

    public void setData(final boolean fromNetwork) {
        mFromNetwork = fromNetwork;
    }

    @Override
    protected Observable buildObservableTask() {
        return mAuthRepository.logout(mFromNetwork);
    }
}
