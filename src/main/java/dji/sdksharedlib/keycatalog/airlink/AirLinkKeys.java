package dji.sdksharedlib.keycatalog.airlink;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class AirLinkKeys extends DJISDKCacheKeys {
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String BASE_STATION_SIGNAL_QUALITY = "GroundSystemSignalQuality";
    public static final String COMPONENT_KEY = "AirLink";
    @Key(accessType = 2, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String COUNTRY_CODE = "CountryCode";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DOWNLINK_SIGNAL_QUALITY = "DownlinkSignalQuality";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_LIGHTBRIDGE_LINK_SUPPORTED = "IsLightbridgeLinkSupported";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_LTE_LINK_SUPPORTED = "IsLteLinkSupported";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_WIFI_LINK_SUPPORTED = "IsWiFiLinkSupported";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String UPLINK_SIGNAL_QUALITY = "UplinkSignalQuality";

    public AirLinkKeys(String name) {
        super(name);
    }
}
