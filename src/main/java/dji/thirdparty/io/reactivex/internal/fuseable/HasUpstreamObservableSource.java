package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.ObservableSource;

public interface HasUpstreamObservableSource<T> {
    ObservableSource<T> source();
}
