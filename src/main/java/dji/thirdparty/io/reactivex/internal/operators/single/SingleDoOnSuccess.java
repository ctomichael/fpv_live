package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Consumer;

public final class SingleDoOnSuccess<T> extends Single<T> {
    final Consumer<? super T> onSuccess;
    final SingleSource<T> source;

    public SingleDoOnSuccess(SingleSource<T> source2, Consumer<? super T> onSuccess2) {
        this.source = source2;
        this.onSuccess = onSuccess2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleDoOnSuccess.AnonymousClass1 */

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }

            public void onSuccess(T value) {
                try {
                    SingleDoOnSuccess.this.onSuccess.accept(value);
                    s.onSuccess(value);
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
