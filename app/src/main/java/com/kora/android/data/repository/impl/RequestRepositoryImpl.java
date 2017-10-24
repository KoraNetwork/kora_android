package com.kora.android.data.repository.impl;

import com.kora.android.data.network.model.request.RequestRequest;
import com.kora.android.data.network.service.RequestService;
import com.kora.android.data.repository.RequestRepository;
import com.kora.android.data.repository.mapper.RequestMapper;
import com.kora.android.presentation.model.RequestEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class RequestRepositoryImpl implements RequestRepository {

    private final RequestService mRequestService;
    private final RequestMapper mRequestMapper;

    @Inject
    public RequestRepositoryImpl(final RequestService requestService,
                                     final RequestMapper requestMapper) {
        mRequestService = requestService;
        mRequestMapper = requestMapper;
    }

    @Override
    public Observable<RequestEntity> addToRequests(final String to,
                                                   final double fromAmount,
                                                   final double toAmount,
                                                   final String additionalNote) {
        final RequestRequest requestRequest = new RequestRequest()
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addAdditionalNote(additionalNote);
        return mRequestService.addToRequests(requestRequest)
                .compose(mRequestMapper.transformResponseToRequestEntity());
    }
}