package com.kora.android.domain.usecase.request;

import com.kora.android.data.repository.RequestRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class DeleteRequestUseCase extends AsyncUseCase {

    private final RequestRepository mRequestRepository;

    private String mRequestId;
    private double mFromAmount;
    private double mToAmount;
    private List<String> mTransactionHash;

    @Inject
    public DeleteRequestUseCase(final RequestRepository requestRepository) {
        mRequestRepository = requestRepository;
    }

    public void setData(final String requestId,
                        final double fromAmount,
                        final double toAmount,
                        final List<String> transactionHash) {
        mRequestId = requestId;
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mTransactionHash = transactionHash;
    }

    @Override
    protected Observable buildObservableTask() {
        return mRequestRepository.deleteRequest(
                mRequestId,
                mFromAmount,
                mToAmount,
                mTransactionHash
        );
    }
}
