package com.kora.android.data.web3j;

import java.math.BigInteger;

public interface Constants {

    String RINKEBY_TESTNET_CLIENT = "https://rinkeby.infura.io/Q86Oq3CueCrb1idXkNGW";

    BigInteger GAS_PRICE = BigInteger.valueOf(2000000000L);
    BigInteger GAS_LIMIT = BigInteger.valueOf(1000000L);

    String KORA_WALLET_ADDRESS = "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";

    double MIN_OWNER_BALANCE = 0.003;

    String RINKEBY_META_IDENTITY_MANAGER_ADDRESS = "0x7c338672f483795eCA47106dC395660D95041DBe";

    String FUNCTION_TRANSFER = "transfer";
    String FUNCTION_FORWARD_TO = "forwardTo";

    String FUNCTION_GET_BALANCE = "balanceOf";
    String FUNCTION_APPROVE = "approve";

    String RINKEBY_KORA_LOAN_ADDRESS = "0x52a6ea5a93a58f51f1761145b1a93538f64df292";

    String FUNCTION_CREATE_LOAN = "createLoan";
    String FUNCTION_AGREE_LOAN = "agreeLoan";
    String FUNCTION_FUND_LOAN = "fundLoan";
    String FUNCTION_PAY_BACK_LOAN = "payBackLoan";

    String RINKKEBY_TX_URL = "https://rinkeby.etherscan.io/tx/";
}
