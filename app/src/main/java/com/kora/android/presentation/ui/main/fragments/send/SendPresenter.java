package com.kora.android.presentation.ui.main.fragments.send;

import android.support.v4.util.Pair;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultDisposableObserver;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.GetUsersUseCase;
import com.kora.android.domain.usecase.user.SetAsRecentUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class SendPresenter extends BasePresenter<SendView> {

    private final GetUsersUseCase mGetUsersUseCase;
    private final SetAsRecentUseCase mSetAsRecentUseCase;

    private String mSearch;

    @Inject
    public SendPresenter(final GetUsersUseCase getUsersUseCase,
                         final SetAsRecentUseCase setAsRecentUseCase) {
        mGetUsersUseCase = getUsersUseCase;
        mSetAsRecentUseCase = setAsRecentUseCase;
    }

    public void getUsers() {
        getUsers(0);
    }

    public void getUsers(final int skip) {
        mGetUsersUseCase.setData(mSearch, skip, new ArrayList<>(), false);
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


    public void setAsRecent(UserEntity item) {
        mSetAsRecentUseCase.setData(item);
        mSetAsRecentUseCase.execute(new DefaultDisposableObserver());
    }

    private class GetUsersSubscriber extends DefaultInternetSubscriber<Pair<List<UserEntity>, List<UserEntity>>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final Pair<List<UserEntity>, List<UserEntity>> users) {
            if (!isViewAttached()) return;
            getView().showUsers(users);
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
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            super.handleUnprocessableEntity(errorModel);
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mSearchUsersAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetUsersUseCase.dispose();
        mSetAsRecentUseCase.dispose();
    }
}