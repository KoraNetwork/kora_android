package com.kora.android.data.repository;

import com.kora.android.presentation.model.RequestEntity;

import io.reactivex.Observable;

public interface RequestRepository {

    Observable<RequestEntity> addToRequests(final String to,
                                            final double fromAmount,
                                            final double toAmount,
                                            final String additionalNote);
}
