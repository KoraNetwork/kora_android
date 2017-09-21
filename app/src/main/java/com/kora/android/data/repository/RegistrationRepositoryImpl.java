package com.kora.android.data.repository;

import com.kora.android.common.helper.AuthPrefHelper;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.sercvice.RegistrationService;

import io.reactivex.Completable;

public class RegistrationRepositoryImpl implements RegistrationRepository {

    private final RegistrationService mRegistrationService;
    private final AuthPrefHelper mAuthPrefHelper;

    public RegistrationRepositoryImpl(final RegistrationService registrationService,
                                      final AuthPrefHelper authPrefHelper) {
        this.mRegistrationService = registrationService;
        this.mAuthPrefHelper = authPrefHelper;
    }

    @Override
    public Completable sendPhoneNumber(final String phoneNumber) {
        final PhoneNumberRequest phoneNumberRequest = new PhoneNumberRequest()
                .addPhoneNumber(phoneNumber);
        return mRegistrationService.sendPhoneNumber(phoneNumberRequest)
                .map(sessionResponse -> {
                    mAuthPrefHelper.storeSessionToken(sessionResponse.getSessionToken());
                    return true;
                }).toCompletable();
    }
}
