package com.kora.android.domain.usecase.identity;

import android.util.Log;

import com.kora.android.data.network.enumclass.Kind;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class CreateIdentityUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;

    private Response.Error mError;

    @Inject
    public CreateIdentityUseCase(final Web3jConnection web3jConnection,
                                 final EtherWalletStorage etherWalletStorage) {
        this.mWeb3jConnection = web3jConnection;
        this.mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Single buildTask() {
        return Single.just(true).map(a -> {

            final String fromAddress = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
            final BigInteger amount = BigInteger.ONE;
            final String walletFileName = "5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
            final String password = "123456789";

            ////////////////////////////////////////////////////////////////////////////////////////

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            mError = ethGetTransactionCount.getError();
            if (mError != null)
                throw new RetrofitException(mError.getMessage(), null, null, Kind.UNEXPECTED, null, null);
            final BigInteger nonce = ethGetTransactionCount.getTransactionCount();



//            final Function function = new Function(
//                    "createIdentity",
//                    Arrays.asList(new Address("123"), new Address("123")),
//                    Collections.<TypeReference<?>>emptyList());
//
//            final String encodedFunction = FunctionEncoder.encode(function);
//
//            final Transaction transaction = Transaction.createFunctionCallTransaction(
//                    fromAddress,
//                    nonce,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit(),
//                    mWeb3jConnection.getIdentityManagerRinkeby(),
//                    amount,
//                    encodedFunction);
//
//            final EthSendTransaction ethSendTransaction = web3j
//                    .ethSendTransaction(transaction)
//                    .sendAsync()
//                    .get();
//            mError = ethSendTransaction.getError();
//            if (mError != null)
//                throw new RetrofitException(mError.getMessage(), null, null, Kind.UNEXPECTED, null, null);
//            final String transactionHash = ethSendTransaction.getTransactionHash();

            return nonce;
        });
    }
}