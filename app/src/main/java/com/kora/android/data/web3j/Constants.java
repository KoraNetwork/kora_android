package com.kora.android.data.web3j;

import java.math.BigInteger;

public interface Constants {

    String RINKEBY_TESTNET_CLIENT = "https://rinkeby.infura.io/Q86Oq3CueCrb1idXkNGW";
    String ROPSTEN_TESTNET_CLIENT = "https://ropsten.infura.io/Q86Oq3CueCrb1idXkNGW";
    String KOVAN_TESTNET_CLIENT = "https://kovan.infura.io/Q86Oq3CueCrb1idXkNGW";

    BigInteger GAS_PRICE = BigInteger.valueOf(21000000000L);
    BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);

    String KORA_WALLET_PRIVATE_KEY = "29984ecd73f5e4f9e2fc5ce49dfe61a5b13571ab34c0d427ca066a170cc4e851";
    String KORA_WALLET_ADDRESS = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
    String KORA_WALLET_FILE_NAME = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890.json";
    String KORA_WALLET_PASSWORD = "123456789";

    double DEFAULT_OWNER_BALANCE = 0.12;
    double MIN_OWNER_BALANCE = 0.1;

    String RINKEBY_IDENTITY_MANAGER_ADDRESS = "0xabbcd5b340c80b5f1c0545c04c987b87310296ae";
    String RINKEBY_META_IDENTITY_MANAGER_ADDRESS = "0x7c338672f483795eCA47106dC395660D95041DBe";

    String FUNCTION_TRANSFER = "transfer";
    String FUNCTION_FORWARD_TO = "forwardTo";

    String FUNCTION_GET_BALANCE = "balanceOf";
    String FUNCTION_APPROVE = "approve";

    String RINKEBY_KORA_LOAN_ADDRESS = "0x181790fdb1934fa0b616f2e5c4e0fce5f98443fe";

    String FUNCTION_CREATE_LOAN = "createLoan";
    String FUNCTION_AGREE_LOAN = "agreeLoan";
    String FUNCTION_FUND_LOAN = "agreeLoan";
    String FUNCTION_PAY_BACK_LOAN = "payBackLoan";
}
