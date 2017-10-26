package com.kora.android.data.repository.impl;

import com.kora.android.data.network.model.request.DeleteRequestRequest;
import com.kora.android.data.network.model.request.RequestRequest;
import com.kora.android.data.network.service.RequestService;
import com.kora.android.data.repository.RequestRepository;
import com.kora.android.data.repository.mapper.RequestMapper;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;

import java.util.List;

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

    @Override
    public Observable<List<RequestEntity>> getRequestList(RequestFilterModel requestFilterModel, int skip, int itemsPerPage) {
        return mRequestService.getRequestList(requestFilterModel.getDirectionAsStrings(), requestFilterModel.getStateAsStrings(), skip, itemsPerPage)
                .compose(mRequestMapper.transformRequestResponseListToEntityList());
    }

    @Override
    public Observable<Object> deleteRequest(final String requestId,
                                            final double fromAmount,
                                            final double toAmount,
                                            final List<String> transactionHash) {
        final DeleteRequestRequest deleteRequestRequest = new DeleteRequestRequest()
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addTransactionHash(transactionHash);
        return mRequestService.deleteRequest(requestId, deleteRequestRequest);
    }
}