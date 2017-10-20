package com.kora.android.domain.usecase.transaction;

import android.content.Context;

import com.kora.android.R;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.Web3jUtils;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.smart_contracts.HumanStandardToken;
import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;

import io.reactivex.Observable;

import static com.kora.android.common.Keys.Shared.USER;

@ConfigPersistent
public class SendTransactionUseCase extends AsyncUseCase {

    private final Context mContext;
    private final PreferenceHandler mPreferenceHandler;
    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;

    private String mPinCode;
    private UserEntity mReceiver;
    private double mSenderAmount;
    private double mReceiverAmount;

    @Inject
    public SendTransactionUseCase(final Context context,
                                  final PreferenceHandler preferenceHandler,
                                  final Web3jConnection web3jConnection,
                                  final EtherWalletStorage etherWalletStorage) {
        mContext = context;
        mPreferenceHandler = preferenceHandler;
        mWeb3jConnection = web3jConnection;
        mEtherWalletStorage = etherWalletStorage;
    }

    public void setData(final String pinCode,
                        final UserEntity receiver,
                        final double senderAmount,
                        final double receiverAmount) {
        mPinCode = pinCode;
        mSenderAmount = senderAmount;
        mReceiver = receiver;
        mReceiverAmount = receiverAmount;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {
            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final UserEntity sender = mPreferenceHandler.remindObject(USER, UserEntity.class);
            if (sender == null) return Collections.emptyList();

            final String ownerFileName = Web3jUtils.getKeystoreFileNameFromAddress(sender.getOwner());
            final Credentials sendCredentials = mEtherWalletStorage.getCredentials(ownerFileName, mPinCode);



            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();



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

            if (mSenderAmount > balanceIdentityToken)
                throw new Exception(mContext.getString(R.string.web3j_error_message_identity_balance,
                        String.valueOf(mSenderAmount),
                        String.valueOf(balanceIdentityToken)));



            if (sender.getCurrency().equals(mReceiver.getCurrency())) {

                if (!CommonUtils.isNetworkConnected(mContext))
                    throw new Exception(mContext.getString(R.string.web3j_error_message_network));

                final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
                        mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                        web3j,
                        sendCredentials,
                        mWeb3jConnection.getGasPrice(),
                        mWeb3jConnection.getGasLimit()
                );

                final Function transferFunction = new Function(
                        mWeb3jConnection.getGetTransferFunction(),
                        Arrays.asList(
                                new Address(mReceiver.getIdentity()),
                                new Uint256(Web3jUtils.convertTokenToBigInteger(mSenderAmount))
                        ),
                        Collections.emptyList()
                );
                final String stringFunction = FunctionEncoder.encode(transferFunction);
                final byte[] byteArrayFunction = Numeric.hexStringToByteArray(stringFunction);

                final TransactionReceipt forwardToTransactionReceipt = metaIdentityManager.forwardTo(
                        new Address(sender.getOwner()),
                        new Address(sender.getIdentity()),
                        new Address(sender.getERC20Token()),
                        Uint256.DEFAULT,
                        new DynamicBytes(byteArrayFunction)).get();

                return Collections.singletonList(forwardToTransactionReceipt.getTransactionHash());



            } else {

                if (!CommonUtils.isNetworkConnected(mContext))
                    throw new Exception(mContext.getString(R.string.web3j_error_message_network));

                final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
                        mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                        web3j,
                        sendCredentials,
                        mWeb3jConnection.getGasPrice(),
                        mWeb3jConnection.getGasLimit()
                );

                final Function transferFunction = new Function(
                        mWeb3jConnection.getGetTransferFunction(),
                        Arrays.asList(
                                new Address(mWeb3jConnection.getKoraWalletAddress()),
                                new Uint256(Web3jUtils.convertTokenToBigInteger(mSenderAmount))
                        ),
                        Collections.emptyList()
                );
                final String stringFunction = FunctionEncoder.encode(transferFunction);
                final byte[] byteArrayFunction = Numeric.hexStringToByteArray(stringFunction);

                final Future<TransactionReceipt> forwardToFuture = metaIdentityManager.forwardTo(
                        new Address(sender.getOwner()),
                        new Address(sender.getIdentity()),
                        new Address(sender.getERC20Token()),
                        Uint256.DEFAULT,
                        new DynamicBytes(byteArrayFunction));



                final Credentials koraCredentials = mEtherWalletStorage.getCredentials(
                        mWeb3jConnection.getKoraWalletFileName(),
                        mWeb3jConnection.getKoraWalletPassword());

                final HumanStandardToken humanStandardToken = HumanStandardToken.load(
                        mReceiver.getERC20Token(),
                        web3j,
                        koraCredentials,
                        mWeb3jConnection.getGasPrice(),
                        mWeb3jConnection.getGasLimit()
                );

                final Future<TransactionReceipt> transferFuture = humanStandardToken.transfer(
                        new Address(mReceiver.getIdentity()),
                        new Uint256(Web3jUtils.convertTokenToBigInteger(mReceiverAmount))
                );



                final TransactionReceipt forwardToTransactionReceipt = forwardToFuture.get();
                final TransactionReceipt transferTransactionReceipt = transferFuture.get();



                return Arrays.asList(
                        forwardToTransactionReceipt.getTransactionHash(),
                        transferTransactionReceipt.getTransactionHash());
            }
        });
    }
}