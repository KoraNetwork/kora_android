package com.kora.android.domain.usecase.transaction;

import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class CreateRawTransactionUseCase extends AsyncUseCase {

    private final TransactionRepository mTransactionRepository;

    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;
    private String mPinCode;

    @Inject
    public CreateRawTransactionUseCase(final TransactionRepository transactionRepository) {
        mTransactionRepository = transactionRepository;
    }

    public void setData(final UserEntity receiver,
                        final double senderAmount,
                        final double receiverAmount,
                        final String pinCode) {
        mReceiver = receiver;
        mSenderAmount = senderAmount;
        mReceiverAmount = receiverAmount;
        mPinCode = pinCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return mTransactionRepository.createRawTransaction(
                mReceiver,
                mSenderAmount,
                mReceiverAmount,
                mPinCode
        );
    }
}
