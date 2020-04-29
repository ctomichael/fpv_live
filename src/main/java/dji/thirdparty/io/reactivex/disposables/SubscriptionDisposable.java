package dji.thirdparty.io.reactivex.disposables;

import org.reactivestreams.Subscription;

final class SubscriptionDisposable extends ReferenceDisposable<Subscription> {
    private static final long serialVersionUID = -707001650852963139L;

    SubscriptionDisposable(Subscription value) {
        super(value);
    }

    /* access modifiers changed from: protected */
    public void onDisposed(Subscription value) {
        value.cancel();
    }
}
