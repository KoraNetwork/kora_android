package com.kora.android.data.repository.impl;

import com.kora.android.data.network.model.request.CurrencyConvertRequest;
import com.kora.android.data.network.model.response.CurrencyConvertResponse;
import com.kora.android.data.network.service.CurrencyConvertService;
import com.kora.android.data.repository.CurrencyConvertRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class CurrencyConvertRepositoryImpl implements CurrencyConvertRepository {

    private final CurrencyConvertService mCurrencyConvertService;

    @Inject
    public CurrencyConvertRepositoryImpl(final CurrencyConvertService currencyConvertService) {
        mCurrencyConvertService = currencyConvertService;
    }

    @Override
    public Observable<Double> getConvertedAmount(final String to,
                                                 final double fromAmount) {
        final CurrencyConvertRequest currencyConvertRequest = new CurrencyConvertRequest()
                .addTo(to)
                .addFromAmount(fromAmount);
        return mCurrencyConvertService.getConvertedAmount(currencyConvertRequest)
                .map(CurrencyConvertResponse::getToAmount);
    }
}
