package com.kora.android.domain.usecase.request;

import com.kora.android.data.repository.RequestRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class AddToRequestsUseCase extends AsyncUseCase {

    private final RequestRepository mRequestRepository;

    private String mTo;
    private double mFromAmount;
    private double mToAmount;
    private String mAdditionalNote;

    @Inject
    public AddToRequestsUseCase(final RequestRepository requestRepository) {
        mRequestRepository = requestRepository;
    }

    public void setData(final String to,
                        final double fromAmount,
                        final double toAmount,
                        final String additionalNote) {
        mTo = to;
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mAdditionalNote = additionalNote;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRequestRepository.addToRequests(
                mTo,
                mFromAmount,
                mToAmount,
                mAdditionalNote
        );
    }
}
