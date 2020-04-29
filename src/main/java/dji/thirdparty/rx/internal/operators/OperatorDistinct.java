package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.util.UtilityFunctions;
import java.util.HashSet;
import java.util.Set;

public final class OperatorDistinct<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    private static class Holder {
        static final OperatorDistinct<?, ?> INSTANCE = new OperatorDistinct<>(UtilityFunctions.identity());

        private Holder() {
        }
    }

    public static <T> OperatorDistinct<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinct(Func1<? super T, ? extends U> keySelector2) {
        this.keySelector = keySelector2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorDistinct.AnonymousClass1 */
            Set<U> keyMemory = new HashSet();

            public void onNext(T t) {
                if (this.keyMemory.add(OperatorDistinct.this.keySelector.call(t))) {
                    child.onNext(t);
                } else {
                    request(1);
                }
            }

            public void onError(Throwable e) {
                this.keyMemory = null;
                child.onError(e);
            }

            public void onCompleted() {
                this.keyMemory = null;
                child.onCompleted();
            }
        };
    }
}
