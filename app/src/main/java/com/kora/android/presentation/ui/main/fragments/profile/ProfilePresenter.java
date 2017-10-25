package com.kora.android.presentation.ui.main.fragments.profile;

import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.domain.usecase.user.UpdateAvatarUseCase;
import com.kora.android.domain.usecase.user.UpdateUserUseCase;
import com.kora.android.presentation.enums.ViewMode;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class ProfilePresenter extends BasePresenter<ProfileView> {

    private final GetUserDataUseCase mGetUserDataUseCase;
    private final UpdateUserUseCase mUpdateUserUseCase;
    private final UpdateAvatarUseCase mUpdateAvatarUseCase;

    private UserEntity mUpdatedUserEntity = new UserEntity();
    private UserEntity mUserEntity = new UserEntity();

    @Inject
    public ProfilePresenter(final GetUserDataUseCase getUserDataUseCase,
                            final UpdateUserUseCase updateUserUseCase,
                            final UpdateAvatarUseCase updateAvatarUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mUpdateUserUseCase = updateUserUseCase;
        mUpdateAvatarUseCase = updateAvatarUseCase;
    }

    public void loadUserData() {
        mGetUserDataUseCase.execute(new GetUserSubscriber());
    }

    public void setUserName(String userName) {
        mUpdatedUserEntity.setUserName(userName);
    }

    public void setLegalName(String legalName) {
        mUpdatedUserEntity.setLegalName(legalName);
    }

    public void setEmail(String email) {
        mUpdatedUserEntity.setEmail(email);
    }

    public void setDateOfBirth(String dateOfBirth) {
        mUpdatedUserEntity.setDateOfBirth(dateOfBirth);
    }

    public void setPhoneNumber(String phoneNumber) {
        mUpdatedUserEntity.setPhoneNumber(phoneNumber);
    }

    public void setPostalCode(String postalCode) {
        mUpdatedUserEntity.setPostalCode(postalCode);
    }

    public void setAddress(String address) {
        mUpdatedUserEntity.setAddress(address);
    }

    public void onSaveClicked() {
        if (mUpdatedUserEntity.getUserName() == null || mUpdatedUserEntity.getUserName().isEmpty()) {
            getView().showEmptyUserName();
            return;
        }
        if (!StringUtils.isUserNameValid(mUpdatedUserEntity.getUserName())) {
            getView().showIncorrectUserName();
            return;
        }
        if (!StringUtils.isUserNameLongEnough(mUpdatedUserEntity.getUserName())) {
            getView().showTooShortUserName();
            return;
        }
        if (mUpdatedUserEntity.getEmail() == null || mUpdatedUserEntity.getEmail().isEmpty()) {
            getView().showEmptyEmail();
            return;
        }
        if (!StringUtils.isEmailValid(mUpdatedUserEntity.getEmail())) {
            getView().showIncorrectEmail();
            return;
        }
        if (!DateUtils.isDateValid(mUpdatedUserEntity.getDateOfBirth())) {
            getView().showIncorrectDate();
            return;
        }

        mUpdateUserUseCase.setData(mUpdatedUserEntity);
        mUpdateUserUseCase.execute(new UpdateUserSubscriber());
    }

    public void setUserEntity(UserEntity user) {
        mUserEntity = user;
    }

    public UserEntity getUserEntity() {
        return mUserEntity;
    }

    public void setCurrency(CountryEntity country) {
        mUpdatedUserEntity.setCurrency(country.getCurrency());
        mUpdatedUserEntity.setFlag(country.getFlag());
        mUpdatedUserEntity.setCountryCode(country.getCountryCode());
    }

    public void onChangeMode(ViewMode viewMode) {
        switch (viewMode) {
            case EDIT_MODE:

                break;

            case VIEW_MODE:
                mUpdatedUserEntity = mUserEntity;
                if (!isViewAttached()) return;
                getView().retrieveUserData(mUserEntity);
                break;
        }
    }

    public void updateAvatar(String absolutePath) {
        mUpdateAvatarUseCase.setData(absolutePath);
        mUpdateAvatarUseCase.execute(new UpdateAvatarSubscriber());
    }

    private Action mGetUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUserDataUseCase.execute(new GetUserSubscriber());
        }
    };

    private Action mUpdateUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mUpdateUserUseCase.execute(new GetUserSubscriber());
        }
    };

    private Action mUpdateAvatarAction = new Action() {
        @Override
        public void run() throws Exception {
            mUpdateAvatarUseCase.execute(new UpdateAvatarSubscriber());
        }
    };

    private class GetUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(UserEntity userEntity) {
            mUserEntity = userEntity;
            mUpdatedUserEntity = userEntity;
            if (!isViewAttached()) return;
            getView().retrieveUserData(userEntity);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mGetUserAction));
        }
    }

    private class UpdateUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(UserEntity userEntity) {
            mUserEntity = userEntity;
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().onUserUpdated();
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mUpdateUserAction));
        }
    }

    private class UpdateAvatarSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(UserEntity userEntity) {
            mUserEntity = userEntity;
            if (!isViewAttached()) return;
            getView().retrieveUserData(userEntity);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().onUserUpdated();
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mUpdateAvatarAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetUserDataUseCase.dispose();
        mUpdateUserUseCase.dispose();
        mUpdateAvatarUseCase.dispose();
    }
}
