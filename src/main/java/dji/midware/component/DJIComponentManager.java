package dji.midware.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.GlobalConfig;
import dji.midware.ble.BLE;
import dji.midware.component.battery.DJIBatteryDetectHelper;
import dji.midware.component.rc.DJIRcDetectHelper;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataGetPushPayloadStatus;
import dji.midware.data.model.P3.DataGimbalGetPushType;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushPowerStatus;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIComponentManager {
    private static final int MSG_RELOAD_CAMERAS = 0;
    private static final String TAG = "DJIComponentManager";
    private static DJIComponentManager instance = null;
    private static final boolean showLog = false;
    private final String GIMBAL_KEY = "DJIComponentManager_gimbal";
    private final String PLATFORM_KEY = "DJIComponentManager_platform";
    private final String PRIMARY_CAMERA_KEY = "DJIComponentManager_camera";
    private final String RC_KEY = "DJIComponentManager_rc";
    private final String SECONDARY_CAMERA_KEY = "DJIComponentManager_camera_second";
    private int count = 0;
    private PayloadComponentType lastPayloadComponentType = PayloadComponentType.None;
    private PayloadComponentType lastSecondaryPayloadComponentType = PayloadComponentType.None;
    private CameraComponentType mCameraComponentType = CameraComponentType.None;
    public String mComponentLogStr = "";
    private Context mCtx;
    private GimbalComponentType mGimbalComponentType = GimbalComponentType.None;
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    private CameraComponentType mLastCameraComponentType = CameraComponentType.None;
    private GimbalComponentType mLastGimbalComponentType = GimbalComponentType.None;
    private PlatformType mLastPlatformType = PlatformType.None;
    private RcComponentType mLastRcComponentType = RcComponentType.None;
    private CameraComponentType mLastSecondaryCameraComponentType = CameraComponentType.None;
    private GimbalComponentType mLastSecondaryGimbalComponentType = GimbalComponentType.None;
    private SmartBatteryComponentType mLastSmartBatteryComponentType = SmartBatteryComponentType.None;
    private PlatformType mPlaformType = PlatformType.None;
    private RcComponentType mRcComponentType = RcComponentType.None;
    private CameraComponentType mSecondaryCameraComponentType = CameraComponentType.None;
    private GimbalComponentType mSecondaryGimbalComponentType = GimbalComponentType.None;
    private SmartBatteryComponentType mSmartBatteryComponentType = SmartBatteryComponentType.None;
    private PayloadComponentType payloadComponentType = PayloadComponentType.None;
    private PayloadComponentType secondaryPayloadComponentType = PayloadComponentType.None;
    private UpdateTask updateTask = new UpdateTask();

    public enum RcComponentType {
        None,
        P3P4,
        P3c,
        P3w,
        Inspire,
        LB2,
        P4P,
        P4A,
        P4PSdr,
        P4RTK,
        FoldingDrone,
        Spark,
        WM230,
        WM240,
        WM245,
        Inspire2,
        Cendence,
        CendenceSDR,
        WM160,
        RM500,
        Unknow
    }

    public enum SmartBatteryComponentType {
        None,
        MgBattery,
        Unknow
    }

    public enum CameraComponentType {
        None,
        X4S,
        X5S,
        X7,
        P3x,
        P3s,
        P3c,
        P3w,
        P4,
        X3,
        X5,
        X5R,
        TAU336,
        TAU640,
        FoldingDroneX,
        FoldingDroneS,
        Z3,
        Spark,
        WM230,
        WM240,
        WM240Hasselblad,
        WM245,
        P4P,
        P4S,
        GD600,
        XT2,
        WM245DualLightCamera,
        PayloadCamera,
        WM160,
        Unknow
    }

    public enum GimbalComponentType {
        None,
        Ronin,
        X5sGimbal,
        X4sGimbal,
        XtGimbal,
        GD600Gimbal,
        FoldingDroneGimbal,
        X7Gimbal,
        X9Gimbal,
        PayloadGimbal,
        OSMO_2,
        WM160,
        Unknow
    }

    public enum PayloadComponentType {
        None,
        Unknow
    }

    public enum PlatformType {
        None,
        P3x,
        P3s,
        P3c,
        P3w,
        P3se,
        Inspire,
        M100,
        OSMO,
        OSMOMobile,
        P4,
        M600,
        Inspire2,
        M200,
        M210,
        M210RTK,
        PM420,
        PM420PRO,
        PM420PRO_RTK,
        FoldingDrone,
        Spark,
        WM230,
        WM240,
        WM245,
        P4P,
        M600Pro,
        P4A,
        P4PSDR,
        P4RTK,
        A3,
        N3,
        A2,
        WM160,
        Unknown
    }

    public static synchronized DJIComponentManager getInstance() {
        DJIComponentManager dJIComponentManager;
        synchronized (DJIComponentManager.class) {
            if (instance == null) {
                instance = new DJIComponentManager();
            }
            dJIComponentManager = instance;
        }
        return dJIComponentManager;
    }

    private DJIComponentManager() {
    }

    public void init(Context ctx) {
        DJIRcDetectHelper.getInstance().init();
        DJIBatteryDetectHelper.getInstance().init();
        this.mCtx = ctx;
        this.mLastPlatformType = getLocalPlatformType();
        this.mLastRcComponentType = getLocalRcComponentType();
        this.mLastCameraComponentType = getLocalCameraComponentType();
        this.mHandler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.midware.component.DJIComponentManager.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                switch (msg.arg1) {
                    case 0:
                        DJIComponentManager.this.reloadCameraAndGimbal();
                        return false;
                    default:
                        return false;
                }
            }
        });
        DJIEventBusUtil.register(this);
        updateValueDelay();
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        this.mHandler.removeCallbacksAndMessages(null);
        DJIBatteryDetectHelper.getInstance().destroy();
        DJIRcDetectHelper.getInstance().destroy();
    }

    private PlatformType getLocalPlatformType() {
        int index = DjiSharedPreferencesManager.getInt(this.mCtx, "DJIComponentManager_platform", 0);
        PlatformType[] type = PlatformType.values();
        if (index < type.length) {
            return type[index];
        }
        return PlatformType.None;
    }

    private RcComponentType getLocalRcComponentType() {
        int index = DjiSharedPreferencesManager.getInt(this.mCtx, "DJIComponentManager_rc", 0);
        RcComponentType[] type = RcComponentType.values();
        if (index < type.length) {
            return type[index];
        }
        return RcComponentType.None;
    }

    private CameraComponentType getLocalCameraComponentType() {
        int index = DjiSharedPreferencesManager.getInt(this.mCtx, "DJIComponentManager_camera", 0);
        CameraComponentType[] type = CameraComponentType.values();
        if (index < type.length) {
            return type[index];
        }
        return CameraComponentType.None;
    }

    private CameraComponentType getLocalSecondartCameraComponentType() {
        int index = DjiSharedPreferencesManager.getInt(this.mCtx, "DJIComponentManager_camera_second", 0);
        CameraComponentType[] type = CameraComponentType.values();
        if (index < type.length) {
            return type[index];
        }
        return CameraComponentType.None;
    }

    private GimbalComponentType getLocalGimbalComponentType() {
        int index = DjiSharedPreferencesManager.getInt(this.mCtx, "DJIComponentManager_gimbal", 0);
        GimbalComponentType[] type = GimbalComponentType.values();
        if (index < type.length) {
            return type[index];
        }
        return GimbalComponentType.None;
    }

    private void saveLocalPlatformType(PlatformType type) {
        if (type != null) {
            DjiSharedPreferencesManager.putInt(this.mCtx, "DJIComponentManager_platform", type.ordinal());
        }
    }

    private void saveLocalRcComponentType(RcComponentType type) {
        if (type != null) {
            DjiSharedPreferencesManager.putInt(this.mCtx, "DJIComponentManager_rc", type.ordinal());
        }
    }

    private void saveLocalCameraComponentType(CameraComponentType type) {
        if (type != null) {
            DjiSharedPreferencesManager.putInt(this.mCtx, "DJIComponentManager_camera", type.ordinal());
        }
    }

    private void saveLocalSecondartCameraComponentType(CameraComponentType type) {
        if (type != null) {
            DjiSharedPreferencesManager.putInt(this.mCtx, "DJIComponentManager_camera_second", type.ordinal());
        }
    }

    private void saveLocalGimbalComponentType(GimbalComponentType type) {
        if (type != null) {
            DjiSharedPreferencesManager.putInt(this.mCtx, "DJIComponentManager_gimbal", type.ordinal());
        }
    }

    public PlatformType getPlatformType() {
        return this.mPlaformType;
    }

    public void setPlatformType(PlatformType type) {
        this.mPlaformType = type;
    }

    public PlatformType getLastPlatformType() {
        return this.mLastPlatformType;
    }

    public RcComponentType getRcComponentType() {
        return this.mRcComponentType;
    }

    public RcComponentType getLastRcComponentType() {
        return this.mLastRcComponentType;
    }

    public CameraComponentType getCameraComponentType() {
        return getCameraComponentType(0);
    }

    public CameraComponentType getCameraComponentType(int index) {
        if (index == 0) {
            return this.mCameraComponentType;
        }
        if (index == 1) {
            return this.mSecondaryCameraComponentType;
        }
        return this.mCameraComponentType;
    }

    public PayloadComponentType getPayloadComponentType(int index) {
        if (index == 0) {
            return this.payloadComponentType;
        }
        if (index == 1) {
            return this.secondaryPayloadComponentType;
        }
        return this.payloadComponentType;
    }

    public GimbalComponentType getGimbalComponentType(int index) {
        if (index == 0) {
            return this.mGimbalComponentType;
        }
        if (index == 1) {
            return this.mSecondaryGimbalComponentType;
        }
        return this.mGimbalComponentType;
    }

    public CameraComponentType getLastCameraComponentType() {
        return this.mLastCameraComponentType;
    }

    public GimbalComponentType getGimbalComponentType() {
        return this.mGimbalComponentType;
    }

    public GimbalComponentType getSecondaryGimbalComponentType() {
        return this.mSecondaryGimbalComponentType;
    }

    public PayloadComponentType getPayloadComponentType() {
        return this.payloadComponentType;
    }

    public PayloadComponentType getSecondaryPayloadComponentType() {
        return this.secondaryPayloadComponentType;
    }

    public GimbalComponentType getLastGimbalComponentType() {
        return this.mLastGimbalComponentType;
    }

    public boolean isAircraftConnected() {
        return this.mPlaformType != PlatformType.None;
    }

    public boolean isRcConnected() {
        return this.mRcComponentType != RcComponentType.None;
    }

    public SmartBatteryComponentType getSmartBatteryComponentType() {
        return this.mSmartBatteryComponentType;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.i(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.i(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.i(java.lang.String, java.lang.String, boolean, boolean):void */
    public void reloadCameraAndGimbal() {
        boolean ret3 = updateCameraComponentType();
        boolean ret5 = updateSecondaryCameraComponentType();
        if (ret3 || ret5) {
            DJILog.i(TAG, "addCameraAbstraction Reload Main CameraType : " + this.mCameraComponentType, true, true);
            DJILog.i(TAG, "addCameraAbstraction Reload Secondary CameraType : " + this.mSecondaryCameraComponentType, true, true);
            EventBus.getDefault().post(CameraComponentType.Unknow);
        }
    }

    /* access modifiers changed from: private */
    public void updateValue() {
        log(TAG, "updateValue", true);
        boolean isPlatformUpdated = updatePlatformType();
        boolean isRCComponentUpdated = updateRcComponentType();
        boolean isCameraComponentUpdated = updateCameraComponentType();
        boolean isGimbalComponentUpdated = updateGimbalComponentType();
        boolean isSecondaryCameraComponentUpdated = updateSecondaryCameraComponentType();
        boolean isPayloadComponentUpdated = updatePayloadComponentType();
        boolean isSmartBatteryComponentUpdated = updateSmartBatteryComponentType();
        boolean isSecondaryPayloadComponentUpdated = updateSecondaryPayloadComponentType();
        boolean isSecondaryGimbalComponentUpdated = updateSecondaryGimbalComponentType();
        if (isPlatformUpdated) {
            EventBus.getDefault().post(this.mPlaformType);
        }
        if (isRCComponentUpdated) {
            EventBus.getDefault().post(this.mRcComponentType);
        }
        if (!isDoubleCameraPlatform(this.mPlaformType)) {
            if (isCameraComponentUpdated) {
                EventBus.getDefault().post(this.mCameraComponentType);
            }
            if (isPayloadComponentUpdated) {
                EventBus.getDefault().post(this.payloadComponentType);
            }
            if (isGimbalComponentUpdated) {
                EventBus.getDefault().post(this.mGimbalComponentType);
            }
        } else {
            if (isCameraComponentUpdated || isSecondaryCameraComponentUpdated) {
                EventBus.getDefault().post(this.mCameraComponentType);
            }
            if (isPayloadComponentUpdated || isSecondaryPayloadComponentUpdated) {
                EventBus.getDefault().post(this.payloadComponentType);
            }
            if (isGimbalComponentUpdated || isSecondaryGimbalComponentUpdated) {
                EventBus.getDefault().post(this.mGimbalComponentType);
            }
        }
        if (isSmartBatteryComponentUpdated) {
            EventBus.getDefault().post(this.mSmartBatteryComponentType);
        }
        if (isPlatformUpdated || isRCComponentUpdated || isCameraComponentUpdated || isGimbalComponentUpdated || isSecondaryCameraComponentUpdated || isPayloadComponentUpdated || isSmartBatteryComponentUpdated || isSecondaryPayloadComponentUpdated || isSecondaryGimbalComponentUpdated) {
            StringBuilder builder = new StringBuilder("");
            String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            builder.append("\r\n ========================");
            builder.append("\r\n" + strDate);
            builder.append("\r\n PlaformType : " + this.mPlaformType);
            builder.append("\r\n RcType : " + this.mRcComponentType);
            builder.append("\r\n CameraType : " + this.mCameraComponentType);
            builder.append("\r\n PayloadType : " + this.payloadComponentType);
            builder.append("\r\n GimbalType : " + this.mGimbalComponentType);
            if (isDoubleCameraPlatform(this.mPlaformType)) {
                builder.append("\r\n Secondary CameraType : " + this.mSecondaryCameraComponentType);
                builder.append("\r\n Secondary PayloadType : " + this.secondaryPayloadComponentType);
                builder.append("\r\n Secondary GimbalType : " + this.mSecondaryGimbalComponentType);
            }
            builder.append("\r\n ========================");
            builder.append("\r\n PlaformType : " + this.mPlaformType);
            builder.append("\r\n RcType : " + this.mRcComponentType);
            builder.append("\r\n CameraType : " + this.mCameraComponentType);
            if (PlatformType.M210 == this.mPlaformType || PlatformType.M210RTK == this.mPlaformType) {
                builder.append("\r\n Secondary CameraType : " + this.mSecondaryCameraComponentType);
            }
            builder.append("\r\n GimbalType:" + this.mGimbalComponentType);
            builder.append("\r\n LastPlatformType : " + this.mLastPlatformType);
            builder.append("\r\n LastRcComponentType : " + this.mLastRcComponentType);
            builder.append("\r\n LastCameraComponentType : " + this.mLastCameraComponentType);
            builder.append("\r\nLastPayloadType : " + this.lastPayloadComponentType);
            builder.append("\r\n LastGimbalComponentType : " + this.mLastGimbalComponentType);
            builder.append("\r\n count : " + this.count);
            builder.append("\r\n ========================");
            builder.append("\r\n ");
            this.mComponentLogStr = builder.toString();
            DJILog.save(TAG, this.mComponentLogStr);
            EventBus.getDefault().post(this);
            this.count++;
        }
    }

    private boolean updatePlatformType() {
        PlatformType type = PlatformType.None;
        if (isPlatformP3x() && checkPlatformLogic(type)) {
            type = PlatformType.P3x;
        }
        if (isPlatformP3s() && checkPlatformLogic(type)) {
            type = PlatformType.P3s;
        }
        if (isPlatformP3c() && checkPlatformLogic(type)) {
            type = PlatformType.P3c;
        }
        if (isPlatformP3w() && checkPlatformLogic(type)) {
            type = PlatformType.P3w;
        }
        if (isPlatformP3se() && checkPlatformLogic(type)) {
            type = PlatformType.P3se;
        }
        if (isPlatformInspire() && checkPlatformLogic(type)) {
            type = PlatformType.Inspire;
        }
        if (isPlatformM100() && checkPlatformLogic(type)) {
            type = PlatformType.M100;
        }
        if (isPlatformOSMO() && checkPlatformLogic(type)) {
            type = PlatformType.OSMO;
        }
        if (isPlatformOSMOMobile() && checkPlatformLogic(type)) {
            type = PlatformType.OSMOMobile;
        }
        if (isPlatformP4() && checkPlatformLogic(type)) {
            type = PlatformType.P4;
        }
        if (isPlatformM600() && checkPlatformLogic(type)) {
            type = PlatformType.M600;
        }
        if (isPlatformM600Pro() && checkPlatformLogic(type)) {
            type = PlatformType.M600Pro;
        }
        if (isPlatformInspire2() && checkPlatformLogic(type)) {
            type = PlatformType.Inspire2;
        }
        if (isPlatformM200() && checkPlatformLogic(type)) {
            type = PlatformType.M200;
        }
        if (isPlatformM210() && checkPlatformLogic(type)) {
            type = PlatformType.M210;
        }
        if (isPlatformM210RTK() && checkPlatformLogic(type)) {
            type = PlatformType.M210RTK;
        }
        if (isPlatformPM420() && checkPlatformLogic(type)) {
            type = PlatformType.PM420;
        }
        if (isPlatformPM420PRO() && checkPlatformLogic(type)) {
            type = PlatformType.PM420PRO;
        }
        if (isPlatformPM420PRORTK() && checkPlatformLogic(type)) {
            type = PlatformType.PM420PRO_RTK;
        }
        if (isPlatformFoldingDrone() && checkPlatformLogic(type)) {
            type = PlatformType.FoldingDrone;
        }
        if (isPlatformP4P() && checkPlatformLogic(type)) {
            type = PlatformType.P4P;
        }
        if (isPlatformSpark() && checkPlatformLogic(type)) {
            type = PlatformType.Spark;
        }
        if (isPlatformWM230() && checkPlatformLogic(type)) {
            type = PlatformType.WM230;
        }
        if (isPlatFormWM240() && checkPlatformLogic(type)) {
            type = PlatformType.WM240;
        }
        if (isPlatformWM245() && checkPlatformLogic(type)) {
            type = PlatformType.WM245;
        }
        if (isPlatformP4PRTK() && checkPlatformLogic(type)) {
            type = PlatformType.P4RTK;
        }
        if (isPlatformP4A() && checkPlatformLogic(type)) {
            type = PlatformType.P4A;
        }
        if (isPlatformA3() && checkPlatformLogic(type)) {
            type = PlatformType.A3;
        }
        if (isPlatformN3() && checkPlatformLogic(type)) {
            type = PlatformType.N3;
        }
        if (isPlatformA2() && checkPlatformLogic(type)) {
            type = PlatformType.A2;
        }
        if (isPlatformP4PRTK() && checkPlatformLogic(type)) {
            type = PlatformType.P4RTK;
        }
        if (isPlatformP4PSdr() && checkPlatformLogic(type)) {
            type = PlatformType.P4PSDR;
        }
        if (isPlatformP4PRTK() && checkPlatformLogic(type)) {
            type = PlatformType.P4RTK;
        }
        if (isPlatformWM160() && checkPlatformLogic(type)) {
            type = PlatformType.WM160;
        }
        if (type == PlatformType.None && isPlatformUnknow() && checkPlatformLogic(type)) {
            type = PlatformType.Unknown;
        }
        if (this.mPlaformType == type) {
            return false;
        }
        if (this.mPlaformType != PlatformType.None) {
            this.mLastPlatformType = this.mPlaformType;
            saveLocalPlatformType(this.mLastPlatformType);
        }
        this.mPlaformType = type;
        return true;
    }

    private boolean checkPlatformLogic(PlatformType type) {
        if (!GlobalConfig.DEBUG && type != PlatformType.None) {
        }
        return true;
    }

    private boolean updateRcComponentType() {
        RcComponentType type = RcComponentType.None;
        if (isRcP4P() && checkRcLogic(type)) {
            type = RcComponentType.P4P;
        }
        if (isRcP4PSdr() && checkRcLogic(type)) {
            type = RcComponentType.P4PSdr;
        }
        if (isRcP4PRTK() && checkRcLogic(type)) {
            type = RcComponentType.P4RTK;
        }
        if (isRcPotato() && checkRcLogic(type)) {
            type = RcComponentType.P4A;
        }
        if (isRcP3P4() && checkRcLogic(type)) {
            type = RcComponentType.P3P4;
        }
        if (isRcP3c() && checkRcLogic(type)) {
            type = RcComponentType.P3c;
        }
        if (isRcP3w() && checkRcLogic(type)) {
            type = RcComponentType.P3w;
        }
        if (isRcInspire2() && checkRcLogic(type)) {
            type = RcComponentType.Inspire2;
        }
        if (isRcCendence() && checkRcLogic(type)) {
            type = RcComponentType.Cendence;
        }
        if (isRcCendenceSDR() && checkRcLogic(type)) {
            type = RcComponentType.CendenceSDR;
        }
        if (isRcInspire() && checkRcLogic(type)) {
            type = RcComponentType.Inspire;
        }
        if (isRcLB2() && checkRcLogic(type)) {
            type = RcComponentType.LB2;
        }
        if (isRcFoldingDrone() && checkRcLogic(type)) {
            type = RcComponentType.FoldingDrone;
        }
        if (isRcSpark() && checkRcLogic(type)) {
            type = RcComponentType.Spark;
        }
        if (isRcWM230() && checkRcLogic(type)) {
            type = RcComponentType.WM230;
        }
        if (isRcWM240() && checkRcLogic(type)) {
            type = RcComponentType.WM240;
        }
        if (isRcWM245() && checkRcLogic(type)) {
            type = RcComponentType.WM245;
        }
        if (isRcWM160() && checkRcLogic(type)) {
            type = RcComponentType.WM160;
        }
        if (isRcRM500() && checkRcLogic(type)) {
            type = RcComponentType.RM500;
        }
        if (type == RcComponentType.None && isRcPlatformUnknow() && checkRcLogic(type)) {
            type = RcComponentType.Unknow;
        }
        DJILog.logWriteI(TAG, "============mRcComponentType: " + type, new Object[0]);
        if (this.mRcComponentType == type) {
            return false;
        }
        if (this.mRcComponentType != RcComponentType.None) {
            this.mLastRcComponentType = this.mRcComponentType;
            saveLocalRcComponentType(this.mLastRcComponentType);
        }
        this.mRcComponentType = type;
        return true;
    }

    private boolean updateSmartBatteryComponentType() {
        SmartBatteryComponentType type = SmartBatteryComponentType.None;
        if (isMGBattery()) {
            type = SmartBatteryComponentType.MgBattery;
        }
        DJILog.d(TAG, "smart battery component type:" + type.name(), new Object[0]);
        if (this.mSmartBatteryComponentType == type) {
            return false;
        }
        if (this.mSmartBatteryComponentType != SmartBatteryComponentType.None) {
            this.mLastSmartBatteryComponentType = this.mSmartBatteryComponentType;
        }
        this.mSmartBatteryComponentType = type;
        return true;
    }

    private boolean checkRcLogic(RcComponentType type) {
        if (!GlobalConfig.DEBUG && type != RcComponentType.None) {
        }
        return true;
    }

    private boolean updateCameraComponentType() {
        DataCameraGetPushStateInfo stateInfo;
        DataCameraGetPushStateInfo.CameraType cameraType;
        CameraComponentType componentType = CameraComponentType.None;
        if (ServiceManager.getInstance().isRemoteOK() && (stateInfo = DataCameraGetPushStateInfo.getInstance()) != null && stateInfo.isGetted() && !stateInfo.isPushLosed(0) && stateInfo.hasPrimaryCameraRecData() && stateInfo.getCameraType(0) != null && CameraComponentType.None == (componentType = transferCameraType((cameraType = stateInfo.getCameraType(0)))) && DataCameraGetPushStateInfo.CameraType.OTHER != cameraType) {
            componentType = CameraComponentType.Unknow;
        }
        if (this.mCameraComponentType == componentType) {
            return false;
        }
        if (this.mCameraComponentType != CameraComponentType.None) {
            this.mLastCameraComponentType = this.mCameraComponentType;
            saveLocalCameraComponentType(this.mLastCameraComponentType);
        }
        this.mCameraComponentType = componentType;
        DJILog.d(TAG, "============mCameraComponentType updateCameraComponentType: " + this.mCameraComponentType, new Object[0]);
        return true;
    }

    private CameraComponentType transferCameraType(DataCameraGetPushStateInfo.CameraType cameraType) {
        CameraComponentType cameraComponentType = CameraComponentType.None;
        switch (cameraType) {
            case DJICameraTypeFC260:
                return CameraComponentType.P3c;
            case DJICameraTypeFC300S:
                return CameraComponentType.P3s;
            case DJICameraTypeFC300X:
                return CameraComponentType.P3x;
            case DJICameraTypeFC300XW:
                return CameraComponentType.P3w;
            case DJICameraTypeFC330X:
                return CameraComponentType.P4;
            case DJICameraTypeFC350:
                return CameraComponentType.X3;
            case DJICameraTypeFC550:
                return CameraComponentType.X5;
            case DJICameraTypeFC550Raw:
                return CameraComponentType.X5R;
            case DJICameraTypeTau336:
                return CameraComponentType.TAU336;
            case DJICameraTypeTau640:
                return CameraComponentType.TAU640;
            case DJICameraTypeFC220:
                return CameraComponentType.FoldingDroneX;
            case DJICameraTypeFC220S:
                return CameraComponentType.FoldingDroneS;
            case DJICameraTypeFC1102:
                return CameraComponentType.Spark;
            case DJICameraTypeFC6310:
                return CameraComponentType.P4P;
            case DJICameraTypeFC6310S:
                return CameraComponentType.P4S;
            case DJICameraTypeCV600:
                return CameraComponentType.Z3;
            case DJICameraTypeFC6510:
                return CameraComponentType.X4S;
            case DJICameraTypeFC6520:
                return CameraComponentType.X5S;
            case DJICameraTypeFC6540:
                return CameraComponentType.X7;
            case DJICameraTypeGD600:
                return CameraComponentType.GD600;
            case DJICameraTypeFC230:
                return CameraComponentType.WM230;
            case DJICameraTypeFC240:
                return CameraComponentType.WM240;
            case DJICameraTypeFC240_1:
                return CameraComponentType.WM240Hasselblad;
            case DJICameraTypeFC245_IMX477:
                return CameraComponentType.WM245;
            case DJICameraTypeFC1705:
                return CameraComponentType.XT2;
            case DJICameraTypeFC2403:
                return CameraComponentType.WM245DualLightCamera;
            case DJIPayloadCamera:
                return CameraComponentType.PayloadCamera;
            case DJICameraTypeFC160:
                return CameraComponentType.WM160;
            default:
                return CameraComponentType.None;
        }
    }

    private boolean updateSecondaryCameraComponentType() {
        DataCameraGetPushStateInfo stateInfo;
        DataCameraGetPushStateInfo.CameraType cameraType;
        CameraComponentType componentType = CameraComponentType.None;
        if (ServiceManager.getInstance().isRemoteOK() && (stateInfo = DataCameraGetPushStateInfo.getInstance()) != null && stateInfo.isGetted() && !stateInfo.isPushLosed(2) && stateInfo.hasSecondaryCameraRecData() && stateInfo.getCameraType(2) != null && CameraComponentType.None == (componentType = transferCameraType((cameraType = stateInfo.getCameraType(2)))) && DataCameraGetPushStateInfo.CameraType.OTHER != cameraType) {
            componentType = CameraComponentType.Unknow;
        }
        if (this.mSecondaryCameraComponentType == componentType) {
            return false;
        }
        if (this.mSecondaryCameraComponentType != CameraComponentType.None) {
            this.mLastSecondaryCameraComponentType = this.mSecondaryCameraComponentType;
            saveLocalSecondartCameraComponentType(this.mLastSecondaryCameraComponentType);
        }
        this.mSecondaryCameraComponentType = componentType;
        return true;
    }

    private boolean updateGimbalComponentType() {
        GimbalComponentType type = GimbalComponentType.None;
        DataGimbalGetPushType pushInfo = DataGimbalGetPushType.getInstance();
        if (pushInfo != null && pushInfo.isGetted() && !pushInfo.isPushLosed(0) && pushInfo.hasPrimaryRecData() && pushInfo.getType(0) != null) {
            type = transferGimbalType(pushInfo.getType(0));
        }
        if (this.mGimbalComponentType == type) {
            return false;
        }
        if (this.mGimbalComponentType != GimbalComponentType.None) {
            this.mLastGimbalComponentType = this.mGimbalComponentType;
            saveLocalGimbalComponentType(this.mLastGimbalComponentType);
        }
        this.mGimbalComponentType = type;
        return true;
    }

    private boolean updateSecondaryGimbalComponentType() {
        GimbalComponentType type = GimbalComponentType.None;
        DataGimbalGetPushType pushInfo = DataGimbalGetPushType.getInstance();
        if (pushInfo != null && pushInfo.isGetted() && !pushInfo.isPushLosed(2) && pushInfo.hasSecondaryRecData() && pushInfo.getType(2) != null) {
            type = transferGimbalType(pushInfo.getType(2));
        }
        if (this.mSecondaryGimbalComponentType == type) {
            return false;
        }
        if (this.mSecondaryGimbalComponentType != GimbalComponentType.None) {
            this.mLastSecondaryGimbalComponentType = this.mSecondaryGimbalComponentType;
        }
        this.mSecondaryGimbalComponentType = type;
        return true;
    }

    private GimbalComponentType transferGimbalType(DataGimbalGetPushType.DJIGimbalType gimbalType) {
        switch (gimbalType) {
            case Ronin:
                return GimbalComponentType.Ronin;
            case WM620_X5S:
                return GimbalComponentType.X5sGimbal;
            case WM620_OneInch:
                return GimbalComponentType.X4sGimbal;
            case GD600:
                return GimbalComponentType.GD600Gimbal;
            case WM220:
                return GimbalComponentType.FoldingDroneGimbal;
            case WM620_X7:
                return GimbalComponentType.X7Gimbal;
            case WM620_FullFrame:
                return GimbalComponentType.X9Gimbal;
            case PALYOAD_GIMBAL:
                return GimbalComponentType.PayloadGimbal;
            case HG301:
                return GimbalComponentType.OSMO_2;
            case XT:
                return GimbalComponentType.XtGimbal;
            default:
                return GimbalComponentType.None;
        }
    }

    private boolean updatePayloadComponentType() {
        PayloadComponentType type = PayloadComponentType.None;
        if (DataGetPushPayloadStatus.getInstance().isGetted() && !DataGetPushPayloadStatus.getInstance().isPushLosed(0) && DataGetPushPayloadStatus.getInstance().hasPrimaryRecData() && DataGetPushPayloadStatus.getInstance().isConnected(0)) {
            type = PayloadComponentType.Unknow;
        }
        if (this.payloadComponentType == type) {
            return false;
        }
        if (this.payloadComponentType != PayloadComponentType.None) {
            this.lastPayloadComponentType = this.payloadComponentType;
        }
        this.payloadComponentType = type;
        return true;
    }

    private boolean updateSecondaryPayloadComponentType() {
        PayloadComponentType type = PayloadComponentType.None;
        if (DataGetPushPayloadStatus.getInstance().isGetted() && !DataGetPushPayloadStatus.getInstance().isPushLosed(2) && DataGetPushPayloadStatus.getInstance().hasSecondaryRecData() && DataGetPushPayloadStatus.getInstance().isConnected(2)) {
            type = PayloadComponentType.Unknow;
        }
        if (this.secondaryPayloadComponentType == type) {
            return false;
        }
        if (this.secondaryPayloadComponentType != PayloadComponentType.None) {
            this.lastSecondaryPayloadComponentType = this.secondaryPayloadComponentType;
        }
        this.secondaryPayloadComponentType = type;
        return true;
    }

    private boolean isPlatformP3x() {
        log(TAG, "=====isPlatformP3x=====", false);
        log(TAG, "isRemoteOK : " + ServiceManager.getInstance().isRemoteOK(), false);
        log(TAG, "isGetted : " + DataCameraGetPushStateInfo.getInstance().isGetted(), false);
        log(TAG, "CameraType : " + DataCameraGetPushStateInfo.getInstance().getCameraType(0), false);
        log(TAG, "OSD is get : " + DataOsdGetPushCommon.getInstance().getDroneType(), false);
        log(TAG, "OSD type : " + DataOsdGetPushCommon.getInstance().isGetted(), false);
        log(TAG, "========================\r\n", false);
        if (ServiceManager.getInstance().isRemoteOK() && DataCameraGetPushStateInfo.getInstance().isGetted() && DataCameraGetPushStateInfo.getInstance().getCameraType(0) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X) {
            return true;
        }
        if (!ProductType.litchiX.equals(DJIRcDetectHelper.getInstance().getProductTypeByOsd())) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP3s() {
        if (ServiceManager.getInstance().isRemoteOK() && DataCameraGetPushStateInfo.getInstance().isGetted() && DataCameraGetPushStateInfo.getInstance().getCameraType(0) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S) {
            return true;
        }
        if (!ProductType.litchiC.equals(DJIRcDetectHelper.getInstance().getProductTypeByOsd())) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP3c() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataCameraGetPushStateInfo.getInstance().isGetted() || DataCameraGetPushStateInfo.getInstance().getCameraType(0) != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP3w() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataCameraGetPushStateInfo.getInstance().isGetted() || DataCameraGetPushStateInfo.getInstance().getCameraType(0) != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP3se() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.P3SE) {
            return false;
        }
        return true;
    }

    private boolean isPlatformInspire() {
        DataCameraGetPushStateInfo.CameraType cameraType;
        if (ServiceManager.getInstance().isRemoteOK() && DataCameraGetPushStateInfo.getInstance().isGetted() && ((DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.ADB || DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.AOA) && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM820 && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM820PRO && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.A3 && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.N3 && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M200 && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210 && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210RTK && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420 && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420PRO && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420PRO_RTK && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.None && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Unknown && ((cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType()) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau336 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640))) {
            log(TAG, "=====isPlatformInspire=====", false);
            return true;
        } else if (!ServiceManager.getInstance().isRemoteOK() || !DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Inspire) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isPlatformM100() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame) {
            return false;
        }
        log(TAG, "=====isPlatformM100=====", false);
        return true;
    }

    private boolean isPlatformOSMO() {
        DataCameraGetPushStateInfo.CameraType cameraType;
        if (ServiceManager.getInstance().isRemoteOK() && DataCameraGetPushStateInfo.getInstance().isGetted() && DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.WIFI && ((cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(0)) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw)) {
            return true;
        }
        DataOsdGetPushPowerStatus powerStatus = DataOsdGetPushPowerStatus.getInstance();
        if (!powerStatus.isGetted() || powerStatus.getPowerStatus() != 1) {
            return false;
        }
        return true;
    }

    private boolean isPlatformOSMOMobile() {
        log(TAG, "=====isPlatformOSMOMobile=====", false);
        log(TAG, "isRemoteOK: " + ServiceManager.getInstance().isRemoteOK() + " LinkType" + DJILinkDaemonService.getInstance().getLinkType(), false);
        if (!ServiceManager.getInstance().isRemoteOK() || DJILinkDaemonService.getInstance().getLinkType() != DJILinkType.BLE) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP4() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataCameraGetPushStateInfo.getInstance().isGetted() || DataCameraGetPushStateInfo.getInstance().getCameraType(0) != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP4P() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataCameraGetPushStateInfo.getInstance().isGetted() || DataCameraGetPushStateInfo.getInstance().getCameraType() != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Pomato) {
            return false;
        }
        return true;
    }

    private boolean isPlatformP4A() {
        return ServiceManager.getInstance().isRemoteOK() && DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.Potato;
    }

    private boolean isPlatformP4PSdr() {
        return ServiceManager.getInstance().isRemoteOK() && DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PomatoSdr;
    }

    private boolean isPlatformP4PRTK() {
        return ServiceManager.getInstance().isRemoteOK() && DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PomatoRTK;
    }

    private boolean isPlatformM600() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed()) {
            return false;
        }
        if (DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PM820 || DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PM820PRO) {
            return true;
        }
        return false;
    }

    private boolean isPlatformM600Pro() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM820PRO) {
            return false;
        }
        return true;
    }

    private boolean isPlatformFoldingDrone() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataOsdGetPushCommon.getInstance().isGetted()) {
            return false;
        }
        return DataOsdGetPushCommon.getInstance().getDroneType().equals(DataOsdGetPushCommon.DroneType.wm220);
    }

    public boolean isPlatformM200Series(PlatformType platformType) {
        return platformType == PlatformType.M200 || platformType == PlatformType.M210 || platformType == PlatformType.M210RTK || platformType == PlatformType.PM420 || platformType == PlatformType.PM420PRO || platformType == PlatformType.PM420PRO_RTK;
    }

    public boolean isDoubleCameraPlatform(PlatformType platformType) {
        return platformType == PlatformType.M210RTK || platformType == PlatformType.M210 || platformType == PlatformType.PM420PRO_RTK || platformType == PlatformType.PM420PRO;
    }

    private boolean isPlatformSpark() {
        if (!ServiceManager.getInstance().isRemoteOK() || !DataCameraGetPushStateInfo.getInstance().isGetted()) {
            return false;
        }
        if ((DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.WIFI || DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.AOA) && DataCameraGetPushStateInfo.getInstance().getCameraType(0) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102) {
            return true;
        }
        return false;
    }

    private boolean isPlatformWM230() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.WM230) {
            return false;
        }
        return true;
    }

    private boolean isPlatFormWM240() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.WM240) {
            return false;
        }
        return true;
    }

    private boolean isPlatformWM245() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.WM245) {
            return false;
        }
        return true;
    }

    private boolean isPlatformInspire2() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Orange2) {
            return false;
        }
        return true;
    }

    private boolean isPlatformM210RTK() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210RTK) {
            return false;
        }
        return true;
    }

    private boolean isPlatformM210() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210) {
            return false;
        }
        return true;
    }

    private boolean isPlatformM200() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M200) {
            return false;
        }
        return true;
    }

    private boolean isPlatformPM420PRORTK() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420PRO_RTK) {
            return false;
        }
        return true;
    }

    private boolean isPlatformPM420PRO() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420PRO) {
            return false;
        }
        return true;
    }

    private boolean isPlatformPM420() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420) {
            return false;
        }
        return true;
    }

    private boolean isPlatformA3() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.A3) {
            return false;
        }
        return true;
    }

    private boolean isPlatformN3() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.N3) {
            return false;
        }
        return true;
    }

    private boolean isPlatformA2() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.A2) {
            return false;
        }
        return true;
    }

    private boolean isPlatformWM160() {
        if (!ServiceManager.getInstance().isRemoteOK() || DataOsdGetPushCommon.getInstance().isPushLosed() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.WM160) {
            return false;
        }
        return true;
    }

    private boolean isPlatformUnknow() {
        boolean isFlycConnected;
        boolean isCameraConnected;
        if (!DataOsdGetPushCommon.getInstance().isPushLosed()) {
            isFlycConnected = true;
        } else {
            isFlycConnected = false;
        }
        if (!DataCameraGetPushStateInfo.getInstance().isPushLosed()) {
            isCameraConnected = true;
        } else {
            isCameraConnected = false;
        }
        boolean isRemoteOk = ServiceManager.getInstance().isRemoteOK();
        boolean isRcOk = ServiceManager.getInstance().isOK();
        logSave("platform unknow judge, flyc: " + isFlycConnected + ", camera: " + isCameraConnected + ", remote: " + isRemoteOk + ", rc: " + isRcOk);
        if (!isRemoteOk || !isRcOk || (!isFlycConnected && !isCameraConnected)) {
            return false;
        }
        return true;
    }

    private boolean isRcPlatformUnknow() {
        boolean isRcConnected = ServiceManager.getInstance().isOK();
        boolean isRcTypeGot = DJIRcDetectHelper.getInstance().hasGotRcTypeInfo();
        logSave("rc platform judge, rc: " + isRcConnected + ", rc type got: " + isRcTypeGot);
        return isRcConnected && isRcTypeGot;
    }

    private boolean isRcP3P4() {
        if (isPlatformP3s() || isPlatformP3x() || isPlatformP4() || isPlatformP4P()) {
            return true;
        }
        log(TAG, "==========isRcP3x=========", false);
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        ProductType rcType = DJIRcDetectHelper.getInstance().getProductTypeByOsd();
        if (rcType == ProductType.litchiX || rcType == ProductType.litchiS || rcType == ProductType.Tomato) {
            return true;
        }
        return false;
    }

    private boolean isRcP3c() {
        if (isPlatformP3c()) {
            return true;
        }
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        if (DJIRcDetectHelper.getInstance().getProductTypeByOsd() != ProductType.litchiC) {
            return false;
        }
        return true;
    }

    private boolean isRcP3w() {
        if (isPlatformP3w()) {
            return true;
        }
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        if (DJIRcDetectHelper.getInstance().getProductTypeByOsd() != ProductType.P34K) {
            return false;
        }
        return true;
    }

    private boolean isRcP4P() {
        if (isPlatformP4P()) {
            return true;
        }
        if (!ServiceManager.getInstance().isConnected()) {
            return false;
        }
        if (DJIRcDetectHelper.getInstance().getProductTypeByOsd() != ProductType.Pomato) {
            return false;
        }
        return true;
    }

    private boolean isRcP4PSdr() {
        if (isPlatformP4PSdr()) {
            return true;
        }
        if (!ServiceManager.getInstance().isConnected()) {
            return false;
        }
        if (DJIRcDetectHelper.getInstance().getProductTypeByOsd() != ProductType.PomatoSDR) {
            return false;
        }
        return true;
    }

    private boolean isRcP4PRTK() {
        if (isPlatformP4PRTK()) {
            return true;
        }
        if (!ServiceManager.getInstance().isConnected()) {
            return false;
        }
        if (DJIRcDetectHelper.getInstance().getProductTypeByUsbInfo() == ProductType.PomatoRTK || DJIProductManager.getInstance().getRcType() == ProductType.PomatoRTK) {
            return true;
        }
        return false;
    }

    private boolean isRcPotato() {
        if (isPlatformP4A()) {
            return true;
        }
        if (!ServiceManager.getInstance().isConnected()) {
            return false;
        }
        if (DJIRcDetectHelper.getInstance().getProductTypeByOsd() != ProductType.Potato) {
            return false;
        }
        return true;
    }

    private boolean isRcInspire2() {
        if (ServiceManager.getInstance().isOK() && DJIRcDetectHelper.getInstance().getRcComponentType() == RcComponentType.Inspire2) {
            return true;
        }
        return false;
    }

    private boolean isRcCendence() {
        if (ServiceManager.getInstance().isOK() && DJIRcDetectHelper.getInstance().getRcComponentType() == RcComponentType.Cendence) {
            return true;
        }
        return false;
    }

    private boolean isRcCendenceSDR() {
        if (ServiceManager.getInstance().isOK() && DJIRcDetectHelper.getInstance().getRcComponentType() == RcComponentType.CendenceSDR) {
            return true;
        }
        return false;
    }

    private boolean isRcInspire() {
        if (isRcP3P4()) {
            return false;
        }
        if (isPlatformInspire()) {
            return true;
        }
        if (!ServiceManager.getInstance().isOK() || isRcLB2() || DJIRcDetectHelper.getInstance().getProductTypeByOsd() != ProductType.Orange) {
            return false;
        }
        return true;
    }

    private boolean isRcLB2() {
        if (ServiceManager.getInstance().isOK() && DJIRcDetectHelper.getInstance().getProductTypeByOsd() == ProductType.Grape2) {
            return true;
        }
        return false;
    }

    private boolean isRcFoldingDrone() {
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        ProductType rcType = DJIRcDetectHelper.getInstance().getProductTypeByOsd();
        if (rcType == ProductType.KumquatS || rcType == ProductType.KumquatX) {
            return true;
        }
        return false;
    }

    private boolean isRcSpark() {
        if (ServiceManager.getInstance().isOK() && DJIRcDetectHelper.getInstance().getProductTypeByOsd() == ProductType.Mammoth) {
            return true;
        }
        return false;
    }

    private boolean isRcWM230() {
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        return ProductType.WM230.equals(DJIRcDetectHelper.getInstance().getProductTypeByOsd());
    }

    public boolean isRcWM240() {
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        return ProductType.WM240.equals(DJIRcDetectHelper.getRcType(DJIRcDetectHelper.getInstance().getOsdGetter()));
    }

    public boolean isRcWM245() {
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        return ProductType.WM245.equals(DJIRcDetectHelper.getRcType(DJIRcDetectHelper.getInstance().getOsdGetter()));
    }

    private boolean isRcWM160() {
        if (!ServiceManager.getInstance().isOK()) {
            return false;
        }
        return ProductType.WM160.equals(DJIRcDetectHelper.getInstance().getProductTypeByOsd());
    }

    public boolean isRcRM500() {
        if (!ServiceManager.getInstance().isOK()) {
            DJILog.e("RM500 RCType", "Service Not Connected", new Object[0]);
            return false;
        }
        if (!ProductType.RM500.equals(DJIRcDetectHelper.getRcType(DJIRcDetectHelper.getInstance().getOsdGetter()))) {
            return false;
        }
        DJILog.e("RM500 RCType", "1301 True", new Object[0]);
        return true;
    }

    private boolean isMGBattery() {
        if (DJIBatteryDetectHelper.getInstance().getSmartBatteryType() == DataSmartBatteryGetStaticData.SmartBatteryType.MgBattery) {
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJILinkType linkType) {
        DJILog.saveConnectDebug("DJIComponentManager eventbus DJILinkType : " + linkType);
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        DJILog.saveConnectDebug("DJIComponentManager eventbus DataEvent : " + event + ", stack: " + DJILog.getCurrentStack());
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        DJILog.saveConnectDebug("DJIComponentManager eventbus DataCameraEvent : " + event);
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIRcDetectHelper event) {
        DJILog.save("DebugNotConnect", "DJIRcDetectHelper : " + event.getRcComponentType());
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushType type) {
        if (type.getType().equals(DataGimbalGetPushType.DJIGimbalType.Ronin)) {
            DJILog.save("DebugNotConnect", "Gimbal Type :Ronin");
            updateValueDelay();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo.Event event) {
        DJILog.save("DebugNotConnect", "DataCameraGetPushStateInfo.Event : " + event);
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushPayloadStatus.PayloadNeedReloadEvent event) {
        DJILog.save("DebugNotConnect", "PayloadNeedReloadEvent : " + event);
        updateValueDelay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(BLE.BLEEvent event) {
        if (event == BLE.BLEEvent.BLE_DEVICE_CONNECTED || event == BLE.BLEEvent.BLE_DEVICE_DISCONNECTED) {
            DJILog.save("DebugNotConnect", "BLE.BLEEvent : " + event);
            updateValueDelay();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIBatteryDetectHelper event) {
        DJILog.save("DebugNotConnect", "DJIBatteryDetectHelper : " + event);
        updateValueDelay();
    }

    private class UpdateTask implements Runnable {
        private long lastPostTime;
        private int updateIndex;
        private int[] updateTimeArray;

        private UpdateTask() {
            this.updateTimeArray = new int[]{1000, 2500, PhotoshopDirectory.TAG_LIGHTROOM_WORKFLOW};
            this.updateIndex = -1;
            this.lastPostTime = 0;
        }

        public void run() {
            this.updateIndex++;
            DJIComponentManager.this.updateValue();
            if (this.updateIndex < this.updateTimeArray.length) {
                DJIComponentManager.this.mHandler.postDelayed(this, (long) this.updateTimeArray[this.updateIndex]);
                this.lastPostTime = System.currentTimeMillis();
                return;
            }
            this.updateIndex = -1;
        }

        public void start() {
            this.updateIndex = 0;
            DJIComponentManager.this.mHandler.postDelayed(this, (long) this.updateTimeArray[this.updateIndex]);
            this.lastPostTime = System.currentTimeMillis();
        }

        public void reset() {
            DJIComponentManager.this.mHandler.removeCallbacks(this);
            this.updateIndex = -1;
        }

        public boolean nextRunTooLong() {
            return System.currentTimeMillis() - this.lastPostTime > ((long) this.updateTimeArray[0]);
        }

        public boolean isRunning() {
            return this.updateIndex >= 0;
        }
    }

    private void updateValueDelay() {
        boolean needStart = false;
        if (!this.updateTask.isRunning()) {
            needStart = true;
        } else if (this.updateTask.nextRunTooLong()) {
            this.updateTask.reset();
            needStart = true;
        }
        if (needStart) {
            this.updateTask.start();
        }
    }

    private void log(String tag, String msg, boolean showLog2) {
        long timestamp = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        if (showLog2) {
            DJILogHelper.getInstance().LOGD(tag, c.getTime() + msg, true, showLog2);
        }
    }

    private void logSave(String content) {
        DJILog.logWriteI(TAG, content, TAG, new Object[0]);
    }
}
