package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraTau640Abstraction extends DJICameraXTBaseAbstraction {
    /* access modifiers changed from: protected */
    public boolean isTau640Camera() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return "Zenmuse XT";
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }
}
