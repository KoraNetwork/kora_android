package com.kora.android.domain.usecase.registration;

import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.di.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendPhoneNumberUseCase extends AsyncUseCase {

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
    protected Observable<PhoneNumberResponse> buildObservableTask() {
        return mRegistrationRepository.sendPhoneNumber(mPhoneNumber);
    }
}
