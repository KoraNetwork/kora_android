package com.kora.android.data.repository;

import io.reactivex.Observable;

public interface CurrencyConvertRepository {

    Observable<Double> getConvertedAmount(final String to, final double fromAmount);
}
