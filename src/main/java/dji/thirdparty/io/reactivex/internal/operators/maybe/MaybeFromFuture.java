package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.Maybe;
import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class MaybeFromFuture<T> extends Maybe<T> {
    final Future<? extends T> future;
    final long timeout;
    final TimeUnit unit;

    public MaybeFromFuture(Future<? extends T> future2, long timeout2, TimeUnit unit2) {
        this.future = future2;
        this.timeout = timeout2;
        this.unit = unit2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        T v;
        Disposable d = Disposables.empty();
        observer.onSubscribe(d);
        if (!d.isDisposed()) {
            try {
                if (this.timeout <= 0) {
                    v = this.future.get();
                } else {
                    v = this.future.get(this.timeout, this.unit);
                }
                if (d.isDisposed()) {
                    return;
                }
                if (v == null) {
                    observer.onComplete();
                } else {
                    observer.onSuccess(v);
                }
            } catch (InterruptedException ex) {
                if (!d.isDisposed()) {
                    observer.onError(ex);
                }
            } catch (ExecutionException ex2) {
                if (!d.isDisposed()) {
                    observer.onError(ex2.getCause());
                }
            } catch (TimeoutException ex3) {
                if (!d.isDisposed()) {
                    observer.onError(ex3);
                }
            }
        }
    }
}
