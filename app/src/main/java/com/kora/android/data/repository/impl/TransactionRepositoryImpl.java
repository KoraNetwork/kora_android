package com.kora.android.data.repository.impl;

import android.content.Context;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.Web3jUtils;
import com.kora.android.data.network.model.request.RawTransactionRequest;
import com.kora.android.data.network.model.request.TransactionRequest;
import com.kora.android.data.network.service.TransactionService;
import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.data.repository.mapper.TransactionMapper;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.main.fragments.transactions.filter.TransactionFilterModel;
import com.kora.android.presentation.model.TransactionEntity;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

import static com.kora.android.common.Keys.Shared.USER;

@Singleton
public class TransactionRepositoryImpl implements TransactionRepository {

    private final Context mContext;
    private final PreferenceHandler mPreferenceHandler;
    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;
    private final TransactionService mTransactionService;
    private final TransactionMapper mTransactionMapper;

    @Inject
    public TransactionRepositoryImpl(final Context context,
                                     final PreferenceHandler preferenceHandler,
                                     final Web3jConnection web3jConnection,
                                     final EtherWalletStorage etherWalletStorage,
                                     final TransactionService transactionService,
                                     final TransactionMapper transactionMapper) {
        mContext = context;
        mPreferenceHandler = preferenceHandler;
        mWeb3jConnection = web3jConnection;
        mEtherWalletStorage = etherWalletStorage;
        mTransactionService = transactionService;
        mTransactionMapper = transactionMapper;
    }

    @Override
    public Observable<List<TransactionEntity>> retrieveTransactions(final TransactionFilterModel transactionFilterModel,
                                                                    final int limit,
                                                                    final int skip) {
        return mTransactionService.retrieveTransactionHistory(
                transactionFilterModel.getTransactionDirectionsAsStrings(),
                transactionFilterModel.getTransactionTypesAsStrings(),
                limit,
                skip)
                .compose(mTransactionMapper.transformTransactionListResponseToEntityList());
    }

    @Override
    public Observable<TransactionEntity> addToTransactions(final String type,
                                                           final String to,
                                                           final double fromAmount,
                                                           final double toAmount,
                                                           final List<String> transactionHash) {
        final TransactionRequest transactionRequest = new TransactionRequest()
                .addType(type)
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addTransactionHash(transactionHash);
        return mTransactionService.addToTransactions(transactionRequest)
                .compose(mTransactionMapper.transformResponseToTransactionEntity());
    }

    @Override
    public Observable<List<String>> createRawTransaction(final UserEntity receiver,
                                                         final double senderAmount,
                                                         final double receiverAmount,
                                                         final String pinCode) {
        return Observable.just(true).map(a -> {

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final UserEntity sender = mPreferenceHandler.remindObject(USER, UserEntity.class);
            if (sender == null)
                throw new Exception(mContext.getString(R.string.web3j_error_message_preferences));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Credentials senderCredentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            ////////////////////////////////////////////////////////////////////////////////////////

            final EthGetBalance ethGetBalance = web3j
                    .ethGetBalance(sender.getOwner(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            final BigInteger balanceOwnerWei = ethGetBalance.getBalance();
            final double balanceOwnerEth = Web3jUtils.convertWeiToEth(balanceOwnerWei);

            if (balanceOwnerEth < mWeb3jConnection.getMinOwnerBalance())
                throw new Exception(mContext.getString(R.string.web3j_error_message_owner_balance,
                        String.valueOf(mWeb3jConnection.getMinOwnerBalance()),
                        String.valueOf(balanceOwnerEth)));

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function getBalanceFunction = new Function(
                    mWeb3jConnection.getGetBalabceFunction(),
                    Collections.singletonList(new Address(sender.getIdentity())),
                    Collections.singletonList(new TypeReference<Uint256>() {
                    })
            );
            final String encodedFunction = FunctionEncoder.encode(getBalanceFunction);

            final EthCall response = web3j
                    .ethCall(
                            Transaction.createEthCallTransaction(sender.getIdentity(), sender.getERC20Token(), encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            final List<Type> someTypes = FunctionReturnDecoder.decode(response.getValue(), getBalanceFunction.getOutputParameters());
            final BigInteger balanceIdentityBigInteger = (BigInteger) someTypes.get(0).getValue();
            final double balanceIdentityToken = Web3jUtils.convertBigIntegerToToken(balanceIdentityBigInteger);

            if (senderAmount > balanceIdentityToken)
                throw new Exception(mContext.getString(R.string.web3j_error_message_identity_balance,
                        String.valueOf(senderAmount),
                        String.valueOf(balanceIdentityToken)));

            ////////////////////////////////////////////////////////////////////////////////////////

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final EthGetTransactionCount senderEthGetTransactionCount = web3j
                    .ethGetTransactionCount(sender.getOwner(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            final BigInteger senderTransactionCount = senderEthGetTransactionCount.getTransactionCount();

            ////////////////////////////////////////////////////////////////////////////////////////

            if (sender.getCurrency().equals(receiver.getCurrency())) {

                final Function transferFunction = new Function(
                        mWeb3jConnection.getGetTransferFunction(),
                        Arrays.asList(
                                new Address(receiver.getIdentity()),
                                new Uint256(Web3jUtils.convertTokenToBigInteger(senderAmount))
                        ),
                        Collections.emptyList()
                );
                final String senderTransferFunctionString = FunctionEncoder.encode(transferFunction);
                final byte[] senderTransferFunctionByteArray = Numeric.hexStringToByteArray(senderTransferFunctionString);

                final Function forwardToFunction = new Function(
                        mWeb3jConnection.getForwardToFunction(),
                        Arrays.asList(
                                new Address(sender.getOwner()),
                                new Address(sender.getIdentity()),
                                new Address(sender.getERC20Token()),
                                Uint256.DEFAULT,
                                new DynamicBytes(senderTransferFunctionByteArray)
                        ),
                        Collections.emptyList()
                );
                final String senderForwardToFunctionString = FunctionEncoder.encode(forwardToFunction);

                final RawTransaction senderRawTransaction = RawTransaction.createTransaction(
                        senderTransactionCount,
                        mWeb3jConnection.getGasPrice(),
                        mWeb3jConnection.getGasLimit(),
                        mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                        senderForwardToFunctionString);

                final byte[] senderSignedMessage = TransactionEncoder.signMessage(senderRawTransaction, senderCredentials);
                final String senderHexValue = Numeric.toHexString(senderSignedMessage);

                return Collections.singletonList(senderHexValue);

            } else {

                final Function senderTransferFunction = new Function(
                        mWeb3jConnection.getGetTransferFunction(),
                        Arrays.asList(
                                new Address(mWeb3jConnection.getKoraWalletAddress()),
                                new Uint256(Web3jUtils.convertTokenToBigInteger(senderAmount))
                        ),
                        Collections.emptyList()
                );
                final String senderTransferFunctionString = FunctionEncoder.encode(senderTransferFunction);
                final byte[] senderTransferFunctionByteArray = Numeric.hexStringToByteArray(senderTransferFunctionString);

                final Function forwardToFunction = new Function(
                        mWeb3jConnection.getForwardToFunction(),
                        Arrays.asList(
                                new Address(sender.getOwner()),
                                new Address(sender.getIdentity()),
                                new Address(sender.getERC20Token()),
                                Uint256.DEFAULT,
                                new DynamicBytes(senderTransferFunctionByteArray)
                        ),
                        Collections.emptyList()
                );
                final String senderForwardToFunctionString = FunctionEncoder.encode(forwardToFunction);

                final RawTransaction senderRawTransaction = RawTransaction.createTransaction(
                        senderTransactionCount,
                        mWeb3jConnection.getGasPrice(),
                        mWeb3jConnection.getGasLimit(),
                        mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                        senderForwardToFunctionString);

                final byte[] senderSignedMessage = TransactionEncoder.signMessage(senderRawTransaction, senderCredentials);
                final String senderHexValue = Numeric.toHexString(senderSignedMessage);



                final Credentials koraCredentials = mEtherWalletStorage.getCredentials(
                        mWeb3jConnection.getKoraWalletFileName(),
                        mWeb3jConnection.getKoraWalletPassword());

                final EthGetTransactionCount koraEthGetTransactionCount = web3j
                        .ethGetTransactionCount(mWeb3jConnection.getKoraWalletAddress(), DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
                final BigInteger koraTransactionCount = koraEthGetTransactionCount.getTransactionCount();

                final Function koraTransferFunction = new Function(
                        mWeb3jConnection.getGetTransferFunction(),
                        Arrays.asList(
                                new Address(sender.getIdentity()),
                                new Uint256(Web3jUtils.convertTokenToBigInteger(receiverAmount))
                        ),
                        Collections.emptyList()
                );
                final String koraTransferFunctionString = FunctionEncoder.encode(koraTransferFunction);

                final RawTransaction koraRawTransaction = RawTransaction.createTransaction(
                        koraTransactionCount,
                        mWeb3jConnection.getGasPrice(),
                        mWeb3jConnection.getGasLimit(),
                        receiver.getERC20Token(),
                        koraTransferFunctionString);

                final byte[] koraSignedMessage = TransactionEncoder.signMessage(koraRawTransaction, koraCredentials);
                final String koraHexValue = Numeric.toHexString(koraSignedMessage);

                return Arrays.asList(senderHexValue, koraHexValue);
            }
        });
    }

    @Override
    public Observable<TransactionEntity> sendRawTransaction(final String type,
                                                            final String to,
                                                            final double fromAmount,
                                                            final double toAmount,
                                                            final List<String> rawTransactions) {
        final RawTransactionRequest rawTransactionRequest = new RawTransactionRequest()
                .addType(type)
                .addTo(to)
                .addFromAmount(fromAmount)
                .addToAmount(toAmount)
                .addRawTransactions(rawTransactions);
        return mTransactionService.sendRawTransaction(rawTransactionRequest)
                .compose(mTransactionMapper.transformResponseToTransactionEntity());
    }
}