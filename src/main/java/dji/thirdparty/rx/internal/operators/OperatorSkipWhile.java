package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;

public final class OperatorSkipWhile<T> implements Observable.Operator<T, T> {
    final Func2<? super T, Integer, Boolean> predicate;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorSkipWhile.AnonymousClass1 */
            int index;
            boolean skipping = true;

            public void onNext(T t) {
                if (!this.skipping) {
                    child.onNext(t);
                    return;
                }
                try {
                    Func2<? super T, Integer, Boolean> func2 = OperatorSkipWhile.this.predicate;
                    int i = this.index;
                    this.index = i + 1;
                    if (!func2.call(t, Integer.valueOf(i)).booleanValue()) {
                        this.skipping = false;
                        child.onNext(t);
                        return;
                    }
                    request(1);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, child, t);
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                child.onCompleted();
            }
        };
    }

    public static <T> Func2<T, Integer, Boolean> toPredicate2(final Func1<? super T, Boolean> predicate2) {
        return new Func2<T, Integer, Boolean>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSkipWhile.AnonymousClass2 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.internal.operators.OperatorSkipWhile.2.call(java.lang.Object, java.lang.Integer):java.lang.Boolean
             arg types: [java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.internal.operators.OperatorSkipWhile.2.call(java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func2.call(java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.internal.operators.OperatorSkipWhile.2.call(java.lang.Object, java.lang.Integer):java.lang.Boolean */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call(x0, (Integer) ((Integer) x1));
            }

            public Boolean call(T t1, Integer t2) {
                return (Boolean) predicate2.call(t1);
            }
        };
    }
}
