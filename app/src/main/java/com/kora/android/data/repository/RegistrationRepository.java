package com.kora.android.data.repository;

import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RegistrationRepository {

    Single<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber);

    Single<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                          final String confirmationCode);

    Completable register(final UserEntity userEntity);

    Single <List<CountryEntity>>getCountries();
}
