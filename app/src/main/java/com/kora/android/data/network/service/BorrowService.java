package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.BorrowAgreedRequest;
import com.kora.android.data.network.model.request.BorrowRequest;
import com.kora.android.data.network.model.request.SendCreateLoanRequest;
import com.kora.android.data.network.model.response.BorrowListResponse;
import com.kora.android.data.network.model.response.BorrowResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BorrowService {

    @GET("borrow/{type}")
    Observable<BorrowListResponse> loadBorrowList(@Path("type") String borrowType,
                                                  @Query("skip") int skip,
                                                  @Query("limit")int limit);

    @POST("borrow")
    Observable<BorrowResponse> addBorrowRequest(@Body BorrowRequest borrowRequest);

    @PUT("borrow/{id}")
    Observable<BorrowResponse> agree(@Path("id") String borrowId, @Body BorrowAgreedRequest borrowAgreedRequest);

    @PUT("borrow/{id}")
    Observable<BorrowResponse> sendCreateLoan(@Path("id") final String borrowId,
                                              @Body final SendCreateLoanRequest sendCreateLoanRequest);
}
