package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.flowable.FlowableMap;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCombineLatest<T, R> extends Flowable<R> {
    @Nullable
    final Publisher<? extends T>[] array;
    final int bufferSize;
    final Function<? super Object[], ? extends R> combiner;
    final boolean delayErrors;
    @Nullable
    final Iterable<? extends Publisher<? extends T>> iterable;

    public FlowableCombineLatest(@NonNull Publisher[] publisherArr, @NonNull Function function, int bufferSize2, boolean delayErrors2) {
        this.array = publisherArr;
        this.iterable = null;
        this.combiner = function;
        this.bufferSize = bufferSize2;
        this.delayErrors = delayErrors2;
    }

    public FlowableCombineLatest(@NonNull Iterable iterable2, @NonNull Function function, int bufferSize2, boolean delayErrors2) {
        this.array = null;
        this.iterable = iterable2;
        this.combiner = function;
        this.bufferSize = bufferSize2;
        this.delayErrors = delayErrors2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        int n;
        int n2;
        Publisher<? extends T>[] a = this.array;
        if (a == null) {
            int n3 = 0;
            a = new Publisher[8];
            try {
                Iterator<? extends Publisher<? extends T>> it2 = (Iterator) ObjectHelper.requireNonNull(this.iterable.iterator(), "The iterator returned is null");
                while (true) {
                    try {
                        n2 = n3;
                        if (!it2.hasNext()) {
                            break;
                        }
                        try {
                            Publisher<? extends T> p = (Publisher) ObjectHelper.requireNonNull(it2.next(), "The publisher returned by the iterator is null");
                            if (n2 == a.length) {
                                Publisher<? extends T>[] c = new Publisher[((n2 >> 2) + n2)];
                                System.arraycopy(a, 0, c, 0, n2);
                                a = c;
                            }
                            n3 = n2 + 1;
                            a[n2] = p;
                        } catch (Throwable e) {
                            Exceptions.throwIfFatal(e);
                            EmptySubscription.error(e, s);
                            return;
                        }
                    } catch (Throwable e2) {
                        Exceptions.throwIfFatal(e2);
                        EmptySubscription.error(e2, s);
                        return;
                    }
                }
                n = n2;
            } catch (Throwable e3) {
                Exceptions.throwIfFatal(e3);
                EmptySubscription.error(e3, s);
                return;
            }
        } else {
            n = a.length;
        }
        if (n == 0) {
            EmptySubscription.complete(s);
        } else if (n == 1) {
            a[0].subscribe(new FlowableMap.MapSubscriber(s, new SingletonArrayFunc()));
        } else {
            CombineLatestCoordinator<T, R> coordinator = new CombineLatestCoordinator<>(s, this.combiner, n, this.bufferSize, this.delayErrors);
            s.onSubscribe(coordinator);
            coordinator.subscribe(a, n);
        }
    }

    static final class CombineLatestCoordinator<T, R> extends BasicIntQueueSubscription<R> {
        private static final long serialVersionUID = -5082275438355852221L;
        volatile boolean cancelled;
        final Function<? super Object[], ? extends R> combiner;
        int completedSources;
        final boolean delayErrors;
        volatile boolean done;
        final Subscriber<? super R> downstream;
        final AtomicReference<Throwable> error;
        final Object[] latest;
        int nonEmptySources;
        boolean outputFused;
        final SpscLinkedArrayQueue<Object> queue;
        final AtomicLong requested;
        final CombineLatestInnerSubscriber<T>[] subscribers;

        CombineLatestCoordinator(Subscriber<? super R> actual, Function<? super Object[], ? extends R> combiner2, int n, int bufferSize, boolean delayErrors2) {
            this.downstream = actual;
            this.combiner = combiner2;
            CombineLatestInnerSubscriber<T>[] a = new CombineLatestInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                a[i] = new CombineLatestInnerSubscriber<>(this, i, bufferSize);
            }
            this.subscribers = a;
            this.latest = new Object[n];
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
            this.requested = new AtomicLong();
            this.error = new AtomicReference<>();
            this.delayErrors = delayErrors2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            cancelAll();
        }

        /* access modifiers changed from: package-private */
        public void subscribe(Publisher<? extends T>[] sources, int n) {
            CombineLatestInnerSubscriber<T>[] a = this.subscribers;
            for (int i = 0; i < n && !this.done && !this.cancelled; i++) {
                sources[i].subscribe(a[i]);
            }
        }

        /* access modifiers changed from: package-private */
        public void innerValue(int index, T value) {
            boolean replenishInsteadOfDrain;
            synchronized (this) {
                Object[] os = this.latest;
                int localNonEmptySources = this.nonEmptySources;
                if (os[index] == null) {
                    localNonEmptySources++;
                    this.nonEmptySources = localNonEmptySources;
                }
                os[index] = value;
                if (os.length == localNonEmptySources) {
                    this.queue.offer(this.subscribers[index], os.clone());
                    replenishInsteadOfDrain = false;
                } else {
                    replenishInsteadOfDrain = true;
                }
            }
            if (replenishInsteadOfDrain) {
                this.subscribers[index].requestOne();
            } else {
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0012, code lost:
            drain();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerComplete(int r4) {
            /*
                r3 = this;
                monitor-enter(r3)
                java.lang.Object[] r1 = r3.latest     // Catch:{ all -> 0x001a }
                r2 = r1[r4]     // Catch:{ all -> 0x001a }
                if (r2 == 0) goto L_0x001d
                int r2 = r3.completedSources     // Catch:{ all -> 0x001a }
                int r0 = r2 + 1
                int r2 = r1.length     // Catch:{ all -> 0x001a }
                if (r0 != r2) goto L_0x0016
                r2 = 1
                r3.done = r2     // Catch:{ all -> 0x001a }
            L_0x0011:
                monitor-exit(r3)     // Catch:{ all -> 0x001a }
                r3.drain()
            L_0x0015:
                return
            L_0x0016:
                r3.completedSources = r0     // Catch:{ all -> 0x001a }
                monitor-exit(r3)     // Catch:{ all -> 0x001a }
                goto L_0x0015
            L_0x001a:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x001a }
                throw r2
            L_0x001d:
                r2 = 1
                r3.done = r2     // Catch:{ all -> 0x001a }
                goto L_0x0011
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableCombineLatest.CombineLatestCoordinator.innerComplete(int):void");
        }

        /* access modifiers changed from: package-private */
        public void innerError(int index, Throwable e) {
            if (!ExceptionHelper.addThrowable(this.error, e)) {
                RxJavaPlugins.onError(e);
            } else if (!this.delayErrors) {
                cancelAll();
                this.done = true;
                drain();
            } else {
                innerComplete(index);
            }
        }

        /* access modifiers changed from: package-private */
        public void drainOutput() {
            Subscriber<? super R> a = this.downstream;
            SpscLinkedArrayQueue<Object> q = this.queue;
            int missed = 1;
            while (!this.cancelled) {
                Throwable ex = this.error.get();
                if (ex != null) {
                    q.clear();
                    a.onError(ex);
                    return;
                }
                boolean d = this.done;
                boolean empty = q.isEmpty();
                if (!empty) {
                    a.onNext(null);
                }
                if (!d || !empty) {
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    a.onComplete();
                    return;
                }
            }
            q.clear();
        }

        /* access modifiers changed from: package-private */
        public void drainAsync() {
            Subscriber<? super R> a = this.downstream;
            SpscLinkedArrayQueue<Object> q = this.queue;
            int missed = 1;
            do {
                long r = this.requested.get();
                long e = 0;
                while (e != r) {
                    boolean d = this.done;
                    Object v = q.poll();
                    boolean empty = v == null;
                    if (!checkTerminated(d, empty, a, q)) {
                        if (empty) {
                            break;
                        }
                        try {
                            a.onNext(ObjectHelper.requireNonNull(this.combiner.apply((Object[]) ((Object[]) q.poll())), "The combiner returned a null value"));
                            ((CombineLatestInnerSubscriber) v).requestOne();
                            e++;
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            cancelAll();
                            ExceptionHelper.addThrowable(this.error, ex);
                            a.onError(ExceptionHelper.terminate(this.error));
                            return;
                        }
                    } else {
                        return;
                    }
                }
                if (e == r) {
                    if (checkTerminated(this.done, q.isEmpty(), a, q)) {
                        return;
                    }
                }
                if (!(e == 0 || r == LongCompanionObject.MAX_VALUE)) {
                    this.requested.addAndGet(-e);
                }
                missed = addAndGet(-missed);
            } while (missed != 0);
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                if (this.outputFused) {
                    drainOutput();
                } else {
                    drainAsync();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
            if (this.cancelled) {
                cancelAll();
                q.clear();
                return true;
            }
            if (d) {
                if (!this.delayErrors) {
                    Throwable e = ExceptionHelper.terminate(this.error);
                    if (e != null && e != ExceptionHelper.TERMINATED) {
                        cancelAll();
                        q.clear();
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        cancelAll();
                        a.onComplete();
                        return true;
                    }
                } else if (empty) {
                    cancelAll();
                    Throwable e2 = ExceptionHelper.terminate(this.error);
                    if (e2 == null || e2 == ExceptionHelper.TERMINATED) {
                        a.onComplete();
                        return true;
                    }
                    a.onError(e2);
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void cancelAll() {
            for (CombineLatestInnerSubscriber<T> inner : this.subscribers) {
                inner.cancel();
            }
        }

        public int requestFusion(int requestedMode) {
            boolean z = false;
            if ((requestedMode & 4) != 0) {
                return 0;
            }
            int m = requestedMode & 2;
            if (m != 0) {
                z = true;
            }
            this.outputFused = z;
            return m;
        }

        @Nullable
        public R poll() throws Exception {
            Object e = this.queue.poll();
            if (e == null) {
                return null;
            }
            R requireNonNull = ObjectHelper.requireNonNull(this.combiner.apply((Object[]) ((Object[]) this.queue.poll())), "The combiner returned a null value");
            ((CombineLatestInnerSubscriber) e).requestOne();
            return requireNonNull;
        }

        public void clear() {
            this.queue.clear();
        }

        public boolean isEmpty() {
            return this.queue.isEmpty();
        }
    }

    static final class CombineLatestInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -8730235182291002949L;
        final int index;
        final int limit;
        final CombineLatestCoordinator<T, ?> parent;
        final int prefetch;
        int produced;

        CombineLatestInnerSubscriber(CombineLatestCoordinator<T, ?> parent2, int index2, int prefetch2) {
            this.parent = parent2;
            this.index = index2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, (long) this.prefetch);
        }

        public void onNext(T t) {
            this.parent.innerValue(this.index, t);
        }

        public void onError(Throwable t) {
            this.parent.innerError(this.index, t);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void requestOne() {
            int p = this.produced + 1;
            if (p == this.limit) {
                this.produced = 0;
                ((Subscription) get()).request((long) p);
                return;
            }
            this.produced = p;
        }
    }

    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return FlowableCombineLatest.this.combiner.apply(new Object[]{t});
        }
    }
}
