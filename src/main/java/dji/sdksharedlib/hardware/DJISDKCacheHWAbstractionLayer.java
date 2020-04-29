package dji.sdksharedlib.hardware;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import dji.common.battery.BatteryOverview;
import dji.common.error.DJISDKCacheError;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.log.DJILog;
import dji.log.GlobalConfig;
import dji.midware.Lifecycle;
import dji.midware.MidWare;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataDm368SetActiveTrackCamera;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSingleSetMainCameraBandwidthPercent;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheKeyCharacteristics;
import dji.sdksharedlib.hardware.abstractions.airlink.DJIAirLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2M210Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4AAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lte.DJILteLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWm100GroundAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryBaseAggregationAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryHandheldHG300Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire1Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryM600Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryMGAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom3Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom4Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatterySparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryWM245Abstraction;
import dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction;
import dji.sdksharedlib.hardware.abstractions.battery.NonSmartBatteryAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneSAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240HasselbladAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240ZoomAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM245Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraPayloadAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraWM245DualLightVLAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3AAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3PAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3SAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3WAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4AAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4PSDRAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTau336Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTau640Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX3Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX3HandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX4SAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5HandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5RAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5RHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5SAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX7Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraZ3Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraZ3HandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3WithLb2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire1Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM100Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPM420Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom3Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM230Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM245Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalMobileHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalPayLoadAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalPhantom3Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalPhantom4Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalPhantom4PSDRAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM230Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalX3Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalXtAbstraction;
import dji.sdksharedlib.hardware.abstractions.handheldcontroller.HandheldControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.handheldcontroller.Mobile2HandheldControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.handheldcontroller.MobileHandheldControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.payload.DJIPayloadAbstraction;
import dji.sdksharedlib.hardware.abstractions.product.DJIProductAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire1Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCLB2Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCMavicAirAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom3Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom3SAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4AAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4PSDRAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCRM500Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM160Abstaction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM245Abstraction;
import dji.sdksharedlib.hardware.accessory.AccessoryAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheAutoGetterVerifier;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISDKCacheListenerLayer;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import dji.sdksharedlib.util.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJISDKCacheHWAbstractionLayer {
    public static final int DEFAULT_EXPIRATION_DURATION = 100;
    public static final int DEFAULT_SETTER_VAL_PROTECTION_DURATION = 500;
    private static final long DELAY_TIME = 3000;
    protected static final String URI_ACCESSORY = "AccessoryAggregation";
    protected static final String URI_AIR_LINK = "AirLink";
    protected static final String URI_BATTERY = "Battery";
    protected static final String URI_CAMERA = "Camera";
    protected static final String URI_FLIGHT_CONTROLLER = "FlightController";
    protected static final String URI_GIMBAL = "Gimbal";
    protected static final String URI_HANDHELD_CONTROLLER = "HandheldController";
    protected static final String URI_PAYLOAD = "Payload";
    protected static final String URI_PRODUCT = "Product";
    protected static final String URI_REMOTE_CONTROLLER = "RemoteController";
    public static final int XT2_IR_CAMERA_INDEX = 2;
    protected final String TAG = getClass().getSimpleName();
    protected DJISDKCacheAutoGetterVerifier autoGetterVerifier = null;
    private DJIParamAccessListener batteryOverviewsListener = new DJISDKCacheHWAbstractionLayer$$Lambda$0(this);
    private int countOfBatterySlot;
    private TimerTask disconnectTask = new TimerTask() {
        /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass8 */

        public void run() {
            DJISDKCacheHWAbstractionLayer.this.removeAbstraction("MockAbstraction");
        }
    };
    private DJISDKCacheError error = null;
    private long finishAddAbstractionTime;
    private boolean hasStartedListener = false;
    public Map<String, SparseArray<DJISDKCacheHWAbstraction>> hwAbstractionMap = null;
    private long initStartTime;
    DJISDKCacheHWAbstraction.OnValueChangeListener listener = new DJISDKCacheHWAbstraction.OnValueChangeListener() {
        /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass1 */

        public void onNewValueFromGetter(Object newValue, DJISDKCacheKey keyPath, DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback callback) {
            boolean unused = DJISDKCacheHWAbstractionLayer.this.updateStoredValueForKeyPath(newValue, keyPath, DJISDKCacheParamValue.Status.Valid, DJISDKCacheParamValue.Source.Get, callback);
        }

        public void onNewValueFromSetter(Object newValue, DJISDKCacheKey keyPath) {
            boolean unused = DJISDKCacheHWAbstractionLayer.this.updateStoredValueForKeyPath(newValue, keyPath, DJISDKCacheParamValue.Status.Valid, DJISDKCacheParamValue.Source.Set, null);
        }

        public void onNewValue(Object newValue, DJISDKCacheKey keyPath) {
            boolean unused = DJISDKCacheHWAbstractionLayer.this.updateStoredValueForKeyPath(newValue, keyPath, DJISDKCacheParamValue.Status.Valid, DJISDKCacheParamValue.Source.Push, null);
        }
    };
    private Runnable printAbstractionRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass2 */

        public void run() {
            DJISDKCacheHWAbstractionLayer.this.printAbstractions();
        }
    };
    protected DJISDKCacheStoreLayer storeLayer = null;
    protected Runnable updateComponentRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass7 */

        public void run() {
            EventBus.getDefault().post(new AbstractionChangeEvent());
        }
    };

    public static class AbstractionChangeEvent {
    }

    public void init(DJISDKCacheStoreLayer storeLayer2, DJISDKCacheListenerLayer listenerLayer) {
        DJILog.d(this.TAG, "init", new Object[0]);
        this.initStartTime = System.currentTimeMillis();
        this.storeLayer = storeLayer2;
        this.hwAbstractionMap = new ConcurrentHashMap();
        DJIEventBusUtil.register(this);
        this.autoGetterVerifier = new DJISDKCacheAutoGetterVerifier();
        this.autoGetterVerifier.init(this, listenerLayer);
        initProductAbstraction();
        initCamera();
        initBattery();
        initGimbalController();
        initHandheldController();
        initFlightController();
        initAccessoryAggregation();
        initAirLink();
        initRc();
    }

    public void destroy() {
        DJILog.d(this.TAG, Lifecycle.STATUS_DESTROY, new Object[0]);
        removeBatteryOverviewsListener();
        DJIEventBusUtil.unRegister(this);
        this.autoGetterVerifier.destroy();
    }

    public void setValue(DJISDKCacheKey keyPath, Object value, DJISetCallback callback) {
        DJISDKCacheHWAbstraction hw = getComponent(keyPath);
        if (hw != null) {
            hw.setValue(keyPath, value, callback);
        } else if (callback != null) {
            callback.onFails(DJISDKCacheError.INVALID_KEY_FOR_COMPONENT);
        }
    }

    public void getValue(DJISDKCacheKey keyPath, DJIGetCallback callback) {
        DJISDKCacheParamValue data;
        DJISDKCacheParamValue data2;
        DJISDKCacheHWAbstraction realHw = getComponentTillSub(keyPath);
        if (realHw != null) {
            if (realHw.isPushKey(keyPath) && this.storeLayer.getValue(keyPath) != null) {
                DJISDKCacheParamValue data3 = this.storeLayer.getValue(keyPath);
                if (callback != null) {
                    callback.onSuccess(data3);
                }
            } else if (TextUtils.isEmpty(keyPath.getSubComponent())) {
                if (!realHw.isUserDrivenKey(keyPath) || (data2 = this.storeLayer.getValue(keyPath)) == null) {
                    realHw.getValue(keyPath, callback);
                } else if (callback != null) {
                    callback.onSuccess(data2);
                }
            } else if (realHw.isContainKey(keyPath)) {
                if (!realHw.isUserDrivenKey(keyPath) || (data = this.storeLayer.getValue(keyPath)) == null) {
                    realHw.getValue(keyPath.extractSubComponentKeyPath(), callback);
                } else if (callback != null) {
                    callback.onSuccess(data);
                }
            } else if (callback != null) {
                callback.onFails(DJISDKCacheError.KEY_UNSUPPORTED);
            }
        } else if (callback != null) {
            callback.onFails(DJISDKCacheError.INVALID_KEY_FOR_COMPONENT);
        }
    }

    public void performAction(DJISDKCacheKey keyPath, DJIActionCallback callback, Object... args) {
        DJISDKCacheHWAbstraction hw = getComponent(keyPath);
        if (hw != null) {
            hw.performAction(keyPath, callback, args);
        } else if (callback != null) {
            callback.onFails(DJISDKCacheError.INVALID_KEY_FOR_COMPONENT);
        }
    }

    private DJISDKCacheHWAbstraction getComponent(DJISDKCacheKey cacheKey) {
        SparseArray<DJISDKCacheHWAbstraction> hws;
        if (cacheKey == null || cacheKey.getComponent() == null || (hws = this.hwAbstractionMap.get(cacheKey.getComponent())) == null) {
            return null;
        }
        return hws.get(cacheKey.getIndex());
    }

    private DJISDKCacheHWAbstraction getComponentTillSub(DJISDKCacheKey cacheKey) {
        return getComponentTillSub(getComponent(cacheKey), cacheKey);
    }

    private DJISDKCacheHWAbstraction getComponentTillSub(DJISDKCacheHWAbstraction hw, DJISDKCacheKey cacheKey) {
        if (hw == null || cacheKey == null) {
            return null;
        }
        if (!(cacheKey.getSubComponent() == null || hw.getSubComponents() == null || hw.getSubComponents().get(cacheKey.getSubComponent()) == null)) {
            hw = (DJISDKCacheHWAbstraction) hw.getSubComponents().get(cacheKey.getSubComponent()).get(Integer.valueOf(cacheKey.getSubComponentIndex()));
        }
        return hw;
    }

    private DJISDKCacheHWAbstraction getComponentIncludeSub(DJISDKCacheKey cacheKey) {
        DJISDKCacheHWAbstraction hw = getComponent(cacheKey);
        if (hw == null || TextUtils.isEmpty(cacheKey.getSubComponent())) {
            return hw;
        }
        return hw.getSubComponent(cacheKey);
    }

    /* access modifiers changed from: protected */
    public void initProductAbstraction() {
        removeAbstraction("Product");
        addAbstraction(1, "Product", DJIProductAbstraction.class);
    }

    private String convertToName(DJISDKCacheHWAbstraction DJISDKCacheHWAbstraction, String key) {
        return DJISDKCacheHWAbstraction.toString() + "." + key;
    }

    /* access modifiers changed from: protected */
    public synchronized void removeAbstraction(String hwName) {
        SparseArray<DJISDKCacheHWAbstraction> abstractions = this.hwAbstractionMap.get(hwName);
        if (abstractions != null && abstractions.size() > 0) {
            int size = abstractions.size();
            for (int i = 0; i < size; i++) {
                DJISDKCacheHWAbstraction abstraction = (DJISDKCacheHWAbstraction) abstractions.valueAt(i);
                abstraction.destroy();
                this.storeLayer.removeAllAbstractionValues(hwName, abstraction.getAbstrationIndex());
            }
            this.hwAbstractionMap.remove(hwName);
        }
        printAbstractionWithDelay();
    }

    /* access modifiers changed from: protected */
    public synchronized void removeAbstractionWithIndex(int index, String hwName) {
        int keyIndex;
        SparseArray<DJISDKCacheHWAbstraction> abstractions = this.hwAbstractionMap.get(hwName);
        if (abstractions != null && (keyIndex = abstractions.indexOfKey(index)) >= 0) {
            DJISDKCacheHWAbstraction abstraction = (DJISDKCacheHWAbstraction) abstractions.valueAt(keyIndex);
            abstraction.destroy();
            this.storeLayer.removeAllAbstractionValues(hwName, abstraction.getAbstrationIndex());
            abstractions.remove(index);
        }
        printAbstractionWithDelay();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:5:0x000a=Splitter:B:5:0x000a, B:17:0x003e=Splitter:B:17:0x003e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void addAbstraction(int r13, java.lang.String r14, java.lang.Class<? extends dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction> r15) {
        /*
            r12 = this;
            r10 = 2147483647(0x7fffffff, float:NaN)
            monitor-enter(r12)
            if (r13 <= 0) goto L_0x000a
            if (r15 == 0) goto L_0x000a
            if (r14 != 0) goto L_0x0017
        L_0x000a:
            java.lang.String r9 = r12.TAG     // Catch:{ all -> 0x0093 }
            java.lang.String r10 = "addAbstraction: invalid input parameters"
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0093 }
            dji.log.DJILog.d(r9, r10, r11)     // Catch:{ all -> 0x0093 }
        L_0x0015:
            monitor-exit(r12)
            return
        L_0x0017:
            java.util.Map<java.lang.String, android.util.SparseArray<dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction>> r9 = r12.hwAbstractionMap     // Catch:{ all -> 0x0093 }
            boolean r9 = r9.containsKey(r14)     // Catch:{ all -> 0x0093 }
            if (r9 == 0) goto L_0x0021
            if (r13 != r10) goto L_0x0015
        L_0x0021:
            android.util.SparseArray r2 = new android.util.SparseArray     // Catch:{ all -> 0x0093 }
            r2.<init>()     // Catch:{ all -> 0x0093 }
            if (r13 != r10) goto L_0x00bd
            java.lang.Object r0 = r15.newInstance()     // Catch:{ Exception -> 0x0096 }
            dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction r0 = (dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction) r0     // Catch:{ Exception -> 0x0096 }
            r9 = 2147483647(0x7fffffff, float:NaN)
            dji.sdksharedlib.store.DJISDKCacheStoreLayer r10 = r12.storeLayer     // Catch:{ Exception -> 0x0096 }
            dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$OnValueChangeListener r11 = r12.listener     // Catch:{ Exception -> 0x0096 }
            r0.init(r14, r9, r10, r11)     // Catch:{ Exception -> 0x0096 }
            r9 = 2147483647(0x7fffffff, float:NaN)
            r2.put(r9, r0)     // Catch:{ Exception -> 0x0096 }
        L_0x003e:
            java.util.Map<java.lang.String, android.util.SparseArray<dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction>> r9 = r12.hwAbstractionMap     // Catch:{ all -> 0x0093 }
            boolean r9 = r9.containsKey(r14)     // Catch:{ all -> 0x0093 }
            if (r9 != 0) goto L_0x00fa
            java.util.Map<java.lang.String, android.util.SparseArray<dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction>> r9 = r12.hwAbstractionMap     // Catch:{ all -> 0x0093 }
            r9.put(r14, r2)     // Catch:{ all -> 0x0093 }
        L_0x004b:
            java.util.Map<java.lang.String, android.util.SparseArray<dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction>> r9 = r12.hwAbstractionMap     // Catch:{ all -> 0x0093 }
            java.lang.Object r4 = r9.get(r14)     // Catch:{ all -> 0x0093 }
            android.util.SparseArray r4 = (android.util.SparseArray) r4     // Catch:{ all -> 0x0093 }
            r3 = 0
            int r7 = r4.size()     // Catch:{ all -> 0x0093 }
        L_0x0058:
            if (r3 >= r7) goto L_0x011a
            java.lang.Object r0 = r4.valueAt(r3)     // Catch:{ all -> 0x0093 }
            dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction r0 = (dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction) r0     // Catch:{ all -> 0x0093 }
            r0.syncPushDataFromMidware()     // Catch:{ all -> 0x0093 }
            java.util.Map r9 = r0.getSubComponents()     // Catch:{ all -> 0x0093 }
            java.util.Collection r9 = r9.values()     // Catch:{ all -> 0x0093 }
            java.util.Iterator r9 = r9.iterator()     // Catch:{ all -> 0x0093 }
        L_0x006f:
            boolean r10 = r9.hasNext()     // Catch:{ all -> 0x0093 }
            if (r10 == 0) goto L_0x0116
            java.lang.Object r5 = r9.next()     // Catch:{ all -> 0x0093 }
            java.util.Map r5 = (java.util.Map) r5     // Catch:{ all -> 0x0093 }
            java.util.Collection r10 = r5.values()     // Catch:{ all -> 0x0093 }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x0093 }
        L_0x0083:
            boolean r11 = r10.hasNext()     // Catch:{ all -> 0x0093 }
            if (r11 == 0) goto L_0x006f
            java.lang.Object r8 = r10.next()     // Catch:{ all -> 0x0093 }
            dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction r8 = (dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction) r8     // Catch:{ all -> 0x0093 }
            r8.syncPushDataFromMidware()     // Catch:{ all -> 0x0093 }
            goto L_0x0083
        L_0x0093:
            r9 = move-exception
            monitor-exit(r12)
            throw r9
        L_0x0096:
            r1 = move-exception
            java.lang.String r9 = r12.TAG     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            r10.<init>()     // Catch:{ all -> 0x0093 }
            java.lang.String r11 = "DJISDKCacheHWAbstractionLayer addAbstraction * Exception  : "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r10 = r10.append(r14)     // Catch:{ all -> 0x0093 }
            java.lang.String r11 = dji.log.DJILog.exceptionToString(r1)     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0093 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0093 }
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0093 }
            dji.log.DJILog.d(r9, r10, r11)     // Catch:{ all -> 0x0093 }
            goto L_0x0015
        L_0x00bd:
            r3 = 0
        L_0x00be:
            if (r3 >= r13) goto L_0x003e
            java.lang.Object r0 = r15.newInstance()     // Catch:{ Exception -> 0x00d3 }
            dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction r0 = (dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction) r0     // Catch:{ Exception -> 0x00d3 }
            dji.sdksharedlib.store.DJISDKCacheStoreLayer r9 = r12.storeLayer     // Catch:{ Exception -> 0x00d3 }
            dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$OnValueChangeListener r10 = r12.listener     // Catch:{ Exception -> 0x00d3 }
            r0.init(r14, r3, r9, r10)     // Catch:{ Exception -> 0x00d3 }
            r2.put(r3, r0)     // Catch:{ Exception -> 0x00d3 }
            int r3 = r3 + 1
            goto L_0x00be
        L_0x00d3:
            r1 = move-exception
            java.lang.String r9 = r12.TAG     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            r10.<init>()     // Catch:{ all -> 0x0093 }
            java.lang.String r11 = "DJISDKCacheHWAbstractionLayer addAbstraction Exception  : "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r10 = r10.append(r14)     // Catch:{ all -> 0x0093 }
            java.lang.String r11 = dji.log.DJILog.exceptionToString(r1)     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0093 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0093 }
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0093 }
            dji.log.DJILog.e(r9, r10, r11)     // Catch:{ all -> 0x0093 }
            goto L_0x0015
        L_0x00fa:
            java.util.Map<java.lang.String, android.util.SparseArray<dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction>> r9 = r12.hwAbstractionMap     // Catch:{ all -> 0x0093 }
            java.lang.Object r6 = r9.get(r14)     // Catch:{ all -> 0x0093 }
            android.util.SparseArray r6 = (android.util.SparseArray) r6     // Catch:{ all -> 0x0093 }
            r9 = 2147483647(0x7fffffff, float:NaN)
            r10 = 2147483647(0x7fffffff, float:NaN)
            java.lang.Object r10 = r2.get(r10)     // Catch:{ all -> 0x0093 }
            r6.put(r9, r10)     // Catch:{ all -> 0x0093 }
            java.util.Map<java.lang.String, android.util.SparseArray<dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction>> r9 = r12.hwAbstractionMap     // Catch:{ all -> 0x0093 }
            r9.put(r14, r6)     // Catch:{ all -> 0x0093 }
            goto L_0x004b
        L_0x0116:
            int r3 = r3 + 1
            goto L_0x0058
        L_0x011a:
            r12.printAbstractionWithDelay()     // Catch:{ all -> 0x0093 }
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.addAbstraction(int, java.lang.String, java.lang.Class):void");
    }

    /* access modifiers changed from: protected */
    public synchronized void addAbstractionWithIndex(int index, String componentName, Class<? extends DJISDKCacheHWAbstraction> componentClass) {
        if (index < 0 || componentClass == null || componentName == null) {
            DJILog.d(this.TAG, "addAbstraction: invalid input parameters", new Object[0]);
        } else {
            SparseArray<DJISDKCacheHWAbstraction> hws = new SparseArray<>();
            boolean hasContain = this.hwAbstractionMap.containsKey(componentName);
            try {
                DJISDKCacheHWAbstraction abstraction = (DJISDKCacheHWAbstraction) componentClass.newInstance();
                abstraction.init(componentName, index, this.storeLayer, this.listener);
                if (hasContain) {
                    this.hwAbstractionMap.get(componentName).put(index, abstraction);
                } else {
                    hws.put(index, abstraction);
                }
                if (!hasContain) {
                    this.hwAbstractionMap.put(componentName, hws);
                }
                SparseArray<DJISDKCacheHWAbstraction> items = this.hwAbstractionMap.get(componentName);
                int size = items.size();
                for (int i = 0; i < size; i++) {
                    DJISDKCacheHWAbstraction abstraction2 = items.valueAt(i);
                    abstraction2.syncPushDataFromMidware();
                    for (Map<Integer, DJISDKCacheHWAbstraction> map : abstraction2.getSubComponents().values()) {
                        for (DJISDKCacheHWAbstraction subComponent : map.values()) {
                            subComponent.syncPushDataFromMidware();
                        }
                    }
                }
                printAbstractionWithDelay();
            } catch (Exception e) {
                DJILog.e(this.TAG, "DJISDKCacheHWAbstractionLayer addAbstraction Exception  : " + componentName + DJILog.exceptionToString(e), new Object[0]);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean updateStoredValueForKeyPath(Object newValue, DJISDKCacheKey keyPath, DJISDKCacheParamValue.Status paramValueStatus, DJISDKCacheParamValue.Source paramValueSource, DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback callback) {
        DJISDKCacheKeyCharacteristics characteristics = null;
        DJISDKCacheHWAbstraction hwAbstraction = getComponentIncludeSub(keyPath);
        if (hwAbstraction != null) {
            characteristics = hwAbstraction.getCharacteristics(keyPath);
        }
        if (characteristics == null) {
            return false;
        }
        boolean isEventType = characteristics.isEventUpdateType();
        DJISDKCacheParamValue value = null;
        if (!isEventType) {
            value = this.storeLayer.getValue(keyPath);
        }
        int expirationDuration = characteristics.getExpirationDuation();
        int protectDuration = characteristics.getProtectDuration();
        if (value == null && newValue == null) {
            notifyUpdateResult(false, newValue, callback);
            return false;
        } else if (value == null || newValue == null) {
            if (value != null) {
                this.storeLayer.removeValue(keyPath);
                notifyUpdateResult(true, newValue, callback);
                return true;
            }
            DJISDKCacheParamValue newParamValue = new DJISDKCacheParamValue(newValue, paramValueStatus, paramValueSource, (long) expirationDuration);
            if (isEventType) {
                this.storeLayer.sendEvent(newParamValue, keyPath);
            } else {
                this.storeLayer.replaceValue(newParamValue, keyPath);
            }
            notifyUpdateResult(true, newValue, callback);
            return true;
        } else if (value.isDataEquals(newValue)) {
            value.updateCreatedAt();
            if (callback != null) {
                callback.onNoChange(value);
            }
            return true;
        } else if (value.getSource() == DJISDKCacheParamValue.Source.Set && paramValueSource == DJISDKCacheParamValue.Source.Push && System.currentTimeMillis() - value.getCreatedAt() < ((long) protectDuration)) {
            return false;
        } else {
            this.storeLayer.replaceValue(new DJISDKCacheParamValue(newValue, paramValueStatus, paramValueSource, (long) expirationDuration), keyPath);
            notifyUpdateResult(true, newValue, callback);
            return true;
        }
    }

    private void notifyUpdateResult(Boolean success, Object newValue, DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback callback) {
        if (callback == null) {
            return;
        }
        if (success.booleanValue()) {
            callback.onSuccess(newValue);
        } else {
            callback.onFails(this.error);
        }
    }

    public void addMockAbstraction(int count, String mockComponentName, Class<? extends DJISDKCacheHWAbstraction> mockComponentClass) {
        addAbstraction(count, mockComponentName, mockComponentClass);
    }

    private void printAbstractionWithDelay() {
        if (GlobalConfig.DEBUG) {
            DJISDKCacheThreadManager.removePendingRunnableInUIThread(this.printAbstractionRunnable);
            DJISDKCacheThreadManager.invokeDelay(this.printAbstractionRunnable, DELAY_TIME, true);
        }
    }

    /* access modifiers changed from: private */
    public void printAbstractions() {
        this.finishAddAbstractionTime = System.currentTimeMillis() - DELAY_TIME;
        DJILog.d(this.TAG, "initHWLayer total time=" + (this.finishAddAbstractionTime - this.initStartTime), new Object[0]);
        StringBuilder builder = new StringBuilder();
        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        builder.append("\r\n ========================");
        builder.append("\r\n" + strDate);
        for (Map.Entry<String, SparseArray<DJISDKCacheHWAbstraction>> entry : this.hwAbstractionMap.entrySet()) {
            int i = 0;
            int size = ((SparseArray) entry.getValue()).size();
            while (i < size) {
                builder.append("\r\n " + ((String) entry.getKey()) + " : ");
                DJISDKCacheHWAbstraction hw = (DJISDKCacheHWAbstraction) ((SparseArray) entry.getValue()).valueAt(i);
                builder.append(hw.getClass().getSimpleName() + " index:" + hw.getAbstrationIndex());
                if (!GlobalConfig.DEBUG || hw.getAbstrationIndex() == ((SparseArray) entry.getValue()).keyAt(i)) {
                    if (hw.getSubComponents() != null && hw.getSubComponents().size() > 0) {
                        Set<Map.Entry<String, Map<Integer, DJISDKCacheHWAbstraction>>> subSets = hw.getSubComponents().entrySet();
                        builder.append("\r\n SubComponent:\r\n ");
                        builder.append(IMemberProtocol.STRING_SEPERATOR_LEFT);
                        for (Map.Entry<String, Map<Integer, DJISDKCacheHWAbstraction>> subEntry : subSets) {
                            for (Map.Entry<Integer, DJISDKCacheHWAbstraction> subComponentEntry : subEntry.getValue().entrySet()) {
                                builder.append(((String) subEntry.getKey()) + ":");
                                DJISDKCacheHWAbstraction subHw = (DJISDKCacheHWAbstraction) subComponentEntry.getValue();
                                builder.append(subHw.getClass().getSimpleName());
                                builder.append(" with AbstractionIndex:");
                                builder.append(subHw.getAbstrationIndex());
                                builder.append(" with SparseArrayKeyIndex:");
                                builder.append(subComponentEntry.getKey());
                                builder.append("\r\n ");
                            }
                        }
                        builder.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
                    }
                    builder.append(", ");
                    i++;
                } else {
                    throw new RuntimeException("index not match in Abstraction! class:" + hw.getClass().getSimpleName() + "AbstractionIndex" + hw.getAbstrationIndex() + "SparseArrayKeyIndex" + ((SparseArray) entry.getValue()).keyAt(i));
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("\r\n ========================");
        builder.append("\r\n ");
        DJILog.saveAsync(this.TAG, builder.toString());
    }

    private void initBattery() {
        addBatteryAbstraction();
    }

    private void updateBattery() {
        removeBatteryOverviewsListener();
        removeAbstraction("Battery");
        addBatteryAbstraction();
    }

    /* access modifiers changed from: protected */
    public void addBatteryAbstraction() {
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
        DJILog.d(this.TAG, "battery add abstration. platformType = " + type, new Object[0]);
        switch (type) {
            case P3c:
            case P3x:
            case P3s:
            case P3w:
                addAbstraction(1, "Battery", BatteryPhantom3Abstraction.class);
                return;
            case P4:
                addAbstraction(1, "Battery", BatteryPhantom4Abstraction.class);
                return;
            case P4P:
            case P4A:
            case P4PSDR:
            case P4RTK:
                addAbstraction(1, "Battery", BatteryPhantom4PAbstraction.class);
                return;
            case M100:
            case Inspire:
                addAbstraction(1, "Battery", BatteryInspire1Abstraction.class);
                return;
            case M600:
            case M600Pro:
                this.countOfBatterySlot = 6;
                addBatteryOverviewsListener();
                addAbstraction(Integer.MAX_VALUE, "Battery", BatteryBaseAggregationAbstraction.class);
                return;
            case M200:
            case M210:
            case M210RTK:
            case PM420:
            case PM420PRO:
            case PM420PRO_RTK:
            case Inspire2:
                this.countOfBatterySlot = 2;
                addBatteryOverviewsListener();
                addAbstraction(Integer.MAX_VALUE, "Battery", BatteryBaseAggregationAbstraction.class);
                return;
            case OSMO:
                addAbstraction(1, "Battery", BatteryHandheldAbstraction.class);
                return;
            case OSMOMobile:
                addAbstraction(1, "Battery", BatteryHandheldHG300Abstraction.class);
                return;
            case FoldingDrone:
                addAbstraction(1, "Battery", BatteryFoldingDroneAbstraction.class);
                return;
            case Spark:
            case WM230:
                DJILog.d(this.TAG, "battery add folding drong abstraction", new Object[0]);
                addAbstraction(1, "Battery", BatterySparkAbstraction.class);
                return;
            case WM240:
                addAbstraction(1, "Battery", BatteryWM240Abstraction.class);
                return;
            case WM245:
                addAbstraction(1, "Battery", BatteryWM245Abstraction.class);
                return;
            case A3:
            case N3:
                if (DJIComponentManager.getInstance().getSmartBatteryComponentType() == DJIComponentManager.SmartBatteryComponentType.MgBattery) {
                    addAbstraction(1, "Battery", BatteryMGAbstraction.class);
                    return;
                } else {
                    addAbstraction(1, "Battery", NonSmartA3BatteryAbstraction.class);
                    return;
                }
            case A2:
                addAbstraction(1, "Battery", NonSmartBatteryAbstraction.class);
                return;
            case WM160:
                addAbstraction(1, "Battery", BatteryWM160Abstraction.class);
                return;
            default:
                return;
        }
    }

    private void addBatteryOverviewsListener() {
        if (!this.hasStartedListener) {
            CacheHelper.addBatteryAggregationListener(this.batteryOverviewsListener, BatteryKeys.OVERVIEWS);
            this.hasStartedListener = true;
        }
    }

    private void removeBatteryOverviewsListener() {
        if (this.hasStartedListener) {
            CacheHelper.removeListener(this.batteryOverviewsListener);
            this.hasStartedListener = false;
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$DJISDKCacheHWAbstractionLayer(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null) {
            updateBatteryConnectivity((BatteryOverview[]) newValue.getData());
        }
    }

    private void updateBatteryConnectivity(BatteryOverview[] batteryOverviews) {
        if (batteryOverviews == null || batteryOverviews.length != this.countOfBatterySlot) {
            DJILog.d(this.TAG, "The length of batteryOverviews is not equal the countOfBatterySlot batteryOverviews.length=" + (batteryOverviews != null ? Integer.valueOf(batteryOverviews.length) : "null"), new Object[0]);
            return;
        }
        DJILog.d(this.TAG, "The length of batteryOverviews is:" + batteryOverviews.length, new Object[0]);
        ArrayList<Integer> connectedIndexList = generateRecipeWithEnergies(batteryOverviews);
        DJILog.d(this.TAG, "The connectedIndexList size:" + connectedIndexList.size() + " list: \n" + this.TAG + " item:" + TextUtils.join("\n" + this.TAG + " item:", connectedIndexList), new Object[0]);
        updateBatteryIfNeeded(connectedIndexList);
    }

    private ArrayList<Integer> generateRecipeWithEnergies(BatteryOverview[] batteryOverviews) {
        ArrayList<Integer> connectedIndexList = new ArrayList<>();
        for (int i = 0; i < this.countOfBatterySlot; i++) {
            if (batteryOverviews[i].isConnected()) {
                connectedIndexList.add(Integer.valueOf(i));
            }
        }
        return connectedIndexList;
    }

    private void updateBatteryIfNeeded(@NonNull ArrayList<Integer> receipt) {
        SparseArray<DJISDKCacheHWAbstraction> hws = this.hwAbstractionMap.get("Battery");
        if (hws != null && hws.size() > 0 && receipt.size() > 0) {
            if (hws.size() == 1) {
                Iterator<Integer> it2 = receipt.iterator();
                while (it2.hasNext()) {
                    addMultiBatteryAbstractionsWithIndex(it2.next().intValue());
                }
                notifyComponentChanged();
                return;
            }
            boolean batteryChanged = false;
            for (int i = 0; i < hws.size(); i++) {
                int key = hws.keyAt(i);
                if (key != Integer.MAX_VALUE && !receipt.contains(Integer.valueOf(key))) {
                    DJILog.d(this.TAG, "remove battery with index " + key, new Object[0]);
                    removeAbstractionWithIndex(key, "Battery");
                    batteryChanged = true;
                }
            }
            Iterator<Integer> it3 = receipt.iterator();
            while (it3.hasNext()) {
                int i2 = it3.next().intValue();
                if (hws.indexOfKey(i2) < 0) {
                    addMultiBatteryAbstractionsWithIndex(i2);
                    batteryChanged = true;
                }
            }
            if (batteryChanged) {
                notifyComponentChanged();
            }
        }
    }

    private void addMultiBatteryAbstractionsWithIndex(int index) {
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        DJILog.d(this.TAG, "add battery for " + type.toString() + " with index:" + index, new Object[0]);
        switch (type) {
            case M600:
            case M600Pro:
                addAbstractionWithIndex(index, "Battery", BatteryM600Abstraction.class);
                return;
            case M200:
            case M210:
            case M210RTK:
            case PM420:
            case PM420PRO:
            case PM420PRO_RTK:
            case Inspire2:
                addAbstractionWithIndex(index, "Battery", BatteryInspire2Abstraction.class);
                return;
            default:
                return;
        }
    }

    private void initHandheldController() {
        addHandheldControllerAbstraction();
    }

    private void updateHandheldController() {
        removeAbstraction("HandheldController");
        addHandheldControllerAbstraction();
    }

    private void addHandheldControllerAbstraction() {
        switch (DJIComponentManager.getInstance().getPlatformType()) {
            case OSMO:
                addAbstraction(1, "HandheldController", HandheldControllerAbstraction.class);
                return;
            case OSMOMobile:
                if (DJIComponentManager.getInstance().getGimbalComponentType() == DJIComponentManager.GimbalComponentType.OSMO_2) {
                    addAbstraction(1, "HandheldController", Mobile2HandheldControllerAbstraction.class);
                    return;
                } else {
                    addAbstraction(1, "HandheldController", MobileHandheldControllerAbstraction.class);
                    return;
                }
            default:
                return;
        }
    }

    private void initAirLink() {
        addAirLinkAbstraction();
    }

    private void updateAirLinkAbstraction() {
        removeAbstraction("AirLink");
        addAirLinkAbstraction();
    }

    /* access modifiers changed from: protected */
    public void addAirLinkAbstraction() {
        DJIAirLinkAbstraction airLink = null;
        try {
            switch (DJIComponentManager.getInstance().getPlatformType()) {
                case P3c:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkSkyAbstraction(), null);
                    break;
                case P3x:
                case P3s:
                case P4:
                case M100:
                case Inspire:
                    airLink = new DJIAirLinkAbstraction(null, new Lightbridge1Abstraction());
                    break;
                case P3w:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkSkyAbstraction(), null);
                    break;
                case P4P:
                case Inspire2:
                    airLink = new DJIAirLinkAbstraction(null, new Lightbridge2Phantom4PAbstraction());
                    break;
                case P4A:
                    airLink = new DJIAirLinkAbstraction(null, new Lightbridge2Phantom4AAbstraction());
                    break;
                case P4PSDR:
                case WM240:
                case WM245:
                    airLink = new DJIAirLinkAbstraction(null, new DJIOcuSyncDualBandLinkAbstraction());
                    break;
                case P4RTK:
                    airLink = new DJIAirLinkAbstraction(null, new DJIOcuSyncLinkAbstraction());
                    break;
                case M600:
                case M600Pro:
                case A3:
                case N3:
                case A2:
                    airLink = new DJIAirLinkAbstraction(null, new Lightbridge2Abstraction());
                    break;
                case M200:
                case M210:
                case M210RTK:
                    airLink = new DJIAirLinkAbstraction(null, new Lightbridge2M210Abstraction());
                    break;
                case PM420:
                case PM420PRO:
                case PM420PRO_RTK:
                    airLink = new DJIAirLinkAbstraction(new DJILteLinkAbstraction(), null, new DJIOcuSyncDualBandLinkPM420Abstraction());
                    break;
                case OSMO:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkGroundAbstraction(), null);
                    break;
                case FoldingDrone:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkFoldingDroneAbstraction(), new DJIOcuSyncLinkAbstraction());
                    break;
                case Spark:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkWm100GroundAbstraction(), null);
                    break;
                case WM230:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkWm100GroundAbstraction(), null);
                    break;
                case WM160:
                    airLink = new DJIAirLinkAbstraction(new DJIWifiLinkWM160Abstraction(), null);
                    break;
            }
            if (airLink != null) {
                addAirLinkAbstraction(airLink);
            }
        } catch (Exception e) {
            DJILog.e(this.TAG, "DJISDKCacheHWAbstractionLayer addAbstraction Exception  : AirLink" + DJILog.exceptionToString(e), new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void addAirLinkAbstraction(DJIAirLinkAbstraction airLink) {
        airLink.init("AirLink", 0, this.storeLayer, this.listener);
        SparseArray<DJISDKCacheHWAbstraction> hws = new SparseArray<>();
        hws.put(0, airLink);
        this.hwAbstractionMap.put("AirLink", hws);
        airLink.syncPushDataFromMidware();
        for (Map<Integer, DJISDKCacheHWAbstraction> map : airLink.getSubComponents().values()) {
            for (DJISDKCacheHWAbstraction subComponent : map.values()) {
                subComponent.syncPushDataFromMidware();
            }
        }
        printAbstractionWithDelay();
    }

    private void initGimbalController() {
        addGimbalAbstraction();
    }

    private void updatePayload() {
        removeAbstraction("Payload");
        addPayloadAbstration();
    }

    /* access modifiers changed from: protected */
    public void addPayloadAbstration() {
        DJIComponentManager.PayloadComponentType type = DJIComponentManager.getInstance().getPayloadComponentType();
        DJIComponentManager.PayloadComponentType secondaryPayloadType = DJIComponentManager.getInstance().getSecondaryPayloadComponentType();
        try {
            if (type != DJIComponentManager.PayloadComponentType.None) {
                addAbstraction(1, "Payload", DJIPayloadAbstraction.class);
            } else if (secondaryPayloadType != DJIComponentManager.PayloadComponentType.None) {
                addAbstractionWithIndex(1, "Payload", DJIPayloadAbstraction.class);
            }
        } catch (Exception e) {
            DJILog.e(this.TAG, "DJISDKCacheHWAbstractionLayer addAbstraction Exception  : Payload" + DJILog.exceptionToString(e), new Object[0]);
        }
    }

    private void initAccessoryAggregation() {
        addAccessoryAggregationAbstraction();
    }

    private void updateAccessoryAggregation() {
        removeAbstraction("AccessoryAggregation");
        addAccessoryAggregationAbstraction();
    }

    /* access modifiers changed from: protected */
    public void addAccessoryAggregationAbstraction() {
        switch (DJIComponentManager.getInstance().getPlatformType()) {
            case PM420:
            case PM420PRO:
            case PM420PRO_RTK:
            case WM245:
                addAbstraction(1, "AccessoryAggregation", AccessoryAbstraction.class);
                return;
            default:
                return;
        }
    }

    private void updateGimbal() {
        removeAbstraction("Gimbal");
        addGimbalAbstraction();
    }

    /* access modifiers changed from: protected */
    public void addGimbalAbstraction() {
        boolean hasCamera;
        DJIComponentManager.GimbalComponentType gimbalType = DJIComponentManager.getInstance().getGimbalComponentType();
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        DJIComponentManager.CameraComponentType cameraType = DJIComponentManager.getInstance().getCameraComponentType();
        if (cameraType == null || cameraType == DJIComponentManager.CameraComponentType.None) {
            hasCamera = false;
        } else {
            hasCamera = true;
        }
        Boolean isAbstractionAdded = false;
        switch (gimbalType) {
            case Ronin:
                isAbstractionAdded = true;
                addAbstraction(1, "Gimbal", DJIGimbalRoninMXAbstraction.class);
                break;
            case X5sGimbal:
            case X4sGimbal:
            case GD600Gimbal:
            case X7Gimbal:
            case X9Gimbal:
                isAbstractionAdded = true;
                addAbstraction(1, "Gimbal", DJIGimbalInspire2Abstraction.class);
                break;
            case FoldingDroneGimbal:
                isAbstractionAdded = true;
                addAbstraction(1, "Gimbal", DJIGimbalFoldingDroneAbstraction.class);
                break;
            case PayloadGimbal:
                isAbstractionAdded = true;
                addAbstraction(1, "Gimbal", DJIGimbalPayLoadAbstraction.class);
                break;
            case OSMO_2:
                isAbstractionAdded = true;
                addAbstraction(1, "Gimbal", DJIGimbalMobileHandheldAbstraction.class);
                break;
            case XtGimbal:
                isAbstractionAdded = true;
                addAbstraction(1, "Gimbal", DJIGimbalXtAbstraction.class);
                break;
        }
        if (!isAbstractionAdded.booleanValue()) {
            switch (type) {
                case P3c:
                case P3x:
                case P3s:
                case P3w:
                    addAbstraction(1, "Gimbal", DJIGimbalPhantom3Abstraction.class);
                    break;
                case P4:
                    addAbstraction(1, "Gimbal", DJIGimbalPhantom4Abstraction.class);
                    break;
                case P4P:
                case P4A:
                case P4RTK:
                    addAbstraction(1, "Gimbal", DJIGimbalPhantom4PAbstraction.class);
                    break;
                case P4PSDR:
                    addAbstraction(1, "Gimbal", DJIGimbalPhantom4PSDRAbstraction.class);
                    break;
                case M100:
                case M600:
                case M600Pro:
                    addAbstraction(1, "Gimbal", DJIGimbalX3Abstraction.class);
                    break;
                case Inspire:
                    if (hasCamera) {
                        addAbstraction(1, "Gimbal", DJIGimbalX3Abstraction.class);
                        break;
                    }
                    break;
                case M200:
                case M210:
                case M210RTK:
                case PM420:
                case PM420PRO:
                case PM420PRO_RTK:
                    if (DJIComponentManager.PayloadComponentType.None == DJIComponentManager.getInstance().getPayloadComponentType()) {
                        if (!DJIComponentManager.CameraComponentType.None.equals(DJIComponentManager.getInstance().getCameraComponentType(0))) {
                            addAbstractionWithIndex(0, "Gimbal", DJIGimbalInspire2Abstraction.class);
                            break;
                        }
                    }
                    break;
                case Inspire2:
                    if (hasCamera) {
                        addAbstraction(1, "Gimbal", DJIGimbalInspire2Abstraction.class);
                        break;
                    }
                    break;
                case OSMO:
                    addAbstraction(1, "Gimbal", DJIGimbalHandheldAbstraction.class);
                    break;
                case OSMOMobile:
                    addAbstraction(1, "Gimbal", DJIGimbalMobileHandheldAbstraction.class);
                    break;
                case FoldingDrone:
                    addAbstraction(1, "Gimbal", DJIGimbalFoldingDroneAbstraction.class);
                    break;
                case Spark:
                    addAbstraction(1, "Gimbal", DJIGimbalSparkAbstraction.class);
                    break;
                case WM230:
                    addAbstraction(1, "Gimbal", DJIGimbalWM230Abstraction.class);
                    break;
                case WM240:
                case WM245:
                    addAbstraction(1, "Gimbal", DJIGimbalWM240Abstraction.class);
                    break;
                case A3:
                case N3:
                case A2:
                case Unknown:
                    switch (cameraType) {
                        case X3:
                        case X5:
                        case X5R:
                        case Z3:
                        case TAU336:
                        case TAU640:
                        case GD600:
                            addAbstraction(1, "Gimbal", DJIGimbalX3Abstraction.class);
                            break;
                    }
                case WM160:
                    addAbstraction(1, "Gimbal", DJIGimbalWM160Abstraction.class);
                    break;
            }
        }
        if (DJIComponentManager.getInstance().isDoubleCameraPlatform(type)) {
            addSecondaryGimbalAbstraction();
        }
    }

    private void addSecondaryGimbalAbstraction() {
        switch (DJIComponentManager.getInstance().getSecondaryGimbalComponentType()) {
            case X5sGimbal:
            case X4sGimbal:
            case GD600Gimbal:
            case X7Gimbal:
            case X9Gimbal:
                addAbstractionWithIndex(1, "Gimbal", DJIGimbalInspire2Abstraction.class);
                return;
            case FoldingDroneGimbal:
            case OSMO_2:
            default:
                if (DJIComponentManager.PayloadComponentType.None == DJIComponentManager.getInstance().getSecondaryPayloadComponentType()) {
                    if (!DJIComponentManager.CameraComponentType.None.equals(DJIComponentManager.getInstance().getCameraComponentType(1))) {
                        addAbstractionWithIndex(1, "Gimbal", DJIGimbalInspire2Abstraction.class);
                        return;
                    }
                    return;
                }
                return;
            case PayloadGimbal:
                addAbstractionWithIndex(1, "Gimbal", DJIGimbalPayLoadAbstraction.class);
                return;
            case XtGimbal:
                addAbstractionWithIndex(1, "Gimbal", DJIGimbalXtAbstraction.class);
                return;
        }
    }

    private void initFlightController() {
        addFlightController();
    }

    private void updateFlightController() {
        removeAbstraction("FlightController");
        addFlightController();
    }

    /* access modifiers changed from: protected */
    public void addFlightController() {
        switch (DJIComponentManager.getInstance().getPlatformType()) {
            case P3c:
            case P3x:
            case P3s:
            case P3w:
                addAbstraction(1, "FlightController", FlightControllerPhantom3Abstraction.class);
                return;
            case P4:
                addAbstraction(1, "FlightController", FlightControllerPhantom4Abstraction.class);
                return;
            case P4P:
            case P4A:
            case P4PSDR:
                addAbstraction(1, "FlightController", FlightControllerPhantom4PAbstraction.class);
                return;
            case P4RTK:
                addAbstraction(1, "FlightController", FlightControllerPhantom4PRTKAbstraction.class);
                return;
            case M100:
                addAbstraction(1, "FlightController", FlightControllerM100Abstraction.class);
                return;
            case Inspire:
                addAbstraction(1, "FlightController", FlightControllerInspire1Abstraction.class);
                return;
            case M600:
            case M600Pro:
                addAbstraction(1, "FlightController", FlightControllerA3Abstraction.class);
                return;
            case M200:
            case M210:
            case M210RTK:
                addAbstraction(1, "FlightController", FlightControllerM200Abstraction.class);
                return;
            case PM420:
            case PM420PRO:
            case PM420PRO_RTK:
                addAbstraction(1, "FlightController", FlightControllerPM420Abstraction.class);
                return;
            case Inspire2:
                addAbstraction(1, "FlightController", FlightControllerInspire2Abstraction.class);
                return;
            case OSMO:
            case OSMOMobile:
            default:
                return;
            case FoldingDrone:
                addAbstraction(1, "FlightController", FlightControllerFoldingDroneAbstraction.class);
                return;
            case Spark:
                addAbstraction(1, "FlightController", DJIWM100FlightControllerAbstraction.class);
                return;
            case WM230:
                addAbstraction(1, "FlightController", FlightControllerWM230Abstraction.class);
                return;
            case WM240:
                addAbstraction(1, "FlightController", FlightControllerWM240Abstraction.class);
                return;
            case WM245:
                addAbstraction(1, "FlightController", FlightControllerWM245Abstraction.class);
                return;
            case A3:
            case N3:
                if (DJIComponentManager.getInstance().getRcComponentType() == DJIComponentManager.RcComponentType.LB2) {
                    addAbstraction(1, "FlightController", FlightControllerA3WithLb2Abstraction.class);
                    return;
                } else {
                    addAbstraction(1, "FlightController", FlightControllerA3Abstraction.class);
                    return;
                }
            case A2:
                addAbstraction(1, "FlightController", FlightControllerA2Abstraction.class);
                return;
            case WM160:
                addAbstraction(1, "FlightController", FlightControllerWM160Abstraction.class);
                return;
        }
    }

    private void updateRemoteController() {
        removeAbstraction("RemoteController");
        addRcAbstraction();
    }

    private void initCamera() {
        addCameraAbstraction();
    }

    private void updateCamera() {
        removeAbstraction("Camera");
        addCameraAbstraction();
    }

    private void initRc() {
        addRcAbstraction();
    }

    /* access modifiers changed from: protected */
    public void addRcAbstraction() {
        switch (DJIComponentManager.getInstance().getRcComponentType()) {
            case Inspire:
                addAbstraction(1, "RemoteController", DJIRCInspire1Abstraction.class);
                return;
            case LB2:
                addAbstraction(1, "RemoteController", DJIRCLB2Abstraction.class);
                return;
            case Cendence:
                addAbstraction(1, "RemoteController", DJIRCProfessionalAbstraction.class);
                return;
            case CendenceSDR:
                addAbstraction(1, "RemoteController", DJIRCC3SDRAbstraction.class);
                return;
            case Inspire2:
                addAbstraction(1, "RemoteController", DJIRCInspire2Abstraction.class);
                return;
            case P3P4:
            case P3w:
                addAbstraction(1, "RemoteController", DJIRCPhantom3Abstraction.class);
                return;
            case P4P:
                addAbstraction(1, "RemoteController", DJIRCPhantom4PAbstraction.class);
                return;
            case P4A:
                addAbstraction(1, "RemoteController", DJIRCPhantom4AAbstraction.class);
                return;
            case P4PSdr:
                addAbstraction(1, "RemoteController", DJIRCPhantom4PSDRAbstraction.class);
                return;
            case P4RTK:
                addAbstraction(1, "RemoteController", DJIRCPhantom4RTKAbstraction.class);
                return;
            case P3c:
                addAbstraction(1, "RemoteController", DJIRCPhantom3SAbstraction.class);
                return;
            case FoldingDrone:
                addAbstraction(1, "RemoteController", DJIRCFoldingDroneAbstraction.class);
                return;
            case Spark:
                addAbstraction(1, "RemoteController", DJIRCSparkAbstraction.class);
                return;
            case None:
                addAbstraction(1, "RemoteController", null);
                return;
            case WM230:
                addAbstraction(1, "RemoteController", DJIRCMavicAirAbstraction.class);
                return;
            case WM240:
                addAbstraction(1, "RemoteController", DJIRCWM240Abstraction.class);
                return;
            case WM245:
                addAbstraction(1, "RemoteController", DJIRCWM245Abstraction.class);
                return;
            case WM160:
                addAbstraction(1, "RemoteController", DJIRCWM160Abstaction.class);
                return;
            case RM500:
                addAbstraction(1, "RemoteController", DJIRCRM500Abstraction.class);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void addCameraAbstraction() {
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        switch (DJIComponentManager.getInstance().getCameraComponentType(0)) {
            case X3:
                if (type != DJIComponentManager.PlatformType.OSMO) {
                    addAbstraction(1, "Camera", DJICameraX3Abstraction.class);
                    break;
                } else {
                    addAbstraction(1, "Camera", DJICameraX3HandheldAbstraction.class);
                    break;
                }
            case X5:
                if (type != DJIComponentManager.PlatformType.OSMO) {
                    addAbstraction(1, "Camera", DJICameraX5Abstraction.class);
                    break;
                } else {
                    addAbstraction(1, "Camera", DJICameraX5HandheldAbstraction.class);
                    break;
                }
            case X5R:
                if (type != DJIComponentManager.PlatformType.OSMO) {
                    addAbstraction(1, "Camera", DJICameraX5RAbstraction.class);
                    break;
                } else {
                    addAbstraction(1, "Camera", DJICameraX5RHandheldAbstraction.class);
                    break;
                }
            case Z3:
                if (type != DJIComponentManager.PlatformType.OSMO) {
                    addAbstraction(1, "Camera", DJICameraZ3Abstraction.class);
                    break;
                } else {
                    addAbstraction(1, "Camera", DJICameraZ3HandheldAbstraction.class);
                    break;
                }
            case TAU336:
                addAbstraction(1, "Camera", DJICameraTau336Abstraction.class);
                break;
            case TAU640:
                addAbstraction(1, "Camera", DJICameraTau640Abstraction.class);
                break;
            case GD600:
                addAbstraction(1, "Camera", DJICameraGD600Abstraction.class);
                break;
            case FoldingDroneX:
                addAbstraction(1, "Camera", DJICameraFoldingDroneXAbstraction.class);
                break;
            case FoldingDroneS:
                addAbstraction(1, "Camera", DJICameraFoldingDroneSAbstraction.class);
                break;
            case Spark:
                addAbstraction(1, "Camera", DJICameraSparkAbstraction.class);
                break;
            case WM230:
                addAbstraction(1, "Camera", DJICameraWM230Abstraction.class);
                break;
            case WM240Hasselblad:
                addAbstraction(1, "Camera", DJICameraWM240HasselbladAbstraction.class);
                break;
            case WM240:
                addAbstraction(1, "Camera", DJICameraWM240ZoomAbstraction.class);
                break;
            case WM245:
                addAbstraction(1, "Camera", DJICameraWM245Abstraction.class);
                break;
            case X4S:
                addAbstraction(1, "Camera", DJICameraX4SAbstraction.class);
                break;
            case X5S:
                addAbstraction(1, "Camera", DJICameraX5SAbstraction.class);
                break;
            case X7:
                addAbstraction(1, "Camera", DJICameraX7Abstraction.class);
                break;
            case PayloadCamera:
                addAbstraction(1, "Camera", DJICameraPayloadAbstraction.class);
                break;
            case WM160:
                addAbstraction(1, "Camera", DJICameraWM160Abstraction.class);
                break;
            case XT2:
                addAbstractionWithIndex(0, "Camera", DJICameraXT2Abstraction.class);
                addAbstractionWithIndex(2, "Camera", DJICameraTauXT2Abstraction.class);
                break;
            case WM245DualLightCamera:
                addAbstractionWithIndex(0, "Camera", DJICameraWM245DualLightVLAbstraction.class);
                addAbstractionWithIndex(2, "Camera", DJICameraWM245DualLightIRAbstraction.class);
                break;
            default:
                switch (type) {
                    case P3c:
                        addAbstraction(1, "Camera", DJICameraPhantom3SAbstraction.class);
                        break;
                    case P3x:
                        addAbstraction(1, "Camera", DJICameraPhantom3PAbstraction.class);
                        break;
                    case P3s:
                        addAbstraction(1, "Camera", DJICameraPhantom3AAbstraction.class);
                        break;
                    case P3w:
                        addAbstraction(1, "Camera", DJICameraPhantom3WAbstraction.class);
                        break;
                    case P4:
                        addAbstraction(1, "Camera", DJICameraPhantom4Abstraction.class);
                        break;
                    case P4P:
                        addAbstraction(1, "Camera", DJICameraPhantom4PAbstraction.class);
                        break;
                    case P4A:
                        addAbstraction(1, "Camera", DJICameraPhantom4AAbstraction.class);
                        break;
                    case P4PSDR:
                        addAbstraction(1, "Camera", DJICameraPhantom4PSDRAbstraction.class);
                        break;
                    case P4RTK:
                        addAbstraction(1, "Camera", DJICameraPhantom4RTKAbstraction.class);
                        break;
                }
        }
        if (DJIComponentManager.getInstance().isDoubleCameraPlatform(type)) {
            addSecondaryCameraAbstraction();
            DoubleCameraSupportUtil.SupportDoubleCamera = true;
            DoubleCameraSupportUtil.reset();
            return;
        }
        DoubleCameraSupportUtil.SupportDoubleCamera = false;
    }

    /* access modifiers changed from: protected */
    public void addSecondaryCameraAbstraction() {
        char c = 6;
        DJIComponentManager.CameraComponentType cameraType = DJIComponentManager.getInstance().getCameraComponentType(1);
        switch (cameraType) {
            case X3:
                addAbstractionWithIndex(1, "Camera", DJICameraX3Abstraction.class);
                break;
            case X5:
                addAbstractionWithIndex(1, "Camera", DJICameraX5Abstraction.class);
                break;
            case X5R:
                addAbstractionWithIndex(1, "Camera", DJICameraX5RAbstraction.class);
                break;
            case Z3:
                addAbstractionWithIndex(1, "Camera", DJICameraZ3Abstraction.class);
                break;
            case TAU336:
                addAbstractionWithIndex(1, "Camera", DJICameraTau336Abstraction.class);
                break;
            case TAU640:
                addAbstractionWithIndex(1, "Camera", DJICameraTau640Abstraction.class);
                break;
            case GD600:
                addAbstractionWithIndex(1, "Camera", DJICameraGD600Abstraction.class);
                break;
            case FoldingDroneX:
                addAbstractionWithIndex(1, "Camera", DJICameraFoldingDroneXAbstraction.class);
                break;
            case FoldingDroneS:
                addAbstractionWithIndex(1, "Camera", DJICameraFoldingDroneSAbstraction.class);
                break;
            case X4S:
                addAbstractionWithIndex(1, "Camera", DJICameraX4SAbstraction.class);
                break;
            case X5S:
                addAbstractionWithIndex(1, "Camera", DJICameraX5SAbstraction.class);
                break;
            case X7:
                addAbstraction(1, "Camera", DJICameraX7Abstraction.class);
                break;
            case PayloadCamera:
                addAbstractionWithIndex(1, "Camera", DJICameraPayloadAbstraction.class);
                break;
        }
        DoubleCameraSupportUtil.setSecondaryCameraType(cameraType);
        DJIComponentManager.CameraComponentType mainCameraType = DJIComponentManager.getInstance().getCameraComponentType(0);
        DoubleCameraSupportUtil.setMainCameraType(mainCameraType);
        final DJISDKCacheKey key = KeyHelper.getLightbridgeLinkKey(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LEFT_CAMERA);
        if (!cameraType.equals(DJIComponentManager.CameraComponentType.None) && !mainCameraType.equals(DJIComponentManager.CameraComponentType.None)) {
            if (MidWare.context.get() != null) {
                final int userSetPercent = DjiSharedPreferencesManager.getInt(MidWare.context.get(), DoubleCameraSupportUtil.USER_SET_MAIN_CAMERA_BANDWIDTH_PERCENT, 5);
                new DataSingleSetMainCameraBandwidthPercent().setPercent(userSetPercent).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        if (DJISDKCacheHWAbstractionLayer.this.listener != null) {
                            DJISDKCacheHWAbstractionLayer.this.listener.onNewValue(Float.valueOf(((float) userSetPercent) / 10.0f), key);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                    }
                });
            } else {
                new DataSingleSetMainCameraBandwidthPercent().setPercent(5).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass4 */

                    public void onSuccess(Object model) {
                        if (DJISDKCacheHWAbstractionLayer.this.listener != null) {
                            DJISDKCacheHWAbstractionLayer.this.listener.onNewValue(Float.valueOf(0.5f), key);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                    }
                });
            }
            ((DataDm368SetActiveTrackCamera) new DataDm368SetActiveTrackCamera().setReceiverId(CommonUtil.isPM420Platform() ? true : true, DataDm368SetActiveTrackCamera.class)).setCameraType(DataCameraGetPushStateInfo.getInstance().getCameraType(0)).start((DJIDataCallBack) null);
            DoubleCameraSupportUtil.setTrackMissionCameraID(0);
        } else if (!cameraType.equals(DJIComponentManager.CameraComponentType.None)) {
            new DataSingleSetMainCameraBandwidthPercent().setPercent(0).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass5 */

                public void onSuccess(Object model) {
                    if (DJISDKCacheHWAbstractionLayer.this.listener != null) {
                        DJISDKCacheHWAbstractionLayer.this.listener.onNewValue(Float.valueOf(0.0f), key);
                    }
                }

                public void onFailure(Ccode ccode) {
                }
            });
            DataDm368SetActiveTrackCamera dataDm368SetActiveTrackCamera = new DataDm368SetActiveTrackCamera();
            if (!CommonUtil.isPM420Platform()) {
                c = 1;
            }
            ((DataDm368SetActiveTrackCamera) dataDm368SetActiveTrackCamera.setReceiverId(c, DataDm368SetActiveTrackCamera.class)).setCameraType(DataCameraGetPushStateInfo.getInstance().getCameraType(2)).start((DJIDataCallBack) null);
            DoubleCameraSupportUtil.setTrackMissionCameraID(2);
        } else if (!mainCameraType.equals(DJIComponentManager.CameraComponentType.None)) {
            new DataSingleSetMainCameraBandwidthPercent().setPercent(10).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer.AnonymousClass6 */

                public void onSuccess(Object model) {
                    if (DJISDKCacheHWAbstractionLayer.this.listener != null) {
                        DJISDKCacheHWAbstractionLayer.this.listener.onNewValue(Float.valueOf(1.0f), key);
                    }
                }

                public void onFailure(Ccode ccode) {
                }
            });
            DataDm368SetActiveTrackCamera dataDm368SetActiveTrackCamera2 = new DataDm368SetActiveTrackCamera();
            if (!CommonUtil.isPM420Platform()) {
                c = 1;
            }
            ((DataDm368SetActiveTrackCamera) dataDm368SetActiveTrackCamera2.setReceiverId(c, DataDm368SetActiveTrackCamera.class)).setCameraType(DataCameraGetPushStateInfo.getInstance().getCameraType(0)).start((DJIDataCallBack) null);
            DoubleCameraSupportUtil.setTrackMissionCameraID(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.PlatformType type) {
        DJILog.d(this.TAG, "onEventBackgroundThread PlatformType " + type, new Object[0]);
        updateFlightController();
        updateAccessoryAggregation();
        updateBattery();
        updateHandheldController();
        updateAirLinkAbstraction();
        if (type == DJIComponentManager.PlatformType.OSMOMobile) {
            updateGimbal();
        }
        if (Util.needUpdateCameraByPlatformChanged(type)) {
            updateCamera();
        }
        notifyComponentChanged();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.PayloadComponentType type) {
        DJILog.d(this.TAG, "onEventBackgroundThread getPayloadComponentType:" + type.name(), new Object[0]);
        updatePayload();
        notifyComponentChanged();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.GimbalComponentType type) {
        DJILog.d(this.TAG, "onEventBackgroundThread getGimbalComponentType:" + type.name(), new Object[0]);
        updateGimbal();
        notifyComponentChanged();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.RcComponentType type) {
        DJILog.d(this.TAG, "onEventBackgroundThread RcComponentType:" + type, new Object[0]);
        updateRemoteController();
        notifyComponentChanged();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.CameraComponentType type) {
        DJILog.d(this.TAG, "onEventBackgroundThread CameraComponentType:" + type, new Object[0]);
        updateCamera();
        updateGimbal();
        notifyComponentChanged();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.SmartBatteryComponentType type) {
        DJILog.d(this.TAG, "onEventBackgroundThread SmartBatteryComponentType:" + type, new Object[0]);
        updateBattery();
        notifyComponentChanged();
    }

    public int getInterval(DJISDKCacheKey keyPath) {
        DJISDKCacheHWAbstraction hw = getComponent(keyPath);
        if (hw != null) {
            return hw.getAutoGetInterval(keyPath);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void notifyComponentChanged() {
        BackgroundLooper.remove(this.updateComponentRunnable);
        BackgroundLooper.postDelayed(this.updateComponentRunnable, 500);
    }

    public void updateStore(Object newValue, DJISDKCacheKey keyPath, DJISDKCacheParamValue.Status paramValueStatus, DJISDKCacheParamValue.Source paramValueSource, DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback callback) {
        updateStoredValueForKeyPath(newValue, keyPath, paramValueStatus, paramValueSource, callback);
    }

    public DJISDKCacheAutoGetterVerifier getAutoGetterVerifier() {
        return this.autoGetterVerifier;
    }

    public boolean isKeySupported(DJISDKCacheKey keyPath) {
        DJISDKCacheHWAbstraction hw = getComponent(keyPath);
        if (hw != null) {
            return hw.isKeySupported(keyPath);
        }
        return false;
    }
}
