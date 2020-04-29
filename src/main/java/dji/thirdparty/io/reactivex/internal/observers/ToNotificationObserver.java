package dji.thirdparty.io.reactivex.internal.observers;

import dji.thirdparty.io.reactivex.Notification;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;

public final class ToNotificationObserver<T> implements Observer<T> {
    final Consumer<? super Notification<Object>> consumer;
    Disposable s;

    public ToNotificationObserver(Consumer<? super Notification<Object>> consumer2) {
        this.consumer = consumer2;
    }

    public void onSubscribe(Disposable s2) {
        if (DisposableHelper.validate(this.s, s2)) {
            this.s = s2;
        }
    }

    public void onNext(T t) {
        if (t == null) {
            this.s.dispose();
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return;
        }
        try {
            this.consumer.accept(Notification.createOnNext(t));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            this.s.dispose();
            onError(ex);
        }
    }

    public void onError(Throwable t) {
        try {
            this.consumer.accept(Notification.createOnError(t));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(new CompositeException(t, ex));
        }
    }

    public void onComplete() {
        try {
            this.consumer.accept(Notification.createOnComplete());
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
        }
    }
}
