package dji.sdksharedlib.hardware.abstractions.camera.phantom;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraPhantom4Abstraction extends DJICameraBaseAbstraction {
    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNamePhantom4Camera;
    }

    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean checkTrueColorDigitalFilterSupported() {
        return DataCameraGetPushStateInfo.getInstance().getCameraProtocolType(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.CameraProtocolType.FC330XTureColor;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return true;
    }
}
