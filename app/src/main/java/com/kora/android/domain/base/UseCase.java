package com.kora.android.domain.base;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public abstract class UseCase<T extends DisposableObserver> {

    protected abstract Observable buildObservableTask();

    public void execute(final T observableTaskSubscriber) {
        buildObservableTask().subscribe(observableTaskSubscriber);
    }
}
