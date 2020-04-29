package dji.thirdparty.rx.observers;

import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.internal.operators.NotificationLite;

public class SerializedObserver<T> implements Observer<T> {
    private static final int MAX_DRAIN_ITERATION = 1024;
    private final Observer<? super T> actual;
    private boolean emitting;
    private final NotificationLite<T> nl = NotificationLite.instance();
    private FastList queue;
    private volatile boolean terminated;

    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object o) {
            int s = this.size;
            Object[] a = this.array;
            if (a == null) {
                a = new Object[16];
                this.array = a;
            } else if (s == a.length) {
                Object[] array2 = new Object[((s >> 2) + s)];
                System.arraycopy(a, 0, array2, 0, s);
                a = array2;
                this.array = a;
            }
            a[s] = o;
            this.size = s + 1;
        }
    }

    public SerializedObserver(Observer<? super T> s) {
        this.actual = s;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r10.actual.onNext(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0033, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0036, code lost:
        if (r2 >= 1024) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0038, code lost:
        monitor-enter(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r5 = r10.queue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x003b, code lost:
        if (r5 != null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x003d, code lost:
        r10.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0040, code lost:
        monitor-exit(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0045, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0046, code lost:
        r10.terminated = true;
        dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r1, r10.actual, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r10.queue = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0051, code lost:
        monitor-exit(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0052, code lost:
        r0 = r5.array;
        r4 = r0.length;
        r3 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0056, code lost:
        if (r3 >= r4) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0058, code lost:
        r6 = r0[r3];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x005a, code lost:
        if (r6 != null) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x005c, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0067, code lost:
        if (r10.nl.accept(r10.actual, r6) == false) goto L_0x007d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0069, code lost:
        r10.terminated = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x006d, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x006e, code lost:
        r10.terminated = true;
        dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r1);
        r10.actual.onError(dji.thirdparty.rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r11));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x007d, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r11) {
        /*
            r10 = this;
            r9 = 1
            boolean r7 = r10.terminated
            if (r7 == 0) goto L_0x0006
        L_0x0005:
            return
        L_0x0006:
            monitor-enter(r10)
            boolean r7 = r10.terminated     // Catch:{ all -> 0x000d }
            if (r7 == 0) goto L_0x0010
            monitor-exit(r10)     // Catch:{ all -> 0x000d }
            goto L_0x0005
        L_0x000d:
            r7 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x000d }
            throw r7
        L_0x0010:
            boolean r7 = r10.emitting     // Catch:{ all -> 0x000d }
            if (r7 == 0) goto L_0x002a
            dji.thirdparty.rx.observers.SerializedObserver$FastList r5 = r10.queue     // Catch:{ all -> 0x000d }
            if (r5 != 0) goto L_0x001f
            dji.thirdparty.rx.observers.SerializedObserver$FastList r5 = new dji.thirdparty.rx.observers.SerializedObserver$FastList     // Catch:{ all -> 0x000d }
            r5.<init>()     // Catch:{ all -> 0x000d }
            r10.queue = r5     // Catch:{ all -> 0x000d }
        L_0x001f:
            dji.thirdparty.rx.internal.operators.NotificationLite<T> r7 = r10.nl     // Catch:{ all -> 0x000d }
            java.lang.Object r7 = r7.next(r11)     // Catch:{ all -> 0x000d }
            r5.add(r7)     // Catch:{ all -> 0x000d }
            monitor-exit(r10)     // Catch:{ all -> 0x000d }
            goto L_0x0005
        L_0x002a:
            r7 = 1
            r10.emitting = r7     // Catch:{ all -> 0x000d }
            monitor-exit(r10)     // Catch:{ all -> 0x000d }
            dji.thirdparty.rx.Observer<? super T> r7 = r10.actual     // Catch:{ Throwable -> 0x0045 }
            r7.onNext(r11)     // Catch:{ Throwable -> 0x0045 }
        L_0x0033:
            r2 = 0
        L_0x0034:
            r7 = 1024(0x400, float:1.435E-42)
            if (r2 >= r7) goto L_0x0033
            monitor-enter(r10)
            dji.thirdparty.rx.observers.SerializedObserver$FastList r5 = r10.queue     // Catch:{ all -> 0x0042 }
            if (r5 != 0) goto L_0x004e
            r7 = 0
            r10.emitting = r7     // Catch:{ all -> 0x0042 }
            monitor-exit(r10)     // Catch:{ all -> 0x0042 }
            goto L_0x0005
        L_0x0042:
            r7 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x0042 }
            throw r7
        L_0x0045:
            r1 = move-exception
            r10.terminated = r9
            dji.thirdparty.rx.Observer<? super T> r7 = r10.actual
            dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r1, r7, r11)
            goto L_0x0005
        L_0x004e:
            r7 = 0
            r10.queue = r7     // Catch:{ all -> 0x0042 }
            monitor-exit(r10)     // Catch:{ all -> 0x0042 }
            java.lang.Object[] r0 = r5.array
            int r4 = r0.length
            r3 = 0
        L_0x0056:
            if (r3 >= r4) goto L_0x005c
            r6 = r0[r3]
            if (r6 != 0) goto L_0x005f
        L_0x005c:
            int r2 = r2 + 1
            goto L_0x0034
        L_0x005f:
            dji.thirdparty.rx.internal.operators.NotificationLite<T> r7 = r10.nl     // Catch:{ Throwable -> 0x006d }
            dji.thirdparty.rx.Observer<? super T> r8 = r10.actual     // Catch:{ Throwable -> 0x006d }
            boolean r7 = r7.accept(r8, r6)     // Catch:{ Throwable -> 0x006d }
            if (r7 == 0) goto L_0x007d
            r7 = 1
            r10.terminated = r7     // Catch:{ Throwable -> 0x006d }
            goto L_0x0005
        L_0x006d:
            r1 = move-exception
            r10.terminated = r9
            dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r1)
            dji.thirdparty.rx.Observer<? super T> r7 = r10.actual
            java.lang.Throwable r8 = dji.thirdparty.rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r11)
            r7.onError(r8)
            goto L_0x0005
        L_0x007d:
            int r3 = r3 + 1
            goto L_0x0056
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.observers.SerializedObserver.onNext(java.lang.Object):void");
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList list = this.queue;
                        if (list == null) {
                            list = new FastList();
                            this.queue = list;
                        }
                        list.add(this.nl.error(e));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onError(e);
                }
            }
        }
    }

    public void onCompleted() {
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList list = this.queue;
                        if (list == null) {
                            list = new FastList();
                            this.queue = list;
                        }
                        list.add(this.nl.completed());
                        return;
                    }
                    this.emitting = true;
                    this.actual.onCompleted();
                }
            }
        }
    }
}
