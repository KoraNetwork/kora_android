package com.kora.android.data.repository;

import com.kora.android.presentation.ui.main.fragments.transactions.filter.TransactionFilterModel;
import com.kora.android.presentation.model.TransactionEntity;

import java.util.List;

import io.reactivex.Observable;

public interface TransactionRepository {

    Observable<List<TransactionEntity>> retrieveTransactions(final TransactionFilterModel transactionFilter,
                                                             final int limit,
                                                             final int skip);

    Observable<TransactionEntity> addToTransactions(final String type,
                                                    final String to,
                                                    final double fromAmount,
                                                    final double toAmount,
                                                    final List<String> transactionHash);

    Observable<TransactionEntity> sendRawTransaction(final String type,
                                                     final String to,
                                                     final double fromAmount,
                                                     final double toAmount,
                                                     final String rawTransaction);

    Observable<TransactionEntity> getTransaction(final String transactionId);
}
