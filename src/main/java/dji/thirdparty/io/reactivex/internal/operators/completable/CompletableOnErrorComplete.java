package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Predicate;

public final class CompletableOnErrorComplete extends Completable {
    final Predicate<? super Throwable> predicate;
    final CompletableSource source;

    public CompletableOnErrorComplete(CompletableSource source2, Predicate<? super Throwable> predicate2) {
        this.source = source2;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver s) {
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableOnErrorComplete.AnonymousClass1 */

            public void onComplete() {
                s.onComplete();
            }

            public void onError(Throwable e) {
                try {
                    if (CompletableOnErrorComplete.this.predicate.test(e)) {
                        s.onComplete();
                    } else {
                        s.onError(e);
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    s.onError(new CompositeException(e, ex));
                }
            }

            public void onSubscribe(Disposable d) {
                s.onSubscribe(d);
            }
        });
    }
}
