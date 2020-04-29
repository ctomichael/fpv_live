package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraX3HandheldAbstraction extends DJICameraBaseAbstraction {
    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isHandHeldProduct() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean checkPortraitDigitalFilterSupported() {
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
    public boolean isShootPanoramaSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameX3;
    }

    public DJIError checkPrerequisiteForSetDigitalZoomScale() {
        if (DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) < 4) {
            return DJICameraError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE;
        }
        return super.checkPrerequisiteForSetDigitalZoomScale();
    }

    /* access modifiers changed from: protected */
    public boolean isTurnOffFanSupported() {
        return true;
    }
}
