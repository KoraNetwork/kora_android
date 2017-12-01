package com.kora.android.data.repository;

import com.kora.android.data.network.model.response.DepositListResponse;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.ui.main.fragments.deposit.filter.DepositFilterModel;

import java.util.List;

import io.reactivex.Observable;

public interface DepositRepository {

    Observable<DepositEntity> addDeposit(final String to,
                                         final double fromAmount,
                                         final double toAmount,
                                         final int interestRate);

    Observable<List<DepositEntity>> getDepositList(final DepositFilterModel depositFilterModel,
                                                   final int skip,
                                                   final int itemsPerPage);

    Observable <DepositEntity> updateDeposit(final String depositId);

    Observable<Object> deleteDeposit(final String depositId,
                                     final double fromAmount,
                                     final double toAmount,
                                     final List<String> rawTransactions);
}
