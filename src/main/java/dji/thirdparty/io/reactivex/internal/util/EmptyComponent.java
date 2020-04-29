package dji.thirdparty.io.reactivex.internal.util;

import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public enum EmptyComponent implements Subscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable {
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
