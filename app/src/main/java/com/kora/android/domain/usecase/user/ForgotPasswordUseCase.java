package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class ForgotPasswordUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    private String mEmail;

    @Inject
    public ForgotPasswordUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final String email) {
        mEmail = email;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.forgotPassword(mEmail);
    }
}
