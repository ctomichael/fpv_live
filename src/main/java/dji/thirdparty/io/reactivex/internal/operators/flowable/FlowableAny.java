package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Predicate;
import dji.thirdparty.io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableAny<T> extends AbstractFlowableWithUpstream<T, Boolean> {
    final Predicate<? super T> predicate;

    public FlowableAny(Publisher<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Boolean> s) {
        this.source.subscribe(new AnySubscriber(s, this.predicate));
    }

    static final class AnySubscriber<T> extends DeferredScalarSubscription<Boolean> implements Subscriber<T> {
        private static final long serialVersionUID = -2311252482644620661L;
        boolean done;
        final Predicate<? super T> predicate;
        Subscription s;

        AnySubscriber(Subscriber<? super Boolean> actual, Predicate<? super T> predicate2) {
            super(actual);
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (this.predicate.test(t)) {
                        this.done = true;
                        this.s.cancel();
                        complete(true);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.s.cancel();
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                complete(false);
            }
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }
    }
}
