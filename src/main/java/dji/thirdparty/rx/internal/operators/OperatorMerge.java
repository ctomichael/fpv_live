package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.CompositeException;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.exceptions.OnErrorThrowable;
import dji.thirdparty.rx.internal.util.RxRingBuffer;
import dji.thirdparty.rx.internal.util.ScalarSynchronousObservable;
import dji.thirdparty.rx.internal.util.atomic.SpscAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.atomic.SpscExactAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.Pow2;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorMerge<T> implements Observable.Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    private static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(false, Integer.MAX_VALUE);

        private HolderNoDelay() {
        }
    }

    private static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(true, Integer.MAX_VALUE);

        private HolderDelayErrors() {
        }
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors2) {
        if (delayErrors2) {
            return HolderDelayErrors.INSTANCE;
        }
        return HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors2, int maxConcurrent2) {
        if (maxConcurrent2 <= 0) {
            throw new IllegalArgumentException("maxConcurrent > 0 required but it was " + maxConcurrent2);
        } else if (maxConcurrent2 == Integer.MAX_VALUE) {
            return instance(delayErrors2);
        } else {
            return new OperatorMerge<>(delayErrors2, maxConcurrent2);
        }
    }

    OperatorMerge(boolean delayErrors2, int maxConcurrent2) {
        this.delayErrors = delayErrors2;
        this.maxConcurrent = maxConcurrent2;
    }

    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> child) {
        MergeSubscriber<T> subscriber = new MergeSubscriber<>(child, this.delayErrors, this.maxConcurrent);
        MergeProducer<T> producer = new MergeProducer<>(subscriber);
        subscriber.producer = producer;
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }

    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void request(long n) {
            if (n > 0) {
                if (get() != LongCompanionObject.MAX_VALUE) {
                    BackpressureUtils.getAndAddRequest(this, n);
                    this.subscriber.emit();
                }
            } else if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
        }

        public long produced(int n) {
            return addAndGet((long) (-n));
        }
    }

    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY = new InnerSubscriber[0];
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        final Object innerGuard = new Object();
        volatile InnerSubscriber<?>[] innerSubscribers = EMPTY;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        final NotificationLite<T> nl = NotificationLite.instance();
        MergeProducer<T> producer;
        volatile Queue<Object> queue;
        int scalarEmissionCount;
        final int scalarEmissionLimit;
        volatile CompositeSubscription subscriptions;
        long uniqueId;

        public /* bridge */ /* synthetic */ void onNext(Object x0) {
            onNext((Observable) ((Observable) x0));
        }

        public MergeSubscriber(Subscriber<? super T> child2, boolean delayErrors2, int maxConcurrent2) {
            this.child = child2;
            this.delayErrors = delayErrors2;
            this.maxConcurrent = maxConcurrent2;
            if (maxConcurrent2 == Integer.MAX_VALUE) {
                this.scalarEmissionLimit = Integer.MAX_VALUE;
                request(LongCompanionObject.MAX_VALUE);
                return;
            }
            this.scalarEmissionLimit = Math.max(1, maxConcurrent2 >> 1);
            request((long) maxConcurrent2);
        }

        /* access modifiers changed from: package-private */
        public Queue<Throwable> getOrCreateErrorQueue() {
            ConcurrentLinkedQueue<Throwable> q = this.errors;
            if (q == null) {
                synchronized (this) {
                    try {
                        q = this.errors;
                        if (q == null) {
                            ConcurrentLinkedQueue<Throwable> q2 = new ConcurrentLinkedQueue<>();
                            try {
                                this.errors = q2;
                                q = q2;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            }
            return q;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0014, code lost:
            if (r2 == false) goto L_0x0019;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0016, code lost:
            add(r0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public dji.thirdparty.rx.subscriptions.CompositeSubscription getOrCreateComposite() {
            /*
                r4 = this;
                dji.thirdparty.rx.subscriptions.CompositeSubscription r0 = r4.subscriptions
                if (r0 != 0) goto L_0x0019
                r2 = 0
                monitor-enter(r4)
                dji.thirdparty.rx.subscriptions.CompositeSubscription r0 = r4.subscriptions     // Catch:{ all -> 0x001a }
                if (r0 != 0) goto L_0x0013
                dji.thirdparty.rx.subscriptions.CompositeSubscription r1 = new dji.thirdparty.rx.subscriptions.CompositeSubscription     // Catch:{ all -> 0x001a }
                r1.<init>()     // Catch:{ all -> 0x001a }
                r4.subscriptions = r1     // Catch:{ all -> 0x001d }
                r2 = 1
                r0 = r1
            L_0x0013:
                monitor-exit(r4)     // Catch:{ all -> 0x001a }
                if (r2 == 0) goto L_0x0019
                r4.add(r0)
            L_0x0019:
                return r0
            L_0x001a:
                r3 = move-exception
            L_0x001b:
                monitor-exit(r4)     // Catch:{ all -> 0x001a }
                throw r3
            L_0x001d:
                r3 = move-exception
                r0 = r1
                goto L_0x001b
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMerge.MergeSubscriber.getOrCreateComposite():dji.thirdparty.rx.subscriptions.CompositeSubscription");
        }

        public void onNext(Observable<? extends T> t) {
            if (t != null) {
                if (t instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) t).get());
                    return;
                }
                long j = this.uniqueId;
                this.uniqueId = 1 + j;
                InnerSubscriber<T> inner = new InnerSubscriber<>(this, j);
                addInner(inner);
                t.unsafeSubscribe(inner);
                emit();
            }
        }

        private void reportError() {
            List<Throwable> list = new ArrayList<>(this.errors);
            if (list.size() == 1) {
                this.child.onError((Throwable) list.get(0));
            } else {
                this.child.onError(new CompositeException(list));
            }
        }

        public void onError(Throwable e) {
            getOrCreateErrorQueue().offer(e);
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: dji.thirdparty.rx.internal.operators.OperatorMerge$InnerSubscriber<?>[]} */
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void addInner(dji.thirdparty.rx.internal.operators.OperatorMerge.InnerSubscriber<T> r7) {
            /*
                r6 = this;
                dji.thirdparty.rx.subscriptions.CompositeSubscription r3 = r6.getOrCreateComposite()
                r3.add(r7)
                java.lang.Object r4 = r6.innerGuard
                monitor-enter(r4)
                dji.thirdparty.rx.internal.operators.OperatorMerge$InnerSubscriber<?>[] r0 = r6.innerSubscribers     // Catch:{ all -> 0x001c }
                int r2 = r0.length     // Catch:{ all -> 0x001c }
                int r3 = r2 + 1
                dji.thirdparty.rx.internal.operators.OperatorMerge$InnerSubscriber[] r1 = new dji.thirdparty.rx.internal.operators.OperatorMerge.InnerSubscriber[r3]     // Catch:{ all -> 0x001c }
                r3 = 0
                r5 = 0
                java.lang.System.arraycopy(r0, r3, r1, r5, r2)     // Catch:{ all -> 0x001c }
                r1[r2] = r7     // Catch:{ all -> 0x001c }
                r6.innerSubscribers = r1     // Catch:{ all -> 0x001c }
                monitor-exit(r4)     // Catch:{ all -> 0x001c }
                return
            L_0x001c:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x001c }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMerge.MergeSubscriber.addInner(dji.thirdparty.rx.internal.operators.OperatorMerge$InnerSubscriber):void");
        }

        /* access modifiers changed from: package-private */
        public void removeInner(InnerSubscriber<T> inner) {
            RxRingBuffer q = inner.queue;
            if (q != null) {
                q.release();
            }
            this.subscriptions.remove(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (inner.equals(a[i])) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        this.innerSubscribers = EMPTY;
                        return;
                    }
                    InnerSubscriber<?>[] b = new InnerSubscriber[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.innerSubscribers = b;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tryEmit(InnerSubscriber<T> subscriber, T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!this.emitting && r != 0) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(subscriber, value, r);
            } else {
                queueScalar(subscriber, value);
            }
        }

        /* access modifiers changed from: protected */
        public void queueScalar(InnerSubscriber<T> subscriber, T value) {
            RxRingBuffer q = subscriber.queue;
            if (q == null) {
                q = RxRingBuffer.getSpscInstance();
                subscriber.add(q);
                subscriber.queue = q;
            }
            try {
                q.onNext(this.nl.next(value));
                emit();
            } catch (MissingBackpressureException ex) {
                subscriber.unsubscribe();
                subscriber.onError(ex);
            } catch (IllegalStateException ex2) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                    subscriber.onError(ex2);
                }
            }
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0024, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0026, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002a, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x005e, code lost:
            if (1 != 0) goto L_0x0065;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0060, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x0064, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x0065, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitScalar(dji.thirdparty.rx.internal.operators.OperatorMerge.InnerSubscriber<T> r6, T r7, long r8) {
            /*
                r5 = this;
                r0 = 0
                dji.thirdparty.rx.Subscriber<? super T> r2 = r5.child     // Catch:{ Throwable -> 0x002c }
                r2.onNext(r7)     // Catch:{ Throwable -> 0x002c }
            L_0x0006:
                r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
                if (r2 == 0) goto L_0x0015
                dji.thirdparty.rx.internal.operators.OperatorMerge$MergeProducer<T> r2 = r5.producer     // Catch:{ all -> 0x004e }
                r3 = 1
                r2.produced(r3)     // Catch:{ all -> 0x004e }
            L_0x0015:
                r2 = 1
                r6.requestMore(r2)     // Catch:{ all -> 0x004e }
                monitor-enter(r5)     // Catch:{ all -> 0x004e }
                r0 = 1
                boolean r2 = r5.missed     // Catch:{ all -> 0x0069 }
                if (r2 != 0) goto L_0x005a
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0069 }
                monitor-exit(r5)     // Catch:{ all -> 0x0069 }
                if (r0 != 0) goto L_0x002b
                monitor-enter(r5)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0057 }
                monitor-exit(r5)     // Catch:{ all -> 0x0057 }
            L_0x002b:
                return
            L_0x002c:
                r1 = move-exception
                boolean r2 = r5.delayErrors     // Catch:{ all -> 0x004e }
                if (r2 != 0) goto L_0x0046
                dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r1)     // Catch:{ all -> 0x004e }
                r0 = 1
                r6.unsubscribe()     // Catch:{ all -> 0x004e }
                r6.onError(r1)     // Catch:{ all -> 0x004e }
                if (r0 != 0) goto L_0x002b
                monitor-enter(r5)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0043 }
                monitor-exit(r5)     // Catch:{ all -> 0x0043 }
                goto L_0x002b
            L_0x0043:
                r2 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0043 }
                throw r2
            L_0x0046:
                java.util.Queue r2 = r5.getOrCreateErrorQueue()     // Catch:{ all -> 0x004e }
                r2.offer(r1)     // Catch:{ all -> 0x004e }
                goto L_0x0006
            L_0x004e:
                r2 = move-exception
                if (r0 != 0) goto L_0x0056
                monitor-enter(r5)
                r3 = 0
                r5.emitting = r3     // Catch:{ all -> 0x006f }
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
            L_0x0056:
                throw r2
            L_0x0057:
                r2 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0057 }
                throw r2
            L_0x005a:
                r2 = 0
                r5.missed = r2     // Catch:{ all -> 0x0069 }
                monitor-exit(r5)     // Catch:{ all -> 0x0069 }
                if (r0 != 0) goto L_0x0065
                monitor-enter(r5)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x006c }
                monitor-exit(r5)     // Catch:{ all -> 0x006c }
            L_0x0065:
                r5.emitLoop()
                goto L_0x002b
            L_0x0069:
                r2 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0069 }
                throw r2     // Catch:{ all -> 0x004e }
            L_0x006c:
                r2 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x006c }
                throw r2
            L_0x006f:
                r2 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(dji.thirdparty.rx.internal.operators.OperatorMerge$InnerSubscriber, java.lang.Object, long):void");
        }

        public void requestMore(long n) {
            request(n);
        }

        /* access modifiers changed from: package-private */
        public void tryEmit(T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!this.emitting && r != 0) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(value, r);
            } else {
                queueScalar(value);
            }
        }

        /* access modifiers changed from: protected */
        public void queueScalar(T value) {
            Queue<Object> q = this.queue;
            if (q == null) {
                int mc = this.maxConcurrent;
                if (mc == Integer.MAX_VALUE) {
                    q = new SpscUnboundedAtomicArrayQueue<>(RxRingBuffer.SIZE);
                } else if (!Pow2.isPowerOfTwo(mc)) {
                    q = new SpscExactAtomicArrayQueue<>(mc);
                } else if (UnsafeAccess.isUnsafeAvailable()) {
                    q = new SpscArrayQueue<>(mc);
                } else {
                    q = new SpscAtomicArrayQueue<>(mc);
                }
                this.queue = q;
            }
            if (!q.offer(this.nl.next(value))) {
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(new MissingBackpressureException(), value));
                return;
            }
            emit();
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0034, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x006b, code lost:
            if (1 != 0) goto L_0x0072;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x006d, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x0071, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x0072, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitScalar(T r7, long r8) {
            /*
                r6 = this;
                r1 = 0
                dji.thirdparty.rx.Subscriber<? super T> r3 = r6.child     // Catch:{ Throwable -> 0x0036 }
                r3.onNext(r7)     // Catch:{ Throwable -> 0x0036 }
            L_0x0006:
                r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r3 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
                if (r3 == 0) goto L_0x0015
                dji.thirdparty.rx.internal.operators.OperatorMerge$MergeProducer<T> r3 = r6.producer     // Catch:{ all -> 0x0058 }
                r4 = 1
                r3.produced(r4)     // Catch:{ all -> 0x0058 }
            L_0x0015:
                int r3 = r6.scalarEmissionCount     // Catch:{ all -> 0x0058 }
                int r0 = r3 + 1
                int r3 = r6.scalarEmissionLimit     // Catch:{ all -> 0x0058 }
                if (r0 != r3) goto L_0x0061
                r3 = 0
                r6.scalarEmissionCount = r3     // Catch:{ all -> 0x0058 }
                long r4 = (long) r0     // Catch:{ all -> 0x0058 }
                r6.requestMore(r4)     // Catch:{ all -> 0x0058 }
            L_0x0024:
                monitor-enter(r6)     // Catch:{ all -> 0x0058 }
                r1 = 1
                boolean r3 = r6.missed     // Catch:{ all -> 0x0076 }
                if (r3 != 0) goto L_0x0067
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0076 }
                monitor-exit(r6)     // Catch:{ all -> 0x0076 }
                if (r1 != 0) goto L_0x0035
                monitor-enter(r6)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0064 }
                monitor-exit(r6)     // Catch:{ all -> 0x0064 }
            L_0x0035:
                return
            L_0x0036:
                r2 = move-exception
                boolean r3 = r6.delayErrors     // Catch:{ all -> 0x0058 }
                if (r3 != 0) goto L_0x0050
                dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r2)     // Catch:{ all -> 0x0058 }
                r1 = 1
                r6.unsubscribe()     // Catch:{ all -> 0x0058 }
                r6.onError(r2)     // Catch:{ all -> 0x0058 }
                if (r1 != 0) goto L_0x0035
                monitor-enter(r6)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x004d }
                monitor-exit(r6)     // Catch:{ all -> 0x004d }
                goto L_0x0035
            L_0x004d:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x004d }
                throw r3
            L_0x0050:
                java.util.Queue r3 = r6.getOrCreateErrorQueue()     // Catch:{ all -> 0x0058 }
                r3.offer(r2)     // Catch:{ all -> 0x0058 }
                goto L_0x0006
            L_0x0058:
                r3 = move-exception
                if (r1 != 0) goto L_0x0060
                monitor-enter(r6)
                r4 = 0
                r6.emitting = r4     // Catch:{ all -> 0x007c }
                monitor-exit(r6)     // Catch:{ all -> 0x007c }
            L_0x0060:
                throw r3
            L_0x0061:
                r6.scalarEmissionCount = r0     // Catch:{ all -> 0x0058 }
                goto L_0x0024
            L_0x0064:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0064 }
                throw r3
            L_0x0067:
                r3 = 0
                r6.missed = r3     // Catch:{ all -> 0x0076 }
                monitor-exit(r6)     // Catch:{ all -> 0x0076 }
                if (r1 != 0) goto L_0x0072
                monitor-enter(r6)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0079 }
                monitor-exit(r6)     // Catch:{ all -> 0x0079 }
            L_0x0072:
                r6.emitLoop()
                goto L_0x0035
            L_0x0076:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0076 }
                throw r3     // Catch:{ all -> 0x0058 }
            L_0x0079:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0079 }
                throw r3
            L_0x007c:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x007c }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(java.lang.Object, long):void");
        }

        /* access modifiers changed from: package-private */
        public void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:231:0x0295, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:232:0x0297, code lost:
            monitor-enter(r32);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:235:?, code lost:
            r32.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:236:0x02a0, code lost:
            monitor-exit(r32);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:307:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:308:?, code lost:
            return;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            /*
                r32 = this;
                r23 = 0
                r0 = r32
                dji.thirdparty.rx.Subscriber<? super T> r4 = r0.child     // Catch:{ all -> 0x0106 }
            L_0x0006:
                boolean r30 = r32.checkTerminate()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x001e
                r23 = 1
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x001b }
                monitor-exit(r32)     // Catch:{ all -> 0x001b }
            L_0x001a:
                return
            L_0x001b:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x001b }
                throw r30
            L_0x001e:
                r0 = r32
                java.util.Queue<java.lang.Object> r0 = r0.queue     // Catch:{ all -> 0x0106 }
                r26 = r0
                r0 = r32
                dji.thirdparty.rx.internal.operators.OperatorMerge$MergeProducer<T> r0 = r0.producer     // Catch:{ all -> 0x0106 }
                r30 = r0
                long r20 = r30.get()     // Catch:{ all -> 0x0106 }
                r30 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1))
                if (r30 != 0) goto L_0x0063
                r28 = 1
            L_0x0039:
                r19 = 0
                if (r26 == 0) goto L_0x0079
            L_0x003d:
                r22 = 0
                r16 = 0
            L_0x0041:
                r30 = 0
                int r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1))
                if (r30 <= 0) goto L_0x0068
                java.lang.Object r16 = r26.poll()     // Catch:{ all -> 0x0106 }
                boolean r30 = r32.checkTerminate()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x0066
                r23 = 1
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x0060 }
                monitor-exit(r32)     // Catch:{ all -> 0x0060 }
                goto L_0x001a
            L_0x0060:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x0060 }
                throw r30
            L_0x0063:
                r28 = 0
                goto L_0x0039
            L_0x0066:
                if (r16 != 0) goto L_0x00b6
            L_0x0068:
                if (r22 <= 0) goto L_0x0071
                if (r28 == 0) goto L_0x0114
                r20 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            L_0x0071:
                r30 = 0
                int r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1))
                if (r30 == 0) goto L_0x0079
                if (r16 != 0) goto L_0x003d
            L_0x0079:
                r0 = r32
                boolean r5 = r0.done     // Catch:{ all -> 0x0106 }
                r0 = r32
                java.util.Queue<java.lang.Object> r0 = r0.queue     // Catch:{ all -> 0x0106 }
                r26 = r0
                r0 = r32
                dji.thirdparty.rx.internal.operators.OperatorMerge$InnerSubscriber<?>[] r9 = r0.innerSubscribers     // Catch:{ all -> 0x0106 }
                int r15 = r9.length     // Catch:{ all -> 0x0106 }
                if (r5 == 0) goto L_0x0129
                if (r26 == 0) goto L_0x0092
                boolean r30 = r26.isEmpty()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x0129
            L_0x0092:
                if (r15 != 0) goto L_0x0129
                r0 = r32
                java.util.concurrent.ConcurrentLinkedQueue<java.lang.Throwable> r6 = r0.errors     // Catch:{ all -> 0x0106 }
                if (r6 == 0) goto L_0x00a0
                boolean r30 = r6.isEmpty()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x0124
            L_0x00a0:
                r4.onCompleted()     // Catch:{ all -> 0x0106 }
            L_0x00a3:
                r23 = 1
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x00b3 }
                monitor-exit(r32)     // Catch:{ all -> 0x00b3 }
                goto L_0x001a
            L_0x00b3:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x00b3 }
                throw r30
            L_0x00b6:
                r0 = r32
                dji.thirdparty.rx.internal.operators.NotificationLite<T> r0 = r0.nl     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r1 = r16
                java.lang.Object r29 = r0.getValue(r1)     // Catch:{ all -> 0x0106 }
                r0 = r29
                r4.onNext(r0)     // Catch:{ Throwable -> 0x00d3 }
            L_0x00c9:
                int r19 = r19 + 1
                int r22 = r22 + 1
                r30 = 1
                long r20 = r20 - r30
                goto L_0x0041
            L_0x00d3:
                r27 = move-exception
                r0 = r32
                boolean r0 = r0.delayErrors     // Catch:{ all -> 0x0106 }
                r30 = r0
                if (r30 != 0) goto L_0x00fa
                dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r27)     // Catch:{ all -> 0x0106 }
                r23 = 1
                r32.unsubscribe()     // Catch:{ all -> 0x0106 }
                r0 = r27
                r4.onError(r0)     // Catch:{ all -> 0x0106 }
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x00f7 }
                monitor-exit(r32)     // Catch:{ all -> 0x00f7 }
                goto L_0x001a
            L_0x00f7:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x00f7 }
                throw r30
            L_0x00fa:
                java.util.Queue r30 = r32.getOrCreateErrorQueue()     // Catch:{ all -> 0x0106 }
                r0 = r30
                r1 = r27
                r0.offer(r1)     // Catch:{ all -> 0x0106 }
                goto L_0x00c9
            L_0x0106:
                r30 = move-exception
                if (r23 != 0) goto L_0x0113
                monitor-enter(r32)
                r31 = 0
                r0 = r31
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x02bd }
                monitor-exit(r32)     // Catch:{ all -> 0x02bd }
            L_0x0113:
                throw r30
            L_0x0114:
                r0 = r32
                dji.thirdparty.rx.internal.operators.OperatorMerge$MergeProducer<T> r0 = r0.producer     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r1 = r22
                long r20 = r0.produced(r1)     // Catch:{ all -> 0x0106 }
                goto L_0x0071
            L_0x0124:
                r32.reportError()     // Catch:{ all -> 0x0106 }
                goto L_0x00a3
            L_0x0129:
                r10 = 0
                if (r15 <= 0) goto L_0x0271
                r0 = r32
                long r0 = r0.lastId     // Catch:{ all -> 0x0106 }
                r24 = r0
                r0 = r32
                int r8 = r0.lastIndex     // Catch:{ all -> 0x0106 }
                if (r15 <= r8) goto L_0x0144
                r30 = r9[r8]     // Catch:{ all -> 0x0106 }
                r0 = r30
                long r0 = r0.id     // Catch:{ all -> 0x0106 }
                r30 = r0
                int r30 = (r30 > r24 ? 1 : (r30 == r24 ? 0 : -1))
                if (r30 == 0) goto L_0x016a
            L_0x0144:
                if (r15 > r8) goto L_0x0147
                r8 = 0
            L_0x0147:
                r14 = r8
                r7 = 0
            L_0x0149:
                if (r7 >= r15) goto L_0x0157
                r30 = r9[r14]     // Catch:{ all -> 0x0106 }
                r0 = r30
                long r0 = r0.id     // Catch:{ all -> 0x0106 }
                r30 = r0
                int r30 = (r30 > r24 ? 1 : (r30 == r24 ? 0 : -1))
                if (r30 != 0) goto L_0x0187
            L_0x0157:
                r8 = r14
                r0 = r32
                r0.lastIndex = r14     // Catch:{ all -> 0x0106 }
                r30 = r9[r14]     // Catch:{ all -> 0x0106 }
                r0 = r30
                long r0 = r0.id     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r2 = r32
                r2.lastId = r0     // Catch:{ all -> 0x0106 }
            L_0x016a:
                r14 = r8
                r7 = 0
            L_0x016c:
                if (r7 >= r15) goto L_0x025f
                boolean r30 = r32.checkTerminate()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x018f
                r23 = 1
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x0184 }
                monitor-exit(r32)     // Catch:{ all -> 0x0184 }
                goto L_0x001a
            L_0x0184:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x0184 }
                throw r30
            L_0x0187:
                int r14 = r14 + 1
                if (r14 != r15) goto L_0x018c
                r14 = 0
            L_0x018c:
                int r7 = r7 + 1
                goto L_0x0149
            L_0x018f:
                r13 = r9[r14]     // Catch:{ all -> 0x0106 }
                r16 = 0
            L_0x0193:
                r17 = 0
            L_0x0195:
                r30 = 0
                int r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1))
                if (r30 <= 0) goto L_0x01ba
                boolean r30 = r32.checkTerminate()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x01b4
                r23 = 1
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x01b1 }
                monitor-exit(r32)     // Catch:{ all -> 0x01b1 }
                goto L_0x001a
            L_0x01b1:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x01b1 }
                throw r30
            L_0x01b4:
                dji.thirdparty.rx.internal.util.RxRingBuffer r0 = r13.queue     // Catch:{ all -> 0x0106 }
                r18 = r0
                if (r18 != 0) goto L_0x020a
            L_0x01ba:
                if (r17 <= 0) goto L_0x01d6
                if (r28 != 0) goto L_0x024f
                r0 = r32
                dji.thirdparty.rx.internal.operators.OperatorMerge$MergeProducer<T> r0 = r0.producer     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r1 = r17
                long r20 = r0.produced(r1)     // Catch:{ all -> 0x0106 }
            L_0x01cc:
                r0 = r17
                long r0 = (long) r0     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r13.requestMore(r0)     // Catch:{ all -> 0x0106 }
            L_0x01d6:
                r30 = 0
                int r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1))
                if (r30 == 0) goto L_0x01de
                if (r16 != 0) goto L_0x0193
            L_0x01de:
                boolean r11 = r13.done     // Catch:{ all -> 0x0106 }
                dji.thirdparty.rx.internal.util.RxRingBuffer r12 = r13.queue     // Catch:{ all -> 0x0106 }
                if (r11 == 0) goto L_0x0259
                if (r12 == 0) goto L_0x01ec
                boolean r30 = r12.isEmpty()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x0259
            L_0x01ec:
                r0 = r32
                r0.removeInner(r13)     // Catch:{ all -> 0x0106 }
                boolean r30 = r32.checkTerminate()     // Catch:{ all -> 0x0106 }
                if (r30 == 0) goto L_0x0256
                r23 = 1
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x0207 }
                monitor-exit(r32)     // Catch:{ all -> 0x0207 }
                goto L_0x001a
            L_0x0207:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x0207 }
                throw r30
            L_0x020a:
                java.lang.Object r16 = r18.poll()     // Catch:{ all -> 0x0106 }
                if (r16 == 0) goto L_0x01ba
                r0 = r32
                dji.thirdparty.rx.internal.operators.NotificationLite<T> r0 = r0.nl     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r1 = r16
                java.lang.Object r29 = r0.getValue(r1)     // Catch:{ all -> 0x0106 }
                r0 = r29
                r4.onNext(r0)     // Catch:{ Throwable -> 0x022b }
                r30 = 1
                long r20 = r20 - r30
                int r17 = r17 + 1
                goto L_0x0195
            L_0x022b:
                r27 = move-exception
                r23 = 1
                dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r27)     // Catch:{ all -> 0x0106 }
                r0 = r27
                r4.onError(r0)     // Catch:{ all -> 0x024a }
                r32.unsubscribe()     // Catch:{ all -> 0x0106 }
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x0247 }
                monitor-exit(r32)     // Catch:{ all -> 0x0247 }
                goto L_0x001a
            L_0x0247:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x0247 }
                throw r30
            L_0x024a:
                r30 = move-exception
                r32.unsubscribe()     // Catch:{ all -> 0x0106 }
                throw r30     // Catch:{ all -> 0x0106 }
            L_0x024f:
                r20 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                goto L_0x01cc
            L_0x0256:
                int r19 = r19 + 1
                r10 = 1
            L_0x0259:
                r30 = 0
                int r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1))
                if (r30 != 0) goto L_0x02a6
            L_0x025f:
                r0 = r32
                r0.lastIndex = r14     // Catch:{ all -> 0x0106 }
                r30 = r9[r14]     // Catch:{ all -> 0x0106 }
                r0 = r30
                long r0 = r0.id     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r30
                r2 = r32
                r2.lastId = r0     // Catch:{ all -> 0x0106 }
            L_0x0271:
                if (r19 <= 0) goto L_0x027f
                r0 = r19
                long r0 = (long) r0     // Catch:{ all -> 0x0106 }
                r30 = r0
                r0 = r32
                r1 = r30
                r0.request(r1)     // Catch:{ all -> 0x0106 }
            L_0x027f:
                if (r10 != 0) goto L_0x0006
                monitor-enter(r32)     // Catch:{ all -> 0x0106 }
                r0 = r32
                boolean r0 = r0.missed     // Catch:{ all -> 0x02ba }
                r30 = r0
                if (r30 != 0) goto L_0x02af
                r23 = 1
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x02ba }
                monitor-exit(r32)     // Catch:{ all -> 0x02ba }
                if (r23 != 0) goto L_0x001a
                monitor-enter(r32)
                r30 = 0
                r0 = r30
                r1 = r32
                r1.emitting = r0     // Catch:{ all -> 0x02a3 }
                monitor-exit(r32)     // Catch:{ all -> 0x02a3 }
                goto L_0x001a
            L_0x02a3:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x02a3 }
                throw r30
            L_0x02a6:
                int r14 = r14 + 1
                if (r14 != r15) goto L_0x02ab
                r14 = 0
            L_0x02ab:
                int r7 = r7 + 1
                goto L_0x016c
            L_0x02af:
                r30 = 0
                r0 = r30
                r1 = r32
                r1.missed = r0     // Catch:{ all -> 0x02ba }
                monitor-exit(r32)     // Catch:{ all -> 0x02ba }
                goto L_0x0006
            L_0x02ba:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x02ba }
                throw r30     // Catch:{ all -> 0x0106 }
            L_0x02bd:
                r30 = move-exception
                monitor-exit(r32)     // Catch:{ all -> 0x02bd }
                throw r30
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMerge.MergeSubscriber.emitLoop():void");
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            Queue<Throwable> e = this.errors;
            if (this.delayErrors || e == null || e.isEmpty()) {
                return false;
            }
            try {
                reportError();
                return true;
            } finally {
                unsubscribe();
            }
        }
    }

    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int limit = (RxRingBuffer.SIZE / 4);
        volatile boolean done;
        final long id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        public InnerSubscriber(MergeSubscriber<T> parent2, long id2) {
            this.parent = parent2;
            this.id = id2;
        }

        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        public void onError(Throwable e) {
            this.done = true;
            this.parent.getOrCreateErrorQueue().offer(e);
            this.parent.emit();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void requestMore(long n) {
            int r = this.outstanding - ((int) n);
            if (r > limit) {
                this.outstanding = r;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            int k = RxRingBuffer.SIZE - r;
            if (k > 0) {
                request((long) k);
            }
        }
    }
}
