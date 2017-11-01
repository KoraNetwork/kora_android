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
import retrofit2.http.Query;

public interface TransactionService {

    @GET("transactions")
    Observable<TransactionListResponse> retrieveTransactionHistory(@Query("direction") String direction,
                                                                   @Query("type[]") List<String> type,
                                                                   @Query("limit") int limit,
                                                                   @Query("skip") int skip);

    @POST("transactions")
    Observable<TransactionResponse> addToTransactions(@Body final TransactionRequest transactionRequest);

    @POST("transactions")
    Observable<TransactionResponse> sendRawTransaction(@Body final RawTransactionRequest rawTransactionRequest);
}
