package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferBoundary<T, U extends Collection<? super T>, Open, Close> extends AbstractFlowableWithUpstream<T, U> {
    final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
    final Publisher<? extends Open> bufferOpen;
    final Callable<U> bufferSupplier;

    public FlowableBufferBoundary(Flowable<T> source, Publisher<? extends Open> bufferOpen2, Function<? super Open, ? extends Publisher<? extends Close>> bufferClose2, Callable<U> bufferSupplier2) {
        super(source);
        this.bufferOpen = bufferOpen2;
        this.bufferClose = bufferClose2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        BufferBoundarySubscriber<T, U, Open, Close> parent = new BufferBoundarySubscriber<>(s, this.bufferOpen, this.bufferClose, this.bufferSupplier);
        s.onSubscribe(parent);
        this.source.subscribe((FlowableSubscriber) parent);
    }

    static final class BufferBoundarySubscriber<T, C extends Collection<? super T>, Open, Close> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -8466418554264089604L;
        final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
        final Publisher<? extends Open> bufferOpen;
        final Callable<C> bufferSupplier;
        Map<Long, C> buffers = new LinkedHashMap();
        volatile boolean cancelled;
        volatile boolean done;
        final Subscriber<? super C> downstream;
        long emitted;
        final AtomicThrowable errors = new AtomicThrowable();
        long index;
        final SpscLinkedArrayQueue<C> queue = new SpscLinkedArrayQueue<>(Flowable.bufferSize());
        final AtomicLong requested = new AtomicLong();
        final CompositeDisposable subscribers = new CompositeDisposable();
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        BufferBoundarySubscriber(Subscriber<? super C> actual, Publisher<? extends Open> bufferOpen2, Function<? super Open, ? extends Publisher<? extends Close>> bufferClose2, Callable<C> bufferSupplier2) {
            this.downstream = actual;
            this.bufferSupplier = bufferSupplier2;
            this.bufferOpen = bufferOpen2;
            this.bufferClose = bufferClose2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.upstream, s)) {
                BufferOpenSubscriber<Open> open = new BufferOpenSubscriber<>(this);
                this.subscribers.add(open);
                this.bufferOpen.subscribe(open);
                s.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                Map<Long, C> bufs = this.buffers;
                if (bufs != null) {
                    for (C b : bufs.values()) {
                        b.add(t);
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (this.errors.addThrowable(t)) {
                this.subscribers.dispose();
                synchronized (this) {
                    this.buffers = null;
                }
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.subscribers.dispose();
            synchronized (this) {
                Map<Long, C> bufs = this.buffers;
                if (bufs != null) {
                    for (C b : bufs.values()) {
                        this.queue.offer(b);
                    }
                    this.buffers = null;
                    this.done = true;
                    drain();
                }
            }
        }

        public void request(long n) {
            BackpressureHelper.add(this.requested, n);
            drain();
        }

        public void cancel() {
            if (SubscriptionHelper.cancel(this.upstream)) {
                this.cancelled = true;
                this.subscribers.dispose();
                synchronized (this) {
                    this.buffers = null;
                }
                if (getAndIncrement() != 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void open(Open token) {
            try {
                C buf = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null Collection");
                Publisher<? extends Close> p = (Publisher) ObjectHelper.requireNonNull(this.bufferClose.apply(token), "The bufferClose returned a null Publisher");
                long idx = this.index;
                this.index = 1 + idx;
                synchronized (this) {
                    Map<Long, C> bufs = this.buffers;
                    if (bufs != null) {
                        bufs.put(Long.valueOf(idx), buf);
                        BufferCloseSubscriber<T, C> bc = new BufferCloseSubscriber<>(this, idx);
                        this.subscribers.add(bc);
                        p.subscribe(bc);
                    }
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                SubscriptionHelper.cancel(this.upstream);
                onError(ex);
            }
        }

        /* access modifiers changed from: package-private */
        public void openComplete(BufferOpenSubscriber<Open> os) {
            this.subscribers.delete(os);
            if (this.subscribers.size() == 0) {
                SubscriptionHelper.cancel(this.upstream);
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
            if (r1 == false) goto L_0x0030;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
            r6.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0030, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close(io.reactivex.internal.operators.flowable.FlowableBufferBoundary.BufferCloseSubscriber<T, C> r7, long r8) {
            /*
                r6 = this;
                io.reactivex.disposables.CompositeDisposable r2 = r6.subscribers
                r2.delete(r7)
                r1 = 0
                io.reactivex.disposables.CompositeDisposable r2 = r6.subscribers
                int r2 = r2.size()
                if (r2 != 0) goto L_0x0014
                r1 = 1
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r2 = r6.upstream
                io.reactivex.internal.subscriptions.SubscriptionHelper.cancel(r2)
            L_0x0014:
                monitor-enter(r6)
                java.util.Map<java.lang.Long, C> r0 = r6.buffers     // Catch:{ all -> 0x0034 }
                if (r0 != 0) goto L_0x001b
                monitor-exit(r6)     // Catch:{ all -> 0x0034 }
            L_0x001a:
                return
            L_0x001b:
                io.reactivex.internal.queue.SpscLinkedArrayQueue<C> r2 = r6.queue     // Catch:{ all -> 0x0034 }
                java.util.Map<java.lang.Long, C> r3 = r6.buffers     // Catch:{ all -> 0x0034 }
                java.lang.Long r4 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x0034 }
                java.lang.Object r3 = r3.remove(r4)     // Catch:{ all -> 0x0034 }
                r2.offer(r3)     // Catch:{ all -> 0x0034 }
                monitor-exit(r6)     // Catch:{ all -> 0x0034 }
                if (r1 == 0) goto L_0x0030
                r2 = 1
                r6.done = r2
            L_0x0030:
                r6.drain()
                goto L_0x001a
            L_0x0034:
                r2 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0034 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableBufferBoundary.BufferBoundarySubscriber.close(io.reactivex.internal.operators.flowable.FlowableBufferBoundary$BufferCloseSubscriber, long):void");
        }

        /* access modifiers changed from: package-private */
        public void boundaryError(Disposable subscriber, Throwable ex) {
            SubscriptionHelper.cancel(this.upstream);
            this.subscribers.delete(subscriber);
            onError(ex);
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                long e = this.emitted;
                Subscriber<? super C> a = this.downstream;
                SpscLinkedArrayQueue<C> q = this.queue;
                do {
                    long r = this.requested.get();
                    while (e != r) {
                        if (this.cancelled) {
                            q.clear();
                            return;
                        }
                        boolean d = this.done;
                        if (!d || this.errors.get() == null) {
                            C v = (Collection) q.poll();
                            boolean empty = v == null;
                            if (d && empty) {
                                a.onComplete();
                                return;
                            } else if (empty) {
                                break;
                            } else {
                                a.onNext(v);
                                e++;
                            }
                        } else {
                            q.clear();
                            a.onError(this.errors.terminate());
                            return;
                        }
                    }
                    if (e == r) {
                        if (this.cancelled) {
                            q.clear();
                            return;
                        } else if (this.done) {
                            if (this.errors.get() != null) {
                                q.clear();
                                a.onError(this.errors.terminate());
                                return;
                            } else if (q.isEmpty()) {
                                a.onComplete();
                                return;
                            }
                        }
                    }
                    this.emitted = e;
                    missed = addAndGet(-missed);
                } while (missed != 0);
            }
        }

        static final class BufferOpenSubscriber<Open> extends AtomicReference<Subscription> implements FlowableSubscriber<Open>, Disposable {
            private static final long serialVersionUID = -8498650778633225126L;
            final BufferBoundarySubscriber<?, ?, Open, ?> parent;

            BufferOpenSubscriber(BufferBoundarySubscriber<?, ?, Open, ?> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Subscription s) {
                SubscriptionHelper.setOnce(this, s, LongCompanionObject.MAX_VALUE);
            }

            public void onNext(Open t) {
                this.parent.open(t);
            }

            public void onError(Throwable t) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.boundaryError(this, t);
            }

            public void onComplete() {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.openComplete(this);
            }

            public void dispose() {
                SubscriptionHelper.cancel(this);
            }

            public boolean isDisposed() {
                return get() == SubscriptionHelper.CANCELLED;
            }
        }
    }

    static final class BufferCloseSubscriber<T, C extends Collection<? super T>> extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
        private static final long serialVersionUID = -8498650778633225126L;
        final long index;
        final BufferBoundarySubscriber<T, C, ?, ?> parent;

        BufferCloseSubscriber(BufferBoundarySubscriber<T, C, ?, ?> parent2, long index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, LongCompanionObject.MAX_VALUE);
        }

        public void onNext(Object t) {
            Subscription s = (Subscription) get();
            if (s != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                s.cancel();
                this.parent.close(this, this.index);
            }
        }

        public void onError(Throwable t) {
            if (get() != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.boundaryError(this, t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (get() != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.close(this, this.index);
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return get() == SubscriptionHelper.CANCELLED;
        }
    }
}
