package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class CreateIdentityUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    private String mOwnerAddress;
    private String mRecoveryAddress;

    @Inject
    public CreateIdentityUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final String ownerAddress,
                        final String recoveryAddress) {
        mOwnerAddress = ownerAddress;
        mRecoveryAddress = recoveryAddress;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.createIdentity(mOwnerAddress, mRecoveryAddress);
    }
}
