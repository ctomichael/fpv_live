package dji.sdksharedlib.hardware.abstractions.camera.phantom;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

@EXClassNullAway
public class DJICameraPhantom4AAbstraction extends DJICameraPhantom4PAbstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplaynamePhantom4AdvancedCamera;
    }
}
