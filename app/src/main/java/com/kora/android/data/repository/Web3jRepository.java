package com.kora.android.data.repository;

import android.net.Uri;
import android.support.v4.util.Pair;

import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.presentation.model.UserEntity;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public interface Web3jRepository {

    Observable<IdentityCreatedResponse> createWallets(final String pinCode);

    Observable<String> getBalance(final String proxyAddress,
                                  final String smartContractAddress);

    Observable<List<String>> increaseBalance(final UserEntity userEntity,
                                             final double amount);

    Observable<File> getWalletFile(final String walletAddress);

    Observable<Object> exportWalletFile(final File walletFile);

    Observable<Object> importWalletFile(final Uri walletFileUri);

    Observable<Object> importKoraWalletFile();

    Observable<List<String>> createRawTransaction(final UserEntity receiver,
                                                  final double senderAmount,
                                                  final double receiverAmount,
                                                  final Integer interestRate,
                                                  final String pinCode);

    Observable<String> createCreateLoan(final UserEntity lender,
                                        final List<UserEntity> guarantors,
                                        final double borrowerAmount,
                                        final double lenderAmount,
                                        final int rate,
                                        final Date startDate,
                                        final Date maturityDate,
                                        final String pinCode);

    Observable<String> createAgreeLoan(final String loanId,
                                       final String pinCode);

    Observable<Pair<List<String>, String>> createFundLoan(final String borrowerErc20Token,
                                                          final String lenderErc20Token,
                                                          final double borrowerAmount,
                                                          final double lenderAmount,
                                                          final String loanId,
                                                          final String pinCode);

    Observable<Pair<List<String>, String>> createPayBackLoan(final String loanId,
                                                             final String borrowerErc20Token,
                                                             final String lenderErc20Token,
                                                             final double borrowerValue,
                                                             final double borrowerBalance,
                                                             final double lenderBalance,
                                                             final String pinCode);
}
