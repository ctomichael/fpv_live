package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.observers.Subscribers;
import java.util.HashMap;
import java.util.Map;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorToMap<T, K, V> implements Observable.Operator<Map<K, V>, T> {
    final Func1<? super T, ? extends K> keySelector;
    private final Func0<? extends Map<K, V>> mapFactory;
    final Func1<? super T, ? extends V> valueSelector;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public static final class DefaultToMapFactory<K, V> implements Func0<Map<K, V>> {
        public Map<K, V> call() {
            return new HashMap();
        }
    }

    public OperatorToMap(Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2) {
        this(keySelector2, valueSelector2, new DefaultToMapFactory());
    }

    public OperatorToMap(Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, Func0<? extends Map<K, V>> mapFactory2) {
        this.keySelector = keySelector2;
        this.valueSelector = valueSelector2;
        this.mapFactory = mapFactory2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Map<K, V>> subscriber) {
        try {
            final Map<K, V> fLocalMap = (Map) this.mapFactory.call();
            return new Subscriber<T>(subscriber) {
                /* class dji.thirdparty.rx.internal.operators.OperatorToMap.AnonymousClass1 */
                private Map<K, V> map = fLocalMap;

                public void onStart() {
                    request(LongCompanionObject.MAX_VALUE);
                }

                public void onNext(T v) {
                    try {
                        this.map.put(OperatorToMap.this.keySelector.call(v), OperatorToMap.this.valueSelector.call(v));
                    } catch (Throwable ex) {
                        Exceptions.throwOrReport(ex, subscriber);
                    }
                }

                public void onError(Throwable e) {
                    this.map = null;
                    subscriber.onError(e);
                }

                public void onCompleted() {
                    Map<K, V> map0 = this.map;
                    this.map = null;
                    subscriber.onNext(map0);
                    subscriber.onCompleted();
                }
            };
        } catch (Throwable ex) {
            Exceptions.throwOrReport(ex, subscriber);
            Subscriber<? super T> parent = Subscribers.empty();
            parent.unsubscribe();
            return parent;
        }
    }
}
