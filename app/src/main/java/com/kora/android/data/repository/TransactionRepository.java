package com.kora.android.data.repository;

import com.kora.android.data.network.model.response.TransactionResponse;
import com.kora.android.presentation.dto.TransactionFilterDto;
import com.kora.android.presentation.model.TransactionEntity;

import java.util.List;

import io.reactivex.Observable;

public interface TransactionRepository {

    Observable<List<TransactionEntity>> retrieveTransactions(final TransactionFilterDto transactionFilter);

    Observable<TransactionEntity> addToTransactions(final String type,
                                                      final String to,
                                                      final double fromAmount,
                                                      final double toAmount,
                                                      final List<String> transactionHash);
}
