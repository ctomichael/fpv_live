package dji.sdksharedlib.keycatalog.airlink;

import dji.common.airlink.LteLinkState;
import dji.common.camera.SettingsDefinitions;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.extension.Key;

public class LteLinkKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "LteLink";
    @Key(accessType = 4, type = LteLinkState.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LTE_LINK_STATE = "LteLinkState";
    @Key(accessType = 5, type = SettingsDefinitions.CameraType.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MODULE_LTE_VIDEO_CAMERA = "ModuleLteVideoCamera";
    @Key(accessType = 8, type = SettingsDefinitions.CameraType.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SWITCH_LTE_VIDEO_CAMERA = "SwitchLteVideoCamera";

    public LteLinkKeys(String name) {
        super(name);
    }
}
