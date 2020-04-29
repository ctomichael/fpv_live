package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.Observable;

public interface FuseToObservable<T> {
    Observable<T> fuseToObservable();
}
