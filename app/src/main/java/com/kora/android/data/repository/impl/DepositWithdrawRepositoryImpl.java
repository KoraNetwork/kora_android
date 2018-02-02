package com.kora.android.data.repository.impl;

import com.kora.android.data.network.model.request.DeleteDepositWithdrawRequest;
import com.kora.android.data.network.model.request.DepositWithdrawRequest;
import com.kora.android.data.network.service.DepositWithdrawService;
import com.kora.android.data.repository.DepositWithdrawRepository;
import com.kora.android.data.repository.mapper.DepositWithdrawMapper;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter.DepositWithdrawFilterModel;

import java.util.List;

import io.reactivex.Observable;

public class DepositWithdrawRepositoryImpl implements DepositWithdrawRepository {

    private final DepositWithdrawService mDepositWithdrawService;
    private final DepositWithdrawMapper mDepositWithdrawMapper;

    public DepositWithdrawRepositoryImpl(final DepositWithdrawService depositWithdrawService,
                                         final DepositWithdrawMapper depositWithdrawMapper) {
        this.mDepositWithdrawService = depositWithdrawService;
        this.mDepositWithdrawMapper = depositWithdrawMapper;
    }

    @Override
    public Observable<List<DepositWithdrawEntity>> getDepositList(final DepositWithdrawFilterModel depositWithdrawFilterModel,
                                                                  final int skip,
                                                                  final int itemsPerPage,
                                                                  final boolean isDeposit) {
        if (isDeposit) {
            return mDepositWithdrawService.getDepositList(
                    depositWithdrawFilterModel.getDirectionAsString(),
                    depositWithdrawFilterModel.getStateAsString(),
                    skip,
                    itemsPerPage
            ).compose(mDepositWithdrawMapper.transformResponseListToDepositEntity());
        } else {
            return mDepositWithdrawService.getWithdrawList(
                    depositWithdrawFilterModel.getDirectionAsString(),
                    depositWithdrawFilterModel.getStateAsString(),
                    skip,
                    itemsPerPage
            ).compose(mDepositWithdrawMapper.transformResponseListToDepositEntity());
        }
    }

    @Override
    public Observable<DepositWithdrawEntity> addDeposit(final String to,
                                                        final double fromAmount,
                                                        final double toAmount,
                                                        final int interestRate,
                                                        final boolean isDeposit) {
        final DepositWithdrawRequest depositWithdrawRequest = new DepositWithdrawRequest()
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addInterestRate(interestRate);
        if (isDeposit) {
            return mDepositWithdrawService.addDeposit(depositWithdrawRequest)
                    .compose(mDepositWithdrawMapper.transformResponseToEntity());
        } else {
            return mDepositWithdrawService.addWithdraw(depositWithdrawRequest)
                    .compose(mDepositWithdrawMapper.transformResponseToEntity());
        }
    }

    @Override
    public Observable<DepositWithdrawEntity> updateDeposit(final String depositId,
                                                           final boolean isDeposit) {
        if (isDeposit) {
            return mDepositWithdrawService.updateDeposit(depositId)
                    .compose(mDepositWithdrawMapper.transformResponseToEntity());
        } else {
            return mDepositWithdrawService.updateWithdraw(depositId)
                    .compose(mDepositWithdrawMapper.transformResponseToEntity());
        }
    }

    @Override
    public Observable<Object> deleteDeposit(final String depositId,
                                            final double fromAmount,
                                            final double toAmount,
                                            final String rawTransaction,
                                            final boolean isDeposit) {
        final DeleteDepositWithdrawRequest deleteDepositWithdrawRequest = new DeleteDepositWithdrawRequest()
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addRawTransaction(rawTransaction);
        if (isDeposit) {
            return mDepositWithdrawService.deleteDeposit(depositId, deleteDepositWithdrawRequest);
        } else {
            return mDepositWithdrawService.deleteWithdraw(depositId, deleteDepositWithdrawRequest);
        }
    }
}
