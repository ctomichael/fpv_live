package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.internal.util.RxRingBuffer;
import dji.thirdparty.rx.internal.util.atomic.SpscAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.schedulers.ImmediateScheduler;
import dji.thirdparty.rx.schedulers.TrampolineScheduler;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public final class OperatorObserveOn<T> implements Observable.Operator<T, T> {
    private final int bufferSize;
    private final boolean delayError;
    private final Scheduler scheduler;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorObserveOn(Scheduler scheduler2, boolean delayError2) {
        this(scheduler2, delayError2, RxRingBuffer.SIZE);
    }

    public OperatorObserveOn(Scheduler scheduler2, boolean delayError2, int bufferSize2) {
        this.scheduler = scheduler2;
        this.delayError = delayError2;
        this.bufferSize = bufferSize2 <= 0 ? RxRingBuffer.SIZE : bufferSize2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        if ((this.scheduler instanceof ImmediateScheduler) || (this.scheduler instanceof TrampolineScheduler)) {
            return child;
        }
        ObserveOnSubscriber<T> parent = new ObserveOnSubscriber<>(this.scheduler, child, this.delayError, this.bufferSize);
        parent.init();
        return parent;
    }

    private static final class ObserveOnSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;
        final AtomicLong counter = new AtomicLong();
        final boolean delayError;
        long emitted;
        Throwable error;
        volatile boolean finished;
        final int limit;
        final NotificationLite<T> on;
        final Queue<Object> queue;
        final Scheduler.Worker recursiveScheduler;
        final AtomicLong requested = new AtomicLong();

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> child2, boolean delayError2, int bufferSize) {
            this.child = child2;
            this.recursiveScheduler = scheduler.createWorker();
            this.delayError = delayError2;
            this.on = NotificationLite.instance();
            int calculatedSize = bufferSize > 0 ? bufferSize : RxRingBuffer.SIZE;
            this.limit = calculatedSize - (calculatedSize >> 2);
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(calculatedSize);
            } else {
                this.queue = new SpscAtomicArrayQueue(calculatedSize);
            }
            request((long) calculatedSize);
        }

        /* access modifiers changed from: package-private */
        public void init() {
            Subscriber<? super T> localChild = this.child;
            localChild.setProducer(new Producer() {
                /* class dji.thirdparty.rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.AnonymousClass1 */

                public void request(long n) {
                    if (n > 0) {
                        BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.this.requested, n);
                        ObserveOnSubscriber.this.schedule();
                    }
                }
            });
            localChild.add(this.recursiveScheduler);
            localChild.add(this);
        }

        public void onNext(T t) {
            if (!isUnsubscribed() && !this.finished) {
                if (!this.queue.offer(this.on.next(t))) {
                    onError(new MissingBackpressureException());
                } else {
                    schedule();
                }
            }
        }

        public void onCompleted() {
            if (!isUnsubscribed() && !this.finished) {
                this.finished = true;
                schedule();
            }
        }

        public void onError(Throwable e) {
            if (isUnsubscribed() || this.finished) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                return;
            }
            this.error = e;
            this.finished = true;
            schedule();
        }

        /* access modifiers changed from: protected */
        public void schedule() {
            if (this.counter.getAndIncrement() == 0) {
                this.recursiveScheduler.schedule(this);
            }
        }

        public void call() {
            long missed = 1;
            long currentEmission = this.emitted;
            Queue<Object> q = this.queue;
            Subscriber<? super T> localChild = this.child;
            NotificationLite<T> localOn = this.on;
            do {
                long requestAmount = this.requested.get();
                while (requestAmount != currentEmission) {
                    boolean done = this.finished;
                    Object v = q.poll();
                    boolean empty = v == null;
                    if (!checkTerminated(done, empty, localChild, q)) {
                        if (empty) {
                            break;
                        }
                        localChild.onNext(localOn.getValue(v));
                        currentEmission++;
                        if (currentEmission == ((long) this.limit)) {
                            requestAmount = BackpressureUtils.produced(this.requested, currentEmission);
                            request(currentEmission);
                            currentEmission = 0;
                        }
                    } else {
                        return;
                    }
                }
                if (requestAmount == currentEmission) {
                    if (checkTerminated(this.finished, q.isEmpty(), localChild, q)) {
                        return;
                    }
                }
                this.emitted = currentEmission;
                missed = this.counter.addAndGet(-missed);
            } while (missed != 0);
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean done, boolean isEmpty, Subscriber<? super T> a, Queue<Object> q) {
            if (a.isUnsubscribed()) {
                q.clear();
                return true;
            }
            if (done) {
                if (!this.delayError) {
                    Throwable e = this.error;
                    if (e != null) {
                        q.clear();
                        try {
                            a.onError(e);
                            return true;
                        } finally {
                            this.recursiveScheduler.unsubscribe();
                        }
                    } else if (isEmpty) {
                        try {
                            a.onCompleted();
                            return true;
                        } finally {
                            this.recursiveScheduler.unsubscribe();
                        }
                    }
                } else if (isEmpty) {
                    Throwable e2 = this.error;
                    if (e2 != null) {
                        try {
                            a.onError(e2);
                        } catch (Throwable th) {
                            this.recursiveScheduler.unsubscribe();
                            throw th;
                        }
                    } else {
                        a.onCompleted();
                    }
                    this.recursiveScheduler.unsubscribe();
                }
            }
            return false;
        }
    }
}
