package com.kora.android.data.web3j.connection;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;

import static com.kora.android.data.web3j.Constants.DEFAULT_OWNER_BALANCE;
import static com.kora.android.data.web3j.Constants.FUNCTION_GET_BALANCE;
import static com.kora.android.data.web3j.Constants.FUNCTION_TRANSFER;
import static com.kora.android.data.web3j.Constants.KORA_WALLET_ADDRESS;
import static com.kora.android.data.web3j.Constants.KORA_WALLET_FILE_NAME;
import static com.kora.android.data.web3j.Constants.KORA_WALLET_PASSWORD;
import static com.kora.android.data.web3j.Constants.KORA_WALLET_PRIVATE_KEY;
import static com.kora.android.data.web3j.Constants.MIN_OWNER_BALANCE;
import static com.kora.android.data.web3j.Constants.RINKEBY_IDENTITY_MANAGER_ADDRESS;
import static com.kora.android.data.web3j.Constants.RINKEBY_META_IDENTITY_MANAGER_ADDRESS;
import static com.kora.android.data.web3j.Constants.ROPSTEN_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.RINKEBY_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.KOVAN_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.GAS_PRICE;
import static com.kora.android.data.web3j.Constants.GAS_LIMIT;

public class Web3jConnection {

    private Web3j getWeb3j(final String testnetClient) {
        final InfuraHttpService infuraHttpService = new InfuraHttpService(testnetClient);
        return Web3jFactory.build(infuraHttpService);
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

    public BigInteger getGasPrice() {
        return GAS_PRICE;
    }

    public BigInteger getGasLimit() {
        return GAS_LIMIT;
    }

    public String getIdentityManagerRinkeby() {
        return RINKEBY_IDENTITY_MANAGER_ADDRESS;
    }

    public String getMetaIdentityManagerRinkeby() {
        return RINKEBY_META_IDENTITY_MANAGER_ADDRESS;
    }

    public String getKoraWalletPrivateKey() {
        return KORA_WALLET_PRIVATE_KEY;
    }

    public String getKoraWalletAddress() {
        return KORA_WALLET_ADDRESS;
    }

    public String getKoraWalletFileName() {
        return KORA_WALLET_FILE_NAME;
    }

    public String getKoraWalletPassword() {
        return KORA_WALLET_PASSWORD;
    }

    public double getDefaultOwnerBalance() {
        return DEFAULT_OWNER_BALANCE;
    }

    public double getMinOwnerBalance() {
        return MIN_OWNER_BALANCE;
    }

    public String getGetBalabceFunction() {
        return FUNCTION_GET_BALANCE;
    }

    public String getGetTransferFunction() {
        return FUNCTION_TRANSFER;
    }
}
