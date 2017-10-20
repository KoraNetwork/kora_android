package com.kora.android.data.repository.impl;

import com.kora.android.common.Keys;
import com.kora.android.data.network.service.TransactionService;
import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.data.repository.mapper.TransactionMapper;
import com.kora.android.presentation.dto.TransactionFilterDto;
import com.kora.android.presentation.model.TransactionEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionService mTransactionService;
    private final TransactionMapper mTransactionMapper;

    @Inject
    public TransactionRepositoryImpl(final TransactionService transactionService,
                                     final TransactionMapper transactionMapper) {
        mTransactionService = transactionService;
        mTransactionMapper = transactionMapper;
    }

    @Override
    public Observable<List<TransactionEntity>> retrieveTransactions(TransactionFilterDto transactionFilter) {
        return mTransactionService.retrieveTransactionHistory(null, Keys.ITEMS_PER_PAGE, 0)
                .compose(mTransactionMapper.transformTransactionListResponseToEntityList());
    }
}
