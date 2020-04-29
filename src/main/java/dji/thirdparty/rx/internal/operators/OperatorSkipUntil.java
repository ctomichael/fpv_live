package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicBoolean;

public final class OperatorSkipUntil<T, U> implements Observable.Operator<T, T> {
    final Observable<U> other;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSkipUntil(Observable<U> other2) {
        this.other = other2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        final AtomicBoolean gate = new AtomicBoolean();
        Subscriber<U> u = new Subscriber<U>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSkipUntil.AnonymousClass1 */

            public void onNext(U u) {
                gate.set(true);
                unsubscribe();
            }

            public void onError(Throwable e) {
                s.onError(e);
                s.unsubscribe();
            }

            public void onCompleted() {
                unsubscribe();
            }
        };
        child.add(u);
        this.other.unsafeSubscribe(u);
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorSkipUntil.AnonymousClass2 */

            public void onNext(T t) {
                if (gate.get()) {
                    s.onNext(t);
                } else {
                    request(1);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
            }

            public void onCompleted() {
                s.onCompleted();
                unsubscribe();
            }
        };
    }
}
