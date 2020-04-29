package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCache<T> extends AbstractFlowableWithUpstream<T, T> implements FlowableSubscriber<T> {
    static final CacheSubscription[] EMPTY = new CacheSubscription[0];
    static final CacheSubscription[] TERMINATED = new CacheSubscription[0];
    final int capacityHint;
    volatile boolean done;
    Throwable error;
    final Node<T> head;
    final AtomicBoolean once = new AtomicBoolean();
    volatile long size;
    final AtomicReference<CacheSubscription<T>[]> subscribers;
    Node<T> tail;
    int tailOffset;

    public FlowableCache(Flowable<T> source, int capacityHint2) {
        super(source);
        this.capacityHint = capacityHint2;
        Node<T> n = new Node<>(capacityHint2);
        this.head = n;
        this.tail = n;
        this.subscribers = new AtomicReference<>(EMPTY);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> t) {
        CacheSubscription<T> consumer = new CacheSubscription<>(t, this);
        t.onSubscribe(consumer);
        add(consumer);
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            replay(consumer);
        } else {
            this.source.subscribe((FlowableSubscriber) this);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.once.get();
    }

    /* access modifiers changed from: package-private */
    public boolean hasSubscribers() {
        return ((CacheSubscription[]) this.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: package-private */
    public long cachedEventCount() {
        return this.size;
    }

    /* access modifiers changed from: package-private */
    public void add(CacheSubscription<T> consumer) {
        CacheSubscription<T>[] current;
        CacheSubscription<T>[] next;
        do {
            current = (CacheSubscription[]) this.subscribers.get();
            if (current != TERMINATED) {
                int n = current.length;
                next = new CacheSubscription[(n + 1)];
                System.arraycopy(current, 0, next, 0, n);
                next[n] = consumer;
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(current, next));
    }

    /* access modifiers changed from: package-private */
    public void remove(CacheSubscription<T> consumer) {
        CacheSubscription<T>[] current;
        CacheSubscription<T>[] next;
        do {
            current = (CacheSubscription[]) this.subscribers.get();
            int n = current.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (current[i] == consumer) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (n == 1) {
                    next = EMPTY;
                } else {
                    next = new CacheSubscription[(n - 1)];
                    System.arraycopy(current, 0, next, 0, j);
                    System.arraycopy(current, j + 1, next, j, (n - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(current, next));
    }

    /* access modifiers changed from: package-private */
    public void replay(CacheSubscription<T> consumer) {
        if (consumer.getAndIncrement() == 0) {
            int missed = 1;
            long index = consumer.index;
            int offset = consumer.offset;
            Node<T> node = consumer.node;
            AtomicLong requested = consumer.requested;
            Subscriber<? super T> downstream = consumer.downstream;
            int capacity = this.capacityHint;
            while (true) {
                boolean sourceDone = this.done;
                boolean empty = this.size == index;
                if (!sourceDone || !empty) {
                    if (!empty) {
                        long consumerRequested = requested.get();
                        if (consumerRequested == Long.MIN_VALUE) {
                            consumer.node = null;
                            return;
                        } else if (consumerRequested != index) {
                            if (offset == capacity) {
                                node = node.next;
                                offset = 0;
                            }
                            downstream.onNext(node.values[offset]);
                            offset++;
                            index++;
                        }
                    }
                    consumer.index = index;
                    consumer.offset = offset;
                    consumer.node = node;
                    missed = consumer.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    consumer.node = null;
                    Throwable ex = this.error;
                    if (ex != null) {
                        downstream.onError(ex);
                        return;
                    } else {
                        downstream.onComplete();
                        return;
                    }
                }
            }
        }
    }

    public void onSubscribe(Subscription s) {
        s.request(LongCompanionObject.MAX_VALUE);
    }

    public void onNext(T t) {
        int tailOffset2 = this.tailOffset;
        if (tailOffset2 == this.capacityHint) {
            Node<T> n = new Node<>(tailOffset2);
            n.values[0] = t;
            this.tailOffset = 1;
            this.tail.next = n;
            this.tail = n;
        } else {
            this.tail.values[tailOffset2] = t;
            this.tailOffset = tailOffset2 + 1;
        }
        this.size++;
        for (CacheSubscription<T> consumer : (CacheSubscription[]) this.subscribers.get()) {
            replay(consumer);
        }
    }

    public void onError(Throwable t) {
        if (this.done) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.error = t;
        this.done = true;
        for (CacheSubscription<T> consumer : (CacheSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            replay(consumer);
        }
    }

    public void onComplete() {
        this.done = true;
        for (CacheSubscription<T> consumer : (CacheSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            replay(consumer);
        }
    }

    static final class CacheSubscription<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 6770240836423125754L;
        final Subscriber<? super T> downstream;
        long index;
        Node<T> node;
        int offset;
        final FlowableCache<T> parent;
        final AtomicLong requested = new AtomicLong();

        CacheSubscription(Subscriber<? super T> downstream2, FlowableCache<T> parent2) {
            this.downstream = downstream2;
            this.parent = parent2;
            this.node = parent2.head;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this.requested, n);
                this.parent.replay(this);
            }
        }

        public void cancel() {
            if (this.requested.getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
            }
        }
    }

    static final class Node<T> {
        volatile Node<T> next;
        final T[] values;

        Node(int capacityHint) {
            this.values = (Object[]) new Object[capacityHint];
        }
    }
}
