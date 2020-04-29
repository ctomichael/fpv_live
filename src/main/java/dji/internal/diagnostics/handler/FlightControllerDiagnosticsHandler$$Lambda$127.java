package dji.internal.diagnostics.handler;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$127 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DJISDKCacheKey arg$2;
    private final DJISDKCacheParamValue arg$3;

    FlightControllerDiagnosticsHandler$$Lambda$127(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dJISDKCacheKey;
        this.arg$3 = dJISDKCacheParamValue;
    }

    public void run() {
        this.arg$1.lambda$onValueChange$76$FlightControllerDiagnosticsHandler(this.arg$2, this.arg$3);
    }
}
