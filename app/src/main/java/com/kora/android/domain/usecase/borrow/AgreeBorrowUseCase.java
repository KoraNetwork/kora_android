package com.kora.android.domain.usecase.borrow;

import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class AgreeBorrowUseCase extends AsyncUseCase {

    private final BorrowRepository mBorrowRepository;

    private String  mBorrowId;
    private boolean mIsAgreed;

    @Inject
    public AgreeBorrowUseCase(final BorrowRepository borrowRepository) {
        mBorrowRepository = borrowRepository;
    }

    public void agree(String borowId, boolean isAgree) {
        mBorrowId = borowId;
        mIsAgreed = isAgree;
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.agree(mBorrowId, mIsAgreed);
    }
}
