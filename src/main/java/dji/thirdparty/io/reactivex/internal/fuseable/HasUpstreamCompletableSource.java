package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.CompletableSource;

public interface HasUpstreamCompletableSource {
    CompletableSource source();
}
