package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CompletableTimeout extends Completable {
    final CompletableSource other;
    final Scheduler scheduler;
    final CompletableSource source;
    final long timeout;
    final TimeUnit unit;

    public CompletableTimeout(CompletableSource source2, long timeout2, TimeUnit unit2, Scheduler scheduler2, CompletableSource other2) {
        this.source = source2;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    public void subscribeActual(final CompletableObserver s) {
        final CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        final AtomicBoolean once = new AtomicBoolean();
        set.add(this.scheduler.scheduleDirect(new Runnable() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableTimeout.AnonymousClass1 */

            public void run() {
                if (once.compareAndSet(false, true)) {
                    set.clear();
                    if (CompletableTimeout.this.other == null) {
                        s.onError(new TimeoutException());
                    } else {
                        CompletableTimeout.this.other.subscribe(new CompletableObserver() {
                            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableTimeout.AnonymousClass1.AnonymousClass1 */

                            public void onSubscribe(Disposable d) {
                                set.add(d);
                            }

                            public void onError(Throwable e) {
                                set.dispose();
                                s.onError(e);
                            }

                            public void onComplete() {
                                set.dispose();
                                s.onComplete();
                            }
                        });
                    }
                }
            }
        }, this.timeout, this.unit));
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableTimeout.AnonymousClass2 */

            public void onSubscribe(Disposable d) {
                set.add(d);
            }

            public void onError(Throwable e) {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onError(e);
                    return;
                }
                RxJavaPlugins.onError(e);
            }

            public void onComplete() {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onComplete();
                }
            }
        });
    }
}
