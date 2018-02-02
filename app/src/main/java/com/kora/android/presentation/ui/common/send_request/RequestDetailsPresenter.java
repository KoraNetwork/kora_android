package com.kora.android.presentation.ui.common.send_request;

import android.support.annotation.Nullable;

import com.kora.android.common.utils.Validator;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.request.AddToRequestsUseCase;
import com.kora.android.domain.usecase.request.UpdateRequestUseCase;
import com.kora.android.domain.usecase.convert.GetConvertedAmountUseCase;
import com.kora.android.domain.usecase.user.GetUserDataUseCase;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionType;
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
    private final GetConvertedAmountUseCase mGetConvertedAmountUseCase;
    private final AddToRequestsUseCase mAddToRequestsUseCase;
    private final UpdateRequestUseCase mUpdateRequestUseCase;

    private UserEntity mSender;
    private UserEntity mReceiver;

    private ActionType mActionType;

    @Nullable
    private RequestEntity mRequest;

    @Inject
    public RequestDetailsPresenter(final GetUserDataUseCase getUserDataUseCase,
                                   final GetConvertedAmountUseCase getConvertedAmountUseCase,
                                   final AddToRequestsUseCase addToRequestsUseCase,
                                   final UpdateRequestUseCase updateRequestUseCase) {
        mGetUserDataUseCase = getUserDataUseCase;
        mGetConvertedAmountUseCase = getConvertedAmountUseCase;
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

    public void convertIfNeed(final Double value) {
        if (mSender == null || mReceiver == null) return;
        if (mSender.getCurrency().equals(mReceiver.getCurrency())) {
            if (getView() == null) return;
            getView().showConvertedCurrency(value, mReceiver.getCurrency());
            return;
        }

        if (mActionType == ActionType.SEND_MONEY) {
            mGetConvertedAmountUseCase.setData(mReceiver.getId(), value, TransactionType.SEND);
        } else if (mActionType == ActionType.CREATE_REQUEST){
            mGetConvertedAmountUseCase.setData(mReceiver.getId(), value, TransactionType.REQUEST);
        }
        mGetConvertedAmountUseCase.execute(new ConvertSubscriber());
    }

    public void sendMoney(double senderAmount, double receiverAmount, String additionalNote) {
        if (!validateForm(senderAmount, receiverAmount)) return;

        getView().openPinScreen(mReceiver, senderAmount, receiverAmount, null);
    }

    private boolean validateForm(double senderAmount, double receiverAmount) {
        if (!Validator.isValidPrice(senderAmount)) {
            if (!isViewAttached()) return false;
            getView().emptySenderAmountError();
            return false;
        }
        if (!Validator.isValidPrice(receiverAmount)) {
            if (!isViewAttached()) return false;
            getView().emptyReceiverAmountError();
            return false;
        }
        return true;
    }

    public void sendRequest(double senderAmount, double receiverAmount, String additionalNote) {
        if (!validateForm(senderAmount, receiverAmount)) return;

        mAddToRequestsUseCase.setData(
                mReceiver.getId(),
                senderAmount,
                receiverAmount,
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

    public RequestEntity getRequest() {
        return mRequest;
    }

    public void onConfirmClicked() {
        if (mRequest == null) return;
        if (!isViewAttached()) return;
        if (mRequest.getDirection().equals(Direction.TO))
            getView().openPinScreen(mRequest.getFrom(), mRequest.getToAmount(), mRequest.getFromAmount(), mRequest);
        else if (mRequest.getDirection().equals(Direction.FROM))
            getView().openPinScreen(mRequest.getFrom(), mRequest.getFromAmount(), mRequest.getToAmount(), mRequest);
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

    public ActionType getActionType() {
        return mActionType;
    }

    public void setActionType(ActionType actionType) {
        this.mActionType = actionType;
    }

    @Override
    public void onDetachView() {
        mGetUserDataUseCase.dispose();
        mGetConvertedAmountUseCase.dispose();
        mAddToRequestsUseCase.dispose();
        mUpdateRequestUseCase.dispose();
    }
}
