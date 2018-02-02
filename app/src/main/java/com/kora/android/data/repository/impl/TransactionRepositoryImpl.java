package com.kora.android.data.repository.impl;

import com.kora.android.data.network.model.request.RawTransactionRequest;
import com.kora.android.data.network.model.request.TransactionRequest;
import com.kora.android.data.network.service.TransactionService;
import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.data.repository.mapper.TransactionMapper;
import com.kora.android.presentation.ui.main.fragments.transactions.filter.TransactionFilterModel;
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
    public Observable<List<TransactionEntity>> retrieveTransactions(final TransactionFilterModel transactionFilterModel,
                                                                    final int limit,
                                                                    final int skip) {
        return mTransactionService.retrieveTransactionHistory(
                transactionFilterModel.getTransactionDirectionsAsStrings(),
                transactionFilterModel.getTransactionTypesAsStrings(),
                transactionFilterModel.getTransactionStatesAsStrings(),
                limit,
                skip)
                .compose(mTransactionMapper.transformTransactionListResponseToEntityList());
    }

    @Override
    public Observable<TransactionEntity> addToTransactions(final String type,
                                                           final String to,
                                                           final double fromAmount,
                                                           final double toAmount,
                                                           final List<String> transactionHash) {
        final TransactionRequest transactionRequest = new TransactionRequest()
                .addType(type)
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addTransactionHash(transactionHash);
        return mTransactionService.addToTransactions(transactionRequest)
                .compose(mTransactionMapper.transformResponseToTransactionEntity());
    }

    @Override
    public Observable<TransactionEntity> sendRawTransaction(final String type,
                                                            final String to,
                                                            final double fromAmount,
                                                            final double toAmount,
                                                            final String rawTransaction) {
        final RawTransactionRequest rawTransactionRequest = new RawTransactionRequest()
                .addType(type)
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addRawTransaction(rawTransaction);
        return mTransactionService.sendRawTransaction(rawTransactionRequest)
                .compose(mTransactionMapper.transformResponseToTransactionEntity());
    }
}