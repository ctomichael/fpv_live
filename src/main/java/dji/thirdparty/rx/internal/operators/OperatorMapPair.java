package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;

public final class OperatorMapPair<T, U, R> implements Observable.Operator<Observable<? extends R>, T> {
    final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public static <T, U> Func1<T, Observable<U>> convertSelector(final Func1<? super T, ? extends Iterable<? extends U>> selector) {
        return new Func1<T, Observable<U>>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorMapPair.AnonymousClass1 */

            public Observable<U> call(T t1) {
                return Observable.from((Iterable) selector.call(t1));
            }
        };
    }

    public OperatorMapPair(Func1<? super T, ? extends Observable<? extends U>> collectionSelector2, Func2<? super T, ? super U, ? extends R> resultSelector2) {
        this.collectionSelector = collectionSelector2;
        this.resultSelector = resultSelector2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Observable<? extends R>> o) {
        return new Subscriber<T>(o) {
            /* class dji.thirdparty.rx.internal.operators.OperatorMapPair.AnonymousClass2 */

            public void onCompleted() {
                o.onCompleted();
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(final T outer) {
                try {
                    o.onNext(((Observable) OperatorMapPair.this.collectionSelector.call(outer)).map(new Func1<U, R>() {
                        /* class dji.thirdparty.rx.internal.operators.OperatorMapPair.AnonymousClass2.AnonymousClass1 */

                        public R call(U inner) {
                            return OperatorMapPair.this.resultSelector.call(outer, inner);
                        }
                    }));
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, o, outer);
                }
            }
        };
    }
}
