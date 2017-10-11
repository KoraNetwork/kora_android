package com.kora.android.data.network.sercvice;

import com.kora.android.data.network.model.request.LoginRequest;
import com.kora.android.data.network.model.response.LoginResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("auth/login/")
    Single<LoginResponse> login(@Body final LoginRequest loginRequest);
}
