package com.kora.android.data.repository;

import io.reactivex.Completable;

public interface RegistrationRepository {

    Completable sendPhoneNumber(final String phoneNumber);
}
