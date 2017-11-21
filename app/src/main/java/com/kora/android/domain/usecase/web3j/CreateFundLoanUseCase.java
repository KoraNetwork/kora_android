package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class CreateFundLoanUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private String mBorrowerErc20Token;
    private String mLenderErc20Token;
    private double mBorrowerAmount;
    private double mLenderAmount;
    private String mLoanId;
    private String mPinCode;

    @Inject
    public CreateFundLoanUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final String borrowerErc20Token,
                        final String lenderErc20Token,
                        final double borrowerAmount,
                        final double lenderAmount,
                        final String loanId,
                        final String pinCode) {
        mBorrowerErc20Token = borrowerErc20Token;
        mLenderErc20Token = lenderErc20Token;
        mBorrowerAmount = borrowerAmount;
        mLenderAmount = lenderAmount;
        mLoanId = loanId;
        mPinCode = pinCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.createFundLoan(
                mBorrowerErc20Token,
                mLenderErc20Token,
                mBorrowerAmount,
                mLenderAmount,
                mLoanId,
                mPinCode
        );
    }
}
