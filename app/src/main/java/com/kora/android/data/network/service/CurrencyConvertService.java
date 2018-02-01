package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.CurrencyConvertRequest;
import com.kora.android.data.network.model.response.CurrencyConvertResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CurrencyConvertService {

    @POST("currencyConvert")
    Observable<CurrencyConvertResponse> getConvertedAmount(@Body final CurrencyConvertRequest currencyConvertRequest);
}
