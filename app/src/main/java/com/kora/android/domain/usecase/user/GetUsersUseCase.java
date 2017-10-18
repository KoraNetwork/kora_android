package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetUsersUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    private String mSearch;
    private int mLimit;
    private int mSkip;
    private String mUseName;

    @Inject
    public GetUsersUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final String search) {
        mSearch = search;
    }

    public void setData(final int limit,
                        final int skip) {
        mLimit = limit;
        mSkip = skip;
    }

    public void setData(final String search,
                        final int limit,
                        final int skip,
                        final String userName) {
        mSearch = search;
        mLimit = limit;
        mSkip = skip;
        mUseName = userName;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.getUsers(mSearch, mLimit, mSkip, mUseName);
    }
}