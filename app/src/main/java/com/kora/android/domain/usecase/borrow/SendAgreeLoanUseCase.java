package com.kora.android.domain.usecase.borrow;

import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendAgreeLoanUseCase extends AsyncUseCase {

    private final BorrowRepository mBorrowRepository;

    private String mBorrowId;
    private String mRawAgreeLoan;

    @Inject
    public SendAgreeLoanUseCase(final BorrowRepository borrowRepository) {
        mBorrowRepository = borrowRepository;
    }

    public void setData(final String borrowId,
                        final String rawAgreeLoan) {
        mBorrowId = borrowId;
        mRawAgreeLoan = rawAgreeLoan;
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.sendAgreeLoan(mBorrowId, mRawAgreeLoan);
    }
}
