package dji.internal.diagnostics.handler.util;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class DiagnosticsSharedLibModel$$Lambda$0 implements DJIParamAccessListener {
    private final DiagnosticsSharedLibModel arg$1;
    private final UpdateInterface arg$2;

    DiagnosticsSharedLibModel$$Lambda$0(DiagnosticsSharedLibModel diagnosticsSharedLibModel, UpdateInterface updateInterface) {
        this.arg$1 = diagnosticsSharedLibModel;
        this.arg$2 = updateInterface;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$startSubscribe$0$DiagnosticsSharedLibModel(this.arg$2, dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
