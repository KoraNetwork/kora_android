package com.kora.android.data.repository;

import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import io.reactivex.Observable;

public interface UserRepository {

    Observable<UserEntity> getUserData(final boolean fromNetwork);

    Observable<UserEntity> updateUser(final UserEntity userEntity);

    Observable<List<UserEntity>> getUsers(final String search,
                                          final int limit,
                                          final int skip,
                                          final String sort);
}
