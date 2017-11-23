package com.kora.android.domain.usecase.borrow;

import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendPayBackLoanUseCase extends AsyncUseCase {

    private BorrowRepository mBorrowRepository;

    private String mBorrowId;
    private String mRawPayBackLoan;

    @Inject
    public SendPayBackLoanUseCase(final BorrowRepository borrowRepository) {
        mBorrowRepository = borrowRepository;
    }

    public void setData(final String borrowId,
                        final String rawPayBackLoan) {
        mBorrowId = borrowId;
        mRawPayBackLoan = rawPayBackLoan;
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.sendPayBackLoan(mBorrowId, mRawPayBackLoan);
    }
}
