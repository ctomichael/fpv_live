package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDelay<T> extends AbstractFlowableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    public FlowableDelay(Publisher<T> source, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        super(source);
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> t) {
        Subscriber<? super T> s;
        if (this.delayError) {
            s = t;
        } else {
            s = new SerializedSubscriber<>(t);
        }
        this.source.subscribe(new DelaySubscriber(s, this.delay, this.unit, this.scheduler.createWorker(), this.delayError));
    }

    static final class DelaySubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final long delay;
        final boolean delayError;
        Subscription s;
        final TimeUnit unit;
        final Scheduler.Worker w;

        DelaySubscriber(Subscriber<? super T> actual2, long delay2, TimeUnit unit2, Scheduler.Worker w2, boolean delayError2) {
            this.actual = actual2;
            this.delay = delay2;
            this.unit = unit2;
            this.w = w2;
            this.delayError = delayError2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(final T t) {
            this.w.schedule(new Runnable() {
                /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelay.DelaySubscriber.AnonymousClass1 */

                public void run() {
                    DelaySubscriber.this.actual.onNext(t);
                }
            }, this.delay, this.unit);
        }

        public void onError(final Throwable t) {
            this.w.schedule(new Runnable() {
                /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelay.DelaySubscriber.AnonymousClass2 */

                public void run() {
                    try {
                        DelaySubscriber.this.actual.onError(t);
                    } finally {
                        DelaySubscriber.this.w.dispose();
                    }
                }
            }, this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.w.schedule(new Runnable() {
                /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelay.DelaySubscriber.AnonymousClass3 */

                public void run() {
                    try {
                        DelaySubscriber.this.actual.onComplete();
                    } finally {
                        DelaySubscriber.this.w.dispose();
                    }
                }
            }, this.delay, this.unit);
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.w.dispose();
            this.s.cancel();
        }
    }
}
