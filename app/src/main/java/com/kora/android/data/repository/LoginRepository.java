package com.kora.android.data.repository;

import com.kora.android.presentation.model.UserEntity;

import io.reactivex.Observable;

public interface LoginRepository {

    Observable<UserEntity> login(final String identifier, final String password);
}
