package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTake<T> extends AbstractFlowableWithUpstream<T, T> {
    final long limit;

    public FlowableTake(Flowable<T> source, long limit2) {
        super(source);
        this.limit = limit2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber) new TakeSubscriber(s, this.limit));
    }

    static final class TakeSubscriber<T> extends AtomicBoolean implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -5636543848937116287L;
        boolean done;
        final Subscriber<? super T> downstream;
        final long limit;
        long remaining;
        Subscription upstream;

        TakeSubscriber(Subscriber<? super T> actual, long limit2) {
            this.downstream = actual;
            this.limit = limit2;
            this.remaining = limit2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                if (this.limit == 0) {
                    s.cancel();
                    this.done = true;
                    EmptySubscription.complete(this.downstream);
                    return;
                }
                this.downstream.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.remaining;
                this.remaining = j - 1;
                if (j > 0) {
                    boolean stop = this.remaining == 0;
                    this.downstream.onNext(t);
                    if (stop) {
                        this.upstream.cancel();
                        onComplete();
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (!this.done) {
                this.done = true;
                this.upstream.cancel();
                this.downstream.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.downstream.onComplete();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                if (get() || !compareAndSet(false, true) || n < this.limit) {
                    this.upstream.request(n);
                } else {
                    this.upstream.request(LongCompanionObject.MAX_VALUE);
                }
            }
        }

        public void cancel() {
            this.upstream.cancel();
        }
    }
}
