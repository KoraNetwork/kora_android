package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.DeleteRequestRequest;
import com.kora.android.data.network.model.request.RequestRequest;
import com.kora.android.data.network.model.response.RequestListResponse;
import com.kora.android.data.network.model.response.RequestResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestService {

    @POST("requests")
    Observable<RequestResponse> addToRequests(@Body final RequestRequest requestRequest);

    @GET("requests")
    Observable<RequestListResponse> getRequestList(@Query("direction") String direction,
                                                   @Query("state") String state,
                                                   @Query("skip") int skip,
                                                   @Query("limit") int itemsPerPage);

    @POST("requests/{requestId}")
    Observable<Object> deleteRequest(@Path("requestId") String requestId,
                                     @Body final DeleteRequestRequest deleteRequestRequest);

    @PUT("requests/{requestId}")
    Observable<RequestResponse> updateRequest(@Path("requestId") String requestId);
}
