package com.kora.android.domain.usecase.borrow;

import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.enums.BorrowType;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetBorrowUseCase extends AsyncUseCase {

    private final BorrowRepository mBorrowRepository;

    private int mSkip = 0;
    private BorrowType mBorrowType;

    @Inject
    public GetBorrowUseCase(BorrowRepository borrowRepository) {
        mBorrowRepository = borrowRepository;
    }

    public void setData(int skip, BorrowType borrowType) {
        mSkip = skip;
        mBorrowType = borrowType;
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.loadBorrowList(mSkip, mBorrowType);
    }
}
