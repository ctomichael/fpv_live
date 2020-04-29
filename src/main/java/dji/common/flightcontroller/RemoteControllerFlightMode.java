package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;

@EXClassNullAway
public enum RemoteControllerFlightMode {
    F,
    A,
    P,
    S,
    G,
    M,
    T,
    UNKNOWN;

    public boolean isMissionAvailable() {
        if (((String) CacheHelper.getFlightController(DJISDKCacheKeys.FIRMWARE_VERSION)).compareTo("03.02") >= 0) {
            return equals(P);
        }
        return equals(F);
    }

    public boolean isTypeGSPMode() {
        return false;
    }
}
