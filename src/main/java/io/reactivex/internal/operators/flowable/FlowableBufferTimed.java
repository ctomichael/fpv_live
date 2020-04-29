package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferTimed<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    public FlowableBufferTimed(Flowable<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, Callable<U> bufferSupplier2, int maxSize2, boolean restartTimerOnMaxSize2) {
        super(source);
        this.timespan = timespan2;
        this.timeskip = timeskip2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.bufferSupplier = bufferSupplier2;
        this.maxSize = maxSize2;
        this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            this.source.subscribe((FlowableSubscriber) new BufferExactUnboundedSubscriber(new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.unit, this.scheduler));
            return;
        }
        Scheduler.Worker w = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            this.source.subscribe((FlowableSubscriber) new BufferExactBoundedSubscriber(new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, w));
        } else {
            this.source.subscribe((FlowableSubscriber) new BufferSkipBoundedSubscriber(new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.timeskip, this.unit, w));
        }
    }

    static final class BufferExactUnboundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;
        Subscription upstream;

        BufferExactUnboundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, Scheduler scheduler2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.downstream.onSubscribe(this);
                    if (!this.cancelled) {
                        s.request(LongCompanionObject.MAX_VALUE);
                        Disposable d = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
                        if (!this.timer.compareAndSet(null, d)) {
                            d.dispose();
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    EmptySubscription.error(e, this.downstream);
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                U b = this.buffer;
                if (b != null) {
                    b.add(t);
                }
            }
        }

        public void onError(Throwable t) {
            DisposableHelper.dispose(this.timer);
            synchronized (this) {
                this.buffer = null;
            }
            this.downstream.onError(t);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001f, code lost:
            io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r5.queue, r5.downstream, false, null, r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0011, code lost:
            r5.queue.offer(r0);
            r5.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
            if (enter() == false) goto L_?;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            /*
                r5 = this;
                r4 = 0
                java.util.concurrent.atomic.AtomicReference<io.reactivex.disposables.Disposable> r1 = r5.timer
                io.reactivex.internal.disposables.DisposableHelper.dispose(r1)
                monitor-enter(r5)
                U r0 = r5.buffer     // Catch:{ all -> 0x0028 }
                if (r0 != 0) goto L_0x000d
                monitor-exit(r5)     // Catch:{ all -> 0x0028 }
            L_0x000c:
                return
            L_0x000d:
                r1 = 0
                r5.buffer = r1     // Catch:{ all -> 0x0028 }
                monitor-exit(r5)     // Catch:{ all -> 0x0028 }
                io.reactivex.internal.fuseable.SimplePlainQueue r1 = r5.queue
                r1.offer(r0)
                r1 = 1
                r5.done = r1
                boolean r1 = r5.enter()
                if (r1 == 0) goto L_0x000c
                io.reactivex.internal.fuseable.SimplePlainQueue r1 = r5.queue
                org.reactivestreams.Subscriber r2 = r5.downstream
                r3 = 0
                io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r1, r2, r3, r4, r5)
                goto L_0x000c
            L_0x0028:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0028 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactUnboundedSubscriber.onComplete():void");
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
            this.upstream.cancel();
            DisposableHelper.dispose(this.timer);
        }

        public void run() {
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                synchronized (this) {
                    U current = this.buffer;
                    if (current != null) {
                        this.buffer = next;
                        fastPathEmitMax(current, false, this);
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                this.downstream.onError(e);
            }
        }

        public boolean accept(Subscriber<? super U> subscriber, U v) {
            this.downstream.onNext(v);
            return true;
        }

        public void dispose() {
            cancel();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }
    }

    static final class BufferSkipBoundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable {
        final Callable<U> bufferSupplier;
        final List<U> buffers = new LinkedList();
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        Subscription upstream;
        final Scheduler.Worker w;

        BufferSkipBoundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, long timespan2, long timeskip2, TimeUnit unit2, Scheduler.Worker w2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.w = w2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.buffers.add(b);
                    this.downstream.onSubscribe(this);
                    s.request(LongCompanionObject.MAX_VALUE);
                    this.w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                    this.w.schedule(new RemoveFromBuffer(b), this.timespan, this.unit);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.w.dispose();
                    s.cancel();
                    EmptySubscription.error(e, this.downstream);
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                for (U b : this.buffers) {
                    b.add(t);
                }
            }
        }

        public void onError(Throwable t) {
            this.done = true;
            this.w.dispose();
            clear();
            this.downstream.onError(t);
        }

        public void onComplete() {
            List<U> bs;
            synchronized (this) {
                bs = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            for (U b : bs) {
                this.queue.offer(b);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, this.w, this);
            }
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
            this.upstream.cancel();
            this.w.dispose();
            clear();
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            synchronized (this) {
                this.buffers.clear();
            }
        }

        public void run() {
            if (!this.cancelled) {
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(b);
                            this.w.schedule(new RemoveFromBuffer(b), this.timespan, this.unit);
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    this.downstream.onError(e);
                }
            }
        }

        public boolean accept(Subscriber<? super U> a, U v) {
            a.onNext(v);
            return true;
        }

        final class RemoveFromBuffer implements Runnable {
            private final U buffer;

            RemoveFromBuffer(U buffer2) {
                this.buffer = buffer2;
            }

            public void run() {
                synchronized (BufferSkipBoundedSubscriber.this) {
                    BufferSkipBoundedSubscriber.this.buffers.remove(this.buffer);
                }
                BufferSkipBoundedSubscriber.this.fastPathOrderedEmitMax(this.buffer, false, BufferSkipBoundedSubscriber.this.w);
            }
        }
    }

    static final class BufferExactBoundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;
        Subscription upstream;
        final Scheduler.Worker w;

        BufferExactBoundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, int maxSize2, boolean restartOnMaxSize, Scheduler.Worker w2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.maxSize = maxSize2;
            this.restartTimerOnMaxSize = restartOnMaxSize;
            this.w = w2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.downstream.onSubscribe(this);
                    this.timer = this.w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
                    s.request(LongCompanionObject.MAX_VALUE);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.w.dispose();
                    s.cancel();
                    EmptySubscription.error(e, this.downstream);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0024, code lost:
            if (r11.restartTimerOnMaxSize == false) goto L_0x002b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0026, code lost:
            r11.timer.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002b, code lost:
            fastPathOrderedEmitMax(r9, false, r11);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            r9 = (java.util.Collection) io.reactivex.internal.functions.ObjectHelper.requireNonNull(r11.bufferSupplier.call(), "The supplied buffer is null");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0040, code lost:
            monitor-enter(r11);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            r11.buffer = r9;
            r11.consumerIndex++;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0048, code lost:
            monitor-exit(r11);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x004b, code lost:
            if (r11.restartTimerOnMaxSize == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x004d, code lost:
            r11.timer = r11.w.schedulePeriodically(r11, r11.timespan, r11.timespan, r11.unit);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x005d, code lost:
            r10 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005e, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r10);
            cancel();
            r11.downstream.onError(r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
            return;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r12) {
            /*
                r11 = this;
                r4 = 1
                monitor-enter(r11)
                U r9 = r11.buffer     // Catch:{ all -> 0x0016 }
                if (r9 != 0) goto L_0x0009
                monitor-exit(r11)     // Catch:{ all -> 0x0016 }
            L_0x0008:
                return
            L_0x0009:
                r9.add(r12)     // Catch:{ all -> 0x0016 }
                int r2 = r9.size()     // Catch:{ all -> 0x0016 }
                int r3 = r11.maxSize     // Catch:{ all -> 0x0016 }
                if (r2 >= r3) goto L_0x0019
                monitor-exit(r11)     // Catch:{ all -> 0x0016 }
                goto L_0x0008
            L_0x0016:
                r2 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x0016 }
                throw r2
            L_0x0019:
                r2 = 0
                r11.buffer = r2     // Catch:{ all -> 0x0016 }
                long r2 = r11.producerIndex     // Catch:{ all -> 0x0016 }
                long r2 = r2 + r4
                r11.producerIndex = r2     // Catch:{ all -> 0x0016 }
                monitor-exit(r11)     // Catch:{ all -> 0x0016 }
                boolean r2 = r11.restartTimerOnMaxSize
                if (r2 == 0) goto L_0x002b
                io.reactivex.disposables.Disposable r2 = r11.timer
                r2.dispose()
            L_0x002b:
                r2 = 0
                r11.fastPathOrderedEmitMax(r9, r2, r11)
                java.util.concurrent.Callable<U> r2 = r11.bufferSupplier     // Catch:{ Throwable -> 0x005d }
                java.lang.Object r2 = r2.call()     // Catch:{ Throwable -> 0x005d }
                java.lang.String r3 = "The supplied buffer is null"
                java.lang.Object r2 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r3)     // Catch:{ Throwable -> 0x005d }
                r0 = r2
                java.util.Collection r0 = (java.util.Collection) r0     // Catch:{ Throwable -> 0x005d }
                r9 = r0
                monitor-enter(r11)
                r11.buffer = r9     // Catch:{ all -> 0x006a }
                long r2 = r11.consumerIndex     // Catch:{ all -> 0x006a }
                long r2 = r2 + r4
                r11.consumerIndex = r2     // Catch:{ all -> 0x006a }
                monitor-exit(r11)     // Catch:{ all -> 0x006a }
                boolean r2 = r11.restartTimerOnMaxSize
                if (r2 == 0) goto L_0x0008
                io.reactivex.Scheduler$Worker r2 = r11.w
                long r4 = r11.timespan
                long r6 = r11.timespan
                java.util.concurrent.TimeUnit r8 = r11.unit
                r3 = r11
                io.reactivex.disposables.Disposable r2 = r2.schedulePeriodically(r3, r4, r6, r8)
                r11.timer = r2
                goto L_0x0008
            L_0x005d:
                r10 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r10)
                r11.cancel()
                org.reactivestreams.Subscriber r2 = r11.downstream
                r2.onError(r10)
                goto L_0x0008
            L_0x006a:
                r2 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x006a }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactBoundedSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable t) {
            synchronized (this) {
                this.buffer = null;
            }
            this.downstream.onError(t);
            this.w.dispose();
        }

        public void onComplete() {
            U b;
            synchronized (this) {
                b = this.buffer;
                this.buffer = null;
            }
            if (b != null) {
                this.queue.offer(b);
                this.done = true;
                if (enter()) {
                    QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, this, this);
                }
                this.w.dispose();
            }
        }

        public boolean accept(Subscriber<? super U> a, U v) {
            a.onNext(v);
            return true;
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                dispose();
            }
        }

        public void dispose() {
            synchronized (this) {
                this.buffer = null;
            }
            this.upstream.cancel();
            this.w.dispose();
        }

        public boolean isDisposed() {
            return this.w.isDisposed();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r8 = this;
                java.util.concurrent.Callable<U> r3 = r8.bufferSupplier     // Catch:{ Throwable -> 0x001e }
                java.lang.Object r3 = r3.call()     // Catch:{ Throwable -> 0x001e }
                java.lang.String r4 = "The supplied buffer is null"
                java.lang.Object r2 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r3, r4)     // Catch:{ Throwable -> 0x001e }
                java.util.Collection r2 = (java.util.Collection) r2     // Catch:{ Throwable -> 0x001e }
                monitor-enter(r8)
                U r0 = r8.buffer     // Catch:{ all -> 0x0033 }
                if (r0 == 0) goto L_0x001c
                long r4 = r8.producerIndex     // Catch:{ all -> 0x0033 }
                long r6 = r8.consumerIndex     // Catch:{ all -> 0x0033 }
                int r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r3 == 0) goto L_0x002b
            L_0x001c:
                monitor-exit(r8)     // Catch:{ all -> 0x0033 }
            L_0x001d:
                return
            L_0x001e:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r8.cancel()
                org.reactivestreams.Subscriber r3 = r8.downstream
                r3.onError(r1)
                goto L_0x001d
            L_0x002b:
                r8.buffer = r2     // Catch:{ all -> 0x0033 }
                monitor-exit(r8)     // Catch:{ all -> 0x0033 }
                r3 = 0
                r8.fastPathOrderedEmitMax(r0, r3, r8)
                goto L_0x001d
            L_0x0033:
                r3 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0033 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactBoundedSubscriber.run():void");
        }
    }
}
