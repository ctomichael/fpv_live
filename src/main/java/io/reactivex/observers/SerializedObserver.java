package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.NotificationLite;

public final class SerializedObserver<T> implements Observer<T>, Disposable {
    static final int QUEUE_LINK_SIZE = 4;
    final boolean delayError;
    volatile boolean done;
    final Observer<? super T> downstream;
    boolean emitting;
    AppendOnlyLinkedArrayList<Object> queue;
    Disposable upstream;

    public SerializedObserver(@NonNull Observer<? super T> downstream2) {
        this(downstream2, false);
    }

    public SerializedObserver(@NonNull Observer<? super T> actual, boolean delayError2) {
        this.downstream = actual;
        this.delayError = delayError2;
    }

    public void onSubscribe(@NonNull Disposable d) {
        if (DisposableHelper.validate(this.upstream, d)) {
            this.upstream = d;
            this.downstream.onSubscribe(this);
        }
    }

    public void dispose() {
        this.upstream.dispose();
    }

    public boolean isDisposed() {
        return this.upstream.isDisposed();
    }

    public void onNext(@NonNull T t) {
        if (!this.done) {
            if (t == null) {
                this.upstream.dispose();
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
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
                    this.downstream.onNext(t);
                    emitLoop();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
        io.reactivex.plugins.RxJavaPlugins.onError(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0044, code lost:
        r4.downstream.onError(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000f, code lost:
        if (r2 == false) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onError(@io.reactivex.annotations.NonNull java.lang.Throwable r5) {
        /*
            r4 = this;
            boolean r3 = r4.done
            if (r3 == 0) goto L_0x0008
            io.reactivex.plugins.RxJavaPlugins.onError(r5)
        L_0x0007:
            return
        L_0x0008:
            monitor-enter(r4)
            boolean r3 = r4.done     // Catch:{ all -> 0x0035 }
            if (r3 == 0) goto L_0x0015
            r2 = 1
        L_0x000e:
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            if (r2 == 0) goto L_0x0044
            io.reactivex.plugins.RxJavaPlugins.onError(r5)
            goto L_0x0007
        L_0x0015:
            boolean r3 = r4.emitting     // Catch:{ all -> 0x0035 }
            if (r3 == 0) goto L_0x003c
            r3 = 1
            r4.done = r3     // Catch:{ all -> 0x0035 }
            io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r1 = r4.queue     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x0028
            io.reactivex.internal.util.AppendOnlyLinkedArrayList r1 = new io.reactivex.internal.util.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0035 }
            r3 = 4
            r1.<init>(r3)     // Catch:{ all -> 0x0035 }
            r4.queue = r1     // Catch:{ all -> 0x0035 }
        L_0x0028:
            java.lang.Object r0 = io.reactivex.internal.util.NotificationLite.error(r5)     // Catch:{ all -> 0x0035 }
            boolean r3 = r4.delayError     // Catch:{ all -> 0x0035 }
            if (r3 == 0) goto L_0x0038
            r1.add(r0)     // Catch:{ all -> 0x0035 }
        L_0x0033:
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            goto L_0x0007
        L_0x0035:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            throw r3
        L_0x0038:
            r1.setFirst(r0)     // Catch:{ all -> 0x0035 }
            goto L_0x0033
        L_0x003c:
            r3 = 1
            r4.done = r3     // Catch:{ all -> 0x0035 }
            r3 = 1
            r4.emitting = r3     // Catch:{ all -> 0x0035 }
            r2 = 0
            goto L_0x000e
        L_0x0044:
            io.reactivex.Observer<? super T> r3 = r4.downstream
            r3.onError(r5)
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.observers.SerializedObserver.onError(java.lang.Throwable):void");
    }

    public void onComplete() {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.complete());
                        return;
                    }
                    this.done = true;
                    this.emitting = true;
                    this.downstream.onComplete();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void emitLoop() {
        AppendOnlyLinkedArrayList<Object> q;
        do {
            synchronized (this) {
                q = this.queue;
                if (q == null) {
                    this.emitting = false;
                    return;
                }
                this.queue = null;
            }
        } while (!q.accept(this.downstream));
    }
}
