package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public final class ObservableRefCount<T> extends AbstractObservableWithUpstream<T, T> {
    volatile CompositeDisposable baseDisposable = new CompositeDisposable();
    final ReentrantLock lock = new ReentrantLock();
    final ConnectableObservable<? extends T> source;
    final AtomicInteger subscriptionCount = new AtomicInteger();

    public ObservableRefCount(ConnectableObservable<T> source2) {
        super(source2);
        this.source = source2;
    }

    public void subscribeActual(Observer<? super T> subscriber) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() == 1) {
            AtomicBoolean writeLocked = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, writeLocked));
            } finally {
                if (writeLocked.get()) {
                    this.lock.unlock();
                }
            }
        } else {
            try {
                doSubscribe(subscriber, this.baseDisposable);
            } finally {
                this.lock.unlock();
            }
        }
    }

    private Consumer<Disposable> onSubscribe(final Observer<? super T> observer, final AtomicBoolean writeLocked) {
        return new Consumer<Disposable>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRefCount.AnonymousClass1 */

            public void accept(Disposable subscription) {
                try {
                    ObservableRefCount.this.baseDisposable.add(subscription);
                    ObservableRefCount.this.doSubscribe(observer, ObservableRefCount.this.baseDisposable);
                } finally {
                    ObservableRefCount.this.lock.unlock();
                    writeLocked.set(false);
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void doSubscribe(Observer<? super T> observer, CompositeDisposable currentBase) {
        ObservableRefCount<T>.ConnectionObserver s = new ConnectionObserver(observer, currentBase, disconnect(currentBase));
        observer.onSubscribe(s);
        this.source.subscribe(s);
    }

    private Disposable disconnect(final CompositeDisposable current) {
        return Disposables.fromRunnable(new Runnable() {
            /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableRefCount.AnonymousClass2 */

            public void run() {
                ObservableRefCount.this.lock.lock();
                try {
                    if (ObservableRefCount.this.baseDisposable == current && ObservableRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        ObservableRefCount.this.baseDisposable.dispose();
                        ObservableRefCount.this.baseDisposable = new CompositeDisposable();
                    }
                } finally {
                    ObservableRefCount.this.lock.unlock();
                }
            }
        });
    }

    final class ConnectionObserver extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 3813126992133394324L;
        final CompositeDisposable currentBase;
        final Disposable resource;
        final Observer<? super T> subscriber;

        ConnectionObserver(Observer<? super T> subscriber2, CompositeDisposable currentBase2, Disposable resource2) {
            this.subscriber = subscriber2;
            this.currentBase = currentBase2;
            this.resource = resource2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this, s);
        }

        public void onError(Throwable e) {
            cleanup();
            this.subscriber.onError(e);
        }

        public void onNext(T t) {
            this.subscriber.onNext(t);
        }

        public void onComplete() {
            cleanup();
            this.subscriber.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.resource.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        /* access modifiers changed from: package-private */
        public void cleanup() {
            ObservableRefCount.this.lock.lock();
            try {
                if (ObservableRefCount.this.baseDisposable == this.currentBase) {
                    ObservableRefCount.this.baseDisposable.dispose();
                    ObservableRefCount.this.baseDisposable = new CompositeDisposable();
                    ObservableRefCount.this.subscriptionCount.set(0);
                }
            } finally {
                ObservableRefCount.this.lock.unlock();
            }
        }
    }
}
