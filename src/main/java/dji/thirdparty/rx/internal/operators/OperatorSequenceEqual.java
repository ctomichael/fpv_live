package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.internal.util.UtilityFunctions;

public final class OperatorSequenceEqual {
    static final Object LOCAL_ONCOMPLETED = new Object();

    private OperatorSequenceEqual() {
        throw new IllegalStateException("No instances!");
    }

    static <T> Observable<Object> materializeLite(Observable<T> source) {
        return Observable.concat(source.map(new Func1<T, Object>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSequenceEqual.AnonymousClass1 */

            public Object call(T t1) {
                return t1;
            }
        }), Observable.just(LOCAL_ONCOMPLETED));
    }

    public static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, final Func2<? super T, ? super T, Boolean> equality) {
        return Observable.zip(materializeLite(first), materializeLite(second), new Func2<Object, Object, Boolean>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSequenceEqual.AnonymousClass2 */

            public Boolean call(Object t1, Object t2) {
                boolean c1;
                boolean c2;
                if (t1 == OperatorSequenceEqual.LOCAL_ONCOMPLETED) {
                    c1 = true;
                } else {
                    c1 = false;
                }
                if (t2 == OperatorSequenceEqual.LOCAL_ONCOMPLETED) {
                    c2 = true;
                } else {
                    c2 = false;
                }
                if (c1 && c2) {
                    return true;
                }
                if (c1 || c2) {
                    return false;
                }
                return (Boolean) equality.call(t1, t2);
            }
        }).all(UtilityFunctions.identity());
    }
}
