package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OnSubscribeFromIterable<T> implements Observable.OnSubscribe<T> {
    final Iterable<? extends T> is;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OnSubscribeFromIterable(Iterable<? extends T> iterable) {
        if (iterable == null) {
            throw new NullPointerException("iterable must not be null");
        }
        this.is = iterable;
    }

    public void call(Subscriber<? super T> o) {
        Iterator<? extends T> it2 = this.is.iterator();
        if (it2.hasNext() || o.isUnsubscribed()) {
            o.setProducer(new IterableProducer(o, it2));
        } else {
            o.onCompleted();
        }
    }

    private static final class IterableProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -8730475647105475802L;

        /* renamed from: it  reason: collision with root package name */
        private final Iterator<? extends T> f29it;
        private final Subscriber<? super T> o;

        IterableProducer(Subscriber<? super T> o2, Iterator<? extends T> it2) {
            this.o = o2;
            this.f29it = it2;
        }

        public void request(long n) {
            if (get() != LongCompanionObject.MAX_VALUE) {
                if (n == LongCompanionObject.MAX_VALUE && compareAndSet(0, LongCompanionObject.MAX_VALUE)) {
                    fastpath();
                } else if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                    slowpath(n);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void slowpath(long n) {
            Subscriber<? super T> o2 = this.o;
            Iterator<? extends T> it2 = this.f29it;
            long r = n;
            do {
                long numToEmit = r;
                while (!o2.isUnsubscribed()) {
                    if (it2.hasNext()) {
                        numToEmit--;
                        if (numToEmit >= 0) {
                            o2.onNext(it2.next());
                        } else {
                            r = addAndGet(-r);
                        }
                    } else if (!o2.isUnsubscribed()) {
                        o2.onCompleted();
                        return;
                    } else {
                        return;
                    }
                }
                return;
            } while (r != 0);
        }

        /* access modifiers changed from: package-private */
        public void fastpath() {
            Subscriber<? super T> o2 = this.o;
            Iterator<? extends T> it2 = this.f29it;
            while (!o2.isUnsubscribed()) {
                if (it2.hasNext()) {
                    o2.onNext(it2.next());
                } else if (!o2.isUnsubscribed()) {
                    o2.onCompleted();
                    return;
                } else {
                    return;
                }
            }
        }
    }
}
