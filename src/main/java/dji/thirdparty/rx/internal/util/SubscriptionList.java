package dji.thirdparty.rx.internal.util;

import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class SubscriptionList implements Subscription {
    private LinkedList<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public SubscriptionList() {
    }

    public SubscriptionList(Subscription... subscriptions2) {
        this.subscriptions = new LinkedList<>(Arrays.asList(subscriptions2));
    }

    public SubscriptionList(Subscription s) {
        this.subscriptions = new LinkedList<>();
        this.subscriptions.add(s);
    }

    public boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(Subscription s) {
        if (!s.isUnsubscribed()) {
            if (!this.unsubscribed) {
                synchronized (this) {
                    if (!this.unsubscribed) {
                        LinkedList<Subscription> subs = this.subscriptions;
                        if (subs == null) {
                            subs = new LinkedList<>();
                            this.subscriptions = subs;
                        }
                        subs.add(s);
                        return;
                    }
                }
            }
            s.unsubscribe();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        if (r1 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        r4.unsubscribe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(dji.thirdparty.rx.Subscription r4) {
        /*
            r3 = this;
            boolean r2 = r3.unsubscribed
            if (r2 != 0) goto L_0x000f
            r1 = 0
            monitor-enter(r3)
            java.util.LinkedList<dji.thirdparty.rx.Subscription> r0 = r3.subscriptions     // Catch:{ all -> 0x001b }
            boolean r2 = r3.unsubscribed     // Catch:{ all -> 0x001b }
            if (r2 != 0) goto L_0x000e
            if (r0 != 0) goto L_0x0010
        L_0x000e:
            monitor-exit(r3)     // Catch:{ all -> 0x001b }
        L_0x000f:
            return
        L_0x0010:
            boolean r1 = r0.remove(r4)     // Catch:{ all -> 0x001b }
            monitor-exit(r3)     // Catch:{ all -> 0x001b }
            if (r1 == 0) goto L_0x000f
            r4.unsubscribe()
            goto L_0x000f
        L_0x001b:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x001b }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.util.SubscriptionList.remove(dji.thirdparty.rx.Subscription):void");
    }

    public void unsubscribe() {
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed) {
                    this.unsubscribed = true;
                    List<Subscription> list = this.subscriptions;
                    this.subscriptions = null;
                    unsubscribeFromAll(list);
                }
            }
        }
    }

    private static void unsubscribeFromAll(Collection<Subscription> subscriptions2) {
        if (subscriptions2 != null) {
            List<Throwable> es = null;
            for (Subscription s : subscriptions2) {
                try {
                    s.unsubscribe();
                } catch (Throwable e) {
                    if (es == null) {
                        es = new ArrayList<>();
                    }
                    es.add(e);
                }
            }
            Exceptions.throwIfAny(es);
        }
    }

    public void clear() {
        List<Subscription> list;
        if (!this.unsubscribed) {
            synchronized (this) {
                list = this.subscriptions;
                this.subscriptions = null;
            }
            unsubscribeFromAll(list);
        }
    }

    public boolean hasSubscriptions() {
        boolean z = false;
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed && this.subscriptions != null && !this.subscriptions.isEmpty()) {
                    z = true;
                }
            }
        }
        return z;
    }
}
