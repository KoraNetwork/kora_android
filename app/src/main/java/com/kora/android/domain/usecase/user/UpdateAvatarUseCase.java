package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class UpdateAvatarUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;
    private String mAvatar;

    @Inject
    public UpdateAvatarUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final String avatar) {
        mAvatar = avatar;
    }


    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.updateAvatar(mAvatar);
    }
}
