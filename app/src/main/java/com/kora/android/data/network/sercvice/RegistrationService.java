package com.kora.android.data.network.sercvice;

import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.SessionResponse;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrationService {

    @POST("phoneNumber")
    Single<SessionResponse> sendPhoneNumber(@Body final PhoneNumberRequest phoneNumberRequest);
}
