package com.kora.android.domain.usecase.transaction;

import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.enums.TransactionType;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class SendRawTransactionUseCase extends AsyncUseCase {

    private final TransactionRepository mTransactionRepository;

    private TransactionType mType;
    private String mTo;
    private double mSenderAmount;
    private double mReceiverAmount;
    private List<String> mRawTransactions;

    @Inject
    public SendRawTransactionUseCase(final TransactionRepository transactionRepository) {
        mTransactionRepository = transactionRepository;
    }

    public void setData(final TransactionType type,
                        final String to,
                        final double senderAmount,
                        final double receiverAmount,
                        final List<String> rawTransactions) {
        mType = type;
        mTo = to;
        mSenderAmount = senderAmount;
        mReceiverAmount = receiverAmount;
        mRawTransactions = rawTransactions;
    }

    @Override
    protected Observable buildObservableTask() {
            return mTransactionRepository.sendRawTransaction(
                    mType.name().toLowerCase(),
                    mTo,
                    mSenderAmount,
                    mReceiverAmount,
                    mRawTransactions);
    }
}
