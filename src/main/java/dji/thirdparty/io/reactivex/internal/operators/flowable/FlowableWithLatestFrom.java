package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.internal.subscriptions.EmptySubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWithLatestFrom<T, U, R> extends AbstractFlowableWithUpstream<T, R> {
    final BiFunction<? super T, ? super U, ? extends R> combiner;
    final Publisher<? extends U> other;

    public FlowableWithLatestFrom(Publisher<T> source, BiFunction<? super T, ? super U, ? extends R> combiner2, Publisher<? extends U> other2) {
        super(source);
        this.combiner = combiner2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        final WithLatestFromSubscriber<T, U, R> wlf = new WithLatestFromSubscriber<>(new SerializedSubscriber<>(s), this.combiner);
        this.other.subscribe(new Subscriber<U>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.flowable.FlowableWithLatestFrom.AnonymousClass1 */

            public void onSubscribe(Subscription s) {
                if (wlf.setOther(s)) {
                    s.request(LongCompanionObject.MAX_VALUE);
                }
            }

            public void onNext(U t) {
                wlf.lazySet(t);
            }

            public void onError(Throwable t) {
                wlf.otherError(t);
            }

            public void onComplete() {
            }
        });
        this.source.subscribe(wlf);
    }

    static final class WithLatestFromSubscriber<T, U, R> extends AtomicReference<U> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -312246233408980075L;
        final Subscriber<? super R> actual;
        final BiFunction<? super T, ? super U, ? extends R> combiner;
        final AtomicReference<Subscription> other = new AtomicReference<>();
        final AtomicReference<Subscription> s = new AtomicReference<>();

        WithLatestFromSubscriber(Subscriber<? super R> actual2, BiFunction<? super T, ? super U, ? extends R> combiner2) {
            this.actual = actual2;
            this.combiner = combiner2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.setOnce(this.s, s2)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            U u = get();
            if (u != null) {
                try {
                    this.actual.onNext(this.combiner.apply(t, u));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    this.actual.onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            SubscriptionHelper.cancel(this.other);
            this.actual.onError(t);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            this.actual.onComplete();
        }

        public void request(long n) {
            this.s.get().request(n);
        }

        public void cancel() {
            this.s.get().cancel();
            SubscriptionHelper.cancel(this.other);
        }

        public boolean setOther(Subscription o) {
            return SubscriptionHelper.setOnce(this.other, o);
        }

        public void otherError(Throwable e) {
            if (this.s.compareAndSet(null, SubscriptionHelper.CANCELLED)) {
                EmptySubscription.error(e, this.actual);
            } else if (this.s.get() != SubscriptionHelper.CANCELLED) {
                cancel();
                this.actual.onError(e);
            } else {
                RxJavaPlugins.onError(e);
            }
        }
    }
}
