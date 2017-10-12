package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.AuthRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendConfirmationCodeUseCase extends AsyncUseCase {

    private final AuthRepository mAuthRepository;

    private String mPhoneNumber;
    private String mConfirmationCode;

    @Inject
    public SendConfirmationCodeUseCase(final AuthRepository authRepository) {
        mAuthRepository = authRepository;
    }

    public void setData(final String phoneNumber,
                        final String confirmationCode) {
        mPhoneNumber = phoneNumber;
        mConfirmationCode = confirmationCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return mAuthRepository.sendConfirmationCode(mPhoneNumber, mConfirmationCode);
    }
}
