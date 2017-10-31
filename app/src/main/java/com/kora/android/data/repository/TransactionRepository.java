package com.kora.android.data.repository;

import com.kora.android.presentation.model.UserEntity;
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

    Observable<List<String>> createRawTransaction(final UserEntity receiver,
                                                  final double senderAmount,
                                                  final double receiverAmount,
                                                  final String pinCode);

    Observable<TransactionEntity> sendRawTransaction(final String type,
                                                     final String to,
                                                     final double fromAmount,
                                                     final double toAmount,
                                                     final List<String> rawTransactions);
}
