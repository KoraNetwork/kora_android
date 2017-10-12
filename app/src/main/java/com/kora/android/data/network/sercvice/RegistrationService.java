package com.kora.android.data.network.sercvice;

import com.kora.android.data.network.model.request.ConfirmationCodeRequest;
import com.kora.android.data.network.model.request.PhoneNumberRequest;
import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.CountryResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.data.network.model.response.RegistrationResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RegistrationService {

    @POST("verificationCode/send/")
    Observable<PhoneNumberResponse> sendPhoneNumber(@Body final PhoneNumberRequest phoneNumberRequest);

    @POST("verificationCode/confirm/")
    Observable<ConfirmationCodeResponse> sendConfirmationCode(@Body final ConfirmationCodeRequest confirmationCodeRequest);

    @Multipart
    @POST("auth/register/")
    Observable<RegistrationResponse> register(@PartMap final Map<String, RequestBody> userMap,
                                          @Part final MultipartBody.Part imageFile);

    @GET("countries/")
    Observable<List<CountryResponse>> getCountries();
}
