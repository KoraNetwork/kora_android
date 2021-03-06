package com.kora.android.data.web3j.smart_contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
public final class MetaIdentityManager extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516080806114a2833981016040528080519190602001805191906020018051919060200180519150505b60008390556001849055600282905560038054600160a060020a031916600160a060020a0383161790555b505050505b6114288061007a6000396000f300606060405236156100bf5763ffffffff60e060020a60003504166311fe12b381146100c45780631a45fac7146100fd5780631e2629e11461012a5780633061e0ef1461015757806332967ea014610184578063422e33f3146101bd578063701b8826146101f8578063754fd3521461026b578063781f5a83146102985780637ddc02d4146102bf578063a5967039146102f8578063a949c6371461031f578063c7576ed41461034c578063c778427b14610373578063d10e73ab146103a4575b600080fd5b34156100cf57600080fd5b6100e9600160a060020a03600435811690602435166103cb565b604051901515815260200160405180910390f35b341561010857600080fd5b610128600160a060020a03600435811690602435811690604435166103f2565b005b341561013557600080fd5b610128600160a060020a0360043581169060243581169060443516610513565b005b341561016257600080fd5b610128600160a060020a03600435811690602435811690604435166105e2565b005b341561018f57600080fd5b6100e9600160a060020a0360043581169060243516610728565b604051901515815260200160405180910390f35b34156101c857600080fd5b6101dc600160a060020a0360043516610792565b604051600160a060020a03909116815260200160405180910390f35b341561020357600080fd5b61012860048035600160a060020a03908116916024803583169260443516916064359160a49060843590810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506107ad95505050505050565b005b341561027657600080fd5b610128600160a060020a03600435811690602435811690604435166108e6565b005b34156102a357600080fd5b610128600160a060020a0360043581169060243516610a50565b005b34156102ca57600080fd5b6100e9600160a060020a0360043581169060243516610b2a565b604051901515815260200160405180910390f35b341561030357600080fd5b610128600160a060020a0360043581169060243516610b94565b005b341561032a57600080fd5b610128600160a060020a0360043581169060243581169060443516610c6c565b005b341561035757600080fd5b610128600160a060020a0360043581169060243516610d95565b005b341561037e57600080fd5b610392600160a060020a0360043516610f24565b60405190815260200160405180910390f35b34156103af57600080fd5b610128600160a060020a0360043581169060243516610f36565b005b600160a060020a038281166000908152600560205260409020548116908216145b92915050565b60035433600160a060020a039081169116148061041357506104133361100c565b5b156100bf5781836104258282610728565b156100bf57600254600160a060020a038086166000908152600660209081526040808320938a16835292905220548591879142919091039010156100bf57600160a060020a0380831660009081526006602090815260408083208585168452825280832042905589841680845260048352818420948a168085529490925280832092909255907fc5d55f5b9504a5698fdae1a66c6327ad53ed43440071e233dc0caad0ca8406d4908a9051600160a060020a03909116815260200160405180910390a35b6104f3565b600080fd5b5b5050610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b60035433600160a060020a039081169116148061053457506105343361100c565b5b156100bf5781836105468282610728565b156100bf57600160a060020a0384811660008181526007602090815260408083204290556008909152908190208054600160a060020a03191693871693841790557fcfc00227bf2b3a4415906c30423e1d01d99604f93a0d756dc25fbf69cf233e2090889051600160a060020a03909116815260200160405180910390a35b610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b60035433600160a060020a039081169116148061060357506106033361100c565b5b156100bf5781836106158282610728565b156100bf57600254600160a060020a038086166000908152600660209081526040808320938a16835292905220548591879142919091039010156100bf57600160a060020a0380831660009081526006602090815260408083208585168452909152902042905585908116156100bf57600160a060020a03878116600081815260056020526040908190208054600160a060020a031916938a1693841790557fa9bb12bc681659b583320c1fa0fbf4d8c1dfb4fcd51566c8f6edf9438a8b8ece908b9051600160a060020a03909116815260200160405180910390a35b6106fc565b600080fd5b5b506104f3565b600080fd5b5b5050610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b600160a060020a0380831660009081526004602090815260408083209385168352929052908120548190118015610789575060008054600160a060020a03808616835260046020908152604080852092871685529190529091205442910111155b90505b92915050565b600860205260009081526040902054600160a060020a031681565b60035433600160a060020a03908116911614806107ce57506107ce3361100c565b5b156100bf5783856107e08282610b2a565b156100bf5785600160a060020a031663d7f31eb98686866040518463ffffffff1660e060020a0281526004018084600160a060020a0316600160a060020a0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156108655780820151818401525b60200161084c565b50505050905090810190601f1680156108925780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b15156108b257600080fd5b6102c65a03f115156108c357600080fd5b5050505b6108d1565b600080fd5b5b50506108de565b600080fd5b5b5050505050565b60035433600160a060020a039081169116148061090757506109073361100c565b5b156100bf57600160a060020a0380831660009081526005602052604090205483918591811690821614156100bf57600254600160a060020a038086166000908152600660209081526040808320938a16835292905220548591879142919091039010156100bf57600160a060020a0380831660009081526006602090815260408083208585168452825280832042905589841683526004825280832093891683529290529081205411156109bb57600080fd5b600160a060020a038087166000818152600460209081526040808320948a1680845294909152908190204290557f3047230d3e8ea09f306e55725064a524f79ccdf75f030deeb6db4f95518b6386908a9051600160a060020a03909116815260200160405180910390a35b6104f3565b600080fd5b5b5050610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b80600160a060020a038116156100bf57600160a060020a033381166000908152600560205260408120549091161115610a8857600080fd5b60008054600160a060020a0333811680845260046020908152604080862089851687528252808620429590950390945581855260059052928290208054918616600160a060020a031990921682179055919081907fc36800ebd6079fdafc3a7100d0d1172815751804a6d1b7eb365b85f6c9c80e6190879051600160a060020a03909116815260200160405180910390a45b61050d565b600080fd5b5b505050565b600160a060020a03808316600090815260046020908152604080832093851683529290529081205481901180156107895750600154600160a060020a0380851660009081526004602090815260408083209387168352929052205442910111155b90505b92915050565b60035460009033600160a060020a0390811691161480610bb85750610bb83361100c565b5b156100bf578183610bca8282610b2a565b156100bf57600160a060020a03808516600081815260086020818152604080842080546007845282862095909555929091528154600160a060020a0319169091559216945084917f788f980ac1598bf6282c4cc596fedd7807726e6e8e51f1505a1b6df5d726f56790889051600160a060020a03909116815260200160405180910390a35b610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b60035433600160a060020a0390811691161480610c8d5750610c8d3361100c565b5b156100bf578183610c9f8282610728565b156100bf57600254600160a060020a038086166000908152600660209081526040808320938a16835292905220548591879142919091039010156100bf57600160a060020a0380831660009081526006602090815260408083208585168452825280832042908190556001548b861680865260048552838620968c168087529690945293829020939003909255907f3047230d3e8ea09f306e55725064a524f79ccdf75f030deeb6db4f95518b6386908a9051600160a060020a03909116815260200160405180910390a35b6104f3565b600080fd5b5b5050610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b60035460009033600160a060020a0390811691161480610db95750610db93361100c565b5b156100bf578183610dcb8282610728565b156100bf57600160a060020a0384166000908152600760205260409020541580610e11575060008054600160a060020a0386168252600760205260409091205442910110155b15610e1b57600080fd5b600160a060020a03808516600081815260086020818152604080842080546007845282862095909555929091528154600160a060020a0319169091559216945090631a6952309085905160e060020a63ffffffff8416028152600160a060020a039091166004820152602401600060405180830381600087803b1515610ea057600080fd5b6102c65a03f11515610eb157600080fd5b50505082600160a060020a031684600160a060020a03167f5d50e5b65a3141f147362981bdcac9e274984e1c2efec57320f86b3e071dd6f887604051600160a060020a03909116815260200160405180910390a35b5b610500565b600080fd5b5b505061050d565b600080fd5b5b505050565b60076020526000908152604090205481565b600081600160a060020a038116156100bf57610f50611034565b604051809103906000f0801515610f6657600080fd5b60008054600160a060020a038381168084526004602090815260408086208b851687528252808620429590950390945581855260059052928290208054600160a060020a031916888316908117909155939550331691907fc36800ebd6079fdafc3a7100d0d1172815751804a6d1b7eb365b85f6c9c80e6190889051600160a060020a03909116815260200160405180910390a45b611005565b600080fd5b5b50505050565b6000602436101561101f5750600061102f565b50600435600160a060020a031681145b919050565b6040516103b88061104583390190560060606040525b60008054600160a060020a03191633600160a060020a03161790555b5b610387806100316000396000f3006060604052361561005f5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a69523081146100a05780632f54bf6e146100c15780638da5cb5b146100f4578063d7f31eb914610123575b5b33600160a060020a03167f88a5966d370b9919b20f3e2c13ff65706f196a4e32cc2c12bf57088f885258743460405190815260200160405180910390a25b005b34156100ab57600080fd5b61009e600160a060020a036004351661018a565b005b34156100cc57600080fd5b6100e0600160a060020a03600435166101e7565b604051901515815260200160405180910390f35b34156100ff57600080fd5b6101076101fe565b604051600160a060020a03909116815260200160405180910390f35b341561012e57600080fd5b61009e60048035600160a060020a03169060248035919060649060443590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061020d95505050505050565b005b610193336101e7565b151561019e57600080fd5b30600160a060020a031681600160a060020a03161415156101e2576000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0383161790555b5b5b50565b600054600160a060020a038281169116145b919050565b600054600160a060020a031681565b610216336101e7565b151561022157600080fd5b82600160a060020a0316828260405180828051906020019080838360005b838110156102585780820151818401525b60200161023f565b50505050905090810190601f1680156102855780820380516001836020036101000a031916815260200191505b5091505060006040518083038185876187965a03f19250505015156102a957600080fd5b82600160a060020a03167fc1de93dfa06362c6a616cde73ec17d116c0d588dd1df70f27f91b500de207c41838360405182815260406020820181815290820183818151815260200191508051906020019080838360005b838110156103195780820151818401525b602001610300565b50505050905090810190601f1680156103465780820380516001836020036101000a031916815260200191505b50935050505060405180910390a25b5b5050505600a165627a7a723058200ba2a8d383b244f198a81f7de550fe98261f9f629ad9e264d910c3373e4da8800029a165627a7a72305820e6f2264bd8329d514dd15e258694f2a842d3407957a8c099e371eac10fc513a90029";

    private MetaIdentityManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private MetaIdentityManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<IdentityCreatedEventResponse> getIdentityCreatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("IdentityCreated", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<IdentityCreatedEventResponse> responses = new ArrayList<IdentityCreatedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            IdentityCreatedEventResponse typedResponse = new IdentityCreatedEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.creator = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.recoveryKey = (Address) eventValues.getIndexedValues().get(2);
            typedResponse.owner = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IdentityCreatedEventResponse> identityCreatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("IdentityCreated", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, IdentityCreatedEventResponse>() {
            @Override
            public IdentityCreatedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                IdentityCreatedEventResponse typedResponse = new IdentityCreatedEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.creator = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.recoveryKey = (Address) eventValues.getIndexedValues().get(2);
                typedResponse.owner = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<OwnerAddedEventResponse> getOwnerAddedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnerAdded", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnerAddedEventResponse> responses = new ArrayList<OwnerAddedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnerAddedEventResponse typedResponse = new OwnerAddedEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.owner = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnerAddedEventResponse> ownerAddedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnerAdded", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnerAddedEventResponse>() {
            @Override
            public OwnerAddedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnerAddedEventResponse typedResponse = new OwnerAddedEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.owner = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<OwnerRemovedEventResponse> getOwnerRemovedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnerRemoved", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnerRemovedEventResponse> responses = new ArrayList<OwnerRemovedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnerRemovedEventResponse typedResponse = new OwnerRemovedEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.owner = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnerRemovedEventResponse> ownerRemovedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnerRemoved", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnerRemovedEventResponse>() {
            @Override
            public OwnerRemovedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnerRemovedEventResponse typedResponse = new OwnerRemovedEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.owner = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<RecoveryChangedEventResponse> getRecoveryChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("RecoveryChanged", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RecoveryChangedEventResponse> responses = new ArrayList<RecoveryChangedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RecoveryChangedEventResponse typedResponse = new RecoveryChangedEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.recoveryKey = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RecoveryChangedEventResponse> recoveryChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("RecoveryChanged", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RecoveryChangedEventResponse>() {
            @Override
            public RecoveryChangedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RecoveryChangedEventResponse typedResponse = new RecoveryChangedEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.recoveryKey = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<MigrationInitiatedEventResponse> getMigrationInitiatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MigrationInitiated", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<MigrationInitiatedEventResponse> responses = new ArrayList<MigrationInitiatedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            MigrationInitiatedEventResponse typedResponse = new MigrationInitiatedEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newIdManager = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MigrationInitiatedEventResponse> migrationInitiatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MigrationInitiated", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MigrationInitiatedEventResponse>() {
            @Override
            public MigrationInitiatedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                MigrationInitiatedEventResponse typedResponse = new MigrationInitiatedEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.newIdManager = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<MigrationCanceledEventResponse> getMigrationCanceledEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MigrationCanceled", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<MigrationCanceledEventResponse> responses = new ArrayList<MigrationCanceledEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            MigrationCanceledEventResponse typedResponse = new MigrationCanceledEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newIdManager = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MigrationCanceledEventResponse> migrationCanceledEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MigrationCanceled", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MigrationCanceledEventResponse>() {
            @Override
            public MigrationCanceledEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                MigrationCanceledEventResponse typedResponse = new MigrationCanceledEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.newIdManager = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<MigrationFinalizedEventResponse> getMigrationFinalizedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MigrationFinalized", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<MigrationFinalizedEventResponse> responses = new ArrayList<MigrationFinalizedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            MigrationFinalizedEventResponse typedResponse = new MigrationFinalizedEventResponse();
            typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newIdManager = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MigrationFinalizedEventResponse> migrationFinalizedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MigrationFinalized", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MigrationFinalizedEventResponse>() {
            @Override
            public MigrationFinalizedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                MigrationFinalizedEventResponse typedResponse = new MigrationFinalizedEventResponse();
                typedResponse.identity = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.newIdManager = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.instigator = (Address) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Future<Bool> isRecovery(Address identity, Address recoveryKey) {
        Function function = new Function("isRecovery", 
                Arrays.<Type>asList(identity, recoveryKey), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> removeOwner(Address sender, Address identity, Address owner) {
        Function function = new Function("removeOwner", Arrays.<Type>asList(sender, identity, owner), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> initiateMigration(Address sender, Address identity, Address newIdManager) {
        Function function = new Function("initiateMigration", Arrays.<Type>asList(sender, identity, newIdManager), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> changeRecovery(Address sender, Address identity, Address recoveryKey) {
        Function function = new Function("changeRecovery", Arrays.<Type>asList(sender, identity, recoveryKey), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Bool> isOlderOwner(Address identity, Address owner) {
        Function function = new Function("isOlderOwner", 
                Arrays.<Type>asList(identity, owner), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Address> migrationNewAddress(Address param0) {
        Function function = new Function("migrationNewAddress", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> forwardTo(Address sender, Address identity, Address destination, Uint256 value, DynamicBytes data) {
        Function function = new Function("forwardTo", Arrays.<Type>asList(sender, identity, destination, value, data), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> addOwnerFromRecovery(Address sender, Address identity, Address newOwner) {
        Function function = new Function("addOwnerFromRecovery", Arrays.<Type>asList(sender, identity, newOwner), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> registerIdentity(Address owner, Address recoveryKey) {
        Function function = new Function("registerIdentity", Arrays.<Type>asList(owner, recoveryKey), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Bool> isOwner(Address identity, Address owner) {
        Function function = new Function("isOwner", 
                Arrays.<Type>asList(identity, owner), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> cancelMigration(Address sender, Address identity) {
        Function function = new Function("cancelMigration", Arrays.<Type>asList(sender, identity), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> addOwner(Address sender, Address identity, Address newOwner) {
        Function function = new Function("addOwner", Arrays.<Type>asList(sender, identity, newOwner), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> finalizeMigration(Address sender, Address identity) {
        Function function = new Function("finalizeMigration", Arrays.<Type>asList(sender, identity), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> migrationInitiated(Address param0) {
        Function function = new Function("migrationInitiated", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> createIdentity(Address owner, Address recoveryKey) {
        Function function = new Function("createIdentity", Arrays.<Type>asList(owner, recoveryKey), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<MetaIdentityManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 _userTimeLock, Uint256 _adminTimeLock, Uint256 _adminRate, Address _relayAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_userTimeLock, _adminTimeLock, _adminRate, _relayAddress));
        return deployAsync(MetaIdentityManager.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Future<MetaIdentityManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 _userTimeLock, Uint256 _adminTimeLock, Uint256 _adminRate, Address _relayAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_userTimeLock, _adminTimeLock, _adminRate, _relayAddress));
        return deployAsync(MetaIdentityManager.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static MetaIdentityManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MetaIdentityManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static MetaIdentityManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MetaIdentityManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class IdentityCreatedEventResponse {
        public Address identity;

        public Address creator;

        public Address recoveryKey;

        public Address owner;
    }

    public static class OwnerAddedEventResponse {
        public Address identity;

        public Address owner;

        public Address instigator;
    }

    public static class OwnerRemovedEventResponse {
        public Address identity;

        public Address owner;

        public Address instigator;
    }

    public static class RecoveryChangedEventResponse {
        public Address identity;

        public Address recoveryKey;

        public Address instigator;
    }

    public static class MigrationInitiatedEventResponse {
        public Address identity;

        public Address newIdManager;

        public Address instigator;
    }

    public static class MigrationCanceledEventResponse {
        public Address identity;

        public Address newIdManager;

        public Address instigator;
    }

    public static class MigrationFinalizedEventResponse {
        public Address identity;

        public Address newIdManager;

        public Address instigator;
    }
}
