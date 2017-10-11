package com.kora.android.data.repository;

import io.reactivex.Completable;

public interface LoginRepository {

    Completable login(final String identifier, final String password);
}
