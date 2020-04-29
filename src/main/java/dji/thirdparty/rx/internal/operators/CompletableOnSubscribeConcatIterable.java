package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableOnSubscribeConcatIterable implements Completable.CompletableOnSubscribe {
    final Iterable<? extends Completable> sources;

    public CompletableOnSubscribeConcatIterable(Iterable<? extends Completable> sources2) {
        this.sources = sources2;
    }

    public void call(Completable.CompletableSubscriber s) {
        try {
            Iterator<? extends Completable> it2 = this.sources.iterator();
            if (it2 == null) {
                s.onSubscribe(Subscriptions.unsubscribed());
                s.onError(new NullPointerException("The iterator returned is null"));
                return;
            }
            ConcatInnerSubscriber inner = new ConcatInnerSubscriber(s, it2);
            s.onSubscribe(inner.sd);
            inner.next();
        } catch (Throwable e) {
            s.onSubscribe(Subscriptions.unsubscribed());
            s.onError(e);
        }
    }

    static final class ConcatInnerSubscriber extends AtomicInteger implements Completable.CompletableSubscriber {
        private static final long serialVersionUID = -7965400327305809232L;
        final Completable.CompletableSubscriber actual;
        int index;
        final SerialSubscription sd = new SerialSubscription();
        final Iterator<? extends Completable> sources;

        public ConcatInnerSubscriber(Completable.CompletableSubscriber actual2, Iterator<? extends Completable> sources2) {
            this.actual = actual2;
            this.sources = sources2;
        }

        public void onSubscribe(Subscription d) {
            this.sd.set(d);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            next();
        }

        /* access modifiers changed from: package-private */
        public void next() {
            if (!this.sd.isUnsubscribed() && getAndIncrement() == 0) {
                Iterator<? extends Completable> a = this.sources;
                while (!this.sd.isUnsubscribed()) {
                    try {
                        if (!a.hasNext()) {
                            this.actual.onCompleted();
                            return;
                        }
                        try {
                            Completable c = (Completable) a.next();
                            if (c == null) {
                                this.actual.onError(new NullPointerException("The completable returned is null"));
                                return;
                            }
                            c.subscribe(this);
                            if (decrementAndGet() == 0) {
                                return;
                            }
                        } catch (Throwable ex) {
                            this.actual.onError(ex);
                            return;
                        }
                    } catch (Throwable ex2) {
                        this.actual.onError(ex2);
                        return;
                    }
                }
            }
        }
    }
}
