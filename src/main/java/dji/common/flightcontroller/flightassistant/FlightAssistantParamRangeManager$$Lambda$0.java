package dji.common.flightcontroller.flightassistant;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class FlightAssistantParamRangeManager$$Lambda$0 implements DJIParamAccessListener {
    private final FlightAssistantParamRangeManager arg$1;

    FlightAssistantParamRangeManager$$Lambda$0(FlightAssistantParamRangeManager flightAssistantParamRangeManager) {
        this.arg$1 = flightAssistantParamRangeManager;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$addListenersForQuickshotActionTypeRange$0$FlightAssistantParamRangeManager(dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
