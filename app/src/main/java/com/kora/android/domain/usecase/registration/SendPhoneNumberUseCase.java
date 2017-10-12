package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.AuthRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendPhoneNumberUseCase extends AsyncUseCase {

    private final AuthRepository mAuthRepository;

    private String mPhoneNumber;

    @Inject
    public SendPhoneNumberUseCase(final AuthRepository authRepository) {
        mAuthRepository = authRepository;
    }

    public void setData(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    protected Observable<Object> buildObservableTask() {
        return mAuthRepository.sendPhoneNumber(mPhoneNumber);
    }
}
