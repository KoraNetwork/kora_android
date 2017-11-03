package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.BorrowListResponse;
import com.kora.android.data.network.model.response.BorrowResponse;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.model.builder.BorrowEntityBuilder;

import java.util.ArrayList;
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
                        mUserMapper.transformResponseToEntityUser(borrowResponse.getGuarantor1()),
                        mUserMapper.transformResponseToEntityUser(borrowResponse.getGuarantor2()),
                        mUserMapper.transformResponseToEntityUser(borrowResponse.getGuarantor3()),
                        (sender, receiver, guarantor1, guarantor2, guarantor3) -> new BorrowEntityBuilder()
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
                                .setGuarantors(combineLists(guarantor1, guarantor2, guarantor3))
                                .createBorrowEntity()));
    }

    private List<UserEntity> combineLists(UserEntity... guarantor) {
        List<UserEntity> users = new ArrayList<>();
        for (UserEntity entity : guarantor) {
            if (entity.getId() != null)
                users.add(entity);
        }

        return users;
    }


    public ObservableTransformer<BorrowListResponse, List<BorrowEntity>> transformBorrowListResponseToBorrowEntityList() {
        return upstream -> upstream
                .flatMap(listResponse -> Observable.fromIterable(listResponse.getList())
                        .compose(transformResponseToEntity())).toList().toObservable();
    }
}
