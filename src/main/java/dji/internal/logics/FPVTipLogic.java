package dji.internal.logics;

import android.os.Handler;
import dji.common.bus.LogicEventBus;
import dji.common.remotecontroller.ChargeRemaining;
import dji.common.util.HistoryInfo;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.Message;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.MidWare;
import dji.midware.R;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoEvent;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPushCheckStatus;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataGimbalGetPushCheckStatus;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;
import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.data.model.P3.DataWifiGetPushSignal;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.CmdIdInterface;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FPVTipLogic implements DJIParamAccessListener {
    private static final int ARG_UPDATE_2100_CHECKSTATUS = 1;
    private static final int ARG_UPDATE_ALL = 16383;
    private static final int ARG_UPDATE_BATTERY_COMMON = 4;
    private static final int ARG_UPDATE_BATTERY_DYNAMIC_DATA = 1024;
    private static final int ARG_UPDATE_CAMERA_STATEINFO = 2;
    private static final int ARG_UPDATE_FLYC_CHECKSTATUS = 8;
    private static final int ARG_UPDATE_GIMBAL_CHECKSTATUS = 16384;
    private static final int ARG_UPDATE_GIMBAL_PARAM = 32;
    private static final int ARG_UPDATE_NONE = 0;
    private static final int ARG_UPDATE_OSD_CHLSTATUS = 64;
    private static final int ARG_UPDATE_OSD_COMMON = 128;
    private static final int ARG_UPDATE_OSD_HOME = 8192;
    private static final int ARG_UPDATE_OSD_SIGNAL = 256;
    private static final int ARG_UPDATE_RC_BATTERYINFO = 512;
    private static final int ARG_UPDATE_SMART_BATTERY = 16;
    private static final int ARG_UPDATE_WIFI_ELEC_SIGNAL = 2048;
    private static final int ARG_UPDATE_WIFI_SIGNAL = 4096;
    private static final long DELAY_UPDATE = 100;
    private static final long DELAY_UPDATE_PARAMS = 2000;
    private static final long FLAG_ATTI_STATE = 576460752303423488L;
    private static final long FLAG_ATTI_STATE_IN_THE_AIR = 288230376151711744L;
    private static final long FLAG_BACK_VISION_CALI = 32768;
    private static final long FLAG_BAROMETER_DEAD = 4096;
    private static final long FLAG_BATTERY_BROKEN = 524288;
    private static final long FLAG_BATTERY_CONN_EXCEPTION = 131072;
    private static final long FLAG_BATTERY_EXCEPTION = 262144;
    private static final long FLAG_BATTERY_LOW_TEMP = 4194304;
    private static final long FLAG_BATTERY_OVER_CURRENT = 1048576;
    private static final long FLAG_BATTERY_OVER_TEMP = 2097152;
    private static final long FLAG_BATTERY_SELF_RELEASE = 8388608;
    private static final long FLAG_CAMERA_ENCRYPT_ERROR = 128;
    private static final long FLAG_CANT_TAKEOFF = 2147483648L;
    private static final long FLAG_CANT_TAKEOFF_NOVICE = 1073741824;
    private static final long FLAG_CHLSTATUS_POOR = 1152921504606846976L;
    private static final long FLAG_COMPASS_DISTURB = 2048;
    private static final long FLAG_COMPASS_ERROR = 256;
    private static final long FLAG_DEVICE_LOCK = 536870912;
    private static final long FLAG_DISCONNECT = 1;
    private static final long FLAG_DOWN_VISION_CALI = 16384;
    private static final long FLAG_ESC_ERROR = 1024;
    private static final long FLAG_ESC_ERROR_SKY = 512;
    private static final long FLAG_FAILSAFE = 8796093022208L;
    private static final long FLAG_FAILSAFE_GOHOME = 4398046511104L;
    private static final long FLAG_FRONT_VISION_CALI = 8192;
    private static final long FLAG_GALE_WARNING = 2251799813685248L;
    private static final long FLAG_GIMBAL_END_POINT_OVERLOAD = 18014398509481984L;
    private static final long FLAG_GIMBAL_END_POINT_STUCK = 9007199254740992L;
    private static final long FLAG_GIMBAL_STUCK = 4503599627370496L;
    private static final long FLAG_GIMBAL_VIBRATION = 36028797018963968L;
    private static final long FLAG_GOHOME = 2305843009213693952L;
    private static final long FLAG_GOHOME_FAILED = 2199023255552L;
    private static final long FLAG_HD_ERROR = 8;
    private static final long FLAG_IMU_CALI = 64;
    private static final long FLAG_IMU_COMPASS_ERROR = 134217728;
    private static final long FLAG_IMU_ERROR = 67108864;
    private static final long FLAG_IMU_HEATING = 268435456;
    private static final long FLAG_IMU_INITIALIZING = 16777216;
    private static final long FLAG_LOW_POWER = 35184372088832L;
    private static final long FLAG_LOW_POWER_GOHOME = 17592186044416L;
    private static final long FLAG_LOW_RADIO_SIGNAL = 281474976710656L;
    private static final long FLAG_LOW_RC_POWER = 70368744177664L;
    private static final long FLAG_LOW_RC_SIGNAL = 140737488355328L;
    private static final long FLAG_LOW_VOLTAGE = 274877906944L;
    private static final long FLAG_MC_DATA_ERROR = 32;
    private static final long FLAG_NEED_UPDATE = 1099511627776L;
    private static final long FLAG_NON_GPS = 144115188075855872L;
    private static final long FLAG_NON_GPS_IN_THE_AIR = 72057594037927936L;
    private static final long FLAG_NORMAL = 0;
    private static final long FLAG_NORMAL_IN_THE_AIR = 4611686018427387904L;
    private static final long FLAG_NOT_ENOUGH_FORCE = 549755813888L;
    private static final long FLAG_NO_VIDEO_SIGNAL = 16;
    private static final long FLAG_RADIO_SIGNAL_DISTURB = 1125899906842624L;
    private static final long FLAG_RC_SIGNAL_DISTURB = 562949953421312L;
    private static final long FLAG_REMOTE_DISCONNECT = 2;
    private static final long FLAG_SENSOR_ERROR = 33554432;
    private static final long FLAG_SERIOUS_LOW_POWER = 34359738368L;
    private static final long FLAG_SERIOUS_LOW_POWER_LANDING = 17179869184L;
    private static final long FLAG_SERIOUS_LOW_VOLTAGE = 8589934592L;
    private static final long FLAG_SERIOUS_LOW_VOLTAGE_LANDING = 4294967296L;
    private static final long FLAG_SMART_LOW_POWER = 137438953472L;
    private static final long FLAG_SMART_LOW_POWER_LANDING = 68719476736L;
    private static final long FLAG_USB_MODE = 4;
    private static final long FLAG_VISION_ERROR = 65536;
    private static final long INIT_STATUS = 19;
    private static final int INVALID = -1;
    private static final int MSG_ID_COMPASS_DISTURB = 4096;
    private static final int MSG_ID_COMPASS_ERROR = 4097;
    private static final int MSG_ID_LOWPOWER_GOHOME = 4098;
    private static final int MSG_ID_POWER_ENOUGH = 4099;
    private static final int MSG_ID_UPDATE = 256;
    private static final int MSG_ID_UPDATE_PARAMS = 4100;
    private static final int MSG_ID_UPDATE_STATUS = 4101;
    private static DJISDKCacheKey batteryPercentKey1;
    private static DJISDKCacheKey batteryPercentKey2;
    private static DJISDKCacheKey fcsnKey;
    private static Message[] resDatas;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private String[] SENSORS_PARAMS;
    private HistoryInfo batteryHistory;
    private long flagStatus;
    private long lastCompassDisturbTime;
    private final DataFlycGetParams paramGetter;
    private Message previousMessage;
    private volatile Subscription subscriptionData2100CheckStatus;
    /* access modifiers changed from: private */
    public TipHandler tipHandler;
    private volatile int updateFlag;

    private void initRes() {
        resDatas = new Message[]{new Message(Message.Type.GOOD, false, getString(R.string.fpv_tip_normal), 0), new Message(Message.Type.OFFLINE, false, getString(R.string.fpv_tip_disconnect), 1), new Message(Message.Type.ERROR, false, getString(dji.sdkcache.R.string.fpv_tip_remote_disconnect), 2), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_mc_sd_mode), 4), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_no_video_signal), 8), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_no_video_signal), 16), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_flyc_data_error), 32), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_imu_cali), 64), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_camera_encrypt_error), 128), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_takeoff_with_compass_error), 256), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_esc_error_sky), 512), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_esc_error), 1024), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_patti_error), 2048), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_barometer_dead), 4096), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_front_vision_cali), 8192), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_down_vision_cali), 16384), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_back_vision_cali), 32768), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_vision_error), 65536), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_battery_exception), 131072), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_battery_status_error), 262144), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_battery_damaged), 524288), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_battery_over_current), 1048576), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_battery_over_temp), 2097152), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_battery_low_temp), FLAG_BATTERY_LOW_TEMP), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_normal), FLAG_BATTERY_SELF_RELEASE), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_imu_initializing), FLAG_IMU_INITIALIZING), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_sensor_error), FLAG_SENSOR_ERROR), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_imu_error), FLAG_IMU_ERROR), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_takeoff_with_compass_error), FLAG_IMU_COMPASS_ERROR), new Message(Message.Type.WARNING, true, getString(R.string.fpv_tip_imu_heating), FLAG_IMU_HEATING), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_cant_takeoff_locked), FLAG_DEVICE_LOCK), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_cant_takeoff_novice), FLAG_CANT_TAKEOFF_NOVICE), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_cant_takeoff), FLAG_CANT_TAKEOFF), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_serious_low_voltage_landing), FLAG_SERIOUS_LOW_VOLTAGE_LANDING), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_serious_low_voltage), FLAG_SERIOUS_LOW_VOLTAGE), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_smart_low_power_landing), FLAG_SERIOUS_LOW_POWER_LANDING), new Message(Message.Type.ERROR, true, getString(dji.sdkcache.R.string.fpv_tip_serious_low_power), FLAG_SERIOUS_LOW_POWER), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_smart_low_power_landing), FLAG_SMART_LOW_POWER_LANDING), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_smart_low_power), FLAG_SMART_LOW_POWER), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_low_voltage), FLAG_LOW_VOLTAGE), new Message(Message.Type.WARNING, true, getString(R.string.fpv_tip_not_enough_force), FLAG_NOT_ENOUGH_FORCE), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_firmware_update_required), FLAG_NEED_UPDATE), new Message(Message.Type.ERROR, false, getString(R.string.fpv_tip_gohome_failed), FLAG_GOHOME_FAILED), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_failsafe_gohome), FLAG_FAILSAFE_GOHOME), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_failsafe), FLAG_FAILSAFE), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_low_power_gohome), FLAG_LOW_POWER_GOHOME), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_low_power), FLAG_LOW_POWER), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_low_rc_power), FLAG_LOW_RC_POWER), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_low_rc_signal), FLAG_LOW_RC_SIGNAL), new Message(Message.Type.WARNING, true, getString(R.string.fpv_tip_low_radio_signal), FLAG_LOW_RADIO_SIGNAL), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_rc_disturbance), FLAG_RC_SIGNAL_DISTURB), new Message(Message.Type.ERROR, true, getString(R.string.fpv_tip_video_disturbance), FLAG_RADIO_SIGNAL_DISTURB), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_gale_warning), FLAG_GALE_WARNING), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_gimbal_stuck), FLAG_GIMBAL_STUCK), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_gimbal_startup_block), FLAG_GIMBAL_END_POINT_STUCK), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_gimbal_wait_restart_detail), FLAG_GIMBAL_END_POINT_OVERLOAD), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_check_gimbal_vibration_detail), FLAG_GIMBAL_VIBRATION), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_non_gps_in_the_air), FLAG_NON_GPS_IN_THE_AIR), new Message(Message.Type.GOOD, false, getString(R.string.fpv_tip_non_gps), FLAG_NON_GPS), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_atti_state), FLAG_ATTI_STATE_IN_THE_AIR), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_atti_state_in_the_air), FLAG_ATTI_STATE), new Message(Message.Type.WARNING, false, getString(R.string.fpv_tip_chlstatus_poor), FLAG_CHLSTATUS_POOR), new Message(Message.Type.GOOD, false, getString(R.string.fpv_tip_gohome), FLAG_GOHOME), new Message(Message.Type.GOOD, false, getString(R.string.fpv_tip_normal_in_the_air), FLAG_NORMAL_IN_THE_AIR)};
    }

    private String getString(int resId) {
        if (MidWare.context == null || MidWare.context.get() == null) {
            return "";
        }
        return MidWare.context.get().getString(resId);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if ("ChargeRemaining".equals(key.getParamKey())) {
            updateRcBattery();
        }
    }

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static FPVTipLogic instance = new FPVTipLogic();

        private HOLDER() {
        }
    }

    private FPVTipLogic() {
        this.paramGetter = new DataFlycGetParams();
        this.SENSORS_PARAMS = new String[]{ParamCfgName.GSTATUS_TOPOLOGY_VERIFY_IMU, ParamCfgName.GSTATUS_TOPOLOGY_VERIFY_MAG};
        this.flagStatus = INIT_STATUS;
        this.updateFlag = 0;
        this.batteryHistory = new HistoryInfo();
        this.lastCompassDisturbTime = 0;
        this.tipHandler = null;
    }

    public static FPVTipLogic getInstance() {
        return HOLDER.instance;
    }

    public void init() {
        this.previousMessage = null;
        if (this.subscriptionData2100CheckStatus == null) {
            initRes();
            this.tipHandler = new TipHandler(this);
            DJIEventBusUtil.register(this);
            this.subscriptionData2100CheckStatus = LogicEventBus.getInstance().register(Data2100GetPushCheckStatus.class).subscribeOn(Schedulers.computation()).subscribe(new FPVTipLogic$$Lambda$0(this));
            CacheHelper.addRemoteControllerListener(this, "ChargeRemaining");
        }
        resetStatus();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$init$0$FPVTipLogic(Data2100GetPushCheckStatus data2100GetPushCheckStatus) {
        handleDataPush(1);
    }

    public void destroy() {
        CacheHelper.removeListener(this);
        DJIEventBusUtil.unRegister(this);
        if (this.subscriptionData2100CheckStatus != null && !this.subscriptionData2100CheckStatus.isUnsubscribed()) {
            this.subscriptionData2100CheckStatus.unsubscribe();
            this.subscriptionData2100CheckStatus = null;
        }
        if (this.tipHandler != null) {
            this.tipHandler.removeCallbacksAndMessages(null);
            this.tipHandler.destroy();
        }
    }

    public static final class FPVTipEvent {
        private final Message message;

        public FPVTipEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }

    private void resetStatus() {
        if (ServiceManager.getInstance().isConnected()) {
            connect();
        } else {
            disconnect();
        }
        if (ServiceManager.getInstance().isRemoteOK()) {
            cameraConnect();
        } else {
            cameraDisconnect();
        }
    }

    public void connect() {
        this.flagStatus &= -2;
        updateMessage();
    }

    public void disconnect() {
        boolean noVideo = hasFlag(this.flagStatus, 16);
        this.flagStatus = 1;
        this.flagStatus |= 2;
        if (noVideo) {
            saveLog(16, null);
            this.flagStatus |= 16;
        }
        updateMessage();
    }

    private void cameraConnect() {
        this.flagStatus &= -3;
        update(true);
        if (!this.tipHandler.hasMessages(4100)) {
            this.tipHandler.sendEmptyMessageDelayed(4100, DELAY_UPDATE_PARAMS);
        }
    }

    private void cameraDisconnect() {
        this.flagStatus &= -5;
        this.flagStatus |= 2;
        saveLog(2, null);
        updateMessage();
    }

    private void handleDataPush(int flag) {
        if ((this.updateFlag & flag) == 0) {
            this.updateFlag |= flag;
            if (!this.tipHandler.hasMessages(256)) {
                this.tipHandler.sendEmptyMessageDelayed(256, DELAY_UPDATE);
            }
        }
    }

    /* access modifiers changed from: private */
    public void update(boolean all) {
        if (all) {
            this.updateFlag |= ARG_UPDATE_ALL;
        }
        long flags = this.flagStatus;
        if ((this.updateFlag & 1) != 0) {
            update2100CheckStatus();
            this.updateFlag &= -2;
        }
        if ((this.updateFlag & 2) != 0) {
            updateCameraStateInfo();
            this.updateFlag &= -3;
        }
        if ((this.updateFlag & 4) != 0) {
            updateBatteryCommon();
            this.updateFlag &= -5;
        }
        if ((this.updateFlag & 8) != 0) {
            updateFlycCheckStatus();
            this.updateFlag &= -9;
        }
        if ((this.updateFlag & 16) != 0) {
            updateFlycSmartBattery();
            this.updateFlag &= -17;
        }
        if ((this.updateFlag & 32) != 0) {
            updateGimbalParam();
            this.updateFlag &= -33;
        }
        if ((this.updateFlag & 128) != 0) {
            updateOsdCommon();
            this.updateFlag &= -129;
        }
        if ((this.updateFlag & 256) != 0) {
            updateOsdSignal();
            this.updateFlag &= -257;
        }
        if ((this.updateFlag & 512) != 0) {
            updateRcBattery();
            this.updateFlag &= -513;
        }
        if ((this.updateFlag & 1024) != 0) {
            updateBatteryDynamicData();
            this.updateFlag &= -1025;
        }
        if ((this.updateFlag & 4096) != 0) {
            updateWifiSignal();
            this.updateFlag &= -4097;
        }
        if ((this.updateFlag & 8192) != 0) {
            updateOsdHome();
            this.updateFlag &= -8193;
        }
        if ((this.updateFlag & 16384) != 0) {
            updateGimbalCheckStatus();
            this.updateFlag &= -16385;
        }
        if (this.flagStatus != flags) {
            updateMessage();
        }
    }

    private void updateGimbalCheckStatus() {
        DataGimbalGetPushCheckStatus status = DataGimbalGetPushCheckStatus.getInstance();
        if (status.isGetted()) {
            if (status.getVibrateStatus()) {
                this.flagStatus |= FLAG_GIMBAL_VIBRATION;
                saveLog(FLAG_GIMBAL_VIBRATION, status);
            } else {
                this.flagStatus &= -36028797018963969L;
            }
            if (status.getLimitStatus() == 1) {
                saveLog(FLAG_GIMBAL_END_POINT_STUCK, status);
                this.flagStatus |= FLAG_GIMBAL_END_POINT_STUCK;
                this.flagStatus &= -18014398509481985L;
            } else if (status.getLimitStatus() == 2) {
                saveLog(FLAG_GIMBAL_END_POINT_STUCK, status);
                this.flagStatus &= -9007199254740993L;
                this.flagStatus |= FLAG_GIMBAL_END_POINT_OVERLOAD;
            } else {
                this.flagStatus &= -9007199254740993L;
                this.flagStatus &= -18014398509481985L;
            }
        }
    }

    private void updateOsdHome() {
        DataOsdGetPushHome home = DataOsdGetPushHome.getInstance();
        if (home.isGetted()) {
            if (home.isBigGaleWarning()) {
                saveLog(FLAG_GALE_WARNING, home);
                this.flagStatus |= FLAG_GALE_WARNING;
                return;
            }
            this.flagStatus &= -2251799813685249L;
        }
    }

    private static long updateVisionSensorStatus(Data2100GetPushCheckStatus status, long flags) {
        long flags2;
        long flags3;
        long flags4;
        if (status.hasVisionError()) {
            flags2 = flags | 65536;
            saveLog(65536, status);
        } else {
            flags2 = flags & -65537;
        }
        if (status.isBackSightDemarkAbnormal()) {
            flags3 = flags2 | 32768;
            saveLog(32768, status);
        } else {
            flags3 = flags2 & -32769;
        }
        if (status.isDownSightDemarkAbnormal()) {
            flags4 = flags3 | 16384;
            saveLog(16384, status);
        } else {
            flags4 = flags3 & -16385;
        }
        if (!status.isForeSightDemarkAbnormal()) {
            return flags4 & -8193;
        }
        long flags5 = flags4 | 8192;
        saveLog(8192, status);
        return flags5;
    }

    private void update2100CheckStatus() {
        Data2100GetPushCheckStatus status = Data2100GetPushCheckStatus.getInstance();
        if (status.isGetted()) {
            this.flagStatus = updateVisionSensorStatus(status, this.flagStatus);
        }
    }

    private void updateGimbalParam() {
        DataGimbalGetPushParams gimbal = DataGimbalGetPushParams.getInstance();
        if (gimbal.isGetted()) {
            if (gimbal.isStuck()) {
                this.flagStatus |= FLAG_GIMBAL_STUCK;
                saveLog(FLAG_GIMBAL_STUCK, gimbal);
                return;
            }
            this.flagStatus &= -4503599627370497L;
        }
    }

    private void updateFlycCheckStatus() {
        DataFlycGetPushCheckStatus status = DataFlycGetPushCheckStatus.getInstance();
        if (status.isGetted()) {
            if (status.getIMUAdvanceCaliStatus() || status.getIMUBasicCaliStatus() || status.getVersionStatus()) {
                saveLog(64, status);
                this.flagStatus |= 64;
            } else {
                this.flagStatus &= -65;
            }
            if (status.getIMUHorizontalCaliStatus() || status.getIMUDirectionStatus() || status.getIMUInitStatus() || status.getBarometerInitStatus() || status.getAccDataStatus() || status.getGyroscopeStatus() || status.getBarometerDataStatus() || status.getAircraftAttiStatus() || status.getIMUDataStatus() || status.getDataLoggerStatus()) {
                saveLog(32, status);
                this.flagStatus |= 32;
                return;
            }
            this.flagStatus &= -33;
        }
    }

    private void updateOsdSignalChlStatus(boolean update) {
        long flags;
        DataOsdGetPushChannalStatus chlStatus = DataOsdGetPushChannalStatus.getInstance();
        if (chlStatus.isGetted()) {
            long flags2 = this.flagStatus;
            if (CommonUtil.isChannelPoor(chlStatus.getChannelStatus())) {
                saveLog(FLAG_CHLSTATUS_POOR, chlStatus);
                flags = flags2 | FLAG_CHLSTATUS_POOR;
            } else {
                flags = flags2 & -1152921504606846977L;
            }
            if (flags != this.flagStatus) {
                this.flagStatus = flags;
                if (update) {
                    updateMessage();
                }
            }
        }
    }

    private void updateBatteryDynamicData() {
        DataSmartBatteryGetPushDynamicData dynamicData = DataSmartBatteryGetPushDynamicData.getInstance();
        if (dynamicData.isGetted()) {
            long status = dynamicData.getStatus();
            if ((3 & status) != 0) {
                this.flagStatus |= 1048576;
                saveLog(1048576, dynamicData);
            } else {
                this.flagStatus &= -1048577;
            }
            if ((12 & status) != 0) {
                this.flagStatus |= 2097152;
                saveLog(2097152, dynamicData);
            } else {
                this.flagStatus &= -2097153;
            }
            if ((48 & status) != 0) {
                this.flagStatus |= FLAG_BATTERY_LOW_TEMP;
                saveLog(FLAG_BATTERY_LOW_TEMP, dynamicData);
            } else {
                this.flagStatus &= -4194305;
            }
            if ((2097152 & status) != 0) {
                this.flagStatus |= FLAG_BATTERY_SELF_RELEASE;
                saveLog(FLAG_BATTERY_SELF_RELEASE, dynamicData);
            } else {
                this.flagStatus &= -8388609;
            }
            if ((17732923532771328L & status) != 0) {
                this.flagStatus |= 262144;
                saveLog(262144, dynamicData);
                return;
            }
            this.flagStatus &= -262145;
        }
    }

    private void updateCameraStateInfo() {
        DataCameraGetPushStateInfo cameraState = DataCameraGetPushStateInfo.getInstance();
        if (cameraState.isGetted()) {
            if (hasFlag(this.flagStatus, 1)) {
                this.flagStatus &= -2;
            }
            if (cameraState.getEncryptStatus() != DataCameraGetPushStateInfo.EncryptStatus.CHECK_SUCCESS) {
                this.flagStatus |= 128;
                saveLog(128, cameraState);
                return;
            }
            this.flagStatus &= -129;
        }
    }

    private void updateBatteryCommon() {
        DataCenterGetPushBatteryCommon battery = DataCenterGetPushBatteryCommon.getInstance();
        if (battery.isGetted()) {
            DataCenterGetPushBatteryCommon.ConnStatus status = battery.getConnStatus();
            if (CommonUtil.useNewBattery()) {
                DataSmartBatteryGetPushDynamicData dd = DataSmartBatteryGetPushDynamicData.getInstance();
                status = dd.isGetted() ? DataCenterGetPushBatteryCommon.ConnStatus.ofData((int) dd.getStatus()) : DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
            }
            boolean isValid = true;
            if (DataOsdGetPushCommon.BatteryType.Smart != DataOsdGetPushCommon.getInstance().getBatteryType()) {
                isValid = false;
            }
            if ((status == DataCenterGetPushBatteryCommon.ConnStatus.INVALID || status == DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION) && isValid) {
                saveLog(131072, battery);
                this.flagStatus |= 131072;
            } else {
                this.flagStatus &= -131073;
            }
            this.batteryHistory.parse(battery.getErrorType());
            if (this.batteryHistory.getInvalidNum() != 0) {
                this.flagStatus |= 524288;
                saveLog(524288, battery);
            } else {
                this.flagStatus &= -524289;
            }
            if (this.batteryHistory.hasFirstLevelCurrent() || this.batteryHistory.hasSecondLevelCurrent()) {
                this.flagStatus |= 1048576;
                saveLog(1048576, battery);
            } else {
                this.flagStatus &= -1048577;
            }
            if (this.batteryHistory.hasFirstLevelOverTemp() || this.batteryHistory.hasSecondLevelOverTemp()) {
                this.flagStatus |= 2097152;
                saveLog(2097152, battery);
            } else {
                this.flagStatus &= -2097153;
            }
            if (this.batteryHistory.hasFirstLevelLowTemp() || this.batteryHistory.hasSecondLevelLowTemp()) {
                this.flagStatus |= FLAG_BATTERY_LOW_TEMP;
                saveLog(FLAG_BATTERY_LOW_TEMP, battery);
                return;
            }
            this.flagStatus &= -4194305;
        }
    }

    private void updateFlycSmartBattery() {
        DataFlycGetPushSmartBattery smart = DataFlycGetPushSmartBattery.getInstance();
        if (smart.isGetted()) {
            int status = smart.getStatus();
            if ((status & 32) != 0) {
                saveLog(FLAG_SERIOUS_LOW_VOLTAGE, smart);
                this.flagStatus |= FLAG_SERIOUS_LOW_VOLTAGE;
                this.flagStatus &= -274877906945L;
            } else if ((status & 16) != 0) {
                saveLog(FLAG_LOW_VOLTAGE, smart);
                this.flagStatus |= FLAG_LOW_VOLTAGE;
                this.flagStatus &= -8589934593L;
            } else {
                this.flagStatus &= -274877906945L;
                this.flagStatus &= -8589934593L;
            }
        }
    }

    private static long updateESCStatus(DataOsdGetPushCommon common, long flags) {
        if (CommonUtil.checkUseNewModeChannel(common)) {
            if (!common.isBarometerDeadInAir()) {
                return flags & -513 & -1025;
            }
            if (common.groundOrSky() == 2) {
                saveLog(512, common);
                return (flags | 512) & -1025;
            }
            saveLog(1024, common);
            return (flags & -513) | 1024;
        } else if (!common.isBarometerDeadInAir()) {
            return flags & -4097;
        } else {
            saveLog(4096, common);
            return flags | 4096;
        }
    }

    private void updateOsdCommon() {
        long flags;
        long flags2;
        long flags3;
        long flags4;
        long flags5;
        long flags6;
        long flags7;
        long flags8;
        long flags9;
        long flags10;
        long flags11;
        long flags12;
        long flags13;
        long flags14;
        long flags15;
        long flags16;
        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
        if (common.isGetted()) {
            long flags17 = this.flagStatus;
            if (hasFlag(flags17, 1)) {
                flags17 &= -2;
            }
            DataOsdGetPushCommon.FLYC_STATE state = common.getFlycState();
            boolean bLanding = checkIsLandingMode(state);
            boolean failsafe = !common.getRcState();
            DataOsdGetPushCommon.IMU_INITFAIL_REASON reason = common.getIMUinitFailReason();
            if (!isFlying() || !CommonUtil.checkGpsValid()) {
                flags = flags17 & -4611686018427387905L;
            } else {
                flags = flags17 | FLAG_NORMAL_IN_THE_AIR;
            }
            if (!common.isNotEnoughForce()) {
                this.tipHandler.removeMessages(4099);
                flags &= -549755813889L;
            } else if (!hasFlag(flags, FLAG_NOT_ENOUGH_FORCE) && !this.tipHandler.hasMessages(4099)) {
                this.tipHandler.sendEmptyMessageDelayed(4099, 3000);
            }
            long flags18 = updateESCStatus(common, flags);
            if (reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.GyroDead || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceDead || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassDead || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerDead || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerNegative || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerNoiseTooLarge) {
                saveLog(FLAG_SENSOR_ERROR, common);
                flags2 = flags18 | FLAG_SENSOR_ERROR;
            } else {
                flags2 = flags18 & -33554433;
            }
            if (reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassModTooLarge || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassNoiseTooLarge) {
                saveLog(FLAG_IMU_COMPASS_ERROR, common);
                flags3 = flags2 | FLAG_IMU_COMPASS_ERROR;
            } else {
                flags3 = flags2 & -134217729;
            }
            if (!CommonUtil.supportRedundancySenor()) {
                if (reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.GyroBiasTooLarge || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceBiasTooLarge) {
                    saveLog(FLAG_IMU_ERROR, common);
                    flags3 |= FLAG_IMU_ERROR;
                } else {
                    flags3 &= -67108865;
                }
            }
            if (reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.WaitingMcStationary || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceMoveTooLarge || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.McHeaderMoved || reason == DataOsdGetPushCommon.IMU_INITFAIL_REASON.McVirbrated) {
                saveLog(FLAG_IMU_INITIALIZING, common);
                flags4 = flags3 | FLAG_IMU_INITIALIZING;
            } else {
                flags4 = flags3 & -16777217;
            }
            if (!failsafe || DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
                flags5 = flags4 & -8796093022209L;
            } else {
                saveLog(FLAG_FAILSAFE, common);
                flags5 = flags4 | FLAG_FAILSAFE;
            }
            if (state == DataOsdGetPushCommon.FLYC_STATE.GoHome) {
                flags6 = flags5 | FLAG_GOHOME;
            } else {
                flags6 = flags5 & -2305843009213693953L;
            }
            if (!failsafe || !hasFlag(flags6, FLAG_GOHOME)) {
                flags7 = flags6 & -4398046511105L;
            } else {
                saveLog(FLAG_FAILSAFE_GOHOME, common);
                flags7 = flags6 | FLAG_FAILSAFE_GOHOME;
            }
            if (CommonUtil.checkGpsValid()) {
                flags8 = flags7 & -144115188075855873L & -72057594037927937L;
            } else if (isFlying()) {
                flags8 = (flags7 | FLAG_NON_GPS_IN_THE_AIR) & -144115188075855873L;
            } else {
                flags8 = (flags7 & -72057594037927937L) | FLAG_NON_GPS;
            }
            int gpsNum = common.getGpsNum();
            if (!CommonUtil.checkIsAttiMode(state) && CommonUtil.checkGpsNumValid(gpsNum)) {
                flags9 = flags8 & -2199023255553L;
            } else if (hasFlag(flags8, FLAG_GOHOME)) {
                saveLog(FLAG_GOHOME_FAILED, common);
                flags9 = flags8 | FLAG_GOHOME_FAILED;
            } else {
                flags9 = flags8 & -2199023255553L;
            }
            int batteryStatus = common.getVoltageWarning();
            DataOsdGetPushCommon.FLIGHT_ACTION flightAction = common.getFlightAction();
            if (batteryStatus != 2) {
                flags10 = flags9 & -17179869185L;
            } else if (bLanding) {
                flags10 = (flags9 | FLAG_SERIOUS_LOW_POWER_LANDING) & -34359738369L;
            } else {
                flags10 = (flags9 | FLAG_SERIOUS_LOW_POWER) & -17179869185L;
            }
            if (!(DJIProductManager.getInstance().getType() == ProductType.A3 || DJIProductManager.getInstance().getType() == ProductType.N3)) {
                if (flightAction != DataOsdGetPushCommon.FLIGHT_ACTION.SERIOUS_LOW_VOLTAGE_LANDING) {
                    flags10 = flags10 & -4294967297L & -8589934593L;
                } else if (bLanding) {
                    saveLog(FLAG_SERIOUS_LOW_VOLTAGE_LANDING, common);
                    flags10 = (flags10 | FLAG_SERIOUS_LOW_VOLTAGE_LANDING) & -8589934593L;
                } else {
                    saveLog(FLAG_SERIOUS_LOW_VOLTAGE, common);
                    flags10 = (flags10 | FLAG_SERIOUS_LOW_VOLTAGE) & -4294967297L;
                }
            }
            if (flightAction != DataOsdGetPushCommon.FLIGHT_ACTION.SMART_POWER_LANDING) {
                flags11 = flags10 & -68719476737L & -137438953473L;
            } else if (bLanding) {
                flags11 = (flags10 | FLAG_SMART_LOW_POWER_LANDING) & -137438953473L;
            } else {
                flags11 = (flags10 | FLAG_SMART_LOW_POWER) & -68719476737L;
            }
            if (batteryStatus == 2) {
                flags12 = (flags11 | FLAG_SERIOUS_LOW_POWER) & -35184372088833L & -17592186044417L;
            } else if (batteryStatus == 1) {
                long flags19 = (flags11 | FLAG_LOW_POWER) & -34359738369L;
                if (hasFlag(flags19, FLAG_GOHOME)) {
                    flags12 = flags19 | FLAG_LOW_POWER_GOHOME;
                } else {
                    flags12 = flags19 & -17592186044417L;
                }
            } else {
                flags12 = flags11 & -34359738369L & -35184372088833L & -17592186044417L;
            }
            if (!CommonUtil.supportRedundancySenor()) {
                if (!common.getCompassError()) {
                    this.tipHandler.removeMessages(4097);
                    flags12 &= -257;
                } else if (!this.tipHandler.hasMessages(4097)) {
                    this.tipHandler.sendEmptyMessageDelayed(4097, 1000);
                }
            }
            if (!common.isImuPreheatd()) {
                saveLog(FLAG_IMU_HEATING, common);
                flags13 = flags12 | FLAG_IMU_HEATING;
            } else {
                flags13 = flags12 & -268435457;
            }
            DataOsdGetPushCommon.MotorStartFailedCause cause = DataOsdGetPushCommon.MotorStartFailedCause.None;
            if (!common.isMotorUp()) {
                cause = common.getMotorStartCauseNoStartAction();
            }
            boolean isFlying = isFlying();
            boolean gpsUsed = common.isGpsUsed();
            if (DataOsdGetPushCommon.MotorStartFailedCause.None == cause || DataOsdGetPushCommon.MotorStartFailedCause.OTHER == cause) {
                flags14 = flags13 & -536870913 & -2147483649L & -1073741825;
            } else if (cause == DataOsdGetPushCommon.MotorStartFailedCause.DeviceLocked) {
                saveLog(FLAG_DEVICE_LOCK, common);
                flags14 = (flags13 | FLAG_DEVICE_LOCK) & -1073741825 & -2147483649L;
            } else if (cause == DataOsdGetPushCommon.MotorStartFailedCause.NoviceProtected) {
                saveLog(FLAG_CANT_TAKEOFF_NOVICE, common);
                flags14 = ((flags13 & -2147483649L) | FLAG_CANT_TAKEOFF_NOVICE) & -536870913;
            } else {
                flags14 = (flags13 & -536870913 & -1073741825) | FLAG_CANT_TAKEOFF;
            }
            if (CommonUtil.checkIsAttiMode(common.getFlycState())) {
                if (isFlying) {
                    saveLog(FLAG_ATTI_STATE_IN_THE_AIR, common);
                    flags16 = flags14 | FLAG_ATTI_STATE_IN_THE_AIR;
                } else {
                    flags16 = flags14 | FLAG_ATTI_STATE;
                }
                flags15 = flags16 & -72057594037927937L & -144115188075855873L;
            } else {
                flags15 = flags14 & -576460752303423489L & -288230376151711745L;
                if (isFlying && !gpsUsed) {
                    flags15 |= FLAG_NON_GPS_IN_THE_AIR;
                } else if (!isFlying && !gpsUsed) {
                    flags15 |= FLAG_NON_GPS;
                }
            }
            if (common.getFlycVersion() < 5) {
                boolean isPAtti = CommonUtil.checkIsPAttiMode(common.getFlycState());
                if (this.lastCompassDisturbTime != 0) {
                    if (common.groundOrSky() != 2 || !isPAtti || !CommonUtil.checkGpsNumValid(gpsNum)) {
                        if (!hasFlag(flags15, 2048)) {
                            this.lastCompassDisturbTime = 0;
                        } else if (System.currentTimeMillis() - this.lastCompassDisturbTime > 1000) {
                            flags15 &= -2049;
                            this.lastCompassDisturbTime = 0;
                        }
                    } else if (hasFlag(flags15, 2048)) {
                        this.lastCompassDisturbTime = System.currentTimeMillis();
                    } else {
                        long now = System.currentTimeMillis();
                        if (now - this.lastCompassDisturbTime > 1000) {
                            saveLog(2048, common);
                            flags15 |= 2048;
                            this.lastCompassDisturbTime = now;
                        }
                    }
                } else if (common.groundOrSky() == 2 && isPAtti && CommonUtil.checkGpsNumValid(gpsNum)) {
                    this.lastCompassDisturbTime = System.currentTimeMillis();
                }
            } else if (CommonUtil.isCompassDisturb(common.getNonGpsCause())) {
                saveLog(2048, common);
                flags15 |= 2048;
            } else {
                flags15 &= -2049;
            }
            this.flagStatus = flags15;
        }
    }

    private void updateWifiSignal() {
        DataWifiGetPushSignal wifi = DataWifiGetPushSignal.getInstance();
        if (!wifi.isGetted() || !CommonUtil.isWifiProduct(null)) {
            return;
        }
        if (wifi.getSignal() <= 50) {
            saveLog(FLAG_LOW_RADIO_SIGNAL, wifi);
            this.flagStatus |= FLAG_LOW_RADIO_SIGNAL;
            return;
        }
        this.flagStatus &= -281474976710657L;
    }

    private void updateOsdSignal() {
        DJISDKCacheParamValue downSignalQuality;
        DataOsdGetPushSignalQuality signal = DataOsdGetPushSignalQuality.getInstance();
        if (signal.isGetted()) {
            int upSignalQuality = signal.getUpSignalQuality();
            if (upSignalQuality < 50) {
                saveLog(FLAG_LOW_RC_SIGNAL, signal);
                this.flagStatus |= FLAG_LOW_RC_SIGNAL;
            } else {
                this.flagStatus &= -140737488355329L;
            }
            this.flagStatus = updateSdrRcSignal(this.flagStatus, upSignalQuality);
            if (!CommonUtil.isWifiProduct(null)) {
                int radioSignal = 0;
                if (CommonUtil.isSdrProducts(null)) {
                    downSignalQuality = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getOcuSyncLinkKey(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY));
                } else {
                    downSignalQuality = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getLightbridgeLinkKey(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY));
                }
                if (downSignalQuality != null) {
                    radioSignal = ((Integer) downSignalQuality.getData()).intValue();
                }
                if (radioSignal < 50) {
                    saveLog(FLAG_LOW_RADIO_SIGNAL, signal);
                    this.flagStatus |= FLAG_LOW_RADIO_SIGNAL;
                } else {
                    this.flagStatus &= -281474976710657L;
                }
                this.flagStatus = updateSdrRadioSignal(this.flagStatus, radioSignal);
            }
            if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
                this.flagStatus &= -140737488355329L;
            }
        }
    }

    private static long updateSdrRadioSignal(long flags, int signalVal) {
        if (!CommonUtil.isSdrProducts(null)) {
            return flags;
        }
        long flags2 = flags & -281474976710657L & -1125899906842625L;
        if (signalVal == 5 || signalVal == 15) {
            saveLog(FLAG_LOW_RADIO_SIGNAL, DataOsdGetPushSignalQuality.getInstance());
            return flags2 | FLAG_LOW_RADIO_SIGNAL;
        } else if (signalVal != 6 && signalVal != 16) {
            return flags2;
        } else {
            saveLog(FLAG_RADIO_SIGNAL_DISTURB, DataOsdGetPushSignalQuality.getInstance());
            return flags2 | FLAG_RADIO_SIGNAL_DISTURB;
        }
    }

    private static long updateSdrRcSignal(long flags, int signalVal) {
        if (!CommonUtil.isSdrProducts(null)) {
            return flags;
        }
        long flags2 = flags & -140737488355329L & -562949953421313L;
        if (signalVal == 5 || signalVal == 15) {
            saveLog(FLAG_LOW_RC_SIGNAL, DataOsdGetPushSignalQuality.getInstance());
            return flags2 | FLAG_LOW_RC_SIGNAL;
        } else if (signalVal != 6 && signalVal != 16) {
            return flags2;
        } else {
            saveLog(FLAG_RC_SIGNAL_DISTURB, DataOsdGetPushSignalQuality.getInstance());
            return flags2 | FLAG_RC_SIGNAL_DISTURB;
        }
    }

    private void updateRcBattery() {
        if (!DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null) || DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null)) {
            ChargeRemaining rcBattery = (ChargeRemaining) CacheHelper.getRemoteController("ChargeRemaining");
            if (rcBattery == null) {
                return;
            }
            if (CommonUtil.checkRcBatteryLow(rcBattery.getRemainingChargeInPercent())) {
                saveLog(FLAG_LOW_RC_POWER, DataOsdGetPushSignalQuality.getInstance());
                this.flagStatus |= FLAG_LOW_RC_POWER;
                return;
            }
            this.flagStatus &= -70368744177665L;
            return;
        }
        this.flagStatus &= -70368744177665L;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushParams gimbal) {
        handleDataPush(32);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushCheckStatus status) {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() >= 4) {
            handleDataPush(8);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushCheckStatus status) {
        handleDataPush(16384);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData pushInfo) {
        handleDataPush(1024);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo state) {
        handleDataPush(2);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterGetPushBatteryCommon battery) {
        handleDataPush(4);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        if (common.isGetted() && common.getDroneType() != DataOsdGetPushCommon.DroneType.NoFlyc) {
            handleDataPush(128);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushSmartBattery smart) {
        if (DJIProductManager.getInstance().getType() == ProductType.A3 || DJIProductManager.getInstance().getType() == ProductType.N3) {
            handleDataPush(16);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushSignal wifi) {
        handleDataPush(4096);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSignalQuality signal) {
        handleDataPush(256);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushBatteryInfo rc) {
        handleDataPush(512);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome home) {
        handleDataPush(8192);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVideoEvent event) {
        long flags = this.flagStatus;
        switch (event) {
            case ConnectLose:
                saveLog(16, null);
                flags |= 16;
                break;
            case ConnectOK:
                flags &= -17;
                break;
        }
        if (this.flagStatus != flags) {
            this.flagStatus = flags;
            updateMessage();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3MainThread(DataCameraEvent ce) {
        if (ce == DataCameraEvent.ConnectOK) {
            cameraConnect();
        } else if (ce == DataCameraEvent.ConnectLose) {
            cameraDisconnect();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (event == DataEvent.ConnectOK) {
            connect();
            updateOsdSignalChlStatus(true);
        } else if (event == DataEvent.ConnectLose) {
            disconnect();
        }
    }

    private static boolean checkIsLandingMode(DataOsdGetPushCommon.FLYC_STATE state) {
        if (state == DataOsdGetPushCommon.FLYC_STATE.AutoLanding || state == DataOsdGetPushCommon.FLYC_STATE.AttiLangding) {
            return true;
        }
        return false;
    }

    private static boolean hasFlag(long value, long flag) {
        return (value & flag) != 0;
    }

    /* access modifiers changed from: private */
    public void handleCompassError() {
        if (!hasFlag(this.flagStatus, 256)) {
            saveLog(256, DataOsdGetPushCommon.getInstance());
            this.flagStatus |= 256;
            updateMessage();
        }
    }

    /* access modifiers changed from: private */
    public void handlePowerEnough() {
        if (DataOsdGetPushCommon.getInstance().isNotEnoughForce() && !hasFlag(this.flagStatus, FLAG_NOT_ENOUGH_FORCE)) {
            saveLog(FLAG_NOT_ENOUGH_FORCE, DataOsdGetPushCommon.getInstance());
            this.flagStatus |= FLAG_NOT_ENOUGH_FORCE;
            updateMessage();
        }
    }

    /* access modifiers changed from: private */
    public void updateParams() {
        if (CommonUtil.supportRedundancySenor()) {
            this.paramGetter.setInfos(this.SENSORS_PARAMS).start(new DJIDataCallBack() {
                /* class dji.internal.logics.FPVTipLogic.AnonymousClass1 */

                public void onSuccess(Object model) {
                    FPVTipLogic.this.tipHandler.sendEmptyMessage(4101);
                }

                public void onFailure(Ccode ccode) {
                    if (!FPVTipLogic.this.tipHandler.hasMessages(4100)) {
                        FPVTipLogic.this.tipHandler.sendEmptyMessageDelayed(4100, FPVTipLogic.DELAY_UPDATE_PARAMS);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateParamStatus() {
        long flags;
        long flags2;
        if (CommonUtil.supportRedundancySenor()) {
            long flags3 = this.flagStatus;
            int imuStat = DJIFlycParamInfoManager.read(this.SENSORS_PARAMS[0]).value.intValue();
            if (imuStat == 1 || imuStat == 0 || imuStat == 2) {
                flags = flags3 & -67108865;
            } else {
                flags = flags3 | FLAG_IMU_ERROR;
            }
            int magStat = DJIFlycParamInfoManager.read(this.SENSORS_PARAMS[1]).value.intValue();
            if (magStat == 1 || magStat == 0) {
                flags2 = flags & -257;
            } else {
                flags2 = flags | 256;
            }
            if (flags2 != this.flagStatus) {
                this.flagStatus = flags2;
                updateMessage();
            }
            if (!this.tipHandler.hasMessages(4100)) {
                this.tipHandler.sendEmptyMessageDelayed(4100, DELAY_UPDATE_PARAMS);
            }
        }
    }

    private static final class TipHandler extends Handler {
        private FPVTipLogic logic;

        TipHandler(FPVTipLogic logic2) {
            super(BackgroundLooper.getLooper());
            this.logic = logic2;
        }

        /* access modifiers changed from: package-private */
        public void destroy() {
            this.logic = null;
        }

        public void handleMessage(android.os.Message msg) {
            if (this.logic != null) {
                switch (msg.what) {
                    case 256:
                        this.logic.update(false);
                        return;
                    case 4097:
                        this.logic.handleCompassError();
                        return;
                    case 4099:
                        this.logic.handlePowerEnough();
                        return;
                    case 4100:
                        if (!DJIFlycParamInfoManager.isInited()) {
                            sendEmptyMessageDelayed(4100, FPVTipLogic.DELAY_UPDATE_PARAMS);
                            return;
                        } else {
                            this.logic.updateParams();
                            return;
                        }
                    case 4101:
                        if (!DJIFlycParamInfoManager.isInited()) {
                            sendEmptyMessageDelayed(4101, FPVTipLogic.DELAY_UPDATE_PARAMS);
                            return;
                        } else {
                            this.logic.updateParamStatus();
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    private static boolean isFlying() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.IS_FLYING));
        return value != null && CacheHelper.toBool(value.getData());
    }

    private static int getPos(long data) {
        long dataTemp = checkPos(data);
        if (dataTemp == 0) {
            return 0;
        }
        for (int i = 1; i <= 64; i++) {
            if (dataTemp % 2 != 0) {
                return i;
            }
            dataTemp >>= 1;
        }
        return -1;
    }

    private static long checkPos(long data) {
        return data & -17 & -129;
    }

    private void updateMessage() {
        int resPos = getPos(this.flagStatus);
        if (resDatas != null && resPos != -1) {
            Message data = resDatas[resPos];
            if (this.previousMessage == null || !data.getTitle().equals(this.previousMessage.getTitle())) {
                this.previousMessage = data;
                LogicEventBus.getInstance().post(new FPVTipEvent(data));
            }
        }
    }

    protected static void saveLog(long textIndex, DataBase dataBase) {
        String strDate = sdf.format(new Date());
        StringBuilder builder = new StringBuilder();
        builder.append(strDate);
        if (resDatas != null) {
            int pos = getPos(textIndex);
            if (pos != -1) {
                builder.append(", [tip:").append(resDatas[pos].getTitle()).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            } else {
                return;
            }
        }
        if (DataOsdGetPushHome.getInstance().isGetted()) {
            builder.append(", logIndex=").append(DataOsdGetPushHome.getInstance().getFlycLogIndex());
        }
        if (dataBase != null) {
            if (dataBase.getPack() != null) {
                builder.append(", [header:");
                builder.append("senderType=").append(Integer.toHexString(dataBase.getPack().senderType));
                builder.append("(").append(DeviceType.find(dataBase.getPack().senderType)).append(")").append(" ");
                builder.append("senderId=").append(Integer.toHexString(dataBase.getPack().senderId)).append(" ");
                builder.append("cmdSet=").append(Integer.toHexString(dataBase.getPack().cmdSet));
                CmdSet cmdSet = CmdSet.find(dataBase.getPack().cmdSet);
                builder.append("(").append(cmdSet).append(")").append(" ");
                builder.append("cmdType=").append(dataBase.getPack().cmdType).append(" ");
                builder.append("cmdId=").append(Integer.toHexString(dataBase.getPack().cmdId));
                builder.append("(").append(getCmdIdName(cmdSet, dataBase.getPack().cmdId)).append(")");
                builder.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            }
            builder.append(", [Platform:").append(getPlatformName());
            builder.append(", sn: ").append(getFlightControllerSN());
            builder.append(", battery: ").append(getBatteryPercent());
            builder.append("]\n");
            if (dataBase.getRecData() != null) {
                builder.append("[data:").append(BytesUtil.byte2hex(dataBase.getRecData())).append("]\n");
            }
        }
    }

    private static String getCmdIdName(CmdSet cmdSet, int cmdId) {
        CmdIdInterface cmdIdInterface;
        if (cmdSet == null || cmdSet.cmdIdClass() == null || (cmdIdInterface = cmdSet.cmdIdClass().getCmdId(cmdId)) == null) {
            return null;
        }
        return cmdIdInterface.toString();
    }

    private static String getFlightControllerSN() {
        DJISDKCacheParamValue value;
        if (fcsnKey == null) {
            fcsnKey = KeyHelper.getFlightControllerKey(DJISDKCacheKeys.SERIAL_NUMBER);
        }
        if (DJISDKCache.getInstance() == null || (value = DJISDKCache.getInstance().getAvailableValue(fcsnKey)) == null || value.getData() == null) {
            return null;
        }
        return (String) value.getData();
    }

    private static String getPlatformName() {
        if (DJIComponentManager.getInstance() != null) {
            return DJIComponentManager.getInstance().getPlatformType().toString();
        }
        return "None";
    }

    private static String getBatteryPercent() {
        if (batteryPercentKey1 == null) {
            batteryPercentKey1 = KeyHelper.getBatteryKey(0, BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
        }
        if (batteryPercentKey2 == null) {
            batteryPercentKey2 = KeyHelper.getBatteryKey(1, BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
        }
        StringBuilder batteryBuilder = new StringBuilder();
        if (DJISDKCache.getInstance() != null) {
            DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(batteryPercentKey1);
            if (value == null || value.getData() == null) {
                batteryBuilder.append("0%");
            } else {
                batteryBuilder.append((Integer) value.getData()).append("%");
            }
            DJISDKCacheParamValue value2 = DJISDKCache.getInstance().getAvailableValue(batteryPercentKey2);
            if (value2 == null || value2.getData() == null) {
                batteryBuilder.append(" 0%");
            } else {
                batteryBuilder.append(" ").append((Integer) value2.getData()).append("%");
            }
        } else {
            batteryBuilder.append("0% 0%");
        }
        return batteryBuilder.toString();
    }
}
