package dji.sdksharedlib.listener;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public interface DJIGetCallback {
    void onFails(DJIError dJIError);

    void onSuccess(DJISDKCacheParamValue dJISDKCacheParamValue);
}
