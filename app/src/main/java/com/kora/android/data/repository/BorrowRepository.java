package com.kora.android.data.repository;

import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.model.BorrowEntity;

import java.util.List;

import io.reactivex.Observable;

public interface BorrowRepository {
    Observable<List<BorrowEntity>> loadBorrowList(int skip, BorrowType borrowType);

}
