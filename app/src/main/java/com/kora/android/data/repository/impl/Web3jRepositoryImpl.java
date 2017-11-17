package com.kora.android.data.repository.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.kora.android.R;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.Web3jUtils;
import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.data.web3j.smart_contracts.HumanStandardToken;
import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.storage.FileMetaData;
import com.kora.android.data.web3j.utils.EtherWalletUtils;
import com.kora.android.presentation.model.UserEntity;

import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
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
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

import static com.kora.android.common.Keys.ADDRESS_PREFIX;
import static com.kora.android.common.Keys.EXPORT_FOLDER_NAME;
import static com.kora.android.common.Keys.JSON_FILE_EXTENSION;
import static com.kora.android.common.Keys.Shared.USER;

@Singleton
public class Web3jRepositoryImpl implements Web3jRepository {

    private final Context mContext;
    private final PreferenceHandler mPreferenceHandler;
    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletUtils mEtherWalletUtils;
    private final EtherWalletStorage mEtherWalletStorage;

    @Inject
    public Web3jRepositoryImpl(final Context context,
                               final PreferenceHandler preferenceHandler,
                               final Web3jConnection web3jConnection,
                               final EtherWalletUtils etherWalletUtils,
                               final EtherWalletStorage etherWalletStorage) {
        mContext = context;
        mPreferenceHandler = preferenceHandler;
        mWeb3jConnection = web3jConnection;
        mEtherWalletUtils = etherWalletUtils;
        mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    public Observable<IdentityCreatedResponse> createWallets(final String pinCode) {
        return Observable.just(true).map(a -> {
            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

            final String ownerWalletFileName = mEtherWalletUtils.generateNewWalletFile(
                    pinCode,
                    new File(mContext.getFilesDir(), ""));
            final EtherWallet ownerEtherWallet = EtherWallet.createEtherWalletFromFileName(ownerWalletFileName);
            mEtherWalletStorage.addWallet(ownerEtherWallet);

            final String recoveryWalletFileName = mEtherWalletUtils.generateNewWalletFile(
                    pinCode,
                    new File(mContext.getFilesDir(), ""));
            final EtherWallet recoveryEtherWallet = EtherWallet.createEtherWalletFromFileName(recoveryWalletFileName);
            mEtherWalletStorage.addWallet(recoveryEtherWallet);

//            final ECKeyPair keys = ECKeyPair.create(Hex.decode(mWeb3jConnection.getKoraWalletPrivateKey()));
//            final String koraWalletFileName = mEtherWalletUtils.generateWalletFile(
//                    mWeb3jConnection.getKoraWalletPassword(),
//                    keys,
//                    new File(mContext.getFilesDir(), ""));
//            final EtherWallet koraEtherWallet = EtherWallet.createEtherWalletFromFileName(koraWalletFileName);
//            mEtherWalletStorage.addWallet(koraEtherWallet);

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

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
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

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
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

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

    @Override
    public Observable<File> getWalletFile(final String walletAddress) {
        return Observable.just(true).map(a -> {

            if (walletAddress == null || walletAddress.isEmpty())
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_identity));

            final List<EtherWallet> etherWalletList = mEtherWalletStorage.getWalletList();
            if (etherWalletList == null || etherWalletList.isEmpty())
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_wallet));

            final EtherWallet etherWallet = EtherWallet.createEtherWalletFromAddress(walletAddress);
            if (!etherWalletList.contains(etherWallet))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_wallet));

            final File walletFile = new File(mContext.getFilesDir(), etherWallet.getWalletFileName());
            if (walletFile.length() == 0)
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_wallet));

            return walletFile;
        });
    }

    @Override
    public Observable<Object> exportWalletFile(final File walletFile) {
        return Observable.just(true).map(a -> {

            final File folder = new File(Environment.getExternalStorageDirectory(), EXPORT_FOLDER_NAME);
            if (!folder.exists())
                folder.mkdirs();
            final File copy = new File(folder, walletFile.getName());
            mEtherWalletStorage.copyFile(walletFile, copy);

            final Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri uri = Uri.fromFile(copy);
            intent.setData(uri);
            mContext.sendBroadcast(intent);

            return a;
        });
    }

    @Override
    public Observable<Object> importWalletFile(final Uri walletFileUri) {
        return Observable.just(true).map(a -> {

            if (walletFileUri == null)
                throw new Exception(mContext.getString(R.string.web3j_error_message_wrong_wallet));

            final FileMetaData fileMetaData = mEtherWalletStorage.getFileMetaData(mContext, walletFileUri);
            if (fileMetaData == null)
                throw new Exception(mContext.getString(R.string.web3j_error_message_wrong_wallet));
            if (fileMetaData.getDisplayName() == null || fileMetaData.getDisplayName().isEmpty())
                throw new Exception(mContext.getString(R.string.web3j_error_message_wrong_wallet));
            if (!fileMetaData.getDisplayName().startsWith(ADDRESS_PREFIX) || !fileMetaData.getDisplayName().endsWith(JSON_FILE_EXTENSION))
                throw new Exception(mContext.getString(R.string.web3j_error_message_wrong_wallet));

            final File destination = new File(mContext.getFilesDir(), fileMetaData.getDisplayName());
            mEtherWalletStorage.copyFile(walletFileUri, destination);

            final EtherWallet etherWallet = EtherWallet.createEtherWalletFromFileName(fileMetaData.getDisplayName());
            mEtherWalletStorage.addWallet(etherWallet);

            return a;
        });
    }

    @Override
    public Observable<Object> importKoraWalletFile() {
        return Observable.just(true).map(a -> {

            final List<EtherWallet> etherWalletList = mEtherWalletStorage.getWalletList();
            final EtherWallet koraEtherWallet = EtherWallet.createEtherWalletFromAddress(mWeb3jConnection.getKoraWalletAddress());
            if (etherWalletList.contains(koraEtherWallet))
                return a;

            final ECKeyPair keys = ECKeyPair.create(Hex.decode(mWeb3jConnection.getKoraWalletPrivateKey()));
            mEtherWalletUtils.generateWalletFile(
                    mWeb3jConnection.getKoraWalletPassword(),
                    keys,
                    new File(mContext.getFilesDir(), ""));
            mEtherWalletStorage.addWallet(koraEtherWallet);

            return a;
        });
    }

    @Override
    public Observable<List<String>> createRawTransaction(final UserEntity receiver,
                                                         final double senderAmount,
                                                         final double receiverAmount,
                                                         final String pinCode) {
        return Observable.just(true).map(a -> {

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

            final UserEntity sender = mPreferenceHandler.remindObject(USER, UserEntity.class);
            if (sender == null)
                throw new Exception(mContext.getString(R.string.web3j_error_message_preferences));

            if (sender.getIdentity() == null || sender.getIdentity().isEmpty())
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_identity));

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
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

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
                                new Address(receiver.getIdentity()),
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
    public Observable<String> createCreateLoan(final UserEntity lender,
                                               final List<UserEntity> guarantors,
                                               final double borrowerAmount,
                                               final double lenderAmount,
                                               final int rate,
                                               final Date startDate,
                                               final Date maturityDate,
                                               final String pinCode) {
        return Observable.just(true).map(a -> {

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

            final UserEntity sender = mPreferenceHandler.remindObject(USER, UserEntity.class);
            if (sender == null)
                throw new Exception(mContext.getString(R.string.web3j_error_message_preferences));

            if (sender.getIdentity() == null || sender.getIdentity().isEmpty())
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_identity));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Credentials credentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            ////////////////////////////////////////////////////////////////////////////////////////

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

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

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

            final EthGetTransactionCount senderEthGetTransactionCount = web3j
                    .ethGetTransactionCount(sender.getOwner(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            final BigInteger senderTransactionCount = senderEthGetTransactionCount.getTransactionCount();

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function createLoanFunction = new Function(
                    mWeb3jConnection.getCreateLoanFunction(),
                    Arrays.asList(
                            new Address(lender.getIdentity()),
                            new DynamicArray<>(Web3jUtils.getAddressesFromUserEntities(guarantors)),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(borrowerAmount)),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(lenderAmount)),
                            new Uint256(Web3jUtils.convertRateToBigInteger(rate)),
                            new Uint256(Web3jUtils.convertDateToMs(startDate)),
                            new Uint256(Web3jUtils.convertDateToMs(maturityDate))
                    ),
                    Collections.emptyList()
            );

            final String createLoanFunctionString = FunctionEncoder.encode(createLoanFunction);
            final byte[] createLoanFunctionByteArray = Numeric.hexStringToByteArray(createLoanFunctionString);

            final Function forwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            Uint256.DEFAULT,
                            new DynamicBytes(createLoanFunctionByteArray)
                    ),
                    Collections.emptyList()
            );
            final String forwardToFunctionString = FunctionEncoder.encode(forwardToFunction);

            final RawTransaction createLoanRawTransaction = RawTransaction.createTransaction(
                    senderTransactionCount,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit(),
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    forwardToFunctionString);

            final byte[] crateLoanSignedMessage = TransactionEncoder.signMessage(createLoanRawTransaction, credentials);
            final String createLoanHexValue = Numeric.toHexString(crateLoanSignedMessage);

            return createLoanHexValue;
        });
    }

    @Override
    public Observable<String> createAgreeLoan(final String loanId,
                                      final String pinCode) {
        return Observable.just(true).map(a -> {

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

            final UserEntity sender = mPreferenceHandler.remindObject(USER, UserEntity.class);
            if (sender == null)
                throw new Exception(mContext.getString(R.string.web3j_error_message_preferences));

            if (sender.getIdentity() == null || sender.getIdentity().isEmpty())
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_identity));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Credentials credentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            ////////////////////////////////////////////////////////////////////////////////////////

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

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

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));

            final EthGetTransactionCount senderEthGetTransactionCount = web3j
                    .ethGetTransactionCount(sender.getOwner(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            final BigInteger senderTransactionCount = senderEthGetTransactionCount.getTransactionCount();

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function agreeLoanFunction = new Function(
                    mWeb3jConnection.getAgreeLoanFunction(),
                    Collections.singletonList(
                            new Uint256(new BigInteger(loanId))
                    ),
                    Collections.emptyList()
            );

            final String agreeLoanFunctionString = FunctionEncoder.encode(agreeLoanFunction);
            final byte[] agreeLoanFunctionByteArray = Numeric.hexStringToByteArray(agreeLoanFunctionString);

            final Function forwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            Uint256.DEFAULT,
                            new DynamicBytes(agreeLoanFunctionByteArray)
                    ),
                    Collections.emptyList()
            );
            final String forwardToFunctionString = FunctionEncoder.encode(forwardToFunction);

            final RawTransaction agreeLoanRawTransaction = RawTransaction.createTransaction(
                    senderTransactionCount,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit(),
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    forwardToFunctionString);

            final byte[] agreeLoanSignedMessage = TransactionEncoder.signMessage(agreeLoanRawTransaction, credentials);
            final String agreeLoanHexValue = Numeric.toHexString(agreeLoanSignedMessage);

            return agreeLoanHexValue;
        });
    }
}