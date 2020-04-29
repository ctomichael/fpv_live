package dji.internal;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class FirmwareVersionLoader {
    private static final String DEFAULT = "N/A";
    private static final boolean FIRMWARE_VERSION_DEBUGGABLE = false;
    private static final String SEPARATOR = ".";
    private static FirmwareVersionLoader instance;
    /* access modifiers changed from: private */
    public String batteryFirmwareVersion = "";
    /* access modifiers changed from: private */
    public String cameraFirmwareVersion = "";
    /* access modifiers changed from: private */
    public String flightControllerFirmwareVersion = "";
    /* access modifiers changed from: private */
    public String flightControllerSerialNumber = "";
    /* access modifiers changed from: private */
    public String gimbalFirmwareVersion = "";
    /* access modifiers changed from: private */
    public String remoteControllerFirmwareVersion = "";

    public String getFlightControllerSerialNumber() {
        return this.flightControllerSerialNumber;
    }

    private FirmwareVersionLoader() {
        startListenToFirmwareVersion();
        startListenToSerialNumber();
    }

    public static synchronized FirmwareVersionLoader getInstance() {
        FirmwareVersionLoader firmwareVersionLoader;
        synchronized (FirmwareVersionLoader.class) {
            if (instance == null) {
                instance = new FirmwareVersionLoader();
            }
            firmwareVersionLoader = instance;
        }
        return firmwareVersionLoader;
    }

    private void startListenToFirmwareVersion() {
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        DJISDKCache.getInstance().startListeningForUpdates(KeyHelper.getFlightControllerKey(DJISDKCacheKeys.FIRMWARE_VERSION), new DJIParamAccessListener() {
            /* class dji.internal.FirmwareVersionLoader.AnonymousClass1 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                String unused = FirmwareVersionLoader.this.flightControllerFirmwareVersion = FirmwareVersionLoader.this.extractFirmwareVerionFromData(newValue);
            }
        }, true);
        this.flightControllerFirmwareVersion = (String) CacheHelper.getFlightController(DJISDKCacheKeys.FIRMWARE_VERSION);
        DJISDKCache.getInstance().startListeningForUpdates(builder.component(CameraKeys.COMPONENT_KEY).index(0).paramKey(DJISDKCacheKeys.FIRMWARE_VERSION).build(), new DJIParamAccessListener() {
            /* class dji.internal.FirmwareVersionLoader.AnonymousClass2 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                String unused = FirmwareVersionLoader.this.cameraFirmwareVersion = FirmwareVersionLoader.this.extractFirmwareVerionFromData(newValue);
            }
        }, true);
        this.cameraFirmwareVersion = (String) CacheHelper.getCamera(DJISDKCacheKeys.FIRMWARE_VERSION);
        DJISDKCache.getInstance().startListeningForUpdates(builder.component(GimbalKeys.COMPONENT_KEY).index(0).paramKey(DJISDKCacheKeys.FIRMWARE_VERSION).build(), new DJIParamAccessListener() {
            /* class dji.internal.FirmwareVersionLoader.AnonymousClass3 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                String unused = FirmwareVersionLoader.this.gimbalFirmwareVersion = FirmwareVersionLoader.this.extractFirmwareVerionFromData(newValue);
            }
        }, true);
        this.gimbalFirmwareVersion = (String) CacheHelper.getGimbal(DJISDKCacheKeys.FIRMWARE_VERSION);
        DJISDKCache.getInstance().startListeningForUpdates(builder.component(BatteryKeys.COMPONENT_KEY).index(0).paramKey(DJISDKCacheKeys.FIRMWARE_VERSION).build(), new DJIParamAccessListener() {
            /* class dji.internal.FirmwareVersionLoader.AnonymousClass4 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                String unused = FirmwareVersionLoader.this.batteryFirmwareVersion = FirmwareVersionLoader.this.extractFirmwareVerionFromData(newValue);
            }
        }, true);
        this.batteryFirmwareVersion = (String) CacheHelper.getBattery(DJISDKCacheKeys.FIRMWARE_VERSION);
        DJISDKCache.getInstance().startListeningForUpdates(builder.component(RemoteControllerKeys.COMPONENT_KEY).index(0).paramKey(DJISDKCacheKeys.FIRMWARE_VERSION).build(), new DJIParamAccessListener() {
            /* class dji.internal.FirmwareVersionLoader.AnonymousClass5 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                String unused = FirmwareVersionLoader.this.remoteControllerFirmwareVersion = FirmwareVersionLoader.this.extractFirmwareVerionFromData(newValue);
            }
        }, true);
        this.remoteControllerFirmwareVersion = (String) CacheHelper.getRemoteController(DJISDKCacheKeys.FIRMWARE_VERSION);
    }

    private void startListenToSerialNumber() {
        this.flightControllerSerialNumber = (String) CacheHelper.getFlightController(DJISDKCacheKeys.SERIAL_NUMBER);
        DJISDKCache.getInstance().startListeningForUpdates(new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).paramKey(DJISDKCacheKeys.SERIAL_NUMBER).build(), new DJIParamAccessListener() {
            /* class dji.internal.FirmwareVersionLoader.AnonymousClass6 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                String unused = FirmwareVersionLoader.this.flightControllerSerialNumber = (String) CacheHelper.getFlightController(DJISDKCacheKeys.SERIAL_NUMBER);
            }
        }, true);
    }

    /* access modifiers changed from: private */
    public String extractFirmwareVerionFromData(DJISDKCacheParamValue data) {
        if (data == null || data.getData() == null) {
            return "N/A";
        }
        if (data.getData() instanceof String) {
            return (String) data.getData();
        }
        if (data.getData() instanceof DataCommonGetVersion) {
            return ((DataCommonGetVersion) data.getData()).getFirmVer(SEPARATOR);
        }
        return "N/A";
    }

    public String getFlightControllerFirmwareVersion() {
        return this.flightControllerFirmwareVersion;
    }

    public String getRemoteControllerFirmwareVersion() {
        return this.remoteControllerFirmwareVersion;
    }

    public String getCameraFirmwareVersion() {
        return this.cameraFirmwareVersion;
    }

    public String getBatteryFirmwareVersion() {
        return this.batteryFirmwareVersion;
    }

    public String getGimbalFirmwareVersion() {
        return this.gimbalFirmwareVersion;
    }

    private void Log(String str) {
    }
}
