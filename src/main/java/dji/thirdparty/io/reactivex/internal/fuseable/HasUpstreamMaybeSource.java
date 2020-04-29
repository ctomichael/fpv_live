package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.MaybeSource;

public interface HasUpstreamMaybeSource<T> {
    MaybeSource<T> source();
}
