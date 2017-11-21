package com.kora.android.data.repository;

import com.kora.android.presentation.enums.BorrowListType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public interface BorrowRepository {

    Observable<List<BorrowEntity>> loadBorrowList(int skip, BorrowListType borrowListType);

    Observable<BorrowEntity> addBorrowRequest(UserEntity lender, List<UserEntity> guarantors, double amount, double convertedAmount, int rate, String note, Date startDate, Date maturityDate);

    Observable<BorrowEntity> agree(String borrowId, boolean isAgreed);

    Observable<BorrowEntity> sendCreateLoan(final String borrowId,
                                            final String rawCreateLoan);

    Observable<BorrowEntity> sendAgreeLoan(final String borrowId,
                                           final String rawAgreeLoan);

    Observable<BorrowEntity> sendFundLoan(final String borrowId,
                                          final List<String> rawApproves,
                                          final String rawFundLoan);
}
