package com.kora.android.data.repository.impl;

import android.content.Context;

import com.kora.android.R;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.Web3jUtils;
import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.data.web3j.smart_contracts.HumanStandardToken;
import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.utils.EtherWalletUtils;
import com.kora.android.presentation.model.UserEntity;

import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class Web3jRepositoryImpl implements Web3jRepository {

    private final Context mContext;
    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletUtils mEtherWalletUtils;
    private final EtherWalletStorage mEtherWalletStorage;

    @Inject
    public Web3jRepositoryImpl(final Context context,
                               final Web3jConnection web3jConnection,
                               final EtherWalletUtils etherWalletUtils,
                               final EtherWalletStorage etherWalletStorage) {
        mContext = context;
        mWeb3jConnection = web3jConnection;
        mEtherWalletUtils = etherWalletUtils;
        mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    public Observable<IdentityCreatedResponse> createWallets(final String pinCode) {
        return Observable.just(true).map(a -> {
            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final String ownerWalletFileName = mEtherWalletUtils.generateNewWalletFile(
                    pinCode,
                    new File(mContext.getFilesDir(), ""));
            final EtherWallet ownerEtherWallet = EtherWallet.creteOwnerEtherWallet(ownerWalletFileName);
            mEtherWalletStorage.addWallet(ownerEtherWallet);

            final String recoveryWalletFileName = mEtherWalletUtils.generateNewWalletFile(
                    pinCode,
                    new File(mContext.getFilesDir(), ""));
            final EtherWallet recoveryEtherWallet = EtherWallet.creteRecoveryEtherWallet(recoveryWalletFileName);
            mEtherWalletStorage.addWallet(recoveryEtherWallet);

            final ECKeyPair keys = ECKeyPair.create(Hex.decode(mWeb3jConnection.getKoraWalletPrivateKey()));
            final String koraWalletFileName = mEtherWalletUtils.generateWalletFile(
                    mWeb3jConnection.getKoraWalletPassword(),
                    keys,
                    new File(mContext.getFilesDir(), ""));
            final EtherWallet koraEtherWallet = EtherWallet.creteKoraEtherWallet(koraWalletFileName);
            mEtherWalletStorage.addWallet(koraEtherWallet);

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Credentials credentials = mEtherWalletStorage.getCredentials(
                    mWeb3jConnection.getKoraWalletFileName(),
                    mWeb3jConnection.getKoraWalletPassword());

            final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    web3j,
                    credentials,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit()
            );

            final TransactionReceipt createIdentityTransactionReceipt =
                    metaIdentityManager.createIdentity(
                            new Address(ownerEtherWallet.getAddress()),
                            new Address(recoveryEtherWallet.getAddress())
                    ).get();

            final MetaIdentityManager.IdentityCreatedEventResponse identityCreatedEventResponse =
                    metaIdentityManager.getIdentityCreatedEvents(createIdentityTransactionReceipt).get(0);
            return new IdentityCreatedResponse(identityCreatedEventResponse);
        });
    }

    @Override
    public Observable<String> getBalance(final String proxyAddress,
                                         final String smartContractAddress) {
        return Observable.just(true).map(a -> {
            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Function function = new Function(
                    mWeb3jConnection.getGetBalabceFunction(),
                    Collections.singletonList(new Address(proxyAddress)),
                    Collections.singletonList(new TypeReference<Uint256>() {
                    })
            );
            final String encodedFunction = FunctionEncoder.encode(function);

            final EthCall response = web3j
                    .ethCall(
                            Transaction.createEthCallTransaction(proxyAddress, smartContractAddress, encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            final List<Type> someTypes = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            final BigInteger balanceIdentityBigInteger = (BigInteger) someTypes.get(0).getValue();
            final double balanceIdentityToken = Web3jUtils.convertBigIntegerToToken(balanceIdentityBigInteger);

            return Web3jUtils.convertDoubleToString(balanceIdentityToken);
        });
    }

    @Override
    public Observable<List<String>> increaseBalance(final UserEntity userEntity,
                                                    final double amount) {
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
                    userEntity.getOwner(),
                    Web3jUtils.convertEthToWei(mWeb3jConnection.getDefaultOwnerBalance()));

            final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            final String hexValue = Numeric.toHexString(signedMessage);

            final EthSendTransaction ethSendTransaction = web3j
                    .ethSendRawTransaction(hexValue)
                    .sendAsync()
                    .get();

            final HumanStandardToken humanStandardToken = HumanStandardToken.load(
                    userEntity.getERC20Token(),
                    web3j,
                    credentials,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit()
            );

            final TransactionReceipt transactionReceipt = humanStandardToken.transfer(
                    new Address(userEntity.getIdentity()),
                    new Uint256(Web3jUtils.convertTokenToBigInteger(amount))
            ).get();

            return Arrays.asList(
                    ethSendTransaction.getTransactionHash(),
                    transactionReceipt.getTransactionHash());
        });
    }
}
