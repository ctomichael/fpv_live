package dji.thirdparty.io.reactivex.internal.disposables;

import dji.thirdparty.io.reactivex.disposables.Disposable;

public interface DisposableContainer {
    boolean add(Disposable disposable);

    boolean delete(Disposable disposable);

    boolean remove(Disposable disposable);
}
