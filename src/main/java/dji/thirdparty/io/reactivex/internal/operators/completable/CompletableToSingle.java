package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import java.util.concurrent.Callable;

public final class CompletableToSingle<T> extends Single<T> {
    final T completionValue;
    final Callable<? extends T> completionValueSupplier;
    final CompletableSource source;

    public CompletableToSingle(CompletableSource source2, Callable<? extends T> completionValueSupplier2, T completionValue2) {
        this.source = source2;
        this.completionValue = completionValue2;
        this.completionValueSupplier = completionValueSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableToSingle.AnonymousClass1 */

            public void onComplete() {
                T v;
                if (CompletableToSingle.this.completionValueSupplier != null) {
                    try {
                        v = CompletableToSingle.this.completionValueSupplier.call();
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        s.onError(e);
                        return;
                    }
                } else {
                    v = CompletableToSingle.this.completionValue;
                }
                if (v == null) {
                    s.onError(new NullPointerException("The value supplied is null"));
                } else {
                    s.onSuccess(v);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
            }

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }
        });
    }
}
