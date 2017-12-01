package com.kora.android.data.repository.impl;

import com.kora.android.data.network.model.request.DeleteDepositRequest;
import com.kora.android.data.network.model.request.DepositRequest;
import com.kora.android.data.network.service.DepositService;
import com.kora.android.data.repository.DepositRepository;
import com.kora.android.data.repository.mapper.DepositMapper;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.ui.main.fragments.deposit.filter.DepositFilterModel;

import java.util.List;

import io.reactivex.Observable;

public class DepositRepositoryImpl implements DepositRepository {

    private final DepositService mDepositService;
    private final DepositMapper mDepositMapper;

    public DepositRepositoryImpl(final DepositService mRequestService,
                                 final DepositMapper mRequestMapper) {
        this.mDepositService = mRequestService;
        this.mDepositMapper = mRequestMapper;
    }

    @Override
    public Observable<DepositEntity> addDeposit(final String to,
                                                final double fromAmount,
                                                final double toAmount,
                                                final int interestRate) {
        final DepositRequest depositRequest = new DepositRequest()
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addInterestRate(interestRate);
        return mDepositService.addRequest(depositRequest)
                .compose(mDepositMapper.transformDepositResponseToDepositEntity());
    }

    @Override
    public Observable<List<DepositEntity>> getDepositList(final DepositFilterModel depositFilterModel,
                                                          final int skip,
                                                          final int itemsPerPage) {
        return mDepositService.getDepositList(
                depositFilterModel.getDirectionAsString(),
                depositFilterModel.getStateAsString(),
                skip,
                itemsPerPage
        ).compose(mDepositMapper.transformDepositResponseListToDepositEntityList());
    }

    @Override
    public Observable<DepositEntity> updateDeposit(final String depositId) {
        return mDepositService.updateRequest(depositId)
                .compose(mDepositMapper.transformDepositResponseToDepositEntity());
    }

    @Override
    public Observable<Object> deleteDeposit(final String depositId,
                                            final double fromAmount,
                                            final double toAmount,
                                            final List<String> rawTransactions) {
        final DeleteDepositRequest deleteDepositRequest = new DeleteDepositRequest()
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addRawTransactions(rawTransactions);
        return mDepositService.deleteDeposit(depositId, deleteDepositRequest);
    }
}
