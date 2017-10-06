package com.kora.android.data.web3j;

import java.math.BigInteger;

public interface Constants {

    String RINKEBY_IDENTITY_MANAGER_ADDRESS = "0xabbcd5b340c80b5f1c0545c04c987b87310296ae";
    String RINKEBY_META_IDENTITY_MANAGER_ADDRESS = "0x7c338672f483795eCA47106dC395660D95041DBe";

    String ROPSTEN_TESTNET_CLIENT = "https://ropsten.infura.io/Q86Oq3CueCrb1idXkNGW";
    String RINKEBY_TESTNET_CLIENT = "https://rinkeby.infura.io/Q86Oq3CueCrb1idXkNGW";
    String KOVAN_TESTNET_CLIENT = "https://kovan.infura.io/Q86Oq3CueCrb1idXkNGW";

    BigInteger GAS_PRICE = BigInteger.valueOf(21000000000L);
    BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);

    String KORA_WALLET_PRIVATE_KEY = "29984ecd73f5e4f9e2fc5ce49dfe61a5b13571ab34c0d427ca066a170cc4e851";
    String KORA_WALLET_PASSWORD = "123456789";
}
