package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import java.util.ArrayDeque;
import java.util.Deque;

public class OperatorSkipLast<T> implements Observable.Operator<T, T> {
    final int count;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSkipLast(int count2) {
        if (count2 < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.count = count2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            /* class dji.thirdparty.rx.internal.operators.OperatorSkipLast.AnonymousClass1 */
            private final Deque<Object> deque = new ArrayDeque();
            private final NotificationLite<T> on = NotificationLite.instance();

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onNext(T value) {
                if (OperatorSkipLast.this.count == 0) {
                    subscriber.onNext(value);
                    return;
                }
                if (this.deque.size() == OperatorSkipLast.this.count) {
                    subscriber.onNext(this.on.getValue(this.deque.removeFirst()));
                } else {
                    request(1);
                }
                this.deque.offerLast(this.on.next(value));
            }
        };
    }
}
