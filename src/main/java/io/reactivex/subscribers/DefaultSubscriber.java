package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscription;

public abstract class DefaultSubscriber<T> implements FlowableSubscriber<T> {
    Subscription upstream;

    public final void onSubscribe(Subscription s) {
        if (EndConsumerHelper.validate(this.upstream, s, getClass())) {
            this.upstream = s;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        Subscription s = this.upstream;
        if (s != null) {
            s.request(n);
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Subscription s = this.upstream;
        this.upstream = SubscriptionHelper.CANCELLED;
        s.cancel();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(LongCompanionObject.MAX_VALUE);
    }
}
