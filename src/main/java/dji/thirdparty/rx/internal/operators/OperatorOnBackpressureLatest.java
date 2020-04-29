package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorOnBackpressureLatest<T> implements Observable.Operator<T, T> {
    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    static final class Holder {
        static final OperatorOnBackpressureLatest<Object> INSTANCE = new OperatorOnBackpressureLatest<>();

        Holder() {
        }
    }

    public static <T> OperatorOnBackpressureLatest<T> instance() {
        return Holder.INSTANCE;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        LatestEmitter<T> producer = new LatestEmitter<>(child);
        LatestSubscriber<T> parent = new LatestSubscriber<>(producer);
        producer.parent = parent;
        child.add(parent);
        child.add(producer);
        child.setProducer(producer);
        return parent;
    }

    static final class LatestEmitter<T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        static final Object EMPTY = new Object();
        static final long NOT_REQUESTED = -4611686018427387904L;
        private static final long serialVersionUID = -1364393685005146274L;
        final Subscriber<? super T> child;
        volatile boolean done;
        boolean emitting;
        boolean missed;
        LatestSubscriber<? super T> parent;
        Throwable terminal;
        final AtomicReference<Object> value = new AtomicReference<>(EMPTY);

        public LatestEmitter(Subscriber<? super T> child2) {
            this.child = child2;
            lazySet(NOT_REQUESTED);
        }

        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r != Long.MIN_VALUE) {
                        if (r == NOT_REQUESTED) {
                            u = n;
                        } else {
                            u = r + n;
                            if (u < 0) {
                                u = LongCompanionObject.MAX_VALUE;
                            }
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                if (r == NOT_REQUESTED) {
                    this.parent.requestMore(LongCompanionObject.MAX_VALUE);
                }
                emit();
            }
        }

        /* access modifiers changed from: package-private */
        public long produced(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r < 0) {
                    return r;
                }
                u = r - n;
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        public void unsubscribe() {
            if (get() >= 0) {
                getAndSet(Long.MIN_VALUE);
            }
        }

        public void onNext(T t) {
            this.value.lazySet(t);
            emit();
        }

        public void onError(Throwable e) {
            this.terminal = e;
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            r2 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
            if (r2 != Long.MIN_VALUE) goto L_0x002b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
            if (r1 != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x001f, code lost:
            monitor-enter(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0023, code lost:
            monitor-exit(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            r4 = r8.value.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0035, code lost:
            if (r2 <= 0) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0039, code lost:
            if (r4 == dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x003b, code lost:
            r8.child.onNext(r4);
            r8.value.compareAndSet(r4, dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY);
            produced(1);
            r4 = dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0051, code lost:
            if (r4 != dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY) goto L_0x0060;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0055, code lost:
            if (r8.done == false) goto L_0x0060;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0057, code lost:
            r0 = r8.terminal;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0059, code lost:
            if (r0 == null) goto L_0x0077;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x005b, code lost:
            r8.child.onError(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0060, code lost:
            monitor-enter(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0063, code lost:
            if (r8.missed != false) goto L_0x007d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0065, code lost:
            r8.emitting = false;
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0069, code lost:
            monitor-exit(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x006e, code lost:
            r6 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x006f, code lost:
            if (0 == 0) goto L_0x0071;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0071, code lost:
            monitor-enter(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x0076, code lost:
            throw r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            r8.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
            r8.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x0080, code lost:
            monitor-exit(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit() {
            /*
                r8 = this;
                monitor-enter(r8)
                boolean r6 = r8.emitting     // Catch:{ all -> 0x0028 }
                if (r6 == 0) goto L_0x000a
                r6 = 1
                r8.missed = r6     // Catch:{ all -> 0x0028 }
                monitor-exit(r8)     // Catch:{ all -> 0x0028 }
            L_0x0009:
                return
            L_0x000a:
                r6 = 1
                r8.emitting = r6     // Catch:{ all -> 0x0028 }
                r6 = 0
                r8.missed = r6     // Catch:{ all -> 0x0028 }
                monitor-exit(r8)     // Catch:{ all -> 0x0028 }
                r1 = 0
            L_0x0012:
                long r2 = r8.get()     // Catch:{ all -> 0x006e }
                r6 = -9223372036854775808
                int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r6 != 0) goto L_0x002b
                r1 = 1
            L_0x001d:
                if (r1 != 0) goto L_0x0009
                monitor-enter(r8)
                r6 = 0
                r8.emitting = r6     // Catch:{ all -> 0x0025 }
                monitor-exit(r8)     // Catch:{ all -> 0x0025 }
                goto L_0x0009
            L_0x0025:
                r6 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0025 }
                throw r6
            L_0x0028:
                r6 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0028 }
                throw r6
            L_0x002b:
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r6 = r8.value     // Catch:{ all -> 0x006e }
                java.lang.Object r4 = r6.get()     // Catch:{ all -> 0x006e }
                r6 = 0
                int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r6 <= 0) goto L_0x004f
                java.lang.Object r6 = dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch:{ all -> 0x006e }
                if (r4 == r6) goto L_0x004f
                r5 = r4
                dji.thirdparty.rx.Subscriber<? super T> r6 = r8.child     // Catch:{ all -> 0x006e }
                r6.onNext(r5)     // Catch:{ all -> 0x006e }
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r6 = r8.value     // Catch:{ all -> 0x006e }
                java.lang.Object r7 = dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch:{ all -> 0x006e }
                r6.compareAndSet(r4, r7)     // Catch:{ all -> 0x006e }
                r6 = 1
                r8.produced(r6)     // Catch:{ all -> 0x006e }
                java.lang.Object r4 = dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch:{ all -> 0x006e }
            L_0x004f:
                java.lang.Object r6 = dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch:{ all -> 0x006e }
                if (r4 != r6) goto L_0x0060
                boolean r6 = r8.done     // Catch:{ all -> 0x006e }
                if (r6 == 0) goto L_0x0060
                java.lang.Throwable r0 = r8.terminal     // Catch:{ all -> 0x006e }
                if (r0 == 0) goto L_0x0077
                dji.thirdparty.rx.Subscriber<? super T> r6 = r8.child     // Catch:{ all -> 0x006e }
                r6.onError(r0)     // Catch:{ all -> 0x006e }
            L_0x0060:
                monitor-enter(r8)     // Catch:{ all -> 0x006e }
                boolean r6 = r8.missed     // Catch:{ all -> 0x006b }
                if (r6 != 0) goto L_0x007d
                r6 = 0
                r8.emitting = r6     // Catch:{ all -> 0x006b }
                r1 = 1
                monitor-exit(r8)     // Catch:{ all -> 0x006b }
                goto L_0x001d
            L_0x006b:
                r6 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x006b }
                throw r6     // Catch:{ all -> 0x006e }
            L_0x006e:
                r6 = move-exception
                if (r1 != 0) goto L_0x0076
                monitor-enter(r8)
                r7 = 0
                r8.emitting = r7     // Catch:{ all -> 0x0082 }
                monitor-exit(r8)     // Catch:{ all -> 0x0082 }
            L_0x0076:
                throw r6
            L_0x0077:
                dji.thirdparty.rx.Subscriber<? super T> r6 = r8.child     // Catch:{ all -> 0x006e }
                r6.onCompleted()     // Catch:{ all -> 0x006e }
                goto L_0x0060
            L_0x007d:
                r6 = 0
                r8.missed = r6     // Catch:{ all -> 0x006b }
                monitor-exit(r8)     // Catch:{ all -> 0x006b }
                goto L_0x0012
            L_0x0082:
                r6 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0082 }
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.emit():void");
        }
    }

    static final class LatestSubscriber<T> extends Subscriber<T> {
        private final LatestEmitter<T> producer;

        LatestSubscriber(LatestEmitter<T> producer2) {
            this.producer = producer2;
        }

        public void onStart() {
            request(0);
        }

        public void onNext(T t) {
            this.producer.onNext(t);
        }

        public void onError(Throwable e) {
            this.producer.onError(e);
        }

        public void onCompleted() {
            this.producer.onCompleted();
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long n) {
            request(n);
        }
    }
}
