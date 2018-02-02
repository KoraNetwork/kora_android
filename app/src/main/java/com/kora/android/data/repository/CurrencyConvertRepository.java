package com.kora.android.data.repository;

import com.kora.android.presentation.enums.TransactionType;

import io.reactivex.Observable;

public interface CurrencyConvertRepository {

    Observable<Double> getConvertedAmount(final String to,
                                          final double fromAmount,
                                          final TransactionType type);
}
