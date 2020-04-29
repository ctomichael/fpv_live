package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.subscriptions.EmptySubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionArbiter;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.processors.FlowableProcessor;
import dji.thirdparty.io.reactivex.processors.UnicastProcessor;
import dji.thirdparty.io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeatWhen<T> extends AbstractFlowableWithUpstream<T, T> {
    final Function<? super Flowable<Object>, ? extends Publisher<?>> handler;

    public FlowableRepeatWhen(Publisher<T> source, Function<? super Flowable<Object>, ? extends Publisher<?>> handler2) {
        super(source);
        this.handler = handler2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        SerializedSubscriber<T> z = new SerializedSubscriber<>(s);
        FlowableProcessor<Object> processor = UnicastProcessor.create(8).toSerialized();
        try {
            Publisher<?> when = (Publisher) ObjectHelper.requireNonNull(this.handler.apply(processor), "handler returned a null Publisher");
            WhenReceiver<T, Object> receiver = new WhenReceiver<>(this.source);
            RepeatWhenSubscriber<T> subscriber = new RepeatWhenSubscriber<>(z, processor, receiver);
            receiver.subscriber = subscriber;
            s.onSubscribe(subscriber);
            when.subscribe(receiver);
            receiver.onNext(0);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptySubscription.error(ex, s);
        }
    }

    static final class WhenReceiver<T, U> extends AtomicInteger implements Subscriber<Object>, Subscription {
        private static final long serialVersionUID = 2827772011130406689L;
        final AtomicLong requested = new AtomicLong();
        final Publisher<T> source;
        WhenSourceSubscriber<T, U> subscriber;
        final AtomicReference<Subscription> subscription = new AtomicReference<>();

        WhenReceiver(Publisher<T> source2) {
            this.source = source2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.subscription, this.requested, s);
        }

        public void onNext(Object t) {
            if (getAndIncrement() == 0) {
                while (!SubscriptionHelper.isCancelled(this.subscription.get())) {
                    this.source.subscribe(this.subscriber);
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }

        public void onError(Throwable t) {
            this.subscriber.cancel();
            this.subscriber.actual.onError(t);
        }

        public void onComplete() {
            this.subscriber.cancel();
            this.subscriber.actual.onComplete();
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.subscription, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.subscription);
        }
    }

    static abstract class WhenSourceSubscriber<T, U> extends SubscriptionArbiter implements Subscriber<T> {
        private static final long serialVersionUID = -5604623027276966720L;
        protected final Subscriber<? super T> actual;
        protected final FlowableProcessor<U> processor;
        private long produced;
        protected final Subscription receiver;

        WhenSourceSubscriber(Subscriber<? super T> actual2, FlowableProcessor<U> processor2, Subscription receiver2) {
            this.actual = actual2;
            this.processor = processor2;
            this.receiver = receiver2;
        }

        public final void onSubscribe(Subscription s) {
            setSubscription(s);
        }

        public final void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        /* access modifiers changed from: protected */
        public final void again(U signal) {
            long p = this.produced;
            if (p != 0) {
                this.produced = 0;
                produced(p);
            }
            this.receiver.request(1);
            this.processor.onNext(signal);
        }

        public final void cancel() {
            super.cancel();
            this.receiver.cancel();
        }
    }

    static final class RepeatWhenSubscriber<T> extends WhenSourceSubscriber<T, Object> {
        private static final long serialVersionUID = -2680129890138081029L;

        RepeatWhenSubscriber(Subscriber<? super T> actual, FlowableProcessor<Object> processor, Subscription receiver) {
            super(actual, processor, receiver);
        }

        public void onError(Throwable t) {
            this.receiver.cancel();
            this.actual.onError(t);
        }

        public void onComplete() {
            again(0);
        }
    }
}
