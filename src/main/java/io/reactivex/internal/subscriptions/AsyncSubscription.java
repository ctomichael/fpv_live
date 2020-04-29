package io.reactivex.internal.subscriptions;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class AsyncSubscription extends AtomicLong implements Subscription, Disposable {
    private static final long serialVersionUID = 7028635084060361255L;
    final AtomicReference<Subscription> actual;
    final AtomicReference<Disposable> resource;

    public AsyncSubscription() {
        this.resource = new AtomicReference<>();
        this.actual = new AtomicReference<>();
    }

    public AsyncSubscription(Disposable resource2) {
        this();
        this.resource.lazySet(resource2);
    }

    public void request(long n) {
        SubscriptionHelper.deferredRequest(this.actual, this, n);
    }

    public void cancel() {
        dispose();
    }

    public void dispose() {
        SubscriptionHelper.cancel(this.actual);
        DisposableHelper.dispose(this.resource);
    }

    public boolean isDisposed() {
        return this.actual.get() == SubscriptionHelper.CANCELLED;
    }

    public boolean setResource(Disposable r) {
        return DisposableHelper.set(this.resource, r);
    }

    public boolean replaceResource(Disposable r) {
        return DisposableHelper.replace(this.resource, r);
    }

    public void setSubscription(Subscription s) {
        SubscriptionHelper.deferredSetOnce(this.actual, this, s);
    }
}
