package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.mc.DJIMcHelper;
import dji.midware.data.model.P3.DataFlycFunctionControl;
import dji.midware.data.model.base.DJIOsdDataBase;
import dji.publics.protocol.IProtocol;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataOsdGetPushCommon extends DJIOsdDataBase {
    private static DataOsdGetPushCommon instance = null;

    @Keep
    public enum MotorStateEvent {
        MOTOR_UP,
        MOTOR_DOWN
    }

    public static synchronized DataOsdGetPushCommon getInstance() {
        DataOsdGetPushCommon dataOsdGetPushCommon;
        synchronized (DataOsdGetPushCommon.class) {
            if (instance == null) {
                instance = new DataOsdGetPushCommon();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataOsdGetPushCommon = instance;
        }
        return dataOsdGetPushCommon;
    }

    public DataOsdGetPushCommon() {
    }

    public DataOsdGetPushCommon(boolean isRegist) {
        super(isRegist);
    }

    public double getLongitude() {
        return (((Double) get(0, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getLatitude() {
        return (((Double) get(8, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public int getHeight() {
        return ((Short) get(16, 2, Short.class)).shortValue();
    }

    public int getXSpeed() {
        return ((Short) get(18, 2, Short.class)).shortValue();
    }

    public int getYSpeed() {
        return ((Short) get(20, 2, Short.class)).shortValue();
    }

    public int getZSpeed() {
        return ((Short) get(22, 2, Short.class)).shortValue();
    }

    public int getPitch() {
        return ((Short) get(24, 2, Short.class)).shortValue();
    }

    public int getRoll() {
        return ((Short) get(26, 2, Short.class)).shortValue();
    }

    public int getYaw() {
        return ((Short) get(28, 2, Short.class)).shortValue();
    }

    public boolean getRcState() {
        return (((Short) get(30, 1, Short.class)).shortValue() & 128) == 0;
    }

    public FLYC_STATE getFlycState() {
        if (this._recData == null) {
            return FLYC_STATE.OTHER;
        }
        return FLYC_STATE.find(((Short) get(30, 1, Short.class)).shortValue() & -129);
    }

    public DataFlycFunctionControl.FLYC_COMMAND getAppCommand() {
        return DataFlycFunctionControl.FLYC_COMMAND.find(((Short) get(31, 1, Short.class)).shortValue());
    }

    public boolean canIOCWork() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 1) == 1;
    }

    public int groundOrSky() {
        return (((Integer) get(32, 4, Integer.class)).intValue() >> 1) & 3;
    }

    public boolean isMotorUp() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean isSwaveWork() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 16) != 0;
    }

    public GOHOME_STATUS getGohomeStatus() {
        if (this._recData == null) {
            return GOHOME_STATUS.OTHER;
        }
        return GOHOME_STATUS.find((((Integer) get(32, 4, Integer.class)).intValue() >> 5) & 7);
    }

    public boolean isImuPreheatd() {
        if (this._recData == null) {
            return true;
        }
        return (((Integer) get(32, 4, Integer.class)).intValue() & 4096) != 0;
    }

    public boolean isVisionUsed() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 256) != 0;
    }

    public int getVoltageWarning() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 1536) >>> 9;
    }

    public RcModeChannel getModeChannel() {
        return RcModeChannel.find((((Integer) get(32, 4, Integer.class)).intValue() & IProtocol.CMDID_ACCOUNT_GET_FLIGHTS) >>> 13, getFlycVersion(), getDroneType());
    }

    public RcModeChannel getModeChannelByFR() {
        return RcModeChannel.find((((Integer) get(32, 4, Integer.class)).intValue() & IProtocol.CMDID_ACCOUNT_GET_FLIGHTS) >>> 13, getFlycVersion(), getDroneType(), false);
    }

    public boolean isGpsUsed() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean getCompassError() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 65536) != 0;
    }

    public boolean getWaveError() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 131072) != 0;
    }

    public int getGpsLevel() {
        return (((Integer) get(32, 4, Integer.class)).intValue() >>> 18) & 15;
    }

    public BatteryType getBatteryType() {
        DroneType type = getDroneType();
        if (type == DroneType.Unknown || type == DroneType.None) {
            return BatteryType.Smart;
        }
        return BatteryType.find((((Integer) get(32, 4, Integer.class)).intValue() >>> 22) & 3);
    }

    public boolean isAcceletorOverRange() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 24) & 1) != 0;
    }

    public boolean isVibrating() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 25) & 1) != 0;
    }

    public boolean isBarometerDeadInAir() {
        if (getFlycVersion() >= 7) {
            return false;
        }
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 26) & 1) != 0;
    }

    public boolean isEscError() {
        if (getFlycVersion() < 7) {
            return false;
        }
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 26) & 1) != 0;
    }

    public boolean isMotorBlock() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 27) & 1) != 0;
    }

    public boolean isNotEnoughForce() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 28) & 1) != 0;
    }

    public boolean isPropellerCatapult() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 29) & 1) != 0;
    }

    public boolean isGoHomeHeightModified() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 30) & 1) == 1;
    }

    public boolean isOutOfLimit() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >>> 31) & 1) == 1;
    }

    public boolean isGPSValid() {
        return ((((Integer) get(33, 1, Integer.class)).intValue() >> 7) & 1) == 1;
    }

    public boolean isCompassError() {
        return ((((Integer) get(34, 1, Integer.class)).intValue() >>> 0) & 1) == 1;
    }

    public int getGpsNum() {
        return ((Short) get(36, 1, Short.class)).shortValue();
    }

    public FLIGHT_ACTION getFlightAction() {
        return FLIGHT_ACTION.find(((Short) get(37, 1, Short.class)).shortValue());
    }

    public NON_GPS_CAUSE getNonGpsCause() {
        return NON_GPS_CAUSE.find(((Integer) get(39, 1, Integer.class)).intValue() & 15);
    }

    public boolean getWaypointLimitMode() {
        return (((Integer) get(39, 1, Integer.class)).intValue() & 16) == 16;
    }

    public boolean isQuickSpin() {
        return (((Integer) get(39, 1, Integer.class)).intValue() & 32) == 32;
    }

    public boolean isShowNearGroundProtectTips() {
        return (((Integer) get(39, 1, Integer.class)).intValue() & 64) == 64;
    }

    public int getBattery() {
        return ((Integer) get(40, 1, Integer.class)).intValue();
    }

    public int getSwaveHeight() {
        return ((Short) get(41, 1, Short.class)).shortValue();
    }

    public int getFlyTime() {
        return ((Integer) get(42, 2, Integer.class)).intValue();
    }

    public int getMotorRevolution() {
        return ((Short) get(44, 1, Short.class)).shortValue();
    }

    public int getFlycVersion() {
        return ((Integer) get(47, 1, Integer.class)).intValue();
    }

    public DroneType getDroneType() {
        return DroneType.find(((Integer) get(48, 1, Integer.class)).intValue());
    }

    public MotorStartFailedCause getMotorFailedCause() {
        if (getFlycVersion() >= 26) {
            return MotorStartFailedCause.find(((Short) get(38, 1, Short.class)).shortValue());
        }
        if ((((Short) get(38, 1, Short.class)).shortValue() >> 7) == 0) {
            return MotorStartFailedCause.None;
        }
        if (this._recData == null || this._recData.length <= 51) {
            return MotorStartFailedCause.find(((Short) get(38, 1, Short.class)).shortValue() & 127);
        }
        return getMotorStartCauseNoStartAction();
    }

    public MotorStartFailedCause getMotorStartCauseNoStartAction() {
        if (this._recData == null || this._recData.length <= 51) {
            return MotorStartFailedCause.None;
        }
        return MotorStartFailedCause.find(((Short) get(51, 1, Short.class)).shortValue() & 255);
    }

    public IMU_INITFAIL_REASON getIMUinitFailReason() {
        return IMU_INITFAIL_REASON.find(((Integer) get(49, 1, Integer.class)).intValue());
    }

    public boolean isImuInitError() {
        IMU_INITFAIL_REASON reason = getIMUinitFailReason();
        if (reason == IMU_INITFAIL_REASON.None || reason == IMU_INITFAIL_REASON.ColletingData || reason == IMU_INITFAIL_REASON.MonitorError) {
            return false;
        }
        return true;
    }

    public boolean isAllowImuInitfailReason() {
        if (DataFlycGetPushCheckStatus.getInstance().getGyroscopeStatus() || DataFlycGetPushCheckStatus.getInstance().getAccDataStatus() || DataFlycGetPushCheckStatus.getInstance().getBarometerInitStatus() || DataFlycGetPushCheckStatus.getInstance().getIMUInitStatus()) {
            return false;
        }
        return true;
    }

    public MotorFailReason getMotorFailReason() {
        return MotorFailReason.find(((Integer) get(50, 1, Integer.class)).intValue());
    }

    public SDKCtrlDevice getSDKCtrlDevice() {
        return SDKCtrlDevice.find(((Integer) get(52, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum SDKCtrlDevice {
        RC(0),
        APP(1),
        ONBOARD_DEVICE(2),
        CAMERA(3),
        OTHER(128);
        
        private int _value = 0;

        private SDKCtrlDevice(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static SDKCtrlDevice find(int value) {
            SDKCtrlDevice[] values = values();
            for (SDKCtrlDevice ts : values) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return OTHER;
        }
    }

    @Keep
    public enum RcModeChannel {
        CHANNEL_MANUAL(0),
        CHANNEL_A(1),
        CHANNEL_P(2),
        CHANNEL_NAV(3),
        CHANNEL_FPV(4),
        CHANNEL_FARM(5),
        CHANNEL_S(6),
        CHANNEL_F(7),
        CHANNEL_M(8),
        CHANNEL_G(9),
        CHANNEL_T(10),
        CHANNEL_UNKNOWN(255);
        
        private int _value = 0;

        private RcModeChannel(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        public boolean belongsTo(int value) {
            return this._value == value;
        }

        public static RcModeChannel realFind(int value) {
            if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 16) {
                RcModeChannel[] values = values();
                for (RcModeChannel mode : values) {
                    if (mode.belongsTo(value)) {
                        return mode;
                    }
                }
                return CHANNEL_P;
            } else if (isValueMatchChannelAMode(value)) {
                return CHANNEL_A;
            } else {
                if (isValueMatchChannelPMode(value)) {
                    return CHANNEL_P;
                }
                if (isValueMatchChannelSMode(value)) {
                    return CHANNEL_S;
                }
                if (isValueMatchChannelGMode(value)) {
                    return CHANNEL_G;
                }
                if (isValueMatchChannelMMode(value)) {
                    return CHANNEL_M;
                }
                if (isValueMatchChannelTMode(value)) {
                    return CHANNEL_T;
                }
                return CHANNEL_P;
            }
        }

        private static boolean isValueMatchChannelGMode(int value) {
            return value == RcModeChannelAfter16.ATTI_GENTLE.value() || value == RcModeChannelAfter16.GPS_GENTLE.value();
        }

        private static boolean isValueMatchChannelMMode(int value) {
            return value == RcModeChannelAfter16.MANUAL.value();
        }

        private static boolean isValueMatchChannelAMode(int value) {
            return value == RcModeChannelAfter16.ATTI.value() || value == RcModeChannelAfter16.ATTI_MANUAL.value();
        }

        private static boolean isValueMatchChannelPMode(int value) {
            return value == RcModeChannelAfter16.GPS_ATTI.value() || value == RcModeChannelAfter16.GPS_NORMAL.value();
        }

        private static boolean isValueMatchChannelSMode(int value) {
            return value == RcModeChannelAfter16.GPS_SPORT.value() || value == RcModeChannelAfter16.ATTI_SPORT.value();
        }

        private static boolean isValueMatchChannelTMode(int value) {
            return value == RcModeChannelAfter16.T_MODE.value();
        }

        public static RcModeChannel find(int value, int version, DroneType type) {
            return find(value, version, type, true);
        }

        public static RcModeChannel find(int value, int version, DroneType type, boolean diffVer) {
            RcModeChannel rcModeChannel = CHANNEL_P;
            if (version >= 14 && diffVer) {
                return DJIMcHelper.getInstance().getRcModeChannel(value);
            }
            if (DroneType.P4 == type || DroneType.wm220 == type) {
                if (value == 0) {
                    return CHANNEL_A;
                }
                if (value == 1) {
                    return CHANNEL_S;
                }
                return CHANNEL_P;
            } else if (value == 0) {
                return CHANNEL_F;
            } else {
                if (value == 1) {
                    return CHANNEL_A;
                }
                return CHANNEL_P;
            }
        }
    }

    @Keep
    public enum TRIPOD_STATUS {
        UNKNOWN((byte) 0),
        FOLD_COMPELTE((byte) 1),
        FOLOING((byte) 2),
        STRETCH_COMPLETE((byte) 3),
        STRETCHING((byte) 4),
        STOP_DEFORMATION((byte) 5);
        
        private static volatile TRIPOD_STATUS[] sValues = null;
        private byte _value = 0;

        private TRIPOD_STATUS(byte value) {
            this._value = value;
        }

        public byte value() {
            return this._value;
        }

        private boolean belongsTo(byte value) {
            return this._value == value;
        }

        public static TRIPOD_STATUS ofValue(byte value) {
            if (sValues == null) {
                sValues = values();
            }
            TRIPOD_STATUS[] tripod_statusArr = sValues;
            for (TRIPOD_STATUS ts : tripod_statusArr) {
                if (ts.belongsTo(value)) {
                    return ts;
                }
            }
            return UNKNOWN;
        }
    }

    @Keep
    public enum FLYC_STATE {
        Manula(0),
        Atti(1),
        Atti_CL(2),
        Atti_Hover(3),
        Hover(4),
        GPS_Blake(5),
        GPS_Atti(6),
        GPS_CL(7),
        GPS_HomeLock(8),
        GPS_HotPoint(9),
        AssitedTakeoff(10),
        AutoTakeoff(11),
        AutoLanding(12),
        AttiLangding(13),
        NaviGo(14),
        GoHome(15),
        ClickGo(16),
        Joystick(17),
        Cinematic(19),
        Atti_Limited(23),
        NaviSubMode_Draw(24),
        NaviMissionFollow(25),
        NaviSubMode_Tracking(26),
        NaviSubMode_Pointing(27),
        PANO(28),
        Farming(29),
        FPV(30),
        SPORT(31),
        NOVICE(32),
        FORCE_LANDING(33),
        TERRAIN_TRACKING(35),
        PALM_CONTROL(36),
        QUICK_SHOT(37),
        TRIPOD_GPS(38),
        TRACK_HEADLOCK(39),
        ENGINE_START(41),
        DETOUR(43),
        TIME_LAPSE(46),
        POI_WITH_VISION(50),
        OMNI_MOVING(49),
        OTHER(100);
        
        private static volatile FLYC_STATE[] sValues = null;
        private int data;

        private FLYC_STATE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FLYC_STATE find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            FLYC_STATE result = OTHER;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FLIGHT_ACTION {
        NONE(0),
        WARNING_POWER_GOHOME(1),
        WARNING_POWER_LANDING(2),
        SMART_POWER_GOHOME(3),
        SMART_POWER_LANDING(4),
        LOW_VOLTAGE_LANDING(5),
        LOW_VOLTAGE_GOHOME(6),
        SERIOUS_LOW_VOLTAGE_LANDING(7),
        RC_ONEKEY_GOHOME(8),
        RC_ASSISTANT_TAKEOFF(9),
        RC_AUTO_TAKEOFF(10),
        RC_AUTO_LANDING(11),
        APP_AUTO_GOHOME(12),
        APP_AUTO_LANDING(13),
        APP_AUTO_TAKEOFF(14),
        OUTOF_CONTROL_GOHOME(15),
        API_AUTO_TAKEOFF(16),
        API_AUTO_LANDING(17),
        API_AUTO_GOHOME(18),
        AVOID_GROUND_LANDING(19),
        AIRPORT_AVOID_LANDING(20),
        TOO_CLOSE_GOHOME_LANDING(21),
        TOO_FAR_GOHOME_LANDING(22),
        APP_WP_MISSION(23),
        WP_AUTO_TAKEOFF(24),
        GOHOME_AVOID(25),
        GOHOME_FINISH(26),
        VERT_LOW_LIMIT_LANDING(27),
        BATTERY_FORCE_LANDING(28),
        MC_PROTECT_GOHOME(29),
        MOTORBLOCK_LANDING(30),
        APP_REQUEST_FORCE_LANDING(31),
        FAKEBATTERY_LANDING(32),
        RTH_COMING_OBSTACLE_LANDING(33),
        IMUERROR_RTH(34),
        OVER_HEAT_LEVEL2_GOHOME(37),
        LANDING_FOR_PLANE(38);
        
        private static volatile FLIGHT_ACTION[] sValues = null;
        private int _value = 0;

        private FLIGHT_ACTION(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        public boolean belongsTo(int value) {
            return this._value == value;
        }

        public static FLIGHT_ACTION find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            FLIGHT_ACTION result = NONE;
            FLIGHT_ACTION[] flight_actionArr = sValues;
            for (FLIGHT_ACTION action : flight_actionArr) {
                if (action.belongsTo(value)) {
                    return action;
                }
            }
            return result;
        }
    }

    @Keep
    public enum MotorStartFailedCause {
        None(0),
        CompassError(1),
        AssistantProtected(2),
        DeviceLocked(3),
        DistanceLimit(4),
        IMUNeedCalibration(5),
        IMUSNError(6),
        IMUWarning(7),
        CompassCalibrating(8),
        AttiError(9),
        NoviceProtected(10),
        BatteryCellError(11),
        BatteryCommuniteError(12),
        SeriouLowVoltage(13),
        SeriouLowPower(14),
        LowVoltage(15),
        TempureVolLow(16),
        SmartLowToLand(17),
        BatteryNotReady(18),
        SimulatorMode(19),
        PackMode(20),
        AttitudeAbNormal(21),
        UnActive(22),
        FlyForbiddenError(23),
        BiasError(24),
        EscError(25),
        ImuInitError(26),
        SystemUpgrade(27),
        SimulatorStarted(28),
        ImuingError(29),
        AttiAngleOver(30),
        GyroscopeError(31),
        AcceletorError(32),
        CompassFailed(33),
        BarometerError(34),
        BarometerNegative(35),
        CompassBig(36),
        GyroscopeBiasBig(37),
        AcceletorBiasBig(38),
        CompassNoiseBig(39),
        BarometerNoiseBig(40),
        InvalidSn(41),
        FLASH_OPERATING(44),
        GPS_DISCONNECT(45),
        SDCardException(47),
        IMUNoconnection(61),
        RCCalibration(62),
        RCCalibrationException(63),
        RCCalibrationUnfinished(64),
        RCCalibrationException2(65),
        RCCalibrationException3(66),
        AircraftTypeMismatch(67),
        FoundUnfinishedModule(68),
        CYRO_ABNORMAL(70),
        BARO_ABNORMAL(71),
        COMPASS_ABNORMAL(72),
        GPS_ABNORMAL(73),
        NS_ABNORMAL(74),
        TOPOLOGY_ABNORMAL(75),
        RC_NEED_CALI(76),
        INVALID_FLOAT(77),
        M600_BAT_TOO_LITTLE(78),
        M600_BAT_AUTH_ERR(79),
        M600_BAT_COMM_ERR(80),
        M600_BAT_DIF_VOLT_LARGE_1(81),
        M600_BAT_DIF_VOLT_LARGE_2(82),
        INVALID_VERSION(83),
        GIMBAL_GYRO_ABNORMAL(84),
        GIMBAL_ESC_PITCH_NON_DATA(85),
        GIMBAL_ESC_ROLL_NON_DATA(86),
        GIMBAL_ESC_YAW_NON_DATA(87),
        GIMBAL_FIRM_IS_UPDATING(88),
        GIMBAL_DISORDER(89),
        GIMBAL_PITCH_SHOCK(90),
        GIMBAL_ROLL_SHOCK(91),
        GIMBAL_YAW_SHOCK(92),
        IMUcCalibrationFinished(93),
        TAKEOFF_ROLLOVER(94),
        MOTOR_STUCK(95),
        MOTOR_UNBALANCED(96),
        MOTOR_LESS_PADDLE(97),
        MOTOR_START_ERROR(98),
        MOTOR_AUTO_TAKEOFF_FAIL(99),
        RollOverOnGround(100),
        BatVersionError(101),
        RTK_BAD_SIGNAL(102),
        RTK_DEVIATION_ERROR(103),
        ESC_SHORT_CUT_ERROR(104),
        POWER_SYSTEM_HARDWARE_ERROR(105),
        BAT_HW_VERSION_ERROR(106),
        BATTERY_IN_LOADER(107),
        ESC_CALIBRATING(112),
        GPS_SIGN_INVALID(113),
        GIMBAL_IS_CALIBRATING(114),
        LOCK_BY_APP(115),
        START_FLY_HEIGHT_ERROR(116),
        ESC_VERSION_NOT_MATCH(117),
        IMU_ORI_NOT_MATCH(118),
        STOP_BY_APP(119),
        COMPASS_IMU_ORI_NOT_MATCH(120),
        ESC_ECHOING(122),
        ESC_OVER_HEAT(123),
        BATTERY_INSTALL_ERROR(PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH),
        BE_IMPACT(FTPCodes.DATA_CONNECTION_ALREADY_OPEN),
        MODE_FAILUER(126),
        CRASH(CertificateBody.profileType),
        HEIGHT_CONTROL_ANOMALY(128),
        LOW_VERSION_OF_BATTERY(129),
        VOLTAGE_OF_BATTERY_IS_TOO_HIGH(NikonType2MakernoteDirectory.TAG_ADAPTER),
        BATTERY_EMBED_ERROR(131),
        COOLING_FAN_EXCEPTION(132),
        EAGEL_TEMPERATURE_ERROR(133),
        LOST_GPS_IN_POR_A_ERROR(134),
        RC_THROTTLE_IS_NOT_IN_MIDDLE(136),
        FLIGHT_BSP_ERROR(138),
        FLIGHT_RESTRICTION_STRATEGY_ERROR(139),
        BATTERY_ICO_ERROR(146),
        DARK_NEED_GPS(147),
        REMOTE_USB_CONNECTED(200, 255),
        OTHER(Integer.MAX_VALUE);
        
        private static volatile MotorStartFailedCause[] sValues = null;
        private final int maxValue;
        private final int minValue;
        private int relValue;

        private MotorStartFailedCause(int value) {
            this(r1, r2, value, value);
            this.relValue = value;
        }

        private MotorStartFailedCause(int minValue2, int maxValue2) {
            this.relValue = 0;
            this.minValue = minValue2;
            this.maxValue = maxValue2;
        }

        public int relValue() {
            return this.relValue;
        }

        public boolean _equals(int b) {
            return this.minValue <= b && b <= this.maxValue;
        }

        public static MotorStartFailedCause find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            MotorStartFailedCause result = OTHER;
            MotorStartFailedCause[] values = sValues;
            int i = 0;
            int length = values.length;
            while (true) {
                if (i >= length) {
                    break;
                } else if (values[i]._equals(b)) {
                    result = values[i];
                    break;
                } else {
                    i++;
                }
            }
            result.relValue = b;
            return result;
        }
    }

    @Keep
    public enum GOHOME_STATUS {
        STANDBY(0),
        PREASCENDING(1),
        ALIGN(2),
        ASCENDING(3),
        CRUISE(4),
        BRAKING(5),
        BYPASSING(6),
        OTHER(7);
        
        private static volatile GOHOME_STATUS[] sValues = null;
        private int data = 0;

        private GOHOME_STATUS(int value) {
            this.data = value;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GOHOME_STATUS find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            GOHOME_STATUS result = OTHER;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum NON_GPS_CAUSE {
        ALREADY(0),
        FORBIN(1),
        GPSNUM_NONENOUGH(2),
        GPS_HDOP_LARGE(3),
        GPS_POSITION_NONMATCH(4),
        SPEED_ERROR_LARGE(5),
        YAW_ERROR_LARGE(6),
        COMPASS_ERROR_LARGE(7),
        UNKNOWN(8);
        
        private static volatile NON_GPS_CAUSE[] sValues = null;
        private int data = 0;

        private NON_GPS_CAUSE(int value) {
            this.data = value;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static NON_GPS_CAUSE find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            NON_GPS_CAUSE result = UNKNOWN;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum IMU_INITFAIL_REASON {
        MonitorError(0),
        ColletingData(1),
        GyroDead(2),
        AcceDead(3),
        CompassDead(4),
        BarometerDead(5),
        BarometerNegative(6),
        CompassModTooLarge(7),
        GyroBiasTooLarge(8),
        AcceBiasTooLarge(9),
        CompassNoiseTooLarge(10),
        BarometerNoiseTooLarge(11),
        WaitingMcStationary(12),
        AcceMoveTooLarge(13),
        McHeaderMoved(14),
        McVirbrated(15),
        None(100);
        
        private static volatile IMU_INITFAIL_REASON[] sValues = null;
        private int _value = 0;

        private IMU_INITFAIL_REASON(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static IMU_INITFAIL_REASON find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            IMU_INITFAIL_REASON[] imu_initfail_reasonArr = sValues;
            for (IMU_INITFAIL_REASON ts : imu_initfail_reasonArr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return None;
        }
    }

    @Keep
    public enum DroneType {
        Unknown(0),
        Inspire(1),
        P3S(2),
        P3X(3),
        P3C(4),
        OpenFrame(5),
        ACEONE(6),
        WKM(7),
        NAZA(8),
        A2(9),
        A3(10),
        P4(11),
        PM820(14),
        P34K(15),
        wm220(16),
        Orange2(17),
        Pomato(18),
        N3(20),
        Mammoth(21),
        PM820PRO(23),
        WM230(24),
        M200(25),
        Potato(27),
        M210(28),
        P3SE(29),
        M210RTK(30),
        PomatoRTK(35),
        PomatoSdr(36),
        WM240(41),
        WM245(51),
        PM420(44),
        PM420PRO(45),
        PM420PRO_RTK(46),
        WM160(53),
        NoFlyc(255),
        None(100);
        
        private static volatile DroneType[] sValues = null;
        private int _value = 0;

        private DroneType(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static DroneType find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            DroneType[] droneTypeArr = sValues;
            for (DroneType ts : droneTypeArr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return None;
        }
    }

    @Keep
    public enum BatteryType {
        Unknown(0),
        NonSmart(1),
        Smart(2);
        
        private static volatile BatteryType[] sValues = null;
        private int _value = 0;

        private BatteryType(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static BatteryType find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            BatteryType[] batteryTypeArr = sValues;
            for (BatteryType ts : batteryTypeArr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return Unknown;
        }
    }

    @Keep
    public enum MotorFailReason {
        TAKEOFF_EXCEPTION(94),
        ESC_STALL_NEAR_GROUND(95),
        ESC_UNBALANCE_ON_GRD(96),
        ESC_PART_EMPTY_ON_GRD(97),
        ENGINE_START_FAILED(98),
        AUTO_TAKEOFF_LANCH_FAILED(99),
        ROLL_OVER_ON_GRD(100),
        ESC_SHORT_CIRCUIT(104),
        START_FLY_HEIGHT_ERROR(116),
        BE_IMPACT(FTPCodes.DATA_CONNECTION_ALREADY_OPEN),
        OTHER(128);
        
        private static volatile MotorFailReason[] sValues = null;
        private int _value = 0;

        private MotorFailReason(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static MotorFailReason find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            MotorFailReason[] motorFailReasonArr = sValues;
            for (MotorFailReason ts : motorFailReasonArr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return OTHER;
        }
    }

    @Keep
    enum RcModeChannelAfter16 {
        MANUAL(0),
        ATTI(1),
        ATTI_GENTLE(2),
        ATTI_MANUAL(3),
        ATTI_SPORT(4),
        GPS_ATTI(5),
        GPS_GENTLE(6),
        GPS_NORMAL(7),
        GPS_SPORT(8),
        NAVIGATION(9),
        FPV(10),
        FARM(11),
        T_MODE(12),
        OTHER(255);
        
        private static volatile RcModeChannelAfter16[] sValues = null;
        private int _value = 0;

        private RcModeChannelAfter16(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static RcModeChannelAfter16 find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            RcModeChannelAfter16[] rcModeChannelAfter16Arr = sValues;
            for (RcModeChannelAfter16 ts : rcModeChannelAfter16Arr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return OTHER;
        }
    }
}
