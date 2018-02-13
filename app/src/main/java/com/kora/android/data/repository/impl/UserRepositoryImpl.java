package com.kora.android.data.repository.impl;

import android.support.v4.util.Pair;

import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.model.request.CreateIdentityRequest;
import com.kora.android.data.network.model.request.ForgotPasswordRequest;
import com.kora.android.data.network.model.request.RestorePasswordRequest;
import com.kora.android.data.network.model.request.UserIdRequest;
import com.kora.android.data.network.model.response.LoginResponse;
import com.kora.android.data.network.model.response.UserResponse;
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
    public Observable<UserEntity> getUserData(final boolean fromNetwork) {
        final UserEntity userEntity = mPreferenceHandler.remindObject(Keys.Shared.USER, UserEntity.class);
        if (userEntity == null) return null;
        if (fromNetwork)
            return mUserService.getUserData()
                    .compose(mUserMapper.transformResponseToEntityUser())
                    .compose(storeUser());
        else
            return Observable.just(userEntity);
    }

    @Override
    public Observable<UserEntity> createIdentity(String ownerAddress, String recoveryAddress) {
        final CreateIdentityRequest createIdentityRequest = new CreateIdentityRequest()
                .addOwner(ownerAddress)
                .addRecovery(recoveryAddress);
        return mUserService.createIdentity(createIdentityRequest)
                .compose(mUserMapper.transformResponseToEntityUser())
                .compose(storeUser());
    }

    @Override
    public Observable<Object> increaseBalance() {
        return mUserService.increaseBalance();
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
    public Observable<Pair<List<UserEntity>, List<UserEntity>>> getUsers(final String search,
                                                                         final int skip,
                                                                         final String sort,
                                                                         final List<String> excluded,
                                                                         final boolean getAgents) {
        if (getAgents) {
            return mUserService.getAgents(search, Keys.ITEMS_PER_PAGE, skip, sort, excluded)
                    .flatMap(userListResponse ->
                            Observable.zip(
                                    mUserMapper.transformUserListResponseToEntityUserList(userListResponse.getRecents()),
                                    mUserMapper.transformUserListResponseToEntityUserList(userListResponse.getContacts()),
                                    Pair::new));
        } else {
            return mUserService.getUsers(search, Keys.ITEMS_PER_PAGE, skip, sort, excluded)
                    .flatMap(userListResponse ->
                            Observable.zip(
                                    mUserMapper.transformUserListResponseToEntityUserList(userListResponse.getRecents()),
                                    mUserMapper.transformUserListResponseToEntityUserList(userListResponse.getContacts()),
                                    Pair::new));
        }
    }

    @Override
    public Observable<Object> forgotPassword(final String email) {
        final ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
                .addEmail(email);
        return mUserService.forgotPassword(forgotPasswordRequest);
    }

    @Override
    public Observable<UserEntity> restorePassword(final String token, final String newPassword) {
        final RestorePasswordRequest restorePasswordRequest = new RestorePasswordRequest()
                .addToken(token)
                .addNewPassword(newPassword);
        return mUserService.restorePassword(restorePasswordRequest)
                .compose(saveToken())
                .compose(mUserMapper.transformResponseToEntityUser())
                .compose(storeUser());
    }

    @Override
    public Observable<UserEntity> confirmEmail(final String token) {
        return mUserService.confirmEmail(token)
                .compose(mUserMapper.transformUserListResponseToEntityUserList())
                .map(userEntityList -> userEntityList.get(0))
                .compose(storeUser());
    }

    public ObservableTransformer<UserEntity, UserEntity> storeUser() {
        return observable -> observable.map(user -> {
            mPreferenceHandler.rememberObject(Keys.Shared.USER, user);
            return user;
        });
    }

    public ObservableTransformer<LoginResponse, UserResponse> saveToken() {
        return observable -> observable.map(authResponse -> {
            mPreferenceHandler.rememberString(Keys.Shared.TOKEN, authResponse.getSessionToken());
            return authResponse.getUserResponse();
        });
    }
}
