package com.kora.android.presentation.ui.main;

import android.util.Log;

import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.wallet.GenerateWalletUseCase;
import com.kora.android.domain.usecase.wallet.GetWalletListUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainView> {

    private final GenerateWalletUseCase mGenerateWalletUseCase;
    private final GetWalletListUseCase mGetWalletListUseCase;

    @Inject
    public MainPresenter(final GenerateWalletUseCase generateWalletUseCase,
                         final GetWalletListUseCase getWalletListUseCase) {
        mGenerateWalletUseCase = generateWalletUseCase;
        mGetWalletListUseCase = getWalletListUseCase;
    }

    public void generateWallet(final String password, final String privateKey) {
        mGenerateWalletUseCase.setData(password, privateKey);
        addDisposable(mGenerateWalletUseCase.execute(new GenerateWalletObserver()));
    }

    public void getWalletList() {
        mGetWalletListUseCase.execute(new GetWalletListSubscriber());
    }

    private class GetWalletListSubscriber extends DefaultSingleObserver<List<EtherWallet>> {
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
}
