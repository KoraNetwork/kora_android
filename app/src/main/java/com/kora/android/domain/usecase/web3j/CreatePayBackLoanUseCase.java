package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class CreatePayBackLoanUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private String mLoanId;
    private String mBorrowerErc20Token;
    private String mLenderErc20Token;
    private double mPayBackValue;
    private String mPinCode;

    @Inject
    public CreatePayBackLoanUseCase(Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final String loanId,
                        final String borrowerErc20Token,
                        final String lenderErc20Token,
                        final double payBackValue,
                        final String pinCode) {
        mLoanId = loanId;
        mBorrowerErc20Token = borrowerErc20Token;
        mLenderErc20Token = lenderErc20Token;
        mPayBackValue = payBackValue;
        mPinCode = pinCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.createPayBackLoan(
                mLoanId,
                mBorrowerErc20Token,
                mLenderErc20Token,
                mPayBackValue,
                mPinCode
        );
    }
}
