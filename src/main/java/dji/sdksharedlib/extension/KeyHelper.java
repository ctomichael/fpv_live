package dji.sdksharedlib.extension;

import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.keycatalog.AccessLockerKeys;
import dji.sdksharedlib.keycatalog.AccessoryAggregationKeys;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.BeaconKeys;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.keycatalog.HandheldControllerKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.PayloadKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.keycatalog.RTKKeys;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.keycatalog.SpeakerKeys;
import dji.sdksharedlib.keycatalog.SpotlightKeys;
import dji.sdksharedlib.keycatalog.VirtualFenceKeys;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.LteLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;

@EXClassNullAway
public class KeyHelper {
    public static DJISDKCacheKey getProductKey(String paramKey) {
        return get(ProductKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getProductKey(String[] paramKeys) {
        return get(ProductKeys.COMPONENT_KEY, paramKeys);
    }

    @Deprecated
    public static DJISDKCacheKey getCameraKey(String paramKey) {
        return get(CameraKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey getCameraKey(int index, String paramKey) {
        return get(CameraKeys.COMPONENT_KEY, index, paramKey);
    }

    @Deprecated
    public static DJISDKCacheKey[] getCameraKey(String[] paramKeys) {
        return get(CameraKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey[] getCameraKey(int index, String[] paramKeys) {
        return get(CameraKeys.COMPONENT_KEY, index, paramKeys);
    }

    public static DJISDKCacheKey getGimbalKey(String paramKey) {
        return get(GimbalKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey getGimbalKey(int index, String paramKey) {
        return get(GimbalKeys.COMPONENT_KEY, index, paramKey);
    }

    public static DJISDKCacheKey[] getGimbalKey(String[] paramKeys) {
        return get(GimbalKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey[] getGimbalKey(int index, String[] paramKeys) {
        return get(GimbalKeys.COMPONENT_KEY, index, paramKeys);
    }

    public static DJISDKCacheKey getBatteryKey(String paramKey) {
        return get(BatteryKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getBatteryKey(String[] paramKeys) {
        return get(BatteryKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey getPayloadKey(int index, String paramKey) {
        return get(PayloadKeys.COMPONENT_KEY, index, paramKey);
    }

    public static DJISDKCacheKey getBatteryKey(int index, String paramKey) {
        return get(BatteryKeys.COMPONENT_KEY, index, paramKey);
    }

    public static DJISDKCacheKey[] getBatteryKey(int index, String[] paramKeys) {
        return get(BatteryKeys.COMPONENT_KEY, paramKeys, index);
    }

    public static DJISDKCacheKey getBatteryAggregationKey(String paramKeys) {
        return get(BatteryKeys.COMPONENT_KEY, paramKeys, Integer.MAX_VALUE);
    }

    public static DJISDKCacheKey[] getBatteryAggregationKey(String[] paramKeys) {
        return get(BatteryKeys.COMPONENT_KEY, paramKeys, Integer.MAX_VALUE);
    }

    public static DJISDKCacheKey getFlightControllerKey(String paramKey) {
        return get(FlightControllerKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getFlightControllerKey(String[] paramKeys) {
        return get(FlightControllerKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey getIntelligentFlightAssistantKey(String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(IntelligentFlightAssistantKeys.COMPONENT_KEY).subComponentIndex(0);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getRTKKey(String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(RTKKeys.COMPONENT_KEY).subComponentIndex(0);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getAccessLockerKey(String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(AccessLockerKeys.COMPONENT_KEY).subComponentIndex(0);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getVirtualFenceKey(String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(VirtualFenceKeys.COMPONENT_KEY).subComponentIndex(0);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey[] getIntelligentFlightAssistantKeys(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(IntelligentFlightAssistantKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey[] getRTKKeys(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(RTKKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey[] getAccessLockerKeys(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(AccessLockerKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey getAccessoryKey(String paramKey) {
        return get(AccessoryAggregationKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getAccessoryKey(String[] paramKeys) {
        return get(AccessoryAggregationKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey getBeaconKey(String paramKey) {
        return getBeaconKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getBeaconKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AccessoryAggregationKeys.COMPONENT_KEY).index(index).subComponent(BeaconKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getSpeakerKey(String paramKey) {
        return getSpeakerKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getSpeakerKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AccessoryAggregationKeys.COMPONENT_KEY).index(index).subComponent(SpeakerKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getSpotlightKey(String paramKey) {
        return getSpotlightKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getSpotlightKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AccessoryAggregationKeys.COMPONENT_KEY).index(index).subComponent(SpotlightKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey[] getSpotlightKeys(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AccessoryAggregationKeys.COMPONENT_KEY).index(0).subComponent(SpotlightKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey getAirLinkKey(String paramKey) {
        return get(AirLinkKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getAirLinkKey(String[] paramKeys) {
        return get(AirLinkKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey[] getWiFiAirLinkKey(String[] paramKeys) {
        return getSubComponentKeys(AirLinkKeys.COMPONENT_KEY, WifiLinkKeys.COMPONENT_KEY, 0, 0, paramKeys);
    }

    public static DJISDKCacheKey[] getLightbridgeLinkKey(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(0).subComponent(LightbridgeLinkKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey getLightbridgeLinkKey(String paramKey) {
        return getLightbridgeLinkKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getLightbridgeLinkKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(index).subComponent(LightbridgeLinkKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getOcuSyncLinkKey(String paramKey) {
        return getOcuSyncLinkKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getOcuSyncLinkKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(index).subComponent(OcuSyncLinkKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey[] getOcuSyncLinkKey(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(0).subComponent(OcuSyncLinkKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey getWiFiAirLinkKey(String paramKey) {
        return getWiFiAirLinkKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getWiFiAirLinkKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(index).subComponent(WifiLinkKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey[] getLteLinkKey(String[] paramKeys) {
        DJISDKCacheKey[] keys = new DJISDKCacheKey[paramKeys.length];
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(0).subComponent(LteLinkKeys.COMPONENT_KEY).subComponentIndex(0);
        for (int i = 0; i < paramKeys.length; i++) {
            keys[i] = builder.paramKey(paramKeys[i]).build();
        }
        return keys;
    }

    public static DJISDKCacheKey getLteLinkKey(String paramKey) {
        return getLteLinkKey(0, 0, paramKey);
    }

    public static DJISDKCacheKey getLteLinkKey(int index, int componentIndex, String paramKey) {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(index).subComponent(LteLinkKeys.COMPONENT_KEY).subComponentIndex(componentIndex);
        return builder.paramKey(paramKey).build();
    }

    public static DJISDKCacheKey getHandheldControllerKey(String paramKey) {
        return get(HandheldControllerKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getHandheldControllerKey(String[] paramKeys) {
        return get(HandheldControllerKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey getRemoteControllerKey(String paramKey) {
        return get(RemoteControllerKeys.COMPONENT_KEY, paramKey);
    }

    public static DJISDKCacheKey[] getRemoteControllerKey(String[] paramKeys) {
        return get(RemoteControllerKeys.COMPONENT_KEY, paramKeys);
    }

    public static DJISDKCacheKey[] get(String component, String[] paramKeys) {
        if (paramKeys == null) {
            return null;
        }
        int len = paramKeys.length;
        DJISDKCacheKey[] keyPaths = new DJISDKCacheKey[len];
        for (int i = 0; i < len; i++) {
            keyPaths[i] = get(component, paramKeys[i]);
        }
        return keyPaths;
    }

    public static DJISDKCacheKey[] get(String component, int index, String[] paramKeys) {
        if (paramKeys == null) {
            return null;
        }
        int len = paramKeys.length;
        DJISDKCacheKey[] keyPaths = new DJISDKCacheKey[len];
        for (int i = 0; i < len; i++) {
            keyPaths[i] = get(component, index, paramKeys[i]);
        }
        return keyPaths;
    }

    public static DJISDKCacheKey[] getSubComponentKeys(String component, String subComponent, int index, int subIndex, String[] paramKeys) {
        if (paramKeys == null) {
            return null;
        }
        int len = paramKeys.length;
        DJISDKCacheKey[] keys = new DJISDKCacheKey[len];
        for (int i = 0; i < len; i++) {
            keys[i] = get(component, subComponent, index, subIndex, paramKeys[i]);
        }
        return keys;
    }

    public static DJISDKCacheKey get(String component, String paramKey) {
        return get(component, 0, paramKey);
    }

    public static DJISDKCacheKey get(String component, String paramKey, int subComponentIndex) {
        return get(component, subComponentIndex, paramKey);
    }

    public static DJISDKCacheKey[] get(String component, String[] paramKeys, int subComponentIndex) {
        if (paramKeys == null) {
            return null;
        }
        int len = paramKeys.length;
        DJISDKCacheKey[] keyPaths = new DJISDKCacheKey[len];
        for (int i = 0; i < len; i++) {
            keyPaths[i] = get(component, subComponentIndex, paramKeys[i]);
        }
        return keyPaths;
    }

    public static DJISDKCacheKey get(String component, int index, String paramKey) {
        DJISDKCacheKey key = DJISDKCacheKey.getCache(component + IMemberProtocol.PARAM_SEPERATOR + index + IMemberProtocol.PARAM_SEPERATOR + paramKey);
        if (key != null) {
            return key;
        }
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(component);
        builder.index(index);
        builder.paramKey(paramKey);
        return builder.build();
    }

    public static DJISDKCacheKey get(String component, String subComponent, int index, int subComponentIndex, String paramKey) {
        DJISDKCacheKey key = DJISDKCacheKey.getCache(component + IMemberProtocol.PARAM_SEPERATOR + index + IMemberProtocol.PARAM_SEPERATOR + paramKey);
        if (key != null) {
            return key;
        }
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(component);
        builder.index(index);
        builder.subComponent(subComponent);
        builder.subComponentIndex(subComponentIndex);
        builder.paramKey(paramKey);
        return builder.build();
    }
}
