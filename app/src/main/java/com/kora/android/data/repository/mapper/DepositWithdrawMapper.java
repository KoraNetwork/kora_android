package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.DepositWithdrawListResponse;
import com.kora.android.data.network.model.response.DepositWithdrawResponse;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.builder.DepositWithdrawEntityBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class DepositWithdrawMapper {

    private final UserMapper mUserMapper;

    @Inject
    public DepositWithdrawMapper(final UserMapper userMapper) {
        mUserMapper = userMapper;
    }

    public ObservableTransformer<DepositWithdrawResponse, DepositWithdrawEntity> transformResponseToEntity() {
        return requestResponseObservable -> requestResponseObservable
                .filter(depositWithdrawResponse ->
                        depositWithdrawResponse.getFrom() != null && depositWithdrawResponse.getTo() != null)
                .flatMap(requestResponse ->
                        Observable.zip(
                                mUserMapper.transformResponseToEntityUser(requestResponse.getFrom()),
                                mUserMapper.transformResponseToEntityUser(requestResponse.getTo()),
                                (from, to) -> new DepositWithdrawEntityBuilder()
                                        .setId(requestResponse.getId())
                                        .setFrom(from)
                                        .setTo(to)
                                        .setFromAmount(requestResponse.getFromAmount())
                                        .setToAmount(requestResponse.getToAmount())
                                        .setInterestRate(requestResponse.getInterestRate())
                                        .setState(requestResponse.getState())
                                        .setDirection(requestResponse.getDirection())
                                        .setCreatedAt(requestResponse.getCreatedAt())
                                        .createDepositEntity()));
    }

    public ObservableTransformer<DepositWithdrawListResponse, List<DepositWithdrawEntity>> transformResponseListToDepositEntity() {
        return responseListObservable -> responseListObservable
                .flatMap(depositWithdrawListResponse -> Observable.fromIterable(depositWithdrawListResponse.getResponseList())
                        .compose(transformResponseToEntity()).toList().toObservable());
    }
}
