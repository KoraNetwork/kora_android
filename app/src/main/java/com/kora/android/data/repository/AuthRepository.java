package com.kora.android.data.repository;

import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import io.reactivex.Observable;

public interface AuthRepository {

    Observable<UserEntity> login(final String identifier, final String password);

    Observable<Object> sendPhoneNumber(final String phoneNumber);

    Observable<Object> sendConfirmationCode(final String phoneNumber, final String confirmationCode);

    Observable<UserEntity> register(final UserEntity user);

    Observable<List<CountryEntity>> getCountries();

    Observable<Object> logout();
}
