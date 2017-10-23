package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.TransactionListResponse;
import com.kora.android.data.network.model.response.TransactionResponse;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.builder.TransactionEntityBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class TransactionMapper {

    private final UserMapper mUserMapper;

    @Inject
    public TransactionMapper(final UserMapper userMapper) {
        mUserMapper = userMapper;
    }

    public ObservableTransformer<TransactionResponse, TransactionEntity> transformResponseToTransactionEntity() {
        return transactionResponseObservable -> transactionResponseObservable
                .filter(transactionResponse -> transactionResponse.getSender() != null && transactionResponse.getReceiver() != null)
                .flatMap(transactionResponse ->
                        Observable.zip(mUserMapper.transformResponseToEntityUser(transactionResponse.getSender()),
                                mUserMapper.transformResponseToEntityUser(transactionResponse.getReceiver()),
                                (sender, receiver) -> new TransactionEntityBuilder()
                                        .setId(transactionResponse.getId())
                                        .setFromAmount(transactionResponse.getFromAmount())
                                        .setToAmount(transactionResponse.getToAmount())
                                        .setTransactionHash(transactionResponse.getTransactionHash())
                                        .setTransactionType(transactionResponse.getType())
                                        .setTransactionDirection(transactionResponse.getDirection())
                                        .setCreatedAt(transactionResponse.getCreatedAt())
                                        .setSender(sender)
                                        .setReceiver(receiver)
                                        .createTransactionEntity()));
    }

    public ObservableTransformer<TransactionListResponse, List<TransactionEntity>> transformTransactionListResponseToEntityList() {
        return responseListObservable -> responseListObservable
                .flatMap(transactionListResponse -> Observable.fromIterable(transactionListResponse.getTransactions())
                        .compose(transformResponseToTransactionEntity()).toList().toObservable());

    }
}
