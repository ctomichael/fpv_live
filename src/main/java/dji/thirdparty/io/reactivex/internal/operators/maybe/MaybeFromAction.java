package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.Maybe;
import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class MaybeFromAction<T> extends Maybe<T> implements Callable<T> {
    final Action action;

    public MaybeFromAction(Action action2) {
        this.action = action2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        Disposable d = Disposables.empty();
        observer.onSubscribe(d);
        if (!d.isDisposed()) {
            try {
                this.action.run();
                if (!d.isDisposed()) {
                    observer.onComplete();
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                if (!d.isDisposed()) {
                    observer.onError(ex);
                } else {
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    public T call() throws Exception {
        this.action.run();
        return null;
    }
}
