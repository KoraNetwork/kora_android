package com.kora.android.data.repository;

import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public interface BorrowRepository {

    Observable<List<BorrowEntity>> loadBorrowList(int skip, BorrowType borrowType);

    Observable<BorrowEntity> addBorrowRequest(UserEntity lender, List<UserEntity> guarantors, double amount, double convertedAmount, int rate, String note, Date startDate, Date maturityDate);

    Observable<BorrowEntity> agree(String borrowId, boolean isAgreed);

    Observable<BorrowEntity> sendCreateLoan(final String borrowId, final String rawCreateLoan);
}
