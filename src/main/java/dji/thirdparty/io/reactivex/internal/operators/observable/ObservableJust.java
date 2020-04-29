package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.internal.fuseable.ScalarCallable;
import dji.thirdparty.io.reactivex.internal.operators.observable.ObservableScalarXMap;

public final class ObservableJust<T> extends Observable<T> implements ScalarCallable<T> {
    private final T value;

    public ObservableJust(T value2) {
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        ObservableScalarXMap.ScalarDisposable<T> sd = new ObservableScalarXMap.ScalarDisposable<>(s, this.value);
        s.onSubscribe(sd);
        sd.run();
    }

    public T call() {
        return this.value;
    }
}
