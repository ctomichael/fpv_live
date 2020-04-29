package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.CompositeException;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.Arrays;

public class OperatorDoOnEach<T> implements Observable.Operator<T, T> {
    final Observer<? super T> doOnEachObserver;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorDoOnEach(Observer<? super T> doOnEachObserver2) {
        this.doOnEachObserver = doOnEachObserver2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> observer) {
        return new Subscriber<T>(observer) {
            /* class dji.thirdparty.rx.internal.operators.OperatorDoOnEach.AnonymousClass1 */
            private boolean done = false;

            public void onCompleted() {
                if (!this.done) {
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onCompleted();
                        this.done = true;
                        observer.onCompleted();
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this);
                    }
                }
            }

            public void onError(Throwable e) {
                Exceptions.throwIfFatal(e);
                if (!this.done) {
                    this.done = true;
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onError(e);
                        observer.onError(e);
                    } catch (Throwable e2) {
                        Exceptions.throwIfFatal(e2);
                        observer.onError(new CompositeException(Arrays.asList(e, e2)));
                    }
                }
            }

            public void onNext(T value) {
                if (!this.done) {
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onNext(value);
                        observer.onNext(value);
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this, value);
                    }
                }
            }
        };
    }
}
