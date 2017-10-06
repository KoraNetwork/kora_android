package com.kora.android.presentation.ui.main;

import android.util.Log;

import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.GetCountriesUseCase;
import com.kora.android.domain.usecase.test.TestUseCase;
import com.kora.android.domain.usecase.transaction.SendTransactionUseCase;
import com.kora.android.domain.usecase.test.ExportWalletUseCase;
import com.kora.android.domain.usecase.test.GenerateWalletUseCase;
import com.kora.android.domain.usecase.test.GetWalletListUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.registration.step1.FirstStepPresenter;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainView> {

    private final GenerateWalletUseCase mGenerateWalletUseCase;
    private final GetWalletListUseCase mGetWalletListUseCase;
    private final ExportWalletUseCase mExportWalletUseCase;
    private final SendTransactionUseCase mSendTransactionUseCase;
    private final TestUseCase mTestUseCase;
    private final GetCountriesUseCase mGetCountriesUseCase;

    @Inject
    public MainPresenter(final GenerateWalletUseCase generateWalletUseCase,
                         final GetWalletListUseCase getWalletListUseCase,
                         final ExportWalletUseCase exportWalletUseCase,
                         final SendTransactionUseCase sendTransactionUseCase,
                         final TestUseCase testUseCase,
                         final GetCountriesUseCase getCountriesUseCase) {
        mGenerateWalletUseCase = generateWalletUseCase;
        mGetWalletListUseCase = getWalletListUseCase;
        mExportWalletUseCase = exportWalletUseCase;
        mSendTransactionUseCase = sendTransactionUseCase;
        mTestUseCase = testUseCase;
        mGetCountriesUseCase = getCountriesUseCase;
    }

    public void startGetCountriesTask() {
        addDisposable(mGetCountriesUseCase.execute(new GetCountriesObserver()));
    }

    public void generateWallet(final String password, final String privateKey) {
        mGenerateWalletUseCase.setData(password, privateKey);
        addDisposable(mGenerateWalletUseCase.execute(new GenerateWalletObserver()));
    }

    public void getWalletList() {
        addDisposable(mGetWalletListUseCase.execute(new GetWalletListObserver()));
    }

    public void exportWallet(String walletFileNmae) {
        mExportWalletUseCase.setData(walletFileNmae);
        addDisposable(mExportWalletUseCase.execute(new ExportWalletObserver()));
    }

    public void sendTransaction(final String walletFileName,
                                final String password,
                                final String addressFrom,
                                final String addressTo,
                                final BigInteger amount) {
        mSendTransactionUseCase.setData(
                walletFileName,
                password,
                addressFrom,
                addressTo,
                amount);
        addDisposable(mSendTransactionUseCase.execute(new SendTransactionObserver()));
    }

    public void createIdentity() {
        addDisposable(mTestUseCase.execute(new CreateIdentityObserver()));
    }

    private class GetWalletListObserver extends DefaultSingleObserver<List<EtherWallet>> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull List<EtherWallet> etherWalletList) {
            if (etherWalletList == null)
                Log.e("_____", "NULL");
            else if (etherWalletList.isEmpty())
                Log.e("_____", "EMPTY");
            else if (!etherWalletList.isEmpty()) {
                for (int i = 0; i < etherWalletList.size(); i++) {
                    Log.e("_____", etherWalletList.get(i).toString());
                }
            }

            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.e("_____", e.toString());
            e.printStackTrace();

            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }
    }

    private class GenerateWalletObserver extends DefaultSingleObserver<EtherWallet> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull EtherWallet etherWallet) {
            Log.e("_____", etherWallet.toString());

            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.e("_____", e.toString());
            e.printStackTrace();

            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }
    }

    private class ExportWalletObserver extends DefaultCompletableObserver {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onComplete() {
            Log.e("_____", "EXPORTED");

            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.e("_____", e.toString());
            e.printStackTrace();

            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }
    }

    private class SendTransactionObserver extends DefaultSingleObserver<String> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull String transactionHash) {
            Log.e("_____", transactionHash);

            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            Log.e("_____", retrofitException.getMessage());
            retrofitException.printStackTrace();
        }

        @Override
        public void handleUnexpectedError(RetrofitException exception) {
            Log.e("_____", exception.getMessage());
            super.handleUnexpectedError(exception);
        }
    }

    private class CreateIdentityObserver extends DefaultSingleObserver<String> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull String transactionHash) {
            Log.e("_____", transactionHash);

            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            Log.e("_____", retrofitException.getMessage());
            retrofitException.printStackTrace();
        }

        @Override
        public void handleUnexpectedError(RetrofitException exception) {
            Log.e("_____", exception.getMessage());
            super.handleUnexpectedError(exception);
        }
    }

    private Action mGetPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mGetCountriesUseCase.execute(new GetCountriesObserver()));
        }
    };

    private class GetCountriesObserver extends DefaultSingleObserver<List<Country>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull final List<Country> countryList) {
            if (!isViewAttached()) return;
            getView().showProgress(false);

            for (int i = 0; i < countryList.size(); i++)
                Log.e("_____", countryList.get(i).toString());
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mGetPhoneNumberAction));
        }
    }
}
