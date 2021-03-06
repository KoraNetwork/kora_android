package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.CreateIdentityRequest;
import com.kora.android.data.network.model.request.ForgotPasswordRequest;
import com.kora.android.data.network.model.request.RestorePasswordRequest;
import com.kora.android.data.network.model.request.UserIdRequest;
import com.kora.android.data.network.model.response.LoginResponse;
import com.kora.android.data.network.model.response.UserListResponse;
import com.kora.android.data.network.model.response.UserResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("profile")
    Observable<UserResponse> getUserData();

    @PUT("profile/createIdentity")
    Observable<UserResponse> createIdentity(@Body CreateIdentityRequest createIdentityRequest);

    @PUT("profile/sendMoneyFromKora")
    Observable<Object> increaseBalance();

    @Multipart
    @PUT("profile")
    Observable<UserResponse> updateUser(@PartMap final Map<String, RequestBody> userMap);

    @Multipart
    @PUT("profile/avatar")
    Observable<UserResponse> updateAvatar(@Part final MultipartBody.Part imageFile);

    @GET("recents")
    Observable<List<UserResponse>> getRecentUsers();

    @GET("recents/contacts")
    Observable<UserListResponse> getUsers(@Query("search") String search,
                                          @Query("limit") int limit,
                                          @Query("skip") int skip,
                                          @Query("sort") String sort,
                                          @Query("not[]") List<String> excluded);

    @GET("recents/agents")
    Observable<UserListResponse> getAgents(@Query("search") String search,
                                           @Query("limit") int limit,
                                           @Query("skip") int skip,
                                           @Query("sort") String sort,
                                           @Query("not[]") List<String> excluded);

    @POST("recents/add")
    Observable<Object> addToRecent(@Body UserIdRequest userIdRequest);

    @POST("profile/forgotPassword")
    Observable<Object> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @PUT("profile/restorePassword")
    Observable<LoginResponse> restorePassword(@Body RestorePasswordRequest restorePasswordRequest);

    @GET("profile/confirmEmail/{token}")
    Observable<List<UserResponse>> confirmEmail(@Path("token") final String token);
}
