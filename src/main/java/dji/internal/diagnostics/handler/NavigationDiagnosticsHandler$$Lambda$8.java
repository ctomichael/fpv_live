package dji.internal.diagnostics.handler;

import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.io.reactivex.ObservableEmitter;
import dji.utils.Optional;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$8 implements DJIParamAccessListener {
    private final ObservableEmitter arg$1;
    private final DJISDKCacheKey arg$2;

    NavigationDiagnosticsHandler$$Lambda$8(ObservableEmitter observableEmitter, DJISDKCacheKey dJISDKCacheKey) {
        this.arg$1 = observableEmitter;
        this.arg$2 = dJISDKCacheKey;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.onNext(Optional.ofNullable(CacheHelper.getValue(this.arg$2)));
    }
}
