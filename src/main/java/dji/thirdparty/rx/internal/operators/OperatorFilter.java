package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;

public final class OperatorFilter<T> implements Observable.Operator<T, T> {
    final Func1<? super T, Boolean> predicate;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorFilter(Func1<? super T, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorFilter.AnonymousClass1 */

            public void onCompleted() {
                child.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T t) {
                try {
                    if (OperatorFilter.this.predicate.call(t).booleanValue()) {
                        child.onNext(t);
                    } else {
                        request(1);
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, child, t);
                }
            }
        };
    }
}
