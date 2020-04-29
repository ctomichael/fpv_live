package dji.thirdparty.rx.subscriptions;

import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CompositeSubscription implements Subscription {
    private Set<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public CompositeSubscription() {
    }

    public CompositeSubscription(Subscription... subscriptions2) {
        this.subscriptions = new HashSet(Arrays.asList(subscriptions2));
    }

    public boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(Subscription s) {
        if (!s.isUnsubscribed()) {
            if (!this.unsubscribed) {
                synchronized (this) {
                    if (!this.unsubscribed) {
                        if (this.subscriptions == null) {
                            this.subscriptions = new HashSet(4);
                        }
                        this.subscriptions.add(s);
                        return;
                    }
                }
            }
            s.unsubscribe();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        if (r0 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
        r3.unsubscribe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(dji.thirdparty.rx.Subscription r3) {
        /*
            r2 = this;
            boolean r1 = r2.unsubscribed
            if (r1 != 0) goto L_0x000f
            r0 = 0
            monitor-enter(r2)
            boolean r1 = r2.unsubscribed     // Catch:{ all -> 0x001d }
            if (r1 != 0) goto L_0x000e
            java.util.Set<dji.thirdparty.rx.Subscription> r1 = r2.subscriptions     // Catch:{ all -> 0x001d }
            if (r1 != 0) goto L_0x0010
        L_0x000e:
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
        L_0x000f:
            return
        L_0x0010:
            java.util.Set<dji.thirdparty.rx.Subscription> r1 = r2.subscriptions     // Catch:{ all -> 0x001d }
            boolean r0 = r1.remove(r3)     // Catch:{ all -> 0x001d }
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x000f
            r3.unsubscribe()
            goto L_0x000f
        L_0x001d:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.subscriptions.CompositeSubscription.remove(dji.thirdparty.rx.Subscription):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clear() {
        /*
            r2 = this;
            boolean r1 = r2.unsubscribed
            if (r1 != 0) goto L_0x000f
            r0 = 0
            monitor-enter(r2)
            boolean r1 = r2.unsubscribed     // Catch:{ all -> 0x001a }
            if (r1 != 0) goto L_0x000e
            java.util.Set<dji.thirdparty.rx.Subscription> r1 = r2.subscriptions     // Catch:{ all -> 0x001a }
            if (r1 != 0) goto L_0x0010
        L_0x000e:
            monitor-exit(r2)     // Catch:{ all -> 0x001a }
        L_0x000f:
            return
        L_0x0010:
            java.util.Set<dji.thirdparty.rx.Subscription> r0 = r2.subscriptions     // Catch:{ all -> 0x001a }
            r1 = 0
            r2.subscriptions = r1     // Catch:{ all -> 0x001a }
            monitor-exit(r2)     // Catch:{ all -> 0x001a }
            unsubscribeFromAll(r0)
            goto L_0x000f
        L_0x001a:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001a }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.subscriptions.CompositeSubscription.clear():void");
    }

    public void unsubscribe() {
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed) {
                    this.unsubscribed = true;
                    Collection<Subscription> unsubscribe = this.subscriptions;
                    this.subscriptions = null;
                    unsubscribeFromAll(unsubscribe);
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
