package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableZip<T, R> extends Flowable<R> {
    final int bufferSize;
    final boolean delayError;
    final Publisher<? extends T>[] sources;
    final Iterable<? extends Publisher<? extends T>> sourcesIterable;
    final Function<? super Object[], ? extends R> zipper;

    public FlowableZip(Publisher<? extends T>[] sources2, Iterable<? extends Publisher<? extends T>> sourcesIterable2, Function<? super Object[], ? extends R> zipper2, int bufferSize2, boolean delayError2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
        this.zipper = zipper2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        Publisher<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 == null) {
            sources2 = new Publisher[8];
            for (Publisher<? extends T> p : this.sourcesIterable) {
                if (count == sources2.length) {
                    Publisher<? extends T>[] b = new Publisher[((count >> 2) + count)];
                    System.arraycopy(sources2, 0, b, 0, count);
                    sources2 = b;
                }
                sources2[count] = p;
                count++;
            }
        } else {
            count = sources2.length;
        }
        if (count == 0) {
            EmptySubscription.complete(s);
            return;
        }
        ZipCoordinator<T, R> coordinator = new ZipCoordinator<>(s, this.zipper, count, this.bufferSize, this.delayError);
        s.onSubscribe(coordinator);
        coordinator.subscribe(sources2, count);
    }

    static final class ZipCoordinator<T, R> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = -2434867452883857743L;
        volatile boolean cancelled;
        final Object[] current;
        final boolean delayErrors;
        final Subscriber<? super R> downstream;
        final AtomicThrowable errors;
        final AtomicLong requested;
        final ZipSubscriber<T, R>[] subscribers;
        final Function<? super Object[], ? extends R> zipper;

        ZipCoordinator(Subscriber<? super R> actual, Function<? super Object[], ? extends R> zipper2, int n, int prefetch, boolean delayErrors2) {
            this.downstream = actual;
            this.zipper = zipper2;
            this.delayErrors = delayErrors2;
            ZipSubscriber<T, R>[] a = new ZipSubscriber[n];
            for (int i = 0; i < n; i++) {
                a[i] = new ZipSubscriber<>(this, prefetch);
            }
            this.current = new Object[n];
            this.subscribers = a;
            this.requested = new AtomicLong();
            this.errors = new AtomicThrowable();
        }

        /* access modifiers changed from: package-private */
        public void subscribe(Publisher<? extends T>[] sources, int n) {
            ZipSubscriber<T, R>[] a = this.subscribers;
            int i = 0;
            while (i < n && !this.cancelled) {
                if (this.delayErrors || this.errors.get() == null) {
                    sources[i].subscribe(a[i]);
                    i++;
                } else {
                    return;
                }
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
            }
        }

        /* access modifiers changed from: package-private */
        public void error(ZipSubscriber<T, R> inner, Throwable e) {
            if (this.errors.addThrowable(e)) {
                inner.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: package-private */
        public void cancelAll() {
            for (ZipSubscriber<T, R> s : this.subscribers) {
                s.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                Subscriber<? super R> a = this.downstream;
                ZipSubscriber<T, R>[] qs = this.subscribers;
                int n = qs.length;
                Object[] values = this.current;
                int missed = 1;
                do {
                    long r = this.requested.get();
                    long e = 0;
                    while (r != e) {
                        if (this.cancelled) {
                            return;
                        }
                        if (this.delayErrors || this.errors.get() == null) {
                            boolean empty = false;
                            for (int j = 0; j < n; j++) {
                                ZipSubscriber<T, R> inner = qs[j];
                                if (values[j] == null) {
                                    try {
                                        boolean d = inner.done;
                                        SimpleQueue<T> q = inner.queue;
                                        T v = q != null ? q.poll() : null;
                                        boolean sourceEmpty = v == null;
                                        if (d && sourceEmpty) {
                                            cancelAll();
                                            if (((Throwable) this.errors.get()) != null) {
                                                a.onError(this.errors.terminate());
                                                return;
                                            } else {
                                                a.onComplete();
                                                return;
                                            }
                                        } else if (!sourceEmpty) {
                                            values[j] = v;
                                        } else {
                                            empty = true;
                                        }
                                    } catch (Throwable ex) {
                                        Exceptions.throwIfFatal(ex);
                                        this.errors.addThrowable(ex);
                                        if (!this.delayErrors) {
                                            cancelAll();
                                            a.onError(this.errors.terminate());
                                            return;
                                        }
                                        empty = true;
                                    }
                                }
                            }
                            if (empty) {
                                break;
                            }
                            try {
                                a.onNext(ObjectHelper.requireNonNull(this.zipper.apply(values.clone()), "The zipper returned a null value"));
                                e++;
                                Arrays.fill(values, (Object) null);
                            } catch (Throwable ex2) {
                                Exceptions.throwIfFatal(ex2);
                                cancelAll();
                                this.errors.addThrowable(ex2);
                                a.onError(this.errors.terminate());
                                return;
                            }
                        } else {
                            cancelAll();
                            a.onError(this.errors.terminate());
                            return;
                        }
                    }
                    if (r == e) {
                        if (this.cancelled) {
                            return;
                        }
                        if (this.delayErrors || this.errors.get() == null) {
                            for (int j2 = 0; j2 < n; j2++) {
                                ZipSubscriber<T, R> inner2 = qs[j2];
                                if (values[j2] == null) {
                                    try {
                                        boolean d2 = inner2.done;
                                        SimpleQueue<T> q2 = inner2.queue;
                                        T v2 = q2 != null ? q2.poll() : null;
                                        boolean empty2 = v2 == null;
                                        if (d2 && empty2) {
                                            cancelAll();
                                            if (((Throwable) this.errors.get()) != null) {
                                                a.onError(this.errors.terminate());
                                                return;
                                            } else {
                                                a.onComplete();
                                                return;
                                            }
                                        } else if (!empty2) {
                                            values[j2] = v2;
                                        }
                                    } catch (Throwable ex3) {
                                        Exceptions.throwIfFatal(ex3);
                                        this.errors.addThrowable(ex3);
                                        if (!this.delayErrors) {
                                            cancelAll();
                                            a.onError(this.errors.terminate());
                                            return;
                                        }
                                    }
                                }
                            }
                        } else {
                            cancelAll();
                            a.onError(this.errors.terminate());
                            return;
                        }
                    }
                    if (e != 0) {
                        int length = qs.length;
                        for (int i = 0; i < length; i++) {
                            qs[i].request(e);
                        }
                        if (r != LongCompanionObject.MAX_VALUE) {
                            this.requested.addAndGet(-e);
                        }
                    }
                    missed = addAndGet(-missed);
                } while (missed != 0);
            }
        }
    }

    static final class ZipSubscriber<T, R> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -4627193790118206028L;
        volatile boolean done;
        final int limit;
        final ZipCoordinator<T, R> parent;
        final int prefetch;
        long produced;
        SimpleQueue<T> queue;
        int sourceMode;

        ZipSubscriber(ZipCoordinator<T, R> parent2, int prefetch2) {
            this.parent = parent2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> f = (QueueSubscription) s;
                    int m = f.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = f;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = f;
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                s.request((long) this.prefetch);
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 2) {
                this.queue.offer(t);
            }
            this.parent.drain();
        }

        public void onError(Throwable t) {
            this.parent.error(this, t);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void request(long n) {
            if (this.sourceMode != 1) {
                long p = this.produced + n;
                if (p >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(p);
                    return;
                }
                this.produced = p;
            }
        }
    }
}
