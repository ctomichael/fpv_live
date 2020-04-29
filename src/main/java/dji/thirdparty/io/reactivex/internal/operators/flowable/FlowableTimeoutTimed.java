package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.subscribers.FullArbiterSubscriber;
import dji.thirdparty.io.reactivex.internal.subscriptions.FullArbiter;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeoutTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    static final Disposable NEW_TIMER = new Disposable() {
        /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimeoutTimed.AnonymousClass1 */

        public void dispose() {
        }

        public boolean isDisposed() {
            return true;
        }
    };
    final Publisher<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public FlowableTimeoutTimed(Publisher<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, Publisher<? extends T> other2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (this.other == null) {
            this.source.subscribe(new TimeoutTimedSubscriber(new SerializedSubscriber(s), this.timeout, this.unit, this.scheduler.createWorker()));
            return;
        }
        this.source.subscribe(new TimeoutTimedOtherSubscriber(s, this.timeout, this.unit, this.scheduler.createWorker(), this.other));
    }

    static final class TimeoutTimedOtherSubscriber<T> implements Subscriber<T>, Disposable {
        final Subscriber<? super T> actual;
        final FullArbiter<T> arbiter;
        volatile boolean done;
        volatile long index;
        final Publisher<? extends T> other;
        Subscription s;
        final long timeout;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;
        final Scheduler.Worker worker;

        TimeoutTimedOtherSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2, Publisher<? extends T> other2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.other = other2;
            this.arbiter = new FullArbiter<>(actual2, this, 8);
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                if (this.arbiter.setSubscription(s2)) {
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
            Disposable d = this.timer.get();
            if (d != null) {
                d.dispose();
            }
            if (this.timer.compareAndSet(d, FlowableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this.timer, this.worker.schedule(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimeoutTimed.TimeoutTimedOtherSubscriber.AnonymousClass1 */

                    public void run() {
                        if (idx == TimeoutTimedOtherSubscriber.this.index) {
                            TimeoutTimedOtherSubscriber.this.done = true;
                            TimeoutTimedOtherSubscriber.this.s.cancel();
                            DisposableHelper.dispose(TimeoutTimedOtherSubscriber.this.timer);
                            TimeoutTimedOtherSubscriber.this.subscribeNext();
                            TimeoutTimedOtherSubscriber.this.worker.dispose();
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeNext() {
            this.other.subscribe(new FullArbiterSubscriber(this.arbiter));
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.worker.dispose();
            DisposableHelper.dispose(this.timer);
            this.arbiter.onError(t, this.s);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.worker.dispose();
                DisposableHelper.dispose(this.timer);
                this.arbiter.onComplete(this.s);
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this.timer);
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }
    }

    static final class TimeoutTimedSubscriber<T> implements Subscriber<T>, Disposable, Subscription {
        final Subscriber<? super T> actual;
        volatile boolean done;
        volatile long index;
        Subscription s;
        final long timeout;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;
        final Scheduler.Worker worker;

        TimeoutTimedSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
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
            Disposable d = this.timer.get();
            if (d != null) {
                d.dispose();
            }
            if (this.timer.compareAndSet(d, FlowableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this.timer, this.worker.schedule(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableTimeoutTimed.TimeoutTimedSubscriber.AnonymousClass1 */

                    public void run() {
                        if (idx == TimeoutTimedSubscriber.this.index) {
                            TimeoutTimedSubscriber.this.done = true;
                            TimeoutTimedSubscriber.this.dispose();
                            TimeoutTimedSubscriber.this.actual.onError(new TimeoutException());
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
            DisposableHelper.dispose(this.timer);
            this.s.cancel();
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            dispose();
        }
    }
}
