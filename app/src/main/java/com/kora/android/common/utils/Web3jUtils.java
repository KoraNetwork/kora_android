package com.kora.android.common.utils;

import com.fasterxml.jackson.databind.node.BigIntegerNode;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class Web3jUtils {

    public static String getKeystoreFileNameFromAddress(final String address) {
        return address.substring(2);
    }

    public static double convertWeiToEth(final BigInteger bigInteger) {
        return bigInteger.divide(new BigInteger("10000000000000000")).longValue() / 100d;
    }

    public static double convertBigIntegerToToken(final BigInteger bigInteger) {
        return bigInteger.longValue() / 100d;
    }

    public static BigInteger convertTokenToBigInteger(final double d) {
        return BigInteger.valueOf((long) (d * 100));
    }

    public static String convertDoubleToString(final double d) {
        return new DecimalFormat("#.##").format(d);
    }
}
