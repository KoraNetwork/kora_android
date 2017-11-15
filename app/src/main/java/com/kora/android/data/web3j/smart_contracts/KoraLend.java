package com.kora.android.data.web3j.smart_contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.3.1.
 */
public final class KoraLend extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b610bc48061001e6000396000f300606060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063b65ee58c14610067578063d463eeb31461008a578063d5c1ce6f146100b3578063e1ec3c681461016d575b600080fd5b341561007257600080fd5b610088600480803590602001909190505061023b565b005b341561009557600080fd5b61009d610562565b6040518082815260200191505060405180910390f35b34156100be57600080fd5b610157600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091908035906020019091908035906020019091908035906020019091908035906020019091908035906020019091905050610568565b6040518082815260200191505060405180910390f35b341561017857600080fd5b61018e6004808035906020019091905050610957565b604051808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200187815260200186815260200185815260200184815260200183815260200182600481111561022057fe5b60ff1681526020019850505050505050505060405180910390f35b600081600080600481111561024c57fe5b6001600084815260200190815260200160002060080160009054906101000a900460ff16600481111561027b57fe5b14151561028757600080fd5b6001600085815260200190815260200160002060060154600160008681526020019081526020016000206007015442821180156102c8575062015180820181115b15156102d357600080fd5b856000806000809250600160008581526020019081526020016000206002019150600090505b818054905081101561038457818181548110151561031357fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141561037757600192505b80806001019150506102f9565b82151561039057600080fd5b896001600082815260200190815260200160002060090160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515156103fe57600080fd5b600160008c8152602001908152602001600020995060018060008d815260200190815260200160002060090160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055507f3586057158cdf38cb36b0a1844dada4c404b611f4d5d6a15bad82399f350410a8b33604051808381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a16104f28b6109ec565b156105555760018a60080160006101000a81548160ff0219169083600481111561051857fe5b02179055507f65cb07019675982f669d27dfbbefd0f582e38e6cb80db11a32d5abe4a539d28f8b6040518082815260200191505060405180910390a15b5050505050505050505050565b60005481565b600087876000806000835111801561058257506003835111155b151561058d57600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff16141515156105c957600080fd5b3373ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415151561060457600080fd5b600091505b825182101561074757600073ffffffffffffffffffffffffffffffffffffffff16838381518110151561063857fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff161415151561066557600080fd5b8373ffffffffffffffffffffffffffffffffffffffff16838381518110151561068a57fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff16141515156106b757600080fd5b600090505b8181101561073a5782818151811015156106d257fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff16838381518110151561070057fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff161415151561072d57600080fd5b80806001019150506106bc565b8180600101925050610609565b8686428211801561075c575062015180820181115b151561076757600080fd5b600080815480929190600101919050559650610120604051908101604052803373ffffffffffffffffffffffffffffffffffffffff1681526020018f73ffffffffffffffffffffffffffffffffffffffff1681526020018e81526020018d81526020018c81526020018b81526020018a8152602001898152602001600060048111156107ef57fe5b8152506001600089815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020190805190602001906108af929190610acb565b50606082015181600301556080820151816004015560a0820151816005015560c0820151816006015560e082015181600701556101008201518160080160006101000a81548160ff0219169083600481111561090757fe5b02179055509050507f2abd59ed776138fb6191e8e3313cd669b51d5a2cab430c2ba0bf2f0cf0c0c425876040518082815260200191505060405180910390a1505050505050979650505050505050565b60016020528060005260406000206000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030154908060040154908060050154908060060154908060070154908060080160009054906101000a900460ff16905088565b600080600060019250600160008581526020019081526020016000209150600090505b8160020180549050811015610ac1578160090160008360020183815481101515610a3557fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610ab457600092505b8080600101915050610a0f565b8292505050919050565b828054828255906000526020600020908101928215610b44579160200282015b82811115610b435782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555091602001919060010190610aeb565b5b509050610b519190610b55565b5090565b610b9591905b80821115610b9157600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101610b5b565b5090565b905600a165627a7a72305820eafb55c4bb7ddc815c09f4804a979ba152687c49f1779ccfa5df7428ce051c8a0029";

    private KoraLend(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private KoraLend(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<LoanCreatedEventResponse> getLoanCreatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanCreated", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanCreatedEventResponse> responses = new ArrayList<LoanCreatedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanCreatedEventResponse typedResponse = new LoanCreatedEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanCreatedEventResponse> loanCreatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanCreated", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanCreatedEventResponse>() {
            @Override
            public LoanCreatedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanCreatedEventResponse typedResponse = new LoanCreatedEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<GuarantorAgreedEventResponse> getGuarantorAgreedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("GuarantorAgreed", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<GuarantorAgreedEventResponse> responses = new ArrayList<GuarantorAgreedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            GuarantorAgreedEventResponse typedResponse = new GuarantorAgreedEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.guarantor = (Address) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<GuarantorAgreedEventResponse> guarantorAgreedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("GuarantorAgreed", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, GuarantorAgreedEventResponse>() {
            @Override
            public GuarantorAgreedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                GuarantorAgreedEventResponse typedResponse = new GuarantorAgreedEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                typedResponse.guarantor = (Address) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public List<LoanAgreedEventResponse> getLoanAgreedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanAgreed", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanAgreedEventResponse> responses = new ArrayList<LoanAgreedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanAgreedEventResponse typedResponse = new LoanAgreedEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanAgreedEventResponse> loanAgreedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanAgreed", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanAgreedEventResponse>() {
            @Override
            public LoanAgreedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanAgreedEventResponse typedResponse = new LoanAgreedEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Future<TransactionReceipt> agreeLoan(Uint256 loanId) {
        Function function = new Function("agreeLoan", Arrays.<Type>asList(loanId), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> numLoans() {
        Function function = new Function("numLoans", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> createLoan(Address lender, DynamicArray<Address> guarantors, Uint256 borrowerAmount, Uint256 lenderAmount, Uint256 rate, Uint256 startDate, Uint256 maturityDate) {
        Function function = new Function("createLoan", Arrays.<Type>asList(lender, guarantors, borrowerAmount, lenderAmount, rate, startDate, maturityDate), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<List<Type>> loans(Uint256 param0) {
        Function function = new Function("loans", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public static Future<KoraLend> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(KoraLend.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<KoraLend> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(KoraLend.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static KoraLend load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new KoraLend(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static KoraLend load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new KoraLend(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class LoanCreatedEventResponse {
        public Uint256 loanId;
    }

    public static class GuarantorAgreedEventResponse {
        public Uint256 loanId;

        public Address guarantor;
    }

    public static class LoanAgreedEventResponse {
        public Uint256 loanId;
    }
}
