package com.kora.android.domain.usecase.convert;

import com.kora.android.data.repository.CurrencyConvertRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.enums.TransactionType;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetConvertedAmountUseCase extends AsyncUseCase {

    private final CurrencyConvertRepository mCurrencyConvertRepository;

    private String mTo;
    private double mFromAmount;
    private TransactionType mType;

    @Inject
    public GetConvertedAmountUseCase(final CurrencyConvertRepository currencyConvertRepository) {
        mCurrencyConvertRepository = currencyConvertRepository;
    }

    public void setData(final String to,
                        final double fromAmount,
                        final TransactionType type) {
        mTo = to;
        mFromAmount = fromAmount;
        mType = type;
    }

    @Override
    protected Observable buildObservableTask() {
        return mCurrencyConvertRepository.getConvertedAmount(mTo, mFromAmount, mType);
    }
}
