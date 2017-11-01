package com.kora.android.domain.usecase.request;

import com.kora.android.common.Keys;
import com.kora.android.data.repository.RequestRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.ui.main.fragments.request.filter.RequestFilterModel;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetRequestListUseCase extends AsyncUseCase {

    private final RequestRepository mRequestRepository;
    private int mSkip = 0;
    private RequestFilterModel mRequestFilterModel;

    @Inject
    public GetRequestListUseCase(final RequestRepository requestRepository) {
        mRequestRepository = requestRepository;
    }

    public void setData(final RequestFilterModel filter, final int skip) {
        mRequestFilterModel = filter;
        mSkip = skip;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRequestRepository.getRequestList(mRequestFilterModel, mSkip, Keys.ITEMS_PER_PAGE_10);
    }
}
