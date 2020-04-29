package dji.thirdparty.rx.internal.util;

import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.annotations.Experimental;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

@Experimental
public final class BackpressureDrainManager extends AtomicLong implements Producer {
    protected final BackpressureQueueCallback actual;
    protected boolean emitting;
    protected Throwable exception;
    protected volatile boolean terminated;

    public interface BackpressureQueueCallback {
        boolean accept(Object obj);

        void complete(Throwable th);

        Object peek();

        Object poll();
    }

    public BackpressureDrainManager(BackpressureQueueCallback actual2) {
        this.actual = actual2;
    }

    public final boolean isTerminated() {
        return this.terminated;
    }

    public final void terminate() {
        this.terminated = true;
    }

    public final void terminate(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
        }
    }

    public final void terminateAndDrain() {
        this.terminated = true;
        drain();
    }

    public final void terminateAndDrain(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
            drain();
        }
    }

    public final void request(long n) {
        long r;
        boolean mayDrain;
        long u;
        if (n != 0) {
            do {
                r = get();
                mayDrain = r == 0;
                if (r == LongCompanionObject.MAX_VALUE) {
                    break;
                } else if (n == LongCompanionObject.MAX_VALUE) {
                    u = n;
                    mayDrain = true;
                } else if (r > LongCompanionObject.MAX_VALUE - n) {
                    u = LongCompanionObject.MAX_VALUE;
                } else {
                    u = r + n;
                }
            } while (!compareAndSet(r, u));
            if (mayDrain) {
                drain();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x00b6, code lost:
        monitor-enter(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:?, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x00ba, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0019, code lost:
        if (r4 > 0) goto L_0x001d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
        if (r8 == false) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        if (r8 == false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0023, code lost:
        if (r0.peek() != null) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0025, code lost:
        r0.complete(r14.exception);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002b, code lost:
        if (1 != 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002d, code lost:
        monitor-enter(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0031, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x003d, code lost:
        if (r4 != 0) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        monitor-enter(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r8 = r14.terminated;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0046, code lost:
        if (r0.peek() == null) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0048, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0054, code lost:
        if (get() != kotlin.jvm.internal.LongCompanionObject.MAX_VALUE) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0056, code lost:
        if (r3 != false) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0058, code lost:
        if (r8 != false) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x005a, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x005e, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x005f, code lost:
        if (1 != 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0061, code lost:
        monitor-enter(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0065, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        r6 = r0.poll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x006e, code lost:
        if (r6 == null) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0074, code lost:
        if (r0.accept(r6) == false) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0077, code lost:
        if (1 != 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0079, code lost:
        monitor-enter(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x007d, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0082, code lost:
        r4 = r4 - 1;
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0088, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x008a, code lost:
        r4 = kotlin.jvm.internal.LongCompanionObject.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:?, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000d, code lost:
        r4 = get();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0094, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0095, code lost:
        if (0 == 0) goto L_0x0097;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0097, code lost:
        monitor-enter(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x009c, code lost:
        throw r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:?, code lost:
        r4 = addAndGet((long) (-r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x00a7, code lost:
        if (r4 == 0) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x00a9, code lost:
        if (r3 != false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x00ab, code lost:
        if (r8 == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x00ad, code lost:
        if (r3 == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x00af, code lost:
        r14.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x00b3, code lost:
        monitor-exit(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x00b4, code lost:
        if (1 != 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        r0 = r14.actual;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void drain() {
        /*
            r14 = this;
            monitor-enter(r14)
            boolean r9 = r14.emitting     // Catch:{ all -> 0x0036 }
            if (r9 == 0) goto L_0x0007
            monitor-exit(r14)     // Catch:{ all -> 0x0036 }
        L_0x0006:
            return
        L_0x0007:
            r9 = 1
            r14.emitting = r9     // Catch:{ all -> 0x0036 }
            boolean r8 = r14.terminated     // Catch:{ all -> 0x0036 }
            monitor-exit(r14)     // Catch:{ all -> 0x0036 }
            long r4 = r14.get()
            r7 = 0
            dji.thirdparty.rx.internal.util.BackpressureDrainManager$BackpressureQueueCallback r0 = r14.actual     // Catch:{ all -> 0x0094 }
        L_0x0014:
            r2 = 0
        L_0x0015:
            r10 = 0
            int r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r9 > 0) goto L_0x001d
            if (r8 == 0) goto L_0x003f
        L_0x001d:
            if (r8 == 0) goto L_0x006a
            java.lang.Object r6 = r0.peek()     // Catch:{ all -> 0x0094 }
            if (r6 != 0) goto L_0x0039
            r7 = 1
            java.lang.Throwable r1 = r14.exception     // Catch:{ all -> 0x0094 }
            r0.complete(r1)     // Catch:{ all -> 0x0094 }
            if (r7 != 0) goto L_0x0006
            monitor-enter(r14)
            r9 = 0
            r14.emitting = r9     // Catch:{ all -> 0x0033 }
            monitor-exit(r14)     // Catch:{ all -> 0x0033 }
            goto L_0x0006
        L_0x0033:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x0033 }
            throw r9
        L_0x0036:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x0036 }
            throw r9
        L_0x0039:
            r10 = 0
            int r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r9 != 0) goto L_0x006a
        L_0x003f:
            monitor-enter(r14)     // Catch:{ all -> 0x0094 }
            boolean r8 = r14.terminated     // Catch:{ all -> 0x0091 }
            java.lang.Object r9 = r0.peek()     // Catch:{ all -> 0x0091 }
            if (r9 == 0) goto L_0x0088
            r3 = 1
        L_0x0049:
            long r10 = r14.get()     // Catch:{ all -> 0x0091 }
            r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r9 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r9 != 0) goto L_0x009d
            if (r3 != 0) goto L_0x008a
            if (r8 != 0) goto L_0x008a
            r7 = 1
            r9 = 0
            r14.emitting = r9     // Catch:{ all -> 0x0091 }
            monitor-exit(r14)     // Catch:{ all -> 0x0091 }
            if (r7 != 0) goto L_0x0006
            monitor-enter(r14)
            r9 = 0
            r14.emitting = r9     // Catch:{ all -> 0x0067 }
            monitor-exit(r14)     // Catch:{ all -> 0x0067 }
            goto L_0x0006
        L_0x0067:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x0067 }
            throw r9
        L_0x006a:
            java.lang.Object r6 = r0.poll()     // Catch:{ all -> 0x0094 }
            if (r6 == 0) goto L_0x003f
            boolean r9 = r0.accept(r6)     // Catch:{ all -> 0x0094 }
            if (r9 == 0) goto L_0x0082
            r7 = 1
            if (r7 != 0) goto L_0x0006
            monitor-enter(r14)
            r9 = 0
            r14.emitting = r9     // Catch:{ all -> 0x007f }
            monitor-exit(r14)     // Catch:{ all -> 0x007f }
            goto L_0x0006
        L_0x007f:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x007f }
            throw r9
        L_0x0082:
            r10 = 1
            long r4 = r4 - r10
            int r2 = r2 + 1
            goto L_0x0015
        L_0x0088:
            r3 = 0
            goto L_0x0049
        L_0x008a:
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x008f:
            monitor-exit(r14)     // Catch:{ all -> 0x0091 }
            goto L_0x0014
        L_0x0091:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x0091 }
            throw r9     // Catch:{ all -> 0x0094 }
        L_0x0094:
            r9 = move-exception
            if (r7 != 0) goto L_0x009c
            monitor-enter(r14)
            r10 = 0
            r14.emitting = r10     // Catch:{ all -> 0x00c0 }
            monitor-exit(r14)     // Catch:{ all -> 0x00c0 }
        L_0x009c:
            throw r9
        L_0x009d:
            int r9 = -r2
            long r10 = (long) r9
            long r4 = r14.addAndGet(r10)     // Catch:{ all -> 0x0091 }
            r10 = 0
            int r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r9 == 0) goto L_0x00ab
            if (r3 != 0) goto L_0x008f
        L_0x00ab:
            if (r8 == 0) goto L_0x00af
            if (r3 == 0) goto L_0x008f
        L_0x00af:
            r7 = 1
            r9 = 0
            r14.emitting = r9     // Catch:{ all -> 0x0091 }
            monitor-exit(r14)     // Catch:{ all -> 0x0091 }
            if (r7 != 0) goto L_0x0006
            monitor-enter(r14)
            r9 = 0
            r14.emitting = r9     // Catch:{ all -> 0x00bd }
            monitor-exit(r14)     // Catch:{ all -> 0x00bd }
            goto L_0x0006
        L_0x00bd:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x00bd }
            throw r9
        L_0x00c0:
            r9 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x00c0 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.util.BackpressureDrainManager.drain():void");
    }
}
