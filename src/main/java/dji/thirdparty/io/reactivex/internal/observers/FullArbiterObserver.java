package dji.thirdparty.io.reactivex.internal.observers;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.disposables.ObserverFullArbiter;

public final class FullArbiterObserver<T> implements Observer<T> {
    final ObserverFullArbiter<T> arbiter;
    Disposable s;

    public FullArbiterObserver(ObserverFullArbiter<T> arbiter2) {
        this.arbiter = arbiter2;
    }

    public void onSubscribe(Disposable s2) {
        if (DisposableHelper.validate(this.s, s2)) {
            this.s = s2;
            this.arbiter.setDisposable(s2);
        }
    }

    public void onNext(T t) {
        this.arbiter.onNext(t, this.s);
    }

    public void onError(Throwable t) {
        this.arbiter.onError(t, this.s);
    }

    public void onComplete() {
        this.arbiter.onComplete(this.s);
    }
}
