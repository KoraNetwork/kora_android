package com.kora.android.data.web3j.connection;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;

import static com.kora.android.data.web3j.Constants.FUNCTION_AGREE_LOAN;
import static com.kora.android.data.web3j.Constants.FUNCTION_APPROVE;
import static com.kora.android.data.web3j.Constants.FUNCTION_CREATE_LOAN;
import static com.kora.android.data.web3j.Constants.FUNCTION_FORWARD_TO;
import static com.kora.android.data.web3j.Constants.FUNCTION_FUND_LOAN;
import static com.kora.android.data.web3j.Constants.FUNCTION_GET_BALANCE;
import static com.kora.android.data.web3j.Constants.FUNCTION_PAY_BACK_LOAN;
import static com.kora.android.data.web3j.Constants.FUNCTION_TRANSFER;
import static com.kora.android.data.web3j.Constants.KORA_WALLET_ADDRESS;
import static com.kora.android.data.web3j.Constants.MIN_OWNER_BALANCE;
import static com.kora.android.data.web3j.Constants.RINKEBY_KORA_LOAN_ADDRESS;
import static com.kora.android.data.web3j.Constants.RINKEBY_META_IDENTITY_MANAGER_ADDRESS;
import static com.kora.android.data.web3j.Constants.RINKEBY_TESTNET_CLIENT;
import static com.kora.android.data.web3j.Constants.GAS_PRICE;
import static com.kora.android.data.web3j.Constants.GAS_LIMIT;

public class Web3jConnection {

    private Web3j getWeb3j(final String testnetClient) {
        final InfuraHttpService infuraHttpService = new InfuraHttpService(testnetClient);
        return Web3jFactory.build(infuraHttpService);
    }

    public Web3j getWeb3jRinkeby() {
        return getWeb3j(RINKEBY_TESTNET_CLIENT);
    }

    public BigInteger getGasPrice() {
        return GAS_PRICE;
    }

    public BigInteger getGasLimit() {
        return GAS_LIMIT;
    }

    public String getMetaIdentityManagerRinkeby() {
        return RINKEBY_META_IDENTITY_MANAGER_ADDRESS;
    }

    public String getKoraWalletAddress() {
        return KORA_WALLET_ADDRESS;
    }

    public double getMinOwnerBalance() {
        return MIN_OWNER_BALANCE;
    }

    public String getGetBalanceFunction() {
        return FUNCTION_GET_BALANCE;
    }

    public String getGetTransferFunction() {
        return FUNCTION_TRANSFER;
    }

    public String getForwardToFunction() {
        return FUNCTION_FORWARD_TO;
    }

    public String getApproveFunction() {
        return FUNCTION_APPROVE;
    }

    public String getKoraLendRinkeby() { return RINKEBY_KORA_LOAN_ADDRESS; }

    public String getCreateLoanFunction() { return FUNCTION_CREATE_LOAN; }

    public String getAgreeLoanFunction() { return FUNCTION_AGREE_LOAN; }

    public String getFundLoanFunction() { return FUNCTION_FUND_LOAN; }

    public String getPayBackLoanFunction() { return FUNCTION_PAY_BACK_LOAN; }
}
