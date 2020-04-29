package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;

public final class SingleOnErrorReturn<T> extends Single<T> {
    final SingleSource<? extends T> source;
    final T value;
    final Function<? super Throwable, ? extends T> valueSupplier;

    public SingleOnErrorReturn(SingleSource<? extends T> source2, Function<? super Throwable, ? extends T> valueSupplier2, T value2) {
        this.source = source2;
        this.valueSupplier = valueSupplier2;
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleOnErrorReturn.AnonymousClass1 */

            public void onError(Throwable e) {
                T v;
                if (SingleOnErrorReturn.this.valueSupplier != null) {
                    try {
                        v = SingleOnErrorReturn.this.valueSupplier.apply(e);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        s.onError(new CompositeException(e, ex));
                        return;
                    }
                } else {
                    v = SingleOnErrorReturn.this.value;
                }
                if (v == null) {
                    NullPointerException npe = new NullPointerException("Value supplied was null");
                    npe.initCause(e);
                    s.onError(npe);
                    return;
                }
                s.onSuccess(v);
            }

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }

            public void onSuccess(T value) {
                s.onSuccess(value);
            }
        });
    }
}
