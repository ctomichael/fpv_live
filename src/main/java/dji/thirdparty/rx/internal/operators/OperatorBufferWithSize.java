package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class OperatorBufferWithSize<T> implements Observable.Operator<List<T>, T> {
    final int count;
    final int skip;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorBufferWithSize(int count2, int skip2) {
        if (count2 <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        } else if (skip2 <= 0) {
            throw new IllegalArgumentException("skip must be greater than 0");
        } else {
            this.count = count2;
            this.skip = skip2;
        }
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        if (this.skip == this.count) {
            BufferExact<T> parent = new BufferExact<>(child, this.count);
            child.add(parent);
            child.setProducer(parent.createProducer());
            return parent;
        } else if (this.skip > this.count) {
            BufferSkip<T> parent2 = new BufferSkip<>(child, this.count, this.skip);
            child.add(parent2);
            child.setProducer(parent2.createProducer());
            return parent2;
        } else {
            BufferOverlap<T> parent3 = new BufferOverlap<>(child, this.count, this.skip);
            child.add(parent3);
            child.setProducer(parent3.createProducer());
            return parent3;
        }
    }

    static final class BufferExact<T> extends Subscriber<T> {
        final Subscriber<? super List<T>> actual;
        List<T> buffer;
        final int count;

        public BufferExact(Subscriber<? super List<T>> actual2, int count2) {
            this.actual = actual2;
            this.count = count2;
            request(0);
        }

        public void onNext(T t) {
            List<T> b = this.buffer;
            if (b == null) {
                b = new ArrayList<>(this.count);
                this.buffer = b;
            }
            b.add(t);
            if (b.size() == this.count) {
                this.buffer = null;
                this.actual.onNext(b);
            }
        }

        public void onError(Throwable e) {
            this.buffer = null;
            this.actual.onError(e);
        }

        public void onCompleted() {
            List<T> b = this.buffer;
            if (b != null) {
                this.actual.onNext(b);
            }
            this.actual.onCompleted();
        }

        /* access modifiers changed from: package-private */
        public Producer createProducer() {
            return new Producer() {
                /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithSize.BufferExact.AnonymousClass1 */

                public void request(long n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("n >= required but it was " + n);
                    } else if (n != 0) {
                        BufferExact.this.request(BackpressureUtils.multiplyCap(n, (long) BufferExact.this.count));
                    }
                }
            };
        }
    }

    static final class BufferSkip<T> extends Subscriber<T> {
        final Subscriber<? super List<T>> actual;
        List<T> buffer;
        final int count;
        long index;
        final int skip;

        public BufferSkip(Subscriber<? super List<T>> actual2, int count2, int skip2) {
            this.actual = actual2;
            this.count = count2;
            this.skip = skip2;
            request(0);
        }

        public void onNext(T t) {
            long i = this.index;
            List<T> b = this.buffer;
            if (i == 0) {
                b = new ArrayList<>(this.count);
                this.buffer = b;
            }
            long i2 = i + 1;
            if (i2 == ((long) this.skip)) {
                this.index = 0;
            } else {
                this.index = i2;
            }
            if (b != null) {
                b.add(t);
                if (b.size() == this.count) {
                    this.buffer = null;
                    this.actual.onNext(b);
                }
            }
        }

        public void onError(Throwable e) {
            this.buffer = null;
            this.actual.onError(e);
        }

        public void onCompleted() {
            List<T> b = this.buffer;
            if (b != null) {
                this.buffer = null;
                this.actual.onNext(b);
            }
            this.actual.onCompleted();
        }

        /* access modifiers changed from: package-private */
        public Producer createProducer() {
            return new BufferSkipProducer();
        }

        final class BufferSkipProducer extends AtomicBoolean implements Producer {
            private static final long serialVersionUID = 3428177408082367154L;

            BufferSkipProducer() {
            }

            public void request(long n) {
                if (n < 0) {
                    throw new IllegalArgumentException("n >= 0 required but it was " + n);
                } else if (n != 0) {
                    BufferSkip<T> parent = BufferSkip.this;
                    if (get() || !compareAndSet(false, true)) {
                        parent.request(BackpressureUtils.multiplyCap(n, (long) parent.skip));
                    } else {
                        parent.request(BackpressureUtils.addCap(BackpressureUtils.multiplyCap(n, (long) parent.count), BackpressureUtils.multiplyCap((long) (parent.skip - parent.count), n - 1)));
                    }
                }
            }
        }
    }

    static final class BufferOverlap<T> extends Subscriber<T> {
        final Subscriber<? super List<T>> actual;
        final int count;
        long index;
        long produced;
        final ArrayDeque<List<T>> queue = new ArrayDeque<>();
        final AtomicLong requested = new AtomicLong();
        final int skip;

        public BufferOverlap(Subscriber<? super List<T>> actual2, int count2, int skip2) {
            this.actual = actual2;
            this.count = count2;
            this.skip = skip2;
            request(0);
        }

        public void onNext(T t) {
            long i = this.index;
            if (i == 0) {
                this.queue.offer(new ArrayList<>(this.count));
            }
            long i2 = i + 1;
            if (i2 == ((long) this.skip)) {
                this.index = 0;
            } else {
                this.index = i2;
            }
            Iterator i$ = this.queue.iterator();
            while (i$.hasNext()) {
                i$.next().add(t);
            }
            List<T> b = this.queue.peek();
            if (b != null && b.size() == this.count) {
                this.queue.poll();
                this.produced++;
                this.actual.onNext(b);
            }
        }

        public void onError(Throwable e) {
            this.queue.clear();
            this.actual.onError(e);
        }

        public void onCompleted() {
            long p = this.produced;
            if (p != 0) {
                if (p > this.requested.get()) {
                    this.actual.onError(new MissingBackpressureException("More produced than requested? " + p));
                    return;
                }
                this.requested.addAndGet(-p);
            }
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual);
        }

        /* access modifiers changed from: package-private */
        public Producer createProducer() {
            return new BufferOverlapProducer();
        }

        final class BufferOverlapProducer extends AtomicBoolean implements Producer {
            private static final long serialVersionUID = -4015894850868853147L;

            BufferOverlapProducer() {
            }

            public void request(long n) {
                BufferOverlap<T> parent = BufferOverlap.this;
                if (BackpressureUtils.postCompleteRequest(parent.requested, n, parent.queue, parent.actual) && n != 0) {
                    if (get() || !compareAndSet(false, true)) {
                        parent.request(BackpressureUtils.multiplyCap((long) parent.skip, n));
                    } else {
                        parent.request(BackpressureUtils.addCap(BackpressureUtils.multiplyCap((long) parent.skip, n - 1), (long) parent.count));
                    }
                }
            }
        }
    }
}
