package dji.internal.mock;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public class MockDJISDKCacheStoreLayer extends DJISDKCacheStoreLayer {
    public ConcurrentHashMap<DJISDKCacheKey, DJISDKCacheParamValue> getStores() {
        return this.stores;
    }
}
