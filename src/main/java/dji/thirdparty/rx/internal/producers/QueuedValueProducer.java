package dji.thirdparty.rx.internal.producers;

import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.internal.operators.BackpressureUtils;
import dji.thirdparty.rx.internal.util.atomic.SpscLinkedAtomicQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscLinkedQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class QueuedValueProducer<T> extends AtomicLong implements Producer {
    static final Object NULL_SENTINEL = new Object();
    private static final long serialVersionUID = 7277121710709137047L;
    final Subscriber<? super T> child;
    final Queue<Object> queue;
    final AtomicInteger wip;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public QueuedValueProducer(Subscriber<? super T> child2) {
        this(child2, UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue());
    }

    public QueuedValueProducer(Subscriber<? super T> child2, Queue<Object> queue2) {
        this.child = child2;
        this.queue = queue2;
        this.wip = new AtomicInteger();
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (n > 0) {
            BackpressureUtils.getAndAddRequest(this, n);
            drain();
        }
    }

    public boolean offer(T value) {
        if (value == null) {
            if (!this.queue.offer(NULL_SENTINEL)) {
                return false;
            }
        } else if (!this.queue.offer(value)) {
            return false;
        }
        drain();
        return true;
    }

    private void drain() {
        T v;
        if (this.wip.getAndIncrement() == 0) {
            Subscriber<? super T> c = this.child;
            Queue<Object> q = this.queue;
            while (!c.isUnsubscribed()) {
                this.wip.lazySet(1);
                long r = get();
                long e = 0;
                while (r != 0 && (v = q.poll()) != null) {
                    try {
                        if (v == NULL_SENTINEL) {
                            c.onNext(null);
                        } else {
                            c.onNext(v);
                        }
                        if (!c.isUnsubscribed()) {
                            r--;
                            e++;
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        if (v == NULL_SENTINEL) {
                            v = null;
                        }
                        Exceptions.throwOrReport(ex, c, v);
                        return;
                    }
                }
                if (!(e == 0 || get() == LongCompanionObject.MAX_VALUE)) {
                    addAndGet(-e);
                }
                if (this.wip.decrementAndGet() == 0) {
                    return;
                }
            }
        }
    }
}
