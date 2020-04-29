package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublishAlt<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T>, ResettableConnectable {
    final AtomicReference<PublishConnection<T>> current = new AtomicReference<>();
    final ObservableSource<T> source;

    public ObservablePublishAlt(ObservableSource<T> source2) {
        this.source = source2;
    }

    public void connect(Consumer<? super Disposable> connection) {
        PublishConnection<T> conn;
        while (true) {
            conn = this.current.get();
            if (conn != null && !conn.isDisposed()) {
                break;
            }
            PublishConnection<T> fresh = new PublishConnection<>(this.current);
            if (this.current.compareAndSet(conn, fresh)) {
                conn = fresh;
                break;
            }
        }
        boolean doConnect = !conn.connect.get() && conn.connect.compareAndSet(false, true);
        try {
            connection.accept(conn);
            if (doConnect) {
                this.source.subscribe(conn);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        PublishConnection<T> conn;
        while (true) {
            conn = this.current.get();
            if (conn != null) {
                break;
            }
            PublishConnection<T> fresh = new PublishConnection<>(this.current);
            if (this.current.compareAndSet(conn, fresh)) {
                conn = fresh;
                break;
            }
        }
        InnerDisposable<T> inner = new InnerDisposable<>(observer, conn);
        observer.onSubscribe(inner);
        if (!conn.add(inner)) {
            Throwable error = conn.error;
            if (error != null) {
                observer.onError(error);
            } else {
                observer.onComplete();
            }
        } else if (inner.isDisposed()) {
            conn.remove(inner);
        }
    }

    public void resetIf(Disposable connection) {
        this.current.compareAndSet((PublishConnection) connection, null);
    }

    public ObservableSource<T> source() {
        return this.source;
    }

    static final class PublishConnection<T> extends AtomicReference<InnerDisposable<T>[]> implements Observer<T>, Disposable {
        static final InnerDisposable[] EMPTY = new InnerDisposable[0];
        static final InnerDisposable[] TERMINATED = new InnerDisposable[0];
        private static final long serialVersionUID = -3251430252873581268L;
        final AtomicBoolean connect = new AtomicBoolean();
        final AtomicReference<PublishConnection<T>> current;
        Throwable error;
        final AtomicReference<Disposable> upstream;

        public PublishConnection(AtomicReference<PublishConnection<T>> current2) {
            this.current = current2;
            this.upstream = new AtomicReference<>();
            lazySet(EMPTY);
        }

        public void dispose() {
            getAndSet(TERMINATED);
            this.current.compareAndSet(this, null);
            DisposableHelper.dispose(this.upstream);
        }

        public boolean isDisposed() {
            return get() == TERMINATED;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.upstream, d);
        }

        public void onNext(T t) {
            for (InnerDisposable<T> inner : (InnerDisposable[]) get()) {
                inner.downstream.onNext(t);
            }
        }

        public void onError(Throwable e) {
            this.error = e;
            this.upstream.lazySet(DisposableHelper.DISPOSED);
            for (InnerDisposable<T> inner : (InnerDisposable[]) getAndSet(TERMINATED)) {
                inner.downstream.onError(e);
            }
        }

        public void onComplete() {
            this.upstream.lazySet(DisposableHelper.DISPOSED);
            for (InnerDisposable<T> inner : (InnerDisposable[]) getAndSet(TERMINATED)) {
                inner.downstream.onComplete();
            }
        }

        public boolean add(InnerDisposable<T> inner) {
            InnerDisposable<T>[] a;
            InnerDisposable<T>[] b;
            do {
                a = (InnerDisposable[]) get();
                if (a == TERMINATED) {
                    return false;
                }
                int n = a.length;
                b = new InnerDisposable[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = inner;
            } while (!compareAndSet(a, b));
            return true;
        }

        public void remove(InnerDisposable<T> inner) {
            InnerDisposable<T>[] a;
            InnerDisposable<T>[] b;
            do {
                a = (InnerDisposable[]) get();
                int n = a.length;
                if (n != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (a[i] == inner) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        b = EMPTY;
                        if (n != 1) {
                            b = new InnerDisposable[(n - 1)];
                            System.arraycopy(a, 0, b, 0, j);
                            System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(a, b));
        }
    }

    static final class InnerDisposable<T> extends AtomicReference<PublishConnection<T>> implements Disposable {
        private static final long serialVersionUID = 7463222674719692880L;
        final Observer<? super T> downstream;

        public InnerDisposable(Observer<? super T> downstream2, PublishConnection<T> parent) {
            this.downstream = downstream2;
            lazySet(parent);
        }

        public void dispose() {
            PublishConnection<T> p = (PublishConnection) getAndSet(null);
            if (p != null) {
                p.remove(this);
            }
        }

        public boolean isDisposed() {
            return get() == null;
        }
    }
}
