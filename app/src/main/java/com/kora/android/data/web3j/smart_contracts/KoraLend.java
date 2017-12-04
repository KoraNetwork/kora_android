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
import org.web3j.abi.datatypes.Bool;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b6111458061001e6000396000f3006060604052600436106100985763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416632d3f38e4811461009d578063510dca19146100d45780637db412cb14610104578063b65ee58c1461013d578063d05951a014610153578063d34af2d71461017d578063d463eeb3146101ae578063d5c1ce6f146101c1578063e1ec3c6814610236575b600080fd5b34156100a857600080fd5b6100bc600160a060020a03600435166102d3565b60405191825260208201526040908101905180910390f35b34156100df57600080fd5b610102600435600160a060020a0360243581169060443581169060643516610302565b005b341561010f57600080fd5b61012b600160a060020a0360043516602435151560443561052d565b60405190815260200160405180910390f35b341561014857600080fd5b610102600435610568565b341561015e57600080fd5b610169600435610741565b604051901515815260200160405180910390f35b341561018857600080fd5b610102600435600160a060020a03602435811690604435811690606435166084356108b0565b34156101b957600080fd5b61012b610b4f565b34156101cc57600080fd5b61012b60048035600160a060020a031690604460248035908101908301358060208082020160405190810160405280939291908181526020018383602002808284375094965050843594602081013594506040810135935060608101359250608001359050610b55565b341561024157600080fd5b61024c600435610e59565b604051808b600160a060020a0316600160a060020a031681526020018a600160a060020a0316600160a060020a031681526020018981526020018881526020018781526020018681526020018581526020018481526020018381526020018260058111156102b657fe5b60ff1681526020019a505050505050505050505060405180910390f35b600160a060020a0316600090815260026020908152604080832060018452909152808220548280529120549091565b600080856001806000838152600160205260409020600a015460ff16600581111561032957fe5b1461033357600080fd5b60008881526001602052604090206008810154600990910154428211801561036057508162015180018110155b151561036b57600080fd5b60008a815260016020819052604090912001548a9033600160a060020a0390811691161461039857600080fd5b600160a060020a038a16158015906103b85750600160a060020a03891615155b15156103c357600080fd5b60008b81526001602052604081209097509550600160a060020a038a8116908a16141561042b5760048601546003870154146103fe57600080fd5b600186015486546003880154610424928d92600160a060020a0391821692911690610ebc565b965061048b565b600160a060020a038816151561044057600080fd5b60018601546004870154610463918b91600160a060020a03909116908b90610ebc565b8015610488575085546003870154610488918c918b91600160a060020a031690610ebc565b96505b8615610520576104a386600301548760050154610f65565b6006870155600486015460058701546104bc9190610f65565b60078701819055600a8701805460ff1916600317905560068701547f6009e2c04884d1faf1de4c6d01f16d7acc6d1b2038996b6cd751c3c975dbf274918d919060405180848152602001838152602001828152602001935050505060405180910390a15b5050505050505050505050565b60026020528260005260406000206020528160005260406000208181548110151561055457fe5b600091825260209091200154925083915050565b60008181806000838152600160205260409020600a015460ff16600581111561058d57fe5b1461059757600080fd5b6000848152600160205260409020600881015460099091015442821180156105c457508162015180018110155b15156105cf57600080fd5b6000868152600160205260408120879190600201815b81548110156106295781818154811015156105fc57fe5b60009182526020909120015433600160a060020a039081169116141561062157600192505b6001016105e5565b82151561063557600080fd5b60008a8152600160209081526040808320600160a060020a0333168452600b019091529020548a9060ff161561066a57600080fd5b60008b815260016020818152604080842033600160a060020a0381168652600b820190935293819020805460ff1916909317909255919b507f3586057158cdf38cb36b0a1844dada4c404b611f4d5d6a15bad82399f350410a918d9151918252600160a060020a031660208201526040908101905180910390a16106ed8b610f70565b1561052057600a8a01805460ff191660011790557f65cb07019675982f669d27dfbbefd0f582e38e6cb80db11a32d5abe4a539d28f8b60405190815260200160405180910390a15050505050505050505050565b60008181526001602052604081206002600a82015460ff16600581111561076457fe5b1415801561078557506005600a82015460ff16600581111561078257fe5b14155b151561079057600080fd5b806008015442101580156107d257506000600a82015460ff1660058111156107b457fe5b14806107d257506001600a82015460ff1660058111156107d057fe5b145b1561082257600a8101805460ff191660021790557fcf230024caa09eb797262f4b8a440ff68d6569427807de536898b30c7db013008360405190815260200160405180910390a1600191506108aa565b80600901544211801561084757506003600a82015460ff16600581111561084557fe5b145b1561009857600a8101805460ff19166005179055805461087290600160a060020a0316600085610fe8565b7ff39869fa528ee7cc49f717ec484f4b69b167958c5ce2bd5f3e80c4e877943c168360405190815260200160405180910390a1600191505b50919050565b6000808080886003806000838152600160205260409020600a015460ff1660058111156108d957fe5b146108e357600080fd5b60008b8152600160205260409020548b9033600160a060020a0390811691161461090c57600080fd5b60008c81526001602052604090206008810154909750429011801590610936575086600901544211155b151561094157600080fd5b600160a060020a038b16158015906109615750600160a060020a038a1615155b151561096c57600080fd5b60009550879450866006015485111561098757866006015494505b89600160a060020a03168b600160a060020a031614156109df5760048701546003880154146109b557600080fd5b865460018801548695506109d8918d91600160a060020a03918216911687610ebc565b9550610a63565b600160a060020a03891615156109f457600080fd5b8660060154851415610a0c5786600701549350610a25565b866006015487600701548602811515610a2157fe5b0493505b8654610a3d908c90600160a060020a03168b88610ebc565b8015610a6057506001870154610a60908b908b90600160a060020a031687610ebc565b95505b8515610b415760068701805486900390819055600788018054869003908190557f912c96f80892dc498664e97cca5dec2095b89c63a5b462972ffa963ea5812ee0918e9188918891604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390a160068701541515610b4157600a8701805460ff191660041790558654610b0d90600160a060020a031660018e610fe8565b7f60c5bf138604dd0054d792d746e9850320594eff02d83fad60ac81adb9184b248c60405190815260200160405180910390a15b505050505050505050505050565b60005481565b6000878760008060008351118015610b6f57506003835111155b1515610b7a57600080fd5b600160a060020a0384161515610b8f57600080fd5b33600160a060020a031684600160a060020a031614151515610bb057600080fd5b600091505b8251821015610c86576000838381518110610bcc57fe5b90602001906020020151600160a060020a03161415610bea57600080fd5b83600160a060020a0316838381518110610c0057fe5b90602001906020020151600160a060020a03161415610c1e57600080fd5b5060005b81811015610c7b57828181518110610c3657fe5b90602001906020020151600160a060020a0316838381518110610c5557fe5b90602001906020020151600160a060020a03161415610c7357600080fd5b600101610c22565b600190910190610bb5565b86864282118015610c9c57508162015180018110155b1515610ca757600080fd5b600080546001810190915596506101606040519081016040528033600160a060020a031681526020018f600160a060020a031681526020018e81526020018d81526020018c81526020018b815260200160008152602001600081526020018a815260200189815260200160006005811115610d1e57fe5b905260008881526001602052604090208151815473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0391909116178155602082015160018201805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055604082015181600201908051610da692916020019061102e565b50606082015181600301556080820151816004015560a0820151816005015560c0820151816006015560e0820151816007015561010082015181600801556101208201518160090155610140820151600a8201805460ff19166001836005811115610e0d57fe5b02179055509050507f2abd59ed776138fb6191e8e3313cd669b51d5a2cab430c2ba0bf2f0cf0c0c4258760405190815260200160405180910390a1505050505050979650505050505050565b60016020819052600091825260409091208054918101546003820154600483015460058401546006850154600786015460088701546009880154600a90980154600160a060020a03998a1699909716979596949593949293919290919060ff168a565b600084600160a060020a0381166323b872dd86868686604051602001526040517c010000000000000000000000000000000000000000000000000000000063ffffffff8616028152600160a060020a0393841660048201529190921660248201526044810191909152606401602060405180830381600087803b1515610f4157600080fd5b6102c65a03f11515610f5257600080fd5b5050506040518051979650505050505050565b612710908202040190565b6000818152600160208190526040822090915b6002820154811015610fe15781600b0160008360020183815481101515610fa657fe5b6000918252602080832090910154600160a060020a0316835282019290925260400190205460ff161515610fd957600092505b600101610f83565b5050919050565b600160a060020a03831660009081526002602090815260408083208515158452909152902080546001810161101d83826110a2565b506000918252602090912001555050565b828054828255906000526020600020908101928215611092579160200282015b82811115611092578251825473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03919091161782556020929092019160019091019061104e565b5061109e9291506110cb565b5090565b8154818355818115116110c6576000838152602090206110c69181019083016110ff565b505050565b6110fc91905b8082111561109e57805473ffffffffffffffffffffffffffffffffffffffff191681556001016110d1565b90565b6110fc91905b8082111561109e57600081556001016111055600a165627a7a72305820843098db48397d5c5c58b3cda3c596fd40b76ab7c887ef997d01048bf3168ead0029";

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

    public List<LoanFundedEventResponse> getLoanFundedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanFunded", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanFundedEventResponse> responses = new ArrayList<LoanFundedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanFundedEventResponse typedResponse = new LoanFundedEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.borrowerBalance = (Uint256) eventValues.getNonIndexedValues().get(1);
            typedResponse.lenderBalance = (Uint256) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanFundedEventResponse> loanFundedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanFunded", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanFundedEventResponse>() {
            @Override
            public LoanFundedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanFundedEventResponse typedResponse = new LoanFundedEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                typedResponse.borrowerBalance = (Uint256) eventValues.getNonIndexedValues().get(1);
                typedResponse.lenderBalance = (Uint256) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public List<LoanPaymentDoneEventResponse> getLoanPaymentDoneEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanPaymentDone", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanPaymentDoneEventResponse> responses = new ArrayList<LoanPaymentDoneEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanPaymentDoneEventResponse typedResponse = new LoanPaymentDoneEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.borrowerValue = (Uint256) eventValues.getNonIndexedValues().get(1);
            typedResponse.lenderValue = (Uint256) eventValues.getNonIndexedValues().get(2);
            typedResponse.borrowerBalance = (Uint256) eventValues.getNonIndexedValues().get(3);
            typedResponse.lenderBalance = (Uint256) eventValues.getNonIndexedValues().get(4);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanPaymentDoneEventResponse> loanPaymentDoneEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanPaymentDone", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanPaymentDoneEventResponse>() {
            @Override
            public LoanPaymentDoneEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanPaymentDoneEventResponse typedResponse = new LoanPaymentDoneEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                typedResponse.borrowerValue = (Uint256) eventValues.getNonIndexedValues().get(1);
                typedResponse.lenderValue = (Uint256) eventValues.getNonIndexedValues().get(2);
                typedResponse.borrowerBalance = (Uint256) eventValues.getNonIndexedValues().get(3);
                typedResponse.lenderBalance = (Uint256) eventValues.getNonIndexedValues().get(4);
                return typedResponse;
            }
        });
    }

    public List<LoanPaidBackEventResponse> getLoanPaidBackEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanPaidBack", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanPaidBackEventResponse> responses = new ArrayList<LoanPaidBackEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanPaidBackEventResponse typedResponse = new LoanPaidBackEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanPaidBackEventResponse> loanPaidBackEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanPaidBack", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanPaidBackEventResponse>() {
            @Override
            public LoanPaidBackEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanPaidBackEventResponse typedResponse = new LoanPaidBackEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<LoanExpiredEventResponse> getLoanExpiredEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanExpired", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanExpiredEventResponse> responses = new ArrayList<LoanExpiredEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanExpiredEventResponse typedResponse = new LoanExpiredEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanExpiredEventResponse> loanExpiredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanExpired", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanExpiredEventResponse>() {
            @Override
            public LoanExpiredEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanExpiredEventResponse typedResponse = new LoanExpiredEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<LoanOverdueEventResponse> getLoanOverdueEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoanOverdue", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<LoanOverdueEventResponse> responses = new ArrayList<LoanOverdueEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            LoanOverdueEventResponse typedResponse = new LoanOverdueEventResponse();
            typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoanOverdueEventResponse> loanOverdueEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoanOverdue", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoanOverdueEventResponse>() {
            @Override
            public LoanOverdueEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                LoanOverdueEventResponse typedResponse = new LoanOverdueEventResponse();
                typedResponse.loanId = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Future<List<Type>> getNumBorrowerLoans(Address borrower) {
        Function function = new Function("getNumBorrowerLoans", 
                Arrays.<Type>asList(borrower), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> fundLoan(Uint256 loanId, Address borrowerToken, Address lenderToken, Address koraWallet) {
        Function function = new Function("fundLoan", Arrays.<Type>asList(loanId, borrowerToken, lenderToken, koraWallet), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> borrowerLoans(Address param0, Bool param1, Uint256 param2) {
        Function function = new Function("borrowerLoans", 
                Arrays.<Type>asList(param0, param1, param2), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> agreeLoan(Uint256 loanId) {
        Function function = new Function("agreeLoan", Arrays.<Type>asList(loanId), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> closeLoan(Uint256 loanId) {
        Function function = new Function("closeLoan", Arrays.<Type>asList(loanId), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> payBackLoan(Uint256 loanId, Address borrowerToken, Address lenderToken, Address koraWallet, Uint256 value) {
        Function function = new Function("payBackLoan", Arrays.<Type>asList(loanId, borrowerToken, lenderToken, koraWallet, value), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> numLoans() {
        Function function = new Function("numLoans", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> createLoan(Address lender, DynamicArray<Address> guarantors, Uint256 borrowerAmount, Uint256 lenderAmount, Uint256 interestRate, Uint256 startDate, Uint256 maturityDate) {
        Function function = new Function("createLoan", Arrays.<Type>asList(lender, guarantors, borrowerAmount, lenderAmount, interestRate, startDate, maturityDate), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<List<Type>> loans(Uint256 param0) {
        Function function = new Function("loans", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
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

    public static class LoanFundedEventResponse {
        public Uint256 loanId;

        public Uint256 borrowerBalance;

        public Uint256 lenderBalance;
    }

    public static class LoanPaymentDoneEventResponse {
        public Uint256 loanId;

        public Uint256 borrowerValue;

        public Uint256 lenderValue;

        public Uint256 borrowerBalance;

        public Uint256 lenderBalance;
    }

    public static class LoanPaidBackEventResponse {
        public Uint256 loanId;
    }

    public static class LoanExpiredEventResponse {
        public Uint256 loanId;
    }

    public static class LoanOverdueEventResponse {
        public Uint256 loanId;
    }
}
