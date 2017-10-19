package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.CurrencyConverterRepository;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ConvertAmountUseCase extends AsyncUseCase {

    private final CurrencyConverterRepository mCurrencyConverterRepository;

    private double mAmount;
    private String mFromCurrency;
    private String mToCurrency;

    @Inject
    public ConvertAmountUseCase(final CurrencyConverterRepository currencyConverterRepository) {
        mCurrencyConverterRepository = currencyConverterRepository;
    }

    public void setData(double amount, String fromCurrency, String toCurrency) {
        mAmount = amount;
        mFromCurrency = fromCurrency;
        mToCurrency = toCurrency;
    }

    @Override
    protected Observable buildObservableTask() {
        return mCurrencyConverterRepository.convertAmount(mAmount, mFromCurrency, mToCurrency);
    }
}
