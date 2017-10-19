package com.kora.android.data.network.service;

import com.kora.android.data.network.model.response.CurrencyResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface CurrencyConverterService {

    @GET
    Observable<CurrencyResponse> getCurrentCourse(@Url String url, @Query("q") String key, @Query("compact") String compact);
}
