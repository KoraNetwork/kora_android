package com.kora.android.data.repository;

import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;

import java.util.List;

import io.reactivex.Observable;

public interface RequestRepository {

    Observable<RequestEntity> addToRequests(final String to,
                                            final double fromAmount,
                                            final double toAmount,
                                            final String additionalNote);

    Observable<List<RequestEntity>> getRequestList(RequestFilterModel requestFilterModel, int skip, int itemsPerPage);
}
