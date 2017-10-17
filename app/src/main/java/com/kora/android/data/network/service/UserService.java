package com.kora.android.data.network.service;

import com.kora.android.data.network.model.response.UserResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;

public interface UserService {

    @POST("profile")
    Observable<UserResponse> getUserData();

    @Multipart
    @PUT("profile")
    Observable<UserResponse> updateUser(@PartMap final Map<String, RequestBody> userMap);
}
