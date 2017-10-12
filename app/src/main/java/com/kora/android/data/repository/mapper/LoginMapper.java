package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.SingleTransformer;

public class LoginMapper {

    @Inject
    public LoginMapper() {

    }

    public SingleTransformer<UserResponse, UserEntity> transformResponseToEntityUser() {
        return userResponseObservable -> userResponseObservable
                .map(userResponse -> new UserEntity()
                        .addAvatar(userResponse.getAvatar())
                        .addPhoneNumber(userResponse.getPhoneNumber())
                        .addIdentity(userResponse.getIdentity())
                        .addCreator(userResponse.getCreator())
                        .addRecoveryKey(userResponse.getRecoveryKey())
                        .addOwner(userResponse.getOwner())
                        .addUserName(userResponse.getUserName())
                        .addLegalName(userResponse.getLegalName())
                        .addEmail(userResponse.getEmail())
                        .addDateOfBirth(userResponse.getDateOfBirth())
                        .addCurrency(userResponse.getCurrency())
                        .addPostalCode(userResponse.getPostalCode())
                        .addAddress(userResponse.getAddress())
                );
    }
}
