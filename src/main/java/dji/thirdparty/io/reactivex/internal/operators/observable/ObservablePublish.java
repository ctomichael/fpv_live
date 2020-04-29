package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.observables.ConnectableObservable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublish<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T> {
    final AtomicReference<PublishObserver<T>> current;
    final ObservableSource<T> onSubscribe;
    final ObservableSource<T> source;

    public static <T> ConnectableObservable<T> create(ObservableSource observableSource) {
        final AtomicReference<PublishObserver<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly((ConnectableObservable) new ObservablePublish(new ObservableSource<T>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservablePublish.AnonymousClass1 */

            public void subscribe(Observer<? super T> child) {
                InnerDisposable<T> inner = new InnerDisposable<>(child);
                child.onSubscribe(inner);
                while (true) {
                    PublishObserver<T> r = (PublishObserver) curr.get();
                    if (r == null || r.isDisposed()) {
                        PublishObserver<T> u = new PublishObserver<>(curr);
                        if (curr.compareAndSet(r, u)) {
                            r = u;
                        } else {
                            continue;
                        }
                    }
                    if (r.add(inner)) {
                        inner.setParent(r);
                        return;
                    }
                }
            }
        }, observableSource, curr));
    }

    private ObservablePublish(ObservableSource<T> onSubscribe2, ObservableSource<T> source2, AtomicReference<PublishObserver<T>> current2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
    }

    public ObservableSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.onSubscribe.subscribe(observer);
    }

    public void connect(Consumer<? super Disposable> connection) {
        PublishObserver<T> ps;
        boolean doConnect = true;
        while (true) {
            ps = this.current.get();
            if (ps != null && !ps.isDisposed()) {
                break;
            }
            PublishObserver<T> u = new PublishObserver<>(this.current);
            if (this.current.compareAndSet(ps, u)) {
                ps = u;
                break;
            }
        }
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        }
        try {
            connection.accept(ps);
            if (doConnect) {
                this.source.subscribe(ps);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    static final class PublishObserver<T> implements Observer<T>, Disposable {
        static final InnerDisposable[] EMPTY = new InnerDisposable[0];
        static final InnerDisposable[] TERMINATED = new InnerDisposable[0];
        final AtomicReference<PublishObserver<T>> current;
        final AtomicReference<InnerDisposable<T>[]> observers = new AtomicReference<>(EMPTY);
        final AtomicReference<Disposable> s = new AtomicReference<>();
        final AtomicBoolean shouldConnect;

        PublishObserver(AtomicReference<PublishObserver<T>> current2) {
            this.current = current2;
            this.shouldConnect = new AtomicBoolean();
        }

        public void dispose() {
            if (this.observers.get() != TERMINATED && ((InnerDisposable[]) this.observers.getAndSet(TERMINATED)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                DisposableHelper.dispose(this.s);
            }
        }

        public boolean isDisposed() {
            return this.observers.get() == TERMINATED;
        }

        public void onSubscribe(Disposable s2) {
            DisposableHelper.setOnce(this.s, s2);
        }

        public void onNext(T t) {
            for (InnerDisposable<T> inner : (InnerDisposable[]) this.observers.get()) {
                inner.child.onNext(t);
            }
        }

        public void onError(Throwable e) {
            this.current.compareAndSet(this, null);
            InnerDisposable<T>[] a = (InnerDisposable[]) this.observers.getAndSet(TERMINATED);
            if (a.length != 0) {
                for (InnerDisposable<T> inner : a) {
                    inner.child.onError(e);
                }
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            this.current.compareAndSet(this, null);
            for (InnerDisposable<T> inner : (InnerDisposable[]) this.observers.getAndSet(TERMINATED)) {
                inner.child.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerDisposable<T> producer) {
            InnerDisposable<T>[] c;
            InnerDisposable<T>[] u;
            do {
                c = (InnerDisposable[]) this.observers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerDisposable[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.observers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerDisposable<T> producer) {
            InnerDisposable<T>[] c;
            InnerDisposable<T>[] u;
            do {
                c = (InnerDisposable[]) this.observers.get();
                int len = c.length;
                if (len != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= len) {
                            break;
                        } else if (c[i].equals(producer)) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j < 0) {
                        return;
                    }
                    if (len == 1) {
                        u = EMPTY;
                    } else {
                        u = new InnerDisposable[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(c, u));
        }
    }

    static final class InnerDisposable<T> extends AtomicReference<Object> implements Disposable {
        private static final long serialVersionUID = -1100270633763673112L;
        final Observer<? super T> child;

        InnerDisposable(Observer<? super T> child2) {
            this.child = child2;
        }

        public boolean isDisposed() {
            return get() == this;
        }

        public void dispose() {
            Object o = getAndSet(this);
            if (o != null && o != this) {
                ((PublishObserver) o).remove(this);
            }
        }

        /* access modifiers changed from: package-private */
        public void setParent(PublishObserver<T> p) {
            if (!compareAndSet(null, p)) {
                p.remove(this);
            }
        }
    }
}
