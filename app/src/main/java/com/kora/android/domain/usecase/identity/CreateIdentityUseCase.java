package com.kora.android.domain.usecase.identity;

import android.content.Context;

import com.kora.android.R;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.data.web3j.smart_contracts.MetaIdentityManager;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.utils.EtherWalletUtils;
import com.kora.android.domain.base.AsyncUseCase;

import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;

public class CreateIdentityUseCase extends AsyncUseCase {

    private final Context mContext;
    private final Web3jConnection mWeb3jConnection;
    private final EtherWalletUtils mEtherWalletUtils;
    private final EtherWalletStorage mEtherWalletStorage;

    private String mPinCode;

    @Inject
    public CreateIdentityUseCase(final Context context,
                                 final Web3jConnection web3jConnection,
                                 final EtherWalletUtils etherWalletUtils,
                                 final EtherWalletStorage etherWalletStorage) {
        mContext = context;
        mWeb3jConnection = web3jConnection;
        mEtherWalletUtils = etherWalletUtils;
        mEtherWalletStorage = etherWalletStorage;
    }

    public void setData(final String pinCode) {
        mPinCode = pinCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {
            if (!CommonUtils.isNetworkConnected(mContext))
                throw new Exception(mContext.getString(R.string.web3j_error_message_network));

            final String ownerWalletFileName = mEtherWalletUtils.generateNewWalletFile(
                    mPinCode,
                    new File(mContext.getFilesDir(), ""));
            final EtherWallet ownerEtherWallet = EtherWallet.creteOwnerEtherWallet(ownerWalletFileName);
            mEtherWalletStorage.addWallet(ownerEtherWallet);

            final String recoveryWalletFileName = mEtherWalletUtils.generateNewWalletFile(
                    mPinCode,
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
                    koraWalletFileName,
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
}
