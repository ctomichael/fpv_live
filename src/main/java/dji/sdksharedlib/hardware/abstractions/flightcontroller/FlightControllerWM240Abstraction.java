package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM240Abstraction;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;

public class FlightControllerWM240Abstraction extends FlightControllerWM230Abstraction {
    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantWM240Abstraction();
    }

    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        this.compassCount = 1;
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
    }

    @Setter(FlightControllerKeys.TRIPOD_MODE_ENABLED)
    public void setTripodModeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }
}
