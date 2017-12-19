package com.kora.android.domain.usecase.test;

import android.util.Log;

import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.smart_contracts.HumanStandardToken;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Observable;

public class TestUseCase extends AsyncUseCase {

    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletStorage mEtherWalletStorage;

    private Response.Error mError;

    @Inject
    public TestUseCase(final Web3jConnection web3jConnection,
                       final EtherWalletStorage etherWalletStorage) {
        this.mWeb3jConnection = web3jConnection;
        this.mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {

            final String ownerFileName = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890.json".toLowerCase();
            final String password = "123456789";

            final String ownerAddress = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890".toLowerCase();
            final String recoveryAddress = "0xBb01aE766D1c3E6873d793F4f6ADb7bd63A07c7c".toLowerCase();
            final String receiverAddress = "0x471fff4a05bbd9c5cab781464d6a4e0f1582779a".toLowerCase();

            final String smartContractAddress = "0xe57768e12c50c7d4134bb7a1ca2917689719183c";
            final String proxyAddress = "0xe0a7ba5eaba1c4599f91edd639d080fd670efe4d";

            ////////////////////////////////////////////////////////////////////////////////////////

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials credentials = mEtherWalletStorage.getCredentials(ownerFileName, password);



            // CREATE TOKEN SMART CONTRACT
//            final HumanStandardToken humanStandardToken = HumanStandardToken.deploy(
//                    web3j,
//                    credentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit(),
//                    BigInteger.ZERO,
//                    new Uint256(new BigInteger("1000000000000000000000")),
//                    new Utf8String("Kora Token"),
//                    new Uint8(BigInteger.valueOf(18)),
//                    new Utf8String("KTN")
//            ).get();



            // LOAD TOKEN SMART CONTRACT AND CALL FUNCTIONS
            final HumanStandardToken humanStandardToken = HumanStandardToken.load(
                    "0x545053968018bff704408dacf69bb8f9cacf161f",
                    web3j,
                    credentials,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit()
            );
//
//            final Uint256 totalSupplyUint256 = humanStandardToken.totalSupply().get();
//            final BigDecimal totalSupplyBigDecimal = Convert.fromWei(new BigDecimal(totalSupplyUint256.getValue()), Convert.Unit.ETHER);
//            Log.e("_____", "totalSupply:" + String.valueOf(totalSupplyBigDecimal));
//
//            final Uint256 balanceUint256 = humanStandardToken.balanceOf(new Address(proxyAddress)).get();
//            final BigDecimal balanceBigDecimal = Convert.fromWei(new BigDecimal(balanceUint256.getValue()), Convert.Unit.ETHER);
//            Log.e("_____", "balance:" + String.valueOf(balanceBigDecimal));
//
            final TransactionReceipt transactionReceipt = humanStandardToken.transfer(
                    new Address("0x43319ff89263849d63fe7c0fc21dcb410a5b5f0e"),
                    new Uint256(new BigInteger("100000"))
            ).get();
            Log.e("_____", "transactionHash:" + transactionReceipt.getTransactionHash());



            // ETH_CALL balanceOf()
//            final Function function = new Function(
//                    "balanceOf",
//                    Collections.singletonList(new Address(proxyAddress)),
//                    Collections.singletonList(new TypeReference<Uint256>() {})
//            );
//            final String encodedFunction = FunctionEncoder.encode(function);
//
//            final EthCall response = web3j
//                    .ethCall(
//                            Transaction.createEthCallTransaction(ownerAddress, smartContractAddress, encodedFunction),
//                            DefaultBlockParameterName.LATEST)
//                    .sendAsync()
//                    .get();
//
//            final List<Type> someTypes = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
//            Log.e("_____", String.valueOf(someTypes.get(0).getValue()));


            // TRANSACTIONS USING UPORT
//            final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
//                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
//                    web3j,
//                    credentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit()
//            );
//
//            final Function function = new Function(
//                    "transfer",
//                    Arrays.asList(
//                            new Address(receiverAddress),
//                            new Uint256(new BigInteger("1000000000000000000"))
//                    ),
//                    Collections.emptyList()
//            );
//
//            final String stringFunction = FunctionEncoder.encode(function);
//            final byte[] byteArrayFunction = Numeric.hexStringToByteArray(stringFunction);
//
//            ethCallCheckBalance(web3j, proxyAddress, smartContractAddress);
//
//            final TransactionReceipt transactionReceipt = metaIdentityManager.forwardTo(
//                    new Address(ownerAddress),
//                    new Address(proxyAddress),
//                    new Address(smartContractAddress),
//                    Uint256.DEFAULT,
//                    new DynamicBytes(byteArrayFunction)
//            ).get();
//            Log.e("_____", transactionReceipt.getTransactionHash());
//
//            ethCallCheckBalance(web3j, proxyAddress, smartContractAddress);









//            final String bankFileName = "97bb2587b02715e2936b95f36892a457966757ff";
//            final String bankAddress = "0x97bb2587b02715e2936b95f36892a457966757ff";
//
//            final Credentials ownerCredentials = mEtherWalletStorage.getCredentials(ownerFileName, password);
//            final Credentials bankCredentials = mEtherWalletStorage.getCredentials(bankFileName, password);
//
//            final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
//                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
//                    web3j,
//                    ownerCredentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit()
//            );
//
//            final HumanStandardToken humanStandardToken = HumanStandardToken.load(
//                    smartContractAddress,
//                    web3j,
//                    bankCredentials,
//                    mWeb3jConnection.getGasPrice(),
//                    mWeb3jConnection.getGasLimit()
//            );
//
//            final Function function = new Function(
//                    "transfer",
//                    Arrays.asList(
//                            new Address("0x97bb2587b02715e2936b95f36892a457966757ff"),
//                            new Uint256(new BigInteger("1000000000000000000"))
//                    ),
//                    Collections.emptyList()
//            );
//
//            final String stringFunction = FunctionEncoder.encode(function);
//            final byte[] byteArrayFunction = Numeric.hexStringToByteArray(stringFunction);
//
//            final Future<TransactionReceipt> forwardTo = metaIdentityManager.forwardTo(
//                    new Address(ownerAddress),
//                    new Address(proxyAddress),
//                    new Address(smartContractAddress),
//                    Uint256.DEFAULT,
//                    new DynamicBytes(byteArrayFunction)
//            );
//
//            final Future<TransactionReceipt> transfer = humanStandardToken.transfer(
//                    new Address(proxyAddress),
//                    new Uint256(new BigInteger("500000000000000000"))
//            );
//
//            final TransactionReceipt forwardToTransactionReceipt = forwardTo.get();
//            final TransactionReceipt transferTransactionReceipt = transfer.get();
//
//            Log.e("_____", "bankTransactionHash:" + forwardToTransactionReceipt.getTransactionHash());
//            Log.e("_____", "ownerTransactionHash:" + transferTransactionReceipt.getTransactionHash());










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

//            final MetaIdentityManager.IdentityCreatedResponse identityCreatedEventResponse =
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

    private void ethCallCheckBalance(final Web3j web3j,
                                     final String checkBalanceAddress,
                                     final String smartContractAddress)
            throws ExecutionException, InterruptedException {

        final Function function = new Function(
                "balanceOf",
                Collections.singletonList(new Address(checkBalanceAddress)),
                Collections.singletonList(new TypeReference<Uint256>() {
                })
        );
        final String encodedFunction = FunctionEncoder.encode(function);

        final EthCall response = web3j
                .ethCall(
                        Transaction.createEthCallTransaction(checkBalanceAddress, smartContractAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        final List<Type> someTypes = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        Log.e("_____", String.valueOf(someTypes.get(0).getValue()));
    }
}