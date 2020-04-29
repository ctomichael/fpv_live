package dji.thirdparty.rx.internal.producers;

import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.internal.operators.BackpressureUtils;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER = new Producer() {
        /* class dji.thirdparty.rx.internal.producers.ProducerObserverArbiter.AnonymousClass1 */

        public void request(long n) {
        }
    };
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    public ProducerObserverArbiter(Subscriber<? super T> child2) {
        this.child = child2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r6.child.onNext(r7);
        r2 = r6.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0026, code lost:
        if (r2 == kotlin.jvm.internal.LongCompanionObject.MAX_VALUE) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        r6.requested = r2 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (1 != 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r6.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0038, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0040, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0041, code lost:
        if (0 == 0) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0043, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r6.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0048, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            boolean r4 = r6.emitting     // Catch:{ all -> 0x003d }
            if (r4 == 0) goto L_0x0016
            java.util.List<T> r0 = r6.queue     // Catch:{ all -> 0x003d }
            if (r0 != 0) goto L_0x0011
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x003d }
            r4 = 4
            r0.<init>(r4)     // Catch:{ all -> 0x003d }
            r6.queue = r0     // Catch:{ all -> 0x003d }
        L_0x0011:
            r0.add(r7)     // Catch:{ all -> 0x003d }
            monitor-exit(r6)     // Catch:{ all -> 0x003d }
        L_0x0015:
            return
        L_0x0016:
            monitor-exit(r6)     // Catch:{ all -> 0x003d }
            r1 = 0
            dji.thirdparty.rx.Subscriber<? super T> r4 = r6.child     // Catch:{ all -> 0x0040 }
            r4.onNext(r7)     // Catch:{ all -> 0x0040 }
            long r2 = r6.requested     // Catch:{ all -> 0x0040 }
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 == 0) goto L_0x002e
            r4 = 1
            long r4 = r2 - r4
            r6.requested = r4     // Catch:{ all -> 0x0040 }
        L_0x002e:
            r6.emitLoop()     // Catch:{ all -> 0x0040 }
            r1 = 1
            if (r1 != 0) goto L_0x0015
            monitor-enter(r6)
            r4 = 0
            r6.emitting = r4     // Catch:{ all -> 0x003a }
            monitor-exit(r6)     // Catch:{ all -> 0x003a }
            goto L_0x0015
        L_0x003a:
            r4 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003a }
            throw r4
        L_0x003d:
            r4 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003d }
            throw r4
        L_0x0040:
            r4 = move-exception
            if (r1 != 0) goto L_0x0048
            monitor-enter(r6)
            r5 = 0
            r6.emitting = r5     // Catch:{ all -> 0x0049 }
            monitor-exit(r6)     // Catch:{ all -> 0x0049 }
        L_0x0048:
            throw r4
        L_0x0049:
            r4 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0049 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.producers.ProducerObserverArbiter.onNext(java.lang.Object):void");
    }

    public void onError(Throwable e) {
        boolean emit;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = e;
                emit = false;
            } else {
                this.emitting = true;
                emit = true;
            }
        }
        if (emit) {
            this.child.onError(e);
        } else {
            this.hasError = true;
        }
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = true;
                return;
            }
            this.emitting = true;
            this.child.onCompleted();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0027, code lost:
        r0 = r11.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r4 = r11.requested + r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0030, code lost:
        if (r4 >= 0) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0032, code lost:
        r4 = kotlin.jvm.internal.LongCompanionObject.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0037, code lost:
        r11.requested = r4;
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x003d, code lost:
        if (1 != 0) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x003f, code lost:
        monitor-enter(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r11.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0043, code lost:
        monitor-exit(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0044, code lost:
        if (r0 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0046, code lost:
        r0.request(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x004d, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x004e, code lost:
        if (0 == 0) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0050, code lost:
        monitor-enter(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        r11.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0055, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r12) {
        /*
            r11 = this;
            r8 = 0
            int r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x000f
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "n >= 0 required"
            r6.<init>(r7)
            throw r6
        L_0x000f:
            int r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r6 != 0) goto L_0x0014
        L_0x0013:
            return
        L_0x0014:
            monitor-enter(r11)
            boolean r6 = r11.emitting     // Catch:{ all -> 0x0020 }
            if (r6 == 0) goto L_0x0023
            long r6 = r11.missedRequested     // Catch:{ all -> 0x0020 }
            long r6 = r6 + r12
            r11.missedRequested = r6     // Catch:{ all -> 0x0020 }
            monitor-exit(r11)     // Catch:{ all -> 0x0020 }
            goto L_0x0013
        L_0x0020:
            r6 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0020 }
            throw r6
        L_0x0023:
            r6 = 1
            r11.emitting = r6     // Catch:{ all -> 0x0020 }
            monitor-exit(r11)     // Catch:{ all -> 0x0020 }
            dji.thirdparty.rx.Producer r0 = r11.currentProducer
            r1 = 0
            long r2 = r11.requested     // Catch:{ all -> 0x004d }
            long r4 = r2 + r12
            int r6 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0037
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x0037:
            r11.requested = r4     // Catch:{ all -> 0x004d }
            r11.emitLoop()     // Catch:{ all -> 0x004d }
            r1 = 1
            if (r1 != 0) goto L_0x0044
            monitor-enter(r11)
            r6 = 0
            r11.emitting = r6     // Catch:{ all -> 0x004a }
            monitor-exit(r11)     // Catch:{ all -> 0x004a }
        L_0x0044:
            if (r0 == 0) goto L_0x0013
            r0.request(r12)
            goto L_0x0013
        L_0x004a:
            r6 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x004a }
            throw r6
        L_0x004d:
            r6 = move-exception
            if (r1 != 0) goto L_0x0055
            monitor-enter(r11)
            r7 = 0
            r11.emitting = r7     // Catch:{ all -> 0x0056 }
            monitor-exit(r11)     // Catch:{ all -> 0x0056 }
        L_0x0055:
            throw r6
        L_0x0056:
            r6 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0056 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.producers.ProducerObserverArbiter.request(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
        r6.currentProducer = r7;
        r0 = r6.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001b, code lost:
        if (1 != 0) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001d, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r6.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0021, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0022, code lost:
        if (r7 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0028, code lost:
        if (r0 == 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002a, code lost:
        r7.request(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0034, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0035, code lost:
        if (0 == 0) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0037, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r6.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x003c, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(dji.thirdparty.rx.Producer r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            boolean r3 = r6.emitting     // Catch:{ all -> 0x002e }
            if (r3 == 0) goto L_0x000e
            if (r7 == 0) goto L_0x000b
        L_0x0007:
            r6.missedProducer = r7     // Catch:{ all -> 0x002e }
            monitor-exit(r6)     // Catch:{ all -> 0x002e }
        L_0x000a:
            return
        L_0x000b:
            dji.thirdparty.rx.Producer r7 = dji.thirdparty.rx.internal.producers.ProducerObserverArbiter.NULL_PRODUCER     // Catch:{ all -> 0x002e }
            goto L_0x0007
        L_0x000e:
            r3 = 1
            r6.emitting = r3     // Catch:{ all -> 0x002e }
            monitor-exit(r6)     // Catch:{ all -> 0x002e }
            r2 = 0
            r6.currentProducer = r7
            long r0 = r6.requested
            r6.emitLoop()     // Catch:{ all -> 0x0034 }
            r2 = 1
            if (r2 != 0) goto L_0x0022
            monitor-enter(r6)
            r3 = 0
            r6.emitting = r3     // Catch:{ all -> 0x0031 }
            monitor-exit(r6)     // Catch:{ all -> 0x0031 }
        L_0x0022:
            if (r7 == 0) goto L_0x000a
            r4 = 0
            int r3 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r3 == 0) goto L_0x000a
            r7.request(r0)
            goto L_0x000a
        L_0x002e:
            r3 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x002e }
            throw r3
        L_0x0031:
            r3 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0031 }
            throw r3
        L_0x0034:
            r3 = move-exception
            if (r2 != 0) goto L_0x003c
            monitor-enter(r6)
            r4 = 0
            r6.emitting = r4     // Catch:{ all -> 0x003d }
            monitor-exit(r6)     // Catch:{ all -> 0x003d }
        L_0x003c:
            throw r3
        L_0x003d:
            r3 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003d }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.producers.ProducerObserverArbiter.setProducer(dji.thirdparty.rx.Producer):void");
    }

    /* access modifiers changed from: package-private */
    public void emitLoop() {
        long localRequested;
        Producer localProducer;
        Object localTerminal;
        List<T> q;
        Subscriber<? super T> c = this.child;
        long toRequest = 0;
        Producer requestFrom = null;
        while (true) {
            boolean quit = false;
            synchronized (this) {
                localRequested = this.missedRequested;
                localProducer = this.missedProducer;
                localTerminal = this.missedTerminal;
                q = this.queue;
                if (localRequested == 0 && localProducer == null && q == null && localTerminal == null) {
                    this.emitting = false;
                    quit = true;
                } else {
                    this.missedRequested = 0;
                    this.missedProducer = null;
                    this.queue = null;
                    this.missedTerminal = null;
                }
            }
            if (!quit) {
                boolean empty = q == null || q.isEmpty();
                if (localTerminal != null) {
                    if (localTerminal != Boolean.TRUE) {
                        c.onError((Throwable) localTerminal);
                        return;
                    } else if (empty) {
                        c.onCompleted();
                        return;
                    }
                }
                long e = 0;
                if (q != null) {
                    Iterator i$ = q.iterator();
                    while (true) {
                        if (!i$.hasNext()) {
                            e = 0 + ((long) q.size());
                            break;
                        }
                        T v = i$.next();
                        if (!c.isUnsubscribed()) {
                            if (this.hasError) {
                                break;
                            }
                            try {
                                c.onNext(v);
                            } catch (Throwable ex) {
                                Exceptions.throwOrReport(ex, c, v);
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
                long r = this.requested;
                if (r != LongCompanionObject.MAX_VALUE) {
                    if (localRequested != 0) {
                        long u = r + localRequested;
                        if (u < 0) {
                            u = LongCompanionObject.MAX_VALUE;
                        }
                        r = u;
                    }
                    if (!(e == 0 || r == LongCompanionObject.MAX_VALUE)) {
                        long u2 = r - e;
                        if (u2 < 0) {
                            throw new IllegalStateException("More produced than requested");
                        }
                        r = u2;
                    }
                    this.requested = r;
                }
                if (localProducer == null) {
                    Producer p = this.currentProducer;
                    if (p != null && localRequested != 0) {
                        toRequest = BackpressureUtils.addCap(toRequest, localRequested);
                        requestFrom = p;
                    }
                } else if (localProducer == NULL_PRODUCER) {
                    this.currentProducer = null;
                } else {
                    this.currentProducer = localProducer;
                    if (r != 0) {
                        toRequest = BackpressureUtils.addCap(toRequest, r);
                        requestFrom = localProducer;
                    }
                }
            } else if (toRequest != 0 && requestFrom != null) {
                requestFrom.request(toRequest);
                return;
            } else {
                return;
            }
        }
    }
}
