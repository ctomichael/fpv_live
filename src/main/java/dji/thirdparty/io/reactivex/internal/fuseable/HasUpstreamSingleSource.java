package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.SingleSource;

public interface HasUpstreamSingleSource<T> {
    SingleSource<T> source();
}
