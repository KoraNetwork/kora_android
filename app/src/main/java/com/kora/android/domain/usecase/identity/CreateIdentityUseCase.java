package com.kora.android.domain.usecase.identity;

import android.util.Log;

import com.kora.android.common.helper.ProxyPrefHelper;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.smart_contracts.HumanStandardToken;
import com.kora.android.data.web3j.smart_contracts.IdentityManager;
import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;

import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class CreateIdentityUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;
    private final ProxyPrefHelper mProxyPrefHelper;

    private Response.Error mError;

    @Inject
    public CreateIdentityUseCase(final Web3jConnection web3jConnection,
                                 final EtherWalletStorage etherWalletStorage,
                                 final ProxyPrefHelper proxyPrefHelper) {
        this.mProxyPrefHelper = proxyPrefHelper;
        this.mWeb3jConnection = web3jConnection;
        this.mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Single buildTask() {
        return Single.just(true).map(a -> {

            final String ownerFileName = "5c3d13b00f0fde8de60c45ab62ec0125c6b0f890".toLowerCase();
            final String password = "123456789";

            final String ownerAddress = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890".toLowerCase();
            final String recoveryAddress = "0xBb01aE766D1c3E6873d793F4f6ADb7bd63A07c7c".toLowerCase();
            final String receiverAddress = "0x471fff4a05bbd9c5cab781464d6a4e0f1582779a".toLowerCase();

            final String smartContractAddress = "0xe57768e12c50c7d4134bb7a1ca2917689719183c";
            final String proxyAddress = "0xe0a7ba5eaba1c4599f91edd639d080fd670efe4d";

            ////////////////////////////////////////////////////////////////////////////////////////

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials credentials = mEtherWalletStorage.getCredentials(ownerFileName, password);

            final Function function = new Function(
                    "transfer",
                    Arrays.asList(
                            new Address(receiverAddress),
                            new Uint256(new BigInteger("1000000000000000000"))
                    ),
                    Collections.emptyList()
            );

            final String stringFunction = FunctionEncoder.encode(function);
            final byte[] byteArrayFunction = Numeric.hexStringToByteArray(stringFunction);

            final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    web3j,
                    credentials,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit()
            );

            final TransactionReceipt transactionReceipt = metaIdentityManager.forwardTo(
                    new Address(ownerAddress),
                    new Address(proxyAddress),
                    new Address(smartContractAddress),
                    Uint256.DEFAULT,
                    new DynamicBytes(byteArrayFunction)
            ).get();
            Log.e("_____", transactionReceipt.getTransactionHash());

//            final HumanStandardToken humanStandardToken = HumanStandardToken.load(
//                    smartContractAddress,
//                    web3j,
//                    credentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit()
//            );

//            final long l1 = Long.MAX_VALUE;
//            final long l2 = Long.MAX_VALUE;
//            final BigInteger bigInteger = new BigInteger("1000000000000000000000");
//
//            final HumanStandardToken humanStandardToken = HumanStandardToken.deploy(
//                    web3j,
//                    credentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit(),
//                    BigInteger.ZERO,
//                    new Uint256(bigInteger),
//                    new Utf8String("Kora Token"),
//                    new Uint8(BigInteger.valueOf(18)),
//                    new Utf8String("KTN")
//            ).get();

//            final Uint256 totalSupplyUint256 = humanStandardToken.totalSupply().get();
//            final BigDecimal totalSupplyBigDecimal = Convert.fromWei(new BigDecimal(totalSupplyUint256.getValue()), Convert.Unit.ETHER);
//            Log.e("_____", "totalSupply:" + String.valueOf(totalSupplyBigDecimal));
//
//            final Uint256 balanceUint256 = humanStandardToken.balanceOf(new Address(credentials.getAddress())).get();
//            final BigDecimal balanceBigDecimal = Convert.fromWei(new BigDecimal(balanceUint256.getValue()), Convert.Unit.ETHER);
//            Log.e("_____", "balance:" + String.valueOf(balanceBigDecimal));

//            final TransactionReceipt transactionReceipt = humanStandardToken.transfer(
//                    new Address(proxyAddress),
//                    new Uint256(new BigInteger("100000000000000000000"))
//            ).get();
//            Log.e("_____", "transactionHash:" + transactionReceipt.getTransactionHash());



//            final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
//                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
//                    web3j,
//                    credentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit()
//            );

//            final TransactionReceipt transactionReceipt = metaIdentityManager.createIdentity(
//                    new Address(ownerAddress), new Address(recoveryAddress)
//            ).get();

//            final MetaIdentityManager.IdentityCreatedEventResponse identityCreatedEventResponse =
//                    metaIdentityManager.getIdentityCreatedEvents(transactionReceipt).get(0);
//            Log.e("_____", identityCreatedEventResponse.toString());
//
//            mProxyPrefHelper.storeProxyAddress(identityCreatedEventResponse.getIdentity().toString());



//            final String identityAddress = mProxyPrefHelper.getProxyAddress();
//            Log.e("_____", identityAddress);

//            final Bool bool = metaIdentityManager.isOwner(
//                    new Address(proxyAddress),
//                    new Address(ownerAddress)
//            ).get();
//            Log.e("_____", String.valueOf(bool.getValue()));

//            final Bool bool = metaIdentityManager.isRecovery(
//                    new Address(identityAddress),
//                    new Address(recoveryAddress)
//            ).get();
//            Log.e("_____", String.valueOf(bool.getValue()));



//            final String identityAddress = mProxyPrefHelper.getProxyAddress();
//            Log.e("_____", identityAddress);

//            final TransactionReceipt transactionReceipt = metaIdentityManager.forwardTo(
//                    new Address(ownerAddress),
//                    new Address(identityAddress),
//                    new Address(recoveryAddress),
//                    new Uint256(BigInteger.valueOf(10000000000000000L)),
//                    DynamicBytes.DEFAULT
//            ).get();
//            Log.e("_____", transactionReceipt.getTransactionHash());



            return "";
        });
    }
}