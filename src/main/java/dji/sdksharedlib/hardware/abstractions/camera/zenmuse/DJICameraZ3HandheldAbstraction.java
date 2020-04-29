package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraZ3HandheldAbstraction extends DJICameraX3HandheldAbstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameZ3;
    }

    /* access modifiers changed from: protected */
    public boolean isTurnOffFanSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAudioRecordSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isTimeLapseSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
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
    public boolean isHandHeldProduct() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }
}
