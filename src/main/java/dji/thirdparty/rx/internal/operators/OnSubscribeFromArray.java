package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OnSubscribeFromArray<T> implements Observable.OnSubscribe<T> {
    final T[] array;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OnSubscribeFromArray(T[] array2) {
        this.array = array2;
    }

    public void call(Subscriber<? super T> child) {
        child.setProducer(new FromArrayProducer(child, this.array));
    }

    static final class FromArrayProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = 3534218984725836979L;
        final T[] array;
        final Subscriber<? super T> child;
        int index;

        public FromArrayProducer(Subscriber<? super T> child2, T[] array2) {
            this.child = child2;
            this.array = array2;
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required but it was " + n);
            } else if (n == LongCompanionObject.MAX_VALUE) {
                if (BackpressureUtils.getAndAddRequest(this, n) == 0) {
                    fastPath();
                }
            } else if (n != 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                slowPath(n);
            }
        }

        /* access modifiers changed from: package-private */
        public void fastPath() {
            Subscriber<? super T> child2 = this.child;
            T[] arr$ = this.array;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                T t = arr$[i$];
                if (!child2.isUnsubscribed()) {
                    child2.onNext(t);
                    i$++;
                } else {
                    return;
                }
            }
            if (!child2.isUnsubscribed()) {
                child2.onCompleted();
            }
        }

        /* access modifiers changed from: package-private */
        public void slowPath(long r) {
            Subscriber<? super T> child2 = this.child;
            T[] array2 = this.array;
            int n = array2.length;
            long e = 0;
            int i = this.index;
            while (true) {
                if (r == 0 || i == n) {
                    r = get() + e;
                    if (r == 0) {
                        this.index = i;
                        r = addAndGet(e);
                        if (r != 0) {
                            e = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!child2.isUnsubscribed()) {
                    child2.onNext(array2[i]);
                    i++;
                    if (i != n) {
                        r--;
                        e--;
                    } else if (!child2.isUnsubscribed()) {
                        child2.onCompleted();
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }
}
