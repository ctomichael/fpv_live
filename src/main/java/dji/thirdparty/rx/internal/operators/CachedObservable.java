package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.internal.util.LinkedArrayList;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class CachedObservable<T> extends Observable<T> {
    private final CacheState<T> state;

    public static <T> CachedObservable<T> from(Observable observable) {
        return from(observable, 16);
    }

    public static <T> CachedObservable<T> from(Observable observable, int capacityHint) {
        if (capacityHint < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required");
        }
        CacheState<T> state2 = new CacheState<>(observable, capacityHint);
        return new CachedObservable<>(new CachedSubscribe<>(state2), state2);
    }

    private CachedObservable(Observable.OnSubscribe<T> onSubscribe, CacheState<T> state2) {
        super(onSubscribe);
        this.state = state2;
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.state.isConnected;
    }

    /* access modifiers changed from: package-private */
    public boolean hasObservers() {
        return this.state.producers.length != 0;
    }

    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY = new ReplayProducer[0];
        final SerialSubscription connection = new SerialSubscription();
        volatile boolean isConnected;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile ReplayProducer<?>[] producers = EMPTY;
        final Observable<? extends T> source;
        boolean sourceDone;

        public CacheState(Observable<? extends T> source2, int capacityHint) {
            super(capacityHint);
            this.source = source2;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: dji.thirdparty.rx.internal.operators.CachedObservable$ReplayProducer<?>[]} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void addProducer(dji.thirdparty.rx.internal.operators.CachedObservable.ReplayProducer<T> r7) {
            /*
                r6 = this;
                dji.thirdparty.rx.subscriptions.SerialSubscription r4 = r6.connection
                monitor-enter(r4)
                dji.thirdparty.rx.internal.operators.CachedObservable$ReplayProducer<?>[] r0 = r6.producers     // Catch:{ all -> 0x0015 }
                int r2 = r0.length     // Catch:{ all -> 0x0015 }
                int r3 = r2 + 1
                dji.thirdparty.rx.internal.operators.CachedObservable$ReplayProducer[] r1 = new dji.thirdparty.rx.internal.operators.CachedObservable.ReplayProducer[r3]     // Catch:{ all -> 0x0015 }
                r3 = 0
                r5 = 0
                java.lang.System.arraycopy(r0, r3, r1, r5, r2)     // Catch:{ all -> 0x0015 }
                r1[r2] = r7     // Catch:{ all -> 0x0015 }
                r6.producers = r1     // Catch:{ all -> 0x0015 }
                monitor-exit(r4)     // Catch:{ all -> 0x0015 }
                return
            L_0x0015:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0015 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.CachedObservable.CacheState.addProducer(dji.thirdparty.rx.internal.operators.CachedObservable$ReplayProducer):void");
        }

        public void removeProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a = this.producers;
                int n = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i].equals(p)) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        this.producers = EMPTY;
                        return;
                    }
                    ReplayProducer<?>[] b = new ReplayProducer[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.producers = b;
                }
            }
        }

        public void connect() {
            Subscriber<T> subscriber = new Subscriber<T>() {
                /* class dji.thirdparty.rx.internal.operators.CachedObservable.CacheState.AnonymousClass1 */

                public void onNext(T t) {
                    CacheState.this.onNext(t);
                }

                public void onError(Throwable e) {
                    CacheState.this.onError(e);
                }

                public void onCompleted() {
                    CacheState.this.onCompleted();
                }
            };
            this.connection.set(subscriber);
            this.source.unsafeSubscribe(subscriber);
            this.isConnected = true;
        }

        public void onNext(T t) {
            if (!this.sourceDone) {
                add(this.nl.next(t));
                dispatch();
            }
        }

        public void onError(Throwable e) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.error(e));
                this.connection.unsubscribe();
                dispatch();
            }
        }

        public void onCompleted() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.completed());
                this.connection.unsubscribe();
                dispatch();
            }
        }

        /* access modifiers changed from: package-private */
        public void dispatch() {
            for (ReplayProducer<?> rp : this.producers) {
                rp.replay();
            }
        }
    }

    static final class CachedSubscribe<T> extends AtomicBoolean implements Observable.OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            call((Subscriber) ((Subscriber) x0));
        }

        public CachedSubscribe(CacheState<T> state2) {
            this.state = state2;
        }

        public void call(Subscriber<? super T> t) {
            ReplayProducer<T> rp = new ReplayProducer<>(t, this.state);
            this.state.addProducer(rp);
            t.add(rp);
            t.setProducer(rp);
            if (!get() && compareAndSet(false, true)) {
                this.state.connect();
            }
        }
    }

    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> child2, CacheState<T> state2) {
            this.child = child2;
            this.state = state2;
        }

        public void request(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r + n;
                    if (u < 0) {
                        u = LongCompanionObject.MAX_VALUE;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(r, u));
            replay();
        }

        public long produced(long n) {
            return addAndGet(-n);
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            if (get() >= 0 && getAndSet(-1) >= 0) {
                this.state.removeProducer(this);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:104:0x0111, code lost:
            r6 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:106:?, code lost:
            dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r6);
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:110:0x0125, code lost:
            r5.onError(dji.thirdparty.rx.exceptions.OnErrorThrowable.addValueAsLastCause(r6, r10.getValue(r11)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:111:0x0134, code lost:
            if (1 == 0) goto L_0x0136;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:112:0x0136, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:115:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
            r10 = r20.state.nl;
            r5 = r20.child;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:121:0x0145, code lost:
            r8 = r8 + 1;
            r7 = r7 + 1;
            r12 = r12 - 1;
            r16 = r16 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:124:0x0155, code lost:
            if (r5.isUnsubscribed() == false) goto L_0x0169;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:126:0x0158, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:127:0x015a, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
            r12 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:130:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:131:0x0163, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:138:?, code lost:
            r20.index = r7;
            r20.currentIndexInBuffer = r8;
            r20.currentBuffer = r4;
            produced((long) r16);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:139:0x0181, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:143:0x0188, code lost:
            if (r20.missed != false) goto L_0x01a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:144:0x018a, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:145:0x0193, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:146:0x0194, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:147:0x0196, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0033, code lost:
            if (r12 >= 0) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:150:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:151:0x019f, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:158:?, code lost:
            r20.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:159:0x01ad, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:165:0x01b3, code lost:
            r17 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:166:0x01b4, code lost:
            if (0 == 0) goto L_0x01b6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:167:0x01b6, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0036, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:170:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:172:0x01c0, code lost:
            throw r17;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0038, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:191:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:192:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:193:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:194:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:195:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:196:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:197:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:198:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:199:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:200:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:201:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:202:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:203:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:204:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:205:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:206:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r14 = r20.state.size();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0053, code lost:
            if (r14 == 0) goto L_0x0181;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0055, code lost:
            r4 = r20.currentBuffer;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0059, code lost:
            if (r4 != null) goto L_0x0069;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x005b, code lost:
            r4 = r20.state.head();
            r20.currentBuffer = r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0069, code lost:
            r9 = r4.length - 1;
            r7 = r20.index;
            r8 = r20.currentIndexInBuffer;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x007a, code lost:
            if (r12 != 0) goto L_0x00c0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x007c, code lost:
            r11 = r4[r8];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0082, code lost:
            if (r10.isCompleted(r11) == false) goto L_0x009c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0084, code lost:
            r5.onCompleted();
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x008b, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x008d, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0096, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a0, code lost:
            if (r10.isError(r11) == false) goto L_0x0181;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00a2, code lost:
            r5.onError(r10.getError(r11));
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00af, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b1, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00ba, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x00c4, code lost:
            if (r12 <= 0) goto L_0x0181;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x00c6, code lost:
            r16 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x00c8, code lost:
            if (r7 >= r14) goto L_0x0151;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x00ce, code lost:
            if (r12 <= 0) goto L_0x0151;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x00d4, code lost:
            if (r5.isUnsubscribed() == false) goto L_0x00e8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x00d7, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x00d9, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x00e2, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:0x00e8, code lost:
            if (r8 != r9) goto L_0x00f4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:?, code lost:
            r4 = (java.lang.Object[]) r4[r9];
            r8 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x00f4, code lost:
            r11 = r4[r8];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x00fa, code lost:
            if (r10.accept(r5, r11) == false) goto L_0x0145;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x00fc, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x0100, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:0x0102, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
            r20.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x010b, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay() {
            /*
                r20 = this;
                monitor-enter(r20)
                r0 = r20
                boolean r0 = r0.emitting     // Catch:{ all -> 0x0046 }
                r17 = r0
                if (r17 == 0) goto L_0x0013
                r17 = 1
                r0 = r17
                r1 = r20
                r1.missed = r0     // Catch:{ all -> 0x0046 }
                monitor-exit(r20)     // Catch:{ all -> 0x0046 }
            L_0x0012:
                return
            L_0x0013:
                r17 = 1
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x0046 }
                monitor-exit(r20)     // Catch:{ all -> 0x0046 }
                r15 = 0
                r0 = r20
                dji.thirdparty.rx.internal.operators.CachedObservable$CacheState<T> r0 = r0.state     // Catch:{ all -> 0x01b3 }
                r17 = r0
                r0 = r17
                dji.thirdparty.rx.internal.operators.NotificationLite<T> r10 = r0.nl     // Catch:{ all -> 0x01b3 }
                r0 = r20
                dji.thirdparty.rx.Subscriber<? super T> r5 = r0.child     // Catch:{ all -> 0x01b3 }
            L_0x002b:
                long r12 = r20.get()     // Catch:{ all -> 0x01b3 }
                r18 = 0
                int r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
                if (r17 >= 0) goto L_0x0049
                r15 = 1
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x0043 }
                monitor-exit(r20)     // Catch:{ all -> 0x0043 }
                goto L_0x0012
            L_0x0043:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x0043 }
                throw r17
            L_0x0046:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x0046 }
                throw r17
            L_0x0049:
                r0 = r20
                dji.thirdparty.rx.internal.operators.CachedObservable$CacheState<T> r0 = r0.state     // Catch:{ all -> 0x01b3 }
                r17 = r0
                int r14 = r17.size()     // Catch:{ all -> 0x01b3 }
                if (r14 == 0) goto L_0x0181
                r0 = r20
                java.lang.Object[] r4 = r0.currentBuffer     // Catch:{ all -> 0x01b3 }
                if (r4 != 0) goto L_0x0069
                r0 = r20
                dji.thirdparty.rx.internal.operators.CachedObservable$CacheState<T> r0 = r0.state     // Catch:{ all -> 0x01b3 }
                r17 = r0
                java.lang.Object[] r4 = r17.head()     // Catch:{ all -> 0x01b3 }
                r0 = r20
                r0.currentBuffer = r4     // Catch:{ all -> 0x01b3 }
            L_0x0069:
                int r0 = r4.length     // Catch:{ all -> 0x01b3 }
                r17 = r0
                int r9 = r17 + -1
                r0 = r20
                int r7 = r0.index     // Catch:{ all -> 0x01b3 }
                r0 = r20
                int r8 = r0.currentIndexInBuffer     // Catch:{ all -> 0x01b3 }
                r18 = 0
                int r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
                if (r17 != 0) goto L_0x00c0
                r11 = r4[r8]     // Catch:{ all -> 0x01b3 }
                boolean r17 = r10.isCompleted(r11)     // Catch:{ all -> 0x01b3 }
                if (r17 == 0) goto L_0x009c
                r5.onCompleted()     // Catch:{ all -> 0x01b3 }
                r15 = 1
                r20.unsubscribe()     // Catch:{ all -> 0x01b3 }
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x0099 }
                monitor-exit(r20)     // Catch:{ all -> 0x0099 }
                goto L_0x0012
            L_0x0099:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x0099 }
                throw r17
            L_0x009c:
                boolean r17 = r10.isError(r11)     // Catch:{ all -> 0x01b3 }
                if (r17 == 0) goto L_0x0181
                java.lang.Throwable r17 = r10.getError(r11)     // Catch:{ all -> 0x01b3 }
                r0 = r17
                r5.onError(r0)     // Catch:{ all -> 0x01b3 }
                r15 = 1
                r20.unsubscribe()     // Catch:{ all -> 0x01b3 }
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x00bd }
                monitor-exit(r20)     // Catch:{ all -> 0x00bd }
                goto L_0x0012
            L_0x00bd:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x00bd }
                throw r17
            L_0x00c0:
                r18 = 0
                int r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
                if (r17 <= 0) goto L_0x0181
                r16 = 0
            L_0x00c8:
                if (r7 >= r14) goto L_0x0151
                r18 = 0
                int r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
                if (r17 <= 0) goto L_0x0151
                boolean r17 = r5.isUnsubscribed()     // Catch:{ all -> 0x01b3 }
                if (r17 == 0) goto L_0x00e8
                r15 = 1
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x00e5 }
                monitor-exit(r20)     // Catch:{ all -> 0x00e5 }
                goto L_0x0012
            L_0x00e5:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x00e5 }
                throw r17
            L_0x00e8:
                if (r8 != r9) goto L_0x00f4
                r17 = r4[r9]     // Catch:{ all -> 0x01b3 }
                java.lang.Object[] r17 = (java.lang.Object[]) r17     // Catch:{ all -> 0x01b3 }
                r0 = r17
                java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x01b3 }
                r4 = r0
                r8 = 0
            L_0x00f4:
                r11 = r4[r8]     // Catch:{ all -> 0x01b3 }
                boolean r17 = r10.accept(r5, r11)     // Catch:{ Throwable -> 0x0111 }
                if (r17 == 0) goto L_0x0145
                r15 = 1
                r20.unsubscribe()     // Catch:{ Throwable -> 0x0111 }
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x010e }
                monitor-exit(r20)     // Catch:{ all -> 0x010e }
                goto L_0x0012
            L_0x010e:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x010e }
                throw r17
            L_0x0111:
                r6 = move-exception
                dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r6)     // Catch:{ all -> 0x01b3 }
                r15 = 1
                r20.unsubscribe()     // Catch:{ all -> 0x01b3 }
                boolean r17 = r10.isError(r11)     // Catch:{ all -> 0x01b3 }
                if (r17 != 0) goto L_0x0134
                boolean r17 = r10.isCompleted(r11)     // Catch:{ all -> 0x01b3 }
                if (r17 != 0) goto L_0x0134
                java.lang.Object r17 = r10.getValue(r11)     // Catch:{ all -> 0x01b3 }
                r0 = r17
                java.lang.Throwable r17 = dji.thirdparty.rx.exceptions.OnErrorThrowable.addValueAsLastCause(r6, r0)     // Catch:{ all -> 0x01b3 }
                r0 = r17
                r5.onError(r0)     // Catch:{ all -> 0x01b3 }
            L_0x0134:
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x0142 }
                monitor-exit(r20)     // Catch:{ all -> 0x0142 }
                goto L_0x0012
            L_0x0142:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x0142 }
                throw r17
            L_0x0145:
                int r8 = r8 + 1
                int r7 = r7 + 1
                r18 = 1
                long r12 = r12 - r18
                int r16 = r16 + 1
                goto L_0x00c8
            L_0x0151:
                boolean r17 = r5.isUnsubscribed()     // Catch:{ all -> 0x01b3 }
                if (r17 == 0) goto L_0x0169
                r15 = 1
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x0166 }
                monitor-exit(r20)     // Catch:{ all -> 0x0166 }
                goto L_0x0012
            L_0x0166:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x0166 }
                throw r17
            L_0x0169:
                r0 = r20
                r0.index = r7     // Catch:{ all -> 0x01b3 }
                r0 = r20
                r0.currentIndexInBuffer = r8     // Catch:{ all -> 0x01b3 }
                r0 = r20
                r0.currentBuffer = r4     // Catch:{ all -> 0x01b3 }
                r0 = r16
                long r0 = (long) r0     // Catch:{ all -> 0x01b3 }
                r18 = r0
                r0 = r20
                r1 = r18
                r0.produced(r1)     // Catch:{ all -> 0x01b3 }
            L_0x0181:
                monitor-enter(r20)     // Catch:{ all -> 0x01b3 }
                r0 = r20
                boolean r0 = r0.missed     // Catch:{ all -> 0x01b0 }
                r17 = r0
                if (r17 != 0) goto L_0x01a5
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x01b0 }
                r15 = 1
                monitor-exit(r20)     // Catch:{ all -> 0x01b0 }
                if (r15 != 0) goto L_0x0012
                monitor-enter(r20)
                r17 = 0
                r0 = r17
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x01a2 }
                monitor-exit(r20)     // Catch:{ all -> 0x01a2 }
                goto L_0x0012
            L_0x01a2:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x01a2 }
                throw r17
            L_0x01a5:
                r17 = 0
                r0 = r17
                r1 = r20
                r1.missed = r0     // Catch:{ all -> 0x01b0 }
                monitor-exit(r20)     // Catch:{ all -> 0x01b0 }
                goto L_0x002b
            L_0x01b0:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x01b0 }
                throw r17     // Catch:{ all -> 0x01b3 }
            L_0x01b3:
                r17 = move-exception
                if (r15 != 0) goto L_0x01c0
                monitor-enter(r20)
                r18 = 0
                r0 = r18
                r1 = r20
                r1.emitting = r0     // Catch:{ all -> 0x01c1 }
                monitor-exit(r20)     // Catch:{ all -> 0x01c1 }
            L_0x01c0:
                throw r17
            L_0x01c1:
                r17 = move-exception
                monitor-exit(r20)     // Catch:{ all -> 0x01c1 }
                throw r17
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.CachedObservable.ReplayProducer.replay():void");
        }
    }
}
