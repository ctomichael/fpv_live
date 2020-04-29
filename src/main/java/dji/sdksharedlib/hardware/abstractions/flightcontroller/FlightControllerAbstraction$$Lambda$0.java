package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class FlightControllerAbstraction$$Lambda$0 implements DJIParamAccessListener {
    private final FlightControllerAbstraction arg$1;

    FlightControllerAbstraction$$Lambda$0(FlightControllerAbstraction flightControllerAbstraction) {
        this.arg$1 = flightControllerAbstraction;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$setupForRealNameSystem$0$FlightControllerAbstraction(dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
