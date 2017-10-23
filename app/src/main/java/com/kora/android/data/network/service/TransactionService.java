package com.kora.android.data.network.service;

import com.kora.android.data.network.model.response.TransactionListResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TransactionService {

    @GET("transactions")
    Observable<TransactionListResponse> retrieveTransactionHistory(@Query("direction[]") List<String> direction,
                                                                   @Query("type[]") List<String> type,
                                                                   @Query("limit") int limit,
                                                                   @Query("skip") int skip);
}
