package com.kora.android.domain.usecase.balance;

import android.content.Context;
import android.util.Log;

import com.kora.android.common.utils.CommonUtils;
import com.kora.android.data.network.enumclass.Kind;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetBalanceUseCase extends AsyncUseCase {

    private final Context mContext;
    private final Web3jConnection mWeb3jConnection;

    private String mProxyAddress;
    private String mSmartContractAddress;

    @Inject
    public GetBalanceUseCase(final Context context,
                             final Web3jConnection web3jConnection) {
        mContext = context;
        mWeb3jConnection = web3jConnection;
    }

    public void setData(final String proxyAddress,
                        final String smartContractAddress) {
        mProxyAddress = proxyAddress;
        mSmartContractAddress = smartContractAddress;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {

            if (!CommonUtils.isNetworkConnected(mContext))
                throw new RetrofitException("Network is offline.", null, null, Kind.NETWORK, null, null);

            final Web3j web3j = mWeb3jConnection.getWeb3jRinkeby();

            final Function function = new Function(
                    mWeb3jConnection.getEthCallGetBalabce(),
                    Collections.singletonList(new Address(mProxyAddress)),
                    Collections.singletonList(new TypeReference<Uint256>() {
                    })
            );
            final String encodedFunction = FunctionEncoder.encode(function);

            final EthCall response = web3j
                    .ethCall(
                            Transaction.createEthCallTransaction(mProxyAddress, mSmartContractAddress, encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            final List<Type> someTypes = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            return String.valueOf(someTypes.get(0).getValue());
        });
    }
}
