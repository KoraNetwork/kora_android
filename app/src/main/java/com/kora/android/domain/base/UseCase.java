package com.kora.android.domain.base;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public abstract class UseCase<T, O> {

    protected abstract O buildTask();

    public T execute(final T observableTaskSubscriber) {
        final O task = buildTask();
        if (task instanceof Single) {
            return (T) ((Single) task).subscribeWith((DisposableSingleObserver) observableTaskSubscriber);
        } else if (task instanceof Observable) {
            return (T) ((Observable) task).subscribeWith((DisposableObserver) observableTaskSubscriber);
        } else if (task instanceof Completable) {
            return (T) ((Completable) task).subscribeWith((CompletableObserver) observableTaskSubscriber);
        } else {
            throw new RuntimeException("Not allowed task type");
        }
    }
}
