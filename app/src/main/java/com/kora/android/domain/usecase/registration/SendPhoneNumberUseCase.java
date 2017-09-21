package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;

@ConfigPersistent
public class SendPhoneNumberUseCase extends AsyncUseCase<DisposableCompletableObserver, Completable> {

    private final RegistrationRepository mRegistrationRepository;

    private String mPhoneNumber;

    @Inject
    public SendPhoneNumberUseCase(final RegistrationRepository registrationRepository) {
        mRegistrationRepository = registrationRepository;
    }

    public void setData(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    protected Completable buildTask() {
        return mRegistrationRepository.sendPhoneNumber(mPhoneNumber);
    }
}
