package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.BorrowRequest;
import com.kora.android.data.network.model.response.BorrowListResponse;
import com.kora.android.data.network.model.response.BorrowResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BorrowService {

    @GET("borrow")
    Observable<BorrowListResponse> loadBorrowList(@Query("skip") int skip,
                                                  @Query("limit")int limit,
                                                  @Query("state") String borrowType);

    @POST("borrow")
    Observable<BorrowResponse> addBorrowRequest(@Body BorrowRequest borrowRequest);

}
