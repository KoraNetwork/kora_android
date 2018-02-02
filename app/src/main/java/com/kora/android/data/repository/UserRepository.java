package com.kora.android.data.repository;

import android.support.v4.util.Pair;

import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import io.reactivex.Observable;

public interface UserRepository {

    Observable<UserEntity> getUserData(final boolean fromNetwork);

    Observable<UserEntity> createIdentity(final String ownerAddress,
                                          final String recoveryAddress);

    Observable <Object>increaseBalance();

    Observable<UserEntity> updateUser(final UserEntity userEntity);

    Observable<Pair<List<UserEntity>, List<UserEntity>>> getUsers(final String search,
                                                                  final int skip,
                                                                  final String sort,
                                                                  final List<String> excluded,
                                                                  final boolean getAgents);

    Observable<UserEntity> updateAvatar(final String avatar);

    Observable<List<UserEntity>> getRecentUsers();

    Observable<Object>addToRecent(final UserEntity userEntity);

    Observable<Object> forgotPassword(final String email);

    Observable<UserEntity> restorePassword(final String token,
                                           final String newPassword);
}
