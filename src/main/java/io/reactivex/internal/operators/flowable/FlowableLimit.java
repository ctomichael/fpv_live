package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableLimit<T> extends AbstractFlowableWithUpstream<T, T> {
    final long n;

    public FlowableLimit(Flowable<T> source, long n2) {
        super(source);
        this.n = n2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber) new LimitSubscriber(s, this.n));
    }

    static final class LimitSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = 2288246011222124525L;
        final Subscriber<? super T> downstream;
        long remaining;
        Subscription upstream;

        LimitSubscriber(Subscriber<? super T> actual, long remaining2) {
            this.downstream = actual;
            this.remaining = remaining2;
            lazySet(remaining2);
        }

        public void onSubscribe(Subscription s) {
            if (!SubscriptionHelper.validate(this.upstream, s)) {
                return;
            }
            if (this.remaining == 0) {
                s.cancel();
                EmptySubscription.complete(this.downstream);
                return;
            }
            this.upstream = s;
            this.downstream.onSubscribe(this);
        }

        public void onNext(T t) {
            long r = this.remaining;
            if (r > 0) {
                long r2 = r - 1;
                this.remaining = r2;
                this.downstream.onNext(t);
                if (r2 == 0) {
                    this.upstream.cancel();
                    this.downstream.onComplete();
                }
            }
        }

        public void onError(Throwable t) {
            if (this.remaining > 0) {
                this.remaining = 0;
                this.downstream.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.remaining > 0) {
                this.remaining = 0;
                this.downstream.onComplete();
            }
        }

        public void request(long n) {
            long r;
            long toRequest;
            if (SubscriptionHelper.validate(n)) {
                do {
                    r = get();
                    if (r != 0) {
                        if (r <= n) {
                            toRequest = r;
                        } else {
                            toRequest = n;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, r - toRequest));
                this.upstream.request(toRequest);
            }
        }

        public void cancel() {
            this.upstream.cancel();
        }
    }
}
