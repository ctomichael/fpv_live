package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.CompositeException;
import dji.thirdparty.rx.functions.FuncN;
import dji.thirdparty.rx.internal.util.RxRingBuffer;
import dji.thirdparty.rx.internal.util.atomic.SpscLinkedArrayQueue;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;

public final class OnSubscribeCombineLatest<T, R> implements Observable.OnSubscribe<R> {
    final int bufferSize;
    final FuncN<? extends R> combiner;
    final boolean delayError;
    final Observable<? extends T>[] sources;
    final Iterable<? extends Observable<? extends T>> sourcesIterable;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OnSubscribeCombineLatest(Iterable<? extends Observable<? extends T>> sourcesIterable2, FuncN<? extends R> combiner2) {
        this(null, sourcesIterable2, combiner2, RxRingBuffer.SIZE, false);
    }

    public OnSubscribeCombineLatest(Observable<? extends T>[] sources2, Iterable<? extends Observable<? extends T>> sourcesIterable2, FuncN<? extends R> combiner2, int bufferSize2, boolean delayError2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
        this.combiner = combiner2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void call(Subscriber<? super R> s) {
        Observable<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 != null) {
            count = sources2.length;
        } else if (this.sourcesIterable instanceof List) {
            List list = (List) this.sourcesIterable;
            sources2 = (Observable[]) ((Observable[]) list.toArray(new Observable[list.size()]));
            count = sources2.length;
        } else {
            sources2 = new Observable[8];
            for (Observable<? extends T> p : this.sourcesIterable) {
                if (count == sources2.length) {
                    Observable<? extends T>[] b = new Observable[((count >> 2) + count)];
                    System.arraycopy(sources2, 0, b, 0, count);
                    sources2 = b;
                }
                sources2[count] = p;
                count++;
            }
        }
        if (count == 0) {
            s.onCompleted();
            return;
        }
        new LatestCoordinator<>(s, this.combiner, count, this.bufferSize, this.delayError).subscribe(sources2);
    }

    static final class LatestCoordinator<T, R> extends AtomicInteger implements Producer, Subscription {
        static final Object MISSING = new Object();
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final FuncN<? extends R> combiner;
        int complete;
        final int count;
        final boolean delayError;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Object[] latest;
        final SpscLinkedArrayQueue<Object> queue;
        final AtomicLong requested = new AtomicLong();
        final CombinerSubscriber<T, R>[] subscribers;

        public LatestCoordinator(Subscriber<? super R> actual2, FuncN<? extends R> combiner2, int count2, int bufferSize2, boolean delayError2) {
            this.actual = actual2;
            this.combiner = combiner2;
            this.count = count2;
            this.bufferSize = bufferSize2;
            this.delayError = delayError2;
            this.latest = new Object[count2];
            Arrays.fill(this.latest, MISSING);
            this.subscribers = new CombinerSubscriber[count2];
            this.queue = new SpscLinkedArrayQueue<>(bufferSize2);
        }

        public void subscribe(Observable<? extends T>[] sources) {
            Subscriber<T>[] as = this.subscribers;
            int len = as.length;
            for (int i = 0; i < len; i++) {
                as[i] = new CombinerSubscriber<>(this, i);
            }
            lazySet(0);
            this.actual.add(this);
            this.actual.setProducer(this);
            for (int i2 = 0; i2 < len && !this.cancelled; i2++) {
                sources[i2].subscribe((Subscriber) as[i2]);
            }
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= required but it was " + n);
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                drain();
            }
        }

        public void unsubscribe() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    cancel(this.queue);
                }
            }
        }

        public boolean isUnsubscribed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void cancel(Queue<?> q) {
            q.clear();
            for (CombinerSubscriber<T, R> s : this.subscribers) {
                s.unsubscribe();
            }
        }

        /* access modifiers changed from: package-private */
        public void combine(Object value, int index) {
            boolean allSourcesFinished;
            boolean empty = false;
            CombinerSubscriber<T, R> combinerSubscriber = this.subscribers[index];
            synchronized (this) {
                int sourceCount = this.latest.length;
                Object o = this.latest[index];
                int activeCount = this.active;
                if (o == MISSING) {
                    activeCount++;
                    this.active = activeCount;
                }
                int completedCount = this.complete;
                if (value == null) {
                    completedCount++;
                    this.complete = completedCount;
                } else {
                    this.latest[index] = combinerSubscriber.nl.getValue(value);
                }
                if (activeCount == sourceCount) {
                    allSourcesFinished = true;
                } else {
                    allSourcesFinished = false;
                }
                if (completedCount == sourceCount || (value == null && o == MISSING)) {
                    empty = true;
                }
                if (empty) {
                    this.done = true;
                } else if (value != null && allSourcesFinished) {
                    this.queue.offer(combinerSubscriber, this.latest.clone());
                } else if (value == null) {
                    if (this.error.get() != null && (o == MISSING || !this.delayError)) {
                        this.done = true;
                    }
                }
            }
            if (allSourcesFinished || value == null) {
                drain();
            } else {
                combinerSubscriber.requestMore(1);
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                Queue<Object> q = this.queue;
                Subscriber<? super R> a = this.actual;
                boolean delayError2 = this.delayError;
                AtomicLong localRequested = this.requested;
                int missed = 1;
                do {
                    if (!checkTerminated(this.done, q.isEmpty(), a, q, delayError2)) {
                        long requestAmount = localRequested.get();
                        boolean unbounded = requestAmount == LongCompanionObject.MAX_VALUE;
                        long emitted = 0;
                        while (requestAmount != 0) {
                            boolean d = this.done;
                            CombinerSubscriber<T, R> cs = (CombinerSubscriber) q.peek();
                            boolean empty = cs == null;
                            if (checkTerminated(d, empty, a, q, delayError2)) {
                                return;
                            }
                            if (empty) {
                                break;
                            }
                            q.poll();
                            Object[] array = (Object[]) q.poll();
                            if (array == null) {
                                this.cancelled = true;
                                cancel(q);
                                a.onError(new IllegalStateException("Broken queue?! Sender received but not the array."));
                                return;
                            }
                            try {
                                a.onNext(this.combiner.call(array));
                                cs.requestMore(1);
                                requestAmount--;
                                emitted--;
                            } catch (Throwable ex) {
                                this.cancelled = true;
                                cancel(q);
                                a.onError(ex);
                                return;
                            }
                        }
                        if (emitted != 0 && !unbounded) {
                            localRequested.addAndGet(emitted);
                        }
                        missed = addAndGet(-missed);
                    } else {
                        return;
                    }
                } while (missed != 0);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean mainDone, boolean queueEmpty, Subscriber<?> childSubscriber, Queue<?> q, boolean delayError2) {
            if (this.cancelled) {
                cancel(q);
                return true;
            }
            if (mainDone) {
                if (!delayError2) {
                    Throwable e = this.error.get();
                    if (e != null) {
                        cancel(q);
                        childSubscriber.onError(e);
                        return true;
                    } else if (queueEmpty) {
                        childSubscriber.onCompleted();
                        return true;
                    }
                } else if (queueEmpty) {
                    Throwable e2 = this.error.get();
                    if (e2 != null) {
                        childSubscriber.onError(e2);
                        return true;
                    }
                    childSubscriber.onCompleted();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void onError(Throwable e) {
            Throwable curr;
            Throwable next;
            AtomicReference<Throwable> localError = this.error;
            do {
                curr = localError.get();
                if (curr == null) {
                    next = e;
                } else if (curr instanceof CompositeException) {
                    List<Throwable> es = new ArrayList<>(((CompositeException) curr).getExceptions());
                    es.add(e);
                    next = new CompositeException(es);
                } else {
                    next = new CompositeException(Arrays.asList(curr, e));
                }
            } while (!localError.compareAndSet(curr, next));
        }
    }

    static final class CombinerSubscriber<T, R> extends Subscriber<T> {
        boolean done;
        final int index;
        final NotificationLite<T> nl = NotificationLite.instance();
        final LatestCoordinator<T, R> parent;

        public CombinerSubscriber(LatestCoordinator<T, R> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
            request((long) parent2.bufferSize);
        }

        public void onNext(T t) {
            if (!this.done) {
                this.parent.combine(this.nl.next(t), this.index);
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                return;
            }
            this.parent.onError(t);
            this.done = true;
            this.parent.combine(null, this.index);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.parent.combine(null, this.index);
            }
        }

        public void requestMore(long n) {
            request(n);
        }
    }
}
