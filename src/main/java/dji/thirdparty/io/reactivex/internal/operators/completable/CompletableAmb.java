package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CompletableAmb extends Completable {
    private final CompletableSource[] sources;
    private final Iterable<? extends CompletableSource> sourcesIterable;

    public CompletableAmb(CompletableSource[] sources2, Iterable<? extends CompletableSource> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    public void subscribeActual(CompletableObserver s) {
        int count;
        CompletableSource[] sources2 = this.sources;
        int count2 = 0;
        if (sources2 == null) {
            sources2 = new CompletableSource[8];
            try {
                Iterator i$ = this.sourcesIterable.iterator();
                while (true) {
                    try {
                        int count3 = count2;
                        if (!i$.hasNext()) {
                            count = count3;
                            break;
                        }
                        CompletableSource element = (CompletableSource) i$.next();
                        if (element == null) {
                            EmptyDisposable.error(new NullPointerException("One of the sources is null"), s);
                            int i = count3;
                            return;
                        }
                        if (count3 == sources2.length) {
                            CompletableSource[] b = new CompletableSource[((count3 >> 2) + count3)];
                            System.arraycopy(sources2, 0, b, 0, count3);
                            sources2 = b;
                        }
                        count2 = count3 + 1;
                        sources2[count3] = element;
                    } catch (Throwable th) {
                        e = th;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, s);
                        return;
                    }
                }
            } catch (Throwable th2) {
                e = th2;
            }
        } else {
            count = sources2.length;
        }
        final CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        final AtomicBoolean once = new AtomicBoolean();
        final CompletableObserver completableObserver = s;
        CompletableObserver inner = new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableAmb.AnonymousClass1 */

            public void onComplete() {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    completableObserver.onComplete();
                }
            }

            public void onError(Throwable e) {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    completableObserver.onError(e);
                    return;
                }
                RxJavaPlugins.onError(e);
            }

            public void onSubscribe(Disposable d) {
                set.add(d);
            }
        };
        for (int i2 = 0; i2 < count; i2++) {
            CompletableSource c = sources2[i2];
            if (set.isDisposed()) {
                return;
            }
            if (c == null) {
                NullPointerException npe = new NullPointerException("One of the sources is null");
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onError(npe);
                    return;
                }
                RxJavaPlugins.onError(npe);
                return;
            }
            c.subscribe(inner);
        }
        if (count == 0) {
            s.onComplete();
        }
    }
}
