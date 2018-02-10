package com.kora.android.domain.usecase.transaction;

import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetTransactionUseCase extends AsyncUseCase {

    private final TransactionRepository mTransactionRepository;

    private String mTransactionId;

    @Inject
    public GetTransactionUseCase(final TransactionRepository transactionRepository) {
        mTransactionRepository = transactionRepository;
    }

    public void setData(final String transactionId) {
        mTransactionId = transactionId;
    }

    @Override
    protected Observable buildObservableTask() {
        return mTransactionRepository.getTransaction(mTransactionId);
    }
}
