package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Consumer;

public final class CompletableDoOnEvent extends Completable {
    final Consumer<? super Throwable> onEvent;
    final CompletableSource source;

    public CompletableDoOnEvent(CompletableSource source2, Consumer<? super Throwable> onEvent2) {
        this.source = source2;
        this.onEvent = onEvent2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver s) {
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDoOnEvent.AnonymousClass1 */

            public void onComplete() {
                try {
                    CompletableDoOnEvent.this.onEvent.accept(null);
                    s.onComplete();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    s.onError(e);
                }
            }

            public void onError(Throwable e) {
                try {
                    CompletableDoOnEvent.this.onEvent.accept(e);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    e = new CompositeException(e, ex);
                }
                s.onError(e);
            }

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }
        });
    }
}
