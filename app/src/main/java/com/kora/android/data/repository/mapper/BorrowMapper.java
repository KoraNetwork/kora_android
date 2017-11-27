package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.BorrowListResponse;
import com.kora.android.data.network.model.response.BorrowResponse;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.builder.BorrowEntityBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class BorrowMapper {

    private final UserMapper mUserMapper;

    @Inject
    public BorrowMapper(final UserMapper userMapper) {
        mUserMapper = userMapper;
    }

    public ObservableTransformer<BorrowResponse, BorrowEntity> transformResponseToEntity() {
        return upstream -> upstream
                .flatMap(borrowResponse -> Observable.zip(
                        mUserMapper.transformResponseToEntityUser(borrowResponse.getSender()),
                        mUserMapper.transformResponseToEntityUser(borrowResponse.getReceiver()),
                        mUserMapper.transformUserListResponseToEntityUserList(borrowResponse.getGuarantors()),
                        (sender, receiver, guarantors) -> new BorrowEntityBuilder()
                                .setId(borrowResponse.getId())
                                .setDirection(borrowResponse.getDirection())
                                .setRate(borrowResponse.getRate())
                                .setState(borrowResponse.getState())
                                .setAdditionalNote(borrowResponse.getAdditionalNote())
                                .setFromAmount(borrowResponse.getFromAmount())
                                .setToAmount(borrowResponse.getToAmount())
                                .setCreatedAt(borrowResponse.getCreatedAt())
                                .setStartDate(borrowResponse.getStartDate())
                                .setMaturityDate(borrowResponse.getMaturityDate())
                                .setSender(sender)
                                .setReceiver(receiver)
                                .setGuarantors(guarantors)
                                .setLoanId(borrowResponse.getLoanId())
                                .setType(borrowResponse.getType())
                                .setFromTotalAmount(borrowResponse.getFromTotalAmount())
                                .setToTotalAmount(borrowResponse.getToTotalAmount())
                                .setFromBalance(borrowResponse.getFromBalance())
                                .setToBalance(borrowResponse.getToBalance())
                                .setFromReturnedMoney(borrowResponse.getFromReturnedMoney())
                                .setToReturnedMoney(borrowResponse.getToReturnedMoney())
                                .createBorrowEntity()));
    }

    public ObservableTransformer<BorrowListResponse, List<BorrowEntity>> transformBorrowListResponseToBorrowEntityList() {
        return upstream -> upstream
                .flatMap(listResponse -> Observable.fromIterable(listResponse.getList())
                        .compose(transformResponseToEntity())).toList().toObservable();
    }
}
