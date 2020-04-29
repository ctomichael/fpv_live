package dji.sdksharedlib.hardware.extension;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJISDKCacheCommonMergeCallback {
    void onFailure(DJIError dJIError);

    void onSuccess(Object obj);
}
