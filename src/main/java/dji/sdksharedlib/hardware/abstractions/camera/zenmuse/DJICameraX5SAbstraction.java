package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

@EXClassNullAway
public class DJICameraX5SAbstraction extends DJICameraInspire2RawAbstraction {
    private static final String TAG = "X5SCamera";

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameX5S;
    }
}
