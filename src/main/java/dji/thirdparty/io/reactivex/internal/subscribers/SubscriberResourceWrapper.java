package dji.thirdparty.io.reactivex.internal.subscribers;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SubscriberResourceWrapper<T> extends AtomicReference<Disposable> implements Subscriber<T>, Disposable, Subscription {
    private static final long serialVersionUID = -8612022020200669122L;
    final Subscriber<? super T> actual;
    final AtomicReference<Subscription> subscription = new AtomicReference<>();

    public SubscriberResourceWrapper(Subscriber<? super T> actual2) {
        this.actual = actual2;
    }

    public void onSubscribe(Subscription s) {
        do {
            Subscription current = this.subscription.get();
            if (current == SubscriptionHelper.CANCELLED) {
                s.cancel();
                return;
            } else if (current != null) {
                s.cancel();
                SubscriptionHelper.reportSubscriptionSet();
                return;
            }
        } while (!this.subscription.compareAndSet(null, s));
        this.actual.onSubscribe(this);
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable t) {
        dispose();
        this.actual.onError(t);
    }

    public void onComplete() {
        dispose();
        this.actual.onComplete();
    }

    public void request(long n) {
        if (SubscriptionHelper.validate(n)) {
            this.subscription.get().request(n);
        }
    }

    public void dispose() {
        SubscriptionHelper.cancel(this.subscription);
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return this.subscription.get() == SubscriptionHelper.CANCELLED;
    }

    public void cancel() {
        dispose();
    }

    public void setResource(Disposable resource) {
        DisposableHelper.set(this, resource);
    }
}
