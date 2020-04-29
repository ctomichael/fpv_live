package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrain;
import io.reactivex.internal.util.QueueDrainHelper;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;

public abstract class QueueDrainSubscriber<T, U, V> extends QueueDrainSubscriberPad4 implements FlowableSubscriber<T>, QueueDrain<U, V> {
    /* access modifiers changed from: protected */
    public volatile boolean cancelled;
    protected volatile boolean done;
    protected final Subscriber<? super V> downstream;
    protected Throwable error;
    /* access modifiers changed from: protected */
    public final SimplePlainQueue<U> queue;

    public QueueDrainSubscriber(Subscriber<? super V> actual, SimplePlainQueue<U> queue2) {
        this.downstream = actual;
        this.queue = queue2;
    }

    public final boolean cancelled() {
        return this.cancelled;
    }

    public final boolean done() {
        return this.done;
    }

    public final boolean enter() {
        return this.wip.getAndIncrement() == 0;
    }

    public final boolean fastEnter() {
        return this.wip.get() == 0 && this.wip.compareAndSet(0, 1);
    }

    /* access modifiers changed from: protected */
    public final void fastPathEmitMax(U value, boolean delayError, Disposable dispose) {
        Subscriber<? super V> s = this.downstream;
        SimplePlainQueue<U> q = this.queue;
        if (fastEnter()) {
            long r = this.requested.get();
            if (r != 0) {
                if (accept(s, value) && r != LongCompanionObject.MAX_VALUE) {
                    produced(1);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                dispose.dispose();
                s.onError(new MissingBackpressureException("Could not emit buffer due to lack of requests"));
                return;
            }
        } else {
            q.offer(value);
            if (!enter()) {
                return;
            }
        }
        QueueDrainHelper.drainMaxLoop(q, s, delayError, dispose, this);
    }

    /* access modifiers changed from: protected */
    public final void fastPathOrderedEmitMax(U value, boolean delayError, Disposable dispose) {
        Subscriber<? super V> s = this.downstream;
        SimplePlainQueue<U> q = this.queue;
        if (fastEnter()) {
            long r = this.requested.get();
            if (r == 0) {
                this.cancelled = true;
                dispose.dispose();
                s.onError(new MissingBackpressureException("Could not emit buffer due to lack of requests"));
                return;
            } else if (q.isEmpty()) {
                if (accept(s, value) && r != LongCompanionObject.MAX_VALUE) {
                    produced(1);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                q.offer(value);
            }
        } else {
            q.offer(value);
            if (!enter()) {
                return;
            }
        }
        QueueDrainHelper.drainMaxLoop(q, s, delayError, dispose, this);
    }

    public boolean accept(Subscriber subscriber, Object obj) {
        return false;
    }

    public final Throwable error() {
        return this.error;
    }

    public final int leave(int m) {
        return this.wip.addAndGet(m);
    }

    public final long requested() {
        return this.requested.get();
    }

    public final long produced(long n) {
        return this.requested.addAndGet(-n);
    }

    public final void requested(long n) {
        if (SubscriptionHelper.validate(n)) {
            BackpressureHelper.add(this.requested, n);
        }
    }
}
