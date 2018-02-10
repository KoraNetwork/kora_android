package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.RawTransactionRequest;
import com.kora.android.data.network.model.request.TransactionRequest;
import com.kora.android.data.network.model.response.TransactionListResponse;
import com.kora.android.data.network.model.response.TransactionResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TransactionService {

    @GET("transactions")
    Observable<TransactionListResponse> retrieveTransactionHistory(@Query("direction") final String direction,
                                                                   @Query("type[]") final List<String> type,
                                                                   @Query("state[]") final List<String> state,
                                                                   @Query("limit") final int limit,
                                                                   @Query("skip") final int skip);

    @POST("transactions")
    Observable<TransactionResponse> addToTransactions(@Body final TransactionRequest transactionRequest);

    @POST("transactions")
    Observable<TransactionResponse> sendRawTransaction(@Body final RawTransactionRequest rawTransactionRequest);

    @GET("transactions/{transactionId}")
    Observable<TransactionResponse> getTransaction(@Path("transactionId") final String requestId);
}
