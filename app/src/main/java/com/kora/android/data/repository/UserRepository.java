package com.kora.android.data.repository;

import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import io.reactivex.Observable;

public interface UserRepository {
    Observable<UserEntity> getUserData(boolean fromNetwork);

    Observable<UserEntity> updateUser(UserEntity userEntity);

    Observable<String> updateAvatar(String avatar);

    Observable<List<UserEntity>> getRecentUsers();
}
