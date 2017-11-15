package com.kora.android.domain.usecase.borrow;

import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendCreateLoanUseCase extends AsyncUseCase {

    private final BorrowRepository mBorrowRepository;

    private String mBorrowId;
    private String mRawCreateLoan;

    @Inject
    public SendCreateLoanUseCase(final BorrowRepository borrowRepository) {
        mBorrowRepository = borrowRepository;
    }

    public void setData(final String borrowId,
                        final String rawCreateLoan) {
        mRawCreateLoan = rawCreateLoan;
        mBorrowId = borrowId;
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.sendCreateLoan(mRawCreateLoan, mBorrowId);
    }
}
