package com.kora.android.data.repository.impl;

import com.kora.android.common.Keys;
import com.kora.android.data.network.model.request.BorrowAgreedRequest;
import com.kora.android.data.network.model.request.BorrowRequest;
import com.kora.android.data.network.model.request.SendAgreeLoanRequest;
import com.kora.android.data.network.model.request.SendCreateLoanRequest;
import com.kora.android.data.network.model.request.SendFundLoanRequest;
import com.kora.android.data.network.model.request.SendPayBackLoanRequest;
import com.kora.android.data.network.service.BorrowService;
import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.data.repository.mapper.BorrowMapper;
import com.kora.android.presentation.enums.BorrowListType;
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
    public Observable<List<BorrowEntity>> loadBorrowList(int skip, BorrowListType borrowListType) {
        return mBorrowService.loadBorrowList(borrowListType.text(), skip, Keys.ITEMS_PER_PAGE)
                .compose(mBorrowMapper.transformBorrowListResponseToBorrowEntityList());
    }

    @Override
    public Observable<BorrowEntity> addBorrowRequest(UserEntity lender,
                                                     List<UserEntity> guarantors,
                                                     double amount,
                                                     double convertedAmount,
                                                     int rate, String note,
                                                     Date startDate,
                                                     Date maturityDate) {
        return mBorrowService.addBorrowRequest(new BorrowRequest(
                amount,
                convertedAmount,
                rate,
                note,
                startDate,
                maturityDate,
                lender.getId(),
                getGuarantorId(guarantors, GUARANTOR_1),
                getGuarantorId(guarantors, GUARANTOR_2),
                getGuarantorId(guarantors, GUARANTOR_3))
        ).compose(mBorrowMapper.transformResponseToEntity());
    }

    @Override
    public Observable<BorrowEntity> agree(String borrowId, boolean isAgreed) {
        return mBorrowService.agree(borrowId, new BorrowAgreedRequest(isAgreed))
                .compose(mBorrowMapper.transformResponseToEntity());
    }

    private String getGuarantorId(List<UserEntity> guaranters, int pos) {
        if (guaranters == null) return null;
        return guaranters.size() < pos ? null : guaranters.get(pos - 1).getId();
    }

    @Override
    public Observable<BorrowEntity> sendCreateLoan(final String borrowId,
                                                   final String rawCreateLoan) {
        final SendCreateLoanRequest sendCreateLoanRequest = new SendCreateLoanRequest()
                .addRawCreateLoan(rawCreateLoan);
        return mBorrowService.sendCreateLoan(borrowId, sendCreateLoanRequest)
                .compose(mBorrowMapper.transformResponseToEntity());
    }

    @Override
    public Observable<BorrowEntity> sendAgreeLoan(final String borrowId,
                                                  final String rawAgreeLoan) {
        final SendAgreeLoanRequest sendAgreeLoanRequest = new SendAgreeLoanRequest()
                .addRawCreateLoan(rawAgreeLoan);
        return mBorrowService.sendAgreeLoan(borrowId, sendAgreeLoanRequest)
                .compose(mBorrowMapper.transformResponseToEntity());
    }

    @Override
    public Observable<BorrowEntity> sendFundLoan(final String borrowId,
                                                 final String rawApprove,
                                                 final String rawFundLoan) {
        final SendFundLoanRequest sendFundLoanRequest = new SendFundLoanRequest()
                .addRawApprove(rawApprove)
                .addRawFundLoan(rawFundLoan);
        return mBorrowService.sendFundLoan(borrowId, sendFundLoanRequest)
                .compose(mBorrowMapper.transformResponseToEntity());
    }

    @Override
    public Observable<BorrowEntity> sendPayBackLoan(final String borrowId,
                                                    final String rawApprove,
                                                    final String rawPayBackLoan) {
        final SendPayBackLoanRequest sendPayBackLoanRequest = new SendPayBackLoanRequest()
                .addRawApprove(rawApprove)
                .addRawPayBackLoan(rawPayBackLoan);
        return mBorrowService.sendPayBackLoan(borrowId, sendPayBackLoanRequest)
                .compose(mBorrowMapper.transformResponseToEntity());
    }
}
