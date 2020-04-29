package dji.sdksharedlib.hardware;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class DJISDKCacheHWAbstractionLayer$$Lambda$0 implements DJIParamAccessListener {
    private final DJISDKCacheHWAbstractionLayer arg$1;

    DJISDKCacheHWAbstractionLayer$$Lambda$0(DJISDKCacheHWAbstractionLayer dJISDKCacheHWAbstractionLayer) {
        this.arg$1 = dJISDKCacheHWAbstractionLayer;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$new$0$DJISDKCacheHWAbstractionLayer(dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
