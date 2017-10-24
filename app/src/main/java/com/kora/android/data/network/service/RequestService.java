package com.kora.android.data.network.service;

import com.kora.android.data.network.model.request.RequestRequest;
import com.kora.android.data.network.model.response.RequestResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestService {

    @POST("requests")
    Observable<RequestResponse> addToRequests(@Body final RequestRequest requestRequest);
}
