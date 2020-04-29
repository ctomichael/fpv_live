package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiConsumer;

public final class SingleDoOnEvent<T> extends Single<T> {
    final BiConsumer<? super T, ? super Throwable> onEvent;
    final SingleSource<T> source;

    public SingleDoOnEvent(SingleSource<T> source2, BiConsumer<? super T, ? super Throwable> onEvent2) {
        this.source = source2;
        this.onEvent = onEvent2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnEvent.AnonymousClass1 */

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }

            public void onSuccess(T value) {
                try {
                    SingleDoOnEvent.this.onEvent.accept(value, null);
                    s.onSuccess(value);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    s.onError(ex);
                }
            }

            public void onError(Throwable e) {
                try {
                    SingleDoOnEvent.this.onEvent.accept(null, e);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    e = new CompositeException(e, ex);
                }
                s.onError(e);
            }
        });
    }
}
