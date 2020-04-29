package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Notification;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDematerialize<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super T, ? extends Notification<R>> selector;

    public FlowableDematerialize(Flowable<T> source, Function<? super T, ? extends Notification<R>> selector2) {
        super(source);
        this.selector = selector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        this.source.subscribe((FlowableSubscriber) new DematerializeSubscriber(subscriber, this.selector));
    }

    static final class DematerializeSubscriber<T, R> implements FlowableSubscriber<T>, Subscription {
        boolean done;
        final Subscriber<? super R> downstream;
        final Function<? super T, ? extends Notification<R>> selector;
        Subscription upstream;

        DematerializeSubscriber(Subscriber<? super R> downstream2, Function<? super T, ? extends Notification<R>> selector2) {
            this.downstream = downstream2;
            this.selector = selector2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
            }
        }

        public void onNext(T item) {
            if (!this.done) {
                try {
                    Notification<R> notification = (Notification) ObjectHelper.requireNonNull(this.selector.apply(item), "The selector returned a null Notification");
                    if (notification.isOnError()) {
                        this.upstream.cancel();
                        onError(notification.getError());
                    } else if (notification.isOnComplete()) {
                        this.upstream.cancel();
                        onComplete();
                    } else {
                        this.downstream.onNext(notification.getValue());
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.upstream.cancel();
                    onError(ex);
                }
            } else if (item instanceof Notification) {
                Notification<?> notification2 = item;
                if (notification2.isOnError()) {
                    RxJavaPlugins.onError(notification2.getError());
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.downstream.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.downstream.onComplete();
            }
        }

        public void request(long n) {
            this.upstream.request(n);
        }

        public void cancel() {
            this.upstream.cancel();
        }
    }
}
