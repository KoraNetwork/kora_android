package com.kora.android.data.repository;

import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.data.network.model.request.ConfirmationCodeRequest;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.network.sercvice.RegistrationService;

import io.reactivex.Completable;
import io.reactivex.Single;

public class RegistrationRepositoryImpl implements RegistrationRepository {

    private final RegistrationService mRegistrationService;

    public RegistrationRepositoryImpl(final RegistrationService registrationService) {
        this.mRegistrationService = registrationService;
    }

    @Override
    public Single<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber) {
        final PhoneNumberRequest phoneNumberRequest = new PhoneNumberRequest()
                .addPhoneNumber(phoneNumber);
        return mRegistrationService.sendPhoneNumber(phoneNumberRequest);
    }

    @Override
    public Single<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                                 final String confirmationCode) {
        final ConfirmationCodeRequest confirmationCodeRequest = new ConfirmationCodeRequest()
                .addPhoneNumber(phoneNumber)
                .addConfirmationCode(confirmationCode);
        return mRegistrationService.sendConfirmationCode(confirmationCodeRequest);
    }
}
