package com.kora.android.domain.usecase.login;

import com.kora.android.data.repository.LoginRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.di.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableCompletableObserver;

@ConfigPersistent
public class LoginUseCase extends AsyncUseCase {

    private final LoginRepository mLoginRepository;

    private String mIdentifier;
    private String mPassword;

    @Inject
    public LoginUseCase(LoginRepository loginRepository) {
        mLoginRepository = loginRepository;
    }

    public void setData(final String identifier,
                        final String password) {
        mIdentifier = identifier;
        mPassword = password;
    }

    @Override
    protected Observable buildObservableTask() {
        return mLoginRepository.login(mIdentifier, mPassword);
    }
}
