package dji.internal.mock;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

@EXClassNullAway
public class MockHelper {
    public static void doOnResultForCallback(DJISDKCacheHWAbstraction.InnerCallback callback, Object value) {
        if (callback != null) {
            callback.onSuccess(value);
        }
    }
}
