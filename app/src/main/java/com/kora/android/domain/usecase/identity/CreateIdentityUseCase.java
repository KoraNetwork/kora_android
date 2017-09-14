package com.kora.android.domain.usecase.identity;

import android.util.Log;

import com.kora.android.common.helper.ProxyPrefHelper;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.smart_contracts.IdentityManager;
import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

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

            final String walletFileName = "5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
            final String password = "123456789";
            final String addressFrom = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
            final BigInteger amount = BigInteger.ZERO;
            final String addressTo = "0x97bb2587b02715e2936b95f36892a457966757ff";

            ////////////////////////////////////////////////////////////////////////////////////////

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();
            final Credentials credentials = mEtherWalletStorage.getCredentials(walletFileName, password);

            final MetaIdentityManager metaIdentityManager = MetaIdentityManager.load(
                    mWeb3jConnection.getMetaIdentityManagerRinkeby(),
                    web3j,
                    credentials,
                    mWeb3jConnection.getGasPrice(),
                    mWeb3jConnection.getGasLimit()
            );



//            final TransactionReceipt transactionReceipt = metaIdentityManager.createIdentity(
//                    new Address(addressFrom), new Address(password)
//            ).get();
//
//            final List<MetaIdentityManager.IdentityCreatedEventResponse> identityCreatedEventResponses =
//                    metaIdentityManager.getIdentityCreatedEvents(transactionReceipt);
//
//            final MetaIdentityManager.IdentityCreatedEventResponse identityCreatedEventResponse =
//                    identityCreatedEventResponses.get(0);
//            Log.e("_____", identityCreatedEventResponse.toString());
//
//            mProxyPrefHelper.storeProxyAddress(identityCreatedEventResponse.getIdentity().toString());



//            final String identityAddress = mProxyPrefHelper.getProxyAddress();
//            Log.e("_____", identityAddress);
//
//            final Bool bool = metaIdentityManager.isOwner(
//                    new Address(identityAddress),
//                    new Address(addressFrom)
//            ).get();
//            Log.e("_____", String.valueOf(bool.getValue()));



            final String identityAddress = mProxyPrefHelper.getProxyAddress();
            Log.e("_____", identityAddress);

            final TransactionReceipt transactionReceipt = metaIdentityManager.forwardTo(
                    new Address(addressFrom),
                    new Address(identityAddress),
                    new Address(addressTo),
                    new Uint256(BigInteger.ONE),
                    DynamicBytes.DEFAULT
            ).get();
            Log.e("_____", transactionReceipt.getTransactionHash());



            return "";
        });
    }
}