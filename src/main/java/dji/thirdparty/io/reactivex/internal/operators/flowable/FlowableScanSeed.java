package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import dji.thirdparty.io.reactivex.internal.subscriptions.EmptySubscription;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableScanSeed<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final BiFunction<R, ? super T, R> accumulator;
    final Callable<R> seedSupplier;

    public FlowableScanSeed(Publisher<T> source, Callable<R> seedSupplier2, BiFunction<R, ? super T, R> accumulator2) {
        super(source);
        this.accumulator = accumulator2;
        this.seedSupplier = seedSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        try {
            this.source.subscribe(new ScanSeedSubscriber(s, this.accumulator, ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seed supplied is null")));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }

    static final class ScanSeedSubscriber<T, R> extends SinglePostCompleteSubscriber<T, R> {
        private static final long serialVersionUID = -1776795561228106469L;
        final BiFunction<R, ? super T, R> accumulator;

        ScanSeedSubscriber(Subscriber<? super R> actual, BiFunction<R, ? super T, R> accumulator2, R value) {
            super(actual);
            this.accumulator = accumulator2;
            this.value = value;
        }

        public void onNext(T t) {
            R v = this.value;
            try {
                this.value = ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The accumulator returned a null value");
                this.produced++;
                this.actual.onNext(v);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.s.cancel();
                onError(e);
            }
        }

        public void onError(Throwable t) {
            this.value = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            complete(this.value);
        }
    }
}
