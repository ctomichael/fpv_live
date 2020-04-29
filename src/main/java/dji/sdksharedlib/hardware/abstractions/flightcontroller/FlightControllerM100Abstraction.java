package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FlightControllerM100Abstraction extends FlightControllerPhantom3Abstraction {
    /* access modifiers changed from: protected */
    public boolean isOnboardSDKAvailable() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return false;
    }
}
