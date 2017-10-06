package com.kora.android.data.repository;

import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.CountryResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.network.model.response.RegistrationResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.model.User;

import java.util.List;

import io.reactivex.Single;

public interface RegistrationRepository {

    Single<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber);

    Single<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                          final String confirmationCode);

    Single<UserResponse> register(final User user);

    Single <List<Country>>getCountries();
}
