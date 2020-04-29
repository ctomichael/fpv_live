package dji.thirdparty.io.reactivex.processors;

import dji.thirdparty.io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class SerializedProcessor<T> extends FlowableProcessor<T> {
    final FlowableProcessor<T> actual;
    volatile boolean done;
    boolean emitting;
    AppendOnlyLinkedArrayList<Object> queue;

    SerializedProcessor(FlowableProcessor<T> actual2) {
        this.actual = actual2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.actual.subscribe(s);
    }

    public void onSubscribe(Subscription s) {
        boolean cancel;
        if (!this.done) {
            synchronized (this) {
                if (this.done) {
                    cancel = true;
                } else if (this.emitting) {
                    AppendOnlyLinkedArrayList<Object> q = this.queue;
                    if (q == null) {
                        q = new AppendOnlyLinkedArrayList<>(4);
                        this.queue = q;
                    }
                    q.add(NotificationLite.subscription(s));
                    return;
                } else {
                    this.emitting = true;
                    cancel = false;
                }
            }
        } else {
            cancel = true;
        }
        if (cancel) {
            s.cancel();
            return;
        }
        this.actual.onSubscribe(s);
        emitLoop();
    }

    public void onNext(T t) {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.next(t));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onNext(t);
                    emitLoop();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
        dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onError(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0039, code lost:
        r3.actual.onError(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000f, code lost:
        if (r1 == false) goto L_0x0039;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onError(java.lang.Throwable r4) {
        /*
            r3 = this;
            boolean r2 = r3.done
            if (r2 == 0) goto L_0x0008
            dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onError(r4)
        L_0x0007:
            return
        L_0x0008:
            monitor-enter(r3)
            boolean r2 = r3.done     // Catch:{ all -> 0x0031 }
            if (r2 == 0) goto L_0x0015
            r1 = 1
        L_0x000e:
            monitor-exit(r3)     // Catch:{ all -> 0x0031 }
            if (r1 == 0) goto L_0x0039
            dji.thirdparty.io.reactivex.plugins.RxJavaPlugins.onError(r4)
            goto L_0x0007
        L_0x0015:
            r2 = 1
            r3.done = r2     // Catch:{ all -> 0x0031 }
            boolean r2 = r3.emitting     // Catch:{ all -> 0x0031 }
            if (r2 == 0) goto L_0x0034
            dji.thirdparty.io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r0 = r3.queue     // Catch:{ all -> 0x0031 }
            if (r0 != 0) goto L_0x0028
            dji.thirdparty.io.reactivex.internal.util.AppendOnlyLinkedArrayList r0 = new dji.thirdparty.io.reactivex.internal.util.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0031 }
            r2 = 4
            r0.<init>(r2)     // Catch:{ all -> 0x0031 }
            r3.queue = r0     // Catch:{ all -> 0x0031 }
        L_0x0028:
            java.lang.Object r2 = dji.thirdparty.io.reactivex.internal.util.NotificationLite.error(r4)     // Catch:{ all -> 0x0031 }
            r0.setFirst(r2)     // Catch:{ all -> 0x0031 }
            monitor-exit(r3)     // Catch:{ all -> 0x0031 }
            goto L_0x0007
        L_0x0031:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0031 }
            throw r2
        L_0x0034:
            r1 = 0
            r2 = 1
            r3.emitting = r2     // Catch:{ all -> 0x0031 }
            goto L_0x000e
        L_0x0039:
            dji.thirdparty.io.reactivex.processors.FlowableProcessor<T> r2 = r3.actual
            r2.onError(r4)
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.processors.SerializedProcessor.onError(java.lang.Throwable):void");
    }

    public void onComplete() {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.complete());
                        return;
                    }
                    this.emitting = true;
                    this.actual.onComplete();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void emitLoop() {
        AppendOnlyLinkedArrayList<Object> q;
        while (true) {
            synchronized (this) {
                q = this.queue;
                if (q == null) {
                    this.emitting = false;
                    return;
                }
                this.queue = null;
            }
            q.accept(this.actual);
        }
        while (true) {
        }
    }

    public boolean hasSubscribers() {
        return this.actual.hasSubscribers();
    }

    public boolean hasThrowable() {
        return this.actual.hasThrowable();
    }

    public Throwable getThrowable() {
        return this.actual.getThrowable();
    }

    public boolean hasComplete() {
        return this.actual.hasComplete();
    }
}
