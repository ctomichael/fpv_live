package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableIntervalRange extends Observable<Long> {
    final long end;
    final long initialDelay;
    final long period;
    final Scheduler scheduler;
    final long start;
    final TimeUnit unit;

    public ObservableIntervalRange(long start2, long end2, long initialDelay2, long period2, TimeUnit unit2, Scheduler scheduler2) {
        this.initialDelay = initialDelay2;
        this.period = period2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.start = start2;
        this.end = end2;
    }

    public void subscribeActual(Observer<? super Long> s) {
        IntervalRangeObserver is = new IntervalRangeObserver(s, this.start, this.end);
        s.onSubscribe(is);
        is.setResource(this.scheduler.schedulePeriodicallyDirect(is, this.initialDelay, this.period, this.unit));
    }

    static final class IntervalRangeObserver extends AtomicReference<Disposable> implements Disposable, Runnable {
        private static final long serialVersionUID = 1891866368734007884L;
        final Observer<? super Long> actual;
        long count;
        final long end;

        IntervalRangeObserver(Observer<? super Long> actual2, long start, long end2) {
            this.actual = actual2;
            this.count = start;
            this.end = end2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void run() {
            if (!isDisposed()) {
                long c = this.count;
                this.actual.onNext(Long.valueOf(c));
                if (c == this.end) {
                    DisposableHelper.dispose(this);
                    this.actual.onComplete();
                    return;
                }
                this.count = 1 + c;
            }
        }

        public void setResource(Disposable d) {
            DisposableHelper.setOnce(this, d);
        }
    }
}
