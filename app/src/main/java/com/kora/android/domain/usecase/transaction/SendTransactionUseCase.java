package com.kora.android.domain.usecase.transaction;

import android.util.Log;

import com.kora.android.data.network.enumclass.Kind;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class SendTransactionUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;

    private String mWalletFileName;
    private String mPassword;
    private String mAddressFrom;
    private String mAddressTo;
    private BigInteger mAmount;

    private Response.Error mError;

    public void setData(final String walletFileName,
                        final String password,
                        final String addressFrom,
                        final String addressTo,
                        final BigInteger amount) {
        mWalletFileName = walletFileName;
        mPassword = password;
        mAddressFrom = addressFrom;
        mAddressTo = addressTo;
        mAmount = amount;
    }

    @Inject
    public SendTransactionUseCase(final Web3jConnection web3jConnection,
                                  final EtherWalletStorage etherWalletStorage) {
        this.mWeb3jConnection = web3jConnection;
        this.mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Single buildTask() {
        return Single.just(true).map(a -> {

            final Web3j web3j = mWeb3jConnection.getWeb3j();

            final EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(mAddressFrom, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            mError = ethGetTransactionCount.getError();
            if (mError != null)
                throw new RetrofitException(mError.getMessage(), null, null, Kind.UNEXPECTED, null, null);
            final BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            final RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit(),
                    mAddressTo,
                    mAmount);
            final Credentials credentials = mEtherWalletStorage.getCredentials(mWalletFileName, mPassword);
            final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            final String hexValue = Numeric.toHexString(signedMessage);

            final EthSendTransaction ethSendTransaction = web3j
                    .ethSendRawTransaction(hexValue)
                    .sendAsync()
                    .get();
            mError = ethSendTransaction.getError();
            if (mError != null)
                throw new RetrofitException(mError.getMessage(), null, null, Kind.UNEXPECTED, null, null);
            final String transactionHash = ethSendTransaction.getTransactionHash();

            return transactionHash;
        });
    }
}