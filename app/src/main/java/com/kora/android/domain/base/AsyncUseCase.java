package com.kora.android.domain.base;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class AsyncUseCase extends UseCase {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void execute(DisposableObserver observableTaskSubscriber) {
        final Observable requestObservable;
        if ((requestObservable = buildObservableTask()) == null)
            throw new RuntimeException("Error observable request not declared");
        final Observable observable = requestObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        addDisposable((Disposable) observable.subscribeWith(observableTaskSubscriber));

    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
            disposables.clear();
        }
    }

    protected void addDisposable(Disposable disposable) {
        Preconditions.checkNotNull(disposable);
        Preconditions.checkNotNull(disposables);
        disposables.add(disposable);
    }
}