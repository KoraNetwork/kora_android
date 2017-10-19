package com.kora.android.data.repository;

import io.reactivex.Observable;

public interface CurrencyConverterRepository {

    Observable convertAmount(double amount, String fromCurrency, String toCurrency);
}
