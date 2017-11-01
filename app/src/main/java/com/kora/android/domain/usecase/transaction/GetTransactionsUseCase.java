package com.kora.android.domain.usecase.transaction;

import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.ui.main.fragments.transactions.filter.TransactionFilterModel;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetTransactionsUseCase extends AsyncUseCase {

    private final TransactionRepository mTransactionRepository;
    private TransactionFilterModel mTransactionFilter;
    private int mSkip = 0;
    private int mLimit = 0;

    @Inject
    public GetTransactionsUseCase(final TransactionRepository transactionRepository) {
        mTransactionRepository = transactionRepository;
    }

    public void setData(final TransactionFilterModel data,
                        final int limit,
                        final int skip) {
        mTransactionFilter = data;
        mLimit = limit;
        mSkip = skip;
    }

    @Override
    protected Observable buildObservableTask() {
        return mTransactionRepository.retrieveTransactions(mTransactionFilter, mLimit, mSkip);
    }
}
