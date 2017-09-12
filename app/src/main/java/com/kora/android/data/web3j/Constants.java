package com.kora.android.data.web3j;

import java.math.BigInteger;

public interface Constants {

    String RINKEBY_IDENTITY_MANAGER_ADDRESS = "0xabbcd5b340c80b5f1c0545c04c987b87310296ae";
    String KOVAN_IDENTITY_MANAGER_ADDRESS = "0x71845bbfe5ddfdb919e780febfff5eda62a30fdc";

    String ROPSTEN_TESTNET_CLIENT = "https://ropsten.infura.io/Q86Oq3CueCrb1idXkNGW";
    String RINKEBY_TESTNET_CLIENT = "https://rinkeby.infura.io/Q86Oq3CueCrb1idXkNGW";
    String KOVAN_TESTNET_CLIENT = "https://kovan.infura.io/Q86Oq3CueCrb1idXkNGW";

    BigInteger GAS_PRICE = BigInteger.valueOf(21000000000L);
    BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);
}
