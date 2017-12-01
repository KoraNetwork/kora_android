package com.kora.android.domain.usecase.user;

import com.kora.android.data.repository.UserRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetUsersUseCase extends AsyncUseCase {

    private final UserRepository mUserRepository;

    private String mSearch;
    private int mSkip;
    private String mSort;
    private List<String> mExcluded;
    private boolean mGetAgents;

    @Inject
    public GetUsersUseCase(final UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void setData(final String search,
                        final int skip,
                        final List<String> excluded,
                        final boolean getAgents) {
        mSearch = search;
        mSkip = skip;
        mExcluded = excluded;
        mGetAgents = getAgents;
    }

    @Override
    protected Observable buildObservableTask() {
        return mUserRepository.getUsers(
                mSearch,
                mSkip,
                mSort,
                mExcluded,
                mGetAgents
        );
    }
}
