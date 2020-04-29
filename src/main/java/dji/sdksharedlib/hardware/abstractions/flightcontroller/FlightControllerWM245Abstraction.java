package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM245Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence.VirtualFenceAbstraction;
import dji.sdksharedlib.keycatalog.AccessLockerKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.VirtualFenceKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

public class FlightControllerWM245Abstraction extends FlightControllerWM240Abstraction {
    /* access modifiers changed from: protected */
    public void initializeSubComponents(DJISDKCacheStoreLayer storeLayer) {
        addSubComponents(new IntelligentFlightAssistantWM245Abstraction(), IntelligentFlightAssistantKeys.COMPONENT_KEY, 0, storeLayer);
        addSubComponents(new AccessLockerAbstraction(), AccessLockerKeys.COMPONENT_KEY, 0, storeLayer);
        addSubComponents(new VirtualFenceAbstraction(), VirtualFenceKeys.COMPONENT_KEY, 0, storeLayer);
    }

    /* access modifiers changed from: protected */
    public boolean isDataProtectionAssistantSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVirtualFenceSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public VirtualFenceAbstraction newVirtualFenceIfSupport() {
        return new VirtualFenceAbstraction();
    }
}
