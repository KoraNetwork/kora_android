package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.DeleteDepositRequest;
import com.kora.android.data.network.model.request.DepositRequest;
import com.kora.android.data.network.model.response.DepositListResponse;
import com.kora.android.data.network.model.response.DepositResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DepositService {

    @POST("deposit")
    Observable<DepositResponse> addRequest(@Body final DepositRequest depositRequest);

    @GET("deposit")
    Observable<DepositListResponse> getDepositList(@Query("direction") String direction,
                                                   @Query("state") String state,
                                                   @Query("skip") int skip,
                                                   @Query("limit") int itemsPerPage);

    @PUT("deposit/{depositId}")
    Observable<DepositResponse> updateRequest(@Path("depositId") String requestId);

    @POST("deposit/{depositId}")
    Observable<Object> deleteDeposit(@Path("depositId") String depositId,
                                     @Body final DeleteDepositRequest deleteRequestRequest);
}
