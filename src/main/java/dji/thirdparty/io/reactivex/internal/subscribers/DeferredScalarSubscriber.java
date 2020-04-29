package dji.thirdparty.io.reactivex.internal.subscribers;

import dji.thirdparty.io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class DeferredScalarSubscriber<T, R> extends DeferredScalarSubscription<R> implements Subscriber<T> {
    private static final long serialVersionUID = 2984505488220891551L;
    protected boolean hasValue;
    protected Subscription s;

    public DeferredScalarSubscriber(Subscriber<? super R> actual) {
        super(actual);
    }

    public void onSubscribe(Subscription s2) {
        if (SubscriptionHelper.validate(this.s, s2)) {
            this.s = s2;
            this.actual.onSubscribe(this);
            s2.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onError(Throwable t) {
        this.value = null;
        this.actual.onError(t);
    }

    public void onComplete() {
        if (this.hasValue) {
            complete(this.value);
        } else {
            this.actual.onComplete();
        }
    }

    public void cancel() {
        super.cancel();
        this.s.cancel();
    }
}
