package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.TimeUnit;

public final class SingleDelay<T> extends Single<T> {
    final boolean delayError;
    final Scheduler scheduler;
    final SingleSource<? extends T> source;
    final long time;
    final TimeUnit unit;

    public SingleDelay(SingleSource<? extends T> source2, long time2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        this.source = source2;
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        SequentialDisposable sd = new SequentialDisposable();
        observer.onSubscribe(sd);
        this.source.subscribe(new Delay(sd, observer));
    }

    final class Delay implements SingleObserver<T> {
        final SingleObserver<? super T> downstream;
        private final SequentialDisposable sd;

        Delay(SequentialDisposable sd2, SingleObserver<? super T> observer) {
            this.sd = sd2;
            this.downstream = observer;
        }

        public void onSubscribe(Disposable d) {
            this.sd.replace(d);
        }

        public void onSuccess(T value) {
            this.sd.replace(SingleDelay.this.scheduler.scheduleDirect(new OnSuccess(value), SingleDelay.this.time, SingleDelay.this.unit));
        }

        public void onError(Throwable e) {
            this.sd.replace(SingleDelay.this.scheduler.scheduleDirect(new OnError(e), SingleDelay.this.delayError ? SingleDelay.this.time : 0, SingleDelay.this.unit));
        }

        final class OnSuccess implements Runnable {
            private final T value;

            OnSuccess(T value2) {
                this.value = value2;
            }

            public void run() {
                Delay.this.downstream.onSuccess(this.value);
            }
        }

        final class OnError implements Runnable {
            private final Throwable e;

            OnError(Throwable e2) {
                this.e = e2;
            }

            public void run() {
                Delay.this.downstream.onError(this.e);
            }
        }
    }
}
