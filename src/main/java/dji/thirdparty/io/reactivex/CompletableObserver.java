package dji.thirdparty.io.reactivex;

import dji.thirdparty.io.reactivex.disposables.Disposable;

public interface CompletableObserver {
    void onComplete();

    void onError(Throwable th);

    void onSubscribe(Disposable disposable);
}
