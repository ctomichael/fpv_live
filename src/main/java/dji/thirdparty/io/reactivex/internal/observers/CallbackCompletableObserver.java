package dji.thirdparty.io.reactivex.internal.observers;

import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class CallbackCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, Consumer<Throwable> {
    private static final long serialVersionUID = -4361286194466301354L;
    final Action onComplete;
    final Consumer<? super Throwable> onError;

    public CallbackCompletableObserver(Action onComplete2) {
        this.onError = this;
        this.onComplete = onComplete2;
    }

    public CallbackCompletableObserver(Consumer<? super Throwable> onError2, Action onComplete2) {
        this.onError = onError2;
        this.onComplete = onComplete2;
    }

    public void accept(Throwable e) {
        RxJavaPlugins.onError(e);
    }

    public void onComplete() {
        try {
            this.onComplete.run();
            lazySet(DisposableHelper.DISPOSED);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            onError(ex);
        }
    }

    public void onError(Throwable e) {
        try {
            this.onError.accept(e);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
        }
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onSubscribe(Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}
