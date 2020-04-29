package dji.sdksharedlib.extension;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class CacheHelper {
    public static boolean isDataValid(DJISDKCacheKey key, DJISDKCacheParamValue value) {
        return (key == null || value == null || value.getData() == null) ? false : true;
    }

    public static void getRemoteController(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getRemoteControllerKey(paramKey), callback);
    }

    public static void getHandheld(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getHandheldControllerKey(paramKey), callback);
    }

    public static void getGimbal(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getGimbalKey(paramKey), callback);
    }

    public static void getGimbal(int index, String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getGimbalKey(index, paramKey), callback);
    }

    public static void get(DJISDKCacheKey key, DJIGetCallback callback) {
        DJISDKCache.getInstance().getValue(key, callback);
    }

    public static void getFlightController(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getFlightControllerKey(paramKey), callback);
    }

    public static void getAccessory(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getAccessoryKey(paramKey), callback);
    }

    public static void getFlightAssistant(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getIntelligentFlightAssistantKey(paramKey), callback);
    }

    public static void getAccessLockerAssistant(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getAccessLockerKey(paramKey), callback);
    }

    public static void getRTK(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getRTKKey(paramKey), callback);
    }

    public static void getCamera(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getCameraKey(paramKey), callback);
    }

    public static void getCamera(int index, String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getCameraKey(index, paramKey), callback);
    }

    public static void getLightbridgeLink(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getLightbridgeLinkKey(paramKey), callback);
    }

    public static void getWiFiAirLink(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getWiFiAirLinkKey(paramKey), callback);
    }

    public static void getOcuSyncLink(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getOcuSyncLinkKey(paramKey), callback);
    }

    public static void getPayload(int index, String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getPayloadKey(index, paramKey), callback);
    }

    public static <T> T getProduct(String paramKey) {
        return getValue(KeyHelper.getProductKey(paramKey));
    }

    public static <T> T getCamera(String paramKey) {
        return getValue(KeyHelper.getCameraKey(paramKey));
    }

    public static <T> T getCamera(int index, String paramKey) {
        return getValue(KeyHelper.getCameraKey(index, paramKey));
    }

    public static <T> T getLightbridgeLink(String paramKey) {
        return getValue(KeyHelper.getLightbridgeLinkKey(paramKey));
    }

    public static <T> T getBattery(String paramKey) {
        return getValue(KeyHelper.getBatteryKey(paramKey));
    }

    public static <T> T getBattery(int index, String paramKey) {
        return getValue(KeyHelper.getBatteryKey(index, paramKey));
    }

    public static void getBattery(String paramKey, DJIGetCallback callback) {
        get(KeyHelper.getBatteryKey(paramKey), callback);
    }

    public static <T> T getBattery(String paramKey, boolean isAggregation) {
        if (!isAggregation) {
            return getValue(KeyHelper.getBatteryKey(paramKey));
        }
        return getValue(KeyHelper.getBatteryAggregationKey(paramKey));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.extension.CacheHelper.getBattery(java.lang.String, boolean):T
     arg types: [java.lang.String, int]
     candidates:
      dji.sdksharedlib.extension.CacheHelper.getBattery(int, java.lang.String):T
      dji.sdksharedlib.extension.CacheHelper.getBattery(java.lang.String, int):T
      dji.sdksharedlib.extension.CacheHelper.getBattery(java.lang.String, dji.sdksharedlib.listener.DJIGetCallback):void
      dji.sdksharedlib.extension.CacheHelper.getBattery(java.lang.String, boolean):T */
    public static <T> T getBattery(String paramKey, int subComponentIndex) {
        if (subComponentIndex == Integer.MAX_VALUE) {
            return getBattery(paramKey, true);
        }
        return getValue(KeyHelper.getBatteryKey(subComponentIndex, paramKey));
    }

    public static <T> T getFlightController(String paramKey) {
        return getValue(KeyHelper.getFlightControllerKey(paramKey));
    }

    public static <T> T getRTK(String paramKey) {
        return getValue(KeyHelper.getRTKKey(paramKey));
    }

    public static <T> T getFlightAssistant(String paramKey) {
        return getValue(KeyHelper.getIntelligentFlightAssistantKey(paramKey));
    }

    public static <T> T getAccessLockerAssistant(String paramKey) {
        return getValue(KeyHelper.getAccessLockerKey(paramKey));
    }

    public static <T> T getAccessory(String paramKey) {
        return getValue(KeyHelper.getAccessoryKey(paramKey));
    }

    public static <T> T getSpeaker(String paramKey) {
        return getValue(KeyHelper.getSpeakerKey(paramKey));
    }

    public static <T> T getBeacon(String paramKey) {
        return getValue(KeyHelper.getBeaconKey(paramKey));
    }

    public static <T> T getSpotlight(String paramKey) {
        return getValue(KeyHelper.getSpotlightKey(paramKey));
    }

    public static <T> T getFlightAssistant(String paramKey, T defaultValue) {
        return getValue(KeyHelper.getIntelligentFlightAssistantKey(paramKey), defaultValue);
    }

    public static <T> T getAirlink(String paramKey) {
        return getValue(KeyHelper.getAirLinkKey(paramKey));
    }

    public static <T> T getWiFiAirLink(String paramKey) {
        return getValue(KeyHelper.getWiFiAirLinkKey(paramKey));
    }

    public static <T> T getOcuSyncLink(String paramKey) {
        return getValue(KeyHelper.getOcuSyncLinkKey(paramKey));
    }

    public static <T> T getGimbal(String paramKey) {
        return getValue(KeyHelper.getGimbalKey(paramKey));
    }

    public static <T> T getGimbal(int index, String paramKey) {
        return getValue(KeyHelper.getGimbalKey(index, paramKey));
    }

    public static <T> T getRemoteController(String paramKey) {
        return getValue(KeyHelper.getRemoteControllerKey(paramKey));
    }

    public static <T> T getValue(DJISDKCacheKey keyPath) {
        return getValue(keyPath, null);
    }

    public static <T> T getValue(DJISDKCacheKey keyPath, T defaultValue) {
        T data;
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(keyPath);
        if (value == null || value.getData() == null || (data = value.getData()) == null) {
            return defaultValue;
        }
        return data;
    }

    public static <T> T transValue(DJISDKCacheParamValue value, Class<T> clazz) {
        if (value == null || value.getData() == null || !value.getData().getClass().equals(clazz)) {
            return null;
        }
        return value.getData();
    }

    public static int toInt(Object value) {
        if (value == null || !(value instanceof Integer)) {
            return 0;
        }
        return ((Integer) value).intValue();
    }

    public static int toInt(Object value, int def) {
        if (value == null || !(value instanceof Integer)) {
            return def;
        }
        return ((Integer) value).intValue();
    }

    public static boolean toBool(Object value) {
        if (value == null || !(value instanceof Boolean)) {
            return false;
        }
        return ((Boolean) value).booleanValue();
    }

    public static boolean toBool(Object value, boolean defaultValue) {
        if (value == null || !(value instanceof Boolean)) {
            return defaultValue;
        }
        return ((Boolean) value).booleanValue();
    }

    public static float toFloat(Object value) {
        if (value == null || !(value instanceof Float)) {
            return 0.0f;
        }
        return ((Float) value).floatValue();
    }

    public static float toFloat(Object value, float defaultValue) {
        if (value == null || !(value instanceof Float)) {
            return defaultValue;
        }
        return ((Float) value).floatValue();
    }

    public static double toDouble(Object value) {
        if (value == null || !(value instanceof Double)) {
            return 0.0d;
        }
        return ((Double) value).doubleValue();
    }

    public static long toLong(Object value) {
        if (value == null || !(value instanceof Long)) {
            return 0;
        }
        return ((Long) value).longValue();
    }

    public static short toShort(Object value) {
        if (value == null || !(value instanceof Short)) {
            return 0;
        }
        return ((Short) value).shortValue();
    }

    public static boolean containsCameraKey(String paramKey, DJIParamAccessListener listener) {
        return DJISDKCache.getInstance().containsKey(KeyHelper.getCameraKey(paramKey), listener);
    }

    public static void setCamera(int index, String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getCameraKey(index, paramKey), value, callback);
    }

    public static void setCamera(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getCameraKey(paramKey), value, callback);
    }

    public static void setBattery(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getBatteryKey(paramKey), value, callback);
    }

    public static void setBattery(String paramKey, int subComponentIndex, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getBatteryKey(subComponentIndex, paramKey), value, callback);
    }

    public static void setFlightController(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getFlightControllerKey(paramKey), value, callback);
    }

    public static void setWifiAirLink(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getWiFiAirLinkKey(paramKey), value, callback);
    }

    public static void setRTK(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getRTKKey(paramKey), value, callback);
    }

    public static void performActionToFlightController(int index, String paramKey, DJIActionCallback callback, Object... value) {
        performAction(KeyHelper.getFlightControllerKey(paramKey), callback, value);
    }

    public static void setFlightAssistant(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getIntelligentFlightAssistantKey(paramKey), value, callback);
    }

    public static void setDataProtectionFlightAssistant(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getAccessLockerKey(paramKey), value, callback);
    }

    public static void setRemoteController(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getRemoteControllerKey(paramKey), value, callback);
    }

    public static void performActionToRemoteController(String paramKey, DJIActionCallback callback, Object... value) {
        performAction(KeyHelper.getRemoteControllerKey(paramKey), callback, value);
    }

    public static void performActionToCamera(int index, String paramKey, DJIActionCallback callback, Object... value) {
        performAction(KeyHelper.getCameraKey(index, paramKey), callback, value);
    }

    public static void performActionToDataProtectionAssistant(String paramKey, DJIActionCallback callback, Object... value) {
        performAction(KeyHelper.getAccessLockerKey(paramKey), callback, value);
    }

    public static void performActionToVirtualFence(String paramKey, DJIActionCallback callback, Object... value) {
        performAction(KeyHelper.getVirtualFenceKey(paramKey), callback, value);
    }

    public static void setGimbal(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getGimbalKey(paramKey), value, callback);
    }

    public static void setGimbal(int index, String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getGimbalKey(index, paramKey), value, callback);
    }

    public static void setAccessory(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getAccessoryKey(paramKey), value, callback);
    }

    public static void setSpeaker(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getSpeakerKey(paramKey), value, callback);
    }

    public static void setNavigationLED(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getBeaconKey(paramKey), value, callback);
    }

    public static void setSearchlightLED(String paramKey, Object value, DJISetCallback callback) {
        setValue(KeyHelper.getSpotlightKey(paramKey), value, callback);
    }

    public static void performActionToGimbal(int index, String paramKey, DJIActionCallback callback, Object... value) {
        performAction(KeyHelper.getGimbalKey(index, paramKey), callback, value);
    }

    public static void addProductListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getProductKey(paramKeys));
    }

    public static void addProductListener(DJIParamAccessListener listener, String paramKeys) {
        addListener(listener, KeyHelper.getProductKey(paramKeys));
    }

    public static void addCameraListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getCameraKey(paramKeys));
    }

    public static void addCameraListener(DJIParamAccessListener listener, int index, String... paramKeys) {
        addListener(listener, KeyHelper.getCameraKey(index, paramKeys));
    }

    public static void addGimbalListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getGimbalKey(paramKeys));
    }

    public static void addGimbalListener(DJIParamAccessListener listener, int index, String... paramKeys) {
        addListener(listener, KeyHelper.getGimbalKey(index, paramKeys));
    }

    public static void addBatteryListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getBatteryKey(paramKeys));
    }

    public static void addBatteryListener(DJIParamAccessListener listener, int subComponentIndex, String... paramKeys) {
        addListener(listener, KeyHelper.getBatteryKey(subComponentIndex, paramKeys));
    }

    public static void addBatteryAggregationListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getBatteryAggregationKey(paramKeys));
    }

    public static void addRemoteControllerListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getRemoteControllerKey(paramKeys));
    }

    public static void addFlightControllerListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getFlightControllerKey(paramKeys));
    }

    public static void addRTKListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getRTKKeys(paramKeys));
    }

    public static void addFlightAssistantListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getIntelligentFlightAssistantKeys(paramKeys));
    }

    public static void addDataProtectionFlightAssistantListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getAccessLockerKeys(paramKeys));
    }

    public static void addAirlinkListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getAirLinkKey(paramKeys));
    }

    public static void addWiFiAirlinkListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getWiFiAirLinkKey(paramKeys));
    }

    public static void addLightbridgeLinkListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getLightbridgeLinkKey(paramKeys));
    }

    public static void addOcuSyncLinkListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getOcuSyncLinkKey(paramKeys));
    }

    public static void addSpotlightListener(DJIParamAccessListener listener, String... paramKeys) {
        addListener(listener, KeyHelper.getSpotlightKeys(paramKeys));
    }

    public static void addListener(DJIParamAccessListener listener, DJISDKCacheKey keyPath) {
        DJISDKCache.getInstance().startListeningForUpdates(keyPath, listener, true);
    }

    public static void addListenerOnBackgroundThread(DJIParamAccessListener listener, DJISDKCacheKey... keyPaths) {
        for (DJISDKCacheKey keyPath : keyPaths) {
            DJISDKCache.getInstance().startListeningForUpdates(keyPath, listener, false);
        }
    }

    public static void addListener(DJIParamAccessListener listener, DJISDKCacheKey... keyPaths) {
        addListener(listener, true, keyPaths);
    }

    public static void addListener(DJIParamAccessListener listener, boolean inMainThread, DJISDKCacheKey... keyPaths) {
        for (DJISDKCacheKey keyPath : keyPaths) {
            DJISDKCache.getInstance().startListeningForUpdates(keyPath, listener, inMainThread);
        }
    }

    @Deprecated
    public static void removeKeyListener(DJIParamAccessListener listener, DJISDKCacheKey... keyPaths) {
        removeListenerWithKeys(listener, keyPaths);
    }

    public static void removeListenerWithKeys(DJIParamAccessListener listener, DJISDKCacheKey... keyPaths) {
        if (keyPaths == null || keyPaths.length == 0) {
            removeListener(listener);
            return;
        }
        for (DJISDKCacheKey keyPath : keyPaths) {
            DJISDKCache.getInstance().stopListeningOnKey(keyPath, listener);
        }
    }

    public static void removeListener(DJIParamAccessListener listener) {
        DJISDKCache.getInstance().stopListening(listener);
    }

    public static void setValue(DJISDKCacheKey keyPath, Object value, DJISetCallback callback) {
        DJISDKCache.getInstance().setValue(keyPath, value, callback);
    }

    public static void performAction(DJISDKCacheKey keyPath, DJIActionCallback callback, Object... args) {
        DJISDKCache.getInstance().performAction(keyPath, callback, args);
    }

    private static class GetCallback implements DJIGetCallback {
        public DJIError error;
        public CountDownLatch latch;
        public boolean rst;
        public DJISDKCacheParamValue value;

        public GetCallback(CountDownLatch latch2) {
            this.latch = latch2;
            if (latch2 == null) {
                this.latch = new CountDownLatch(1);
            }
        }

        public void onSuccess(DJISDKCacheParamValue value2) {
            this.value = value2;
            this.rst = true;
            this.latch.countDown();
        }

        public void onFails(DJIError error2) {
            this.error = error2;
            this.rst = false;
            this.latch.countDown();
        }

        public void await() {
            try {
                this.latch.await();
            } catch (InterruptedException e) {
            }
        }

        public void await(int time, TimeUnit timeUnit) {
            try {
                this.latch.await((long) time, timeUnit);
            } catch (InterruptedException e) {
            }
        }
    }

    public static DJISDKCacheParamValue getValueSynchronously(DJISDKCacheKey key, int timeoutInMillis) {
        GetCallback callback = new GetCallback(new CountDownLatch(1));
        DJISDKCache.getInstance().getValue(key, callback);
        if (timeoutInMillis > 0) {
            callback.await(timeoutInMillis, TimeUnit.MILLISECONDS);
        } else {
            callback.await();
        }
        if (callback.rst) {
            return callback.value;
        }
        return null;
    }
}
