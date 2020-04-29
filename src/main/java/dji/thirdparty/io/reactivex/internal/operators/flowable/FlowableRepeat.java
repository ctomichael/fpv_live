package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeat<T> extends AbstractFlowableWithUpstream<T, T> {
    final long count;

    public FlowableRepeat(Publisher<T> source, long count2) {
        super(source);
        this.count = count2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        long j = LongCompanionObject.MAX_VALUE;
        SubscriptionArbiter sa = new SubscriptionArbiter();
        s.onSubscribe(sa);
        if (this.count != LongCompanionObject.MAX_VALUE) {
            j = this.count - 1;
        }
        new RepeatSubscriber<>(s, j, sa, this.source).subscribeNext();
    }

    static final class RepeatSubscriber<T> extends AtomicInteger implements Subscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        long remaining;
        final SubscriptionArbiter sa;
        final Publisher<? extends T> source;

        RepeatSubscriber(Subscriber<? super T> actual2, long count, SubscriptionArbiter sa2, Publisher<? extends T> source2) {
            this.actual = actual2;
            this.sa = sa2;
            this.source = source2;
            this.remaining = count;
        }

        public void onSubscribe(Subscription s) {
            this.sa.setSubscription(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            this.sa.produced(1);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            long r = this.remaining;
            if (r != LongCompanionObject.MAX_VALUE) {
                this.remaining = r - 1;
            }
            if (r != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.sa.isCancelled()) {
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }
}
