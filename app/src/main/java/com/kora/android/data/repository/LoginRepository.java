package com.kora.android.data.repository;

import io.reactivex.Observable;

public interface LoginRepository {

    Observable login(final String identifier, final String password);
}
