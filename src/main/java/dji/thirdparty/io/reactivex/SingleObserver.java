package dji.thirdparty.io.reactivex;

import dji.thirdparty.io.reactivex.disposables.Disposable;

public interface SingleObserver<T> {
    void onError(Throwable th);

    void onSubscribe(Disposable disposable);

    void onSuccess(T t);
}
