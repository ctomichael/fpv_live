package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelSortedJoin<T> extends Flowable<T> {
    final Comparator<? super T> comparator;
    final ParallelFlowable<List<T>> source;

    public ParallelSortedJoin(ParallelFlowable<List<T>> source2, Comparator<? super T> comparator2) {
        this.source = source2;
        this.comparator = comparator2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        SortedJoinSubscription<T> parent = new SortedJoinSubscription<>(s, this.source.parallelism(), this.comparator);
        s.onSubscribe(parent);
        this.source.subscribe(parent.subscribers);
    }

    static final class SortedJoinSubscription<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 3481980673745556697L;
        volatile boolean cancelled;
        final Comparator<? super T> comparator;
        final Subscriber<? super T> downstream;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final int[] indexes;
        final List<T>[] lists;
        final AtomicInteger remaining = new AtomicInteger();
        final AtomicLong requested = new AtomicLong();
        final SortedJoinInnerSubscriber<T>[] subscribers;

        SortedJoinSubscription(Subscriber<? super T> actual, int n, Comparator<? super T> comparator2) {
            this.downstream = actual;
            this.comparator = comparator2;
            SortedJoinInnerSubscriber<T>[] s = new SortedJoinInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                s[i] = new SortedJoinInnerSubscriber<>(this, i);
            }
            this.subscribers = s;
            this.lists = new List[n];
            this.indexes = new int[n];
            this.remaining.lazySet(n);
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                if (this.remaining.get() == 0) {
                    drain();
                }
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    Arrays.fill(this.lists, (Object) null);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void cancelAll() {
            for (SortedJoinInnerSubscriber<T> s : this.subscribers) {
                s.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext(List<T> value, int index) {
            this.lists[index] = value;
            if (this.remaining.decrementAndGet() == 0) {
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable e) {
            if (this.error.compareAndSet(null, e)) {
                drain();
            } else if (e != this.error.get()) {
                RxJavaPlugins.onError(e);
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            boolean smaller;
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = this.downstream;
                List<T>[] lists2 = this.lists;
                int[] indexes2 = this.indexes;
                int n = indexes2.length;
                while (true) {
                    long r = this.requested.get();
                    long e = 0;
                    while (e != r) {
                        if (this.cancelled) {
                            Arrays.fill(lists2, (Object) null);
                            return;
                        }
                        Throwable ex = this.error.get();
                        if (ex != null) {
                            cancelAll();
                            Arrays.fill(lists2, (Object) null);
                            a.onError(ex);
                            return;
                        }
                        T min = null;
                        int minIndex = -1;
                        for (int i = 0; i < n; i++) {
                            List<T> list = lists2[i];
                            int index = indexes2[i];
                            if (list.size() != index) {
                                if (min == null) {
                                    min = list.get(index);
                                    minIndex = i;
                                } else {
                                    T b = list.get(index);
                                    try {
                                        if (this.comparator.compare(min, b) > 0) {
                                            smaller = true;
                                        } else {
                                            smaller = false;
                                        }
                                        if (smaller) {
                                            min = b;
                                            minIndex = i;
                                        }
                                    } catch (Throwable exc) {
                                        Exceptions.throwIfFatal(exc);
                                        cancelAll();
                                        Arrays.fill(lists2, (Object) null);
                                        if (!this.error.compareAndSet(null, exc)) {
                                            RxJavaPlugins.onError(exc);
                                        }
                                        a.onError(this.error.get());
                                        return;
                                    }
                                }
                            }
                        }
                        if (min == null) {
                            Arrays.fill(lists2, (Object) null);
                            a.onComplete();
                            return;
                        }
                        a.onNext(min);
                        indexes2[minIndex] = indexes2[minIndex] + 1;
                        e++;
                    }
                    if (e == r) {
                        if (this.cancelled) {
                            Arrays.fill(lists2, (Object) null);
                            return;
                        }
                        Throwable ex2 = this.error.get();
                        if (ex2 != null) {
                            cancelAll();
                            Arrays.fill(lists2, (Object) null);
                            a.onError(ex2);
                            return;
                        }
                        boolean empty = true;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= n) {
                                break;
                            }
                            if (indexes2[i2] != lists2[i2].size()) {
                                empty = false;
                                break;
                            }
                            i2++;
                        }
                        if (empty) {
                            Arrays.fill(lists2, (Object) null);
                            a.onComplete();
                            return;
                        }
                    }
                    if (!(e == 0 || r == LongCompanionObject.MAX_VALUE)) {
                        this.requested.addAndGet(-e);
                    }
                    int w = get();
                    if (w == missed) {
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else {
                        missed = w;
                    }
                }
            }
        }
    }

    static final class SortedJoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<List<T>> {
        private static final long serialVersionUID = 6751017204873808094L;
        final int index;
        final SortedJoinSubscription<T> parent;

        SortedJoinInnerSubscriber(SortedJoinSubscription<T> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, LongCompanionObject.MAX_VALUE);
        }

        public void onNext(List<T> t) {
            this.parent.innerNext(t, this.index);
        }

        public void onError(Throwable t) {
            this.parent.innerError(t);
        }

        public void onComplete() {
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            SubscriptionHelper.cancel(this);
        }
    }
}
