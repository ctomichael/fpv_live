package dji.sdksharedlib.keycatalog.airlink;

import dji.common.airlink.WiFiFrequencyBand;
import dji.common.airlink.WiFiMagneticInterferenceLevel;
import dji.common.airlink.WiFiSelectionMode;
import dji.common.airlink.WifiChannelInterference;
import dji.common.airlink.WifiDataRate;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWm100GroundAbstraction;
import dji.sdksharedlib.keycatalog.extension.ComplexKey;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class WifiLinkKeys extends AirLinkKeys {
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = Integer[].class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 1, type = Integer[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)})
    public static final String AVAILABLE_CHANNEL_NUMBERS = "AvailableChannelNumbers";
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = WifiChannelInterference[].class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 1, type = WifiChannelInterference[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)})
    public static final String CHANNEL_INTERFERENCE = "ChannelInterference";
    @InternalKey
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 1, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)})
    public static final String CHANNEL_LIST_24G = "ChannelList24G";
    @InternalKey
    @ComplexKey({@Key(accessType = 4, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 1, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)})
    public static final String CHANNEL_LIST_5G = "ChannelList5G";
    @ComplexKey({@Key(accessType = 6, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)})
    public static final String CHANNEL_NUMBER = "ChannelNumber";
    @InternalKey
    @Key(accessType = 2, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CHN_INTERFERENCE_SWITCH = "ChnInterferenceSwitch";
    public static final String COMPONENT_KEY = "WifiLink";
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String COUNTRY_FROM_GROUND = "CountryCodeFromRc";
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String COUNTRY_FROM_SKY = "CountryCodeFromSky";
    @Key(accessType = 3, excludedAbstractions = {DJIWifiLinkWm100GroundAbstraction.class}, type = WifiDataRate.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DATA_RATE = "DataRate";
    @Key(accessType = 3, type = WiFiFrequencyBand.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FREQUENCY_BAND = "FrequencyBand";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GROUND_WIFI_POWER = "GroundWifiPower";
    @Key(accessType = 4, type = WiFiMagneticInterferenceLevel.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MAGNETIC_INTERFERENCE = "MagneticInterference";
    @Key(accessType = 3, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PASSWORD = "Password";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String REBOOT = "Reboot";
    @InternalKey
    @Key(accessType = 4, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RSSI_LIST_24G = "RssiList24G";
    @InternalKey
    @Key(accessType = 4, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RSSI_LIST_5G = "RssiList5G";
    @ComplexKey({@Key(accessType = 6, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = WiFiSelectionMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 3, type = WiFiSelectionMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)})
    public static final String SELECTION_MODE = "SelectionMode";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {DJIWifiLinkWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SKY_WIFI_POWER = "SkyWifiPower";
    @Key(accessType = 3, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SSID = "SSID";
    @InternalKey
    @Key(accessType = 4, type = DataWifiGetPushElecSignal.SIGNAL_STATUS.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WIFI_SIGNAL = "WifiSignal";

    public WifiLinkKeys(String name) {
        super(name);
    }
}
