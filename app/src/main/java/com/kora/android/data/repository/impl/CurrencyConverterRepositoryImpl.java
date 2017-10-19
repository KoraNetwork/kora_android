package com.kora.android.data.repository.impl;

import com.kora.android.data.network.Constants;
import com.kora.android.data.network.model.response.CurrencyResponse;
import com.kora.android.data.network.service.CurrencyConverterService;
import com.kora.android.data.repository.CurrencyConverterRepository;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class CurrencyConverterRepositoryImpl implements CurrencyConverterRepository {

    private static final String SEPARATOR = "_";
    private static final String COMPACT = "n";

    private final CurrencyConverterService mCurrencyConverterService;

    @Inject
    public CurrencyConverterRepositoryImpl(final CurrencyConverterService currencyConverterService) {
        mCurrencyConverterService = currencyConverterService;
    }

    @Override
    public Observable<Double> convertAmount(double amount, String fromCurrency, String toCurrency) {
        String key = fromCurrency + SEPARATOR + toCurrency;
        return mCurrencyConverterService.getCurrentCourse(Constants.API_CONVERTER_URL, key, COMPACT)
                .map(currencyResponse -> {
                    Map<String, CurrencyResponse.Currency> currencyMap = currencyResponse.getCurrencyMap();
                    Set<Map.Entry<String, CurrencyResponse.Currency>> entries = currencyMap.entrySet();
                    Iterator iter = entries.iterator();
                    Map.Entry<String, CurrencyResponse.Currency> first = (Map.Entry<String, CurrencyResponse.Currency>) iter.next();
                    CurrencyResponse.Currency value = first.getValue();
                    return value.getValue() * amount;
                });
    }
}
