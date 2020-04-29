package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.SequentialDisposable;

public final class CompletableResumeNext extends Completable {
    final Function<? super Throwable, ? extends CompletableSource> errorMapper;
    final CompletableSource source;

    public CompletableResumeNext(CompletableSource source2, Function<? super Throwable, ? extends CompletableSource> errorMapper2) {
        this.source = source2;
        this.errorMapper = errorMapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver s) {
        final SequentialDisposable sd = new SequentialDisposable();
        s.onSubscribe(sd);
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableResumeNext.AnonymousClass1 */

            public void onComplete() {
                s.onComplete();
            }

            public void onError(Throwable e) {
                try {
                    CompletableSource c = (CompletableSource) CompletableResumeNext.this.errorMapper.apply(e);
                    if (c == null) {
                        NullPointerException npe = new NullPointerException("The CompletableConsumable returned is null");
                        npe.initCause(e);
                        s.onError(npe);
                        return;
                    }
                    c.subscribe(new CompletableObserver() {
                        /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableResumeNext.AnonymousClass1.AnonymousClass1 */

                        public void onComplete() {
                            s.onComplete();
                        }

                        public void onError(Throwable e) {
                            s.onError(e);
                        }

                        public void onSubscribe(Disposable d) {
                            sd.update(d);
                        }
                    });
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    s.onError(new CompositeException(ex, e));
                }
            }

            public void onSubscribe(Disposable d) {
                sd.update(d);
            }
        });
    }
}
