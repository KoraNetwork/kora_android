package com.kora.android.presentation.ui.home;

import android.util.Log;

import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.balance.GetBalanceUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.common.Keys.Shared.USER;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeView> {

    private final PreferenceHandler mPreferenceHandler;
    private final GetBalanceUseCase mGetBalanceUseCase;

    private UserEntity mUserEntity;

    @Inject
    public HomePresenter(final PreferenceHandler preferenceHandler,
                         final GetBalanceUseCase getBalanceUseCase) {
        mPreferenceHandler = preferenceHandler;
        mGetBalanceUseCase = getBalanceUseCase;
    }

    public void startGetBalanceTask() {
        if (mPreferenceHandler.isRemember(USER)) {
            mUserEntity = mPreferenceHandler.remindObject(USER, UserEntity.class);
            getView().showFlag(mUserEntity.getFlag());

            mGetBalanceUseCase.setData(mUserEntity.getIdentity(), mUserEntity.getERC20Token());
            mGetBalanceUseCase.execute(new GetBalanceUseObserver());
        }
    }

    private Action mCreateIdentityAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetBalanceUseCase.execute(new GetBalanceUseObserver());
        }
    };

    private class GetBalanceUseObserver extends DefaultInternetSubscriber<String> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final String balance) {
            if(!isViewAttached()) return;
            getView().showBalance(balance + " " + mUserEntity.getCurrency());
            getView().showCurrencyName(mUserEntity.getCurrencyNameFull());
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if(!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mCreateIdentityAction));
        }
    }
}
