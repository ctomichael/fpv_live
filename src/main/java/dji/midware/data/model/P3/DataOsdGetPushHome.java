package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycGetIoc;

@Keep
@EXClassNullAway
public class DataOsdGetPushHome extends DataBase {
    public static final int COUNT_MOTOR_ESCM = 8;
    private static DataOsdGetPushHome instance = null;
    private final MotorEscmState[] mMotorEscmStates = new MotorEscmState[8];

    public static synchronized DataOsdGetPushHome getInstance() {
        DataOsdGetPushHome dataOsdGetPushHome;
        synchronized (DataOsdGetPushHome.class) {
            if (instance == null) {
                instance = new DataOsdGetPushHome();
            }
            dataOsdGetPushHome = instance;
        }
        return dataOsdGetPushHome;
    }

    public DataOsdGetPushHome() {
    }

    public DataOsdGetPushHome(boolean isRegist) {
        super(isRegist);
    }

    public double getLongitude() {
        return (((Double) get(0, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getLatitude() {
        return (((Double) get(8, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public float getHeight() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() >= 8) {
            return ((Float) get(16, 4, Float.class)).floatValue() * 0.1f;
        }
        return ((Float) get(16, 4, Float.class)).floatValue();
    }

    public DataFlycGetIoc.MODE getIOCMode() {
        return DataFlycGetIoc.MODE.find((((Integer) get(20, 2, Integer.class)).intValue() & 57344) >>> 13);
    }

    public boolean isIOCEnabled() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() & 4096) >>> 12) != 0;
    }

    public boolean isBeginnerMode() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() >> 11) & 1) != 0;
    }

    public boolean isCompassCeleing() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() & 1024) >>> 10) != 0;
    }

    public int getCompassCeleStatus() {
        return (((Integer) get(20, 2, Integer.class)).intValue() & 768) >>> 8;
    }

    public boolean hasGoHome() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() & 128) >>> 7) != 0;
    }

    public int getGoHomeStatus() {
        return (((Integer) get(20, 2, Integer.class)).intValue() & 112) >>> 4;
    }

    public boolean isMultipleModeOpen() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() >> 6) & 1) != 0 && DataOsdGetPushCommon.getInstance().getFlycVersion() >= 3;
    }

    public boolean isReatchLimitHeight() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() & 32) >>> 5) != 0;
    }

    public boolean isReatchLimitDistance() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() & 16) >>> 4) != 0;
    }

    public boolean isDynamicHomePiontEnable() {
        return ((((Integer) get(20, 2, Integer.class)).intValue() & 8) >>> 3) != 0;
    }

    public int getAircraftHeadDirection() {
        return (((Integer) get(20, 2, Integer.class)).intValue() & 4) >>> 2;
    }

    public int getGoHomeMode() {
        return (((Integer) get(20, 2, Integer.class)).intValue() & 2) >>> 1;
    }

    public boolean isHomeRecord() {
        return (((Integer) get(20, 2, Integer.class)).intValue() & 1) != 0;
    }

    public int getGoHomeHeight() {
        return ((Integer) get(22, 2, Integer.class)).intValue();
    }

    public short getCourseLockAngle() {
        return ((Short) get(24, 2, Short.class)).shortValue();
    }

    public int getDataRecorderStatus() {
        return ((Integer) get(26, 1, Integer.class)).intValue();
    }

    public int getFlycLogIndex() {
        return ((Integer) get(30, 2, Integer.class)).intValue();
    }

    public int getDataRecorderRemainCapacity() {
        return ((Integer) get(27, 1, Integer.class)).intValue();
    }

    public int getDataRecorderRemainTime() {
        return ((Integer) get(28, 2, Integer.class)).intValue();
    }

    public int getCurDataRecorderFileIndex() {
        return ((Integer) get(30, 2, Integer.class)).intValue();
    }

    public boolean isFlycInSimulationMode() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isFlycInNavigationMode() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isDetectImpactInAir() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isDetectImpactOnGrd() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isWingBroken() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 4096) != 0;
    }

    public boolean isBigGale() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 16) {
            return false;
        }
        if ((((Integer) get(32, 4, Integer.class)).intValue() & 16384) != 0) {
            return true;
        }
        return false;
    }

    public boolean isBigGaleWarning() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 16) {
            return false;
        }
        if ((((Integer) get(32, 4, Integer.class)).intValue() & 1048576) != 0) {
            return true;
        }
        return false;
    }

    public PaddleState getPaddleState() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() >= 16) {
            return PaddleState.find((((Integer) get(32, 4, Integer.class)).intValue() >> 21) & 3);
        }
        return PaddleState.NORMAL;
    }

    public boolean isCompassInstallErr() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 18) {
            return false;
        }
        if ((((Integer) get(32, 4, Integer.class)).intValue() & 8388608) != 0) {
            return true;
        }
        return false;
    }

    public boolean isFanCurrentInAbnormalState() {
        return (((Integer) get(32, 4, Integer.class)).intValue() & 67108864) != 0;
    }

    public boolean isPropellerCoverConfirmRequired() {
        return DataOsdGetPushCommon.getInstance().getFlycVersion() >= 28 && ((((Integer) get(32, 4, Integer.class)).intValue() >> 29) & 1) == 1;
    }

    public boolean isMainGPSSignalSheltered() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >> 30) & 1) == 1;
    }

    public boolean isExtraLoadDetected() {
        return ((((Integer) get(32, 4, Integer.class)).intValue() >> 31) & 1) == 1;
    }

    public HeightLimitStatus getHeightLimitStatus() {
        return HeightLimitStatus.find(((Integer) get(36, 1, Integer.class)).intValue() & 31);
    }

    public boolean useAbsoluteHeight() {
        return (((Integer) get(36, 1, Integer.class)).intValue() & 32) != 0;
    }

    public float getHeightLimitValue() {
        return ((Float) get(37, 4, Float.class)).floatValue();
    }

    public MotorEscmState[] getMotorEscmState() {
        int status = ((Integer) get(41, 4, Integer.class)).intValue();
        for (int i = 0; i < 8; i++) {
            this.mMotorEscmStates[i] = MotorEscmState.find((status >> (i * 4)) & 15);
        }
        return this.mMotorEscmStates;
    }

    public int getForceLandingHeight() {
        if (this._recData == null || this._recData.length <= 45) {
            return Integer.MIN_VALUE;
        }
        return ((Integer) get(45, 1, Integer.class)).intValue();
    }

    public int getRedunancyStatusVersion() {
        if (this._recData == null || this._recData.length < 49) {
            return 0;
        }
        return (((Integer) get(46, 4, Integer.class)).intValue() >> 0) & 3;
    }

    public boolean isIMUDevicesAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 2) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isCompassDevicesAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 3) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isBarometerDevicesAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 4) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isGpsDevicesAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 5) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isNavigationSysDevicesAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 6) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isIMUAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 12) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isCompassAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 13) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isBarometerAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 14) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isGpsAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 15) & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isNavigationSysAbnormal() {
        if (this._recData == null || this._recData.length < 49) {
            return false;
        }
        if (((((Integer) get(46, 4, Integer.class)).intValue() >> 16) & 1) == 1) {
            return true;
        }
        return false;
    }

    public int getIMUSwitchFlag() {
        if (this._recData == null || this._recData.length < 49) {
            return 0;
        }
        return (((Integer) get(46, 4, Integer.class)).intValue() >> 22) & 1;
    }

    public int getCompassSwitchFlag() {
        if (this._recData == null || this._recData.length < 49) {
            return 0;
        }
        return (((Integer) get(46, 4, Integer.class)).intValue() >> 23) & 1;
    }

    public int getBarometerSwitchFlag() {
        if (this._recData == null || this._recData.length < 49) {
            return 0;
        }
        return (((Integer) get(46, 4, Integer.class)).intValue() >> 24) & 1;
    }

    public int getGpsSwitchFlag() {
        if (this._recData == null || this._recData.length < 49) {
            return 0;
        }
        return (((Integer) get(46, 4, Integer.class)).intValue() >> 25) & 1;
    }

    public int getNavigationSysSwitchFlag() {
        if (this._recData == null || this._recData.length < 49) {
            return 0;
        }
        return (((Integer) get(46, 4, Integer.class)).intValue() >> 26) & 1;
    }

    public boolean isAboveWater() {
        return ((((Integer) get(73, 1, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public PropCoverLimitState getPropCoverLimitState() {
        if (this._recData == null || this._recData.length < 75) {
            return PropCoverLimitState.UNKNOWN;
        }
        return PropCoverLimitState.find(((Integer) get(74, 1, Integer.class)).intValue());
    }

    public EnvTempState getEnvTempState() {
        if (this._recData == null || this._recData.length < 76) {
            return EnvTempState.TEMP_NORMAL;
        }
        return EnvTempState.find(((Integer) get(75, 1, Integer.class)).intValue() & 3);
    }

    public GPSJammingState getGPSJammingState() {
        return GPSJammingState.find((((Integer) get(75, 1, Integer.class)).intValue() >>> 3) & 3);
    }

    public GPSSpoofingState getGPSSpoofingState() {
        return GPSSpoofingState.find((((Integer) get(75, 1, Integer.class)).intValue() >>> 5) & 3);
    }

    public int getMotorPropellerAbnormalState() {
        return ((Integer) get(77, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public enum GPSJammingState {
        GPS_JAMMING_STATE_UNKNOWN(0),
        GPS_JAMMING_STATE_OK(1),
        GPS_JAMMING_STATE_WARNNING(2),
        GPS_JAMMING_STATE_CRITICAL(3),
        UNKNOWN(255);
        
        private static GPSJammingState[] values = values();
        private int data;

        private GPSJammingState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static GPSJammingState find(int b) {
            GPSJammingState result = UNKNOWN;
            for (int i = 0; i < values.length; i++) {
                if (values[i]._equals(b)) {
                    return values[i];
                }
            }
            return result;
        }
    }

    public enum GPSSpoofingState {
        GPS_SPOOFING_STATE_UNKNOW(0),
        GPS_SPOOFING_STATE_OK(1),
        GPS_SPOOFING_STATE_WARNING(2),
        GPS_SPOOFING_STATE_CRITICAL(3),
        UNKNOWN(255);
        
        private static GPSSpoofingState[] values = values();
        private int data;

        private GPSSpoofingState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static GPSSpoofingState find(int b) {
            GPSSpoofingState result = UNKNOWN;
            for (int i = 0; i < values.length; i++) {
                if (values[i]._equals(b)) {
                    return values[i];
                }
            }
            return result;
        }
    }

    public enum EnvTempState {
        TEMP_NORMAL(0),
        TEMP_HIGH(1),
        TEMP_LOW(2),
        UNKNOWN(255);
        
        private static EnvTempState[] values = values();
        private int data;

        private EnvTempState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static EnvTempState find(int b) {
            EnvTempState result = UNKNOWN;
            for (int i = 0; i < values.length; i++) {
                if (values[i]._equals(b)) {
                    return values[i];
                }
            }
            return result;
        }
    }

    public enum PropCoverLimitState {
        PROP_COVER_ON_GND(0),
        PROP_COVER_APP_ENABLE_NO_LIMIT(1),
        PROP_COVER_APP_ENABLE_LIMIT(2),
        PROP_COVER_FC_ENABLE_NO_LIMIT(3),
        PROP_COVER_FC_ENABLE_LIMIT(4),
        PROP_COVER_DISABLE_IN_AIR(5),
        UNKNOWN(255);
        
        private static PropCoverLimitState[] values = values();
        private int data;

        private PropCoverLimitState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static PropCoverLimitState find(int b) {
            PropCoverLimitState result = UNKNOWN;
            for (int i = 0; i < values.length; i++) {
                if (values[i]._equals(b)) {
                    return values[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum HeightLimitStatus {
        NON_LIMIT(0),
        NON_GPS(1),
        ORIENTATION_NEED_CALI(2),
        ORIENTATION_GO(3),
        AVOID_GROUND(4),
        NORMAL_LIMIT(5),
        LIMIT_BY_NFZ(6),
        LIMIT_BY_NOVICE_MODE(7),
        LIMIT_BY_WIFI_MODE(8),
        LIMIT_BY_WRISTBAND(9),
        LIMIT_BY_REALNAME(10),
        LIMIT_BY_INVALID_REF_HEIGHT(11),
        LIMIT_BY_PROP_COVER(12);
        
        private static volatile HeightLimitStatus[] sValues = null;
        private int _value = 0;

        private HeightLimitStatus(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static HeightLimitStatus find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            HeightLimitStatus[] heightLimitStatusArr = sValues;
            for (HeightLimitStatus ts : heightLimitStatusArr) {
                if (ts._equals(value)) {
                    return ts;
                }
            }
            return NON_LIMIT;
        }
    }

    @Keep
    public enum MotorEscmState {
        NON_SMART(0),
        DISCONNECT(1),
        SIGNAL_ERROR(2),
        RESISTANCE_ERROR(3),
        BLOCK(4),
        NON_BALANCE(5),
        ESCM_ERROR(6),
        PROPELLER_OFF(7),
        MOTOR_IDLE(8),
        MOTOR_UP(9),
        MOTOR_OFF(10),
        NON_CONNECT(11),
        OTHER(100);
        
        private static volatile MotorEscmState[] sValues = null;
        private int _value = 0;

        private MotorEscmState(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static MotorEscmState find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            MotorEscmState[] motorEscmStateArr = sValues;
            for (MotorEscmState arg : motorEscmStateArr) {
                if (arg._equals(value)) {
                    return arg;
                }
            }
            return NON_CONNECT;
        }
    }

    @Keep
    public enum PaddleState {
        NORMAL(0),
        FLATLAND_ON_HIGHLAND(1),
        HIGHLAND_ON_FLATLAND(2),
        OTHER(3);
        
        private static volatile PaddleState[] sValues = null;
        private int _value = 0;

        private PaddleState(int value) {
            this._value = value;
        }

        private boolean _equal(int value) {
            return this._value == value;
        }

        public static PaddleState find(int value) {
            if (sValues == null) {
                sValues = values();
            }
            PaddleState[] paddleStateArr = sValues;
            for (PaddleState arg : paddleStateArr) {
                if (arg._equal(value)) {
                    return arg;
                }
            }
            return NORMAL;
        }
    }
}
