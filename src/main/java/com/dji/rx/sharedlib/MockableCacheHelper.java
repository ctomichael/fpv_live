package com.dji.rx.sharedlib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

public class MockableCacheHelper {
    public boolean isDataValid(@NonNull DJISDKCacheKey key, @NonNull DJISDKCacheParamValue value) {
        return CacheHelper.isDataValid(key, value);
    }

    public void getRemoteController(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getRemoteController(paramKey, callback);
    }

    public void getHandheld(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getHandheld(paramKey, callback);
    }

    public void getGimbal(String paramKey, DJIGetCallback callback) {
        CacheHelper.getGimbal(paramKey, callback);
    }

    public void getGimbal(int index, String paramKey, DJIGetCallback callback) {
        CacheHelper.getGimbal(index, paramKey, callback);
    }

    public void get(@NonNull DJISDKCacheKey key, @NonNull DJIGetCallback callback) {
        CacheHelper.get(key, callback);
    }

    public void getFlightController(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getFlightController(paramKey, callback);
    }

    public void getAccessory(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getAccessory(paramKey, callback);
    }

    public void getFlightAssistant(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getFlightAssistant(paramKey, callback);
    }

    public void getCamera(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getCamera(paramKey, callback);
    }

    public void getCamera(int index, @NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getCamera(index, paramKey, callback);
    }

    public void getLightbridgeLink(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getLightbridgeLink(paramKey, callback);
    }

    public void getWiFiAirLink(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getWiFiAirLink(paramKey, callback);
    }

    public void getOcuSyncLink(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getOcuSyncLink(paramKey, callback);
    }

    public void getPayload(int index, @NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getPayload(index, paramKey, callback);
    }

    public <T> T getProduct(@NonNull String paramKey) {
        return CacheHelper.getProduct(paramKey);
    }

    public <T> T getCamera(@NonNull String paramKey) {
        return CacheHelper.getCamera(paramKey);
    }

    public <T> T getCamera(int index, @NonNull String paramKey) {
        return CacheHelper.getCamera(index, paramKey);
    }

    public <T> T getLightbridgeLink(@NonNull String paramKey) {
        return CacheHelper.getLightbridgeLink(paramKey);
    }

    public <T> T getBattery(@NonNull String paramKey) {
        return CacheHelper.getBattery(paramKey);
    }

    public void getBattery(@NonNull String paramKey, @NonNull DJIGetCallback callback) {
        CacheHelper.getBattery(paramKey, callback);
    }

    public <T> T getBattery(@NonNull String paramKey, boolean isAggregation) {
        return CacheHelper.getBattery(paramKey, isAggregation);
    }

    public <T> T getBattery(@NonNull String paramKey, int subComponentIndex) {
        return CacheHelper.getBattery(paramKey, subComponentIndex);
    }

    public <T> T getFlightController(@NonNull String paramKey) {
        return CacheHelper.getFlightController(paramKey);
    }

    public <T> T getFlightAssistant(@NonNull String paramKey) {
        return CacheHelper.getFlightAssistant(paramKey);
    }

    public <T> T getAccessory(@NonNull String paramKey) {
        return CacheHelper.getAccessory(paramKey);
    }

    public <T> T getSpeaker(@NonNull String paramKey) {
        return CacheHelper.getSpeaker(paramKey);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.extension.CacheHelper.getFlightAssistant(java.lang.String, java.lang.Object):T
     arg types: [java.lang.String, T]
     candidates:
      dji.sdksharedlib.extension.CacheHelper.getFlightAssistant(java.lang.String, dji.sdksharedlib.listener.DJIGetCallback):void
      dji.sdksharedlib.extension.CacheHelper.getFlightAssistant(java.lang.String, java.lang.Object):T */
    public <T> T getFlightAssistant(@NonNull String paramKey, @NonNull T defaultValue) {
        return CacheHelper.getFlightAssistant(paramKey, (Object) defaultValue);
    }

    public <T> T getAirlink(@NonNull String paramKey) {
        return CacheHelper.getAirlink(paramKey);
    }

    public <T> T getWiFiAirLink(@NonNull String paramKey) {
        return CacheHelper.getWiFiAirLink(paramKey);
    }

    public <T> T getOcuSyncLink(@NonNull String paramKey) {
        return CacheHelper.getOcuSyncLink(paramKey);
    }

    public <T> T getGimbal(@NonNull String paramKey) {
        return CacheHelper.getGimbal(paramKey);
    }

    public <T> T getGimbal(int index, @NonNull String paramKey) {
        return CacheHelper.getGimbal(index, paramKey);
    }

    public <T> T getRemoteController(@NonNull String paramKey) {
        return CacheHelper.getRemoteController(paramKey);
    }

    @Nullable
    public <T> T getValue(@NonNull DJISDKCacheKey keyPath) {
        return CacheHelper.getValue(keyPath);
    }

    @NonNull
    public <T> T getValue(@NonNull DJISDKCacheKey keyPath, @NonNull T defaultValue) {
        return CacheHelper.getValue(keyPath, defaultValue);
    }

    @Nullable
    public <T> T transValue(@NonNull DJISDKCacheParamValue value, @NonNull Class<T> clazz) {
        return CacheHelper.transValue(value, clazz);
    }

    public int toInt(@Nullable Object value) {
        return CacheHelper.toInt(value);
    }

    public boolean toBool(@Nullable Object value) {
        return CacheHelper.toBool(value);
    }

    public boolean toBool(@Nullable Object value, boolean defaultValue) {
        return CacheHelper.toBool(value, defaultValue);
    }

    public float toFloat(@Nullable Object value) {
        return CacheHelper.toFloat(value);
    }

    public float toFloat(@Nullable Object value, float defaultValue) {
        return CacheHelper.toFloat(value, defaultValue);
    }

    public double toDouble(@Nullable Object value) {
        return CacheHelper.toDouble(value);
    }

    public long toLong(@Nullable Object value) {
        return CacheHelper.toLong(value);
    }

    public short toShort(@Nullable Object value) {
        return CacheHelper.toShort(value);
    }

    public boolean containsCameraKey(@NonNull String paramKey, @NonNull DJIParamAccessListener listener) {
        return CacheHelper.containsCameraKey(paramKey, listener);
    }

    public void setCamera(int index, @NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setCamera(index, paramKey, value, callback);
    }

    public void setCamera(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setCamera(paramKey, value, callback);
    }

    public void setBattery(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setBattery(paramKey, value, callback);
    }

    public void setBattery(@NonNull String paramKey, int subComponentIndex, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setBattery(paramKey, subComponentIndex, value, callback);
    }

    public void setFlightController(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setFlightController(paramKey, value, callback);
    }

    public void performActionToFlightController(int index, @NonNull String paramKey, @NonNull DJIActionCallback callback, @NonNull Object... value) {
        CacheHelper.performActionToFlightController(index, paramKey, callback, value);
    }

    public void setFlightAssistant(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setFlightAssistant(paramKey, value, callback);
    }

    public void setDataProtectionFlightAssistant(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setDataProtectionFlightAssistant(paramKey, value, callback);
    }

    public void setRemoteController(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setRemoteController(paramKey, value, callback);
    }

    public void performActionToRemoteController(@NonNull String paramKey, @NonNull DJIActionCallback callback, @NonNull Object... value) {
        CacheHelper.performActionToRemoteController(paramKey, callback, value);
    }

    public void performActionToCamera(int index, @NonNull String paramKey, @NonNull DJIActionCallback callback, @NonNull Object... value) {
        CacheHelper.performActionToCamera(index, paramKey, callback, value);
    }

    public void performActionToDataProtectionAssistant(@NonNull String paramKey, @NonNull DJIActionCallback callback, @NonNull Object... value) {
        CacheHelper.performActionToDataProtectionAssistant(paramKey, callback, value);
    }

    public void setGimbal(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setGimbal(paramKey, value, callback);
    }

    public void setGimbal(int index, @NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setGimbal(index, paramKey, value, callback);
    }

    public void setAccessory(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setAccessory(paramKey, value, callback);
    }

    public void setSpeaker(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setSpeaker(paramKey, value, callback);
    }

    public void setNavigationLED(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setNavigationLED(paramKey, value, callback);
    }

    public void setSearchlightLED(@NonNull String paramKey, @NonNull Object value, @NonNull DJISetCallback callback) {
        CacheHelper.setSearchlightLED(paramKey, value, callback);
    }

    public void performActionToGimbal(int index, @NonNull String paramKey, @NonNull DJIActionCallback callback, @NonNull Object... value) {
        CacheHelper.performActionToGimbal(index, paramKey, callback, value);
    }

    public void addProductListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addProductListener(listener, paramKeys);
    }

    public void addProductListener(@NonNull DJIParamAccessListener listener, @NonNull String paramKeys) {
        CacheHelper.addProductListener(listener, paramKeys);
    }

    public void addCameraListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addCameraListener(listener, paramKeys);
    }

    public void addCameraListener(@NonNull DJIParamAccessListener listener, int index, @NonNull String... paramKeys) {
        CacheHelper.addCameraListener(listener, index, paramKeys);
    }

    public void addGimbalListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addGimbalListener(listener, paramKeys);
    }

    public void addGimbalListener(@NonNull DJIParamAccessListener listener, int index, @NonNull String... paramKeys) {
        CacheHelper.addGimbalListener(listener, index, paramKeys);
    }

    public void addBatteryListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addBatteryListener(listener, paramKeys);
    }

    public void addBatteryListener(@NonNull DJIParamAccessListener listener, int subComponentIndex, @NonNull String... paramKeys) {
        CacheHelper.addBatteryListener(listener, subComponentIndex, paramKeys);
    }

    public void addBatteryAggregationListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addBatteryAggregationListener(listener, paramKeys);
    }

    public void addRemoteControllerListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addRemoteControllerListener(listener, paramKeys);
    }

    public void addFlightControllerListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addFlightControllerListener(listener, paramKeys);
    }

    public void addFlightAssistantListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addFlightAssistantListener(listener, paramKeys);
    }

    public void addDataProtectionFlightAssistantListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addDataProtectionFlightAssistantListener(listener, paramKeys);
    }

    public void addAirlinkListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addAirlinkListener(listener, paramKeys);
    }

    public void addWiFiAirlinkListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addWiFiAirlinkListener(listener, paramKeys);
    }

    public void addLightbridgeLinkListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addLightbridgeLinkListener(listener, paramKeys);
    }

    public void addOcuSyncLinkListener(@NonNull DJIParamAccessListener listener, @NonNull String... paramKeys) {
        CacheHelper.addOcuSyncLinkListener(listener, paramKeys);
    }

    public void addListener(@NonNull DJIParamAccessListener listener, @NonNull DJISDKCacheKey keyPath) {
        CacheHelper.addListener(listener, keyPath);
    }

    public void addListenerOnBackgroundThread(@NonNull DJIParamAccessListener listener, @NonNull DJISDKCacheKey... keyPaths) {
        CacheHelper.addListenerOnBackgroundThread(listener, keyPaths);
    }

    public void addListener(@NonNull DJIParamAccessListener listener, @NonNull DJISDKCacheKey... keyPaths) {
        CacheHelper.addListener(listener, keyPaths);
    }

    public void addListener(@NonNull DJIParamAccessListener listener, boolean inMainThread, @NonNull DJISDKCacheKey... keyPaths) {
        CacheHelper.addListener(listener, inMainThread, keyPaths);
    }

    @Deprecated
    public void removeKeyListener(@NonNull DJIParamAccessListener listener, @NonNull DJISDKCacheKey... keyPaths) {
        CacheHelper.removeKeyListener(listener, keyPaths);
    }

    public void removeListenerWithKeys(@NonNull DJIParamAccessListener listener, @NonNull DJISDKCacheKey... keyPaths) {
        CacheHelper.removeListenerWithKeys(listener, keyPaths);
    }

    public void removeListener(@NonNull DJIParamAccessListener listener) {
        CacheHelper.removeListener(listener);
    }

    public void setValue(@NonNull DJISDKCacheKey keyPath, @NonNull Object value, @Nullable DJISetCallback callback) {
        CacheHelper.setValue(keyPath, value, callback);
    }

    public void performAction(@NonNull DJISDKCacheKey keyPath, @Nullable DJIActionCallback callback, @NonNull Object... args) {
        CacheHelper.performAction(keyPath, callback, args);
    }
}
