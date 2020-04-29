package dji.thirdparty.io.reactivex.observers;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;

public abstract class DefaultObserver<T> implements Observer<T> {
    private Disposable s;

    public final void onSubscribe(Disposable s2) {
        if (DisposableHelper.validate(this.s, s2)) {
            this.s = s2;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Disposable s2 = this.s;
        this.s = DisposableHelper.DISPOSED;
        s2.dispose();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }
}
