package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.disposables.ObserverFullArbiter;
import dji.thirdparty.io.reactivex.internal.observers.FullArbiterObserver;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimeoutTimed<T> extends AbstractObservableWithUpstream<T, T> {
    static final Disposable NEW_TIMER = new Disposable() {
        /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimeoutTimed.AnonymousClass1 */

        public void dispose() {
        }

        public boolean isDisposed() {
            return true;
        }
    };
    final ObservableSource<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public ObservableTimeoutTimed(ObservableSource<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, ObservableSource<? extends T> other2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> t) {
        if (this.other == null) {
            this.source.subscribe(new TimeoutTimedObserver(new SerializedObserver(t), this.timeout, this.unit, this.scheduler.createWorker()));
            return;
        }
        this.source.subscribe(new TimeoutTimedOtherObserver(t, this.timeout, this.unit, this.scheduler.createWorker(), this.other));
    }

    static final class TimeoutTimedOtherObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -4619702551964128179L;
        final Observer<? super T> actual;
        final ObserverFullArbiter<T> arbiter;
        volatile boolean done;
        volatile long index;
        final ObservableSource<? extends T> other;
        Disposable s;
        final long timeout;
        final TimeUnit unit;
        final Scheduler.Worker worker;

        TimeoutTimedOtherObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2, ObservableSource<? extends T> other2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.other = other2;
            this.arbiter = new ObserverFullArbiter<>(actual2, this, 8);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                if (this.arbiter.setDisposable(s2)) {
                    this.actual.onSubscribe(this.arbiter);
                    scheduleTimeout(0);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                if (this.arbiter.onNext(t, this.s)) {
                    scheduleTimeout(idx);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void scheduleTimeout(final long idx) {
            Disposable d = (Disposable) get();
            if (d != null) {
                d.dispose();
            }
            if (compareAndSet(d, ObservableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this, this.worker.schedule(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimeoutTimed.TimeoutTimedOtherObserver.AnonymousClass1 */

                    public void run() {
                        if (idx == TimeoutTimedOtherObserver.this.index) {
                            TimeoutTimedOtherObserver.this.done = true;
                            TimeoutTimedOtherObserver.this.s.dispose();
                            DisposableHelper.dispose(TimeoutTimedOtherObserver.this);
                            TimeoutTimedOtherObserver.this.subscribeNext();
                            TimeoutTimedOtherObserver.this.worker.dispose();
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeNext() {
            this.other.subscribe(new FullArbiterObserver(this.arbiter));
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.worker.dispose();
            DisposableHelper.dispose(this);
            this.arbiter.onError(t, this.s);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.worker.dispose();
                DisposableHelper.dispose(this);
                this.arbiter.onComplete(this.s);
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    static final class TimeoutTimedObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -8387234228317808253L;
        final Observer<? super T> actual;
        volatile boolean done;
        volatile long index;
        Disposable s;
        final long timeout;
        final TimeUnit unit;
        final Scheduler.Worker worker;

        TimeoutTimedObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                scheduleTimeout(0);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                this.actual.onNext(t);
                scheduleTimeout(idx);
            }
        }

        /* access modifiers changed from: package-private */
        public void scheduleTimeout(final long idx) {
            Disposable d = (Disposable) get();
            if (d != null) {
                d.dispose();
            }
            if (compareAndSet(d, ObservableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this, this.worker.schedule(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTimeoutTimed.TimeoutTimedObserver.AnonymousClass1 */

                    public void run() {
                        if (idx == TimeoutTimedObserver.this.index) {
                            TimeoutTimedObserver.this.done = true;
                            DisposableHelper.dispose(TimeoutTimedObserver.this);
                            TimeoutTimedObserver.this.s.dispose();
                            TimeoutTimedObserver.this.actual.onError(new TimeoutException());
                            TimeoutTimedObserver.this.worker.dispose();
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            dispose();
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                dispose();
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this);
            this.s.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }
}
