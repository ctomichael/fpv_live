package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.flowables.ConnectableFlowable;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRefCount<T> extends AbstractFlowableWithUpstream<T, T> {
    volatile CompositeDisposable baseDisposable = new CompositeDisposable();
    final ReentrantLock lock = new ReentrantLock();
    final ConnectableFlowable<? extends T> source;
    final AtomicInteger subscriptionCount = new AtomicInteger();

    final class ConnectionSubscriber extends AtomicReference<Subscription> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 152064694420235350L;
        final CompositeDisposable currentBase;
        final AtomicLong requested = new AtomicLong();
        final Disposable resource;
        final Subscriber<? super T> subscriber;

        ConnectionSubscriber(Subscriber<? super T> subscriber2, CompositeDisposable currentBase2, Disposable resource2) {
            this.subscriber = subscriber2;
            this.currentBase = currentBase2;
            this.resource = resource2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this, this.requested, s);
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

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
            this.resource.dispose();
        }

        /* access modifiers changed from: package-private */
        public void cleanup() {
            FlowableRefCount.this.lock.lock();
            try {
                if (FlowableRefCount.this.baseDisposable == this.currentBase) {
                    FlowableRefCount.this.baseDisposable.dispose();
                    FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    FlowableRefCount.this.subscriptionCount.set(0);
                }
            } finally {
                FlowableRefCount.this.lock.unlock();
            }
        }
    }

    public FlowableRefCount(ConnectableFlowable<T> source2) {
        super(source2);
        this.source = source2;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
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

    private Consumer<Disposable> onSubscribe(final Subscriber<? super T> subscriber, final AtomicBoolean writeLocked) {
        return new Consumer<Disposable>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRefCount.AnonymousClass1 */

            public void accept(Disposable subscription) {
                try {
                    FlowableRefCount.this.baseDisposable.add(subscription);
                    FlowableRefCount.this.doSubscribe(subscriber, FlowableRefCount.this.baseDisposable);
                } finally {
                    FlowableRefCount.this.lock.unlock();
                    writeLocked.set(false);
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void doSubscribe(Subscriber<? super T> subscriber, CompositeDisposable currentBase) {
        FlowableRefCount<T>.ConnectionSubscriber connection = new ConnectionSubscriber(subscriber, currentBase, disconnect(currentBase));
        subscriber.onSubscribe(connection);
        this.source.subscribe(connection);
    }

    private Disposable disconnect(final CompositeDisposable current) {
        return Disposables.fromRunnable(new Runnable() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableRefCount.AnonymousClass2 */

            public void run() {
                FlowableRefCount.this.lock.lock();
                try {
                    if (FlowableRefCount.this.baseDisposable == current && FlowableRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        FlowableRefCount.this.baseDisposable.dispose();
                        FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    }
                } finally {
                    FlowableRefCount.this.lock.unlock();
                }
            }
        });
    }
}
