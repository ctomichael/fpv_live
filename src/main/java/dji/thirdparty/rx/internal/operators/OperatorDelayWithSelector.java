package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.observers.Subscribers;
import dji.thirdparty.rx.subjects.PublishSubject;

public final class OperatorDelayWithSelector<T, V> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends Observable<V>> itemDelay;
    final Observable<? extends T> source;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorDelayWithSelector(Observable<? extends T> source2, Func1<? super T, ? extends Observable<V>> itemDelay2) {
        this.source = source2;
        this.itemDelay = itemDelay2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> _child) {
        final SerializedSubscriber<T> child = new SerializedSubscriber<>(_child);
        final PublishSubject<Observable<T>> delayedEmissions = PublishSubject.create();
        _child.add(Observable.merge(delayedEmissions).unsafeSubscribe(Subscribers.from(child)));
        return new Subscriber<T>(_child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorDelayWithSelector.AnonymousClass1 */

            public void onCompleted() {
                delayedEmissions.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(final T t) {
                try {
                    delayedEmissions.onNext(((Observable) OperatorDelayWithSelector.this.itemDelay.call(t)).take(1).defaultIfEmpty(null).map(new Func1<V, T>() {
                        /* class dji.thirdparty.rx.internal.operators.OperatorDelayWithSelector.AnonymousClass1.AnonymousClass1 */

                        public T call(V v) {
                            return t;
                        }
                    }));
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this);
                }
            }
        };
    }
}
