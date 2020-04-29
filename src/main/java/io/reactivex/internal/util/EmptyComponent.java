package io.reactivex.internal.util;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public enum EmptyComponent implements FlowableSubscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable {
    INSTANCE;

    public static <T> Subscriber<T> asSubscriber() {
        return INSTANCE;
    }

    public static <T> Observer<T> asObserver() {
        return INSTANCE;
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return true;
    }

    public void request(long n) {
    }

    public void cancel() {
    }

    public void onSubscribe(Disposable d) {
        d.dispose();
    }

    public void onSubscribe(Subscription s) {
        s.cancel();
    }

    public void onNext(Object t) {
    }

    public void onError(Throwable t) {
        RxJavaPlugins.onError(t);
    }

    public void onComplete() {
    }

    public void onSuccess(Object value) {
    }
}
