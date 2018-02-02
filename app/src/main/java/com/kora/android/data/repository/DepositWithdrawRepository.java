package com.kora.android.data.repository;

import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter.DepositWithdrawFilterModel;

import java.util.List;

import io.reactivex.Observable;

public interface DepositWithdrawRepository {

    Observable<DepositWithdrawEntity> addDeposit(final String to,
                                                 final double fromAmount,
                                                 final double toAmount,
                                                 final int interestRate,
                                                 final boolean isDeposit);

    Observable<List<DepositWithdrawEntity>> getDepositList(final DepositWithdrawFilterModel depositWithdrawFilterModel,
                                                           final int skip,
                                                           final int itemsPerPage,
                                                           final boolean isDeposit);

    Observable <DepositWithdrawEntity> updateDeposit(final String depositId,
                                                     final boolean isDeposit);

    Observable<Object> deleteDeposit(final String depositId,
                                     final double fromAmount,
                                     final double toAmount,
                                     final String rawTransaction,
                                     final boolean isDeposit);
}
