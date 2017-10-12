package com.kora.android.data.repository.impl;

import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.data.network.model.request.LoginRequest;
import com.kora.android.data.network.sercvice.LoginService;
import com.kora.android.data.repository.LoginRepository;
import com.kora.android.data.repository.mapper.LoginMapper;
import com.kora.android.presentation.model.UserEntity;

import io.reactivex.Observable;

public class LoginRepositoryImpl implements LoginRepository {

    private final SessionPrefHelper mSessionPrefHelper;
    private final LoginService mLoginService;
    private final LoginMapper mLoginMapper;

    public LoginRepositoryImpl(final SessionPrefHelper sessionPrefHelper,
                               final LoginService loginService,
                               final LoginMapper loginMapper) {
        mSessionPrefHelper = sessionPrefHelper;
        mLoginService = loginService;
        mLoginMapper = loginMapper;
    }

    @Override
    public Observable<UserEntity> login(final String identifier, final String password) {
        final LoginRequest loginRequest = new LoginRequest()
                .addIdentifier(identifier)
                .addPassword(password);
        return mLoginService.login(loginRequest)
                .map(loginResponse -> {
                    mSessionPrefHelper.storeSessionToken(loginResponse.getSessionToken());

                    return loginResponse.getUserResponse();
                }).compose(mLoginMapper.transformResponseToEntityUser())
                .map(userEntity -> {
                    mSessionPrefHelper.storeUser(userEntity);

                    return userEntity;
                }).toObservable();
    }
}
