package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObserverResourceWrapper<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
    private static final long serialVersionUID = -8612022020200669122L;
    final Observer<? super T> actual;
    final AtomicReference<Disposable> subscription = new AtomicReference<>();

    public ObserverResourceWrapper(Observer<? super T> actual2) {
        this.actual = actual2;
    }

    public void onSubscribe(Disposable s) {
        if (DisposableHelper.setOnce(this.subscription, s)) {
            this.actual.onSubscribe(this);
        }
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable t) {
        dispose();
        this.actual.onError(t);
    }

    public void onComplete() {
        dispose();
        this.actual.onComplete();
    }

    public void dispose() {
        DisposableHelper.dispose(this.subscription);
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return this.subscription.get() == DisposableHelper.DISPOSED;
    }

    public void setResource(Disposable resource) {
        DisposableHelper.set(this, resource);
    }
}
