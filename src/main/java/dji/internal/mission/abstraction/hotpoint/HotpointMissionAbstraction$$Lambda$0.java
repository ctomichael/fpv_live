package dji.internal.mission.abstraction.hotpoint;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class HotpointMissionAbstraction$$Lambda$0 implements DJIParamAccessListener {
    private final HotpointMissionAbstraction arg$1;

    HotpointMissionAbstraction$$Lambda$0(HotpointMissionAbstraction hotpointMissionAbstraction) {
        this.arg$1 = hotpointMissionAbstraction;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$new$0$HotpointMissionAbstraction(dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
