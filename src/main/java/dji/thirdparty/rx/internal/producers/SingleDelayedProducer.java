package dji.thirdparty.rx.internal.producers;

import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.concurrent.atomic.AtomicInteger;

public final class SingleDelayedProducer<T> extends AtomicInteger implements Producer {
    static final int HAS_REQUEST_HAS_VALUE = 3;
    static final int HAS_REQUEST_NO_VALUE = 2;
    static final int NO_REQUEST_HAS_VALUE = 1;
    static final int NO_REQUEST_NO_VALUE = 0;
    private static final long serialVersionUID = -2873467947112093874L;
    final Subscriber<? super T> child;
    T value;

    public SingleDelayedProducer(Subscriber<? super T> child2) {
        this.child = child2;
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (n != 0) {
            do {
                int s = get();
                if (s != 0) {
                    if (s == 1 && compareAndSet(1, 3)) {
                        emit(this.child, this.value);
                        return;
                    }
                    return;
                }
            } while (!compareAndSet(0, 2));
        }
    }

    public void setValue(T value2) {
        do {
            int s = get();
            if (s == 0) {
                this.value = value2;
            } else if (s == 2 && compareAndSet(2, 3)) {
                emit(this.child, value2);
                return;
            } else {
                return;
            }
        } while (!compareAndSet(0, 1));
    }

    private static <T> void emit(Subscriber<? super T> c, T v) {
        if (!c.isUnsubscribed()) {
            try {
                c.onNext(v);
                if (!c.isUnsubscribed()) {
                    c.onCompleted();
                }
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, c, v);
            }
        }
    }
}
