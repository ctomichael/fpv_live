package dji.thirdparty.rx.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func0;

public final class OperatorIfThen<R> implements Observable.OnSubscribe<R> {
    final Func0<Boolean> condition;
    final Observable<? extends R> orElse;
    final Observable<? extends R> then;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OperatorIfThen(Func0<Boolean> condition2, Observable<? extends R> then2, Observable<? extends R> orElse2) {
        this.condition = condition2;
        this.then = then2;
        this.orElse = orElse2;
    }

    public void call(Subscriber<? super R> t1) {
        Observable<? extends R> target;
        try {
            if (this.condition.call().booleanValue()) {
                target = this.then;
            } else {
                target = this.orElse;
            }
            target.subscribe((Subscriber) t1);
        } catch (Throwable t) {
            t1.onError(t);
        }
    }
}
