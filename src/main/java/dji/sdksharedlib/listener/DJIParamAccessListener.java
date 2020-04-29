package dji.sdksharedlib.listener;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public interface DJIParamAccessListener {
    void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2);
}
