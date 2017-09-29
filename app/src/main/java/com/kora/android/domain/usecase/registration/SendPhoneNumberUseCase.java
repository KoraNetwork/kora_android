package com.kora.android.domain.usecase.registration;

import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

@ConfigPersistent
public class SendPhoneNumberUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

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
    protected Single<PhoneNumberResponse> buildTask() {
        return mRegistrationRepository.sendPhoneNumber(mPhoneNumber);
    }
}
