package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.subscriptions.EmptySubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableZipIterable<T, U, V> extends Flowable<V> {
    final Iterable<U> other;
    final Publisher<? extends T> source;
    final BiFunction<? super T, ? super U, ? extends V> zipper;

    public FlowableZipIterable(Publisher<? extends T> source2, Iterable<U> other2, BiFunction<? super T, ? super U, ? extends V> zipper2) {
        this.source = source2;
        this.other = other2;
        this.zipper = zipper2;
    }

    public void subscribeActual(Subscriber<? super V> t) {
        try {
            Iterator<U> it2 = (Iterator) ObjectHelper.requireNonNull(this.other.iterator(), "The iterator returned by other is null");
            try {
                if (!it2.hasNext()) {
                    EmptySubscription.complete(t);
                } else {
                    this.source.subscribe(new ZipIterableSubscriber(t, it2, this.zipper));
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptySubscription.error(e, t);
            }
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            EmptySubscription.error(e2, t);
        }
    }

    static final class ZipIterableSubscriber<T, U, V> implements Subscriber<T>, Subscription {
        final Subscriber<? super V> actual;
        boolean done;
        final Iterator<U> iterator;
        Subscription s;
        final BiFunction<? super T, ? super U, ? extends V> zipper;

        ZipIterableSubscriber(Subscriber<? super V> actual2, Iterator<U> iterator2, BiFunction<? super T, ? super U, ? extends V> zipper2) {
            this.actual = actual2;
            this.iterator = iterator2;
            this.zipper = zipper2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    try {
                        this.actual.onNext(ObjectHelper.requireNonNull(this.zipper.apply(t, ObjectHelper.requireNonNull(this.iterator.next(), "The iterator returned a null value")), "The zipper function returned a null value"));
                        try {
                            if (!this.iterator.hasNext()) {
                                this.done = true;
                                this.s.cancel();
                                this.actual.onComplete();
                            }
                        } catch (Throwable e) {
                            error(e);
                        }
                    } catch (Throwable e2) {
                        error(e2);
                    }
                } catch (Throwable e3) {
                    error(e3);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void error(Throwable e) {
            Exceptions.throwIfFatal(e);
            this.done = true;
            this.s.cancel();
            this.actual.onError(e);
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
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.s.cancel();
        }
    }
}
