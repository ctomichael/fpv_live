package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataSingleVisualParam extends DataBase implements DJIDataSyncListener {
    private boolean bGet = false;
    private float mBackWard = 0.0f;
    private float mCircleY = 0.0f;
    private byte[] mCustomData = null;
    private int mCustomValue = 0;
    private float mFollowGain = 0.5f;
    private ParamCmdId mParamCmdId = ParamCmdId.OTHER;
    private float mSettingSpeed = 0.0f;
    private TrackingMode mTrackMode = TrackingMode.HEADLESS_FOLLOW;

    public DataSingleVisualParam setGet(boolean get) {
        this.bGet = get;
        return this;
    }

    public DataSingleVisualParam setParamCmdId(ParamCmdId id) {
        this.mParamCmdId = id;
        return this;
    }

    public DataSingleVisualParam setTrackMode(TrackingMode mode) {
        this.mTrackMode = mode;
        return this;
    }

    public TrackingMode getTrackMode() {
        TrackingMode mode = TrackingMode.HEADLESS_FOLLOW;
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_MODE) {
            return mode;
        }
        return TrackingMode.find(((Integer) get(2, 1, Integer.class)).intValue());
    }

    public DataSingleVisualParam setFollowGain(float gain) {
        this.mFollowGain = gain;
        return this;
    }

    public float getFollowGain() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_FOLLOW_GAIN) {
            return 0.5f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public DataSingleVisualParam setBackWardGain(float value) {
        this.mBackWard = value;
        return this;
    }

    public float getBackWardGain() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_BACKWARD) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public boolean getPalmControlAway() {
        int open = 0;
        if (this._recData != null && this._recData.length > 0 && ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) == ParamCmdId.PALM_CONTROL_AWAY) {
            open = ((Integer) get(2, 1, Integer.class)).intValue();
        }
        return open == 1;
    }

    public boolean getPanoramaSaveOrg() {
        int open = 0;
        if (this._recData != null && this._recData.length > 0 && ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) == ParamCmdId.PANORAMA_SAVE_ORG) {
            open = ((Integer) get(2, 1, Integer.class)).intValue();
        }
        return open == 1;
    }

    public DataSingleVisualParam setTrackAss(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public boolean getTrackAss() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_ASS) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public DataSingleVisualParam setCircleY(float ySpeed) {
        this.mCircleY = ySpeed;
        return this;
    }

    public float getCircleY() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_CIRCLE_Y) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public DataSingleVisualParam setTrackIntelligent(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public boolean getTrackIntelligent() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_INTELLIGENT) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public DataSingleVisualParam setLogEnable(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setOmniAvoidanceEnable(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public boolean getOmniAvoidanceEnable() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.OMNI_AVOIDANCE_SWITCH) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public boolean isMultiTrackingEnabled() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.MULTI_TRACKING) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() == 1) {
            return true;
        }
        return false;
    }

    public boolean isMultiQuickShotEnabled() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.MULTI_QUICK_SHOT) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() == 1) {
            return true;
        }
        return false;
    }

    public DataSingleVisualParam setTrackHeading(TrackHeading heading) {
        this.mCustomValue = heading.value();
        return this;
    }

    public boolean isHorizontalObstacleBypassEnabled() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public TrackHeading getTrackHeading() {
        TrackHeading heading = TrackHeading.FOLLOW;
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_PROFILE_HEAD) {
            return heading;
        }
        return TrackHeading.find(((Integer) get(2, 1, Integer.class)).intValue());
    }

    public DataSingleVisualParam setTrackAutoFocus(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public boolean getTrackAutoFocus() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_AUTO_FOCUS) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public DataSingleVisualParam setRcGimbalCtrl(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setPOIEnabled(boolean isEnabled) {
        this.mCustomValue = isEnabled ? 1 : 0;
        return this;
    }

    public boolean isRcGimbalCtrlEnable() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.FLY_RC_GIMBAL_CTRL) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public DataSingleVisualParam setPalmControlMode(int palmControlMode) {
        this.mCustomValue = palmControlMode;
        return this;
    }

    public int getPalmControlMode() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.PALM_CONTROL_MODE) {
            return 0;
        }
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public DataSingleVisualParam setTrackingMaximumSpeed(int speed) {
        this.mCustomValue = speed;
        return this;
    }

    public DataSingleVisualParam setGpsTracking(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setPalmControlAway(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setPanoramaSaveOrg(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public boolean getGpsTracking() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TRACK_GPS) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public DataSingleVisualParam setUserSpeed(float speed) {
        this.mSettingSpeed = speed;
        return this;
    }

    public float getUserSpeed() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.FLY_USER_SPEED) {
            return 3.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public DataSingleVisualParam setParallelGo(int value) {
        this.mCustomValue = value;
        return this;
    }

    public int getParallelGo() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.FLY_PARALLEL_GO) {
            return 0;
        }
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public DataSingleVisualParam setYawResponse(int value) {
        this.mCustomValue = value;
        return this;
    }

    public DataSingleVisualParam setMultiTrackEnabled(boolean isEnabled) {
        this.mCustomValue = isEnabled ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setMultiQuickShotEnabled(boolean isEnabled) {
        this.mCustomValue = isEnabled ? 1 : 0;
        return this;
    }

    public int getYawResponse() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.FLY_YAW_RESPONE) {
            return 0;
        }
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public DataSingleVisualParam setHomingMonoSenseOn(int value) {
        this.mCustomValue = value;
        return this;
    }

    public DataSingleVisualParam setTimeLapseSpeed(float value) {
        this.mCustomData = BytesUtil.getBytes(value);
        return this;
    }

    public int getHomingMonoSenseOn() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.HOMING_MONO_SENSE_ON) {
            return 0;
        }
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public DataSingleVisualParam setDrawHeading(DrawHeading heading) {
        this.mCustomData = new byte[1];
        this.mCustomData[0] = (byte) heading.value();
        return this;
    }

    public DrawHeading getDrawHeading() {
        DrawHeading heading = DrawHeading.FREE;
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.DRAW_HEADING) {
            return heading;
        }
        return DrawHeading.find(((Integer) get(2, 1, Integer.class)).intValue());
    }

    public DataSingleVisualParam setDrawMode(DrawMode mode) {
        this.mCustomData = new byte[1];
        this.mCustomData[0] = (byte) mode.value();
        return this;
    }

    public DrawMode getDrawMode() {
        DrawMode mode = DrawMode.AUTO;
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.DRAW_MODE) {
            return mode;
        }
        return DrawMode.find(((Integer) get(2, 1, Integer.class)).intValue());
    }

    public DataSingleVisualParam setDrawSpeed(float speed) {
        this.mCustomData = BytesUtil.getBytes(speed);
        return this;
    }

    public float getQuickMovieDronieDistance() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.QUICK_MOVIE_DRONIE_MAXIMUM_DISTANCE) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public float getQuickMovieHelixDistance() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.QUICK_MOVIE_HELIX_MAXIMUM_DISTANCE) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public float getQuickMovieRocketDistance() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.QUICK_MOVIE_ROCKET_MAXIMUM_DISTANCE) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public float getDrawSpeed() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.DRAW_SPEED) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public DataSingleVisualParam setDynamicHome(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setHighSpeedEnabled(boolean enabled) {
        this.mCustomValue = enabled ? 1 : 0;
        return this;
    }

    public DataSingleVisualParam setTimelapseEnabled(boolean enable) {
        this.mCustomValue = enable ? 1 : 0;
        return this;
    }

    public boolean getDynamicHome() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.DYNAMIC_HOME) {
            return false;
        }
        if (((Integer) get(2, 1, Integer.class)).intValue() != 0) {
            return true;
        }
        return false;
    }

    public float getTimeLapseSpeed() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TIME_LAPSE_SPEED) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public float getTimeLapseMinSpeed() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TIME_LAPSE_MIN_SPEED) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public float getTimeLapseMaxSpeed() {
        if (this._recData == null || this._recData.length <= 0 || ParamCmdId.find(((Integer) get(0, 1, Integer.class)).intValue()) != ParamCmdId.TIME_LAPSE_MAX_SPEED) {
            return 0.0f;
        }
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public DataSingleVisualParam setTapFlyEnterOrExit(boolean enter) {
        setParamCmdId(ParamCmdId.TAPFLY_ENTER_EXIT_SET);
        this.mCustomValue = enter ? 1 : 0;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.bGet) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) this.mParamCmdId.value();
            return;
        }
        int paramLength = 0;
        switch (this.mParamCmdId) {
            case TRACK_MODE:
                paramLength = 1;
                updateTrackModeSet();
                break;
            case TRACK_PROFILE_HEAD:
            case TRACK_ASS:
            case TRACK_INTELLIGENT:
            case MULTI_TRACKING:
            case MULTI_QUICK_SHOT:
            case TRACK_GPS:
            case FLY_PARALLEL_GO:
            case FLY_YAW_RESPONE:
            case HOMING_MONO_SENSE_ON:
            case TRACK_MAXIMUM_SPEED:
            case FLY_RC_GIMBAL_CTRL:
            case PALM_CONTROL_AWAY:
            case TRACK_AUTO_FOCUS:
            case DYNAMIC_HOME:
            case LOG_ENABLE:
            case OMNI_AVOIDANCE_SWITCH:
            case PANORAMA_SAVE_ORG:
            case PALM_CONTROL_MODE:
            case POI_ENABLED:
            case TIMELAPSE:
            case TAPFLY_ENTER_EXIT_SET:
            case HIGH_SPEED_ENABLED:
                paramLength = 1;
                update3ByteDataValue();
                break;
            case TRACK_FOLLOW_GAIN:
                paramLength = 4;
                updateTrackFollowGain();
                break;
            case TRACK_BACKWARD:
                paramLength = 4;
                updateTrackBackWard();
                break;
            case TRACK_CIRCLE_Y:
                paramLength = 4;
                updateTrackCircle();
                break;
            case FLY_USER_SPEED:
                paramLength = 4;
                updateFlyUserSpeed();
                break;
            case DRAW_HEADING:
            case DRAW_MODE:
            case DRAW_SPEED:
                paramLength = this.mCustomData.length;
                updateDrawData();
                break;
            case TIME_LAPSE_SPEED:
            case TIME_LAPSE_MAX_SPEED:
            case TIME_LAPSE_MIN_SPEED:
                paramLength = 4;
                updateTimelapseSpeed(4);
                break;
        }
        if (this._sendData != null && this._sendData.length >= 2) {
            this._sendData[0] = (byte) this.mParamCmdId.value();
            this._sendData[1] = (byte) paramLength;
        }
    }

    private void updateFlyUserSpeed() {
        this._sendData = new byte[6];
        System.arraycopy(BytesUtil.getBytes(this.mSettingSpeed), 0, this._sendData, 2, 4);
    }

    private void updateTrackCircle() {
        this._sendData = new byte[6];
        System.arraycopy(BytesUtil.getBytes(this.mCircleY), 0, this._sendData, 2, 4);
    }

    private void updateTrackBackWard() {
        this._sendData = new byte[6];
        System.arraycopy(BytesUtil.getBytes(this.mBackWard), 0, this._sendData, 2, 4);
    }

    private void updateTrackFollowGain() {
        this._sendData = new byte[6];
        System.arraycopy(BytesUtil.getBytes(this.mFollowGain), 0, this._sendData, 2, 4);
    }

    private void updateTimelapseSpeed(int paramLength) {
        this._sendData = new byte[((short) (this.mCustomData.length + 2))];
        System.arraycopy(this.mCustomData, 0, this._sendData, 2, paramLength);
    }

    private void updateDrawData() {
        this._sendData = new byte[((short) (this.mCustomData.length + 2))];
        System.arraycopy(this.mCustomData, 0, this._sendData, 2, this.mCustomData.length);
    }

    private void updateTrackModeSet() {
        this._sendData = new byte[3];
        this._sendData[2] = (byte) this.mTrackMode.value();
    }

    private void update3ByteDataValue() {
        this._sendData = new byte[3];
        this._sendData[2] = (byte) this.mCustomValue;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        if (this.bGet) {
            pack.cmdId = CmdIdEYE.CmdIdType.GetParams.value();
        } else {
            pack.cmdId = CmdIdEYE.CmdIdType.SetParam.value();
        }
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    @Keep
    public enum ParamCmdId {
        TRACK_MODE(1),
        TRACK_TERRAIN_FOLLOW(2),
        TRACK_FOLLOW_GAIN(3),
        QUICK_MOVIE_DRONIE_MAXIMUM_DISTANCE(4),
        HIGH_SPEED_ENABLED(2),
        TRACK_BACKWARD(6),
        TRACK_PROFILE_HEAD(7),
        QUICK_MOVIE_CIRCLE_NUMBER(8),
        TRACK_ASS(12),
        PALM_CONTROL_MODE(13),
        TRACK_CIRCLE_Z(14),
        TRACK_CIRCLE_X(15),
        TRACK_CIRCLE_Y(26),
        TRACK_MAXIMUM_SPEED(27),
        TRACK_GPS(31),
        TRACK_INTELLIGENT(41),
        MULTI_TRACKING(44),
        MULTI_QUICK_SHOT(46),
        FLY_USER_SPEED(32),
        FLY_PARALLEL_GO(33),
        FLY_YAW_RESPONE(34),
        DRAW_SPEED(35),
        DRAW_HEADING(36),
        DRAW_MODE(37),
        FLY_RC_GIMBAL_CTRL(38),
        QUICK_MOVIE_HELIX_MAXIMUM_DISTANCE(39),
        DYNAMIC_HOME(40),
        QUICK_MOVIE_ROCKET_MAXIMUM_DISTANCE(42),
        HOMING_MONO_SENSE_ON(50),
        PALM_CONTROL_AWAY(56),
        PANORAMA_SAVE_ORG(60),
        TRACK_AUTO_FOCUS(66),
        POI_ENABLED(74),
        LOG_ENABLE(45),
        OMNI_AVOIDANCE_SWITCH(68),
        TIMELAPSE(71),
        TIME_LAPSE_SPEED(72),
        TIME_LAPSE_MIN_SPEED(78),
        TIME_LAPSE_MAX_SPEED(79),
        TAPFLY_ENTER_EXIT_SET(80),
        OTHER(255);
        
        private static volatile ParamCmdId[] paramCmdIds;
        private int data;

        private ParamCmdId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ParamCmdId find(int b) {
            ParamCmdId result = OTHER;
            if (paramCmdIds == null) {
                paramCmdIds = values();
            }
            for (int i = 0; i < paramCmdIds.length; i++) {
                if (paramCmdIds[i]._equals(b)) {
                    return paramCmdIds[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum TrackingMode {
        HEADLESS_FOLLOW(0),
        PARALLEL_FOLLOW(1),
        FIXED_ANGLE(2),
        WATCH_TARGET(3),
        HEAD_LOCK(4),
        WAYPOINT(5),
        QUICK_MOVIE(6),
        SPOTLIGHT(10),
        OTHER(255);
        
        private int data;

        private TrackingMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TrackingMode find(int b) {
            TrackingMode result = HEADLESS_FOLLOW;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DrawHeading {
        FREE(0),
        FORWARD(1),
        OTHER(100);
        
        private final int data;

        private DrawHeading(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DrawHeading find(int b) {
            DrawHeading result = FREE;
            DrawHeading[] values = values();
            for (DrawHeading tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum TrackHeading {
        FOLLOW(0),
        FORWARD(1),
        OTHER(100);
        
        private final int data;

        private TrackHeading(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TrackHeading find(int b) {
            TrackHeading result = FOLLOW;
            TrackHeading[] values = values();
            for (TrackHeading tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum DrawMode {
        AUTO(4),
        MANUAL(5),
        OTHER(100);
        
        private final int data;

        private DrawMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DrawMode find(int b) {
            DrawMode result = AUTO;
            DrawMode[] values = values();
            for (DrawMode tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
