package dji.thirdparty.rx.internal.producers;

import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SingleProducer<T> extends AtomicBoolean implements Producer {
    private static final long serialVersionUID = -3353584923995471404L;
    final Subscriber<? super T> child;
    final T value;

    public SingleProducer(Subscriber<? super T> child2, T value2) {
        this.child = child2;
        this.value = value2;
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (n != 0 && compareAndSet(false, true)) {
            Subscriber<? super T> c = this.child;
            T v = this.value;
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
}
