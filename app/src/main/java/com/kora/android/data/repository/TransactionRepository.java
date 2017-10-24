package com.kora.android.data.repository;

import com.kora.android.presentation.dto.TransactionFilterDto;
import com.kora.android.presentation.model.TransactionEntity;

import java.util.List;

import io.reactivex.Observable;

public interface TransactionRepository {
    Observable<List<TransactionEntity>> retrieveTransactions(TransactionFilterDto transactionFilter, int skip);

    Observable<TransactionEntity> addToTransactions(final String type,
                                                    final String to,
                                                    final double fromAmount,
                                                    final double toAmount,
                                                    final List<String> transactionHash);
}
