package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class ObservableDefer<T> extends Observable<T> {
    final Callable<? extends ObservableSource<? extends T>> supplier;

    public ObservableDefer(Callable<? extends ObservableSource<? extends T>> supplier2) {
        this.supplier = supplier2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            ((ObservableSource) ObjectHelper.requireNonNull(this.supplier.call(), "null publisher supplied")).subscribe(s);
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            EmptyDisposable.error(t, s);
        }
    }
}
