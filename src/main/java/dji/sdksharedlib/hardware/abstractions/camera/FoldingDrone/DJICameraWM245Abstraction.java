package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

public class DJICameraWM245Abstraction extends DJICameraWM240ZoomAbstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameMavic2EnterpriseCamera;
    }

    /* access modifiers changed from: protected */
    public int getProtocolVersion() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterCol() {
        return 15;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterRow() {
        return 15;
    }
}
