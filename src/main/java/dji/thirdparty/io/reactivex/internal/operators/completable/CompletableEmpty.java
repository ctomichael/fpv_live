package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;

public final class CompletableEmpty extends Completable {
    public static final Completable INSTANCE = new CompletableEmpty();

    private CompletableEmpty() {
    }

    public void subscribeActual(CompletableObserver s) {
        EmptyDisposable.complete(s);
    }
}
