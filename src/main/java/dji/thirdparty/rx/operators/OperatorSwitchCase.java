package dji.thirdparty.rx.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func0;
import java.util.Map;

public final class OperatorSwitchCase<K, R> implements Observable.OnSubscribe<R> {
    final Func0<? extends K> caseSelector;
    final Observable<? extends R> defaultCase;
    final Map<? super K, ? extends Observable<? extends R>> mapOfCases;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSwitchCase(Func0<? extends K> caseSelector2, Map<? super K, ? extends Observable<? extends R>> mapOfCases2, Observable<? extends R> defaultCase2) {
        this.caseSelector = caseSelector2;
        this.mapOfCases = mapOfCases2;
        this.defaultCase = defaultCase2;
    }

    public void call(Subscriber<? super R> t1) {
        Observable<? extends R> target;
        try {
            K caseKey = this.caseSelector.call();
            if (this.mapOfCases.containsKey(caseKey)) {
                target = (Observable) this.mapOfCases.get(caseKey);
            } else {
                target = this.defaultCase;
            }
            target.subscribe((Subscriber) t1);
        } catch (Throwable t) {
            t1.onError(t);
        }
    }
}
