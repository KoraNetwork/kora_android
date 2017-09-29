package com.kora.android.data.repository;

import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;

import io.reactivex.Single;

public interface RegistrationRepository {

    Single<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber);

    Single<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                          final String confirmationCode);
}
