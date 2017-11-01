package com.kora.android.presentation.ui.common.send_to;

import android.support.annotation.Nullable;

import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultDisposableObserver;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.request.AddToRequestsUseCase;
import com.kora.android.domain.usecase.request.UpdateRequestUseCase;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.domain.usecase.user.SetAsRecentUseCase;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class RequestDetailsPresenter extends BasePresenter<RequestDetailsView> {

    private final GetUserDataUseCase mGetUserDataUseCase;
    private final ConvertAmountUseCase mConvertAmountUseCase;
    private final SetAsRecentUseCase mSetAsRecentUseCase;
    private final AddToRequestsUseCase mAddToRequestsUseCase;
    private final UpdateRequestUseCase mUpdateRequestUseCase;

    private UserEntity mSender;
    private UserEntity mReceiver;
    @Nullable
    private RequestEntity mRequest;

    @Inject
    public RequestDetailsPresenter(final GetUserDataUseCase getUserDataUseCase,
                                   final ConvertAmountUseCase convertAmountUseCase,
                                   final SetAsRecentUseCase setAsRecentUseCase,
                                   final AddToRequestsUseCase addToRequestsUseCase,
                                   final UpdateRequestUseCase updateRequestUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mConvertAmountUseCase = convertAmountUseCase;
        mSetAsRecentUseCase = setAsRecentUseCase;
        mAddToRequestsUseCase = addToRequestsUseCase;
        mUpdateRequestUseCase = updateRequestUseCase;
    }

    public void getCurrentUser() {
        mGetUserDataUseCase.setData(false);
        mGetUserDataUseCase.execute(new GetUserSubscriber());
    }

    private Action mGetCurrentUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetUserDataUseCase.execute(new GetUserSubscriber());
        }
    };

    public void setReceiver(UserEntity receiver) {
        mReceiver = receiver;
        if (!isViewAttached()) return;
        if (receiver == null) return;
        getView().retrieveReceiver(receiver);

    }

    public void setSender(UserEntity sender) {
        mSender = sender;
        if (!isViewAttached()) return;
        if (sender == null) return;
        getView().retrieveSender(sender);
    }

    public void convertIfNeed(String amountString) {
        if (mSender == null || mReceiver == null) return;
        double amount = Double.valueOf(amountString);
        if (mSender.getCurrency().equals(mReceiver.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(amount, mReceiver.getCurrency());
            return;
        }
        mConvertAmountUseCase.setData(amount, mSender.getCurrency(), mReceiver.getCurrency());
        mConvertAmountUseCase.execute(new ConvertSubscriber());
    }

    public void sendMoney(String senderAmount, String receiverAmount, String additionalNote) {

        if (validateForm(senderAmount, receiverAmount)) return;

        Double sAmount = Double.valueOf(senderAmount);
        Double rAmount = Double.valueOf(receiverAmount);

        getView().openPinScreen(mReceiver, sAmount, rAmount, null);
    }

    private boolean validateForm(String senderAmount, String receiverAmount) {
        if (Validator.isEmpty(senderAmount)) {
            if (getView() == null) return true;
            getView().emptySenderAmountError();
            return true;
        }
        if (Validator.isEmpty(receiverAmount)) {
            if (getView() == null) return true;
            getView().emptyReceiverAmountError();
            return true;
        }
        return false;
    }

    public void sendRequest(String senderAmount, String receiverAmount, String additionalNote) {

        if (validateForm(senderAmount, receiverAmount)) return;

        Double sAmount = Double.valueOf(senderAmount);
        Double rAmount = Double.valueOf(receiverAmount);

        mAddToRequestsUseCase.setData(
                mReceiver.getId(),
                sAmount,
                rAmount,
                additionalNote);
        mAddToRequestsUseCase.execute(new AddToRequestsSubscriber());
    }

    private Action mAddToRequestsAction = new Action() {
        @Override
        public void run() throws Exception {
            mAddToRequestsUseCase.execute(new AddToRequestsSubscriber());
        }
    };

    public void setRequest(RequestEntity request) {
        mRequest = request;
        if (request == null) return;
        if (mRequest.getDirection() == Direction.FROM) {
            setReceiver(mRequest.getTo());
            setSender(mRequest.getFrom());
        } else {
            setReceiver(mRequest.getFrom());
            setSender(mRequest.getTo());
        }

    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public UserEntity getSender() {
        return mSender;
    }

    public void setAsResent() {
        if (mReceiver == null) return;
        mSetAsRecentUseCase.setData(mReceiver);
        mSetAsRecentUseCase.execute(new DefaultDisposableObserver());
    }

    public RequestEntity getRequest() {
        return mRequest;
    }

    public void onConfirmClicked() {
        if (mRequest == null) return;
        if (!isViewAttached()) return;
        if (mRequest.getDirection().equals(Direction.TO))
            getView().openPinScreen(mRequest.getFrom(), mRequest.getToAmount(), mRequest.getFromAmount(), mRequest.getId());
        else if (mRequest.getDirection().equals(Direction.FROM))
            getView().openPinScreen(mRequest.getFrom(), mRequest.getFromAmount(), mRequest.getToAmount(), mRequest.getId());
    }

    public void onRejectClicked() {
        if (mRequest == null) return;
        mUpdateRequestUseCase.setData(mRequest.getId());
        mUpdateRequestUseCase.execute(new UpdateRequestSubscriber());
    }

    private class AddToRequestsSubscriber extends DefaultInternetSubscriber<RequestEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final RequestEntity requestEntity) {
            if (!isViewAttached()) return;
            getView().onRequestSend(requestEntity);
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
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mAddToRequestsAction));
        }
    }

    private class GetUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(UserEntity userEntity) {
            mSender = userEntity;
            if (!isViewAttached()) return;
            getView().retrieveSender(userEntity);
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
            getView().showErrorWithRetry(new RetryAction(mGetCurrentUserAction));
        }
    }

    private class ConvertSubscriber extends DefaultInternetSubscriber<Double> {

        @Override
        public void onNext(Double amount) {
            if (!isViewAttached()) return;
            getView().showConvertedCurrency(amount, mReceiver.getCurrency());

        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
        }
    }

    private Action mUpdateRequestAction = new Action() {
        @Override
        public void run() throws Exception {
            mUpdateRequestUseCase.execute(new UpdateRequestSubscriber());
        }
    };

    private class UpdateRequestSubscriber extends DefaultInternetSubscriber<RequestEntity> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final RequestEntity requestEntity) {
            if (!isViewAttached()) return;
            getView().onUserRejected(requestEntity);
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
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mUpdateRequestAction));
        }
    }

    @Override
    public void onDetachView() {
        mGetUserDataUseCase.dispose();
        mConvertAmountUseCase.dispose();
        mSetAsRecentUseCase.dispose();
        mAddToRequestsUseCase.dispose();
        mUpdateRequestUseCase.dispose();
    }
}
