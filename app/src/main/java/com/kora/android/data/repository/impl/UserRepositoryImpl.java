package com.kora.android.data.repository.impl;

import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.service.UserService;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.data.repository.mapper.UserMapper;
import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    private final UserService mUserService;
    private final UserMapper mUserMapper;
    private final PreferenceHandler mPreferenceHandler;

    @Inject
    public UserRepositoryImpl(final UserService userService,
                              final UserMapper userMapper,
                              final PreferenceHandler preferenceHandler) {
        mUserService = userService;
        mPreferenceHandler = preferenceHandler;
        mUserMapper = userMapper;
    }

    @Override
    public Observable<UserEntity> getUserData(boolean fromNetwork) {
        UserEntity userEntity = mPreferenceHandler.remindObject(Keys.Shared.USER, UserEntity.class);
        if (userEntity == null) return null;
        if (fromNetwork)
            return mUserService.getUserData()
                    .compose(mUserMapper.transformResponseToEntityUser())
                    .compose(storeUser());
        else
            return Observable.just(userEntity);
    }

    @Override
    public Observable<UserEntity> updateUser(UserEntity userEntity) {
        return mUserMapper.transformUserToFormData(userEntity)
                .flatMap(userMap -> mUserService.updateUser(userMap)
                        .compose(mUserMapper.transformResponseToEntityUser())
                        .compose(storeUser()));
    }

    @Override
    public Observable<String> updateAvatar(String avatar) {
        return mUserMapper.transformAvatarToFormData(avatar)
                .flatMap(mUserService::updateAvatar)
                .map(o -> avatar);
    }

    @Override
    public Observable<List<UserEntity>> getRecentUsers() {
        return mUserService.getRecentUsers()
                .compose(mUserMapper.transformUserListResponseToEntityUserList());
    }

    public ObservableTransformer<UserEntity, UserEntity> storeUser() {
        return observable -> observable.map(user -> {
            mPreferenceHandler.rememberObject(Keys.Shared.USER, user);
            return user;
        });
    }
}
