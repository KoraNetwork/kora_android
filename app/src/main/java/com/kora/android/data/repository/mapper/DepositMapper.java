package com.kora.android.data.repository.mapper;


import com.kora.android.data.network.model.response.DepositListResponse;
import com.kora.android.data.network.model.response.DepositResponse;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.model.builder.DepositEntityBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class DepositMapper {

    private final UserMapper mUserMapper;

    @Inject
    public DepositMapper(final UserMapper userMapper) {
        mUserMapper = userMapper;
    }

    public ObservableTransformer<DepositResponse, DepositEntity> transformDepositResponseToDepositEntity() {
        return requestResponseObservable -> requestResponseObservable
                .filter(requestResponse -> requestResponse.getFrom() != null && requestResponse.getTo() != null)
                .flatMap(requestResponse ->
                        Observable.zip(
                                mUserMapper.transformResponseToEntityUser(requestResponse.getFrom()),
                                mUserMapper.transformResponseToEntityUser(requestResponse.getTo()),
                                (from, to) -> new DepositEntityBuilder()
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

    public ObservableTransformer<DepositListResponse, List<DepositEntity>> transformDepositResponseListToDepositEntityList() {
        return responseListObservable -> responseListObservable
                .flatMap(depositListResponse -> Observable.fromIterable(depositListResponse.getDepositResponses())
                        .compose(transformDepositResponseToDepositEntity()).toList().toObservable());
    }
}
