package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableRefCount<T> extends Observable<T> {
    RefConnection connection;
    final int n;
    final Scheduler scheduler;
    final ConnectableObservable<T> source;
    final long timeout;
    final TimeUnit unit;

    public ObservableRefCount(ConnectableObservable<T> source2) {
        this(source2, 1, 0, TimeUnit.NANOSECONDS, null);
    }

    public ObservableRefCount(ConnectableObservable<T> source2, int n2, long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        this.source = source2;
        this.n = n2;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        RefConnection conn;
        boolean connect = false;
        synchronized (this) {
            conn = this.connection;
            if (conn == null) {
                conn = new RefConnection(this);
                this.connection = conn;
            }
            long c = conn.subscriberCount;
            if (c == 0 && conn.timer != null) {
                conn.timer.dispose();
            }
            conn.subscriberCount = c + 1;
            if (!conn.connected && c + 1 == ((long) this.n)) {
                connect = true;
                conn.connected = true;
            }
        }
        this.source.subscribe(new RefCountObserver(observer, this, conn));
        if (connect) {
            this.source.connect(conn);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel(io.reactivex.internal.operators.observable.ObservableRefCount.RefConnection r11) {
        /*
            r10 = this;
            r8 = 0
            monitor-enter(r10)
            io.reactivex.internal.operators.observable.ObservableRefCount$RefConnection r3 = r10.connection     // Catch:{ all -> 0x001f }
            if (r3 == 0) goto L_0x000b
            io.reactivex.internal.operators.observable.ObservableRefCount$RefConnection r3 = r10.connection     // Catch:{ all -> 0x001f }
            if (r3 == r11) goto L_0x000d
        L_0x000b:
            monitor-exit(r10)     // Catch:{ all -> 0x001f }
        L_0x000c:
            return
        L_0x000d:
            long r4 = r11.subscriberCount     // Catch:{ all -> 0x001f }
            r6 = 1
            long r0 = r4 - r6
            r11.subscriberCount = r0     // Catch:{ all -> 0x001f }
            int r3 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
            if (r3 != 0) goto L_0x001d
            boolean r3 = r11.connected     // Catch:{ all -> 0x001f }
            if (r3 != 0) goto L_0x0022
        L_0x001d:
            monitor-exit(r10)     // Catch:{ all -> 0x001f }
            goto L_0x000c
        L_0x001f:
            r3 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x001f }
            throw r3
        L_0x0022:
            long r4 = r10.timeout     // Catch:{ all -> 0x001f }
            int r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r3 != 0) goto L_0x002d
            r10.timeout(r11)     // Catch:{ all -> 0x001f }
            monitor-exit(r10)     // Catch:{ all -> 0x001f }
            goto L_0x000c
        L_0x002d:
            io.reactivex.internal.disposables.SequentialDisposable r2 = new io.reactivex.internal.disposables.SequentialDisposable     // Catch:{ all -> 0x001f }
            r2.<init>()     // Catch:{ all -> 0x001f }
            r11.timer = r2     // Catch:{ all -> 0x001f }
            monitor-exit(r10)     // Catch:{ all -> 0x001f }
            io.reactivex.Scheduler r3 = r10.scheduler
            long r4 = r10.timeout
            java.util.concurrent.TimeUnit r6 = r10.unit
            io.reactivex.disposables.Disposable r3 = r3.scheduleDirect(r11, r4, r6)
            r2.replace(r3)
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableRefCount.cancel(io.reactivex.internal.operators.observable.ObservableRefCount$RefConnection):void");
    }

    /* access modifiers changed from: package-private */
    public void terminated(RefConnection rc) {
        synchronized (this) {
            if (this.connection != null && this.connection == rc) {
                this.connection = null;
                if (rc.timer != null) {
                    rc.timer.dispose();
                }
            }
            long j = rc.subscriberCount - 1;
            rc.subscriberCount = j;
            if (j == 0) {
                if (this.source instanceof Disposable) {
                    ((Disposable) this.source).dispose();
                } else if (this.source instanceof ResettableConnectable) {
                    ((ResettableConnectable) this.source).resetIf((Disposable) rc.get());
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void timeout(RefConnection rc) {
        synchronized (this) {
            if (rc.subscriberCount == 0 && rc == this.connection) {
                this.connection = null;
                Disposable connectionObject = (Disposable) rc.get();
                DisposableHelper.dispose(rc);
                if (this.source instanceof Disposable) {
                    ((Disposable) this.source).dispose();
                } else if (this.source instanceof ResettableConnectable) {
                    if (connectionObject == null) {
                        rc.disconnectedEarly = true;
                    } else {
                        ((ResettableConnectable) this.source).resetIf(connectionObject);
                    }
                }
            }
        }
    }

    static final class RefConnection extends AtomicReference<Disposable> implements Runnable, Consumer<Disposable> {
        private static final long serialVersionUID = -4552101107598366241L;
        boolean connected;
        boolean disconnectedEarly;
        final ObservableRefCount<?> parent;
        long subscriberCount;
        Disposable timer;

        RefConnection(ObservableRefCount<?> parent2) {
            this.parent = parent2;
        }

        public void run() {
            this.parent.timeout(this);
        }

        public void accept(Disposable t) throws Exception {
            DisposableHelper.replace(this, t);
            synchronized (this.parent) {
                if (this.disconnectedEarly) {
                    ((ResettableConnectable) this.parent.source).resetIf(t);
                }
            }
        }
    }

    static final class RefCountObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
        private static final long serialVersionUID = -7419642935409022375L;
        final RefConnection connection;
        final Observer<? super T> downstream;
        final ObservableRefCount<T> parent;
        Disposable upstream;

        RefCountObserver(Observer<? super T> downstream2, ObservableRefCount<T> parent2, RefConnection connection2) {
            this.downstream = downstream2;
            this.parent = parent2;
            this.connection = connection2;
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable t) {
            if (compareAndSet(false, true)) {
                this.parent.terminated(this.connection);
                this.downstream.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (compareAndSet(false, true)) {
                this.parent.terminated(this.connection);
                this.downstream.onComplete();
            }
        }

        public void dispose() {
            this.upstream.dispose();
            if (compareAndSet(false, true)) {
                this.parent.cancel(this.connection);
            }
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                this.downstream.onSubscribe(this);
            }
        }
    }
}
