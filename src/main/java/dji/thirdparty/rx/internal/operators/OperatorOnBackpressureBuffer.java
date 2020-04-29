package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.BackpressureOverflow;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.internal.util.BackpressureDrainManager;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public class OperatorOnBackpressureBuffer<T> implements Observable.Operator<T, T> {
    private final Long capacity;
    private final Action0 onOverflow;
    private final BackpressureOverflow.Strategy overflowStrategy;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    private static class Holder {
        static final OperatorOnBackpressureBuffer<?> INSTANCE = new OperatorOnBackpressureBuffer<>();

        private Holder() {
        }
    }

    public static <T> OperatorOnBackpressureBuffer<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorOnBackpressureBuffer() {
        this.capacity = null;
        this.onOverflow = null;
        this.overflowStrategy = BackpressureOverflow.ON_OVERFLOW_DEFAULT;
    }

    public OperatorOnBackpressureBuffer(long capacity2) {
        this(capacity2, null, BackpressureOverflow.ON_OVERFLOW_DEFAULT);
    }

    public OperatorOnBackpressureBuffer(long capacity2, Action0 onOverflow2) {
        this(capacity2, onOverflow2, BackpressureOverflow.ON_OVERFLOW_DEFAULT);
    }

    public OperatorOnBackpressureBuffer(long capacity2, Action0 onOverflow2, BackpressureOverflow.Strategy overflowStrategy2) {
        if (capacity2 <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be > 0");
        } else if (overflowStrategy2 == null) {
            throw new NullPointerException("The BackpressureOverflow strategy must not be null");
        } else {
            this.capacity = Long.valueOf(capacity2);
            this.onOverflow = onOverflow2;
            this.overflowStrategy = overflowStrategy2;
        }
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        BufferSubscriber<T> parent = new BufferSubscriber<>(child, this.capacity, this.onOverflow, this.overflowStrategy);
        child.add(parent);
        child.setProducer(parent.manager());
        return parent;
    }

    private static final class BufferSubscriber<T> extends Subscriber<T> implements BackpressureDrainManager.BackpressureQueueCallback {
        private final Long baseCapacity;
        private final AtomicLong capacity;
        private final Subscriber<? super T> child;
        private final BackpressureDrainManager manager;
        private final NotificationLite<T> on = NotificationLite.instance();
        private final Action0 onOverflow;
        private final BackpressureOverflow.Strategy overflowStrategy;
        private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
        private final AtomicBoolean saturated = new AtomicBoolean(false);

        public BufferSubscriber(Subscriber<? super T> child2, Long capacity2, Action0 onOverflow2, BackpressureOverflow.Strategy overflowStrategy2) {
            this.child = child2;
            this.baseCapacity = capacity2;
            this.capacity = capacity2 != null ? new AtomicLong(capacity2.longValue()) : null;
            this.onOverflow = onOverflow2;
            this.manager = new BackpressureDrainManager(this);
            this.overflowStrategy = overflowStrategy2;
        }

        public void onStart() {
            request(LongCompanionObject.MAX_VALUE);
        }

        public void onCompleted() {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain();
            }
        }

        public void onError(Throwable e) {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain(e);
            }
        }

        public void onNext(T t) {
            if (assertCapacity()) {
                this.queue.offer(this.on.next(t));
                this.manager.drain();
            }
        }

        public boolean accept(Object value) {
            return this.on.accept(this.child, value);
        }

        public void complete(Throwable exception) {
            if (exception != null) {
                this.child.onError(exception);
            } else {
                this.child.onCompleted();
            }
        }

        public Object peek() {
            return this.queue.peek();
        }

        public Object poll() {
            Object value = this.queue.poll();
            if (!(this.capacity == null || value == null)) {
                this.capacity.incrementAndGet();
            }
            return value;
        }

        private boolean assertCapacity() {
            long currCapacity;
            if (this.capacity == null) {
                return true;
            }
            do {
                currCapacity = this.capacity.get();
                if (currCapacity <= 0) {
                    boolean hasCapacity = false;
                    try {
                        hasCapacity = this.overflowStrategy.mayAttemptDrop() && poll() != null;
                    } catch (MissingBackpressureException e) {
                        if (this.saturated.compareAndSet(false, true)) {
                            unsubscribe();
                            this.child.onError(e);
                        }
                    }
                    if (this.onOverflow != null) {
                        try {
                            this.onOverflow.call();
                        } catch (Throwable e2) {
                            Exceptions.throwIfFatal(e2);
                            this.manager.terminateAndDrain(e2);
                            return false;
                        }
                    }
                    if (!hasCapacity) {
                        return false;
                    }
                }
            } while (!this.capacity.compareAndSet(currCapacity, currCapacity - 1));
            return true;
        }

        /* access modifiers changed from: protected */
        public Producer manager() {
            return this.manager;
        }
    }
}
