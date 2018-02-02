package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class RestorePasswordUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    private String mToken;
    private String mNewPassword;

    @Inject
    public RestorePasswordUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final String token,
                        final String newPassword) {
        mToken = token;
        mNewPassword = newPassword;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.restorePassword(mToken, mNewPassword);
    }
}
