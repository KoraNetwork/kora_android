package com.kora.android.data.web3j.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kora.android.data.network.enumclass.Kind;
import com.kora.android.data.network.exception.RetrofitException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;

import static com.kora.android.data.web3j.Constants.RINKEBY_IDENTITY_MANAGER_ADDRESS;
import static com.kora.android.data.web3j.Constants.KOVAN_IDENTITY_MANAGER_ADDRESS;
import static com.kora.android.data.web3j.Constants.ROPSTEN_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.RINKEBY_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.KOVAN_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.GAS_PRICE;
import static com.kora.android.data.web3j.Constants.GAS_LIMIT;

public class Web3jConnection {

    private final Context mContext;

    public Web3jConnection(final Context context) {
        this.mContext = context;
    }

    public Web3j getWeb3jRopsten() {
       return getWeb3j(ROPSTEN_TESTNET_CLIENT);
    }

    public Web3j getWeb3jRinkeby() {
        return getWeb3j(RINKEBY_TESTNET_CLIENT);
    }

    public Web3j getWeb3jKovan() {
        return getWeb3j(KOVAN_TESTNET_CLIENT);
    }

    private Web3j getWeb3j(String testnetClient) {
        if (isNetworkConnected(mContext)) {
            final InfuraHttpService infuraHttpService = new InfuraHttpService(testnetClient);
            return Web3jFactory.build(infuraHttpService);
        } else
            throw new RetrofitException("Network is offline.", null, null, Kind.NETWORK, null, null);
    }

    private boolean isNetworkConnected(final Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public BigInteger getGasPrice() {
        return GAS_PRICE;
    }

    public BigInteger getGasLimit() {
        return GAS_LIMIT;
    }

    public String getIdentityManagerRinkeby() {
        return RINKEBY_IDENTITY_MANAGER_ADDRESS;
    }

    public String getIdentityManagerKovan() {
        return KOVAN_IDENTITY_MANAGER_ADDRESS;
    }
}
