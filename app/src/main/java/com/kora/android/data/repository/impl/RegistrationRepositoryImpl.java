package com.kora.android.data.repository.impl;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.data.repository.mapper.RegistrationMapper;
import com.kora.android.data.network.model.request.ConfirmationCodeRequest;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.network.sercvice.RegistrationService;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrationRepositoryImpl implements RegistrationRepository {

    private final SessionPrefHelper mSessionPrefHelper;
    private final RegistrationService mRegistrationService;
    private final RegistrationMapper mRegistrationMapper;

    public RegistrationRepositoryImpl(final SessionPrefHelper sessionPrefHelper,
                                      final RegistrationService registrationService,
                                      final RegistrationMapper registrationMapper) {
        mSessionPrefHelper = sessionPrefHelper;
        mRegistrationService = registrationService;
        mRegistrationMapper = registrationMapper;
    }

    @Override
    public Single<List<CountryEntity>> getCountries() {
        return mRegistrationService.getCountries()
                .flatMap(countryResponses -> Observable.fromIterable(countryResponses)
                        .compose(mRegistrationMapper.transformResponseToEntityCountry())
                ).toList();
    }

    @Override
    public Single<PhoneNumberResponse> sendPhoneNumber(final String phoneNumber) {
        final PhoneNumberRequest phoneNumberRequest = new PhoneNumberRequest()
                .addPhoneNumber(phoneNumber);
        return mRegistrationService.sendPhoneNumber(phoneNumberRequest);
    }

    @Override
    public Single<ConfirmationCodeResponse> sendConfirmationCode(final String phoneNumber,
                                                                 final String confirmationCode) {
        final ConfirmationCodeRequest confirmationCodeRequest = new ConfirmationCodeRequest()
                .addPhoneNumber(phoneNumber)
                .addConfirmationCode(confirmationCode);
        return mRegistrationService.sendConfirmationCode(confirmationCodeRequest);
    }

    @Override
    public Completable register(final UserEntity userEntity) {
        return mRegistrationMapper.transformUserToFormData(userEntity)
                .flatMap(userMap ->
                        mRegistrationService.register(userMap, getFile(userEntity.getAvatar(), "avatar"))
                                .map(registrationResponse -> {
                                    mSessionPrefHelper.storeSessionToken(registrationResponse.getSessionToken());

                                    Log.e("_____", registrationResponse.getSessionToken());

                                    return registrationResponse.getUserResponse();
                                }).compose(mRegistrationMapper.transformResponseToEntityUser())
                                .map(userEntity1 -> {
                                    mSessionPrefHelper.storeUser(userEntity1);

                                    Log.e("_____", userEntity1.toString());

                                    return userEntity1;
                                })
                ).toCompletable();
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
}
