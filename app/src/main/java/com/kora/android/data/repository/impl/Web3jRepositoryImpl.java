package com.kora.android.data.repository.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.util.Pair;

import com.kora.android.R;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.Web3jUtils;
import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.data.web3j.model.response.CreateWalletsResponse;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.storage.FileMetaData;
import com.kora.android.data.web3j.utils.EtherWalletUtils;
import com.kora.android.presentation.model.UserEntity;

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
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
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
    public Observable<CreateWalletsResponse> createWallets(final String pinCode) {
        return Observable.just(true).map(a -> {
            checkConnection();

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

            return new CreateWalletsResponse()
                    .addOwner(ownerEtherWallet.getAddress())
                    .addRecovery(recoveryEtherWallet.getAddress());
        });
    }

    @Override
    public Observable<String> getBalance(final String proxyAddress,
                                         final String smartContractAddress) {
        return Observable.just(true).map(a -> {
            checkConnection();

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Function function = new Function(
                    mWeb3jConnection.getGetBalanceFunction(),
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
    public Observable<String> createRawTransaction(final UserEntity receiver,
                                                   final double senderAmount,
                                                   final double receiverAmount,
                                                   final Integer interestRate,
                                                   final String pinCode) {
        return Observable.just(true).map(a -> {
            checkConnection();

            final UserEntity sender = getUser();
            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials senderCredentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            checkEthBalance(web3j, sender);
            checkTokenBalance(web3j, sender, senderAmount);

            final BigInteger senderTransactionCount = getTransactionCount(web3j, sender.getOwner());

            ////////////////////////////////////////////////////////////////////////////////////////

            final String receiverAddress = (sender.getCurrency().equals(receiver.getCurrency()))
                    ? receiver.getIdentity()
                    : mWeb3jConnection.getKoraWalletAddress();

            double simpleOrTotalSenderAmount;
            if (interestRate != null) {
                final double totalInterest = Math.floor(senderAmount * (double) interestRate) / 100;
                simpleOrTotalSenderAmount = senderAmount + totalInterest;
            } else {
                simpleOrTotalSenderAmount = senderAmount;
            }

            final Function transferFunction = new Function(
                    mWeb3jConnection.getGetTransferFunction(),
                    Arrays.asList(
                            new Address(receiverAddress),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(simpleOrTotalSenderAmount))),
                    Collections.emptyList());
            final byte[] senderTransferFunctionByteArray = getFunctionByteArray(transferFunction);

            final Function forwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(sender.getERC20Token()),
                            Uint256.DEFAULT,
                            new DynamicBytes(senderTransferFunctionByteArray)),
                    Collections.emptyList());
            final String senderHexValue = getTransactionHexValue(
                    forwardToFunction,
                    senderTransactionCount,
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    senderCredentials);

            return senderHexValue;
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
            checkConnection();

            final UserEntity sender = getUser();
            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials credentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            checkEthBalance(web3j, sender);

            final BigInteger senderTransactionCount = getTransactionCount(web3j, sender.getOwner());

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function createLoanFunction = new Function(
                    mWeb3jConnection.getCreateLoanFunction(),
                    Arrays.asList(
                            new Address(lender.getIdentity()),
                            new DynamicArray<>(Web3jUtils.getAddressesFromUserEntities(guarantors)),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(borrowerAmount)),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(lenderAmount)),
                            new Uint256(Web3jUtils.convertRateToBigInteger(rate)),
                            new Uint256(Web3jUtils.convertDateToSeconds(startDate)),
                            new Uint256(Web3jUtils.convertDateToSeconds(maturityDate))),
                    Collections.emptyList());
            final byte[] createLoanFunctionByteArray = getFunctionByteArray(createLoanFunction);

            final Function forwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            Uint256.DEFAULT,
                            new DynamicBytes(createLoanFunctionByteArray)),
                    Collections.emptyList());
            final String createLoanHexValue = getTransactionHexValue(
                    forwardToFunction,
                    senderTransactionCount,
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    credentials);

            return createLoanHexValue;
        });
    }

    @Override
    public Observable<String> createAgreeLoan(final String loanId,
                                              final String pinCode) {
        return Observable.just(true).map(a -> {
            checkConnection();

            final UserEntity sender = getUser();
            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials credentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            checkEthBalance(web3j, sender);

            final BigInteger senderTransactionCount = getTransactionCount(web3j, sender.getOwner());

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function agreeLoanFunction = new Function(
                    mWeb3jConnection.getAgreeLoanFunction(),
                    Collections.singletonList(
                            new Uint256(new BigInteger(loanId))),
                    Collections.emptyList());
            final byte[] agreeLoanFunctionByteArray = getFunctionByteArray(agreeLoanFunction);

            final Function forwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            Uint256.DEFAULT,
                            new DynamicBytes(agreeLoanFunctionByteArray)),
                    Collections.emptyList());
            final String agreeLoanHexValue = getTransactionHexValue(
                    forwardToFunction,
                    senderTransactionCount,
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    credentials);

            return agreeLoanHexValue;
        });
    }

    @Override
    public Observable<Pair<String, String>> createFundLoan(final String borrowerErc20Token,
                                                                 final String lenderErc20Token,
                                                                 final double borrowerAmount,
                                                                 final double lenderAmount,
                                                                 final String loanId,
                                                                 final String pinCode) {
        return Observable.just(true).map(a -> {
            checkConnection();

            final UserEntity sender = getUser();
            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials senderCredentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            checkEthBalance(web3j, sender);
            checkTokenBalance(web3j, sender, lenderAmount);

            final BigInteger senderTransactionCount = getTransactionCount(web3j, sender.getOwner());

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function lenderApproveFunction = new Function(
                    mWeb3jConnection.getApproveFunction(),
                    Arrays.asList(
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(lenderAmount))),
                    Collections.emptyList());
            final byte[] lenderApproveFunctionByteArray = getFunctionByteArray(lenderApproveFunction);

            final Function lenderApproveForwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(lenderErc20Token),
                            Uint256.DEFAULT,
                            new DynamicBytes(lenderApproveFunctionByteArray)),
                    Collections.emptyList());
            final String lenderApproveHexValue = getTransactionHexValue(
                    lenderApproveForwardToFunction,
                    senderTransactionCount,
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    senderCredentials);

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function lenderFundLoanFunction = new Function(
                    mWeb3jConnection.getFundLoanFunction(),
                    Arrays.asList(
                            new Uint256(new BigInteger(loanId)),
                            new Address(borrowerErc20Token),
                            new Address(lenderErc20Token),
                            new Address(mWeb3jConnection.getKoraWalletAddress())),
                    Collections.emptyList());
            final byte[] lenderFundLoanFunctionByteArray = getFunctionByteArray(lenderFundLoanFunction);

            final Function lenderFundLoanForwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            Uint256.DEFAULT,
                            new DynamicBytes(lenderFundLoanFunctionByteArray)),
                    Collections.emptyList());
            final String lenderFundLoanHexValue = getTransactionHexValue(
                    lenderFundLoanForwardToFunction,
                    Web3jUtils.increaseTransactionCount(senderTransactionCount),
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    senderCredentials);

            ////////////////////////////////////////////////////////////////////////////////////////

            return new Pair<>(lenderApproveHexValue, lenderFundLoanHexValue);
        });
    }

    @Override
    public Observable<Pair<List<String>, String>> createPayBackLoan(final String loanId,
                                                                    final String borrowerErc20Token,
                                                                    final String lenderErc20Token,
                                                                    final double borrowerValue,
                                                                    final double borrowerBalance,
                                                                    final double lenderBalance,
                                                                    final String pinCode) {
        return Observable.just(true).map(a -> {
            checkConnection();

            final UserEntity sender = getUser();
            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials senderCredentials = mEtherWalletStorage.getCredentials(
                    Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner()),
                    pinCode);

            checkEthBalance(web3j, sender);
            checkTokenBalance(web3j, sender, borrowerValue);

            final BigInteger senderTransactionCount = getTransactionCount(web3j, sender.getOwner());

            ////////////////////////////////////////////////////////////////////////////////////////

            final List<String> rawApproves = new ArrayList<>();

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function borrowerApproveFunction = new Function(
                    mWeb3jConnection.getApproveFunction(),
                    Arrays.asList(
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(borrowerValue))),
                    Collections.emptyList());
            final byte[] borrowerApproveFunctionByteArray = getFunctionByteArray(borrowerApproveFunction);

            final Function borrowerApproveForwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(borrowerErc20Token),
                            Uint256.DEFAULT,
                            new DynamicBytes(borrowerApproveFunctionByteArray)),
                    Collections.emptyList());
            final String borrowerApproveHexValue = getTransactionHexValue(
                    borrowerApproveForwardToFunction,
                    senderTransactionCount,
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    senderCredentials);

            rawApproves.add(borrowerApproveHexValue);

            ////////////////////////////////////////////////////////////////////////////////////////

            final Function payBackLoanFunction = new Function(
                    mWeb3jConnection.getPayBackLoanFunction(),
                    Arrays.asList(
                            new Uint256(new BigInteger(loanId)),
                            new Address(borrowerErc20Token),
                            new Address(lenderErc20Token),
                            new Address(mWeb3jConnection.getKoraWalletAddress()),
                            new Uint256(Web3jUtils.convertTokenToBigInteger(borrowerValue))),
                    Collections.emptyList());
            final byte[] payBackLoanFunctionByteArray = getFunctionByteArray(payBackLoanFunction);

            final Function forwardToFunction = new Function(
                    mWeb3jConnection.getForwardToFunction(),
                    Arrays.asList(
                            new Address(sender.getOwner()),
                            new Address(sender.getIdentity()),
                            new Address(mWeb3jConnection.getKoraLendRinkeby()),
                            Uint256.DEFAULT,
                            new DynamicBytes(payBackLoanFunctionByteArray)),
                    Collections.emptyList());
            final String payBackLoanHexValue = getTransactionHexValue(
                    forwardToFunction,
                    Web3jUtils.increaseTransactionCount(senderTransactionCount),
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    senderCredentials);

            ////////////////////////////////////////////////////////////////////////////////////////

            return new Pair<>(rawApproves, payBackLoanHexValue);
        });
    }

    private void checkConnection() throws Exception {
        if (!CommonUtils.isNetworkConnected(mContext))
            throw new Exception(mContext.getString(R.string.web3j_error_message_no_network));
    }

    private UserEntity getUser() throws Exception {
        final UserEntity sender = mPreferenceHandler.remindObject(USER, UserEntity.class);
        if (sender == null)
            throw new Exception(mContext.getString(R.string.web3j_error_message_preferences));
        if (sender.getIdentity() == null || sender.getIdentity().isEmpty())
            throw new Exception(mContext.getString(R.string.web3j_error_message_no_identity));
        return sender;
    }

    private BigInteger getTransactionCount(final Web3j web3j,
                                           final String address) throws Exception {

        final EthGetTransactionCount senderEthGetTransactionCount = web3j
                .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();
        return senderEthGetTransactionCount.getTransactionCount();
    }

    private void checkEthBalance(final Web3j web3j,
                                 final UserEntity sender) throws Exception {

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
    }

    private void checkTokenBalance(final Web3j web3j,
                                   final UserEntity sender,
                                   final double value) throws Exception {

        final Function getBalanceFunction = new Function(
                mWeb3jConnection.getGetBalanceFunction(),
                Collections.singletonList(new Address(sender.getIdentity())),
                Collections.singletonList(new TypeReference<Uint256>() {
                })
        );
        final String encodedFunction = FunctionEncoder.encode(getBalanceFunction);

        final EthCall response = web3j
                .ethCall(
                        Transaction.createEthCallTransaction(
                                sender.getIdentity(),
                                sender.getERC20Token(),
                                encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();
        final List<Type> someTypes = FunctionReturnDecoder.decode(response.getValue(), getBalanceFunction.getOutputParameters());
        final BigInteger balanceIdentityBigInteger = (BigInteger) someTypes.get(0).getValue();
        final double balanceIdentityToken = Web3jUtils.convertBigIntegerToToken(balanceIdentityBigInteger);

        if (value > balanceIdentityToken)
            throw new Exception(mContext.getString(R.string.web3j_error_message_identity_balance,
                    String.valueOf(value),
                    sender.getCurrency(),
                    String.valueOf(balanceIdentityToken),
                    sender.getCurrency()));
    }

    private byte[] getFunctionByteArray(final Function function) {
        final String payBackLonaFunctionString = FunctionEncoder.encode(function);
        return Numeric.hexStringToByteArray(payBackLonaFunctionString);
    }

    private String getTransactionHexValue(final Function function,
                                          final BigInteger transactionCount,
                                          final String address,
                                          final Credentials credentials) {
        final String functionString = FunctionEncoder.encode(function);

        final RawTransaction rawTransaction = RawTransaction.createTransaction(
                transactionCount,
                mWeb3jConnection.getGasPrice(),
                mWeb3jConnection.getGasLimit(),
                address,
                functionString);

        final byte[] signedMessage = TransactionEncoder.signMessage(
                rawTransaction,
                credentials);
        return Numeric.toHexString(signedMessage);
    }
}