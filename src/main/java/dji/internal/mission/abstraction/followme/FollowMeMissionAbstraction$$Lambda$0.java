package dji.internal.mission.abstraction.followme;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class FollowMeMissionAbstraction$$Lambda$0 implements DJIParamAccessListener {
    private final FollowMeMissionAbstraction arg$1;

    FollowMeMissionAbstraction$$Lambda$0(FollowMeMissionAbstraction followMeMissionAbstraction) {
        this.arg$1 = followMeMissionAbstraction;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$new$0$FollowMeMissionAbstraction(dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
