package com.kora.android.data.repository.impl;

import android.webkit.MimeTypeMap;

import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.model.request.ConfirmationCodeRequest;
import com.kora.android.data.network.model.request.LoginRequest;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.LoginResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.data.network.service.AuthService;
import com.kora.android.data.repository.AuthRepository;
import com.kora.android.data.repository.mapper.UserMapper;
import com.kora.android.presentation.model.CountryEntity;
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

@Singleton
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthService mAuthService;
    private final PreferenceHandler mPreferenceHandler;
    private final UserMapper mUserMapper;

    @Inject
    public AuthRepositoryImpl(final AuthService authService,
                              final PreferenceHandler preferenceHandler,
                              final UserMapper userMapper) {
        mAuthService = authService;
        mPreferenceHandler = preferenceHandler;
        mUserMapper = userMapper;
    }

    @Override
    public Observable<UserEntity> login(final String identifier, final String password) {
        final LoginRequest loginRequest = new LoginRequest()
                .addIdentifier(identifier)
                .addPassword(password);
        return mAuthService.login(loginRequest)
                .compose(saveToken())
                .compose(mUserMapper.transformResponseToEntityUser())
                .compose(storeUser());
    }

    @Override
    public Observable<List<CountryEntity>> getCountries() {
        return mAuthService.getCountries()
                .flatMap(countryResponses -> Observable.fromIterable(countryResponses)
                        .compose(mUserMapper.transformResponseToEntityCountry())
                ).toList().toObservable();
    }

    @Override
    public Observable<List<CountryEntity>> getCurrencies() {
        return mAuthService.getCurrencies()
                .flatMap(countryResponses -> Observable.fromIterable(countryResponses)
                        .compose(mUserMapper.transformResponseToEntityCountry())
                ).toList().toObservable();
    }

    @Override
    public Observable<Object> logout(final boolean fromNetwork) {
        if (fromNetwork) {
            return mAuthService.logout()
                    .map(o -> {
                        mPreferenceHandler.forgetAll();
                        return o;
                    });
        } else {
            return Observable.just(true).map(a -> {
                mPreferenceHandler.forgetAll();
                return a;
            });
        }
    }

    @Override
    public Observable<Object> sendPhoneNumber(final String phoneNumber) {
        final PhoneNumberRequest phoneNumberRequest = new PhoneNumberRequest()
                .addPhoneNumber(phoneNumber);
        return mAuthService.sendPhoneNumber(phoneNumberRequest);
    }

    @Override
    public Observable<Object> sendConfirmationCode(final String phoneNumber,
                                                                     final String confirmationCode) {
        final ConfirmationCodeRequest confirmationCodeRequest = new ConfirmationCodeRequest()
                .addPhoneNumber(phoneNumber)
                .addConfirmationCode(confirmationCode);
        return mAuthService.sendConfirmationCode(confirmationCodeRequest);
    }

    @Override
    public Observable<UserEntity> register(final UserEntity userEntity) {
        return mUserMapper.transformUserToFormData(userEntity)
                .flatMap(userMap -> mAuthService.register(userMap, getFile(userEntity.getAvatar(), "avatar"))
                        .compose(saveToken())
                        .compose(mUserMapper.transformResponseToEntityUser())
                        .compose(storeUser()));
    }

    private MultipartBody.Part getFile(final String imagePath, final String key) {
        if (imagePath == null)
            return null;
        final File file = new File(imagePath);
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse(getMimeType(imagePath)), file);
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }

    private String getMimeType(final String imagePath) {
        String type = null;
        final String extension = MimeTypeMap.getFileExtensionFromUrl(imagePath);
        if (extension != null)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return type;
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
