package com.kora.android.data.repository.impl;

import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.service.UserService;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.data.repository.mapper.AuthMapper;
import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    private final UserService mUserService;
    private final AuthMapper mAuthMapper;
    private final PreferenceHandler mPreferenceHandler;

    @Inject
    public UserRepositoryImpl(final UserService userService,
                              final AuthMapper authMapper,
                              final PreferenceHandler preferenceHandler) {
        mUserService = userService;
        mPreferenceHandler = preferenceHandler;
        mAuthMapper = authMapper;
    }

    @Override
    public Observable<UserEntity> getUserData(boolean fromNetwork) {
        UserEntity userEntity = mPreferenceHandler.remindObject(Keys.Shared.USER, UserEntity.class);
        if (userEntity == null) return null;
        if (fromNetwork)
            return mUserService.getUserData()
                    .compose(mAuthMapper.transformResponseToEntityUser())
                    .compose(storeUser());
        else
            return Observable.just(userEntity);
    }

    @Override
    public Observable<UserEntity> updateUser(UserEntity userEntity) {
        return mAuthMapper.transformUserToFormData(userEntity)
                .flatMap(userMap -> mUserService.updateUser(userMap)
                        .compose(mAuthMapper.transformResponseToEntityUser())
                        .compose(storeUser()));
    }

    @Override
    public Observable<List<UserEntity>> getUsers(final String search,
                                                 final int limit,
                                                 final int skip,
                                                 final String sort) {
        return mUserService.getUsers(search, limit, skip, sort)
                .flatMap(userResponses -> Observable.fromIterable(userResponses)
                        .compose(mAuthMapper.transformResponseToEntityUser())
                ).toList().toObservable();
    }

    public ObservableTransformer<UserEntity, UserEntity> storeUser() {
        return observable -> observable.map(user -> {
            mPreferenceHandler.rememberObject(Keys.Shared.USER, user);
            return user;
        });
    }
}
