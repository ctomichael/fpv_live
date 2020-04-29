package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;

public final class SingleMap<T, R> extends Single<R> {
    final Function<? super T, ? extends R> mapper;
    final SingleSource<? extends T> source;

    public SingleMap(SingleSource<? extends T> source2, Function<? super T, ? extends R> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super R> t) {
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleMap.AnonymousClass1 */

            public void onSubscribe(Disposable d) {
                t.onSubscribe(d);
            }

            public void onSuccess(T value) {
                try {
                    t.onSuccess(SingleMap.this.mapper.apply(value));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    onError(e);
                }
            }

            public void onError(Throwable e) {
                t.onError(e);
            }
        });
    }
}
