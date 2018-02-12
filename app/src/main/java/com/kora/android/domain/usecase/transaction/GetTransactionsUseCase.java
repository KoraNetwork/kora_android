package com.kora.android.domain.usecase.transaction;

import com.kora.android.common.Keys;
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

    @Inject
    public GetTransactionsUseCase(final TransactionRepository transactionRepository) {
        mTransactionRepository = transactionRepository;
    }

    public void setData(final TransactionFilterModel data,
                        final int skip) {
        mTransactionFilter = data;
        mSkip = skip;
    }

    @Override
    protected Observable buildObservableTask() {
        return mTransactionRepository.retrieveTransactions(
                mTransactionFilter,
                Keys.ITEMS_PER_PAGE,
                mSkip);
    }
}
