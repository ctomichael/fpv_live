package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.Flowable;

public interface FuseToFlowable<T> {
    Flowable<T> fuseToFlowable();
}
