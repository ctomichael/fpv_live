package dji.thirdparty.io.reactivex.subscribers;

import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class DefaultSubscriber<T> implements Subscriber<T> {
    private Subscription s;

    public final void onSubscribe(Subscription s2) {
        if (SubscriptionHelper.validate(this.s, s2)) {
            this.s = s2;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        Subscription s2 = this.s;
        if (s2 != null) {
            s2.request(n);
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Subscription s2 = this.s;
        this.s = SubscriptionHelper.CANCELLED;
        s2.cancel();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(LongCompanionObject.MAX_VALUE);
    }
}
