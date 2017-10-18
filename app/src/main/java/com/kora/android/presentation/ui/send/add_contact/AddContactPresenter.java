package com.kora.android.presentation.ui.send.add_contact;

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

    @Inject
    public AddContactPresenter(final GetUsersUseCase getUsersUseCase) {
        mGetUsersUseCase = getUsersUseCase;
    }

    public void startGetUsersTask() {
//        mGetUsersUseCase.setData("bad");
//        mGetUsersUseCase.setData(2,0);
//        mGetUsersUseCase.setData(2,2);
//        mGetUsersUseCase.setData("kora", 2, 2, "phone");
        mGetUsersUseCase.execute(new GetUsersSubscriber());
    }


    private Action mGetPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUsersUseCase.execute(new GetUsersSubscriber());
        }
    };

    private class GetUsersSubscriber extends DefaultInternetSubscriber<List<UserEntity>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final List<UserEntity> userEntityList) {
            if (!isViewAttached()) return;
            getView().showUsers(userEntityList);
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
            getView().showErrorWithRetry(new RetryAction(mGetPhoneNumberAction));
        }
    }
}
