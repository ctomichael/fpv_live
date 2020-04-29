package dji.sdksharedlib.keycatalog;

import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.extension.Key;
import java.util.ArrayList;

public class VirtualFenceKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "VirtualFence";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DELETE_VIRTUAL_FENCE = "DeleteVirtualFenceArea";
    @Key(accessType = 3, type = ArrayList.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VIRTUAL_FENCE_AREA = "VirtualFenceArea";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VIRTUAL_FENCE_ENABLED = "VirtualFenceAreaEnabled";

    public VirtualFenceKeys(String name) {
        super(name);
    }
}
