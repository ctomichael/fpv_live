package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCache<T> extends AbstractObservableWithUpstream<T, T> implements Observer<T> {
    static final CacheDisposable[] EMPTY = new CacheDisposable[0];
    static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
    final int capacityHint;
    volatile boolean done;
    Throwable error;
    final Node<T> head;
    final AtomicReference<CacheDisposable<T>[]> observers;
    final AtomicBoolean once = new AtomicBoolean();
    volatile long size;
    Node<T> tail;
    int tailOffset;

    public ObservableCache(Observable<T> source, int capacityHint2) {
        super(source);
        this.capacityHint = capacityHint2;
        Node<T> n = new Node<>(capacityHint2);
        this.head = n;
        this.tail = n;
        this.observers = new AtomicReference<>(EMPTY);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> t) {
        CacheDisposable<T> consumer = new CacheDisposable<>(t, this);
        t.onSubscribe(consumer);
        add(consumer);
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            replay(consumer);
        } else {
            this.source.subscribe(this);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.once.get();
    }

    /* access modifiers changed from: package-private */
    public boolean hasObservers() {
        return ((CacheDisposable[]) this.observers.get()).length != 0;
    }

    /* access modifiers changed from: package-private */
    public long cachedEventCount() {
        return this.size;
    }

    /* access modifiers changed from: package-private */
    public void add(CacheDisposable<T> consumer) {
        CacheDisposable<T>[] current;
        CacheDisposable<T>[] next;
        do {
            current = (CacheDisposable[]) this.observers.get();
            if (current != TERMINATED) {
                int n = current.length;
                next = new CacheDisposable[(n + 1)];
                System.arraycopy(current, 0, next, 0, n);
                next[n] = consumer;
            } else {
                return;
            }
        } while (!this.observers.compareAndSet(current, next));
    }

    /* access modifiers changed from: package-private */
    public void remove(CacheDisposable<T> consumer) {
        CacheDisposable<T>[] current;
        CacheDisposable<T>[] next;
        do {
            current = (CacheDisposable[]) this.observers.get();
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
                    next = new CacheDisposable[(n - 1)];
                    System.arraycopy(current, 0, next, 0, j);
                    System.arraycopy(current, j + 1, next, j, (n - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.observers.compareAndSet(current, next));
    }

    /* access modifiers changed from: package-private */
    public void replay(CacheDisposable<T> consumer) {
        if (consumer.getAndIncrement() == 0) {
            int missed = 1;
            long index = consumer.index;
            int offset = consumer.offset;
            Node<T> node = consumer.node;
            Observer<? super T> downstream = consumer.downstream;
            int capacity = this.capacityHint;
            while (!consumer.disposed) {
                boolean sourceDone = this.done;
                boolean empty = this.size == index;
                if (sourceDone && empty) {
                    consumer.node = null;
                    Throwable ex = this.error;
                    if (ex != null) {
                        downstream.onError(ex);
                        return;
                    } else {
                        downstream.onComplete();
                        return;
                    }
                } else if (!empty) {
                    if (offset == capacity) {
                        node = node.next;
                        offset = 0;
                    }
                    downstream.onNext(node.values[offset]);
                    offset++;
                    index++;
                } else {
                    consumer.index = index;
                    consumer.offset = offset;
                    consumer.node = node;
                    missed = consumer.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
            consumer.node = null;
        }
    }

    public void onSubscribe(Disposable d) {
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
        for (CacheDisposable<T> consumer : (CacheDisposable[]) this.observers.get()) {
            replay(consumer);
        }
    }

    public void onError(Throwable t) {
        this.error = t;
        this.done = true;
        for (CacheDisposable<T> consumer : (CacheDisposable[]) this.observers.getAndSet(TERMINATED)) {
            replay(consumer);
        }
    }

    public void onComplete() {
        this.done = true;
        for (CacheDisposable<T> consumer : (CacheDisposable[]) this.observers.getAndSet(TERMINATED)) {
            replay(consumer);
        }
    }

    static final class CacheDisposable<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 6770240836423125754L;
        volatile boolean disposed;
        final Observer<? super T> downstream;
        long index;
        Node<T> node;
        int offset;
        final ObservableCache<T> parent;

        CacheDisposable(Observer<? super T> downstream2, ObservableCache<T> parent2) {
            this.downstream = downstream2;
            this.parent = parent2;
            this.node = parent2.head;
        }

        public void dispose() {
            if (!this.disposed) {
                this.disposed = true;
                this.parent.remove(this);
            }
        }

        public boolean isDisposed() {
            return this.disposed;
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
