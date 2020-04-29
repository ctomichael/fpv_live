package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorTakeUntil<T, E> implements Observable.Operator<T, T> {
    private final Observable<? extends E> other;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorTakeUntil(Observable<? extends E> other2) {
        this.other = other2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final Subscriber<T> serial = new SerializedSubscriber<>(child, false);
        final Subscriber<T> main = new Subscriber<T>(false, serial) {
            /* class dji.thirdparty.rx.internal.operators.OperatorTakeUntil.AnonymousClass1 */

            public void onNext(T t) {
                serial.onNext(t);
            }

            public void onError(Throwable e) {
                try {
                    serial.onError(e);
                } finally {
                    serial.unsubscribe();
                }
            }

            public void onCompleted() {
                try {
                    serial.onCompleted();
                } finally {
                    serial.unsubscribe();
                }
            }
        };
        Subscriber<E> so = new Subscriber<E>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTakeUntil.AnonymousClass2 */

            public void onStart() {
                request(LongCompanionObject.MAX_VALUE);
            }

            public void onCompleted() {
                main.onCompleted();
            }

            public void onError(Throwable e) {
                main.onError(e);
            }

            public void onNext(E e) {
                onCompleted();
            }
        };
        serial.add(main);
        serial.add(so);
        child.add(serial);
        this.other.unsafeSubscribe(so);
        return main;
    }
}
