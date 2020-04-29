package dji.sdksharedlib.hardware.extension;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheKeyCharacteristics;

@EXClassNullAway
public class DJISDKCacheCollectorCallback {
    public DJISDKCacheHWAbstraction.InnerCallback callback;
    public DJISDKCacheKeyCharacteristics keyCharacteristics;

    public DJISDKCacheCollectorCallback(DJISDKCacheKeyCharacteristics keyCharacteristics2, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.keyCharacteristics = keyCharacteristics2;
        this.callback = callback2;
    }
}
