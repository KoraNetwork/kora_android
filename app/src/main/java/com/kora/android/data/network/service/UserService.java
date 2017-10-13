package com.kora.android.data.network.service;

import com.kora.android.data.network.model.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/{id}")
    Observable<UserResponse> getUserData(@Path("id") String userId);
}
