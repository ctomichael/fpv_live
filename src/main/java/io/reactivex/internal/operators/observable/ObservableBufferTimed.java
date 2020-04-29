package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferTimed<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    public ObservableBufferTimed(ObservableSource<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, Callable<U> bufferSupplier2, int maxSize2, boolean restartTimerOnMaxSize2) {
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
    public void subscribeActual(Observer<? super U> t) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            this.source.subscribe(new BufferExactUnboundedObserver(new SerializedObserver(t), this.bufferSupplier, this.timespan, this.unit, this.scheduler));
            return;
        }
        Scheduler.Worker w = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            this.source.subscribe(new BufferExactBoundedObserver(new SerializedObserver(t), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, w));
        } else {
            this.source.subscribe(new BufferSkipBoundedObserver(new SerializedObserver(t), this.bufferSupplier, this.timespan, this.timeskip, this.unit, w));
        }
    }

    static final class BufferExactUnboundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;
        Disposable upstream;

        BufferExactUnboundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, Scheduler scheduler2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.downstream.onSubscribe(this);
                    if (!this.cancelled) {
                        Disposable task = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
                        if (!this.timer.compareAndSet(null, task)) {
                            task.dispose();
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    dispose();
                    EmptyDisposable.error(e, this.downstream);
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
            synchronized (this) {
                this.buffer = null;
            }
            this.downstream.onError(t);
            DisposableHelper.dispose(this.timer);
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
                    QueueDrainHelper.drainLoop(this.queue, this.downstream, false, null, this);
                }
            }
            DisposableHelper.dispose(this.timer);
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }

        public void run() {
            U current;
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                synchronized (this) {
                    current = this.buffer;
                    if (current != null) {
                        this.buffer = next;
                    }
                }
                if (current == null) {
                    DisposableHelper.dispose(this.timer);
                } else {
                    fastPathEmit(current, false, this);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.downstream.onError(e);
                dispose();
            }
        }

        public void accept(Observer<? super U> observer, U v) {
            this.downstream.onNext(v);
        }
    }

    static final class BufferSkipBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        final Callable<U> bufferSupplier;
        final List<U> buffers = new LinkedList();
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        Disposable upstream;
        final Scheduler.Worker w;

        BufferSkipBoundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, long timespan2, long timeskip2, TimeUnit unit2, Scheduler.Worker w2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.w = w2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.buffers.add(b);
                    this.downstream.onSubscribe(this);
                    this.w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                    this.w.schedule(new RemoveFromBufferEmit(b), this.timespan, this.unit);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    d.dispose();
                    EmptyDisposable.error(e, this.downstream);
                    this.w.dispose();
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
            clear();
            this.downstream.onError(t);
            this.w.dispose();
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
                QueueDrainHelper.drainLoop(this.queue, this.downstream, false, this.w, this);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                clear();
                this.upstream.dispose();
                this.w.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
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
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(b);
                            this.w.schedule(new RemoveFromBuffer(b), this.timespan, this.unit);
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.downstream.onError(e);
                    dispose();
                }
            }
        }

        public void accept(Observer<? super U> a, U v) {
            a.onNext(v);
        }

        final class RemoveFromBuffer implements Runnable {
            private final U b;

            RemoveFromBuffer(U b2) {
                this.b = b2;
            }

            public void run() {
                synchronized (BufferSkipBoundedObserver.this) {
                    BufferSkipBoundedObserver.this.buffers.remove(this.b);
                }
                BufferSkipBoundedObserver.this.fastPathOrderedEmit(this.b, false, BufferSkipBoundedObserver.this.w);
            }
        }

        final class RemoveFromBufferEmit implements Runnable {
            private final U buffer;

            RemoveFromBufferEmit(U buffer2) {
                this.buffer = buffer2;
            }

            public void run() {
                synchronized (BufferSkipBoundedObserver.this) {
                    BufferSkipBoundedObserver.this.buffers.remove(this.buffer);
                }
                BufferSkipBoundedObserver.this.fastPathOrderedEmit(this.buffer, false, BufferSkipBoundedObserver.this.w);
            }
        }
    }

    static final class BufferExactBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;
        Disposable upstream;
        final Scheduler.Worker w;

        BufferExactBoundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, int maxSize2, boolean restartOnMaxSize, Scheduler.Worker w2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.maxSize = maxSize2;
            this.restartTimerOnMaxSize = restartOnMaxSize;
            this.w = w2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.downstream.onSubscribe(this);
                    this.timer = this.w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    d.dispose();
                    EmptyDisposable.error(e, this.downstream);
                    this.w.dispose();
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
            fastPathOrderedEmit(r9, false, r11);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            r9 = (java.util.Collection) io.reactivex.internal.functions.ObjectHelper.requireNonNull(r11.bufferSupplier.call(), "The buffer supplied is null");
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
            r11.downstream.onError(r10);
            dispose();
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
                r11.fastPathOrderedEmit(r9, r2, r11)
                java.util.concurrent.Callable<U> r2 = r11.bufferSupplier     // Catch:{ Throwable -> 0x005d }
                java.lang.Object r2 = r2.call()     // Catch:{ Throwable -> 0x005d }
                java.lang.String r3 = "The buffer supplied is null"
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
                io.reactivex.Observer r2 = r11.downstream
                r2.onError(r10)
                r11.dispose()
                goto L_0x0008
            L_0x006a:
                r2 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x006a }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableBufferTimed.BufferExactBoundedObserver.onNext(java.lang.Object):void");
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
            this.w.dispose();
            synchronized (this) {
                b = this.buffer;
                this.buffer = null;
            }
            if (b != null) {
                this.queue.offer(b);
                this.done = true;
                if (enter()) {
                    QueueDrainHelper.drainLoop(this.queue, this.downstream, false, this, this);
                }
            }
        }

        public void accept(Observer<? super U> a, U v) {
            a.onNext(v);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.dispose();
                this.w.dispose();
                synchronized (this) {
                    this.buffer = null;
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
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
                java.lang.String r4 = "The bufferSupplier returned a null buffer"
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
                r8.dispose()
                io.reactivex.Observer r3 = r8.downstream
                r3.onError(r1)
                goto L_0x001d
            L_0x002b:
                r8.buffer = r2     // Catch:{ all -> 0x0033 }
                monitor-exit(r8)     // Catch:{ all -> 0x0033 }
                r3 = 0
                r8.fastPathOrderedEmit(r0, r3, r8)
                goto L_0x001d
            L_0x0033:
                r3 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0033 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableBufferTimed.BufferExactBoundedObserver.run():void");
        }
    }
}
