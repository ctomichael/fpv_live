package dji.thirdparty.rx.subscriptions;

import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.functions.Action0;
import java.util.concurrent.atomic.AtomicReference;

public final class BooleanSubscription implements Subscription {
    static final Action0 EMPTY_ACTION = new Action0() {
        /* class dji.thirdparty.rx.subscriptions.BooleanSubscription.AnonymousClass1 */

        public void call() {
        }
    };
    final AtomicReference<Action0> actionRef;

    public BooleanSubscription() {
        this.actionRef = new AtomicReference<>();
    }

    private BooleanSubscription(Action0 action) {
        this.actionRef = new AtomicReference<>(action);
    }

    public static BooleanSubscription create() {
        return new BooleanSubscription();
    }

    public static BooleanSubscription create(Action0 onUnsubscribe) {
        return new BooleanSubscription(onUnsubscribe);
    }

    public boolean isUnsubscribed() {
        return this.actionRef.get() == EMPTY_ACTION;
    }

    public final void unsubscribe() {
        Action0 action;
        if (this.actionRef.get() != EMPTY_ACTION && (action = this.actionRef.getAndSet(EMPTY_ACTION)) != null && action != EMPTY_ACTION) {
            action.call();
        }
    }
}
