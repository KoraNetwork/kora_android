package com.kora.android.domain.usecase.transaction;

import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.dto.TransactionFilterDto;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetTransactionsUseCase extends AsyncUseCase {

    private final TransactionRepository mTransactionRepository;
    private TransactionFilterDto mTransactionFilter;

    @Inject
    public GetTransactionsUseCase(final TransactionRepository transactionRepository) {
        mTransactionRepository = transactionRepository;
    }

    public void setData(TransactionFilterDto data) {
        mTransactionFilter = data;
    }

    @Override
    protected Observable buildObservableTask() {
        return mTransactionRepository.retrieveTransactions(mTransactionFilter);
    }
}
