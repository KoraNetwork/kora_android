package com.kora.android.data.network.sercvice;

import com.kora.android.data.network.model.request.ConfirmationCodeRequest;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrationService {

    @POST("verificationCode/send/")
    Single<PhoneNumberResponse> sendPhoneNumber(@Body final PhoneNumberRequest phoneNumberRequest);

    @POST("verificationCode/confirm/")
    Single<ConfirmationCodeResponse> sendConfirmationCode(@Body final ConfirmationCodeRequest confirmationCodeRequest);
}
