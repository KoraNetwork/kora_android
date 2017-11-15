package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class CreateLoanUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private UserEntity mLender;
    private List<UserEntity> mGuarantors;
    private double mBorrowerAmount;
    private double mLenderAmount;
    private int mRate;
    private Date mStartDate;
    private Date mMaturityDate;
    private String mPinCode;

    @Inject
    public CreateLoanUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final UserEntity lender,
                        final List<UserEntity> guarantors,
                        final double borrowerAmount,
                        final double lenderAmount,
                        final int rate,
                        final Date startDate,
                        final Date maturityDate,
                        final String pinCode) {
        mLender = lender;
        mGuarantors = guarantors;
        mBorrowerAmount = borrowerAmount;
        mLenderAmount = lenderAmount;
        mRate = rate;
        mStartDate = startDate;
        mMaturityDate = maturityDate;
        mPinCode = pinCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.createLoan(
                mLender,
                mGuarantors,
                mBorrowerAmount,
                mLenderAmount,
                mRate,
                mStartDate,
                mMaturityDate,
                mPinCode
        );
    }
}
