package com.kora.android.domain.usecase.request;

import com.kora.android.data.repository.RequestRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class UpdateRequestUseCase extends AsyncUseCase {

    private final RequestRepository mRequestRepository;

    private String mRequestId;

    @Inject
    public UpdateRequestUseCase(RequestRepository requestRepository) {
        mRequestRepository = requestRepository;
    }

    public void setData(final String requestId) {
        mRequestId = requestId;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRequestRepository.updateRequest(mRequestId);
    }
}
