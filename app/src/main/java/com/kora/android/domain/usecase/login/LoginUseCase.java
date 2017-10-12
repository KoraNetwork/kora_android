package com.kora.android.domain.usecase.login;

import com.kora.android.data.repository.AuthRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class LoginUseCase extends AsyncUseCase {

    private final AuthRepository mAuthRepository;

    private String mIdentifier;
    private String mPassword;

    @Inject
    public LoginUseCase(AuthRepository loginRepository) {
        mAuthRepository = loginRepository;
    }

    public void setData(final String identifier,
                        final String password) {
        mIdentifier = identifier;
        mPassword = password;
    }

    @Override
    protected Observable buildObservableTask() {
        return mAuthRepository.login(mIdentifier, mPassword);
    }
}
