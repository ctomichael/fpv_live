package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.observers.Subscribers;
import java.util.Iterator;

public final class OperatorZipIterable<T1, T2, R> implements Observable.Operator<R, T1> {
    final Iterable<? extends T2> iterable;
    final Func2<? super T1, ? super T2, ? extends R> zipFunction;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorZipIterable(Iterable<? extends T2> iterable2, Func2<? super T1, ? super T2, ? extends R> zipFunction2) {
        this.iterable = iterable2;
        this.zipFunction = zipFunction2;
    }

    public Subscriber<? super T1> call(final Subscriber<? super R> subscriber) {
        final Iterator<? extends T2> iterator = this.iterable.iterator();
        try {
            if (iterator.hasNext()) {
                return new Subscriber<T1>(subscriber) {
                    /* class dji.thirdparty.rx.internal.operators.OperatorZipIterable.AnonymousClass1 */
                    boolean done;

                    public void onCompleted() {
                        if (!this.done) {
                            this.done = true;
                            subscriber.onCompleted();
                        }
                    }

                    public void onError(Throwable e) {
                        if (this.done) {
                            Exceptions.throwIfFatal(e);
                            return;
                        }
                        this.done = true;
                        subscriber.onError(e);
                    }

                    public void onNext(T1 t) {
                        if (!this.done) {
                            try {
                                subscriber.onNext(OperatorZipIterable.this.zipFunction.call(t, iterator.next()));
                                if (!iterator.hasNext()) {
                                    onCompleted();
                                }
                            } catch (Throwable e) {
                                Exceptions.throwOrReport(e, this);
                            }
                        }
                    }
                };
            }
            subscriber.onCompleted();
            return Subscribers.empty();
        } catch (Throwable e) {
            Exceptions.throwOrReport(e, subscriber);
            return Subscribers.empty();
        }
    }
}
