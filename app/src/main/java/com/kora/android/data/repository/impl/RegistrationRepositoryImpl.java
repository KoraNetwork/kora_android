package com.kora.android.data.repository.impl;

import android.webkit.MimeTypeMap;

import com.kora.android.common.preferences.Constants;
import com.kora.android.common.preferences.PreferenceHelper;
import com.kora.android.data.network.model.request.ConfirmationCodeRequest;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.network.model.response.RegistrationResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.data.network.sercvice.RegistrationService;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.data.repository.mapper.RegistrationMapper;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrationRepositoryImpl implements RegistrationRepository {

    private final RegistrationService mRegistrationService;
    private final RegistrationMapper mRegistrationMapper;

    public RegistrationRepositoryImpl(final RegistrationService registrationService,
                                      final RegistrationMapper registrationMapper) {
        mRegistrationService = registrationService;
        mRegistrationMapper = registrationMapper;
    }

    @Override
    public Observable<List<CountryEntity>> getCountries() {
        return mRegistrationService.getCountries()
                .flatMap(countryResponses -> Observable.fromIterable(countryResponses)
                        .compose(mRegistrationMapper.transformResponseToEntityCountry())
                ).toList().toObservable();
    }

    @Override
    public Observable<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber) {
        final PhoneNumberRequest phoneNumberRequest = new PhoneNumberRequest()
                .addPhoneNumber(phoneNumber);
        return mRegistrationService.sendPhoneNumber(phoneNumberRequest);
    }

    @Override
    public Observable<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                                     final String confirmationCode) {
        final ConfirmationCodeRequest confirmationCodeRequest = new ConfirmationCodeRequest()
                .addPhoneNumber(phoneNumber)
                .addConfirmationCode(confirmationCode);
        return mRegistrationService.sendConfirmationCode(confirmationCodeRequest);
    }

    @Override
    public Observable<UserEntity> register(final UserEntity userEntity) {
        return mRegistrationMapper.transformUserToFormData(userEntity)
                .flatMap(userMap -> mRegistrationService.register(userMap, getFile(userEntity.getAvatar(), "avatar"))
                        .compose(saveToken())
                        .compose(mRegistrationMapper.transformResponseToEntityUser())
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
            PreferenceHelper.rememberObject(Constants.Shared.USER, user);
            return user;
        });
    }

    public ObservableTransformer<RegistrationResponse, UserResponse> saveToken() {
        return observable -> observable.map(authResponse -> {
            PreferenceHelper.rememberString(Constants.Shared.TOKEN, authResponse.getSessionToken());
            return authResponse.getUserResponse();
        });
    }
}
