package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiPredicate;

public final class SingleContains<T> extends Single<Boolean> {
    final BiPredicate<Object, Object> comparer;
    final SingleSource<T> source;
    final Object value;

    public SingleContains(SingleSource<T> source2, Object value2, BiPredicate<Object, Object> comparer2) {
        this.source = source2;
        this.value = value2;
        this.comparer = comparer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super Boolean> s) {
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleContains.AnonymousClass1 */

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }

            public void onSuccess(T v) {
                try {
                    s.onSuccess(Boolean.valueOf(SingleContains.this.comparer.test(v, SingleContains.this.value)));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    s.onError(ex);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
            }
        });
    }
}
