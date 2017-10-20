package com.kora.android.data.repository.impl;

import android.util.Pair;

import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.model.request.UserIdRequest;
import com.kora.android.data.network.service.UserService;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.data.repository.mapper.UserMapper;
import com.kora.android.presentation.model.UserEntity;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.miguelbcr.ui.rx_paparazzo2.interactors.ImageUtils.getMimeType;

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
    public Observable<UserEntity> updateAvatar(String avatar) {
        return mUserService.updateAvatar(getFile(avatar, "avatar"))
                .compose(mUserMapper.transformResponseToEntityUser());
    }

    private MultipartBody.Part getFile(final String imagePath, final String key) {
        if (imagePath == null)
            return null;
        final File file = new File(imagePath);
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse(getMimeType(imagePath)), file);
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }

    @Override
    public Observable<List<UserEntity>> getRecentUsers() {
        return mUserService.getRecentUsers()
                .compose(mUserMapper.transformUserListResponseToEntityUserList());
    }

    @Override
    public Observable<Object> addToRecent(UserEntity userEntity) {
        return mUserService.addToRecent(new UserIdRequest(userEntity.getId()));
    }

    @Override
    public Observable<Pair<Integer, List<UserEntity>>> getUsers(final String search,
                                                                final int limit,
                                                                final int skip,
                                                                final String sort) {
        return mUserService.getUsers(search, limit, skip, sort)
                .flatMap(userListResponse -> Observable.fromIterable(userListResponse.getData())
                        .compose(mUserMapper.transformResponseToEntityUser())
                        .toList().toObservable()
                        .flatMap(userEntityList -> Observable.just(new Pair<>(userListResponse.getTotal(), userEntityList))));
    }

    public ObservableTransformer<UserEntity, UserEntity> storeUser() {
        return observable -> observable.map(user -> {
            mPreferenceHandler.rememberObject(Keys.Shared.USER, user);
            return user;
        });
    }
}
