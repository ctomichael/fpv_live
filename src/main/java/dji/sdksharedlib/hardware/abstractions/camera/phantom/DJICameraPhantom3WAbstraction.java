package dji.sdksharedlib.hardware.abstractions.camera.phantom;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraPhantom3WAbstraction extends DJICameraBaseAbstraction {
    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNamePhantom34KCamera;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }
}
