package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.processors.UnicastProcessor;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindow<T> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
    final int bufferSize;
    final long size;
    final long skip;

    public FlowableWindow(Publisher<T> source, long size2, long skip2, int bufferSize2) {
        super(source);
        this.size = size2;
        this.skip = skip2;
        this.bufferSize = bufferSize2;
    }

    public void subscribeActual(Subscriber<? super Flowable<T>> s) {
        if (this.skip == this.size) {
            this.source.subscribe(new WindowExactSubscriber(s, this.size, this.bufferSize));
        } else if (this.skip > this.size) {
            this.source.subscribe(new WindowSkipSubscriber(s, this.size, this.skip, this.bufferSize));
        } else {
            this.source.subscribe(new WindowOverlapSubscriber(s, this.size, this.skip, this.bufferSize));
        }
    }

    static final class WindowExactSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -2365647875069161133L;
        final Subscriber<? super Flowable<T>> actual;
        final int bufferSize;
        boolean done;
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        Subscription s;
        final long size;
        UnicastProcessor<T> window;

        WindowExactSubscriber(Subscriber<? super Flowable<T>> actual2, long size2, int bufferSize2) {
            super(1);
            this.actual = actual2;
            this.size = size2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long i = this.index;
                UnicastProcessor<T> w = this.window;
                if (i == 0) {
                    getAndIncrement();
                    w = UnicastProcessor.create(this.bufferSize, this);
                    this.window = w;
                    this.actual.onNext(w);
                }
                long i2 = i + 1;
                w.onNext(t);
                if (i2 == this.size) {
                    this.index = 0;
                    this.window = null;
                    w.onComplete();
                    return;
                }
                this.index = i2;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            Processor<T, T> w = this.window;
            if (w != null) {
                this.window = null;
                w.onError(t);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                Processor<T, T> w = this.window;
                if (w != null) {
                    this.window = null;
                    w.onComplete();
                }
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                this.s.request(BackpressureHelper.multiplyCap(this.size, n));
            }
        }

        public void cancel() {
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.s.cancel();
            }
        }
    }

    static final class WindowSkipSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -8792836352386833856L;
        final Subscriber<? super Flowable<T>> actual;
        final int bufferSize;
        boolean done;
        final AtomicBoolean firstRequest = new AtomicBoolean();
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        Subscription s;
        final long size;
        final long skip;
        UnicastProcessor<T> window;

        WindowSkipSubscriber(Subscriber<? super Flowable<T>> actual2, long size2, long skip2, int bufferSize2) {
            super(1);
            this.actual = actual2;
            this.size = size2;
            this.skip = skip2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long i = this.index;
                UnicastProcessor<T> w = this.window;
                if (i == 0) {
                    getAndIncrement();
                    w = UnicastProcessor.create(this.bufferSize, this);
                    this.window = w;
                    this.actual.onNext(w);
                }
                long i2 = i + 1;
                if (w != null) {
                    w.onNext(t);
                }
                if (i2 == this.size) {
                    this.window = null;
                    w.onComplete();
                }
                if (i2 == this.skip) {
                    this.index = 0;
                } else {
                    this.index = i2;
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            Processor<T, T> w = this.window;
            if (w != null) {
                this.window = null;
                w.onError(t);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                Processor<T, T> w = this.window;
                if (w != null) {
                    this.window = null;
                    w.onComplete();
                }
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            if (!SubscriptionHelper.validate(n)) {
                return;
            }
            if (this.firstRequest.get() || !this.firstRequest.compareAndSet(false, true)) {
                this.s.request(BackpressureHelper.multiplyCap(this.skip, n));
                return;
            }
            this.s.request(BackpressureHelper.addCap(BackpressureHelper.multiplyCap(this.size, n), BackpressureHelper.multiplyCap(this.skip - this.size, n - 1)));
        }

        public void cancel() {
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.s.cancel();
            }
        }
    }

    static final class WindowOverlapSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = 2428527070996323976L;
        final Subscriber<? super Flowable<T>> actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final AtomicBoolean firstRequest = new AtomicBoolean();
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        long produced;
        final SpscLinkedArrayQueue<UnicastProcessor<T>> queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final long size;
        final long skip;
        final ArrayDeque<UnicastProcessor<T>> windows = new ArrayDeque<>();
        final AtomicInteger wip = new AtomicInteger();

        WindowOverlapSubscriber(Subscriber<? super Flowable<T>> actual2, long size2, long skip2, int bufferSize2) {
            super(1);
            this.actual = actual2;
            this.size = size2;
            this.skip = skip2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize2);
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long i = this.index;
                if (i == 0 && !this.cancelled) {
                    getAndIncrement();
                    UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize, this);
                    this.windows.offer(w);
                    this.queue.offer(w);
                    drain();
                }
                long i2 = i + 1;
                Iterator i$ = this.windows.iterator();
                while (i$.hasNext()) {
                    i$.next().onNext(t);
                }
                long p = this.produced + 1;
                if (p == this.size) {
                    this.produced = p - this.skip;
                    Processor<T, T> w2 = this.windows.poll();
                    if (w2 != null) {
                        w2.onComplete();
                    }
                } else {
                    this.produced = p;
                }
                if (i2 == this.skip) {
                    this.index = 0;
                } else {
                    this.index = i2;
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            Iterator i$ = this.windows.iterator();
            while (i$.hasNext()) {
                i$.next().onError(t);
            }
            this.windows.clear();
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                Iterator i$ = this.windows.iterator();
                while (i$.hasNext()) {
                    i$.next().onComplete();
                }
                this.windows.clear();
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber<? super Flowable<T>> a = this.actual;
                SpscLinkedArrayQueue<UnicastProcessor<T>> q = this.queue;
                int missed = 1;
                do {
                    long r = this.requested.get();
                    long e = 0;
                    while (e != r) {
                        boolean d = this.done;
                        UnicastProcessor<T> t = q.poll();
                        boolean empty = t == null;
                        if (checkTerminated(d, empty, a, q)) {
                            return;
                        }
                        if (empty) {
                            break;
                        }
                        a.onNext(t);
                        e++;
                    }
                    if (e != r || !checkTerminated(this.done, q.isEmpty(), a, q)) {
                        if (!(e == 0 || r == LongCompanionObject.MAX_VALUE)) {
                            this.requested.addAndGet(-e);
                        }
                        missed = this.wip.addAndGet(-missed);
                    } else {
                        return;
                    }
                } while (missed != 0);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
            if (this.cancelled) {
                q.clear();
                return true;
            }
            if (d) {
                Throwable e = this.error;
                if (e != null) {
                    q.clear();
                    a.onError(e);
                    return true;
                } else if (empty) {
                    a.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                if (this.firstRequest.get() || !this.firstRequest.compareAndSet(false, true)) {
                    this.s.request(BackpressureHelper.multiplyCap(this.skip, n));
                } else {
                    this.s.request(BackpressureHelper.addCap(this.size, BackpressureHelper.multiplyCap(this.skip, n - 1)));
                }
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.s.cancel();
            }
        }
    }
}
