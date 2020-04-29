package dji.thirdparty.io.reactivex.internal.subscriptions;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FullArbiter<T> extends FullArbiterPad2 implements Subscription {
    static final Subscription INITIAL = new Subscription() {
        /* class dji.thirdparty.io.reactivex.internal.subscriptions.FullArbiter.AnonymousClass1 */

        public void request(long n) {
        }

        public void cancel() {
        }
    };
    static final Object REQUEST = new Object();
    final Subscriber<? super T> actual;
    volatile boolean cancelled;
    final SpscLinkedArrayQueue<Object> queue;
    long requested;
    Disposable resource;
    volatile Subscription s = INITIAL;

    public FullArbiter(Subscriber<? super T> actual2, Disposable resource2, int capacity) {
        this.actual = actual2;
        this.resource = resource2;
        this.queue = new SpscLinkedArrayQueue<>(capacity);
    }

    public void request(long n) {
        if (SubscriptionHelper.validate(n)) {
            BackpressureHelper.add(this.missedRequested, n);
            this.queue.offer(REQUEST, REQUEST);
            drain();
        }
    }

    public void cancel() {
        if (!this.cancelled) {
            this.cancelled = true;
            dispose();
        }
    }

    /* access modifiers changed from: package-private */
    public void dispose() {
        Disposable d = this.resource;
        this.resource = null;
        if (d != null) {
            d.dispose();
        }
    }

    public boolean setSubscription(Subscription s2) {
        if (this.cancelled) {
            if (s2 != null) {
                s2.cancel();
            }
            return false;
        }
        ObjectHelper.requireNonNull(s2, "s is null");
        this.queue.offer(this.s, NotificationLite.subscription(s2));
        drain();
        return true;
    }

    public boolean onNext(T value, Subscription s2) {
        if (this.cancelled) {
            return false;
        }
        this.queue.offer(s2, NotificationLite.next(value));
        drain();
        return true;
    }

    public void onError(Throwable value, Subscription s2) {
        if (this.cancelled) {
            RxJavaPlugins.onError(value);
            return;
        }
        this.queue.offer(s2, NotificationLite.error(value));
        drain();
    }

    public void onComplete(Subscription s2) {
        this.queue.offer(s2, NotificationLite.complete());
        drain();
    }

    /* access modifiers changed from: package-private */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            int missed = 1;
            SpscLinkedArrayQueue<Object> q = this.queue;
            Subscriber<? super T> a = this.actual;
            while (true) {
                Object o = q.poll();
                if (o == null) {
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    Object v = q.poll();
                    if (o == REQUEST) {
                        long mr = this.missedRequested.getAndSet(0);
                        if (mr != 0) {
                            this.requested = BackpressureHelper.addCap(this.requested, mr);
                            this.s.request(mr);
                        }
                    } else if (o == this.s) {
                        if (NotificationLite.isSubscription(v)) {
                            Subscription next = NotificationLite.getSubscription(v);
                            if (!this.cancelled) {
                                this.s = next;
                                long r = this.requested;
                                if (r != 0) {
                                    next.request(r);
                                }
                            } else {
                                next.cancel();
                            }
                        } else if (NotificationLite.isError(v)) {
                            q.clear();
                            dispose();
                            Throwable ex = NotificationLite.getError(v);
                            if (!this.cancelled) {
                                this.cancelled = true;
                                a.onError(ex);
                            } else {
                                RxJavaPlugins.onError(ex);
                            }
                        } else if (NotificationLite.isComplete(v)) {
                            q.clear();
                            dispose();
                            if (!this.cancelled) {
                                this.cancelled = true;
                                a.onComplete();
                            }
                        } else {
                            long r2 = this.requested;
                            if (r2 != 0) {
                                a.onNext(NotificationLite.getValue(v));
                                this.requested = r2 - 1;
                            }
                        }
                    }
                }
            }
        }
    }
}
