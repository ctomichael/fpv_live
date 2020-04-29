package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatArray<T> extends Flowable<T> {
    final boolean delayError;
    final Publisher<? extends T>[] sources;

    public FlowableConcatArray(Publisher<? extends T>[] sources2, boolean delayError2) {
        this.sources = sources2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        ConcatArraySubscriber<T> parent = new ConcatArraySubscriber<>(this.sources, this.delayError, s);
        s.onSubscribe(parent);
        parent.onComplete();
    }

    static final class ConcatArraySubscriber<T> extends SubscriptionArbiter implements Subscriber<T> {
        private static final long serialVersionUID = -8158322871608889516L;
        final Subscriber<? super T> actual;
        final boolean delayError;
        List<Throwable> errors;
        int index;
        long produced;
        final Publisher<? extends T>[] sources;
        final AtomicInteger wip = new AtomicInteger();

        ConcatArraySubscriber(Publisher<? extends T>[] sources2, boolean delayError2, Subscriber<? super T> actual2) {
            this.actual = actual2;
            this.sources = sources2;
            this.delayError = delayError2;
        }

        public void onSubscribe(Subscription s) {
            setSubscription(s);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (this.delayError) {
                List<Throwable> list = this.errors;
                if (list == null) {
                    list = new ArrayList<>((this.sources.length - this.index) + 1);
                    this.errors = list;
                }
                list.add(t);
                onComplete();
                return;
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            if (this.wip.getAndIncrement() == 0) {
                Publisher<? extends T>[] sources2 = this.sources;
                int n = sources2.length;
                int i = this.index;
                while (i != n) {
                    Publisher<? extends T> p = sources2[i];
                    if (p == null) {
                        Throwable ex = new NullPointerException("A Publisher entry is null");
                        if (this.delayError) {
                            List<Throwable> list = this.errors;
                            if (list == null) {
                                list = new ArrayList<>((n - i) + 1);
                                this.errors = list;
                            }
                            list.add(ex);
                            i++;
                        } else {
                            this.actual.onError(ex);
                            return;
                        }
                    } else {
                        long r = this.produced;
                        if (r != 0) {
                            this.produced = 0;
                            produced(r);
                        }
                        p.subscribe(this);
                        i++;
                        this.index = i;
                        if (this.wip.decrementAndGet() == 0) {
                            return;
                        }
                    }
                }
                List<Throwable> list2 = this.errors;
                if (list2 == null) {
                    this.actual.onComplete();
                } else if (list2.size() == 1) {
                    this.actual.onError(list2.get(0));
                } else {
                    this.actual.onError(new CompositeException(list2));
                }
            }
        }
    }
}
