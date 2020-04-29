package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Consumer;

public final class SingleDoOnError<T> extends Single<T> {
    final Consumer<? super Throwable> onError;
    final SingleSource<T> source;

    public SingleDoOnError(SingleSource<T> source2, Consumer<? super Throwable> onError2) {
        this.source = source2;
        this.onError = onError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnError.AnonymousClass1 */

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }

            public void onSuccess(T value) {
                s.onSuccess(value);
            }

            public void onError(Throwable e) {
                try {
                    SingleDoOnError.this.onError.accept(e);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    e = new CompositeException(e, ex);
                }
                s.onError(e);
            }
        });
    }
}
