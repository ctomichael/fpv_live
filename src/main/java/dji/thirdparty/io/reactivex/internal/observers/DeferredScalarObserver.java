package dji.thirdparty.io.reactivex.internal.observers;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;

public abstract class DeferredScalarObserver<T, R> extends DeferredScalarDisposable<R> implements Observer<T> {
    private static final long serialVersionUID = -266195175408988651L;
    protected Disposable s;

    public DeferredScalarObserver(Observer<? super R> actual) {
        super(actual);
    }

    public void onSubscribe(Disposable s2) {
        if (DisposableHelper.validate(this.s, s2)) {
            this.s = s2;
            this.actual.onSubscribe(this);
        }
    }

    public void onError(Throwable t) {
        this.value = null;
        error(t);
    }

    public void onComplete() {
        R v = this.value;
        if (v != null) {
            this.value = null;
            complete(v);
            return;
        }
        complete();
    }

    public void dispose() {
        super.dispose();
        this.s.dispose();
    }
}
