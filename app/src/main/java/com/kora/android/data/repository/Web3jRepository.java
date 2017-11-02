package com.kora.android.data.repository;

import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.presentation.model.UserEntity;

import java.util.List;

import io.reactivex.Observable;

public interface Web3jRepository {

    Observable<IdentityCreatedResponse> createWallets(final String pinCode);

    Observable<String> getBalance(final String proxyAddress,
                                  final String smartContractAddress);

    Observable<List<String>> increaseBalance(final UserEntity userEntity,
                                             final double amount);
}
