package com.kora.android.presentation.ui.main;

import android.util.Log;

import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.registration.GetCountriesUseCase;
import com.kora.android.domain.usecase.test.ExportWalletUseCase;
import com.kora.android.domain.usecase.test.GenerateWalletUseCase;
import com.kora.android.domain.usecase.test.GetWalletListUseCase;
import com.kora.android.domain.usecase.test.TestUseCase;
import com.kora.android.domain.usecase.transaction.SendTransactionUseCase;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

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
        mGetCountriesUseCase.execute(new GetCountriesObserver());
    }

    public void generateWallet(final String password, final String privateKey) {
        mGenerateWalletUseCase.setData(password, privateKey);
        mGenerateWalletUseCase.execute(new GenerateWalletObserver());
    }

    public void getWalletList() {
        mGetWalletListUseCase.execute(new GetWalletListObserver());
    }

    public void exportWallet(String walletFileNmae) {
        mExportWalletUseCase.setData(walletFileNmae);
        mExportWalletUseCase.execute(new ExportWalletObserver());
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
        mSendTransactionUseCase.execute(new SendTransactionObserver());
    }

    public void createIdentity() {
        mTestUseCase.execute(new CreateIdentityObserver());
    }

    private class GetWalletListObserver extends DefaultInternetSubscriber<List<EtherWallet>> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull List<EtherWallet> etherWalletList) {
            if (etherWalletList == null)
                Log.e("_____", "NULL");
            else if (etherWalletList.isEmpty())
                Log.e("_____", "EMPTY");
            else if (!etherWalletList.isEmpty()) {
                for (int i = 0; i < etherWalletList.size(); i++) {
                    Log.e("_____", etherWalletList.get(i).toString());
                }
            }

        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
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

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }
    }

    private class GenerateWalletObserver extends DefaultInternetSubscriber<EtherWallet> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull EtherWallet etherWallet) {
            Log.e("_____", etherWallet.toString());
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }
    }

    private class ExportWalletObserver extends DefaultInternetSubscriber {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onComplete() {
            Log.e("_____", "EXPORTED");
            if (!isViewAttached()) return;
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

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }
    }

    private class SendTransactionObserver extends DefaultInternetSubscriber<String> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull String transactionHash) {
            Log.e("_____", transactionHash);
        }

        @Override
        public void onComplete() {
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
        public void handleUnexpectedError(RetrofitException exception) {
            Log.e("_____", exception.getMessage());
            super.handleUnexpectedError(exception);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }
    }

    private class CreateIdentityObserver extends DefaultInternetSubscriber<String> {
        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull String transactionHash) {
            Log.e("_____", transactionHash);
        }

        @Override
        public void onComplete() {
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
        public void handleUnexpectedError(RetrofitException exception) {
            Log.e("_____", exception.getMessage());
            super.handleUnexpectedError(exception);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }
    }

    private Action mGetPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            mGetCountriesUseCase.execute(new GetCountriesObserver());
        }
    };

    private class GetCountriesObserver extends DefaultInternetSubscriber<List<Country>> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final List<Country> countryList) {
            for (int i = 0; i < countryList.size(); i++)
                Log.e("_____", countryList.get(i).toString());
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }
    }

    @Override
    public void onDetachView() {
        mGenerateWalletUseCase.dispose();
        mGetWalletListUseCase.dispose();
        mExportWalletUseCase.dispose();
        mSendTransactionUseCase.dispose();
        mTestUseCase.dispose();
        mGetCountriesUseCase.dispose();
    }
}
