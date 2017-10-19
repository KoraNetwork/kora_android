package com.kora.android.presentation.ui.send.add_contact;

import android.util.Pair;

import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.GetUsersUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class AddContactPresenter extends BasePresenter<AddContactView> {

    private final GetUsersUseCase mGetUsersUseCase;

    private String mSearch;
    private int mLimit = 10;
    private int mSkip = 0;

    private int mTotal;

    @Inject
    public AddContactPresenter(final GetUsersUseCase getUsersUseCase) {
        mGetUsersUseCase = getUsersUseCase;
    }

    public void startGetUsersTask(final int userCount, final boolean doSkip) {
        if (doSkip)
            mSkip = userCount;
        else
            mSkip = 0;
        if (mTotal != 0 && mTotal == mSkip) return;
        mGetUsersUseCase.setData(mSearch, mLimit, mSkip);
        mGetUsersUseCase.execute(new GetUsersSubscriber());
    }

    private Action mSearchUsersAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUsersUseCase.execute(new GetUsersSubscriber());
        }
    };

    public void setSearch(final String search) {
        mSearch = search;
    }

    public void setSkip(int skip) {
        mSkip = skip;
    }

    private class GetUsersSubscriber extends DefaultInternetSubscriber<Pair<Integer, List<UserEntity>>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final Pair<Integer, List<UserEntity>> pair) {
            if (!isViewAttached()) return;
            mTotal = pair.first;
            if (mSkip == 0)
                getView().showUsers(pair.second, true);
            else
                getView().showUsers(pair.second, false);

        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mSearchUsersAction));
        }
    }
}
