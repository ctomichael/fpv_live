package dji.thirdparty.rx.internal.util;

import dji.thirdparty.rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription s;

    public SynchronizedSubscription(Subscription s2) {
        this.s = s2;
    }

    public synchronized void unsubscribe() {
        this.s.unsubscribe();
    }

    public synchronized boolean isUnsubscribed() {
        return this.s.isUnsubscribed();
    }
}
