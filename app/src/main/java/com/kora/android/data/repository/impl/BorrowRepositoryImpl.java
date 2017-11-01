package com.kora.android.data.repository.impl;

import com.kora.android.common.Keys;
import com.kora.android.data.network.service.BorrowService;
import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.data.repository.mapper.BorrowMapper;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.model.BorrowEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class BorrowRepositoryImpl implements BorrowRepository {

    private final BorrowService mBorrowService;
    private final BorrowMapper mBorrowMapper;

    @Inject
    public BorrowRepositoryImpl(final BorrowService borrowService,
                                final BorrowMapper borrowMapper) {
        mBorrowService = borrowService;
        mBorrowMapper = borrowMapper;
    }

    @Override
    public Observable<List<BorrowEntity>> loadBorrowList(int skip, BorrowType borrowType) {
        return mBorrowService.loadBorrowList(skip, Keys.ITEMS_PER_PAGE_10, borrowType.text())
                .compose(mBorrowMapper.transformBorrowListResponseToBorrowEntityList());
    }
}
