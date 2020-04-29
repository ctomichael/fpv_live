package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraZ3Abstraction extends DJICameraX3Abstraction {
    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameZ3;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableFocalPointSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMFSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }
}
