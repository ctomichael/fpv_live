package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.dji.mapkit.core.models.DJILatLng;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.BatteryThresholdBehavior;
import dji.common.flightcontroller.CalibrationOrientation;
import dji.common.flightcontroller.CompassCalibrationState;
import dji.common.flightcontroller.CompassSensorState;
import dji.common.flightcontroller.CompassState;
import dji.common.flightcontroller.ConnectionFailSafeBehavior;
import dji.common.flightcontroller.DJIMultiLEDControlMode;
import dji.common.flightcontroller.FlightAction;
import dji.common.flightcontroller.FlightMode;
import dji.common.flightcontroller.FlightOrientationMode;
import dji.common.flightcontroller.FlightWindWarning;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.flightcontroller.GoHomeAssessment;
import dji.common.flightcontroller.GoHomeExecutionState;
import dji.common.flightcontroller.HomePointState;
import dji.common.flightcontroller.LEDsSettings;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.MotorStartFailedState;
import dji.common.flightcontroller.NavigationSystemError;
import dji.common.flightcontroller.RedundancySensorUsedState;
import dji.common.flightcontroller.RemoteControllerFlightMode;
import dji.common.flightcontroller.SmartRTHState;
import dji.common.flightcontroller.UrgentStopMotorMode;
import dji.common.flightcontroller.VisionLandingProtectionState;
import dji.common.flightcontroller.adsb.AirSenseAirplaneState;
import dji.common.flightcontroller.adsb.AirSenseAvoidanceAction;
import dji.common.flightcontroller.adsb.AirSenseAvoidanceMode;
import dji.common.flightcontroller.adsb.AirSenseDirection;
import dji.common.flightcontroller.adsb.AirSenseWarningLevel;
import dji.common.flightcontroller.imu.CalibrationState;
import dji.common.flightcontroller.imu.IMUState;
import dji.common.flightcontroller.imu.OrientationCalibrationState;
import dji.common.flightcontroller.imu.SensorState;
import dji.common.flightcontroller.simulator.InitializationData;
import dji.common.flightcontroller.simulator.SimulatorState;
import dji.common.flightcontroller.simulator.SimulatorWindData;
import dji.common.flightcontroller.virtualstick.FlightControlData;
import dji.common.flightcontroller.virtualstick.FlightCoordinateSystem;
import dji.common.flightcontroller.virtualstick.RollPitchControlMode;
import dji.common.flightcontroller.virtualstick.VerticalControlMode;
import dji.common.flightcontroller.virtualstick.YawControlMode;
import dji.common.model.LocationCoordinate2D;
import dji.common.realname.AircraftBindingState;
import dji.common.remotecontroller.HardwareState;
import dji.common.util.CallbackUtils;
import dji.common.util.DJIParamMinMaxCapability;
import dji.common.util.LocationUtils;
import dji.common.util.MultiModeEnabledUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.FirmwareVersionLoader;
import dji.internal.aeroscope.AeroScopeClientSwitch;
import dji.internal.logics.CommonUtil;
import dji.log.DJILog;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.mc.DJIMcHelper;
import dji.midware.MidWare;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.Data1860GetPushCheckStatus;
import dji.midware.data.model.P3.DataADSBGetPushAvoidanceAction;
import dji.midware.data.model.P3.DataADSBGetPushWarning;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataCommonRestartDevice;
import dji.midware.data.model.P3.DataCommonSetGet1860TipsAudio;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushFlatCheck;
import dji.midware.data.model.P3.DataFlyc2GetWaypointInfo;
import dji.midware.data.model.P3.DataFlycActiveStatus;
import dji.midware.data.model.P3.DataFlycDetection;
import dji.midware.data.model.P3.DataFlycDownloadWayPointMissionMsg;
import dji.midware.data.model.P3.DataFlycFunctionControl;
import dji.midware.data.model.P3.DataFlycGetFsAction;
import dji.midware.data.model.P3.DataFlycGetParamInfoByHash;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPlaneName;
import dji.midware.data.model.P3.DataFlycGetPushAvoid;
import dji.midware.data.model.P3.DataFlycGetPushAvoidParam;
import dji.midware.data.model.P3.DataFlycGetPushFlycInstallError;
import dji.midware.data.model.P3.DataFlycGetPushForbidStatus;
import dji.midware.data.model.P3.DataFlycGetPushGoHomeCountDown;
import dji.midware.data.model.P3.DataFlycGetPushParamsByHash;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataFlycGetVoltageWarnning;
import dji.midware.data.model.P3.DataFlycJoystick;
import dji.midware.data.model.P3.DataFlycNavigationSwitch;
import dji.midware.data.model.P3.DataFlycNoeMissionPauseOrResume;
import dji.midware.data.model.P3.DataFlycPushForbidDataInfos;
import dji.midware.data.model.P3.DataFlycPushRedundancyStatus;
import dji.midware.data.model.P3.DataFlycSetEscEcho;
import dji.midware.data.model.P3.DataFlycSetGetVerPhone;
import dji.midware.data.model.P3.DataFlycSetHomePoint;
import dji.midware.data.model.P3.DataFlycSetLVoltageWarnning;
import dji.midware.data.model.P3.DataFlycSetMotorForceDisable;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataFlycSetPlaneName;
import dji.midware.data.model.P3.DataFlycSetSendOnBoard;
import dji.midware.data.model.P3.DataFlycSmartAck;
import dji.midware.data.model.P3.DataFlycStartIoc;
import dji.midware.data.model.P3.DataFlycStartNoeMission;
import dji.midware.data.model.P3.DataFlycStopIoc;
import dji.midware.data.model.P3.DataFlycStopNoeMission;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataRcGetPushGpsInfo;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataSimulatorConnectHeartPacket;
import dji.midware.data.model.P3.DataSimulatorGetPushConnectHeartPacket;
import dji.midware.data.model.P3.DataSimulatorGetPushFlightStatusParams;
import dji.midware.data.model.P3.DataSimulatorGetPushMainControllerReturnParams;
import dji.midware.data.model.P3.DataSimulatorRequestMainControllerParams;
import dji.midware.data.model.P3.DataSimulatorSetGetWind;
import dji.midware.data.model.P3.DataSimulatorSimulateFlightCommend;
import dji.midware.data.model.P3.DataUpgradeSelfRequest;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.model.common.DataCommonActiveGetVer;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.data.params.P3.RangeModel;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.sdk.SDKUtils;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.RepeatDataBase;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeGetFlycParamInfo;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeGetNewFlyParamInfo;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeSetFlyParamInfo;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.ParamInfoCallBack;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence.VirtualFenceAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.RTKKeys;
import dji.sdksharedlib.keycatalog.VirtualFenceKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.DJICompletionHelper;
import dji.sdksharedlib.util.abstractions.DJIRCAbstractionUtil;
import dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class FlightControllerAbstraction extends DJISDKCacheHWAbstraction implements DJIParamAccessListener {
    private static final int AIRPORT_WARNING_MAX_HEIGHT = 120;
    private static final int ATTI = 1;
    private static final int CLICKGO = 7;
    private static final int COURSE_LOCK = 0;
    private static final int DRAW = 18;
    private static final int GOHOME = 4;
    private static final int HOME_LOCK = 1;
    public static final LocationCoordinate2D HOME_LOC_REMOTE_NOT_RECORD = new LocationCoordinate2D(Double.NaN, Double.NaN);
    private static final String[] IMU_CALC_STAT_ALL = {ParamCfgName.GSTATUS_GYRACC_MSC_CURRENT_SIDE, ParamCfgName.GSTATUS_GYRACC_MSC_SAMPLED_SIDE, ParamCfgName.GSTATUS_GYRACC_MSC_REQUIRE_SIDE, ParamCfgName.GSTATUS_GYRACC_CAL_STATE, ParamCfgName.GSTATUS_GYRACC_CAL_CNT, ParamCfgName.GSTATUS_GYRACC_NEED_CALTYPE};
    private static final String INTERNAL_FIRMWARE_VERSION_TURN = "03.01";
    private static final int JOYSTICK = 5;
    private static final int LANDING = 2;
    private static final int MANUAL = 0;
    private static final int MAX_IMU_COUNT = 3;
    private static int MC_HIDE_VERSION_1 = 1;
    private static int MC_HIDE_VERSION_16 = 16;
    private static int MC_HIDE_VERSION_3 = 3;
    private static int MC_HIDE_VERSION_TEST = 0;
    private static final int MINIMUM_BATTERY_INTERVAL = 5;
    private static final int MSG_START_SIMULATE = 1;
    private static final int MSG_STOP_SIMULATE = 2;
    private static final int NAVI = 6;
    private static final int NOVICE = 17;
    private static final int N_A = 255;
    private static final int OSD_PUSH_FREQUENCY = 10;
    private static final int POINTING = 15;
    private static int PRECISION_TAKE_OFF_SUPPORTED_MIN_VERSION = 25;
    private static final int P_ATTI = 8;
    private static final int P_GPS = 10;
    private static final int P_LOCK = 2;
    private static final int P_POTI = 9;
    private static final int SIMULATION_MAX_WIND_SPEED = 20000;
    private static final int SIMULATION_MIN_WIND_SPEED = -2000;
    private static final int SPORT = 16;
    /* access modifiers changed from: private */
    public static String TAG = "DJIFlightControllerAbstraction";
    private static final int TAKEOFF = 3;
    private static final int TRACKING = 14;
    private final String[] COMPASS_MAG_OVER_CONFIG;
    private final String[] COMPASS_MAG_STAT_CONFIG;
    protected final String GSTATUS_RC_STOP_MOTOR_TYPE;
    private final String[] IMU_CALC_STAT_CONFIG_ONLY_ONE;
    private final String[] IMU_CALC_STAT_CONFIG_WITH_THREE;
    private final String[] IMU_CALC_STAT_CONFIG_WITH_TWO;
    private Handler.Callback callback;
    protected int compassCount;
    private InitializationData data;
    private Handler handler;
    private boolean hasCompassCalibrationRecorded;
    private boolean hasSimulatorStarted;
    private boolean hasStartedCalibration;
    private DJICompletionHelper helper;
    protected int imuCount;
    protected int internalFCVerion;
    protected int isBeginnerMode;
    private boolean isHomePointAltitudeSetted;
    private int isRebootInSModeCounter;
    private CompassCalibrationState lastCalibrationStatus;
    private GoHomeExecutionState lastGoHomeStatus;
    private DJILatLng lastHomeLatLng;
    private LocationCoordinate3D latestValidAircraftLocation;
    private boolean mImpactInAir;
    protected MergeGetFlycParamInfo mergeGetFlycParamInfo;
    protected MergeSetFlyParamInfo mergeSetFlyParamInfo;
    protected MergeGetNewFlyParamInfo newMergeGetFlycParamInfo;
    protected int previousRCSwitchPosition;
    private DJIParamAccessListener realNameEnabledListener;
    /* access modifiers changed from: private */
    public Timer realNameSupportTimer;
    /* access modifiers changed from: private */
    public RealNameSupportTimerTask realNameSupportTimerTask;
    private Timer simulatorTimer;
    /* access modifiers changed from: private */
    public DJISDKCacheHWAbstraction.InnerCallback startSimulateCallback;
    /* access modifiers changed from: private */
    public SimulatorInternalState state;
    /* access modifiers changed from: private */
    public DJISDKCacheHWAbstraction.InnerCallback stopSimulateCallback;
    private final String[] structCheckIndexs;

    private enum SimulatorInternalState {
        DISCONNECTED,
        CONNECTED,
        STARTING,
        RESPONSE_RECEIVED,
        RUNNING,
        STOPPING,
        STOPPED
    }

    /* access modifiers changed from: protected */
    public abstract boolean isNewProgressOfActivation();

    public FlightControllerAbstraction() {
        this.internalFCVerion = -1;
        this.imuCount = 3;
        this.compassCount = 3;
        this.isBeginnerMode = -1;
        this.previousRCSwitchPosition = -1;
        this.hasSimulatorStarted = false;
        this.isRebootInSModeCounter = 0;
        this.state = SimulatorInternalState.DISCONNECTED;
        this.mImpactInAir = false;
        this.callback = new Handler.Callback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (FlightControllerAbstraction.this.startSimulateCallback == null) {
                        return false;
                    }
                    if (FlightControllerAbstraction.this.state == SimulatorInternalState.STARTING || FlightControllerAbstraction.this.state == SimulatorInternalState.RESPONSE_RECEIVED) {
                        SimulatorInternalState unused = FlightControllerAbstraction.this.state = SimulatorInternalState.CONNECTED;
                    }
                    CallbackUtils.onFailure(FlightControllerAbstraction.this.startSimulateCallback, DJIError.COMMON_TIMEOUT);
                    DJISDKCacheHWAbstraction.InnerCallback unused2 = FlightControllerAbstraction.this.startSimulateCallback = null;
                    return false;
                } else if (msg.what != 2 || FlightControllerAbstraction.this.stopSimulateCallback == null) {
                    return false;
                } else {
                    if (FlightControllerAbstraction.this.state == SimulatorInternalState.STOPPING) {
                        SimulatorInternalState unused3 = FlightControllerAbstraction.this.state = SimulatorInternalState.CONNECTED;
                    }
                    CallbackUtils.onFailure(FlightControllerAbstraction.this.stopSimulateCallback, DJIError.COMMON_TIMEOUT);
                    DJISDKCacheHWAbstraction.InnerCallback unused4 = FlightControllerAbstraction.this.stopSimulateCallback = null;
                    return false;
                }
            }
        };
        this.handler = new Handler(BackgroundLooper.getLooper(), this.callback);
        this.IMU_CALC_STAT_CONFIG_WITH_THREE = new String[]{"imu_app_temp_cali.state_0", ParamCfgName.GSTATUS_GYRACC_STATE_0, ParamCfgName.GSTATUS_GYRACC_STATE_1, ParamCfgName.GSTATUS_GYRACC_STATE_2, "g_status.acc_gyro[0].cali_cnt_0", "g_status.acc_gyro[1].cali_cnt_0", "g_status.acc_gyro[2].cali_cnt_0", "g_config.fdi_sensor[0].gyr_stat_0", "g_config.fdi_sensor[1].gyr_stat_0", "g_config.fdi_sensor[2].gyr_stat_0", "g_config.fdi_sensor[0].acc_stat_0", "g_config.fdi_sensor[1].acc_stat_0", "g_config.fdi_sensor[2].acc_stat_0", "g_config.fdi_sensor[0].gyr_bias_0", "g_config.fdi_sensor[1].gyr_bias_0", "g_config.fdi_sensor[2].gyr_bias_0", "g_config.fdi_sensor[0].acc_bias_0", "g_config.fdi_sensor[1].acc_bias_0", "g_config.fdi_sensor[2].acc_bias_0", "g_status.ns_busy_dev_0"};
        this.IMU_CALC_STAT_CONFIG_WITH_TWO = new String[]{"imu_app_temp_cali.state_0", ParamCfgName.GSTATUS_GYRACC_STATE_0, ParamCfgName.GSTATUS_GYRACC_STATE_1, "g_status.acc_gyro[0].cali_cnt_0", "g_status.acc_gyro[1].cali_cnt_0", "g_config.fdi_sensor[0].gyr_stat_0", "g_config.fdi_sensor[1].gyr_stat_0", "g_config.fdi_sensor[0].acc_stat_0", "g_config.fdi_sensor[1].acc_stat_0", "g_config.fdi_sensor[0].gyr_bias_0", "g_config.fdi_sensor[1].gyr_bias_0", "g_config.fdi_sensor[0].acc_bias_0", "g_config.fdi_sensor[1].acc_bias_0", "g_status.ns_busy_dev_0"};
        this.IMU_CALC_STAT_CONFIG_ONLY_ONE = new String[]{"imu_app_temp_cali.cali_cnt_0", "imu_app_temp_cali.state_0", "g_config.fdi_sensor[0].gyr_bias_0", "g_config.fdi_sensor[0].acc_bias_0", "g_status.ns_busy_dev_0"};
        this.COMPASS_MAG_OVER_CONFIG = new String[]{"g_config.fdi_sensor[0].mag_over_0", "g_config.fdi_sensor[1].mag_over_0", "g_config.fdi_sensor[2].mag_over_0"};
        this.COMPASS_MAG_STAT_CONFIG = new String[]{"g_config.fdi_sensor[0].mag_stat_0", "g_config.fdi_sensor[1].mag_stat_0", "g_config.fdi_sensor[2].mag_stat_0"};
        this.GSTATUS_RC_STOP_MOTOR_TYPE = ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE;
        this.structCheckIndexs = new String[]{ParamCfgName.GSTATUS_USER_INFO_TOTAL_MOTOR_START_TIME, ParamCfgName.GSTATUS_USER_INFO_LAST_TOTAL_MOTOR_START_TIME};
        this.hasStartedCalibration = false;
        this.helper = DJICompletionHelper.getInstance();
        this.lastCalibrationStatus = CompassCalibrationState.NOT_CALIBRATING;
        this.hasCompassCalibrationRecorded = false;
        this.lastGoHomeStatus = GoHomeExecutionState.NOT_EXECUTING;
        this.state = SimulatorInternalState.CONNECTED;
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability getMaxFlightRadiusRange() {
        if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
            return new DJIParamMinMaxCapability(true, 15, 100);
        }
        return new DJIParamMinMaxCapability(true, 15, Integer.valueOf((int) PhotoshopDirectory.TAG_LIGHTROOM_WORKFLOW));
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability getMaxFlightHeightRange() {
        DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) CacheHelper.getFlightController(FlightControllerKeys.MAX_FLIGHT_HEIGHT_RANGE);
        if (capability == null) {
            return new DJIParamMinMaxCapability(true, 20, 500);
        }
        return capability;
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability getGoHomeHeightRange() {
        DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) CacheHelper.getFlightController(FlightControllerKeys.GO_HOME_HEIGHT_RANGE);
        if (capability == null) {
            return new DJIParamMinMaxCapability(true, 20, 500);
        }
        return capability;
    }

    /* access modifiers changed from: protected */
    public boolean isLandingGearMovable() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOnboardSDKAvailable() {
        return false;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        this.imuCount = 1;
        this.compassCount = 1;
        CacheHelper.addFlightControllerListener(this, FlightControllerKeys.MAX_FLIGHT_HEIGHT_RANGE, FlightControllerKeys.GO_HOME_HEIGHT_RANGE, DJISDKCacheKeys.FIRMWARE_VERSION);
        notifyValueChangeForKeyPath(Boolean.valueOf(isLandingGearMovable()), convertKeyToPath(FlightControllerKeys.IS_LANDING_GEAR_MOVABLE));
        notifyValueChangeForKeyPath(Boolean.valueOf(isOnboardSDKAvailable()), convertKeyToPath(FlightControllerKeys.IS_ON_BOARD_SDK_AVAILABLE));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_ONBOARD_FCHANNEL_AVAILABLE));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_RTK_SUPPORTED));
        notifyValueChangeForKeyPath(Integer.valueOf(this.imuCount), convertKeyToPath(FlightControllerKeys.IMU_COUNT));
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_FLIGHT_ASSISTANT_SUPPORTED));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.NEED_LIMIT_FLIGHT_HEIGHT));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_LANDING_CONFIRMATION_NEEDED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isVirtualFenceSupported()), convertKeyToPath(FlightControllerKeys.IS_VIRTUAL_FENCE_SUPPORTED));
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_IN_LANDING_MODE));
        notifyValueChangeForKeyPath(Boolean.valueOf(isDataProtectionAssistantSupported()), convertKeyToPath(FlightControllerKeys.ACCESS_LOCKER_SUPPORTED));
        notifyValueChangeForKeyPath(Boolean.valueOf(isPropellerCalibrationSupported()), convertKeyToPath(FlightControllerKeys.IS_PROPELLER_CALIBRATION_SUPPORTED));
        notifyValueChangeForKeyPath(getMaxFlightRadiusRange(), convertKeyToPath(FlightControllerKeys.MAX_FLIGHT_RADIUS_RANGE));
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
        this.mergeGetFlycParamInfo = MergeGetFlycParamInfo.getInstance();
        this.newMergeGetFlycParamInfo = MergeGetNewFlyParamInfo.getInstance();
        this.mergeSetFlyParamInfo = new MergeSetFlyParamInfo();
        this.lastHomeLatLng = new DJILatLng(0.0d, 0.0d);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        initFlightControllerSupportParameter();
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
            onEvent3BackgroundThread(DataOsdGetPushHome.getInstance());
        }
        if (DataFlycGetPushAvoid.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushAvoid.getInstance());
        }
        if (DataSimulatorGetPushConnectHeartPacket.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataSimulatorGetPushConnectHeartPacket.getInstance());
        }
        if (DataFlycGetPushParamsByHash.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushParamsByHash.getInstance());
        }
        if (DataOsdGetPushHome.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushHome.getInstance());
        }
        if (DataFlycGetPushGoHomeCountDown.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushGoHomeCountDown.getInstance());
        }
        if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushSmartBattery.getInstance());
        }
        if (DataFlycGetPushForbidStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushForbidStatus.getInstance());
        }
        if (DataFlycGetPushAvoidParam.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushAvoidParam.getInstance());
        }
        if (DataADSBGetPushAvoidanceAction.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataADSBGetPushAvoidanceAction.getInstance());
        }
        if (DataRcGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushParams.getInstance());
        }
        if (Data1860GetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(Data1860GetPushCheckStatus.getInstance());
        }
        if (DataFlycPushForbidDataInfos.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycPushForbidDataInfos.getInstance());
        }
        setupForRealNameSystem();
    }

    public void destroy() {
        CacheHelper.removeListener(this);
        DJIEventBusUtil.unRegister(this);
        this.handler.removeCallbacksAndMessages(null);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(FlightControllerKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public void initializeSubComponents(DJISDKCacheStoreLayer storeLayer) {
        super.initializeSubComponents(storeLayer);
        IntelligentFlightAssistantAbstraction intelligentFlightAssistant = newIntelligentFlightAssistantIfSupport();
        if (intelligentFlightAssistant != null) {
            addSubComponents(intelligentFlightAssistant, IntelligentFlightAssistantKeys.COMPONENT_KEY, 0, storeLayer);
        }
        RTKAbstraction rtkAbstraction = getRTKAbstractionIfSupported();
        if (rtkAbstraction != null) {
            addSubComponents(rtkAbstraction, RTKKeys.COMPONENT_KEY, 0, storeLayer);
        }
        VirtualFenceAbstraction virtualFence = newVirtualFenceIfSupport();
        if (virtualFence != null) {
            addSubComponents(virtualFence, VirtualFenceKeys.COMPONENT_KEY, 0, storeLayer);
        }
    }

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return null;
    }

    /* access modifiers changed from: protected */
    public RTKAbstraction getRTKAbstractionIfSupported() {
        return null;
    }

    /* access modifiers changed from: protected */
    public VirtualFenceAbstraction newVirtualFenceIfSupport() {
        return null;
    }

    @Action(FlightControllerKeys.START_SIMULATOR)
    public void startSimulator(DJISDKCacheHWAbstraction.InnerCallback callback2, InitializationData data2) {
        DJILog.d(FlightControllerKeys.START_SIMULATOR, "click", new Object[0]);
        if (data2 == null || !LocationUtils.checkValidGPSCoordinate(data2.getLocation().getLatitude(), data2.getLocation().getLongitude()) || data2.getUpdateFrequency() > 150 || data2.getUpdateFrequency() < 2 || data2.getSatelliteCount() < 0 || data2.getSatelliteCount() > 20) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (this.data != null && this.data.equals(data2) && this.state == SimulatorInternalState.RUNNING) {
            CallbackUtils.onSuccess(callback2, (Object) null);
        } else if (this.state == SimulatorInternalState.CONNECTED || this.state == SimulatorInternalState.STOPPED) {
            this.state = SimulatorInternalState.STARTING;
            DataSimulatorConnectHeartPacket.getInstance().setFlag(1).start((DJIDataCallBack) null);
            this.startSimulateCallback = callback2;
            startSimulatorHeartBeatTimer();
            this.data = data2;
            Message message = this.handler.obtainMessage();
            message.what = 1;
            if (!this.handler.hasMessages(1)) {
                this.handler.sendMessageDelayed(message, 5000);
            }
        } else {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(FlightControllerKeys.SIMULATOR_WIND_DATA)
    public void setSimulatorWindData(SimulatorWindData data2, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (data2 == null || data2.getWindSpeedX() < SIMULATION_MIN_WIND_SPEED || data2.getWindSpeedX() > 20000 || data2.getWindSpeedY() < SIMULATION_MIN_WIND_SPEED || data2.getWindSpeedY() > 20000 || data2.getWindSpeedZ() < SIMULATION_MIN_WIND_SPEED || data2.getWindSpeedZ() > 20000) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (this.state != SimulatorInternalState.RUNNING) {
            DJILog.d(TAG, "Error: the state of simulator is" + this.state.name(), new Object[0]);
            CallbackUtils.onFailure(callback2, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            DataSimulatorSetGetWind.getInstance().setWindSpeedX(data2.getWindSpeedX()).setWindSpeedY(data2.getWindSpeedY()).setWindSpeedZ(data2.getWindSpeedZ()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    if (callback2 != null) {
                        callback2.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback2 != null) {
                        callback2.onFails(DJIError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    private void startSimulatorHeartBeatTimer() {
        stopSimulatorHeartBeatTimer();
        this.simulatorTimer = new Timer();
        this.simulatorTimer.schedule(new HeartPinTimerTask(), 0, 1000);
    }

    private void stopSimulatorHeartBeatTimer() {
        if (this.simulatorTimer != null) {
            this.simulatorTimer.purge();
            this.simulatorTimer.cancel();
        }
    }

    private void checkStartSimulate() {
        if (this.handler.hasMessages(1) && this.state == SimulatorInternalState.RUNNING) {
            CallbackUtils.onSuccess(this.startSimulateCallback, (Object) null);
            this.startSimulateCallback = null;
            this.handler.removeMessages(1);
        }
    }

    @Action(FlightControllerKeys.STOP_SIMULATOR)
    public void stopSimulator(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (this.state == SimulatorInternalState.STOPPED) {
            CallbackUtils.onSuccess(callback2, (Object) null);
        } else if (this.state != SimulatorInternalState.RUNNING) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            this.state = SimulatorInternalState.STOPPING;
            stopSimulatorHeartBeatTimer();
            DataSimulatorSimulateFlightCommend.getInstance().closeSimulator().start((DJIDataCallBack) null);
            this.stopSimulateCallback = callback2;
            Message message = this.handler.obtainMessage();
            message.what = 2;
            if (!this.handler.hasMessages(2)) {
                this.handler.sendMessageDelayed(message, 5000);
            }
        }
    }

    private void checkStopSimulate() {
        if (this.handler.hasMessages(2) && this.state == SimulatorInternalState.STOPPED) {
            CallbackUtils.onSuccess(this.stopSimulateCallback, (Object) null);
            this.stopSimulateCallback = null;
            this.handler.removeMessages(2);
        }
    }

    @Action(FlightControllerKeys.SET_DATA_UPGRADE_SELF)
    public void setDataUpgradeSelfRequest(DJISDKCacheHWAbstraction.InnerCallback callback2, boolean doCmd) {
        DataUpgradeSelfRequest.getInstance().setControlCmd(doCmd ? DataUpgradeSelfRequest.ControlCmd.DO : DataUpgradeSelfRequest.ControlCmd.UNDO).start();
        callback2.onSuccess(true);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushForbidStatus state2) {
        notifyValueChangeForKeyPath(Boolean.valueOf(state2.isSupportDetectionV3()), convertKeyToPath(FlightControllerKeys.IS_SUPPORT_AERO_SCOPE));
        notifyValueChangeForKeyPath(Integer.valueOf(state2.getLimitMaxHeight()), convertKeyToPath(FlightControllerKeys.NFZ_MAX_HEIGHT));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushAvoid params) {
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isVisualSensorEnable()), convertKeyToPath(FlightControllerKeys.IS_VISION_SENSOR_ENABLE));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isVisualSensorWork()), convertKeyToPath(FlightControllerKeys.IS_VISION_SENSOR_WORK));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSimulatorGetPushMainControllerReturnParams mParams) {
        if (this.state == SimulatorInternalState.STARTING) {
            this.state = SimulatorInternalState.RESPONSE_RECEIVED;
        }
        DataSimulatorSimulateFlightCommend.getInstance().setLatitude(this.data.getLocation().getLatitude()).setLongitude(this.data.getLocation().getLongitude()).setAltitude(0.0d).setHz((int) (600.0f / ((float) this.data.getUpdateFrequency()))).setUseRC(true).setUseThird(false).setBatterySim(false).setGpsCount(this.data.getSatelliteCount()).setRoll(true).setPitch(true).setYaw(true).setPositionX(true).setPositionY(true).setPositionZ(true).setLatitude(true).setLongitude(true).setVelocityX(true).setVelocityY(true).setVelocityZ(true).openSimulator().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
        onEvent3BackgroundThread(DataOsdGetPushHome.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushAvoidParam avoid) {
        notifyValueChangeForKeyPath(Boolean.valueOf(avoid.avoidGroundForceLanding()), convertKeyToPath(FlightControllerKeys.IS_LANDING_CONFIRMATION_NEEDED));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSimulatorGetPushConnectHeartPacket packet) {
        if (this.state == SimulatorInternalState.STARTING) {
            this.state = SimulatorInternalState.RESPONSE_RECEIVED;
            DataSimulatorRequestMainControllerParams.getInstance().start((DJIDataCallBack) null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSimulatorSetGetWind params) {
        notifyValueChangeForKeyPath(new SimulatorWindData.Builder().windSpeedX(params.getWindSpeedX()).windSpeedY(params.getWindSpeedY()).windSpeedZ(params.getWindSpeedZ()).build(), convertKeyToPath(FlightControllerKeys.SIMULATOR_WIND_DATA));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSimulatorGetPushFlightStatusParams params) {
        if (FirmwareVersionLoader.getInstance().getFlightControllerFirmwareVersion() == null || FirmwareVersionLoader.getInstance().getFlightControllerFirmwareVersion().compareTo(INTERNAL_FIRMWARE_VERSION_TURN) < 0) {
            notifyValueChangeForKeyPath(new SimulatorState.Builder().areMotorsOn(params.hasMotorTurnedOn()).isFlying(params.isInTheAir()).pitch(-((float) (((double) (get(params, 1) * 180.0f)) / 3.141592653589793d))).roll((float) (((double) (get(params, 0) * 180.0f)) / 3.141592653589793d)).yaw((float) (((double) (get(params, 2) * 180.0f)) / 3.141592653589793d)).positionX(get(params, 3)).positionY(get(params, 4)).positionZ(get(params, 5)).location(new LocationCoordinate2D((((double) get(params, 6)) * 180.0d) / 3.141592653589793d, (((double) get(params, 7)) * 180.0d) / 3.141592653589793d)).build(), convertKeyToPath(FlightControllerKeys.SIMULATOR_STATE));
        } else {
            notifyValueChangeForKeyPath(new SimulatorState.Builder().areMotorsOn(params.hasMotorTurnedOn()).isFlying(params.isInTheAir()).pitch(-((float) (((double) (get(params, 1) * 180.0f)) / 3.141592653589793d))).roll((float) (((double) (get(params, 0) * 180.0f)) / 3.141592653589793d)).yaw((float) (((double) (get(params, 2) * 180.0f)) / 3.141592653589793d)).positionX(get(params, 3)).positionY(get(params, 4)).positionZ(get(params, 5)).location(new LocationCoordinate2D((double) get(params, 6), (double) get(params, 7))).build(), convertKeyToPath(FlightControllerKeys.SIMULATOR_STATE));
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [int, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushFlycInstallError params) {
        if (params.getYawInstallErrorLevel() >= 2) {
            notifyValueChangeForKeyPath((Object) 2, convertKeyToPath(FlightControllerKeys.DEVICE_INSTALL_ERROR_YAW));
        }
        if (params.getRollInstallErrorLevel() >= 2 || params.getPitchInstallErrorLevel() >= 2) {
            notifyValueChangeForKeyPath((Object) 2, convertKeyToPath(FlightControllerKeys.DEVICE_INSTALL_ERROR_MASS_CENTER));
        }
        if (params.getGyroXInstallErrorLevel() >= 2 || params.getGyroYInstallErrorLevel() >= 2 || params.getGyroZInstallErrorLevel() >= 2 || params.getAccXInstallErrorLevel() >= 2 || params.getAccYInstallErrorLevel() >= 2 || params.getAccZInstallErrorLevel() >= 2) {
            notifyValueChangeForKeyPath((Object) 2, convertKeyToPath(FlightControllerKeys.DEVICE_INSTALL_ERROR_VIBRATION));
        }
        if (params.getThrustInstallErrorLevel() >= 2) {
            notifyValueChangeForKeyPath((Object) 2, convertKeyToPath(FlightControllerKeys.DEVICE_INSTALL_ERROR_HOVER_THRUST_LOW));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataADSBGetPushWarning pushWarning) {
        notifyValueChangeForKeyPath(Boolean.valueOf(pushWarning.isConnectAdsb()), convertKeyToPath(FlightControllerKeys.AIR_SENSE_SYSTEM_CONNECTED));
        notifyValueChangeForKeyPath(AirSenseWarningLevel.find(pushWarning.getWarningType()), convertKeyToPath(FlightControllerKeys.AIR_SENSE_SYSTEM_WARNING_LEVEL));
        ArrayList<DataADSBGetPushWarning.FlightItem> list = pushWarning.getList();
        if (list.size() > 0) {
            List<AirSenseAirplaneState> warningInformations = new ArrayList<>();
            Iterator<DataADSBGetPushWarning.FlightItem> it2 = list.iterator();
            while (it2.hasNext()) {
                DataADSBGetPushWarning.FlightItem item = it2.next();
                warningInformations.add(new AirSenseAirplaneState.Builder().code(item.ICAOAddress).warningLevel(AirSenseWarningLevel.find(item.warningLevel)).relativeDirection(calculateDirectionWithAircraftLocation(item.latitude, item.longitude)).heading(item.heading).distance(item.distance).build());
            }
            notifyValueChangeForKeyPath((AirSenseAirplaneState[]) warningInformations.toArray(new AirSenseAirplaneState[warningInformations.size()]), convertKeyToPath(FlightControllerKeys.AIR_SENSE_AIRPLANE_STATES));
            return;
        }
        notifyValueChangeForKeyPath(new AirSenseAirplaneState[0], convertKeyToPath(FlightControllerKeys.AIR_SENSE_AIRPLANE_STATES));
    }

    private AirSenseDirection calculateDirectionWithAircraftLocation(double latitude, double longitude) {
        LocationCoordinate2D coordinate = new LocationCoordinate2D(latitude, longitude);
        if (this.latestValidAircraftLocation != null && isValidCoordinate(latitude, longitude)) {
            if (isValidCoordinate(this.latestValidAircraftLocation.getLatitude(), this.latestValidAircraftLocation.getLongitude())) {
                double longDiff = coordinate.getLongitude() - this.latestValidAircraftLocation.getLongitude();
                double latiDiff = coordinate.getLatitude() - this.latestValidAircraftLocation.getLatitude();
                if (longDiff < -180.0d) {
                    longDiff += 360.0d;
                } else if (longDiff > 180.0d) {
                    longDiff -= 360.0d;
                }
                if (Math.abs(longDiff) < 6.0E-7d && Math.abs(latiDiff) < 6.0E-7d) {
                    return AirSenseDirection.UNKNOWN;
                }
                double angle = LocationUtils.radianToDegree(Math.acos(latiDiff / Math.sqrt((longDiff * longDiff) + (latiDiff * latiDiff))));
                if (longDiff > 0.0d) {
                    angle *= -1.0d;
                }
                if (-22.5d < angle && angle <= 22.5d) {
                    return AirSenseDirection.NORTH;
                }
                if (-67.5d < angle && angle <= -22.5d) {
                    return AirSenseDirection.NORTH_EAST;
                }
                if (-112.5d < angle && angle <= -67.5d) {
                    return AirSenseDirection.EAST;
                }
                if (-157.5d < angle && angle <= -112.5d) {
                    return AirSenseDirection.SOUTH_EAST;
                }
                if ((-180.0d <= angle && angle <= -157.5d) || (157.5d < angle && angle <= 180.0d)) {
                    return AirSenseDirection.SOUTH;
                }
                if (112.5d < angle && angle <= 157.5d) {
                    return AirSenseDirection.SOUTH_WEST;
                }
                if (67.5d < angle && angle <= 112.5d) {
                    return AirSenseDirection.WEST;
                }
                if (22.5d >= angle || angle > 67.5d) {
                    return AirSenseDirection.UNKNOWN;
                }
                return AirSenseDirection.NORTH_WEST;
            }
        }
        return AirSenseDirection.UNKNOWN;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataADSBGetPushAvoidanceAction avoidanceAction) {
        if (avoidanceAction.isGetted() && avoidanceAction.isConnectAdsb()) {
            notifyValueChangeForKeyPath(new AirSenseAvoidanceAction(((float) avoidanceAction.getActionCountDown()) / 10.0f, AirSenseAvoidanceMode.find(avoidanceAction.getActionMode())), convertKeyToPath(FlightControllerKeys.AIR_SENSE_AVOIDANCE_ACTION));
        }
    }

    private boolean isValidCoordinate(double latitude, double longitude) {
        return (!LocationUtils.checkValidGPSCoordinate(latitude, longitude) || longitude == 0.0d || latitude == 0.0d) ? false : true;
    }

    private float get(DataSimulatorGetPushFlightStatusParams params, int index) {
        return BytesUtil.getFloat(BytesUtil.readBytes(params.getResult(), (index * 4) + 2, 4));
    }

    private class HeartPinTimerTask extends TimerTask {
        private HeartPinTimerTask() {
        }

        public void run() {
            if (FlightControllerAbstraction.this.state == SimulatorInternalState.RUNNING || FlightControllerAbstraction.this.state == SimulatorInternalState.STARTING || FlightControllerAbstraction.this.state == SimulatorInternalState.RESPONSE_RECEIVED) {
                int flag = 0;
                if (FlightControllerAbstraction.this.state == SimulatorInternalState.STARTING) {
                    flag = 1;
                }
                DataSimulatorConnectHeartPacket.getInstance().setFlag(flag).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.HeartPinTimerTask.AnonymousClass1 */

                    public void onSuccess(Object model) {
                    }

                    public void onFailure(Ccode ccode) {
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushParamsByHash pushParams) {
        updateIMUState(pushParams);
        updateCompassState();
    }

    /* access modifiers changed from: protected */
    public void updateIMUState(DataFlycGetPushParamsByHash params) {
        switch (this.imuCount) {
            case 1:
                readTheOnlyOneImuState(null);
                break;
            case 2:
                readFirstImuState(null, -1);
                readSecondImuState(null, -1);
                break;
            case 3:
                readFirstImuState(null, -1);
                readSecondImuState(null, -1);
                readThirdImuState(null, -1);
                break;
        }
        notifyRedundancySensorUsedState();
    }

    /* access modifiers changed from: protected */
    public void updateCompassState() {
        readCompassSensorState();
        notifyRedundancySensorUsedState();
    }

    /* access modifiers changed from: protected */
    public CalibrationState updateIMUCaliStatus(String tag) {
        return CalibrationState.convertIMUCalibrationStatus(DJIFlycParamInfoManager.read(tag).value.intValue());
    }

    private String[] getImuCaliParam() {
        if (this.imuCount >= 3) {
            String[] result = new String[(IMU_CALC_STAT_ALL.length + this.IMU_CALC_STAT_CONFIG_WITH_THREE.length)];
            System.arraycopy(IMU_CALC_STAT_ALL, 0, result, 0, IMU_CALC_STAT_ALL.length);
            System.arraycopy(this.IMU_CALC_STAT_CONFIG_WITH_THREE, 0, result, IMU_CALC_STAT_ALL.length, this.IMU_CALC_STAT_CONFIG_WITH_THREE.length);
            return result;
        } else if (this.imuCount == 2) {
            String[] result2 = new String[(IMU_CALC_STAT_ALL.length + this.IMU_CALC_STAT_CONFIG_WITH_TWO.length)];
            System.arraycopy(IMU_CALC_STAT_ALL, 0, result2, 0, IMU_CALC_STAT_ALL.length);
            System.arraycopy(this.IMU_CALC_STAT_CONFIG_WITH_TWO, 0, result2, IMU_CALC_STAT_ALL.length, this.IMU_CALC_STAT_CONFIG_WITH_TWO.length);
            return result2;
        } else {
            String[] result3 = new String[(IMU_CALC_STAT_ALL.length + this.IMU_CALC_STAT_CONFIG_ONLY_ONE.length)];
            System.arraycopy(IMU_CALC_STAT_ALL, 0, result3, 0, IMU_CALC_STAT_ALL.length);
            System.arraycopy(this.IMU_CALC_STAT_CONFIG_ONLY_ONE, 0, result3, IMU_CALC_STAT_ALL.length, this.IMU_CALC_STAT_CONFIG_ONLY_ONE.length);
            return result3;
        }
    }

    @Getter(FlightControllerKeys.IMU_STATE)
    public void getIMUMultiSideCalibrationState(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (this.internalFCVerion >= 16) {
            new DataFlycGetParams().setInfos(getImuCaliParam()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    IMUState imuState = new IMUState(FlightControllerAbstraction.this.imuCount);
                    imuState.setMultiSideCalibrationType(true);
                    CalibrationState imuCaliStatus = FlightControllerAbstraction.this.updateIMUCaliStatus(ParamCfgName.GSTATUS_GYRACC_CAL_STATE);
                    imuState.setCalibrationState(imuCaliStatus);
                    int progress = DJIFlycParamInfoManager.valueOf(ParamCfgName.GSTATUS_GYRACC_CAL_CNT).intValue();
                    imuState.setCalibrationProgress(progress);
                    if (FlightControllerAbstraction.this.imuCount >= 3) {
                        FlightControllerAbstraction.this.readFirstImuState(imuState, progress);
                        FlightControllerAbstraction.this.readSecondImuState(imuState, progress);
                        FlightControllerAbstraction.this.readThirdImuState(imuState, progress);
                    } else if (FlightControllerAbstraction.this.imuCount == 2) {
                        FlightControllerAbstraction.this.readFirstImuState(imuState, progress);
                        FlightControllerAbstraction.this.readSecondImuState(imuState, progress);
                    } else if (FlightControllerAbstraction.this.imuCount == 1) {
                        FlightControllerAbstraction.this.readTheOnlyOneImuState(imuState);
                    }
                    FlightControllerAbstraction.this.notifyRedundancySensorUsedState();
                    byte sampledSide = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_GYRACC_MSC_SAMPLED_SIDE).value.byteValue();
                    byte requireSides = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_GYRACC_MSC_REQUIRE_SIDE).value.byteValue();
                    int sideStateValue = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_GYRACC_MSC_CURRENT_SIDE).value.intValue();
                    DJILog.logWriteI("IMUCalibrationLog", "[FlightControllerAbstraction] onSuccess: \n imuCaliStatus " + imuCaliStatus + ", progress " + progress + "\n sampledSide " + BytesUtil.byte2hex(sampledSide) + ", requireSides " + BytesUtil.byte2hex(requireSides) + ", sideStateValue " + sideStateValue, "IMUCalibration", new Object[0]);
                    if (sideStateValue == 0 && imuCaliStatus == CalibrationState.CALIBRATING) {
                        imuState.getMultipleOrientationCalibrationHint().setState(OrientationCalibrationState.CALIBRATING);
                    } else if (sideStateValue == 1) {
                        imuState.getMultipleOrientationCalibrationHint().setState(OrientationCalibrationState.COMPLETED);
                    } else {
                        imuState.getMultipleOrientationCalibrationHint().setState(OrientationCalibrationState.UNKNOWN);
                    }
                    for (int i = 0; i < imuState.getNeedCalibrationSide().length; i++) {
                        byte result = (byte) (((byte) (1 << i)) & requireSides);
                        imuState.getNeedCalibrationSide()[i] = result != 0;
                        if (result != 0) {
                            imuState.getMultipleOrientationCalibrationHint().getOrientationsToCalibrate().add(CalibrationOrientation.find(i));
                        } else if (imuState.getMultipleOrientationCalibrationHint().getOrientationsToCalibrate().contains(CalibrationOrientation.find(i))) {
                            imuState.getMultipleOrientationCalibrationHint().getOrientationsToCalibrate().remove(CalibrationOrientation.find(i));
                        }
                    }
                    for (int i2 = 0; i2 < imuState.getFinishCalibrationSide().length; i2++) {
                        byte result2 = (byte) (((byte) (1 << i2)) & sampledSide);
                        imuState.getFinishCalibrationSide()[i2] = result2 != 0;
                        if (result2 != 0) {
                            imuState.getMultipleOrientationCalibrationHint().getOrientationsCalibrated().add(CalibrationOrientation.find(i2));
                            if (imuState.getMultipleOrientationCalibrationHint().getOrientationsToCalibrate().contains(CalibrationOrientation.find(i2))) {
                                imuState.getMultipleOrientationCalibrationHint().getOrientationsToCalibrate().remove(CalibrationOrientation.find(i2));
                            }
                        } else if (imuState.getMultipleOrientationCalibrationHint().getOrientationsCalibrated().contains(CalibrationOrientation.find(i2))) {
                            imuState.getMultipleOrientationCalibrationHint().getOrientationsCalibrated().remove(CalibrationOrientation.find(i2));
                        }
                    }
                    CallbackUtils.onSuccess(callback2, imuState);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
                }
            });
            return;
        }
        IMUState imuState = new IMUState(this.imuCount);
        imuState.setMultiSideCalibrationType(false);
        getSubIMUCalibrateState(imuState, -1, callback2);
    }

    class RealNameSupportTimerTask extends TimerTask {
        int repeatTimes = 3;

        RealNameSupportTimerTask() {
        }

        public void run() {
            if (this.repeatTimes > 0) {
                this.repeatTimes--;
                return;
            }
            if (FlightControllerAbstraction.this.realNameSupportTimerTask != null) {
                FlightControllerAbstraction.this.realNameSupportTimerTask.cancel();
            }
            if (FlightControllerAbstraction.this.realNameSupportTimer != null) {
                FlightControllerAbstraction.this.realNameSupportTimer.cancel();
            }
            FlightControllerAbstraction.this.notifyValueChangeForKeyPath(false, FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.REAL_NAME_SUPPORTED));
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    public void setupForRealNameSystem() {
        DJISDKCacheKey enableKey = KeyHelper.getFlightControllerKey(FlightControllerKeys.REAL_NAME_SYSTEM_ENABLED);
        if (DJISDKCache.getInstance().getAvailableValue(enableKey) != null) {
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.REAL_NAME_SUPPORTED));
            return;
        }
        this.realNameEnabledListener = new FlightControllerAbstraction$$Lambda$0(this);
        this.realNameSupportTimer = new Timer();
        this.realNameSupportTimerTask = new RealNameSupportTimerTask();
        this.realNameSupportTimer.schedule(this.realNameSupportTimerTask, 0, 1000);
        DJISDKCache.getInstance().startListeningForUpdates(enableKey, this.realNameEnabledListener, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$setupForRealNameSystem$0$FlightControllerAbstraction(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && newValue.getData() != null) {
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.REAL_NAME_SUPPORTED));
            DJISDKCache.getInstance().stopListening(this.realNameEnabledListener);
            if (this.realNameSupportTimerTask != null) {
                this.realNameSupportTimerTask.cancel();
            }
            if (this.realNameSupportTimer != null) {
                this.realNameSupportTimer.cancel();
            }
        }
    }

    @Getter(FlightControllerKeys.HAS_WRITTEN_UID)
    public void getRealNameSystemHasWrittenUID(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycDetection dataFlycDetection = new DataFlycDetection(null);
        dataFlycDetection.setSubCmdId(DataFlycDetection.SubCmdId.GetIsSetUUID).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (callback2 != null) {
                    callback2.onSuccess(Boolean.valueOf(dataFlycDetection.getIsSetUUID()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    callback2.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(FlightControllerKeys.REAL_NAME_SYSTEM_UID)
    public void setRealNameSystemUID(String uid, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (!TextUtils.isEmpty(uid) && uid.length() <= 20) {
            new DataFlycDetection(null).setSubCmdId(DataFlycDetection.SubCmdId.SetUUID).setUUID(uid).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    if (callback2 != null) {
                        callback2.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback2 != null) {
                        callback2.onFails(DJIError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback2 != null) {
            callback2.onFails(DJIError.getDJIError(Ccode.INVALID_PARAM));
        }
    }

    @Action(FlightControllerKeys.REAL_NAME_SYSTEM_ENABLED)
    public void setRealNameSystemEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycDetection().setSubCmdId(DataFlycDetection.SubCmdId.SetDJIAppFlag).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                if (callback2 != null) {
                    callback2.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    callback2.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(FlightControllerKeys.REAL_NAME_SYSTEM_ENABLED)
    public void getRealNameSystemEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycDetection dataFlycDetection = new DataFlycDetection(null);
        dataFlycDetection.setSubCmdId(DataFlycDetection.SubCmdId.GetDJIAppFlag).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                if (callback2 != null) {
                    callback2.onSuccess(Boolean.valueOf(dataFlycDetection.getDJIAppFlag()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    callback2.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(FlightControllerKeys.BINDING_STATE)
    public void getRealNameSystemBindingState(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycSetGetVerPhone verPhone = new DataFlycSetGetVerPhone();
        verPhone.setCmdType(DataFlycSetGetVerPhone.VerPhoneCmdType.GET);
        verPhone.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass9 */

            public void onSuccess(Object model) {
                DataFlycSetGetVerPhone.FlycPhoneStatus flycStatus = verPhone.getPhoneFlag();
                String phoneNum = verPhone.getPhone();
                AircraftBindingState state = AircraftBindingState.UNKNOWN;
                switch (AnonymousClass106.$SwitchMap$dji$midware$data$model$P3$DataFlycSetGetVerPhone$FlycPhoneStatus[flycStatus.ordinal()]) {
                    case 1:
                        state = AircraftBindingState.INITIAL;
                        break;
                    case 2:
                        state = AircraftBindingState.BOUND;
                        break;
                    case 3:
                        state = AircraftBindingState.NOT_REQUIRED;
                        break;
                    case 4:
                        state = AircraftBindingState.UNBOUND;
                        break;
                }
                Object[] data = {state, phoneNum};
                if (callback2 != null) {
                    callback2.onSuccess(data);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    callback2.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(FlightControllerKeys.BINDING_STATE)
    public void setRealNameSystemBindingState(Object[] data2, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (data2 == null || data2.length < 2) {
            callback2.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        } else if (!(data2[0] instanceof AircraftBindingState) || !(data2[1] instanceof String)) {
            callback2.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataFlycSetGetVerPhone.FlycPhoneStatus status = DataFlycSetGetVerPhone.FlycPhoneStatus.Unknown;
            switch ((AircraftBindingState) data2[0]) {
                case UNBOUND:
                case UNBOUND_BUT_CANNOT_SYNC:
                    status = DataFlycSetGetVerPhone.FlycPhoneStatus.NeedBind;
                    break;
                case NOT_REQUIRED:
                    status = DataFlycSetGetVerPhone.FlycPhoneStatus.NotBind;
                    break;
                case BOUND:
                    status = DataFlycSetGetVerPhone.FlycPhoneStatus.BindOk;
                    break;
                case INITIAL:
                case UNKNOWN:
                case NOT_SUPPORTED:
                    status = DataFlycSetGetVerPhone.FlycPhoneStatus.Unknown;
                    break;
            }
            DataFlycSetGetVerPhone verPhone = new DataFlycSetGetVerPhone();
            verPhone.setCmdType(DataFlycSetGetVerPhone.VerPhoneCmdType.SET);
            verPhone.setPhoneFlag(status);
            verPhone.setPhone((String) data2[1]);
            verPhone.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass10 */

                public void onSuccess(Object model) {
                    if (callback2 != null) {
                        callback2.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback2 != null) {
                        callback2.onFails(DJIError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    private void getSubIMUCalibrateState(final IMUState state2, final int progress, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycGetParams paramGetter = new DataFlycGetParams();
        if (state2.getSubIMUState() == null || state2.getSubIMUState().length <= 1) {
            if (state2.getSubIMUState() == null) {
                paramGetter.setInfos(this.IMU_CALC_STAT_CONFIG_ONLY_ONE).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass13 */

                    public void onSuccess(Object model) {
                        FlightControllerAbstraction.this.readTheOnlyOneImuState(state2);
                        FlightControllerAbstraction.this.notifyRedundancySensorUsedState();
                        CallbackUtils.onSuccess(callback2, state2);
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
                    }
                });
            }
        } else if (state2.getSubIMUState().length >= 3) {
            paramGetter.setInfos(this.IMU_CALC_STAT_CONFIG_WITH_THREE).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    FlightControllerAbstraction.this.readFirstImuState(state2, progress);
                    FlightControllerAbstraction.this.readSecondImuState(state2, progress);
                    FlightControllerAbstraction.this.readThirdImuState(state2, progress);
                    FlightControllerAbstraction.this.notifyRedundancySensorUsedState();
                    CallbackUtils.onSuccess(callback2, state2);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
                }
            });
        } else if (state2.getSubIMUState().length == 2) {
            paramGetter.setInfos(this.IMU_CALC_STAT_CONFIG_WITH_TWO).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass12 */

                public void onSuccess(Object model) {
                    FlightControllerAbstraction.this.readFirstImuState(state2, progress);
                    FlightControllerAbstraction.this.readSecondImuState(state2, progress);
                    FlightControllerAbstraction.this.notifyRedundancySensorUsedState();
                    CallbackUtils.onSuccess(callback2, state2);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void readTheOnlyOneImuState(IMUState state2) {
        SensorState gyroState;
        SensorState acceState;
        int progress = DJIFlycParamInfoManager.valueOf("imu_app_temp_cali.cali_cnt_0").intValue();
        CalibrationState imuCaliStatus = updateIMUCaliStatus("imu_app_temp_cali.state_0");
        DJILog.logWriteI("IMUCalibrationLog", "[FlightControllerAbstraction] readTheOnlyOneImuState: progress " + progress + ", imuCaliStatus " + imuCaliStatus, "IMUCalibration", new Object[0]);
        if (imuCaliStatus.equals(CalibrationState.CALIBRATING)) {
            gyroState = SensorState.CALIBRATING;
            acceState = SensorState.CALIBRATING;
        } else if (DataOsdGetPushCommon.getInstance().isImuInitError()) {
            gyroState = SensorState.DATA_EXCEPTION;
            acceState = SensorState.DATA_EXCEPTION;
        } else if (!DataOsdGetPushCommon.getInstance().isImuPreheatd()) {
            gyroState = SensorState.WARMING_UP;
            acceState = SensorState.WARMING_UP;
        } else {
            gyroState = SensorState.NORMAL_BIAS;
            acceState = SensorState.NORMAL_BIAS;
        }
        float gyroValue = getGyroValue(gyroState, this.IMU_CALC_STAT_CONFIG_ONLY_ONE[2]);
        float acceValue = getAcceValue(acceState, this.IMU_CALC_STAT_CONFIG_ONLY_ONE[3]);
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(0);
        notifyValueChangeForKeyPath(gyroState, builder.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
        notifyValueChangeForKeyPath(acceState, builder.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
        notifyValueChangeForKeyPath(Integer.valueOf(progress), builder.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
        notifyValueChangeForKeyPath(imuCaliStatus, builder.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
        notifyValueChangeForKeyPath(Float.valueOf(gyroValue), builder.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_VALUE).build());
        notifyValueChangeForKeyPath(Float.valueOf(acceValue), builder.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_VALUE).build());
        if (state2 != null) {
            state2.setIndex(0);
            state2.setGyroscopeState(gyroState);
            state2.setAccelerometerState(acceState);
            state2.setCalibrationProgress(progress);
            state2.setCalibrationState(imuCaliStatus);
            state2.setGyroscopeValue(gyroValue);
            state2.setAccelerometerValue(acceValue);
            state2.getSubIMUState()[0].setIndex(0);
            state2.getSubIMUState()[0].setGyroscopeState(gyroState);
            state2.getSubIMUState()[0].setAccelerometerState(acceState);
            state2.getSubIMUState()[0].setCalibrationProgress(progress);
            state2.getSubIMUState()[0].setCalibrationState(imuCaliStatus);
            state2.getSubIMUState()[0].setGyroscopeValue(gyroValue);
            state2.getSubIMUState()[0].setAccelerometerValue(acceValue);
        }
    }

    /* access modifiers changed from: private */
    public void readFirstImuState(IMUState state2, int progress) {
        CalibrationState imuCaliStatus0;
        boolean z;
        SensorState gyroState0 = SensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[0].gyr_stat_0").value.intValue());
        float gyroValue0 = getGyroValue(gyroState0, "g_config.fdi_sensor[0].gyr_bias_0");
        SensorState acceState0 = SensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[0].acc_stat_0").value.intValue());
        float acceValue0 = getAcceValue(acceState0, "g_config.fdi_sensor[0].acc_bias_0");
        int progress0 = DJIFlycParamInfoManager.read("g_status.acc_gyro[0].cali_cnt_0").value.byteValue();
        if (progress > 0) {
            progress0 = progress;
        }
        if (this.internalFCVerion >= 16) {
            imuCaliStatus0 = updateIMUCaliStatus("imu_app_temp_cali.state_0");
        } else {
            imuCaliStatus0 = updateIMUCaliStatus(ParamCfgName.GSTATUS_GYRACC_STATE_0);
        }
        DJISDKCacheKey.Builder imuBuilder0 = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(0);
        notifyValueChangeForKeyPath(gyroState0, imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
        notifyValueChangeForKeyPath(acceState0, imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
        notifyValueChangeForKeyPath(Integer.valueOf(progress0), imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
        notifyValueChangeForKeyPath(imuCaliStatus0, imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
        notifyValueChangeForKeyPath(Float.valueOf(gyroValue0), imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_VALUE).build());
        notifyValueChangeForKeyPath(Float.valueOf(acceValue0), imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_VALUE).build());
        if (state2 != null) {
            state2.getSubIMUState()[0].setGyroscopeState(gyroState0);
            state2.getSubIMUState()[0].setAccelerometerState(acceState0);
            IMUState iMUState = state2.getSubIMUState()[0];
            if (gyroState0 == SensorState.DISCONNECTED || acceState0 == SensorState.DISCONNECTED) {
                z = false;
            } else {
                z = true;
            }
            iMUState.setIsConnected(z);
            state2.getSubIMUState()[0].setCalibrationProgress(progress0);
            state2.getSubIMUState()[0].setCalibrationState(imuCaliStatus0);
            state2.getSubIMUState()[0].setGyroscopeValue(gyroValue0);
            state2.getSubIMUState()[0].setAccelerometerValue(acceValue0);
        }
    }

    /* access modifiers changed from: private */
    public void readSecondImuState(IMUState state2, int progress) {
        boolean z = false;
        SensorState gyroState1 = SensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[1].gyr_stat_0").value.intValue());
        float gyroValue1 = getGyroValue(gyroState1, "g_config.fdi_sensor[1].gyr_bias_0");
        SensorState acceState1 = SensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[1].acc_stat_0").value.intValue());
        float acceValue1 = getAcceValue(acceState1, "g_config.fdi_sensor[1].acc_bias_0");
        int progress1 = DJIFlycParamInfoManager.read("g_status.acc_gyro[1].cali_cnt_0").value.byteValue();
        if (progress > 0) {
            progress1 = progress;
        }
        CalibrationState imuCaliStatus1 = updateIMUCaliStatus(ParamCfgName.GSTATUS_GYRACC_STATE_1);
        DJISDKCacheKey.Builder imuBuilder1 = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(1);
        notifyValueChangeForKeyPath(gyroState1, imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
        notifyValueChangeForKeyPath(acceState1, imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
        notifyValueChangeForKeyPath(Integer.valueOf(progress1), imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
        notifyValueChangeForKeyPath(imuCaliStatus1, imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
        notifyValueChangeForKeyPath(Float.valueOf(gyroValue1), imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_VALUE).build());
        notifyValueChangeForKeyPath(Float.valueOf(acceValue1), imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_VALUE).build());
        if (state2 != null) {
            state2.getSubIMUState()[1].setGyroscopeState(gyroState1);
            state2.getSubIMUState()[1].setAccelerometerState(acceState1);
            IMUState iMUState = state2.getSubIMUState()[1];
            if (!(gyroState1 == SensorState.DISCONNECTED || acceState1 == SensorState.DISCONNECTED)) {
                z = true;
            }
            iMUState.setIsConnected(z);
            state2.getSubIMUState()[1].setCalibrationProgress(progress1);
            state2.getSubIMUState()[1].setCalibrationState(imuCaliStatus1);
            state2.getSubIMUState()[1].setGyroscopeValue(gyroValue1);
            state2.getSubIMUState()[1].setAccelerometerValue(acceValue1);
        }
    }

    /* access modifiers changed from: private */
    public void readThirdImuState(IMUState state2, int progress) {
        boolean z = false;
        SensorState gyroState2 = SensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[2].gyr_stat_0").value.intValue());
        float gyroValue2 = getGyroValue(gyroState2, "g_config.fdi_sensor[1].gyr_bias_0");
        SensorState acceState2 = SensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[2].acc_stat_0").value.intValue());
        float acceValue2 = getAcceValue(acceState2, "g_config.fdi_sensor[1].acc_bias_0");
        int progress2 = DJIFlycParamInfoManager.read("g_status.acc_gyro[2].cali_cnt_0").value.byteValue();
        if (progress > 0) {
            progress2 = progress;
        }
        CalibrationState imuCaliStatus2 = updateIMUCaliStatus(ParamCfgName.GSTATUS_GYRACC_STATE_2);
        DJISDKCacheKey.Builder imuBuilder2 = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(2);
        notifyValueChangeForKeyPath(gyroState2, imuBuilder2.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
        notifyValueChangeForKeyPath(acceState2, imuBuilder2.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
        notifyValueChangeForKeyPath(Integer.valueOf(progress2), imuBuilder2.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
        notifyValueChangeForKeyPath(imuCaliStatus2, imuBuilder2.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
        notifyValueChangeForKeyPath(Float.valueOf(gyroValue2), imuBuilder2.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_VALUE).build());
        notifyValueChangeForKeyPath(Float.valueOf(acceValue2), imuBuilder2.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_VALUE).build());
        if (state2 != null) {
            state2.getSubIMUState()[2].setGyroscopeState(gyroState2);
            state2.getSubIMUState()[2].setAccelerometerState(acceState2);
            IMUState iMUState = state2.getSubIMUState()[2];
            if (!(gyroState2 == SensorState.DISCONNECTED || acceState2 == SensorState.DISCONNECTED)) {
                z = true;
            }
            iMUState.setIsConnected(z);
            state2.getSubIMUState()[2].setCalibrationProgress(progress2);
            state2.getSubIMUState()[2].setCalibrationState(imuCaliStatus2);
            state2.getSubIMUState()[2].setGyroscopeValue(gyroValue2);
            state2.getSubIMUState()[2].setAccelerometerValue(acceValue2);
        }
    }

    private float getGyroValue(SensorState gyroState, String index) {
        if (gyroState != SensorState.NORMAL_BIAS && gyroState != SensorState.MEDIUM_BIAS && gyroState != SensorState.LARGE_BIAS) {
            return -1.0f;
        }
        float gyroValue = Math.abs(DJIFlycParamInfoManager.valueOf(index).floatValue());
        if (gyroValue > 0.05f) {
            return 0.05f;
        }
        return gyroValue;
    }

    private float getAcceValue(SensorState acceState, String index) {
        if (acceState != SensorState.NORMAL_BIAS && acceState != SensorState.MEDIUM_BIAS && acceState != SensorState.LARGE_BIAS) {
            return -1.0f;
        }
        float acceValue = Math.abs(DJIFlycParamInfoManager.valueOf(index).floatValue());
        if (acceValue > 0.1f) {
            return 0.1f;
        }
        return acceValue;
    }

    /* access modifiers changed from: private */
    public void notifyRedundancySensorUsedState() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        DJISDKCacheKey key = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).paramKey(FlightControllerKeys.REDUNDANCY_SENSOR_USED_STATE).build();
        int rawValue = DJIFlycParamInfoManager.valueOf("g_status.ns_busy_dev_0").intValue();
        RedundancySensorUsedState.Builder baroIndex = new RedundancySensorUsedState.Builder().setNsIndex(rawValue & 3).setAccIndex((rawValue >> 2) & 3).setGyroIndex((rawValue >> 4) & 3).setMagIndex((rawValue >> 6) & 3).setGpsIndex((rawValue >> 8) & 3).setBaroIndex((rawValue >> 10) & 3);
        if (((rawValue >> 12) & 1) == 1) {
            z = true;
        } else {
            z = false;
        }
        RedundancySensorUsedState.Builder rTKUsed = baroIndex.setRTKUsed(z);
        if (((rawValue >> 13) & 1) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        RedundancySensorUsedState.Builder vOUsed = rTKUsed.setVOUsed(z2);
        if (((rawValue >> 14) & 1) == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        RedundancySensorUsedState.Builder uSUsed = vOUsed.setUSUsed(z3);
        if (((rawValue >> 15) & 1) != 1) {
            z4 = false;
        }
        notifyValueChangeForKeyPath(uSUsed.setRadarUsed(z4).build(), key);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        int maximumChannelSize;
        int offset;
        if (params.isGetted() && this.previousRCSwitchPosition != params.getMode()) {
            if (DJIRCAbstractionUtil.isRemoteControllerHaveTwoSwitchMode(DJIProductManager.getInstance().getType())) {
                maximumChannelSize = 2;
                offset = 1;
            } else {
                maximumChannelSize = 3;
                offset = 0;
            }
            RemoteControllerFlightMode[] modes = new RemoteControllerFlightMode[maximumChannelSize];
            for (int i = 0; i < maximumChannelSize; i++) {
                modes[i] = translateRcChannelIntoMode(DJIMcHelper.getInstance().getRcModeChannel(i + offset));
            }
            int modePosition = 0;
            HardwareState.FlightModeSwitch switchMode = HardwareState.FlightModeSwitch.find(DJIProductManager.getInstance().getType(), params.getMode());
            if (switchMode.value() < modes.length) {
                modePosition = switchMode.value();
            }
            notifyValueChangeForKeyPath(modes[modePosition], convertKeyToPath(FlightControllerKeys.CURRENT_MODE));
            this.previousRCSwitchPosition = params.getMode();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycPushForbidDataInfos dbInfo) {
        notifyValueChangeForKeyPath(dbInfo.getVersion(), FlightControllerKeys.DRONE_PRECISE_DB_VERSION);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushWayPointMissionInfo missionPushInfo) {
        if (missionPushInfo.getRecData() != null) {
            notifyValueChangeForKeyPath(Boolean.valueOf(missionPushInfo.isVelocityControl()), convertKeyToPath(FlightControllerKeys.MISSION_TRIPOD_VELOCITY_CTRL));
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon params) {
        GoHomeExecutionState goHomeStatus;
        double latitude;
        double longitude;
        boolean isGoingHome;
        if (params.getRecDataLen() != 0) {
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isVisionUsed()), convertKeyToPath(FlightControllerKeys.IS_VISION_POSITIONING_SENSOR_BEING_USED));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isGpsUsed()), convertKeyToPath(FlightControllerKeys.IS_GPS_BEING_USED));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isGPSValid()), convertKeyToPath(FlightControllerKeys.IS_GPS_VALID));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getFlycVersion()), convertKeyToPath(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
            this.internalFCVerion = params.getFlycVersion();
            notifyValueChangeForKeyPath(FlightMode.find(params.getFlycState().value()), convertKeyToPath(FlightControllerKeys.FLIGHT_MODE));
            DataOsdGetPushCommon.RcModeChannel tmpChannel = params.getModeChannel();
            if (tmpChannel != DataOsdGetPushCommon.RcModeChannel.CHANNEL_UNKNOWN) {
                notifyValueChangeForKeyPath(tmpChannel, FlightControllerKeys.RC_MODE_CHANNEL);
            }
            if (this.isBeginnerMode == 1) {
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.TRIPOD_MODE_ENABLED));
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.CINEMATIC_MODE_ENABLED));
            } else {
                notifyValueChangeForKeyPath(Boolean.valueOf(FlightMode.find(params.getFlycState().value()).equals(FlightMode.TRIPOD)), convertKeyToPath(FlightControllerKeys.TRIPOD_MODE_ENABLED));
                notifyValueChangeForKeyPath(Boolean.valueOf(FlightMode.find(params.getFlycState().value()).equals(FlightMode.CINEMATIC)), convertKeyToPath(FlightControllerKeys.CINEMATIC_MODE_ENABLED));
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getCompassError()), convertKeyToPath(FlightControllerKeys.COMPASS_HAS_ERROR));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isImuInitError()), convertKeyToPath(FlightControllerKeys.IMU_IS_INIT_ERROR));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isAllowImuInitfailReason()), convertKeyToPath(FlightControllerKeys.IMU_ALLOW_INIT_FAIL_REASON));
            notifyValueChangeForKeyPath(params.getIMUinitFailReason(), convertKeyToPath(FlightControllerKeys.IMU_INIT_FAIL_REASON));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getFlightAction().equals(DataOsdGetPushCommon.FLIGHT_ACTION.OUTOF_CONTROL_GOHOME)), convertKeyToPath(FlightControllerKeys.IS_FAIL_SAFE));
            notifyValueChangeForKeyPath(FlightAction.find(params.getFlightAction().value()), convertKeyToPath(FlightControllerKeys.FLIGHT_ACTION));
            notifyValueChangeForKeyPath(Boolean.valueOf(!params.isImuPreheatd()), convertKeyToPath(FlightControllerKeys.IS_IMU_PREHEATING));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getWaveError()), convertKeyToPath(FlightControllerKeys.ULTRASONIC_ERROR));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getFlyTime()), convertKeyToPath(FlightControllerKeys.FLY_TIME_IN_SECONDS));
            int[] result = getFlycModeResId(DataOsdGetPushCommon.getInstance().getFlycState(), DataOsdGetPushCommon.getInstance().isVisionUsed());
            GoHomeExecutionState goHomeExecutionState = GoHomeExecutionState.NOT_EXECUTING;
            if (result[0] == 2) {
                goHomeStatus = GoHomeExecutionState.GO_DOWN_TO_GROUND;
            } else if (result[0] != 4) {
                goHomeStatus = GoHomeExecutionState.NOT_EXECUTING;
            } else if (params.getGohomeStatus().equals(DataOsdGetPushCommon.GOHOME_STATUS.ASCENDING)) {
                goHomeStatus = GoHomeExecutionState.GO_UP_TO_HEIGHT;
            } else {
                goHomeStatus = GoHomeExecutionState.find(params.getGohomeStatus().value());
            }
            if (this.lastGoHomeStatus == GoHomeExecutionState.AUTO_FLY_TO_HOME_POINT && goHomeStatus == GoHomeExecutionState.TURN_DIRECTION_TO_HOME_POINT) {
                goHomeStatus = GoHomeExecutionState.AUTO_FLY_TO_HOME_POINT;
            }
            if (this.lastGoHomeStatus == GoHomeExecutionState.GO_DOWN_TO_GROUND && goHomeStatus == GoHomeExecutionState.NOT_EXECUTING) {
                goHomeStatus = GoHomeExecutionState.COMPLETED;
            }
            notifyValueChangeForKeyPath(goHomeStatus, convertKeyToPath(FlightControllerKeys.GO_HOME_STATUS));
            this.lastGoHomeStatus = goHomeStatus;
            notifyValueChangeForKeyPath(Integer.valueOf(params.getGpsNum()), convertKeyToPath(FlightControllerKeys.SATELLITE_COUNT));
            if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 6 || isOldMC()) {
                notifyValueChangeForKeyPath(GPSSignalLevel.find(getGpsLevelForOldFlightController(params.getGpsNum())), convertKeyToPath(FlightControllerKeys.GPS_SIGNAL_LEVEL));
            } else {
                notifyValueChangeForKeyPath(GPSSignalLevel.find(params.getGpsLevel()), convertKeyToPath(FlightControllerKeys.GPS_SIGNAL_LEVEL));
            }
            boolean gpsValid = CommonUtil.isDroneGpsValid();
            if (!gpsValid) {
                latitude = Double.NaN;
            } else {
                latitude = params.getLatitude();
            }
            notifyValueChangeForKeyPath(Double.valueOf(latitude), convertKeyToPath(FlightControllerKeys.AIRCRAFT_LOCATION_LATITUDE));
            if (!gpsValid) {
                longitude = Double.NaN;
            } else {
                longitude = params.getLongitude();
            }
            notifyValueChangeForKeyPath(Double.valueOf(longitude), convertKeyToPath(FlightControllerKeys.AIRCRAFT_LOCATION_LONGITUDE));
            notifyValueChangeForKeyPath(Float.valueOf(((float) params.getHeight()) * 0.1f), convertKeyToPath(FlightControllerKeys.ALTITUDE));
            this.latestValidAircraftLocation = new LocationCoordinate3D(latitude, longitude, ((float) params.getHeight()) / 10.0f);
            notifyValueChangeForKeyPath(this.latestValidAircraftLocation, convertKeyToPath(FlightControllerKeys.AIRCRAFT_LOCATION));
            if (params.getFlycState().equals(DataOsdGetPushCommon.FLYC_STATE.GoHome) || params.getFlycState().equals(DataOsdGetPushCommon.FLYC_STATE.AutoLanding)) {
                isGoingHome = true;
            } else {
                isGoingHome = false;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isGoingHome), convertKeyToPath(FlightControllerKeys.IS_GOING_HOME));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isMotorUp()), convertKeyToPath(FlightControllerKeys.ARE_MOTOR_ON));
            notifyValueChangeForKeyPath(Float.valueOf(((float) params.getXSpeed()) / 10.0f), convertKeyToPath(FlightControllerKeys.VELOCITY_X));
            notifyValueChangeForKeyPath(Float.valueOf(((float) params.getYSpeed()) / 10.0f), convertKeyToPath(FlightControllerKeys.VELOCITY_Y));
            notifyValueChangeForKeyPath(Float.valueOf(((float) params.getZSpeed()) / 10.0f), convertKeyToPath(FlightControllerKeys.VELOCITY_Z));
            int voltageWarningLevel = params.getVoltageWarning();
            notifyValueChangeForKeyPath(BatteryThresholdBehavior.find(voltageWarningLevel), convertKeyToPath(FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR));
            if (voltageWarningLevel > 1) {
                notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_LOWER_THAN_BATTERY_WARNING_THRESHOLD));
                notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_LOWER_THAN_SERIOUS_BATTERY_WARNING_THRESHOLD));
            } else if (voltageWarningLevel > 0) {
                notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_LOWER_THAN_BATTERY_WARNING_THRESHOLD));
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_LOWER_THAN_SERIOUS_BATTERY_WARNING_THRESHOLD));
            } else {
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_LOWER_THAN_BATTERY_WARNING_THRESHOLD));
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_LOWER_THAN_SERIOUS_BATTERY_WARNING_THRESHOLD));
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(params.groundOrSky() == 2), convertKeyToPath(FlightControllerKeys.IS_FLYING));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.AutoLanding), convertKeyToPath(FlightControllerKeys.IS_LANDING));
            notifyValueChangeForKeyPath(Double.valueOf(((double) params.getPitch()) / 10.0d), convertKeyToPath(FlightControllerKeys.ATTITUDE_PITCH));
            notifyValueChangeForKeyPath(Double.valueOf(((double) params.getRoll()) / 10.0d), convertKeyToPath(FlightControllerKeys.ATTITUDE_ROLL));
            notifyValueChangeForKeyPath(Double.valueOf(((double) params.getYaw()) / 10.0d), convertKeyToPath(FlightControllerKeys.ATTITUDE_YAW));
            notifyValueChangeForKeyPath(Float.valueOf(((float) params.getYaw()) / 10.0f), convertKeyToPath(FlightControllerKeys.COMPASS_HEADING));
            notifyValueChangeForKeyPath(getFlightModeString(FlightMode.find(params.getFlycState().value()), params.isVisionUsed(), params.getDroneType(), params.getModeChannel(), CommonUtil.isMultipleModeOpen()), convertKeyToPath(FlightControllerKeys.FLIGHT_MODE_STRING));
            if (DJIProductManager.getInstance().getType().equals(ProductType.litchiC)) {
                notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_ULTRASONIC_BEING_USED));
            } else {
                notifyValueChangeForKeyPath(Boolean.valueOf(params.isSwaveWork()), convertKeyToPath(FlightControllerKeys.IS_ULTRASONIC_BEING_USED));
            }
            if (params.isSwaveWork()) {
                notifyValueChangeForKeyPath(Float.valueOf(((float) params.getSwaveHeight()) * 0.1f), convertKeyToPath(FlightControllerKeys.ULTRASONIC_HEIGHT_IN_METERS));
            } else {
                notifyValueChangeForKeyPath(Float.valueOf(0.0f), convertKeyToPath(FlightControllerKeys.ULTRASONIC_HEIGHT_IN_METERS));
            }
            boolean isRebootInSMode = params.getModeChannel() != DataOsdGetPushCommon.RcModeChannel.CHANNEL_P && DataOsdGetPushCommon.FLYC_STATE.GPS_Atti.equals(params.getFlycState()) && DataOsdGetPushHome.getInstance().isMultipleModeOpen();
            if (isRebootInSMode) {
                this.isRebootInSModeCounter++;
            } else {
                this.isRebootInSModeCounter = 0;
            }
            notifyValueChangeForKeyPath(Boolean.valueOf(isRebootInSMode && this.isRebootInSModeCounter > 20), KeyHelper.getFlightControllerKey(FlightControllerKeys.IS_REBOOT_IN_S_MODE));
            notifyValueChangeForKeyPath(MotorStartFailedState.find(params.getMotorFailedCause().relValue()), convertKeyToPath(FlightControllerKeys.MOTOR_START_FAILED_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getMotorStartCauseNoStartAction() == DataOsdGetPushCommon.MotorStartFailedCause.LOCK_BY_APP), convertKeyToPath(FlightControllerKeys.IS_MOTORS_LOCKED_BY_APP));
            notifyValueChangeForKeyPath(Boolean.valueOf(DataOsdGetPushCommon.FLYC_STATE.AutoLanding == params.getFlycState() || DataOsdGetPushCommon.FLYC_STATE.AttiLangding == params.getFlycState() || DataOsdGetPushCommon.FLYC_STATE.FORCE_LANDING == params.getFlycState()), convertKeyToPath(FlightControllerKeys.IS_IN_LANDING_MODE));
        }
    }

    public static String getFlightModeString(FlightMode mode, boolean isVisualWork, DataOsdGetPushCommon.DroneType droneType, DataOsdGetPushCommon.RcModeChannel channel, boolean open) {
        String resIds = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.NoFlyc) {
            return resIds;
        }
        boolean useP = CommonUtil.useModePToSmart(null);
        if (FlightMode.MANUAL == mode) {
            resIds = "Manual";
        } else if (FlightMode.ATTI == mode) {
            resIds = "Atti";
        } else if (FlightMode.ATTI_COURSE_LOCK == mode) {
            resIds = "Atti";
        } else if (FlightMode.ATTI_HOVER == mode) {
            resIds = "Atti";
        } else if (FlightMode.ATTI_LIMITED == mode) {
            resIds = "Atti";
        } else if (FlightMode.ATTI_LANDING == mode) {
            resIds = "Landing";
        } else if (FlightMode.AUTO_LANDING == mode) {
            resIds = "Landing";
        } else if (FlightMode.ASSISTED_TAKEOFF == mode) {
            resIds = FlightControllerKeys.TAKE_OFF;
        } else if (FlightMode.AUTO_TAKEOFF == mode) {
            resIds = FlightControllerKeys.TAKE_OFF;
        } else if (FlightMode.GO_HOME == mode) {
            resIds = FlightControllerKeys.START_GO_HOME;
        } else if (FlightMode.JOYSTICK == mode) {
            resIds = "Joystick";
        } else if (FlightMode.GPS_ATTI == mode) {
            resIds = "P-GPS";
        } else if (FlightMode.GPS_BLAKE == mode) {
            resIds = "P-GPS";
        } else if (FlightMode.HOVER == mode) {
            resIds = "P-GPS";
        } else if (FlightMode.GPS_COURSE_LOCK == mode) {
            if (useP) {
                resIds = "CL";
            } else if (DataOsdGetPushCommon.DroneType.A2 == droneType) {
                resIds = "P-CL";
            } else {
                resIds = "F-CL";
            }
        } else if (FlightMode.GPS_HOME_LOCK == mode) {
            if (useP) {
                resIds = "HL";
            } else if (DataOsdGetPushCommon.DroneType.A2 == droneType) {
                resIds = "P-HL";
            } else {
                resIds = "F-HL";
            }
        } else if (FlightMode.GPS_HOT_POINT == mode) {
            if (useP) {
                resIds = "POI";
            } else {
                resIds = "F-POI";
            }
        } else if (FlightMode.GPS_WAYPOINT == mode) {
            if (useP) {
                resIds = "WP";
            } else {
                resIds = "F-WP";
            }
        } else if (FlightMode.CLICK_GO == mode) {
            resIds = "P-GPS";
        } else if (FlightMode.GPS_FOLLOW_ME == mode) {
            if (useP) {
                resIds = "FM";
            } else {
                resIds = "F-FM";
            }
        } else if (FlightMode.ACTIVE_TRACK == mode || FlightMode.TRACK_SPOTLIGHT == mode) {
            if ((FlightMode.ACTIVE_TRACK != mode || !DataEyeGetPushException.getInstance().isMultiQuickShotEnabled()) && !DataEyeGetPushException.getInstance().isQuickMovieExecuting()) {
                resIds = "ActiveTrack";
            } else {
                resIds = "QuickShot";
            }
        } else if (FlightMode.TAP_FLY == mode) {
            if (DataEyeGetPushException.getInstance().isInDraw()) {
                resIds = "Draw";
            } else {
                resIds = "TapFly";
            }
        } else if (FlightMode.PANO == mode) {
            resIds = "Pano";
        } else if (FlightMode.FARMING == mode) {
            resIds = "Farming";
        } else if (FlightMode.FPV == mode) {
            resIds = "P-GPS";
        } else if (FlightMode.GPS_SPORT == mode) {
            resIds = "Sport";
        } else if (FlightMode.GPS_NOVICE == mode) {
            resIds = "Beginner";
        } else if (FlightMode.CONFIRM_LANDING == mode) {
            resIds = "Landing";
        } else if (FlightMode.TERRAIN_FOLLOW == mode) {
            resIds = "TerrainTracking";
        } else if (FlightMode.PALM_CONTROL == mode) {
            resIds = "Palm Control";
        } else if (FlightMode.QUICK_SHOT == mode) {
            resIds = "QuickShot";
        } else if (FlightMode.TRIPOD == mode) {
            resIds = "Tripod";
        } else if (FlightMode.DETOUR == mode) {
            resIds = "APAS";
        } else if (FlightMode.MOTORS_JUST_STARTED == mode) {
            resIds = FlightControllerKeys.TAKE_OFF;
        } else if (FlightMode.DRAW == mode) {
            resIds = "Draw";
        } else if (FlightMode.CINEMATIC == mode) {
            resIds = "Cinematic";
        } else if (FlightMode.TIME_LAPSE == mode) {
            resIds = "HyperLapse";
        } else if (FlightMode.POI2 == mode) {
            resIds = "POI";
        }
        if (resIds.equals("P-GPS")) {
            if (isVisualWork) {
                resIds = "OPTI";
            } else if (useP) {
                resIds = "GPS";
            } else if (open && DataOsdGetPushCommon.RcModeChannel.CHANNEL_F == channel) {
                resIds = "F-GPS";
            }
        } else if (resIds.equals("Atti") && !useP && (!open || channel != DataOsdGetPushCommon.RcModeChannel.CHANNEL_A)) {
            resIds = "P-Atti";
        }
        if (CommonUtil.isHardcodedPositionRequired() && (resIds.equals("OPTI") || resIds.equals("GPS"))) {
            resIds = "Position";
        }
        return resIds;
    }

    private static boolean supportNavigationOnNormalMode(DataOsdGetPushCommon.DroneType droneType) {
        if (droneType == DataOsdGetPushCommon.DroneType.P4 || droneType == DataOsdGetPushCommon.DroneType.wm220 || droneType == DataOsdGetPushCommon.DroneType.Orange2 || droneType == DataOsdGetPushCommon.DroneType.M200 || droneType == DataOsdGetPushCommon.DroneType.M210 || droneType == DataOsdGetPushCommon.DroneType.M210RTK || droneType == DataOsdGetPushCommon.DroneType.PM420 || droneType == DataOsdGetPushCommon.DroneType.PM420PRO || droneType == DataOsdGetPushCommon.DroneType.PM420PRO_RTK || droneType == DataOsdGetPushCommon.DroneType.Pomato || droneType == DataOsdGetPushCommon.DroneType.Potato) {
            return true;
        }
        return false;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome params) {
        CompassCalibrationState compassCalibrationStatus;
        int i;
        if (params.getRecDataLen() != 0) {
            notifyValueChangeForKeyPath(Integer.valueOf(params.getForceLandingHeight()), convertKeyToPath(FlightControllerKeys.FORCE_LANDING_HEIGHT));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isCompassCeleing()), convertKeyToPath(FlightControllerKeys.COMPASS_IS_CALIBRATING));
            notifyValueChangeForKeyPath(params.getMotorEscmState(), convertKeyToPath(FlightControllerKeys.MOTOR_ESCM_STATE));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getForceLandingHeight()), convertKeyToPath(FlightControllerKeys.FORCE_LANDING_HEIGHT));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isFlycInNavigationMode()), convertKeyToPath(FlightControllerKeys.NAVIGATION_MODE_ENABLED));
            notifyValueChangeForKeyPath(Float.valueOf(params.getHeight()), convertKeyToPath(FlightControllerKeys.TAKEOFF_LOCATION_ALTITUDE));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getFlycLogIndex()), convertKeyToPath(FlightControllerKeys.FLIGHT_LOG_INDEX));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isFlycInNavigationMode()), convertKeyToPath(FlightControllerKeys.VIRTUAL_STICK_CONTROL_MODE_ENABLED));
            CompassCalibrationState compassCalibrationState = CompassCalibrationState.NOT_CALIBRATING;
            if (!params.isCompassCeleing()) {
                compassCalibrationStatus = CompassCalibrationState.NOT_CALIBRATING;
                if (!this.hasCompassCalibrationRecorded && (this.lastCalibrationStatus == CompassCalibrationState.VERTICAL || this.lastCalibrationStatus == CompassCalibrationState.SUCCESSFUL)) {
                    compassCalibrationStatus = CompassCalibrationState.SUCCESSFUL;
                }
                this.hasCompassCalibrationRecorded = true;
                this.lastCalibrationStatus = CompassCalibrationState.NOT_CALIBRATING;
            } else {
                compassCalibrationStatus = CompassCalibrationState.find(params.getCompassCeleStatus());
                this.lastCalibrationStatus = CompassCalibrationState.find(params.getCompassCeleStatus());
                this.hasCompassCalibrationRecorded = false;
            }
            notifyValueChangeForKeyPath(compassCalibrationStatus, convertKeyToPath(FlightControllerKeys.COMPASS_CALIBRATION_STATUS));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getGoHomeHeight()), convertKeyToPath(FlightControllerKeys.GO_HOME_HEIGHT_IN_METERS));
            double latitude = params.getLatitude();
            double longitude = params.getLongitude();
            if (LocationUtils.validateLatitude(latitude) || LocationUtils.validateLongitude(longitude)) {
                latitude = HOME_LOC_REMOTE_NOT_RECORD.getLatitude();
                longitude = HOME_LOC_REMOTE_NOT_RECORD.getLongitude();
            }
            notifyValueChangeForKeyPath(Double.valueOf(latitude), convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LATITUDE));
            notifyValueChangeForKeyPath(Double.valueOf(longitude), convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LONGITUDE));
            notifyValueChangeForKeyPath(new LocationCoordinate2D(latitude, longitude), convertKeyToPath(FlightControllerKeys.HOME_LOCATION));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isHomeRecord()), convertKeyToPath(FlightControllerKeys.IS_HOME_LOCATION_SET));
            if (params.isHomeRecord()) {
                if (!this.isHomePointAltitudeSetted) {
                    notifyValueChangeForKeyPath(Float.valueOf(DataOsdGetPushHome.getInstance().getHeight()), convertKeyToPath(FlightControllerKeys.HOME_POINT_ALTITUDE));
                    this.isHomePointAltitudeSetted = true;
                }
                notifyValueChangeForKeyPath(Double.valueOf(latitude), convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LATITUDE));
                notifyValueChangeForKeyPath(Double.valueOf(longitude), convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LONGITUDE));
                notifyValueChangeForKeyPath(new LocationCoordinate2D(latitude, longitude), convertKeyToPath(FlightControllerKeys.HOME_LOCATION));
            } else {
                notifyValueChangeForKeyPath(Float.valueOf(Float.NaN), convertKeyToPath(FlightControllerKeys.HOME_POINT_ALTITUDE));
            }
            notifyValueChangeForKeyPath(params.isIOCEnabled() ? FlightOrientationMode.find(params.getIOCMode().value()) : FlightOrientationMode.AIRCRAFT_HEADING, convertKeyToPath(FlightControllerKeys.ORIENTATION_MODE));
            notifyValueChangeForKeyPath(Short.valueOf(params.getCourseLockAngle()), convertKeyToPath(FlightControllerKeys.COURSE_LOCK_ANGLE));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isMultipleModeOpen()), convertKeyToPath(FlightControllerKeys.MULTI_MODE_OPEN));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isReatchLimitDistance()), convertKeyToPath(FlightControllerKeys.HAS_REACHED_MAX_FLIGHT_RADIUS));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isReatchLimitHeight()), convertKeyToPath(FlightControllerKeys.HAS_REACHED_MAX_FLIGHT_HEIGHT));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isAboveWater()), convertKeyToPath(FlightControllerKeys.IS_ABOVE_WATER));
            boolean impactInAir = params.isDetectImpactInAir();
            if (impactInAir && !this.mImpactInAir) {
                notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IMPACT_IN_AIR_DETECTED));
            }
            this.mImpactInAir = impactInAir;
            if (params.isBeginnerMode()) {
                i = 1;
            } else {
                i = 0;
            }
            this.isBeginnerMode = i;
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isBeginnerMode()), convertKeyToPath(FlightControllerKeys.NOVICE_MODE_ENABLED));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isMainGPSSignalSheltered()), convertKeyToPath(FlightControllerKeys.MAIN_GPS_SIGNAL_SHELTERED));
            if (params.isBigGaleWarning()) {
                notifyValueChangeForKeyPath(FlightWindWarning.LEVEL_2, convertKeyToPath(FlightControllerKeys.FLIGHT_WIND_WARNING));
            } else if (params.isBigGale()) {
                notifyValueChangeForKeyPath(FlightWindWarning.LEVEL_1, convertKeyToPath(FlightControllerKeys.FLIGHT_WIND_WARNING));
            } else {
                notifyValueChangeForKeyPath(FlightWindWarning.LEVEL_0, convertKeyToPath(FlightControllerKeys.FLIGHT_WIND_WARNING));
            }
            if (params.isHomeRecord()) {
                DJILatLng djiLatLng = new DJILatLng(params.getLatitude(), params.getLongitude());
                if (djiLatLng.isAvailable() && !this.lastHomeLatLng.equals(djiLatLng) && LocationUtils.getDistance(djiLatLng, this.lastHomeLatLng) > 1.0f) {
                    if (this.lastHomeLatLng.isAvailable()) {
                        notifyValueChangeForKeyPath(HomePointState.UPDATE, convertKeyToPath(FlightControllerKeys.HOME_POINT_STATE));
                    } else {
                        notifyValueChangeForKeyPath(HomePointState.RECORD, convertKeyToPath(FlightControllerKeys.HOME_POINT_STATE));
                    }
                    this.lastHomeLatLng = djiLatLng;
                }
            }
        }
        this.hasSimulatorStarted = params.isFlycInSimulationMode();
        notifyValueChangeForKeyPath(Boolean.valueOf(this.hasSimulatorStarted), convertKeyToPath(FlightControllerKeys.IS_SIMULATOR_ACTIVE));
        if (this.hasSimulatorStarted) {
            if (!(this.state == SimulatorInternalState.STARTING || this.state == SimulatorInternalState.STOPPING)) {
                this.state = SimulatorInternalState.RUNNING;
            }
        } else if (this.state != SimulatorInternalState.STARTING && this.state != SimulatorInternalState.RESPONSE_RECEIVED) {
            this.state = SimulatorInternalState.STOPPED;
        } else {
            return;
        }
        checkStartSimulate();
        checkStopSimulate();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushGoHomeCountDown params) {
        notifyValueChangeForKeyPath(params.getActionType(), convertKeyToPath(FlightControllerKeys.GO_HOME_ACTION_TYPE));
        notifyValueChangeForKeyPath(Integer.valueOf(params.getNewCountingInSec()), convertKeyToPath(FlightControllerKeys.GO_HOME_ACTION_COUNT_DOWN_TIME));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushSmartBattery params) {
        int timeNeededToGoHome;
        if (params.getRecDataLen() != 0) {
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getGoHomeStatus().value() > 0), convertKeyToPath(FlightControllerKeys.AIRCRAFT_SHOULD_GO_HOME));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getGoHomeBattery()), convertKeyToPath(FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getGoHomeCountDown()), convertKeyToPath(FlightControllerKeys.AIRCRAFT_GO_HOME_COUNT_DOWN));
            notifyValueChangeForKeyPath(Float.valueOf(params.getSafeFlyRadius()), convertKeyToPath(FlightControllerKeys.MAX_RADIUS_AIRCRAFT_CAN_FLY_AND_GO_HOME));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getUsefulTime()), convertKeyToPath(FlightControllerKeys.REMAINING_FLIGHT_TIME));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getLandTime()), convertKeyToPath(FlightControllerKeys.TIME_NEEDED_TO_LAND_FROM_CURRENT_HEIGHT));
            notifyValueChangeForKeyPath(Integer.valueOf(params.getLowWarning()), FlightControllerKeys.LOW_BATTERY_WARNING_THRESHOLD);
            notifyValueChangeForKeyPath(Integer.valueOf(params.getSeriousLowWarning()), FlightControllerKeys.SERIOUS_LOW_BATTERY_WARNING_THRESHOLD);
            notifyValueChangeForKeyPath(Integer.valueOf(params.getGoHomeBattery()), FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME);
            notifyValueChangeForKeyPath(Integer.valueOf(params.getLandBattery()), FlightControllerKeys.CURRENT_LAND_IMMEDIATELY_BATTERY);
            if (DataOsdGetPushCommon.getInstance().getFlycState().equals(DataOsdGetPushCommon.FLYC_STATE.AutoLanding)) {
                timeNeededToGoHome = params.getLandTime();
            } else {
                timeNeededToGoHome = params.getGoHomeTime();
            }
            notifyValueChangeForKeyPath(Integer.valueOf(timeNeededToGoHome), convertKeyToPath(FlightControllerKeys.TIME_NEEDED_TO_GO_HOME));
            notifyValueChangeForKeyPath(new GoHomeAssessment.Builder().batteryPercentageNeededToGoHome(params.getGoHomeBattery()).maxRadiusAircraftCanFlyAndGoHome(params.getSafeFlyRadius()).remainingFlightTime(params.getUsefulTime()).timeNeededToLandFromCurrentHeight(params.getLandTime()).timeNeededToGoHome(timeNeededToGoHome).batteryPercentageNeededToLandFromCurrentHeight(params.getLandBattery()).smartRTHState(SmartRTHState.getSmartRTHStateFromBattery(params)).smartRTHCountdown(params.getGoHomeCountDown()).build(), convertKeyToPath(FlightControllerKeys.GO_HOME_ASSESSMENT));
        }
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataCommonGetVersion dcgv = new DataCommonGetVersion();
        dcgv.setDeviceType(DeviceType.FLYC);
        dcgv.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass14 */

            public void onSuccess(Object model) {
                String firmVersion = dcgv.getFirmVer(".");
                if (callback2 == null) {
                    return;
                }
                if (!TextUtils.isEmpty(firmVersion)) {
                    callback2.onSuccess(firmVersion);
                } else {
                    callback2.onFails(DJIError.UNABLE_TO_GET_FIRMWARE_VERSION);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    callback2.onFails(DJIFlightControllerError.getDJIError(ccode));
                }
            }
        });
    }

    private String[] getCompassSensorParam() {
        String[] result = new String[(this.compassCount + this.compassCount + 1)];
        System.arraycopy(this.COMPASS_MAG_OVER_CONFIG, 0, result, 0, this.compassCount);
        System.arraycopy(this.COMPASS_MAG_STAT_CONFIG, 0, result, this.compassCount, this.compassCount);
        result[result.length - 1] = "g_status.ns_busy_dev_0";
        return result;
    }

    @Getter(FlightControllerKeys.COMPASS_STATE)
    public void getCompassState(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        new DataFlycGetParams().setInfos(getCompassSensorParam()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass15 */

            public void onSuccess(Object model) {
                FlightControllerAbstraction.this.notifyRedundancySensorUsedState();
                CallbackUtils.onSuccess(innerCallback, FlightControllerAbstraction.this.readCompassSensorState());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public CompassState readCompassSensorState() {
        CompassState compassState = new CompassState(this.compassCount);
        if (this.compassCount > 0) {
            CompassSensorState state0 = CompassSensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[0].mag_stat_0").value.intValue());
            int value0 = getCompassSensorValue(state0, "g_config.fdi_sensor[0].mag_over_0");
            compassState.getSubCompassStates()[0].setSensorState(state0);
            compassState.getSubCompassStates()[0].setSensorValue(value0);
        }
        if (this.compassCount > 1) {
            CompassSensorState state1 = CompassSensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[1].mag_stat_0").value.intValue());
            int value1 = getCompassSensorValue(state1, "g_config.fdi_sensor[1].mag_over_0");
            compassState.getSubCompassStates()[1].setSensorState(state1);
            compassState.getSubCompassStates()[1].setSensorValue(value1);
        }
        if (this.compassCount > 2) {
            CompassSensorState state2 = CompassSensorState.find(DJIFlycParamInfoManager.read("g_config.fdi_sensor[2].mag_stat_0").value.intValue());
            int value2 = getCompassSensorValue(state2, "g_config.fdi_sensor[2].mag_over_0");
            compassState.getSubCompassStates()[2].setSensorState(state2);
            compassState.getSubCompassStates()[2].setSensorValue(value2);
        }
        notifyValueChangeForKeyPath(compassState, FlightControllerKeys.COMPASS_STATE);
        return compassState;
    }

    private int getCompassSensorValue(CompassSensorState state2, String index) {
        if (state2 != CompassSensorState.NORMAL_MODULUS && state2 != CompassSensorState.WEAK_MODULUS && state2 != CompassSensorState.SERIOUS_MODULUS) {
            return -1;
        }
        int value = Math.abs(DJIFlycParamInfoManager.valueOf(index).intValue());
        if (value > 999) {
            return 999;
        }
        return value;
    }

    @Getter(FlightControllerKeys.REDUNDANCY_COMPASS_STATUS)
    public void getRedundancyCompassStatus(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.mergeGetFlycParamInfo.getInfo(ParamCfgName.GSTATUS_TOPOLOGY_VERIFY_MAG, new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass16 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(innerCallback, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.STRUCT_GAP_TIME)
    public void getTotalStartTime(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        new DataFlycGetParams().setInfos(this.structCheckIndexs).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass17 */

            public void onSuccess(Object model) {
                float totalStartTime = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_USER_INFO_TOTAL_MOTOR_START_TIME).value.floatValue();
                CallbackUtils.onSuccess(innerCallback, new float[]{totalStartTime, totalStartTime - DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_USER_INFO_LAST_TOTAL_MOTOR_START_TIME).value.floatValue()});
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.REDUNDANCY_IMU_STATUS)
    public void getRedundancyIMUStatus(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.mergeGetFlycParamInfo.getInfo(ParamCfgName.GSTATUS_TOPOLOGY_VERIFY_IMU, new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass18 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(innerCallback, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.MULTI_ROTOR_TYPE)
    public void getMultiRotorType(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.mergeGetFlycParamInfo.getInfo(ParamCfgName.GCONFIG_ROTOR_TYPE, new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass19 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(innerCallback, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        getDataFlycActiveStatus(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass20 */

            public void onSuccess(Object status) {
                CallbackUtils.onSuccess(callback2, FlightControllerAbstraction.this.getHashSerialNum(((DataFlycActiveStatus) status).getSN(), 0));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter("FullSerialNumberHash")
    public void getFullSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        getDataFlycActiveStatus(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass21 */

            public void onSuccess(Object status) {
                CallbackUtils.onSuccess(callback2, FlightControllerAbstraction.this.getHashSerialNum(((DataFlycActiveStatus) status).getSN(), 2));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
            }
        });
    }

    private void getDataFlycActiveStatus(final DJIDataCallBack callback2) {
        if (isNewProgressOfActivation()) {
            final DataCommonActiveGetVer getVer = new DataCommonActiveGetVer();
            getVer.setDevice(DeviceType.FLYC).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass22 */

                public void onSuccess(Object model) {
                    final DataFlycActiveStatus status = new DataFlycActiveStatus();
                    status.setVersion(getVer.getVer()).setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass22.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            callback2.onSuccess(status);
                        }

                        public void onFailure(Ccode ccode) {
                            callback2.onFailure(ccode);
                        }
                    });
                }

                public void onFailure(Ccode ccode) {
                    callback2.onFailure(ccode);
                }
            });
        } else if (callback2 != null) {
            final DataFlycActiveStatus activeStatus = new DataFlycActiveStatus();
            activeStatus.setVersion(DataFlycActiveStatus.getInstance().getVersion()).setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass23 */

                public void onSuccess(Object model) {
                    callback2.onSuccess(activeStatus);
                }

                public void onFailure(Ccode ccode) {
                    callback2.onFailure(ccode);
                }
            });
        }
    }

    @Getter("InternalSerialNumber")
    public void getInternalSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (callback2 != null) {
            getDataFlycActiveStatus(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass24 */

                public void onSuccess(Object status) {
                    CallbackUtils.onSuccess(callback2, ((DataFlycActiveStatus) status).getSN());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(FlightControllerKeys.HOME_LOCATION_USING_CURRENT_AIRCRAFT_LOCATION)
    public void homeLocationUsingCurrentAircraftLocation(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (DataOsdGetPushCommon.getInstance().getGpsLevel() >= 4) {
            new DataFlycSetHomePoint().setHomeType(DataFlycSetHomePoint.HOMETYPE.AIRCRAFT).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass25 */

                public void onSuccess(Object model) {
                    if (((DataFlycSetHomePoint) model).getResult() == 0) {
                        CallbackUtils.onSuccess(callback2, (Object) null);
                        FlightControllerAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(((float) DataOsdGetPushCommon.getInstance().getHeight()) / 10.0f), FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.HOME_POINT_ALTITUDE));
                        FlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(DataOsdGetPushCommon.getInstance().getLatitude()), FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LATITUDE));
                        FlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(DataOsdGetPushCommon.getInstance().getLongitude()), FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LONGITUDE));
                        return;
                    }
                    CallbackUtils.onFailure(callback2, DJIError.COMMON_UNKNOWN);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        } else if (callback2 != null) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.GPS_SIGNAL_WEAK);
        }
    }

    @Setter(FlightControllerKeys.HOME_LOCATION)
    public void setHomeLocation(LocationCoordinate2D homePoint, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        double latitude = homePoint.getLatitude();
        double longitude = homePoint.getLongitude();
        if (latitude < -90.0d || latitude > 90.0d || longitude < -180.0d || longitude > 180.0d) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.INVALID_PARAMETER);
        } else if (guardrailsForHomePoint(homePoint)) {
            DataFlycSetHomePoint.getInstance().setHomeType(DataFlycSetHomePoint.HOMETYPE.APP).setGpsInfo(LocationUtils.DegreeToRadian(latitude), LocationUtils.DegreeToRadian(longitude)).setInerval((byte) 100).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass26 */

                public void onSuccess(Object model) {
                    DataFlycSetHomePoint resultModel = (DataFlycSetHomePoint) model;
                    if (resultModel.getResult() == 0) {
                        CallbackUtils.onSuccess(callback2, (Object) null);
                        FlightControllerAbstraction.this.notifyValueChangeForKeyPath(0, FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.HOME_POINT_ALTITUDE));
                        FlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(DataOsdGetPushHome.getInstance().getLatitude()), FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LATITUDE));
                        FlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(DataOsdGetPushHome.getInstance().getLongitude()), FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.HOME_LOCATION_LONGITUDE));
                    } else if (resultModel.getResult() == 1) {
                        CallbackUtils.onFailure(callback2, DJIFlightControllerError.HOME_POINT_UNKNOWN_FAILED_REASON);
                    } else if (resultModel.getResult() == 2) {
                        CallbackUtils.onFailure(callback2, DJIFlightControllerError.HOME_POINT_INVALID_COORDINATE);
                    } else if (resultModel.getResult() == 3) {
                        CallbackUtils.onFailure(callback2, DJIFlightControllerError.HOME_POINT_INITIALIZING);
                    } else if (resultModel.getResult() == 4) {
                        CallbackUtils.onFailure(callback2, DJIFlightControllerError.HOME_POINT_GPS_SIGNAL_WEAK);
                    } else if (resultModel.getResult() == 5) {
                        CallbackUtils.onFailure(callback2, DJIFlightControllerError.HOME_POINT_TOO_FAR);
                    }
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        } else if (callback2 != null) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.HOME_POINT_TOO_FAR);
        }
    }

    @Setter(FlightControllerKeys.GO_HOME_HEIGHT_IN_METERS)
    public void setGoHomeAltitude(final int altitude, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        boolean isNeedMaxFlightHeight = ((Boolean) CacheHelper.getFlightController(FlightControllerKeys.NEED_LIMIT_FLIGHT_HEIGHT)).booleanValue();
        if (altitude <= 120 || !isNeedMaxFlightHeight) {
            DJIParamMinMaxCapability goHomeHeightCapability = getGoHomeHeightRange();
            if (altitude < goHomeHeightCapability.getMin().intValue()) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_LOW);
            } else if (altitude > goHomeHeightCapability.getMax().intValue()) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_HIGH);
            } else {
                FlightMode flightMode = (FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE);
                if (flightMode == null) {
                    callback2.onFails(DJIError.COMMON_DISCONNECTED);
                } else if (flightMode == FlightMode.GO_HOME) {
                    callback2.onFails(DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    final ParamInfo heightInfo = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_GH_ALTITUDE);
                    getMaxFlightHeight(new DJISDKCacheHWAbstraction.InnerCallback() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass27 */

                        public void onSuccess(Object o) {
                            if (o instanceof Integer) {
                                if (altitude > ((Integer) o).intValue()) {
                                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.GO_HOME_ALTITUDE_HIGHER_THAN_MAX_FLIGHT_HEIGHT);
                                } else {
                                    new DataFlycSetParams().setInfo(heightInfo.name, Integer.valueOf(altitude)).start(new DJIDataCallBack() {
                                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass27.AnonymousClass1 */

                                        public void onSuccess(Object model) {
                                            DJILog.logWriteI(FlightControllerAbstraction.TAG, "set go home altitude success 2 " + altitude, new Object[0]);
                                            CallbackUtils.onSuccess(callback2, (Object) null);
                                        }

                                        public void onFailure(Ccode ccode) {
                                            DJILog.logWriteE(FlightControllerAbstraction.TAG, "set go home altitude fail 2 " + ccode, new Object[0]);
                                            CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
                                        }
                                    });
                                }
                            }
                        }

                        public void onFails(DJIError error) {
                            CallbackUtils.onFailure(callback2, error);
                        }
                    });
                }
            }
        } else {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(FlightControllerKeys.GO_HOME_HEIGHT_IN_METERS)
    public void getGoHomeAltitude(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeGetFlycParamInfo.getInfo(ParamCfgName.GCONFIG_GH_ALTITUDE, new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass28 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.GO_HOME_HEIGHT_RANGE)
    public void getGoHomeAltitudeRange(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycGetParamInfoByHash flycGetParamInfo = new DataFlycGetParamInfoByHash();
        flycGetParamInfo.setIndex(ParamCfgName.GCONFIG_GH_ALTITUDE);
        flycGetParamInfo.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass29 */

            public void onSuccess(Object model) {
                RangeModel range = ((DataFlycGetParamInfoByHash) model).getParamInfo().range;
                CallbackUtils.onSuccess(callback2, new DJIParamMinMaxCapability(true, range.minValue, range.maxValue));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.CONNECTION_FAIL_SAFE_BEHAVIOR)
    public void setFlightFailSafeOperation(ConnectionFailSafeBehavior operation, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (operation == ConnectionFailSafeBehavior.UNKNOWN) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            new DataFlycSetParams().setInfo("g_config.fail_safe.protect_action_0", Integer.valueOf(operation.value())).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass30 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        }
    }

    @Getter(FlightControllerKeys.CONNECTION_FAIL_SAFE_BEHAVIOR)
    public void getFlightFailSafeOperation(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycGetFsAction.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass31 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, ConnectionFailSafeBehavior.find(DataFlycGetFsAction.getInstance().getFsAction().value()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.LOW_BATTERY_WARNING_THRESHOLD)
    public void setGoHomeBatteryThreshold(int param, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (param > 50 || param < 15) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.COMMON_PARAM_ILLEGAL);
        } else if (param < CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.SERIOUS_LOW_BATTERY_WARNING_THRESHOLD)) + 5) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.COMMON_PARAM_ILLEGAL);
        } else {
            setBatteryWarning(param, DataFlycGetVoltageWarnning.WarnningLevel.First, callback2);
        }
    }

    @Setter(FlightControllerKeys.SERIOUS_LOW_BATTERY_WARNING_THRESHOLD)
    public void setLandImmediatelyBatteryThreshold(int param, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int goHomeBattery = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.LOW_BATTERY_WARNING_THRESHOLD));
        int minValue = 10;
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.Orange2 || ProductType.M210 == type || ProductType.M200 == type || ProductType.M210RTK == type || ProductType.PM420 == type || ProductType.PM420PRO == type || ProductType.PM420PRO_RTK == type) {
            minValue = 7;
        }
        if (param < minValue || param > goHomeBattery - 5) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            setBatteryWarning(param, DataFlycGetVoltageWarnning.WarnningLevel.Second, callback2);
        }
    }

    @Setter(FlightControllerKeys.AIRCRAFT_NAME)
    public void setAircraftName(String name, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycSetPlaneName.getInstance().setName(name).start(CallbackUtils.defaultCB(callback2));
    }

    @Getter(FlightControllerKeys.AIRCRAFT_NAME)
    public void getAircraftName(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycGetPlaneName.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass32 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, ((DataFlycGetPlaneName) model).getName());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.LEDS_ENABLED_SETTINGS)
    public void setLedsEnabled(LEDsSettings settings, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_MISC_FOREARM_LAMP);
        setParams.setValues(settings.getByte());
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass33 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.NOVICE_MODE_ENABLED)
    public void setNoviceFunc(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int i = 1;
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_BEGINNER_MODE);
        Number[] numberArr = new Number[1];
        if (!enabled) {
            i = 0;
        }
        numberArr[0] = Integer.valueOf(i);
        setParams.setValues(numberArr);
        setParams.start(CallbackUtils.defaultCB(callback2, DJIFlightControllerError.class));
    }

    @Setter(FlightControllerKeys.ESC_BEEPING_ENABLED)
    public void setESCBeepingEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycSetEscEcho setter = new DataFlycSetEscEcho();
        setter.setEchoCmd(enabled ? DataFlycSetEscEcho.SetEchoCmd.OPEN_ALL : DataFlycSetEscEcho.SetEchoCmd.CLOSE_ALL).setEchoIndex(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass34 */

            public void onSuccess(Object model) {
                if (DataFlycSetEscEcho.SetResult.SUCCESS == setter.getSetResult()) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback2, DJIError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.ESC_BEEPING_ENABLED)
    public void getESCBeepingEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycSetEscEcho setter = new DataFlycSetEscEcho();
        setter.setEchoCmd(DataFlycSetEscEcho.SetEchoCmd.REQUEST_SOME).setEchoIndex(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass35 */

            public void onSuccess(Object model) {
                boolean isEchoing = false;
                if (DataFlycSetEscEcho.SetResult.SUCCESS == setter.getSetResult()) {
                    isEchoing = setter.isEchoing();
                }
                CallbackUtils.onSuccess(callback2, Boolean.valueOf(isEchoing));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.LEDS_ENABLED_SETTINGS)
    public void getLedsEnables(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_MISC_FOREARM_LAMP}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass36 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, LEDsSettings.generateLEDsEnabledSettings(DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_MISC_FOREARM_LAMP).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.MULTI_LEDS_ENABLED)
    public void setMultiLedsEnabled(DJIMultiLEDControlMode enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_MISC_FOREARM_LAMP);
        setParams.setValues(Byte.valueOf(enabled.getDate()));
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass37 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.MULTI_LEDS_ENABLED)
    public void getMultiLedsEnables(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_MISC_FOREARM_LAMP}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass38 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, new DJIMultiLEDControlMode(DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_MISC_FOREARM_LAMP).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(FlightControllerKeys.TAKE_OFF)
    public void takeOff(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (canTakeOff() || callback2 == null) {
            DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.AUTO_FLY).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass39 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.ALREADY_IN_THE_AIR);
        }
    }

    @Action(FlightControllerKeys.PRECISION_TAKE_OFF)
    public void precisionTakeOff(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (!isPrecisionTakeOffSupported()) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.PRECISION_TAKE_OFF_NOT_SUPPORT);
        } else if (!canTakeOff()) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.ALREADY_IN_THE_AIR);
        } else {
            DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.PRECISION_TAKE_OFF).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass40 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(FlightControllerKeys.CANCEL_TAKE_OFF)
    public void cancelTakeOff(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.DropTakeOff).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass41 */

            public void onSuccess(Object model) {
                DJICompletionHelper.getInstance().cancelTakeOffCallbackCompletionHelper(callback2);
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
                }
            }
        });
    }

    @Action(FlightControllerKeys.FORCE_LANDING)
    public void forceLanding(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.ForceLanding, callback2);
    }

    @Action(FlightControllerKeys.START_LANDING)
    public void autoLanding(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.AUTO_LANDING, callback2);
    }

    @Action(FlightControllerKeys.CANCEL_LANDING)
    public void cancelAutoLanding(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.DropLanding, callback2);
    }

    @Action(FlightControllerKeys.TURN_ON_MOTORS)
    public void turnOnMotors(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (FirmwareVersionLoader.getInstance().getFlightControllerFirmwareVersion().compareTo(INTERNAL_FIRMWARE_VERSION_TURN) < 0) {
            CallbackUtils.onFailure(callback2, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else {
            sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.START_MOTOR, callback2);
        }
    }

    @Action(FlightControllerKeys.TURN_OFF_MOTORS)
    public void turnOffMotors(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (DataOsdGetPushCommon.getInstance().groundOrSky() == 2) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.CANNOT_TURN_OFF_MOTORS_WHILE_AIRCRAFT_FLYING);
        } else {
            sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.STOP_MOTOR, callback2);
        }
    }

    @Action(FlightControllerKeys.START_GO_HOME)
    public void goHome(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.GOHOME, callback2);
    }

    @Action(FlightControllerKeys.CANCEL_GO_HOME)
    public void cancelGoHome(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.DropGohome, callback2);
    }

    @Action(FlightControllerKeys.LOCK_COURSE_USING_CURRENT_HEADING)
    public void lockCourseUsingCurrentDirection(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.HOMEPOINT_LOC, callback2);
    }

    @Action(FlightControllerKeys.START_IMU_CALIBRATION)
    public void startImuCalibration(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (!DataOsdGetPushCommon.getInstance().isMotorUp()) {
            switch (this.imuCount) {
                case 1:
                    sendImuCalibrationCmd(new String[]{ParamCfgName.GCONFIG_IMU_STARTCAL_0}, new Number[]{3}, callback2);
                    return;
                case 2:
                    sendImuCalibrationCmd(new String[]{ParamCfgName.GCONFIG_IMU_STARTCAL_0, ParamCfgName.GCONFIG_IMU_STARTCAL_1}, new Number[]{3, 3}, callback2);
                    return;
                case 3:
                    sendImuCalibrationCmd(new String[]{ParamCfgName.GCONFIG_IMU_STARTCAL_0, ParamCfgName.GCONFIG_IMU_STARTCAL_1, ParamCfgName.GCONFIG_IMU_STARTCAL_2}, new Number[]{3, 3, 3}, callback2);
                    return;
                default:
                    if (callback2 != null) {
                        CallbackUtils.onFailure(callback2, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
                        return;
                    }
                    return;
            }
        } else if (callback2 != null) {
            CallbackUtils.onFailure(callback2, DJIFlightControllerError.IMU_CALIBRATION_ERROR_IN_THE_AIR_OR_MOTORS_ON);
        }
    }

    /* access modifiers changed from: protected */
    public void sendImuCalibrationCmd(final String[] str, final Number[] nums, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (this.internalFCVerion >= 16) {
            new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GSTATUS_GYRACC_NEED_CALTYPE}).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass42 */

                public void onSuccess(Object model) {
                    DataFlycSetParams params = new DataFlycSetParams();
                    int type = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_GYRACC_NEED_CALTYPE).value.intValue();
                    if (type != 1) {
                        type = 3;
                    }
                    for (int i = 0; i < nums.length; i++) {
                        nums[i] = Integer.valueOf(type);
                    }
                    params.setIndexs(str);
                    params.setValues(nums);
                    params.start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass42.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback2, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback2, ccode);
                        }
                    });
                }

                public void onFailure(Ccode ccode) {
                    if (callback2 != null) {
                        callback2.onFails(DJIFlightControllerError.getDJIError(ccode));
                    }
                }
            });
            return;
        }
        new DataFlycSetParams().setInfo(DJIFlycParamInfoManager.read("imu_app_temp_cali.start_flag_0").name, 1).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass43 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.ORIENTATION_MODE)
    public void setFlightOrientationMode(final FlightOrientationMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (mode == null && callback2 != null) {
            CallbackUtils.onFailure(callback2, DJIMissionError.FAILED);
        }
        if (mode == FlightOrientationMode.AIRCRAFT_HEADING) {
            cancelFlightOrientationMode(callback2);
        } else {
            enterNavigationMode(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass44 */

                public void onSuccess(Object o) {
                    DataFlycStartIoc.getInstance().setMode(DataFlycStartIoc.IOCType.find(mode.value())).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass44.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            int result = DataFlycStartIoc.getInstance().getResult();
                            if (result == 0) {
                                if (callback2 != null) {
                                    CallbackUtils.onSuccess(callback2, (Object) null);
                                }
                            } else if (callback2 != null) {
                                CallbackUtils.onFailure(callback2, DJIMissionError.getDJIErrorByCode(result));
                            }
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback2, DJIMissionError.FAILED);
                        }
                    });
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback2, error);
                }
            });
        }
    }

    private void cancelFlightOrientationMode(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycStopIoc.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass45 */

            public void onSuccess(Object model) {
                int result = DataFlycStopIoc.getInstance().getResult();
                if (result == 0) {
                    FlightControllerAbstraction.this.exitNavigationMode(new DJISDKCacheHWAbstraction.InnerCallback() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass45.AnonymousClass1 */

                        public void onSuccess(Object o) {
                            CallbackUtils.onSuccess(callback2, (Object) null);
                        }

                        public void onFails(DJIError error) {
                            CallbackUtils.onFailure(callback2, error);
                        }
                    });
                } else if (callback2 != null) {
                    CallbackUtils.onFailure(callback2, DJIMissionError.getDJIErrorByCode(result));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIMissionError.FAILED);
            }
        });
    }

    @Setter(FlightControllerKeys.NAVIGATION_MODE_ENABLED)
    public void setNavigationModeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (enabled) {
            enterNavigationMode(callback2);
        } else {
            exitNavigationMode(callback2);
        }
    }

    @Setter(FlightControllerKeys.MULTI_MODE_OPEN)
    public void setMultiControllModeEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (enabled) {
            MultiModeEnabledUtil.setMultiModeEnabled(callback2);
            return;
        }
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs("g_config.control.multi_control_mode_enable_0");
        setParams.setValues(0);
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass46 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.VIRTUAL_STICK_CONTROL_MODE_ENABLED)
    public void setVirtualStickControlModeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        setNavigationModeEnabled(enabled, callback2);
    }

    private void enterNavigationMode(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        MultiModeEnabledUtil.setMultiModeEnabled(new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass47 */

            public void onSuccess(Object o) {
                FlightControllerAbstraction.this.sendNavigationModeCmd(DataFlycNavigationSwitch.GS_COMMAND.OPEN_GROUND_STATION, callback2);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    /* access modifiers changed from: private */
    public void exitNavigationMode(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendNavigationModeCmd(DataFlycNavigationSwitch.GS_COMMAND.CLOSE_GROUND_STATION, callback2);
    }

    /* access modifiers changed from: private */
    public void sendNavigationModeCmd(DataFlycNavigationSwitch.GS_COMMAND command, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycNavigationSwitch.getInstance().setCommand(command).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass48 */

            public void onSuccess(Object model) {
                if (callback2 == null) {
                    return;
                }
                if (DataFlycNavigationSwitch.getInstance().getResult() == 0) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback2, DJIMissionError.getDJIErrorByCode(DataFlycNavigationSwitch.getInstance().getResult()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Action(FlightControllerKeys.START_IMU_CALIBRATION_WITH_ID)
    public void startImuCalibrationWithID(DJISDKCacheHWAbstraction.InnerCallback callback2, int index) {
        CallbackUtils.onFailure(callback2, DJIError.COMMON_UNSUPPORTED);
    }

    @Action(FlightControllerKeys.SEND_DATA_TO_ON_BOARD_SDK_DEVICE)
    public void sendDataToOnBoardSdkDevice(final DJISDKCacheHWAbstraction.InnerCallback callback2, byte[] data2) {
        if (!CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.IS_ON_BOARD_SDK_AVAILABLE))) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_UNSUPPORTED);
        } else if (data2 == null || data2.length == 0 || data2.length > 100) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataFlycSetSendOnBoard.getInstance().setSendData(data2).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass49 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        }
    }

    @Action(FlightControllerKeys.SEND_VIRTUAL_STICK_FLIGHT_CONTROL_DATA)
    public void sendVirtualStickFlightControlData(DJISDKCacheHWAbstraction.InnerCallback callback2, FlightControlData controlData, VerticalControlMode verticalControlMode, RollPitchControlMode rollPitchControlMode, YawControlMode yawControlMode, FlightCoordinateSystem rollPitchCoordinateSystem, boolean virtualStickAdvancedModeEnabled) {
        DataFlycJoystick joystick = DataFlycJoystick.getInstance();
        if (verticalControlMode.equals(VerticalControlMode.VELOCITY) && (controlData.getVerticalThrottle() < -4.0f || controlData.getVerticalThrottle() > 4.0f)) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (verticalControlMode.equals(VerticalControlMode.POSITION) && (controlData.getVerticalThrottle() < 0.0f || controlData.getVerticalThrottle() > 500.0f)) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (rollPitchControlMode.equals(RollPitchControlMode.ANGLE) && (controlData.getPitch() < -30.0f || controlData.getPitch() > 30.0f || controlData.getRoll() < -30.0f || controlData.getRoll() > 30.0f)) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (rollPitchControlMode.equals(RollPitchControlMode.VELOCITY) && (controlData.getPitch() < -15.0f || controlData.getPitch() > 15.0f || controlData.getRoll() < -15.0f || controlData.getRoll() > 15.0f)) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (yawControlMode.equals(YawControlMode.ANGLE) && (controlData.getYaw() < -180.0f || controlData.getYaw() > 180.0f)) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (!yawControlMode.equals(YawControlMode.ANGULAR_VELOCITY) || (controlData.getYaw() >= -100.0f && controlData.getYaw() <= 100.0f)) {
            FlightControlData mControlData = checkSwapYawAndThrottle(controlData);
            joystick.setFlag(getControlModeFlag(verticalControlMode, rollPitchControlMode, yawControlMode, rollPitchCoordinateSystem, virtualStickAdvancedModeEnabled));
            joystick.setYaw(mControlData.getYaw());
            joystick.setPitch(mControlData.getPitch());
            joystick.setRoll(mControlData.getRoll());
            joystick.setThrottle(mControlData.getVerticalThrottle()).start();
            EventBus.getDefault().post(joystick);
            CallbackUtils.onSuccess(callback2, (Object) null);
        } else {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(FlightControllerKeys.TERRAIN_FOLLOW_MODE_ENABLED)
    public void setTerrainFollowEnabled(Boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (enabled.booleanValue()) {
            enterNavigationMode(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass50 */

                public void onSuccess(Object o) {
                    DataFlycStartNoeMission.getInstance().setHeight(10.0f).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass50.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            DJILog.d(FlightControllerAbstraction.TAG, "return result: " + DataFlycStartNoeMission.getInstance().getResult(), new Object[0]);
                            if (DataFlycStartNoeMission.getInstance().getResult() == 0) {
                                CallbackUtils.onSuccess(callback2, model);
                            } else {
                                CallbackUtils.onFailure(callback2, Ccode.NOT_SUPPORT_CURRENT_STATE);
                            }
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback2, ccode);
                        }
                    });
                }

                public void onFails(DJIError error) {
                    DJILog.d(FlightControllerAbstraction.TAG, "enterNavigationMode failed", new Object[0]);
                    CallbackUtils.onFailure(callback2, error);
                }
            });
        } else {
            DataFlycStopNoeMission.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass51 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        }
    }

    @Action(FlightControllerKeys.PAUSE_TERRAIN_FOLLOW_MODE)
    public void pauseTerrainFollow(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycNoeMissionPauseOrResume.getInstance().pauseMission().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass52 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.common.util.CallbackUtils.onSuccess(dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, java.lang.Object):void
     arg types: [dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, boolean]
     candidates:
      dji.common.util.CallbackUtils.onSuccess(dji.common.util.CommonCallbacks$CompletionCallbackWith, java.lang.Object):void
      dji.common.util.CallbackUtils.onSuccess(dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, java.lang.Object):void */
    @Getter(FlightControllerKeys.TERRAIN_FOLLOW_MODE_ENABLED)
    public void getTerrainModeEnabled(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (DataOsdGetPushCommon.getInstance().getRecData() == null) {
            CallbackUtils.onFailure(callback2);
        } else if (DataOsdGetPushCommon.getInstance().getFlycState().equals(DataOsdGetPushCommon.FLYC_STATE.TERRAIN_TRACKING)) {
            CallbackUtils.onSuccess(callback2, (Object) true);
        } else {
            CallbackUtils.onSuccess(callback2, (Object) false);
        }
    }

    @Action(FlightControllerKeys.RESUME_TERRAIN_FOLLOW_MODE)
    public void resumeTerrainFollow(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycNoeMissionPauseOrResume.getInstance().resumeMission().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass53 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    private FlightControlData checkSwapYawAndThrottle(FlightControlData controlData) {
        FlightControlData mControlData = new FlightControlData(controlData.getPitch(), controlData.getRoll(), controlData.getYaw(), controlData.getVerticalThrottle());
        DataOsdGetPushCommon.DroneType type = DataOsdGetPushCommon.getInstance().getDroneType();
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() >= 16 && type.value() >= DataOsdGetPushCommon.DroneType.wm220.value() && type.value() != DataOsdGetPushCommon.DroneType.PM820PRO.value()) {
            mControlData.setYaw(controlData.getVerticalThrottle());
            mControlData.setVerticalThrottle(controlData.getYaw());
        }
        return mControlData;
    }

    @Action(FlightControllerKeys.COMPASS_START_CALIBRATION)
    public void compassStartCalibration(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.Calibration, callback2);
    }

    @Action(FlightControllerKeys.COMPASS_STOP_CALIBRATION)
    public void compassStopCalibration(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.DropCalibration, callback2);
    }

    @Setter(FlightControllerKeys.MAX_FLIGHT_HEIGHT)
    public void setMaxFlightHeight(final int maxHeight, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        boolean isNeedMaxFlightHeight = ((Boolean) CacheHelper.getFlightController(FlightControllerKeys.NEED_LIMIT_FLIGHT_HEIGHT)).booleanValue();
        DJILog.logWriteI(TAG, "set max flight height " + maxHeight + ", is need max flight height " + isNeedMaxFlightHeight, new Object[0]);
        if (maxHeight <= 120 || !isNeedMaxFlightHeight) {
            DJIParamMinMaxCapability capability = getMaxFlightHeightRange();
            DJILog.logWriteI(TAG, "set max flight height " + capability, new Object[0]);
            if (maxHeight < capability.getMin().intValue() || maxHeight > capability.getMax().intValue()) {
                CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
            DataFlycSetParams setParams = new DataFlycSetParams();
            setParams.setIndexs(ParamCfgName.GCONFIG_LIMIT_HEIGHT);
            setParams.setValues(Integer.valueOf(maxHeight));
            setParams.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass54 */

                public void onSuccess(Object model) {
                    DJILog.logWriteI(FlightControllerAbstraction.TAG, "set max flight height success " + maxHeight, new Object[0]);
                    CallbackUtils.onSuccess(callback2, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.logWriteE(FlightControllerAbstraction.TAG, "set max flight height fails " + ccode, new Object[0]);
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
                }
            });
            return;
        }
        CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Getter(FlightControllerKeys.MAX_FLIGHT_HEIGHT_RANGE)
    public void getMaxFlightHeightRange(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
            CallbackUtils.onSuccess(callback2, new DJIParamMinMaxCapability(true, 20, 50));
            return;
        }
        final DataFlycGetParamInfoByHash flycGetParamInfo = new DataFlycGetParamInfoByHash();
        flycGetParamInfo.setIndex(ParamCfgName.GCONFIG_LIMIT_HEIGHT);
        flycGetParamInfo.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass55 */

            public void onSuccess(Object model) {
                RangeModel range = flycGetParamInfo.getParamInfo().range;
                CallbackUtils.onSuccess(callback2, new DJIParamMinMaxCapability(true, range.minValue, range.maxValue));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.ENABLE_1860)
    public void setEnableStateOf1860(int enable, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs("g_config_airport_limit_cfg_cfg_1860_limit_switch");
        setParams.setValues(Integer.valueOf(enable));
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass56 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.ENABLE_1860)
    public void getEnableStateOf1860(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{"g_config_airport_limit_cfg_cfg_1860_limit_switch"}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass57 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(DJIFlycParamInfoManager.read("g_config_airport_limit_cfg_cfg_1860_limit_switch").value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.URGENT_STOP_MOTOR_MODE)
    public void setStopMotorMode(UrgentStopMotorMode mode, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (UrgentStopMotorMode.IN_OUT_ALWAYS == mode || UrgentStopMotorMode.IN_OUT_WHEN_BREAKDOWN == mode || UrgentStopMotorMode.UNKNOWN == mode) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        int flycVersion = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
        int cmdNo = -1;
        if (flycVersion < 16 || flycVersion >= 21) {
            if (flycVersion < 21) {
                CallbackUtils.onFailure(callback2, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
                return;
            } else if (UrgentStopMotorMode.CSC == mode) {
                cmdNo = 1;
            } else if (UrgentStopMotorMode.NEVER == mode) {
                cmdNo = 0;
            }
        } else if (UrgentStopMotorMode.CSC == mode) {
            cmdNo = 0;
        } else if (UrgentStopMotorMode.NEVER == mode) {
            cmdNo = 1;
        }
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setInfo(ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE, Integer.valueOf(cmdNo));
        setParams.start(CallbackUtils.defaultCB(callback2));
    }

    @Getter(FlightControllerKeys.URGENT_STOP_MOTOR_MODE)
    public void getStopMotorMode(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        String[] indexes = {ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE};
        final int flycVersion = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
        new DataFlycGetParams().setInfos(indexes).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass58 */

            public void onSuccess(Object model) {
                int cmdIndex = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE).value.intValue();
                if (flycVersion < 16 || flycVersion >= 21) {
                    if (flycVersion < 21) {
                        CallbackUtils.onFailure(callback2, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
                    } else if (1 == cmdIndex) {
                        CallbackUtils.onSuccess(callback2, UrgentStopMotorMode.CSC);
                    } else if (cmdIndex == 0) {
                        CallbackUtils.onSuccess(callback2, UrgentStopMotorMode.NEVER);
                    } else {
                        CallbackUtils.onFailure(callback2, DJIError.COMMON_UNKNOWN);
                    }
                } else if (cmdIndex == 0) {
                    CallbackUtils.onSuccess(callback2, UrgentStopMotorMode.CSC);
                } else if (1 == cmdIndex) {
                    CallbackUtils.onSuccess(callback2, UrgentStopMotorMode.NEVER);
                } else {
                    CallbackUtils.onFailure(callback2, DJIError.COMMON_UNKNOWN);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.MAX_FLIGHT_HEIGHT)
    public void getMaxFlightHeight(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_LIMIT_HEIGHT}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass59 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_LIMIT_HEIGHT).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.MAX_FLIGHT_RADIUS)
    public void setMaxFlightRadius(int maxRadius, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) CacheHelper.getFlightController(FlightControllerKeys.MAX_FLIGHT_RADIUS_RANGE);
        int max = capability.getMax().intValue();
        if (maxRadius < capability.getMin().intValue() || maxRadius > max) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_FLY_LIMIT);
        setParams.setValues(Integer.valueOf(maxRadius));
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass60 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.MAX_FLIGHT_RADIUS)
    public void getMaxFlightRadius(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_FLY_LIMIT}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass61 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_FLY_LIMIT).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.MAX_FLIGHT_RADIUS_LIMITATION_ENABLED)
    public void setMaxFlightRadiusEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int i = 1;
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_NOVICE_FUNC);
        Number[] numberArr = new Number[1];
        if (!enabled) {
            i = 0;
        }
        numberArr[0] = Integer.valueOf(i);
        setParams.setValues(numberArr);
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass62 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.MAX_FLIGHT_RADIUS_LIMITATION_ENABLED)
    public void getMaxFlightRadiusEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_NOVICE_FUNC}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass63 */

            public void onSuccess(Object model) {
                boolean z = true;
                ParamInfo flag = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_NOVICE_FUNC);
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback2;
                if (flag.value.intValue() != 1) {
                    z = false;
                }
                CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.IS_VIRTUAL_STICK_CONTROL_MODE_AVAILABLE)
    public void getIsVirtualStickControlModeAvailiable(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (callback2 != null) {
            DataOsdGetPushCommon.FLYC_STATE flycState = DataOsdGetPushCommon.getInstance().getFlycState();
            if (flycState == DataOsdGetPushCommon.FLYC_STATE.GPS_HotPoint || flycState == DataOsdGetPushCommon.FLYC_STATE.NaviGo || flycState == DataOsdGetPushCommon.FLYC_STATE.NaviMissionFollow || flycState == DataOsdGetPushCommon.FLYC_STATE.GPS_HomeLock || flycState == DataOsdGetPushCommon.FLYC_STATE.GPS_CL || flycState == DataOsdGetPushCommon.FLYC_STATE.Atti_CL) {
                callback2.onSuccess(false);
                return;
            }
            boolean isVirtualStickModeOn = false;
            DJISDKCacheParamValue cacheValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.VIRTUAL_STICK_CONTROL_MODE_ENABLED));
            if (cacheValue != null) {
                isVirtualStickModeOn = ((Boolean) cacheValue.getData()).booleanValue();
            }
            callback2.onSuccess(Boolean.valueOf(isVirtualStickModeOn));
        }
    }

    @Setter(FlightControllerKeys.FLY_ZONE_LIMITATION_ENABLED)
    public void setGeoFeatureInSimulatorEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int i = 1;
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs("g_config.airport_limit_cfg.cfg_sim_disable_limit_0");
        Number[] numberArr = new Number[1];
        if (enabled) {
            i = 0;
        }
        numberArr[0] = Integer.valueOf(i);
        setParams.setValues(numberArr);
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass64 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.FLY_ZONE_LIMITATION_ENABLED)
    public void getGeoFeatureInSimulatorEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{"g_config.airport_limit_cfg.cfg_sim_disable_limit_0"}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass65 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, Boolean.valueOf(DJIFlycParamInfoManager.read("g_config.airport_limit_cfg.cfg_sim_disable_limit_0").value.intValue() == 0));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    private void setIOCTypeEnabled(boolean enabled, final DataFlycStartIoc.IOCType type, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        if (enabled) {
            enterNavigationMode(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass66 */

                public void onSuccess(Object o) {
                    DataFlycStartIoc.getInstance().setMode(type).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass66.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback2, model);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback2, ccode);
                        }
                    });
                }

                public void onFails(DJIError error) {
                    DJILog.d(FlightControllerAbstraction.TAG, "enterNavigationMode failed", new Object[0]);
                    CallbackUtils.onFailure(callback2, error);
                }
            });
        } else {
            DataFlycStopIoc.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass67 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        }
    }

    @Setter(FlightControllerKeys.TRIPOD_MODE_ENABLED)
    public void setTripodModeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        setIOCTypeEnabled(enabled, DataFlycStartIoc.IOCType.IOCTripod, callback2);
    }

    @Setter(FlightControllerKeys.CINEMATIC_MODE_ENABLED)
    public void setCinematicModeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        setIOCTypeEnabled(enabled, DataFlycStartIoc.IOCType.Cinematic, callback2);
    }

    @Action(FlightControllerKeys.CONFIRM_LANDING)
    public void startForceLanding(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        VisionLandingProtectionState currentState = VisionLandingProtectionState.find(DataEyeGetPushFlatCheck.getInstance().getFlatStatus().value());
        if (currentState.equals(VisionLandingProtectionState.ANALYZING) || currentState.equals(VisionLandingProtectionState.NOT_SAFE_TO_LAND)) {
            DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.ForceLanding2).start(CallbackUtils.defaultCB(callback2));
        } else {
            DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.ForceLanding).start(CallbackUtils.defaultCB(callback2));
        }
    }

    @Action(FlightControllerKeys.FORCE_LANDING_AFTER_FLAT_MODE)
    public void startForceLandingAfterFlatMode(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.ForceLanding2).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass68 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Getter("Mode")
    public void getModes(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int maximumChannelSize;
        int offset;
        if (DJIRCAbstractionUtil.isRemoteControllerHaveTwoSwitchMode(DJIProductManager.getInstance().getType())) {
            maximumChannelSize = 2;
            offset = 1;
        } else {
            maximumChannelSize = 3;
            offset = 0;
        }
        RemoteControllerFlightMode[] modes = new RemoteControllerFlightMode[maximumChannelSize];
        for (int i = 0; i < maximumChannelSize; i++) {
            modes[i] = translateRcChannelIntoMode(DJIMcHelper.getInstance().getRcModeChannel(i + offset));
        }
        callback2.onSuccess(modes);
    }

    private RemoteControllerFlightMode translateRcChannelIntoMode(DataOsdGetPushCommon.RcModeChannel channel) {
        switch (channel) {
            case CHANNEL_P:
                return RemoteControllerFlightMode.P;
            case CHANNEL_F:
                return RemoteControllerFlightMode.F;
            case CHANNEL_G:
                return RemoteControllerFlightMode.G;
            case CHANNEL_M:
                return RemoteControllerFlightMode.M;
            case CHANNEL_A:
                return RemoteControllerFlightMode.A;
            case CHANNEL_S:
                return RemoteControllerFlightMode.S;
            case CHANNEL_T:
                return RemoteControllerFlightMode.T;
            default:
                return RemoteControllerFlightMode.UNKNOWN;
        }
    }

    private boolean guardrailsForHomePoint(LocationCoordinate2D homePoint) {
        Location nextHomePointLocation = new Location("Next Home Point");
        nextHomePointLocation.setLatitude(homePoint.getLatitude());
        nextHomePointLocation.setLongitude(homePoint.getLongitude());
        double distanceBetweenCurrentHomePointAndNextHomePointLocation = LocationUtils.gps2m(DataOsdGetPushHome.getInstance().getLatitude(), DataOsdGetPushHome.getInstance().getLongitude(), nextHomePointLocation.getLatitude(), nextHomePointLocation.getLongitude());
        double distanceBetweenCurrentAircraftLocationAndNextHomePointLocation = LocationUtils.gps2m(nextHomePointLocation.getLatitude(), nextHomePointLocation.getLongitude(), DataOsdGetPushCommon.getInstance().getLatitude(), DataOsdGetPushCommon.getInstance().getLongitude());
        if (distanceBetweenCurrentHomePointAndNextHomePointLocation < 30.0d || distanceBetweenCurrentAircraftLocationAndNextHomePointLocation < 30.0d || ((LocationUtils.checkLocationPermission() && LocationUtils.getLastBestLocation() != null && LocationUtils.gps2m(nextHomePointLocation.getLatitude(), nextHomePointLocation.getLongitude(), LocationUtils.getLastBestLocation().getLatitude(), LocationUtils.getLastBestLocation().getLongitude()) < 30.0d) || ((DJIProductManager.getInstance().getType().equals(ProductType.N1) || DJIProductManager.getInstance().getType().equals(ProductType.BigBanana) || DJIProductManager.getInstance().getType().equals(ProductType.Orange)) && LocationUtils.gps2m(nextHomePointLocation.getLatitude(), nextHomePointLocation.getLongitude(), DataRcGetPushGpsInfo.getInstance().getLatitude(), DataRcGetPushGpsInfo.getInstance().getLongitude()) < 30.0d))) {
            return true;
        }
        return false;
    }

    private void setBatteryWarning(int param, DataFlycGetVoltageWarnning.WarnningLevel level, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DJIDataCallBack dataCallback = new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass69 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        };
        DataFlycSetLVoltageWarnning setter = DataFlycSetLVoltageWarnning.getInstance();
        setter.setWarnningLevel(level);
        setter.setValue(param);
        if (level == DataFlycGetVoltageWarnning.WarnningLevel.First) {
            setter.setIsNeedGoHome(true);
        } else {
            setter.setIsNeedLanding(true);
        }
        setter.start(dataCallback);
    }

    private void getBatteryWarning(DataFlycGetVoltageWarnning.WarnningLevel level, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DJIDataCallBack dataCallback = new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass70 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(DataFlycGetVoltageWarnning.getInstance().getValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        };
        DataFlycGetVoltageWarnning getter = DataFlycGetVoltageWarnning.getInstance();
        getter.setWarnningLevel(level);
        getter.start(dataCallback);
    }

    /* access modifiers changed from: protected */
    public boolean canTakeOff() {
        return !CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.ARE_MOTOR_ON)) || !CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.IS_FLYING));
    }

    /* access modifiers changed from: protected */
    public boolean isPrecisionTakeOffSupported() {
        if (!CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED)) || CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION)) < PRECISION_TAKE_OFF_SUPPORTED_MIN_VERSION) {
            return false;
        }
        ProductType type = DJIProductManager.getInstance().getType();
        if (CommonUtil.isKumquatSeries(type) || CommonUtil.isP4PSeries(type) || ProductType.Potato == type || CommonUtil.isM200Product(type) || ProductType.WM230 == type) {
            return CommonUtil.checkGpsValid();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND command, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycFunctionControl.getInstance().setCommand(command).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass71 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    private byte getControlModeFlag(VerticalControlMode verticalControlMode, RollPitchControlMode rollPitchControlMode, YawControlMode yawControlMode, FlightCoordinateSystem rollPitchCoordinateSystem, boolean virtualStickAdvancedModeEnabled) {
        return (byte) (((byte) (rollPitchCoordinateSystem.value() << 1)) + ((byte) (rollPitchControlMode.value() << 6)) + ((byte) (verticalControlMode.value() << 4)) + ((byte) (yawControlMode.value() << 3)) + ((byte) (virtualStickAdvancedModeEnabled ? 1 : 0)));
    }

    private int[] getFlycModeResId(DataOsdGetPushCommon.FLYC_STATE mode, boolean isVisualWork) {
        int[] resIds = {255, 255};
        DataRcGetPushParams mParams = DataRcGetPushParams.getInstance();
        if (DataOsdGetPushCommon.FLYC_STATE.Manula == mode) {
            resIds[0] = 0;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti == mode) {
            resIds[0] = 1;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti_CL == mode) {
            resIds[0] = 1;
            resIds[1] = 0;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti_Hover == mode) {
            resIds[0] = 1;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti_Limited == mode) {
            resIds[0] = 1;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AttiLangding == mode) {
            resIds[0] = 2;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AutoLanding == mode) {
            resIds[0] = 2;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AssitedTakeoff == mode) {
            resIds[0] = 3;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AutoTakeoff == mode) {
            resIds[0] = 3;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GoHome == mode) {
            resIds[0] = 4;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_Atti == mode) {
            resIds[0] = 10;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_Blake == mode) {
            resIds[0] = 10;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_CL == mode) {
            resIds[0] = 10;
            resIds[1] = 0;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_HomeLock == mode) {
            resIds[0] = 10;
            resIds[1] = 1;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_HotPoint == mode) {
            resIds[0] = 10;
            resIds[1] = 2;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Hover == mode) {
            resIds[0] = 10;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Joystick == mode) {
            resIds[0] = 5;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviGo == mode) {
            resIds[0] = 6;
        } else if (DataOsdGetPushCommon.FLYC_STATE.ClickGo == mode) {
            resIds[0] = 7;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviSubMode_Tracking == mode) {
            resIds[0] = 14;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviSubMode_Pointing == mode) {
            resIds[0] = 15;
        } else if (DataOsdGetPushCommon.FLYC_STATE.SPORT == mode) {
            resIds[0] = 16;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NOVICE == mode) {
            resIds[0] = 17;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviSubMode_Draw == mode) {
            resIds[0] = 18;
        }
        if (resIds[0] == 10) {
            if (isVisualWork) {
                resIds[0] = 9;
            }
        } else if (resIds[0] == 1) {
            int modeChannel = DataOsdGetPushCommon.getInstance().getModeChannel().value();
            if (!DataOsdGetPushHome.getInstance().isMultipleModeOpen() || modeChannel == 0 || modeChannel == 2) {
                resIds[0] = 8;
            }
        }
        if ((resIds[0] == 10 || resIds[0] == 8 || resIds[0] == 9) && mParams.getMode() == 2) {
            resIds[0] = resIds[0] + 3;
        }
        return resIds;
    }

    private boolean isOldMC() {
        DataOsdGetPushCommon.DroneType type = DataOsdGetPushCommon.getInstance().getDroneType();
        return type == DataOsdGetPushCommon.DroneType.A2 || type == DataOsdGetPushCommon.DroneType.WKM || type == DataOsdGetPushCommon.DroneType.NAZA;
    }

    /* access modifiers changed from: protected */
    public boolean isDataProtectionAssistantSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isPropellerCalibrationSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isVirtualFenceSupported() {
        return false;
    }

    private int getGpsLevelForOldFlightController(int gpsNum) {
        if (gpsNum == 0 || gpsNum >= 50) {
            return 0;
        }
        if (gpsNum <= 7) {
            return 1;
        }
        if (gpsNum > 10) {
            return 5;
        }
        return gpsNum - 6;
    }

    @Getter(FlightControllerKeys.WAYPOINT_MISSION_SPEED)
    public void getWaypointMissionSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycDownloadWayPointMissionMsg downloadWayPointMissionMsg = new DataFlycDownloadWayPointMissionMsg();
        downloadWayPointMissionMsg.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass72 */

            public void onSuccess(Object model) {
                if (downloadWayPointMissionMsg.getResult() == 0) {
                    CallbackUtils.onSuccess(callback2, Float.valueOf(downloadWayPointMissionMsg.getIdleSpeed()));
                } else {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Getter(FlightControllerKeys.WAYPOINT_PROTOCOL_VERSION)
    public void getWaypointProtocolVersion(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlyc2GetWaypointInfo getter = new DataFlyc2GetWaypointInfo();
        getter.setCmdType(DataFlyc2GetWaypointInfo.CmdType.VERSION).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass73 */

            public void onSuccess(Object model) {
                if (getter.getResult() == 0) {
                    CallbackUtils.onSuccess(callback2, Integer.valueOf(getter.getVersion()));
                } else {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Getter(FlightControllerKeys.MAX_WAYPOINT_NUM)
    public void getMaxWaypointNum(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlyc2GetWaypointInfo getter = new DataFlyc2GetWaypointInfo();
        getter.setCmdType(DataFlyc2GetWaypointInfo.CmdType.WAYPOINT_MAX_NUM).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass74 */

            public void onSuccess(Object model) {
                if (getter.getResult() == 0) {
                    CallbackUtils.onSuccess(callback2, Integer.valueOf(getter.getMaxWaypointNum()));
                } else {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                if (ccode == Ccode.TIMEOUT) {
                    FlightControllerAbstraction.this.notifyValueChangeForKeyPath(-1, FlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.MAX_WAYPOINT_NUM));
                }
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Getter(FlightControllerKeys.AUTO_LANDING_GEAR)
    public void getAutoLandingGear(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{"g_config.gear_cfg.auto_control_enable_0"}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass75 */

            public void onSuccess(Object model) {
                boolean z = true;
                ParamInfo disInfo = DJIFlycParamInfoManager.read("g_config.gear_cfg.auto_control_enable_0");
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback2;
                if (disInfo.value.intValue() != 1) {
                    z = false;
                }
                CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.AUTO_LANDING_GEAR)
    public void setAutoLandingGear(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int i = 1;
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs("g_config.gear_cfg.auto_control_enable_0");
        Number[] numberArr = new Number[1];
        if (!enabled) {
            i = 0;
        }
        numberArr[0] = Integer.valueOf(i);
        setParams.setValues(numberArr);
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass76 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.AUTO_LANDING_GEAR_GROUND_NOTIFY)
    public void getAutoLandingGearGroundNotify(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycGetParams().setInfos(new String[]{"g_config.gear_cfg.near_ground_reminder_0"}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass77 */

            public void onSuccess(Object model) {
                boolean z = true;
                ParamInfo disInfo = DJIFlycParamInfoManager.read("g_config.gear_cfg.near_ground_reminder_0");
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback2;
                if (disInfo.value.intValue() != 1) {
                    z = false;
                }
                CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.AUTO_LANDING_GEAR_GROUND_NOTIFY)
    public void setAutoLandingGearGroundNotify(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        int i = 1;
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs("g_config.gear_cfg.near_ground_reminder_0");
        Number[] numberArr = new Number[1];
        if (!enabled) {
            i = 0;
        }
        numberArr[0] = Integer.valueOf(i);
        setParams.setValues(numberArr);
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass78 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_RC_SCALE_IN_AVOIDANCE)
    public void setRcScaleInAvoidance(float value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_RC_SCALE_IN_AVOIDANCE, Float.valueOf(value), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass79 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_RC_SCALE_IN_AVOIDANCE)
    public void getRcScaleInAvoidance(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_RC_SCALE_IN_AVOIDANCE, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass80 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Float.valueOf(((Number) o).floatValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_RC_SCALE_IN_NORMAL)
    public void setRcScaleInNormal(float value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_RC_SCALE_IN_NORMAL, Float.valueOf(value), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass81 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_RC_SCALE_IN_NORMAL)
    public void getRcScaleInNormal(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_RC_SCALE_IN_NORMAL, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass82 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Float.valueOf(((Number) o).floatValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_RC_SCALE_IN_SPORT)
    public void setRcScaleInSport(float value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_RC_SCALE_IN_SPORT, Float.valueOf(value), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass83 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_RC_SCALE_IN_SPORT)
    public void getRcScaleInSport(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_RC_SCALE_IN_SPORT, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass84 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Float.valueOf(((Number) o).floatValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_YAW_RATE_IN_AVOIDANCE)
    public void setYawRateInAvoidance(int value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_YAW_RATE_IN_AVOIDANCE, Integer.valueOf(value), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass85 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_YAW_RATE_IN_AVOIDANCE)
    public void getYawRateInAvoidance(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_YAW_RATE_IN_AVOIDANCE, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass86 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(((Number) o).intValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_YAW_RATE_IN_NORMAL)
    public void setYawRateInNormal(int value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_YAW_RATE_IN_NORMAL, Integer.valueOf(value), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass87 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_YAW_RATE_IN_NORMAL)
    public void getYawRateInNormal(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_YAW_RATE_IN_NORMAL, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass88 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(((Number) o).intValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.HOTPOINT_MAX_ACCELERATION)
    public void getCFGHotpointMaxAcceleration(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.HOTPOINT_MAX_ACCELERATION, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass89 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Float.valueOf(((Number) o).floatValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.HOTPOINT_MIN_RADIUS)
    public void getCFGHotpointMinRadius(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.HOTPOINT_MIN_RADIUS, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass90 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Float.valueOf(((Number) o).floatValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_YAW_RATE_IN_SPORT)
    public void setYawRateInSport(int value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_YAW_RATE_IN_SPORT, Integer.valueOf(value), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass91 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_YAW_RATE_IN_SPORT)
    public void getYawRateInSport(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_YAW_RATE_IN_SPORT, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass92 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback2, Integer.valueOf(((Number) o).intValue()));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CONFIG_RTH_IN_CURRENT_ALTITUDE)
    public void isRTHInCurrentAltitudeEnabled(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CONFIG_RTH_IN_CURRENT_ALTITUDE, CallbackUtils.getFlightControllerDetaultMergeGetCallback(Boolean.class, callback2));
    }

    @Setter(FlightControllerKeys.CONFIG_RTH_IN_CURRENT_ALTITUDE)
    public void setRTHInCurrentAltitudeEnabled(Boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_RTH_IN_CURRENT_ALTITUDE, Integer.valueOf(CacheHelper.toBool(enabled) ? 1 : 0), CallbackUtils.getFlightControllerDefaultMergeSetCallback(callback2));
    }

    @Getter(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY)
    public void getCinematicBrakeSensitivity(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY, CallbackUtils.getFlightControllerDetaultMergeGetCallback(Integer.class, callback2));
    }

    @Setter(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY)
    public void setCinematicBrakeSensitivity(final int value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        CacheHelper.getFlightController(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY_RANGE, new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass93 */

            public void onSuccess(DJISDKCacheParamValue paramValue) {
                DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) paramValue.getData();
                if (capability.getMin().intValue() > value || capability.getMax().intValue() < value) {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.INVALID_PARAMETER);
                } else {
                    FlightControllerAbstraction.this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY, Integer.valueOf(value), CallbackUtils.getFlightControllerDefaultMergeSetCallback(callback2));
                }
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY_RANGE)
    public void getCinematicBrakeSensitivityRange(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DJISDKCacheProductConfigManager.getInstance().readConfigRange(FlightControllerKeys.CINEMATIC_BRAKE_SENSITIVITY, new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass94 */

            public void onSuccess(Object o) {
                DJISDKCacheProductConfigManager.ConfigRange configRange = (DJISDKCacheProductConfigManager.ConfigRange) o;
                CallbackUtils.onSuccess(callback2, new DJIParamMinMaxCapability(true, configRange.minValue, configRange.maxValue));
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CINEMATIC_YAW_SPEED)
    public void getCinematicYawSpeed(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.CINEMATIC_YAW_SPEED, CallbackUtils.getFlightControllerDetaultMergeGetCallback(Float.class, callback2));
    }

    @Setter(FlightControllerKeys.CINEMATIC_YAW_SPEED)
    public void setCinematicYawSpeed(final float value, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        CacheHelper.getFlightController(FlightControllerKeys.CINEMATIC_YAW_SPEED_RANGE, new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass95 */

            public void onSuccess(DJISDKCacheParamValue paramValue) {
                DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) paramValue.getData();
                if (((float) capability.getMin().intValue()) > value || ((float) capability.getMax().intValue()) < value) {
                    CallbackUtils.onFailure(callback2, DJIFlightControllerError.INVALID_PARAMETER);
                } else {
                    FlightControllerAbstraction.this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CINEMATIC_YAW_SPEED, Float.valueOf(value), CallbackUtils.getFlightControllerDefaultMergeSetCallback(callback2));
                }
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Getter(FlightControllerKeys.CINEMATIC_YAW_SPEED_RANGE)
    public void getCinematicYawSpeedRange(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DJISDKCacheProductConfigManager.getInstance().readConfigRange(FlightControllerKeys.CINEMATIC_YAW_SPEED, new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass96 */

            public void onSuccess(Object o) {
                DJISDKCacheProductConfigManager.ConfigRange configRange = (DJISDKCacheProductConfigManager.ConfigRange) o;
                CallbackUtils.onSuccess(callback2, new DJIParamMinMaxCapability(true, configRange.minValue, configRange.maxValue));
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Action(FlightControllerKeys.CONFIRM_SMART_RETURN_TO_HOME_REQUEST)
    public void confirmSmartReturnToHomeRequest(final DJISDKCacheHWAbstraction.InnerCallback callback2, boolean confirmed) {
        int data2;
        if (this.internalFCVerion < 13) {
            CallbackUtils.onFailure(callback2, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (!allowToConfirmSmartRTH()) {
            CallbackUtils.onFailure(callback2, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            if (confirmed) {
                data2 = 1;
            } else {
                data2 = 2;
            }
            DataFlycSmartAck.getInstance().setAck((byte) data2).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass97 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback2, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback2, ccode);
                }
            });
        }
    }

    private boolean allowToConfirmSmartRTH() {
        Object data2 = CacheHelper.getFlightController(FlightControllerKeys.GO_HOME_ASSESSMENT);
        if (data2 == null || !(data2 instanceof GoHomeAssessment) || ((GoHomeAssessment) data2).getSmartRTHState() == SmartRTHState.COUNTING_DOWN) {
            return true;
        }
        return false;
    }

    @Getter(FlightControllerKeys.SMART_RETURN_TO_HOME_ENABLED)
    public void getSmartReturnToHomeEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.SMART_RETURN_TO_HOME_ENABLED, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass98 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback2, Boolean.valueOf(CacheHelper.toInt(object) == 2));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback2, error);
            }
        });
    }

    @Setter(FlightControllerKeys.SMART_RETURN_TO_HOME_ENABLED)
    public void setSmartReturnToHomeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback2) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.SMART_RETURN_TO_HOME_ENABLED, Integer.valueOf(CacheHelper.toBool(Boolean.valueOf(enabled)) ? 2 : 1), CallbackUtils.getFlightControllerDefaultMergeSetCallback(callback2));
    }

    @Getter(FlightControllerKeys.ACTIVATION_TIME)
    public void getActivationTime(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        getDataFlycActiveStatus(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass99 */

            public void onSuccess(Object status) {
                CallbackUtils.onSuccess(callback2, Long.valueOf(((DataFlycActiveStatus) status).getTime()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.UPGRADE_VOICE_OPEN)
    public void setUpgradeVoiceOpen(boolean open, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataCommonSetGet1860TipsAudio.AudioSubCmd audioSubCmd;
        if (open) {
            audioSubCmd = DataCommonSetGet1860TipsAudio.AudioSubCmd.ENABLE_SOUND;
        } else {
            audioSubCmd = DataCommonSetGet1860TipsAudio.AudioSubCmd.DISABLE_SOUND;
        }
        DataCommonSetGet1860TipsAudio.getInstance().setAudioSubCmd(audioSubCmd).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass100 */

            public void onSuccess(Object model) {
                callback2.onSuccess(true);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Getter(FlightControllerKeys.UPGRADE_VOICE_OPEN)
    public void isUpgradeVoiceOpen(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataCommonSetGet1860TipsAudio setGet1860TipsAudio = DataCommonSetGet1860TipsAudio.getInstance();
        setGet1860TipsAudio.setAudioSubCmd(DataCommonSetGet1860TipsAudio.AudioSubCmd.GET_SOUND).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass101 */

            public void onSuccess(Object model) {
                callback2.onSuccess(Boolean.valueOf(setGet1860TipsAudio.isTipsAudioOpened()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, ccode);
            }
        });
    }

    @Action(FlightControllerKeys.RESET_MOTOR)
    public void resetMotor(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataCommonRestartDevice restart = new DataCommonRestartDevice();
        if (DJIProductManager.getInstance().getType() == ProductType.WM160) {
            restart.setReceiveType(DeviceType.DM368).setReceiveId(1).setRestartType(2);
        } else {
            restart.setReceiveType(DeviceType.FLYC).setReceiveId(0).setRestartType(1).setDelay(1000);
        }
        new RepeatDataBase(restart, 3, 200, CallbackUtils.defaultCB(callback2)).start();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(Data1860GetPushCheckStatus status) {
        if (status.isGetted()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(status.isSystemUpgradeAbnormal()), convertKeyToPath(FlightControllerKeys.IS_SYSTEM_UPGRADE_ABNORMAL));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycPushRedundancyStatus status) {
        if (status.isGetted()) {
            DataFlycPushRedundancyStatus.NavigationSystemState state2 = status.getNavigationSystemState();
            int componentCode = state2.devType;
            int errorCode = state2.devErrCode;
            NavigationSystemError.Component component = NavigationSystemError.Component.find(componentCode);
            int reasonId = 0;
            String error = "";
            if (errorCode != 0) {
                switch (component) {
                    case REMOTE_CONTROLLER:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rc_cali_error_need_cali");
                                break;
                            case 2:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rc_cali_error_invalid_params");
                                break;
                            case 3:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rc_cali_error_mapping");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rc_cali_error_center_out_range");
                                break;
                            case 5:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rc_cali_error_failed_RC_calibration_unfinished");
                                break;
                        }
                    case BATTERY:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_cell_error");
                                break;
                            case 2:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_comm_error");
                                break;
                            case 3:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_vol_low_need_charge");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_num_low_insert_more");
                                break;
                            case 5:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_auth_error_contact_assist");
                                break;
                            case 6:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_comm_error");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_vol_diff_large_check_status");
                                break;
                            case 8:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_bat_vol_diff_very_large_check_status");
                                break;
                        }
                    case SYSTEM:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_sys_locked_contact_assist");
                                break;
                            case 2:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_sys_serial_num_invalid_contact_assist");
                                break;
                            case 3:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_sys_sd_card_error_repair");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_sys_not_activated_activate_fc");
                                break;
                            case 5:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_sys_thrust_ratio_large_contact_assist");
                                break;
                        }
                    case ESC:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_esc_error_contact_assist");
                                int solutionId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_solution_contact_dji");
                                break;
                        }
                    case RADAR:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_radar_data_error");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_radar_data_error_lock_dead");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_radar_disconnect_reset_check");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_radar_mount_error_contact_assist");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_radar_version_error_contact_assist");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_radar_comm_error_contact_assist");
                                break;
                        }
                    case ULTRASONIC:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_us_data_error_contact_assist");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_us_data_error_stuck");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_us_disconnect_reset_check");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_us_mount_error_contact_assist");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_us_version_error_contact_assist");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_us_comm_error_contact_assist");
                                break;
                        }
                    case VISION_SENSOR:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_data_error_contact_assist");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_data_error_stuck");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_disconnect_reset_check");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_mount_error_contact_assist");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_version_error_contact_assist");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_comm_error_contact_assist");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_data_error_speed_change");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_vps_data_error_position_change");
                                break;
                        }
                    case RTK:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_data_error_invalid_data");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_data_error_reset_check");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_data_error_speed_change");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_data_error_position_change");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_disconnect_reset_check");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_mount_error_offeset_incomplete");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_mount_error_offset_error");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_version_error_contact_assist");
                                break;
                            case 25:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_comm_error_contact_assist");
                                break;
                            case 28:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_unfixed_contact_assist");
                                break;
                            case 31:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_heading_error_check_info");
                                break;
                            case 34:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_comm_error_frequency");
                                break;
                            case 37:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_gs_antenna_error_contact_assist");
                                break;
                            case 40:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_master_antenna_error_contact_assist");
                                break;
                            case 43:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_slave_antenna_error_contact_assist");
                                break;
                            case 46:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_rtk_comm_error_adapter_plate");
                                break;
                        }
                    case BAROMETER:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_contact_assist");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_stuck");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_disconnect_reset_check");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_exceeds_range");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_measurement_not_matched");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_chage");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_drift");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_temp_error_keep_cool");
                                break;
                            case 25:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_baro_data_error_noise");
                                break;
                        }
                    case GPS:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_data_error_invalid_data");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_disconnect_reset_check");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_data_error_exceeds_range");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_data_error_change");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_data_error_speed_not_matched");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_data_error_position_not_matched");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_mount_error_offeset_incomplete");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_mount_error_offset_error");
                                break;
                            case 25:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_version_error_contact_assist");
                                break;
                            case 28:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_update_error_frequency");
                                break;
                            case 31:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_low_accuracy_error");
                                break;
                            case 34:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gps_hdop_error_not_matched");
                                break;
                        }
                    case COMPASS:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_disconnect_reset_check");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_data_error_stuck");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_data_error_noise");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_data_error_change");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_data_error_exceeds_range");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_data_error_not_matched");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_cali_error_no_param");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_comm_error");
                                break;
                            case 25:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_bias_error");
                                break;
                            case 28:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_scale_error");
                                break;
                            case 31:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_data_error_non_orthogonal_differences");
                                break;
                            case 34:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_mount_error_heading");
                                break;
                            case 37:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_mount_error");
                                break;
                            case 40:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_error_ground_disturbance");
                                break;
                            case 43:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_error_dip_disturbance");
                                break;
                            case 46:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_compass_cali_falied");
                                break;
                        }
                    case GYROSCOPE:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_invalid_data");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_stuck");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_disconnect_reset_check");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_measurement_not_matched");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_exceeds_range");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_invalid_internal_param");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_noise");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_bias_error");
                                break;
                            case 28:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_falied");
                                break;
                            case 37:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_error_preheat");
                                break;
                            case 39:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_temp_error_will_too_high");
                                break;
                            case 42:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_temp_error_too_high");
                                break;
                            case 45:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_temp_error_too_low");
                                break;
                            case 48:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_preheat_error");
                                break;
                            case 51:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_data_error_data_synchronization");
                                break;
                            case 54:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_advanced");
                                break;
                            case 57:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_basic");
                                break;
                            case 60:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_horizontal");
                                break;
                            case 63:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_version_error");
                                break;
                            case 67:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_install_error");
                                break;
                            case 73:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_not_in_stanndard_temp");
                                break;
                            case 76:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_temp_exceeds");
                                break;
                            case 79:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_not_finished");
                                break;
                            case 82:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_temp_not_ready");
                                break;
                            case 85:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_flash_error");
                                break;
                            case 88:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_cali_error_direction_rong");
                                break;
                            case 91:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_gyroscope_error_moved");
                                break;
                        }
                    case ACCELEROMETER:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_invalid_data");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_stuck");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_disconnect_reset_check");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_measurement_not_matched");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_exceeds_range");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_invalid_internal_param");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_noise");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_bias_error");
                                break;
                            case 28:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_falied");
                                break;
                            case 31:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_mount_error_offeset_incomplete");
                                break;
                            case 34:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_mount_error_offset_error");
                                break;
                            case 37:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_error_preheat");
                                break;
                            case 39:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_temp_error_will_too_high");
                                break;
                            case 42:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_temp_error_too_high");
                                break;
                            case 45:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_temp_error_too_low");
                                break;
                            case 48:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_preheat_error");
                                break;
                            case 51:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_data_error_data_synchronization");
                                break;
                            case 54:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_advanced");
                                break;
                            case 57:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_basic");
                                break;
                            case 60:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_horizontal");
                                break;
                            case 63:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_version_error");
                                break;
                            case 67:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_install_error");
                                break;
                            case 70:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_internal_offset");
                                break;
                            case 73:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_not_in_stanndard_temp");
                                break;
                            case 76:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_temp_exceeds");
                                break;
                            case 79:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_not_finished");
                                break;
                            case 82:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_temp_not_ready");
                                break;
                            case 85:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_flash_error");
                                break;
                            case 88:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_cali_error_direction_rong");
                                break;
                            case 91:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_accelerometer_error_moved");
                                break;
                        }
                    case IMU:
                        switch (errorCode) {
                            case 1:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_error_disconnected");
                                break;
                            case 2:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_version_error");
                                break;
                            case 4:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_init_error");
                                break;
                            case 5:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_initializing");
                                break;
                            case 7:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_height_change_rapidly");
                                break;
                            case 10:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_not_divergence");
                                break;
                            case 13:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_data_error_invalid_data");
                                break;
                            case 16:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_data_error_height_difference");
                                break;
                            case 19:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_settle_big_acceleration");
                                break;
                            case 22:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_settle_atitude_difference");
                                break;
                            case 25:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_height_drift");
                                break;
                            case 28:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_angular_velocity_not_matched");
                                break;
                            case 31:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_acceleration_not_matched");
                                break;
                            case 34:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_vertical_velocity_not_matched");
                                break;
                            case 37:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_height_not_matched");
                                break;
                            case 40:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_attitude_not_matched");
                                break;
                            case 43:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_course_not_matched");
                                break;
                            case 47:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_horizontal_velocity_not_matched");
                                break;
                            case 50:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_position_velocity_not_matched");
                                break;
                            case 53:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_compass_course_measure_difference");
                                break;
                            case 56:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_gps_course_measure_difference");
                                break;
                            case 59:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_horizontal_velocity_measure_difference");
                                break;
                            case 62:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_gyroscope_bias_estimation_error");
                                break;
                            case 65:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_filters_horizontal_attitude_not_matched");
                                break;
                            case 68:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_filters_vertical_velocity_not_matched");
                                break;
                            case 71:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_filters_course_not_matched");
                                break;
                            case 74:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_small_filters_horizontal_attitude_not_matched");
                                break;
                            case 77:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_small_filters_vertical_velocity_not_matched");
                                break;
                            case 88:
                                reasonId = SDKUtils.getMidwareResourceId(MidWare.context.get(), "navigation_system_tip_imu_small_filters_course_not_matched");
                                break;
                        }
                }
                error = SDKUtils.getMidwareString(MidWare.context.get(), reasonId);
            }
            notifyValueChangeForKeyPath(new NavigationSystemError(component, error), convertKeyToPath(FlightControllerKeys.NAVIGATION_SYSTEM_ERROR));
        }
    }

    /* renamed from: dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction$106  reason: invalid class name */
    static /* synthetic */ class AnonymousClass106 {
        static final /* synthetic */ int[] $SwitchMap$dji$midware$data$model$P3$DataFlycSetGetVerPhone$FlycPhoneStatus = new int[DataFlycSetGetVerPhone.FlycPhoneStatus.values().length];

        static {
            $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component = new int[NavigationSystemError.Component.values().length];
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.REMOTE_CONTROLLER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.BATTERY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.SYSTEM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.ESC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.RADAR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.ULTRASONIC.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.VISION_SENSOR.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.RTK.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.BAROMETER.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.GPS.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.COMPASS.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.GYROSCOPE.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.ACCELEROMETER.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$dji$common$flightcontroller$NavigationSystemError$Component[NavigationSystemError.Component.IMU.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel = new int[DataOsdGetPushCommon.RcModeChannel.values().length];
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_P.ordinal()] = 1;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_F.ordinal()] = 2;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_G.ordinal()] = 3;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_M.ordinal()] = 4;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_A.ordinal()] = 5;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_S.ordinal()] = 6;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataOsdGetPushCommon$RcModeChannel[DataOsdGetPushCommon.RcModeChannel.CHANNEL_T.ordinal()] = 7;
            } catch (NoSuchFieldError e21) {
            }
            $SwitchMap$dji$common$realname$AircraftBindingState = new int[AircraftBindingState.values().length];
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.UNBOUND.ordinal()] = 1;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.UNBOUND_BUT_CANNOT_SYNC.ordinal()] = 2;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.NOT_REQUIRED.ordinal()] = 3;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.BOUND.ordinal()] = 4;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.INITIAL.ordinal()] = 5;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.UNKNOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$dji$common$realname$AircraftBindingState[AircraftBindingState.NOT_SUPPORTED.ordinal()] = 7;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataFlycSetGetVerPhone$FlycPhoneStatus[DataFlycSetGetVerPhone.FlycPhoneStatus.Unknown.ordinal()] = 1;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataFlycSetGetVerPhone$FlycPhoneStatus[DataFlycSetGetVerPhone.FlycPhoneStatus.BindOk.ordinal()] = 2;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataFlycSetGetVerPhone$FlycPhoneStatus[DataFlycSetGetVerPhone.FlycPhoneStatus.NotBind.ordinal()] = 3;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataFlycSetGetVerPhone$FlycPhoneStatus[DataFlycSetGetVerPhone.FlycPhoneStatus.NeedBind.ordinal()] = 4;
            } catch (NoSuchFieldError e32) {
            }
        }
    }

    @Setter(FlightControllerKeys.LOCK_MOTORS)
    public void lockMotors(final boolean isLock, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycSetMotorForceDisable().setDisable(isLock).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass102 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, Boolean.valueOf(isLock));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.AERO_SCOPE_CLIENT_SWITCH)
    public void getAeroScopeClientSwitch(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataFlycDetection dataFlycDetection = new DataFlycDetection(DataFlycDetection.SubCmdId.GetSwitch);
        dataFlycDetection.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass103 */

            public void onSuccess(Object model) {
                if (dataFlycDetection.getCcode() == Ccode.OK) {
                    CallbackUtils.onSuccess(callback2, new AeroScopeClientSwitch(dataFlycDetection.getEnable()));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.AERO_SCOPE_CLIENT_SWITCH)
    public void setAeroScopeClientSwitch(final AeroScopeClientSwitch switchStatus, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataFlycDetection(DataFlycDetection.SubCmdId.SetSwitch).setEnable(switchStatus.toArray()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass104 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, switchStatus);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIError.getDJIError(ccode));
            }
        });
    }

    @Action(FlightControllerKeys.GO_HOME_ACTION_COUNT_DOWN_CONFIRM)
    public void goHomeCountDownImmediately(final DJISDKCacheHWAbstraction.InnerCallback callback2, boolean confirmed) {
        new DataFlycGetPushGoHomeCountDown().setCmdType(1).setSendAction(confirmed ? 1 : 0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.AnonymousClass105 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
    }
}
