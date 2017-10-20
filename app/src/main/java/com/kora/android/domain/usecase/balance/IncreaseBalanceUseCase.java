package com.kora.android.domain.usecase.balance;

import android.content.Context;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.Web3jUtils;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.smart_contracts.HumanStandardToken;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Collection;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;

import javax.inject.Inject;

import io.reactivex.Observable;

import static com.kora.android.data.web3j.Constants.DEFAULT_USD_BALANCE;
import static com.kora.android.data.web3j.Constants.DEFAULT_EUR_BALANCE;
import static com.kora.android.data.web3j.Constants.DEFAULT_UAH_BALANCE;
import static com.kora.android.data.web3j.Constants.DEFAULT_NGN_BALANCE;
import static com.kora.android.data.web3j.Constants.DEFAULT_GBP_BALANCE;
import static com.kora.android.data.web3j.Constants.EUR;
import static com.kora.android.data.web3j.Constants.GBP;
import static com.kora.android.data.web3j.Constants.NGN;
import static com.kora.android.data.web3j.Constants.UAH;
import static com.kora.android.data.web3j.Constants.USD;

@ConfigPersistent
public class IncreaseBalanceUseCase extends AsyncUseCase {

    private final Context mContext;
    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;

    private UserEntity mUserEntity;

    @Inject
    public IncreaseBalanceUseCase(final Context context,
                                  final Web3jConnection web3jConnection,
                                  final EtherWalletStorage etherWalletStorage) {
        mContext = context;
        mEtherWalletStorage = etherWalletStorage;
        mWeb3jConnection = web3jConnection;
    }

    public void setData(UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {
            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Credentials credentials = mEtherWalletStorage.getCredentials(
                    mWeb3jConnection.getKoraWalletFileName(),
                    mWeb3jConnection.getKoraWalletPassword());



            final EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(mWeb3jConnection.getKoraWalletAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            final BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            final RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit(),
                    mUserEntity.getOwner(),
                    Web3jUtils.convertEthToWei(mWeb3jConnection.getDefaultOwnerBalance()));

            final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            final String hexValue = Numeric.toHexString(signedMessage);
//
//            final Future<EthSendTransaction> ethSendTransactionFuture = web3j
//                    .ethSendRawTransaction(hexValue)
//                    .sendAsync();

            final EthSendTransaction ethSendTransaction = web3j
                    .ethSendRawTransaction(hexValue)
                    .sendAsync()
                    .get();



            final HumanStandardToken humanStandardToken = HumanStandardToken.load(
                    mUserEntity.getERC20Token(),
                    web3j,
                    credentials,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit()
            );

            final String currency = mUserEntity.getCurrency();
            double amount = 0.00;
            if (currency.equals(USD))
                amount = DEFAULT_USD_BALANCE;
            if (currency.equals(EUR))
                amount = DEFAULT_EUR_BALANCE;
            if (currency.equals(GBP))
                amount = DEFAULT_GBP_BALANCE;
            if (currency.equals(UAH))
                amount = DEFAULT_UAH_BALANCE;
            if (currency.equals(NGN))
                amount = DEFAULT_NGN_BALANCE;

//            final Future<TransactionReceipt> transactionReceiptFuture = humanStandardToken.transfer(
//                    new Address(mUserEntity.getIdentity()),
//                    new Uint256(Web3jUtils.convertTokenToBigInteger(amount))
//            );

            final TransactionReceipt transactionReceipt = humanStandardToken.transfer(
                    new Address(mUserEntity.getIdentity()),
                    new Uint256(Web3jUtils.convertTokenToBigInteger(amount))
            ).get();


//            final EthSendTransaction ethSendTransaction = ethSendTransactionFuture.get();
//            final TransactionReceipt transactionReceipt = transactionReceiptFuture.get();


//            return Arrays.asList(
//                    ethSendTransaction.getTransactionHash(),
//                    transactionReceipt.getTransactionHash());

            return Collections.emptyList();
        });
    }
}
