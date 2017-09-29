package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

@ConfigPersistent
public class SendConfirmationCodeUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final RegistrationRepository mRegistrationRepository;

    private String mPhoneNumber;
    private String mConfirmationCode;

    @Inject
    public SendConfirmationCodeUseCase(final RegistrationRepository registrationRepository) {
        mRegistrationRepository = registrationRepository;
    }

    public void setData(final String phoneNumber,
                        final String confirmationCode) {
        mPhoneNumber = phoneNumber;
        mConfirmationCode = confirmationCode;
    }

    @Override
    protected Single buildTask() {
        return mRegistrationRepository.sendConfirmationCode(mPhoneNumber, mConfirmationCode);
    }
}
