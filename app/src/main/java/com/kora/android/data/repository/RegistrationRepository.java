package com.kora.android.data.repository;

import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface RegistrationRepository {

    Observable<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber);

    Observable<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                          final String confirmationCode);

    Observable<UserResponse> register(final User user);

    Observable<List<Country>> getCountries();
}
