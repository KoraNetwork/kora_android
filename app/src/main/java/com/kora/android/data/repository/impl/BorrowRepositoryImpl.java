package com.kora.android.data.repository.impl;

import com.kora.android.common.Keys;
import com.kora.android.data.network.model.request.BorrowRequest;
import com.kora.android.data.network.service.BorrowService;
import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.data.repository.mapper.BorrowMapper;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class BorrowRepositoryImpl implements BorrowRepository {

    private static final int GUARANTOR_1 = 1;
    private static final int GUARANTOR_2 = 2;
    private static final int GUARANTOR_3 = 3;

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
        return mBorrowService.loadBorrowList(skip, Keys.ITEMS_PER_PAGE, borrowType.text())
                .compose(mBorrowMapper.transformBorrowListResponseToBorrowEntityList());
    }

    @Override
    public Observable<BorrowEntity> addBorrowRequest(UserEntity lender, List<UserEntity> guaranters, double amount, double convertedAmount, int rate, String note, Date startDate, Date maturityDate) {
        return mBorrowService.addBorrowRequest(new BorrowRequest(amount,
                convertedAmount,
                rate,
                note,
                startDate,
                maturityDate,
                lender.getId(),
                getGuarantorId(guaranters, GUARANTOR_1),
                getGuarantorId(guaranters, GUARANTOR_2),
                getGuarantorId(guaranters, GUARANTOR_3)
        )).compose(mBorrowMapper.transformResponseToEntity());
    }

    private String getGuarantorId(List<UserEntity> guaranters, int pos) {
        if (guaranters == null) return null;
        return guaranters.size() < pos ? null : guaranters.get(pos - 1).getId();
    }
}
