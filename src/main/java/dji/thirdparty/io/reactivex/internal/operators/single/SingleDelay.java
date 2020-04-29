package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.TimeUnit;

public final class SingleDelay<T> extends Single<T> {
    final Scheduler scheduler;
    final SingleSource<? extends T> source;
    final long time;
    final TimeUnit unit;

    public SingleDelay(SingleSource<? extends T> source2, long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.source = source2;
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        final SequentialDisposable sd = new SequentialDisposable();
        s.onSubscribe(sd);
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleDelay.AnonymousClass1 */

            public void onSubscribe(Disposable d) {
                sd.replace(d);
            }

            public void onSuccess(final T value) {
                sd.replace(SingleDelay.this.scheduler.scheduleDirect(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleDelay.AnonymousClass1.AnonymousClass1 */

                    public void run() {
                        s.onSuccess(value);
                    }
                }, SingleDelay.this.time, SingleDelay.this.unit));
            }

            public void onError(final Throwable e) {
                sd.replace(SingleDelay.this.scheduler.scheduleDirect(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleDelay.AnonymousClass1.AnonymousClass2 */

                    public void run() {
                        s.onError(e);
                    }
                }, 0, SingleDelay.this.unit));
            }
        });
    }
}
