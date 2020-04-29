package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public final class CompletableDelay extends Completable {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final CompletableSource source;
    final TimeUnit unit;

    public CompletableDelay(CompletableSource source2, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        this.source = source2;
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver s) {
        final CompositeDisposable set = new CompositeDisposable();
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDelay.AnonymousClass1 */

            public void onComplete() {
                set.add(CompletableDelay.this.scheduler.scheduleDirect(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDelay.AnonymousClass1.AnonymousClass1 */

                    public void run() {
                        s.onComplete();
                    }
                }, CompletableDelay.this.delay, CompletableDelay.this.unit));
            }

            public void onError(final Throwable e) {
                set.add(CompletableDelay.this.scheduler.scheduleDirect(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletableDelay.AnonymousClass1.AnonymousClass2 */

                    public void run() {
                        s.onError(e);
                    }
                }, CompletableDelay.this.delayError ? CompletableDelay.this.delay : 0, CompletableDelay.this.unit));
            }

            public void onSubscribe(Disposable d) {
                set.add(d);
                s.onSubscribe(set);
            }
        });
    }
}
