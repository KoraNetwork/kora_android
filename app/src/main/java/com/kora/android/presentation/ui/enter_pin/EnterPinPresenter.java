package com.kora.android.presentation.ui.enter_pin;

import android.support.v4.util.Pair;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.borrow.SendAgreeLoanUseCase;
import com.kora.android.domain.usecase.borrow.SendCreateLoanUseCase;
import com.kora.android.domain.usecase.borrow.SendFundLoanUseCase;
import com.kora.android.domain.usecase.borrow.SendPayBackLoanUseCase;
import com.kora.android.domain.usecase.deposit.DeleteDepositUseCase;
import com.kora.android.domain.usecase.request.DeleteRequestUseCase;
import com.kora.android.domain.usecase.web3j.CreateAgreeLoanUseCase;
import com.kora.android.domain.usecase.web3j.CreateCreateLoanUseCase;
import com.kora.android.domain.usecase.web3j.CreateFundLoanUseCase;
import com.kora.android.domain.usecase.web3j.CreatePayBackLoanUseCase;
import com.kora.android.domain.usecase.web3j.CreateRawTransactionUseCase;
import com.kora.android.domain.usecase.transaction.SendRawTransactionUseCase;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import org.web3j.crypto.CipherException;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class EnterPinPresenter extends BasePresenter<EnterPinView> {

    private final CreateRawTransactionUseCase mCreateRawTransactionUseCase;
    private final SendRawTransactionUseCase mSendRawTransactionUseCase;
    private final DeleteRequestUseCase mDeleteRequestUseCase;
    private final CreateCreateLoanUseCase mCreateCreateLoanUseCase;
    private final SendCreateLoanUseCase mSendCreateLoanUseCase;
    private final CreateAgreeLoanUseCase mCreateAgreeLoanUseCase;
    private final SendAgreeLoanUseCase mSendAgreeLoanUseCase;
    private final CreateFundLoanUseCase mCreateFundLoanUseCase;
    private final SendFundLoanUseCase mSendFundLoanUseCase;
    private final CreatePayBackLoanUseCase mCreatePayBackLoanUseCase;
    private final SendPayBackLoanUseCase mSendPayBackLoanUseCase;
    private final DeleteDepositUseCase mDeleteDepositWithdrawUseCase;

    private ActionType mActionType;
    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;
    private RequestEntity mRequestEntity;
    private BorrowEntity mBorrowEntity;
    private double mBorrowerValue;
    private DepositWithdrawEntity mDepositWithdrawEntity;

    @Inject
    public EnterPinPresenter(final CreateRawTransactionUseCase createRawTransactionUseCase,
                             final SendRawTransactionUseCase sendRawTransactionUseCase,
                             final DeleteRequestUseCase deleteRequestUseCase,
                             final CreateCreateLoanUseCase createCreateLoanUseCase,
                             final SendCreateLoanUseCase sendCreateLoanUseCase,
                             final CreateAgreeLoanUseCase createAgreeLoanUseCase,
                             final SendAgreeLoanUseCase sendAgreeLoanUseCase,
                             final CreateFundLoanUseCase createFundLoanUseCase,
                             final SendFundLoanUseCase sendFundLoanUseCase,
                             final CreatePayBackLoanUseCase createPayBackLoanUseCase,
                             final SendPayBackLoanUseCase sendPayBackLoanUseCase,
                             final DeleteDepositUseCase deleteDepositUseCase) {
        mCreateRawTransactionUseCase = createRawTransactionUseCase;
        mSendRawTransactionUseCase = sendRawTransactionUseCase;
        mDeleteRequestUseCase = deleteRequestUseCase;
        mCreateCreateLoanUseCase = createCreateLoanUseCase;
        mSendCreateLoanUseCase = sendCreateLoanUseCase;
        mCreateAgreeLoanUseCase = createAgreeLoanUseCase;
        mSendAgreeLoanUseCase = sendAgreeLoanUseCase;
        mCreateFundLoanUseCase = createFundLoanUseCase;
        mSendFundLoanUseCase = sendFundLoanUseCase;
        mCreatePayBackLoanUseCase = createPayBackLoanUseCase;
        mSendPayBackLoanUseCase = sendPayBackLoanUseCase;
        mDeleteDepositWithdrawUseCase = deleteDepositUseCase;
    }

    public ActionType getActionType() {
        return mActionType;
    }

    public void setActionType(final ActionType actionType) {
        mActionType = actionType;
    }

    public void setReceiver(final UserEntity receiver) {
        mReceiver = receiver;
    }

    public void setSenderAmount(final double senderAmount) {
        mSenderAmount = senderAmount;
    }

    public void setReceiverAmount(final double receiverAmount) {
        mReceiverAmount = receiverAmount;
    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public double getSenderAmount() {
        return mSenderAmount;
    }

    public double getReceiverAmount() {
        return mReceiverAmount;
    }

    public RequestEntity getRequestEntity() {
        return mRequestEntity;
    }

    public void setRequestEntity(final RequestEntity requestId) {
        mRequestEntity = requestId;
    }

    public BorrowEntity getBorrowEntity() {
        return mBorrowEntity;
    }

    public void setBorrowEntity(final BorrowEntity borrowEntity) {
        mBorrowEntity = borrowEntity;
    }

    public double getBorrowerValue() {
        return mBorrowerValue;
    }

    public void setBorrowerValue(final double borrowerValue) {
        mBorrowerValue = borrowerValue;
    }

    public DepositWithdrawEntity getDepositEntity() {
        return mDepositWithdrawEntity;
    }

    public void setDepositEntity(final DepositWithdrawEntity depositWithdrawEntity) {
        mDepositWithdrawEntity = depositWithdrawEntity;
    }

    private TransactionType getTransactionTypeByAction(ActionType actionType) {
        switch (actionType) {
            case SHOW_REQUEST:
                return TransactionType.REQUEST;
            case SEND_MONEY:
                return TransactionType.SEND;
        }
        return null;
    }

    public void startCreateRawTransactionTask(final String pinCode) {
        if (pinCode == null || pinCode.isEmpty()) {
            getView().showEmptyPinCode();
            return;
        }
        if (!StringUtils.isPinCodeLongEnough(pinCode)) {
            getView().showTooShortPinCode();
            return;
        }

        switch (mActionType) {
            case SEND_MONEY:
            case SHOW_REQUEST:
            case SHOW_DEPOSIT:
                mCreateRawTransactionUseCase.setData(
                        mReceiver,
                        mSenderAmount,
                        mReceiverAmount,
                        pinCode);
                mCreateRawTransactionUseCase.execute(new CreateRawTransactionSubscriber());
                break;
            case SHOW_WITHDRAW:
                mCreateRawTransactionUseCase.setData(
                        mReceiver,
                        mSenderAmount,
                        mReceiverAmount,
                        mDepositWithdrawEntity.getInterestRate(),
                        pinCode);
                mCreateRawTransactionUseCase.execute(new CreateRawTransactionSubscriber());
                break;
            case CREATE_LOAN:
                mCreateCreateLoanUseCase.setData(
                        mBorrowEntity.getReceiver(),
                        mBorrowEntity.getGuarantors(),
                        mBorrowEntity.getFromAmount(),
                        mBorrowEntity.getToAmount(),
                        mBorrowEntity.getRate(),
                        mBorrowEntity.getStartDate(),
                        mBorrowEntity.getMaturityDate(),
                        pinCode);
                mCreateCreateLoanUseCase.execute(new CreateCreateLoanSubscriber());
                break;
            case AGREE_LOAN:
                mCreateAgreeLoanUseCase.setData(mBorrowEntity.getLoanId(), pinCode);
                mCreateAgreeLoanUseCase.execute(new CreateAgreeLoanSubscriber());
                break;
            case FUND_LOAN:
                mCreateFundLoanUseCase.setData(
                        mBorrowEntity.getSender().getERC20Token(),
                        mBorrowEntity.getReceiver().getERC20Token(),
                        mBorrowEntity.getFromAmount(),
                        mBorrowEntity.getToAmount(),
                        mBorrowEntity.getLoanId(),
                        pinCode);
                mCreateFundLoanUseCase.execute(new CreateFundLoanSubscriber());
                break;
            case PAY_BACK_LOAN:
                mCreatePayBackLoanUseCase.setData(
                        mBorrowEntity.getLoanId(),
                        mBorrowEntity.getSender().getERC20Token(),
                        mBorrowEntity.getReceiver().getERC20Token(),
                        mBorrowerValue,
                        mBorrowEntity.getFromBalance(),
                        mBorrowEntity.getToBalance(),
                        pinCode);
                mCreatePayBackLoanUseCase.execute(new CreatePayBackLoanSubscriber());
                break;
        }
    }

    private class CreateRawTransactionSubscriber extends DefaultWeb3jSubscriber<String> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final String rawTransaction) {
            if (!isViewAttached()) return;
            switch (mActionType) {
                case SEND_MONEY:
                    startSendRawTransactionTask(rawTransaction);
                    break;
                case SHOW_REQUEST:
                    startDeleteRequestTask(rawTransaction);
                    break;
                case SHOW_DEPOSIT:
                case SHOW_WITHDRAW:
                    startDeleteDepositWithdrawTask(rawTransaction);
                    break;
            }
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();

            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWalletError(final FileNotFoundException fileNotFoundException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_no_wallet);
        }

        @Override
        public void handlePinError(final CipherException cipherException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_wrong_pin_code);
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_network_problems);
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isViewAttached()) return;
            getView().showError(message);
        }
    }

    public void startSendRawTransactionTask(final String rawTransaction) {
        mSendRawTransactionUseCase.setData(
                getTransactionTypeByAction(mActionType),
                mReceiver.getId(),
                mSenderAmount,
                mReceiverAmount,
                rawTransaction);
        mSendRawTransactionUseCase.execute(new SendRawTransactionSubscriber());
    }

    private Action mSendRawTransactionAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendRawTransactionUseCase.execute(new SendRawTransactionSubscriber());
        }
    };

    private class SendRawTransactionSubscriber extends DefaultInternetSubscriber<TransactionEntity> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final TransactionEntity transactionEntity) {
            if (!isViewAttached()) return;
            getView().showTransactionScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mSendRawTransactionAction));
        }
    }

    public void startDeleteRequestTask(final String rawTransaction) {
        if (mRequestEntity.getDirection().equals(Direction.TO)) {
            mDeleteRequestUseCase.setData(
                    mRequestEntity.getId(),
                    mReceiverAmount,
                    mSenderAmount,
                    rawTransaction);
        } else if (mRequestEntity.getDirection().equals(Direction.FROM)) {
            mDeleteRequestUseCase.setData(
                    mRequestEntity.getId(),
                    mSenderAmount,
                    mReceiverAmount,
                    rawTransaction);
        }
        mDeleteRequestUseCase.execute(new DeleteRequestSubscriber());
    }

    private Action mDeleteRequestAction = new Action() {
        @Override
        public void run() throws Exception {
            mDeleteRequestUseCase.execute(new DeleteRequestSubscriber());
        }
    };

    private class DeleteRequestSubscriber extends DefaultInternetSubscriber<Object> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final Object object) {
            if (!isViewAttached()) return;
            getView().showTransactionScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mDeleteRequestAction));
        }
    }

    private class CreateCreateLoanSubscriber extends DefaultWeb3jSubscriber<String> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final String rawCreateLoan) {
            if (!isViewAttached()) return;
            startSendCreateLoanTask(rawCreateLoan);
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();

            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWalletError(final FileNotFoundException fileNotFoundException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_no_wallet);
        }

        @Override
        public void handlePinError(final CipherException cipherException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_wrong_pin_code);
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_network_problems);
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isViewAttached()) return;
            getView().showError(message);
        }
    }

    public void startSendCreateLoanTask(final String rawCreateLoan) {
        mSendCreateLoanUseCase.setData(mBorrowEntity.getId(), rawCreateLoan);
        mSendCreateLoanUseCase.execute(new SendCreateLoanSubscriber ());
    }

    private Action mSendCreateLoanAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendCreateLoanUseCase.execute(new SendCreateLoanSubscriber());
        }
    };

    private class SendCreateLoanSubscriber extends DefaultInternetSubscriber<BorrowEntity> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final BorrowEntity borrowEntity) {
            if (!isViewAttached()) return;
            getView().showBorrowScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mSendCreateLoanAction));
        }
    }

    private class CreateAgreeLoanSubscriber extends DefaultWeb3jSubscriber<String> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final String rawCreateLoan) {
            if (!isViewAttached()) return;
            startSendAgreeLoanTask(rawCreateLoan);
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();

            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWalletError(final FileNotFoundException fileNotFoundException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_no_wallet);
        }

        @Override
        public void handlePinError(final CipherException cipherException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_wrong_pin_code);
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_network_problems);
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isViewAttached()) return;
            getView().showError(message);
        }
    }

    public void startSendAgreeLoanTask(final String rawAgreeLoan) {
        mSendAgreeLoanUseCase.setData(mBorrowEntity.getId(), rawAgreeLoan);
        mSendAgreeLoanUseCase.execute(new SendAgreeLoanSubscriber());
    }

    private Action mSendAgreeLoanAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendAgreeLoanUseCase.execute(new SendAgreeLoanSubscriber());
        }
    };

    private class SendAgreeLoanSubscriber extends DefaultInternetSubscriber<BorrowEntity> {

        //        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final BorrowEntity borrowEntity) {
            if (!isViewAttached()) return;
            getView().showBorrowScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mSendAgreeLoanAction));
        }
    }

    private class CreateFundLoanSubscriber extends DefaultWeb3jSubscriber<Pair<String, String>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final Pair<String, String> rawTransactions) {
            if (!isViewAttached()) return;
            startSendFundLoanTask(rawTransactions.first, rawTransactions.second);
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();

            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWalletError(final FileNotFoundException fileNotFoundException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_no_wallet);
        }

        @Override
        public void handlePinError(final CipherException cipherException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_wrong_pin_code);
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_network_problems);
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isViewAttached()) return;
            getView().showError(message);
        }
    }

    private void startSendFundLoanTask(final String rawApprove, final String rawFundLoan) {
        mSendFundLoanUseCase.setData(mBorrowEntity.getId(), rawApprove, rawFundLoan);
        mSendFundLoanUseCase.execute(new SendFundLoanSubscriber());
    }

    private Action mSendFundLoanAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendFundLoanUseCase.execute(new SendFundLoanSubscriber());
        }
    };

    private class SendFundLoanSubscriber extends DefaultInternetSubscriber<BorrowEntity> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final BorrowEntity borrowEntity) {
            if (!isViewAttached()) return;
            getView().showBorrowScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mSendFundLoanAction));
        }
    }

    private class CreatePayBackLoanSubscriber extends DefaultWeb3jSubscriber<Pair<String, String>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(Pair<String, String> rawTransaction) {
            if (!isViewAttached()) return;
            startSendPayBackLoanTask(rawTransaction.first, rawTransaction.second);
        }

//        @Override
//        public void onComplete() {
//            if (!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void onError(final Throwable throwable) {
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();

            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleWalletError(final FileNotFoundException fileNotFoundException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_no_wallet);
        }

        @Override
        public void handlePinError(final CipherException cipherException) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_wrong_pin_code);
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isViewAttached()) return;
            getView().showError(R.string.web3j_error_message_network_problems);
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isViewAttached()) return;
            getView().showError(message);
        }
    }

    public void startSendPayBackLoanTask(final String rawApprove, final String rawPayBackLoan) {
        mSendPayBackLoanUseCase.setData(mBorrowEntity.getId(), rawApprove, rawPayBackLoan);
        mSendPayBackLoanUseCase.execute(new SendPayBackLoanSubscriber());
    }

    private Action mSendPayBackLoanAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendPayBackLoanUseCase.execute(new SendPayBackLoanSubscriber());
        }
    };

    private class SendPayBackLoanSubscriber extends DefaultInternetSubscriber<BorrowEntity> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final BorrowEntity borrowEntity) {
            if (!isViewAttached()) return;
            getView().showBorrowScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mSendPayBackLoanAction));
        }
    }

    private void startDeleteDepositWithdrawTask(final String rawTransaction) {
        boolean isDeposit = true;
        if (mActionType == ActionType.SHOW_DEPOSIT)
            isDeposit = true;
        else if (mActionType == ActionType.SHOW_WITHDRAW)
            isDeposit = false;

        mDeleteDepositWithdrawUseCase.setData(
                mDepositWithdrawEntity.getId(),
                mSenderAmount,
                mReceiverAmount,
                rawTransaction,
                isDeposit);
        mDeleteDepositWithdrawUseCase.execute(new DeleteDepositWithdrawSubscriber());
    }

    private Action mDeleteDepositAction = new Action() {
        @Override
        public void run() throws Exception {
            mDeleteDepositWithdrawUseCase.execute(new DeleteDepositWithdrawSubscriber());
        }
    };

    private class DeleteDepositWithdrawSubscriber extends DefaultInternetSubscriber<Object> {

//        @Override
//        protected void onStart() {
//            if (!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final Object object) {
            if (!isViewAttached()) return;
            getView().showTransactionScreen();
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
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mDeleteDepositAction));
        }
    }

    @Override
    public void onDetachView() {
        mDeleteRequestUseCase.dispose();
        mCreateRawTransactionUseCase.dispose();
        mSendRawTransactionUseCase.dispose();
        mCreateCreateLoanUseCase.dispose();
        mSendCreateLoanUseCase.dispose();
        mSendAgreeLoanUseCase.dispose();
        mCreateFundLoanUseCase.dispose();
        mSendFundLoanUseCase.dispose();
        mCreatePayBackLoanUseCase.dispose();
        mSendPayBackLoanUseCase.dispose();
        mDeleteDepositWithdrawUseCase.dispose();
    }
}
