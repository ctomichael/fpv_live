package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

public class DJICameraWM240ZoomAbstraction extends DJICameraWM240HasselbladAbstraction {
    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameMavic2ZoomCamera;
    }
}
