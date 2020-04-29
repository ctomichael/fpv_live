package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionArbiter;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDelaySubscriptionOther<T, U> extends Flowable<T> {
    final Publisher<? extends T> main;
    final Publisher<U> other;

    public FlowableDelaySubscriptionOther(Publisher<? extends T> main2, Publisher<U> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void subscribeActual(final Subscriber<? super T> child) {
        final SubscriptionArbiter serial = new SubscriptionArbiter();
        child.onSubscribe(serial);
        this.other.subscribe(new Subscriber<U>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther.AnonymousClass1 */
            boolean done;

            public void onSubscribe(final Subscription s) {
                serial.setSubscription(new Subscription() {
                    /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther.AnonymousClass1.AnonymousClass1 */

                    public void request(long n) {
                    }

                    public void cancel() {
                        s.cancel();
                    }
                });
                s.request(LongCompanionObject.MAX_VALUE);
            }

            public void onNext(U u) {
                onComplete();
            }

            public void onError(Throwable e) {
                if (this.done) {
                    RxJavaPlugins.onError(e);
                    return;
                }
                this.done = true;
                child.onError(e);
            }

            public void onComplete() {
                if (!this.done) {
                    this.done = true;
                    FlowableDelaySubscriptionOther.this.main.subscribe(new Subscriber<T>() {
                        /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther.AnonymousClass1.AnonymousClass2 */

                        public void onSubscribe(Subscription s) {
                            serial.setSubscription(s);
                        }

                        public void onNext(T t) {
                            child.onNext(t);
                        }

                        public void onError(Throwable t) {
                            child.onError(t);
                        }

                        public void onComplete() {
                            child.onComplete();
                        }
                    });
                }
            }
        });
    }
}
