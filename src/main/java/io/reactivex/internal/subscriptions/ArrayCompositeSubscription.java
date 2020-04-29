package io.reactivex.internal.subscriptions;

import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.reactivestreams.Subscription;

public final class ArrayCompositeSubscription extends AtomicReferenceArray<Subscription> implements Disposable {
    private static final long serialVersionUID = 2746389416410565408L;

    public ArrayCompositeSubscription(int capacity) {
        super(capacity);
    }

    public boolean setResource(int index, Subscription resource) {
        Subscription o;
        do {
            o = (Subscription) get(index);
            if (o == SubscriptionHelper.CANCELLED) {
                if (resource != null) {
                    resource.cancel();
                }
                return false;
            }
        } while (!compareAndSet(index, o, resource));
        if (o != null) {
            o.cancel();
        }
        return true;
    }

    public Subscription replaceResource(int index, Subscription resource) {
        Subscription o;
        do {
            o = (Subscription) get(index);
            if (o == SubscriptionHelper.CANCELLED) {
                if (resource != null) {
                    resource.cancel();
                }
                return null;
            }
        } while (!compareAndSet(index, o, resource));
        return o;
    }

    public void dispose() {
        Subscription o;
        if (get(0) != SubscriptionHelper.CANCELLED) {
            int s = length();
            for (int i = 0; i < s; i++) {
                if (!(((Subscription) get(i)) == SubscriptionHelper.CANCELLED || (o = (Subscription) getAndSet(i, SubscriptionHelper.CANCELLED)) == SubscriptionHelper.CANCELLED || o == null)) {
                    o.cancel();
                }
            }
        }
    }

    public boolean isDisposed() {
        return get(0) == SubscriptionHelper.CANCELLED;
    }
}
