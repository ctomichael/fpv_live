package dji.sdksharedlib.store;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJISDKCacheStorageCompletionCallback {
    void onResult(DJIError dJIError);
}
