package com.kora.android.domain.base;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class AsyncUseCase<T, O> extends UseCase<T, O> {

    @Override
    public T execute(final T observableTaskSubscriber) {
        final O task;
        if ((task = buildTask()) == null)
            throw new RuntimeException("Error request not declared");
        if (task instanceof Single) {
            return (T) wrapToAsync((Single) task, (DisposableSingleObserver) observableTaskSubscriber);
        } else if (task instanceof Observable) {
            return (T) wrapToAsync((Observable) task, (DisposableObserver) observableTaskSubscriber);
        } else if (task instanceof Completable) {
            return (T) wrapToAsync((Completable) task, (DisposableCompletableObserver) observableTaskSubscriber);
        } else {
            throw new RuntimeException("Not allowed task type");
        }
    }

    private <T extends Single, O extends DisposableSingleObserver> O wrapToAsync(final T task,
                                                                                 final O observer) {
        return (O) task.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }

    private <T extends Observable, O extends DisposableObserver> O wrapToAsync(final T task,
                                                                               final O observer) {
        return (O) task.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }

    private <T extends Completable, O extends DisposableCompletableObserver> O wrapToAsync(final T task,
                                                                                           final O observer) {
        return task.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }
}