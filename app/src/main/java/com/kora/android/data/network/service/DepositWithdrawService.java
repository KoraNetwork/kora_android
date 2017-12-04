package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.DeleteDepositWithdrawRequest;
import com.kora.android.data.network.model.request.DepositWithdrawRequest;
import com.kora.android.data.network.model.response.DepositWithdrawListResponse;
import com.kora.android.data.network.model.response.DepositWithdrawResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DepositWithdrawService {

    @GET("deposit")
    Observable<DepositWithdrawListResponse> getDepositList(@Query("direction") String direction,
                                                           @Query("state") String state,
                                                           @Query("skip") int skip,
                                                           @Query("limit") int itemsPerPage);

    @GET("withdraw")
    Observable<DepositWithdrawListResponse> getWithdrawList(@Query("direction") String direction,
                                                           @Query("state") String state,
                                                           @Query("skip") int skip,
                                                           @Query("limit") int itemsPerPage);

    @POST("deposit")
    Observable<DepositWithdrawResponse> addDeposit(@Body final DepositWithdrawRequest depositWithdrawRequest);

    @POST("withdraw")
    Observable<DepositWithdrawResponse> addWithdraw(@Body final DepositWithdrawRequest depositWithdrawRequest);

    @PUT("deposit/{depositId}")
    Observable<DepositWithdrawResponse> updateDeposit(@Path("depositId") String depositId);

    @PUT("withdraw/{withdrawId}")
    Observable<DepositWithdrawResponse> updateWithdraw(@Path("withdrawId") String withdrawId);

    @POST("deposit/{depositId}")
    Observable<Object> deleteDeposit(@Path("depositId") String depositId,
                                     @Body final DeleteDepositWithdrawRequest deleteDepositWithdrawRequest);

    @POST("withdraw/{withdrawId}")
    Observable<Object> deleteWithdraw(@Path("withdrawId") String withdrawId,
                                      @Body final DeleteDepositWithdrawRequest deleteDepositWithdrawRequest);
}
