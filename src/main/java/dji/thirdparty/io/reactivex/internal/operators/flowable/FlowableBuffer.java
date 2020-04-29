package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BooleanSupplier;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.internal.util.QueueDrainHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBuffer<T, C extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, C> {
    final Callable<C> bufferSupplier;
    final int size;
    final int skip;

    public FlowableBuffer(Publisher<T> source, int size2, int skip2, Callable<C> bufferSupplier2) {
        super(source);
        this.size = size2;
        this.skip = skip2;
        this.bufferSupplier = bufferSupplier2;
    }

    public void subscribeActual(Subscriber<? super C> s) {
        if (this.size == this.skip) {
            this.source.subscribe(new PublisherBufferExactSubscriber(s, this.size, this.bufferSupplier));
        } else if (this.skip > this.size) {
            this.source.subscribe(new PublisherBufferSkipSubscriber(s, this.size, this.skip, this.bufferSupplier));
        } else {
            this.source.subscribe(new PublisherBufferOverlappingSubscriber(s, this.size, this.skip, this.bufferSupplier));
        }
    }

    static final class PublisherBufferExactSubscriber<T, C extends Collection<? super T>> implements Subscriber<T>, Subscription {
        final Subscriber<? super C> actual;
        C buffer;
        final Callable<C> bufferSupplier;
        boolean done;
        int index;
        Subscription s;
        final int size;

        PublisherBufferExactSubscriber(Subscriber<? super C> actual2, int size2, Callable<C> bufferSupplier2) {
            this.actual = actual2;
            this.size = size2;
            this.bufferSupplier = bufferSupplier2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                this.s.request(BackpressureHelper.multiplyCap(n, (long) this.size));
            }
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                C b = this.buffer;
                if (b == null) {
                    try {
                        b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                        this.buffer = b;
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
                        onError(e);
                        return;
                    }
                }
                b.add(t);
                int i = this.index + 1;
                if (i == this.size) {
                    this.index = 0;
                    this.buffer = null;
                    this.actual.onNext(b);
                    return;
                }
                this.index = i;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                C b = this.buffer;
                if (b != null && !b.isEmpty()) {
                    this.actual.onNext(b);
                }
                this.actual.onComplete();
            }
        }
    }

    static final class PublisherBufferSkipSubscriber<T, C extends Collection<? super T>> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -5616169793639412593L;
        final Subscriber<? super C> actual;
        C buffer;
        final Callable<C> bufferSupplier;
        boolean done;
        int index;
        Subscription s;
        final int size;
        final int skip;

        PublisherBufferSkipSubscriber(Subscriber<? super C> actual2, int size2, int skip2, Callable<C> bufferSupplier2) {
            this.actual = actual2;
            this.size = size2;
            this.skip = skip2;
            this.bufferSupplier = bufferSupplier2;
        }

        public void request(long n) {
            if (!SubscriptionHelper.validate(n)) {
                return;
            }
            if (get() != 0 || !compareAndSet(0, 1)) {
                this.s.request(BackpressureHelper.multiplyCap((long) this.skip, n));
                return;
            }
            this.s.request(BackpressureHelper.addCap(BackpressureHelper.multiplyCap(n, (long) this.size), BackpressureHelper.multiplyCap((long) (this.skip - this.size), n - 1)));
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            int i;
            if (!this.done) {
                C b = this.buffer;
                int i2 = this.index;
                int i3 = i2 + 1;
                if (i2 == 0) {
                    try {
                        b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                        this.buffer = b;
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
                        onError(e);
                        return;
                    }
                }
                if (b != null) {
                    b.add(t);
                    if (b.size() == this.size) {
                        this.buffer = null;
                        this.actual.onNext(b);
                    }
                }
                if (i3 == this.skip) {
                    i = 0;
                } else {
                    i = i3;
                }
                this.index = i;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.buffer = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                C b = this.buffer;
                this.buffer = null;
                if (b != null) {
                    this.actual.onNext(b);
                }
                this.actual.onComplete();
            }
        }
    }

    static final class PublisherBufferOverlappingSubscriber<T, C extends Collection<? super T>> extends AtomicLong implements Subscriber<T>, Subscription, BooleanSupplier {
        private static final long serialVersionUID = -7370244972039324525L;
        final Subscriber<? super C> actual;
        final Callable<C> bufferSupplier;
        final ArrayDeque<C> buffers = new ArrayDeque<>();
        volatile boolean cancelled;
        boolean done;
        int index;
        final AtomicBoolean once = new AtomicBoolean();
        long produced;
        Subscription s;
        final int size;
        final int skip;

        PublisherBufferOverlappingSubscriber(Subscriber<? super C> actual2, int size2, int skip2, Callable<C> bufferSupplier2) {
            this.actual = actual2;
            this.size = size2;
            this.skip = skip2;
            this.bufferSupplier = bufferSupplier2;
        }

        public boolean getAsBoolean() {
            return this.cancelled;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                if (!QueueDrainHelper.postCompleteRequest(n, this.actual, this.buffers, this, this)) {
                    if (this.once.get() || !this.once.compareAndSet(false, true)) {
                        this.s.request(BackpressureHelper.multiplyCap((long) this.skip, n));
                        return;
                    }
                    this.s.request(BackpressureHelper.addCap((long) this.size, BackpressureHelper.multiplyCap((long) this.skip, n - 1)));
                }
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            int i;
            if (!this.done) {
                ArrayDeque<C> bs = this.buffers;
                int i2 = this.index;
                int i3 = i2 + 1;
                if (i2 == 0) {
                    try {
                        bs.offer((Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer"));
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
                        onError(e);
                        return;
                    }
                }
                C b = (Collection) bs.peek();
                if (b != null && b.size() + 1 == this.size) {
                    bs.poll();
                    b.add(t);
                    this.produced++;
                    this.actual.onNext(b);
                }
                Iterator i$ = bs.iterator();
                while (i$.hasNext()) {
                    ((Collection) i$.next()).add(t);
                }
                if (i3 == this.skip) {
                    i = 0;
                } else {
                    i = i3;
                }
                this.index = i;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.buffers.clear();
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                long p = this.produced;
                if (p != 0) {
                    BackpressureHelper.produced(this, p);
                }
                QueueDrainHelper.postComplete(this.actual, this.buffers, this, this);
            }
        }
    }
}
