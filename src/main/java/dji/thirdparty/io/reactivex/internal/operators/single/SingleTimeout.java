package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SingleTimeout<T> extends Single<T> {
    final SingleSource<? extends T> other;
    final Scheduler scheduler;
    final SingleSource<T> source;
    final long timeout;
    final TimeUnit unit;

    public SingleTimeout(SingleSource<T> source2, long timeout2, TimeUnit unit2, Scheduler scheduler2, SingleSource<? extends T> other2) {
        this.source = source2;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> s) {
        final CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        final AtomicBoolean once = new AtomicBoolean();
        set.add(this.scheduler.scheduleDirect(new Runnable() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleTimeout.AnonymousClass1 */

            public void run() {
                if (!once.compareAndSet(false, true)) {
                    return;
                }
                if (SingleTimeout.this.other != null) {
                    set.clear();
                    SingleTimeout.this.other.subscribe(new SingleObserver<T>() {
                        /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleTimeout.AnonymousClass1.AnonymousClass1 */

                        public void onError(Throwable e) {
                            set.dispose();
                            s.onError(e);
                        }

                        public void onSubscribe(Disposable d) {
                            set.add(d);
                        }

                        public void onSuccess(T value) {
                            set.dispose();
                            s.onSuccess(value);
                        }
                    });
                    return;
                }
                set.dispose();
                s.onError(new TimeoutException());
            }
        }, this.timeout, this.unit));
        this.source.subscribe(new SingleObserver<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleTimeout.AnonymousClass2 */

            public void onError(Throwable e) {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onError(e);
                }
            }

            public void onSubscribe(Disposable d) {
                set.add(d);
            }

            public void onSuccess(T value) {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onSuccess(value);
                }
            }
        });
    }
}
