package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;

public final class OperatorTakeWhile<T> implements Observable.Operator<T, T> {
    final Func2<? super T, ? super Integer, Boolean> predicate;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorTakeWhile(final Func1 func1) {
        this(new Func2<T, Integer, Boolean>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTakeWhile.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.internal.operators.OperatorTakeWhile.1.call(java.lang.Object, java.lang.Integer):java.lang.Boolean
             arg types: [java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.internal.operators.OperatorTakeWhile.1.call(java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func2.call(java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.internal.operators.OperatorTakeWhile.1.call(java.lang.Object, java.lang.Integer):java.lang.Boolean */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call(x0, (Integer) ((Integer) x1));
            }

            public Boolean call(T input, Integer index) {
                return (Boolean) Func1.this.call(input);
            }
        });
    }

    public OperatorTakeWhile(Func2 func2) {
        this.predicate = func2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Subscriber<T> s = new Subscriber<T>(false, subscriber) {
            /* class dji.thirdparty.rx.internal.operators.OperatorTakeWhile.AnonymousClass2 */
            private int counter = 0;
            private boolean done = false;

            public void onNext(T t) {
                try {
                    Func2<? super T, ? super Integer, Boolean> func2 = OperatorTakeWhile.this.predicate;
                    int i = this.counter;
                    this.counter = i + 1;
                    if (func2.call(t, Integer.valueOf(i)).booleanValue()) {
                        subscriber.onNext(t);
                        return;
                    }
                    this.done = true;
                    subscriber.onCompleted();
                    unsubscribe();
                } catch (Throwable e) {
                    this.done = true;
                    Exceptions.throwOrReport(e, subscriber, t);
                    unsubscribe();
                }
            }

            public void onCompleted() {
                if (!this.done) {
                    subscriber.onCompleted();
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    subscriber.onError(e);
                }
            }
        };
        subscriber.add(s);
        return s;
    }
}
