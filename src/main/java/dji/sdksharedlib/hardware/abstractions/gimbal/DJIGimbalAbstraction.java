package dji.sdksharedlib.hardware.abstractions.gimbal;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.error.DJIGimbalError;
import dji.common.gimbal.Attitude;
import dji.common.gimbal.BalanceTestResult;
import dji.common.gimbal.CapabilityKey;
import dji.common.gimbal.GimbalMode;
import dji.common.gimbal.MotorControlPreset;
import dji.common.gimbal.MovementSettingsProfile;
import dji.common.gimbal.Rotation;
import dji.common.gimbal.RotationMode;
import dji.common.handheldcontroller.ControllerMode;
import dji.common.util.CallbackUtils;
import dji.common.util.DJIParamCapability;
import dji.common.util.DJIParamMinMaxCapability;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIGimbalParamInfoManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataEyeGetPushStabilizationState;
import dji.midware.data.model.P3.DataGimbalAbsAngleControl;
import dji.midware.data.model.P3.DataGimbalAutoCalibration;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.model.P3.DataGimbalGetPushAutoCalibrationStatus;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataGimbalGetPushUserParams;
import dji.midware.data.model.P3.DataGimbalGetUserParams;
import dji.midware.data.model.P3.DataGimbalNewResetAndSetMode;
import dji.midware.data.model.P3.DataGimbalResetUserParams;
import dji.midware.data.model.P3.DataGimbalRollFinetune;
import dji.midware.data.model.P3.DataGimbalSetUserParams;
import dji.midware.data.model.P3.DataGimbalSpeedControl;
import dji.midware.data.model.P3.DataOnboardGetPushDualGimbals;
import dji.midware.data.model.P3.DataOnboardGetPushMixInfo;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.params.P3.GimbalParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class DJIGimbalAbstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "DJIGimbalAbstraction";
    protected Attitude attitude;
    protected int calibrationProgress = 0;
    public Map<CapabilityKey, DJIParamCapability> gimbalCapability;
    protected int index = 0;
    protected boolean isCalibrating = false;
    protected boolean isCalibrationSuccessful = true;
    protected boolean isTestingBalance = false;
    protected boolean needPauseStabilization = false;
    protected BalanceTestResult pitchResult = BalanceTestResult.UNKNOWN;
    protected DataGimbalGetPushUserParams pushUserParams = null;
    protected BalanceTestResult rollResult = BalanceTestResult.UNKNOWN;
    /* access modifiers changed from: private */
    public CountDownLatch setParamCdl;
    /* access modifiers changed from: private */
    public DJIError setParamErr;
    /* access modifiers changed from: private */
    public int setParamGetVal = Integer.MIN_VALUE;
    protected List<CountDownLatch> waitList = new LinkedList();

    public void init(String parentPath, int index2, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(parentPath, index2, storeLayer, onValueChangeListener);
        this.index = index2;
        this.pushUserParams = DataGimbalGetPushUserParams.getInstance();
        DJIEventBusUtil.register(this);
        initGimbalCapability();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        didInitAbstraction();
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(GimbalKeys.class, getClass());
    }

    protected enum SettingParamCmd {
        TABLE_CHOICE("table_choice"),
        SMOOTH_TRACK_PITCH_SPEED("pitch_speed"),
        SMOOTH_TRACK_YAW_SPEED("yaw_speed"),
        SMOOTH_TRACK_ROLL_SPEED("roll_speed"),
        SMOOTH_TRACK_PITCH_DEADBAND("pitch_deadband"),
        SMOOTH_TRACK_YAW_DEADBAND("yaw_deadband"),
        SMOOTH_TRACK_ROLL_DEADBAND("roll_deadband"),
        SMOOTH_TRACK_PITCH_ACCEL("pitch_accel"),
        SMOOTH_TRACK_YAW_ACCEL("yaw_accel"),
        SMOOTH_TRACK_ROLL_ACCEL("roll_accel"),
        CONTROLLER_PITCH_SPEED("pitch_expo"),
        CONTROLLER_YAW_SPEED("yaw_expo"),
        CONTROLLER_PITCH_MAX_SPEED("pitch_max_speed"),
        CONTROLLER_YAW_MAX_SPEED("yaw_max_speed"),
        CONTROLLER_PITCH_SMOOTH("pitch_time_expo"),
        CONTROLLER_YAW_SMOOTH("yaw_time_expo"),
        PITCH_SMOOTH_TRACK_ENABLED("pitch_smooth_track"),
        YAW_SMOOTH_TRACK_ENABLED("yaw_smooth_track"),
        PITCH_EXP(GimbalParamCfgName.GIMBAL_PITCH_LIMIT),
        ROLL_SMOOTH_TRACK_ENABLED("roll_smooth_track"),
        YAW_SYNCHRONOUS_FOLLOW_ENABLED("yaw_follow_exp");
        
        private String cmd;

        private SettingParamCmd(String cmd2) {
            this.cmd = cmd2;
        }

        public String getCmdString() {
            return this.cmd;
        }
    }

    /* access modifiers changed from: protected */
    public void initGimbalCapability() {
        this.gimbalCapability = new HashMap();
        addDefaultToCapability(CapabilityKey.ADJUST_PITCH);
        addDefaultToCapability(CapabilityKey.ADJUST_YAW);
        addDefaultToCapability(CapabilityKey.ADJUST_ROLL);
        addDefaultToCapability(CapabilityKey.PITCH_RANGE_EXTENSION);
        addDefaultToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT);
        addDefaultToCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT);
        addDefaultToCapability(CapabilityKey.PITCH_CONTROLLER_MAX_SPEED);
        addDefaultToCapability(CapabilityKey.YAW_CONTROLLER_MAX_SPEED);
        addDefaultToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR);
        addDefaultToCapability(CapabilityKey.YAW_CONTROLLER_SMOOTHING_FACTOR);
        addDefaultToCapability(CapabilityKey.PITCH_CONTROLLER_DEADBAND);
        addDefaultToCapability(CapabilityKey.YAW_CONTROLLER_DEADBAND);
        addDefaultToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_ENABLED);
        addDefaultToCapability(CapabilityKey.YAW_SMOOTH_TRACK_ENABLED);
        addDefaultToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_ACCELERATION);
        addDefaultToCapability(CapabilityKey.YAW_SMOOTH_TRACK_ACCELERATION);
        addDefaultToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_SPEED);
        addDefaultToCapability(CapabilityKey.YAW_SMOOTH_TRACK_SPEED);
        addDefaultToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_DEADBAND);
        addDefaultToCapability(CapabilityKey.YAW_SMOOTH_TRACK_DEADBAND);
        addDefaultToCapability(CapabilityKey.PITCH_UP_ENDPOINT);
        addDefaultToCapability(CapabilityKey.PITCH_DOWN_ENDPOINT);
        addDefaultToCapability(CapabilityKey.YAW_LEFT_ENDPOINT);
        addDefaultToCapability(CapabilityKey.YAW_RIGHT_ENDPOINT);
        addDefaultToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_STIFFNESS);
        addDefaultToCapability(CapabilityKey.YAW_MOTOR_CONTROL_STIFFNESS);
        addDefaultToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_STIFFNESS);
        addDefaultToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_STRENGTH);
        addDefaultToCapability(CapabilityKey.YAW_MOTOR_CONTROL_STRENGTH);
        addDefaultToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_STRENGTH);
        addDefaultToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_GYRO_FILTERING_FACTOR);
        addDefaultToCapability(CapabilityKey.YAW_MOTOR_CONTROL_GYRO_FILTERING_FACTOR);
        addDefaultToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_GYRO_FILTERING_FACTOR);
        addDefaultToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_PRE_CONTROL);
        addDefaultToCapability(CapabilityKey.YAW_MOTOR_CONTROL_PRE_CONTROL);
        addDefaultToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_PRE_CONTROL);
        addDefaultToCapability(CapabilityKey.MOVEMENT_SETTINGS);
    }

    /* access modifiers changed from: protected */
    public void addDefaultToCapability(CapabilityKey capabilityKey) {
        if (this.gimbalCapability == null) {
            initGimbalCapability();
        }
        if (DJIParamMinMaxCapability.class.equals(capabilityKey.capabilityClass())) {
            this.gimbalCapability.put(capabilityKey, new DJIParamMinMaxCapability(false, 0, 0));
        } else {
            this.gimbalCapability.put(capabilityKey, new DJIParamCapability(false));
        }
    }

    /* access modifiers changed from: protected */
    public void addIsSupportedToCapability(CapabilityKey capabilityKey, boolean isSupported) {
        if (this.gimbalCapability == null) {
            initGimbalCapability();
        }
        if (DJIParamMinMaxCapability.class.equals(capabilityKey.capabilityClass())) {
            this.gimbalCapability.put(capabilityKey, new DJIParamMinMaxCapability(isSupported, 0, 0));
        } else {
            this.gimbalCapability.put(capabilityKey, new DJIParamCapability(isSupported));
        }
    }

    /* access modifiers changed from: protected */
    public void addMinMaxToCapability(CapabilityKey capabilityKey, Number min, Number max) {
        if (this.gimbalCapability == null) {
            initGimbalCapability();
        }
        if (DJIParamMinMaxCapability.class.equals(capabilityKey.capabilityClass())) {
            this.gimbalCapability.put(capabilityKey, new DJIParamMinMaxCapability(true, min, max));
        } else {
            this.gimbalCapability.put(capabilityKey, new DJIParamCapability(true));
        }
    }

    public void didInitAbstraction() {
        saveGimbalCapabilityToStore();
    }

    /* access modifiers changed from: protected */
    public void saveGimbalCapabilityToStore() {
        notifyValueChangeForKeyPathFromSetter(this.gimbalCapability, GimbalKeys.CAPABILITIES);
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability castMinMaxCapability(DJIParamCapability capability) {
        if (capability == null || !(capability instanceof DJIParamMinMaxCapability)) {
            return null;
        }
        return (DJIParamMinMaxCapability) capability;
    }

    /* access modifiers changed from: protected */
    public Number getMinValueFromCapability(CapabilityKey capabilityKey) {
        if (this.gimbalCapability == null || castMinMaxCapability(this.gimbalCapability.get(capabilityKey)) == null) {
            return Integer.MAX_VALUE;
        }
        return castMinMaxCapability(this.gimbalCapability.get(capabilityKey)).getMin();
    }

    /* access modifiers changed from: protected */
    public Number getMaxValueFromCapability(CapabilityKey capabilityKey) {
        if (this.gimbalCapability == null || castMinMaxCapability(this.gimbalCapability.get(capabilityKey)) == null) {
            return Integer.MIN_VALUE;
        }
        return castMinMaxCapability(this.gimbalCapability.get(capabilityKey)).getMax();
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(Number val, Number minVal, Number maxVal) {
        return val.doubleValue() >= minVal.doubleValue() && val.doubleValue() <= maxVal.doubleValue();
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(Number val, Number minVal, Number maxVal, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (val == null || minVal == null || maxVal == null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
            return false;
        } else if (checkValueValid(val, minVal, maxVal)) {
            return true;
        } else {
            if (callback == null) {
                return false;
            }
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void setParam(final int val, final SettingParamCmd cmd, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (cmd == null) {
            callback.onFails(DJIGimbalError.COMMON_PARAM_ILLEGAL);
            return;
        }
        switch (cmd) {
            case SMOOTH_TRACK_PITCH_SPEED:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.PITCH_SMOOTH_TRACK_SPEED), getMaxValueFromCapability(CapabilityKey.PITCH_SMOOTH_TRACK_SPEED), callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_YAW_SPEED:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.YAW_SMOOTH_TRACK_SPEED), getMaxValueFromCapability(CapabilityKey.YAW_SMOOTH_TRACK_SPEED), callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_ROLL_SPEED:
                if (!checkValueValid(Integer.valueOf(val), 1, 100, callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_PITCH_DEADBAND:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.PITCH_SMOOTH_TRACK_DEADBAND), getMaxValueFromCapability(CapabilityKey.PITCH_SMOOTH_TRACK_DEADBAND), callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_YAW_DEADBAND:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.YAW_SMOOTH_TRACK_DEADBAND), getMaxValueFromCapability(CapabilityKey.YAW_SMOOTH_TRACK_DEADBAND), callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_ROLL_DEADBAND:
                if (!checkValueValid(Integer.valueOf(val), 0, 30, callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_PITCH_ACCEL:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.PITCH_SMOOTH_TRACK_ACCELERATION), getMaxValueFromCapability(CapabilityKey.PITCH_SMOOTH_TRACK_ACCELERATION), callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_YAW_ACCEL:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.YAW_SMOOTH_TRACK_ACCELERATION), getMaxValueFromCapability(CapabilityKey.YAW_SMOOTH_TRACK_ACCELERATION), callback)) {
                    return;
                }
                break;
            case SMOOTH_TRACK_ROLL_ACCEL:
                if (!checkValueValid(Integer.valueOf(val), 0, 30, callback)) {
                    return;
                }
                break;
            case CONTROLLER_PITCH_SPEED:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT), getMaxValueFromCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT), callback)) {
                    return;
                }
                break;
            case CONTROLLER_YAW_SPEED:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT), getMaxValueFromCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT), callback)) {
                    return;
                }
                break;
            case CONTROLLER_PITCH_MAX_SPEED:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT), getMaxValueFromCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT), callback)) {
                    return;
                }
                break;
            case CONTROLLER_YAW_MAX_SPEED:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT), getMaxValueFromCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT), callback)) {
                    return;
                }
                break;
            case CONTROLLER_PITCH_SMOOTH:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR), getMaxValueFromCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR), callback)) {
                    return;
                }
                break;
            case CONTROLLER_YAW_SMOOTH:
                if (!checkValueValid(Integer.valueOf(val), getMinValueFromCapability(CapabilityKey.YAW_CONTROLLER_SMOOTHING_FACTOR), getMaxValueFromCapability(CapabilityKey.YAW_CONTROLLER_SMOOTHING_FACTOR), callback)) {
                    return;
                }
                break;
        }
        this.setParamGetVal = Integer.MIN_VALUE;
        ((DataGimbalSetUserParams) DataGimbalSetUserParams.getInstance().setInfo(cmd.getCmdString(), Integer.valueOf(val)).setReceiverId(getReceiverIdByIndex(), DataGimbalSetUserParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                callback.onSuccess(Integer.valueOf(val));
            }

            public void onFailure(Ccode ccode) {
                if (DJIGimbalAbstraction.this.setParamGetVal != val) {
                    callback.onFails(DJIGimbalError.getDJIError(ccode));
                }
            }
        });
        new Thread(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass2 */

            public void run() {
                int i = 0;
                while (i < 5) {
                    try {
                        Thread.sleep(200);
                        CountDownLatch unused = DJIGimbalAbstraction.this.setParamCdl = new CountDownLatch(1);
                        DJIGimbalAbstraction.this.getParam(cmd, new DJISDKCacheHWAbstraction.DefaultGetInnerCallback(null, null) {
                            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass2.AnonymousClass1 */

                            public void onSuccess(Object o) {
                                DJIError unused = DJIGimbalAbstraction.this.setParamErr = null;
                                int unused2 = DJIGimbalAbstraction.this.setParamGetVal = ((Integer) o).intValue();
                                DJIGimbalAbstraction.this.setParamCdl.countDown();
                            }

                            public void onFails(DJIError error) {
                                DJIGimbalAbstraction.this.setParamCdl.countDown();
                            }
                        });
                        DJIGimbalAbstraction.this.setParamCdl.await(1, TimeUnit.SECONDS);
                        if (DJIGimbalAbstraction.this.setParamGetVal == val) {
                            callback.onSuccess(Integer.valueOf(DJIGimbalAbstraction.this.setParamGetVal));
                            return;
                        } else if (i == 4) {
                            callback.onFails(DJIError.COMMON_TIMEOUT);
                            return;
                        } else {
                            i++;
                        }
                    } catch (InterruptedException e) {
                        DJILog.e(DJIGimbalAbstraction.TAG, DJILog.exceptionToString(e), new Object[0]);
                    }
                }
            }
        }, "gimbalAbs1").start();
    }

    /* access modifiers changed from: protected */
    public void getParam(final SettingParamCmd cmd, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (cmd != null) {
            ((DataGimbalGetUserParams) new DataGimbalGetUserParams().setInfos(new String[]{cmd.getCmdString()}).setReceiverId(getReceiverIdByIndex(), DataGimbalGetUserParams.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DJIGimbalParamInfoManager.read(cmd.getCmdString()).value.intValue()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void setIsTestingBalance(boolean isTestingBalance2) {
        this.isTestingBalance = isTestingBalance2;
    }

    /* access modifiers changed from: protected */
    public void setPitchTestResult(BalanceTestResult pitchTestResult) {
        this.pitchResult = pitchTestResult;
    }

    /* access modifiers changed from: protected */
    public void setRollTestResult(BalanceTestResult rollTestResult) {
        this.rollResult = rollTestResult;
    }

    public Attitude getAttitudeInDegrees() {
        return this.attitude;
    }

    /* access modifiers changed from: package-private */
    public void setAttitude(Attitude attitude2) {
        this.attitude = attitude2;
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dataCommonGetVersion = new DataCommonGetVersion();
        dataCommonGetVersion.setDeviceType(DeviceType.GIMBAL);
        dataCommonGetVersion.setDeviceModel(getReceiverIdByIndex());
        dataCommonGetVersion.startForce(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                String firmVersion = dataCommonGetVersion.getFirmVer(".");
                if (callback == null) {
                    return;
                }
                if (!TextUtils.isEmpty(firmVersion)) {
                    callback.onSuccess(firmVersion);
                } else {
                    callback.onFails(DJIError.UNABLE_TO_GET_FIRMWARE_VERSION);
                }
            }

            public void onFailure(Ccode ccode) {
                DJIError error = DJIError.COMMON_UNKNOWN;
                error.setDescription(ccode.toString());
                if (callback != null) {
                    callback.onFails(error);
                }
            }
        });
    }

    @Setter("Mode")
    public void setGimbalMode(GimbalMode gimbalMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if ((gimbalMode == null || gimbalMode.equals(GimbalMode.UNKNOWN)) && callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataGimbalControl.MODE mode = DataGimbalControl.MODE.find(gimbalMode.value());
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        if (platformType.equals(DJIComponentManager.PlatformType.M200) || platformType.equals(DJIComponentManager.PlatformType.M210) || platformType.equals(DJIComponentManager.PlatformType.M210RTK) || platformType.equals(DJIComponentManager.PlatformType.PM420) || platformType.equals(DJIComponentManager.PlatformType.PM420PRO) || platformType.equals(DJIComponentManager.PlatformType.PM420PRO_RTK)) {
            ((DataGimbalNewResetAndSetMode) new DataGimbalNewResetAndSetMode().setGimbalMode(mode).setReceiverId(getReceiverIdByIndex(), DataGimbalNewResetAndSetMode.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIError.getDJIError(ccode));
                    }
                }
            });
            return;
        }
        ((DataSpecialControl) DataSpecialControl.getInstance().setGimbalMode(mode).setReceiverId(getReceiverIdByIndex(), DataSpecialControl.class)).start(20);
        callback.onSuccess(null);
    }

    @Action(GimbalKeys.ROTATE)
    public void rotate(final DJISDKCacheHWAbstraction.InnerCallback callback, Rotation rotation) {
        if (rotation == null) {
            CallbackUtils.onFailure(callback, DJIGimbalError.COMMON_PARAM_ILLEGAL);
        } else if (rotation.getMode() == null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        } else if ((rotation.getPitch() == Float.MAX_VALUE || this.gimbalCapability.get(CapabilityKey.ADJUST_PITCH).isSupported()) && ((rotation.getRoll() == Float.MAX_VALUE || this.gimbalCapability.get(CapabilityKey.ADJUST_ROLL).isSupported()) && (rotation.getYaw() == Float.MAX_VALUE || this.gimbalCapability.get(CapabilityKey.ADJUST_YAW).isSupported()))) {
            DataGimbalSpeedControl.getInstance().setIgnoreGimbalStick(rotation.getIgnore());
            if (rotation.getMode() == RotationMode.ABSOLUTE_ANGLE || rotation.getMode() == RotationMode.RELATIVE_ANGLE) {
                justSendPauseData();
                double time = rotation.getTime();
                if (time > 25.5d) {
                    time = 25.5d;
                }
                if (time < 0.1d) {
                    time = 0.1d;
                }
                DataGimbalAbsAngleControl absAngleControl = DataGimbalAbsAngleControl.getInstance().setControlMode(true);
                absAngleControl.setPitch(0);
                absAngleControl.setRoll(0);
                absAngleControl.setYaw(0);
                if (rotation.getPitch() == Float.MAX_VALUE) {
                    absAngleControl.setPitchInvalid(true);
                } else if (rotation.getMode() == RotationMode.ABSOLUTE_ANGLE) {
                    float angle = rotation.getPitch();
                    if (angle >= ((float) getMinValueFromCapability(CapabilityKey.ADJUST_PITCH).intValue()) && angle <= ((float) getMaxValueFromCapability(CapabilityKey.ADJUST_PITCH).intValue())) {
                        absAngleControl.setPitch((short) ((int) (10.0f * angle)));
                        absAngleControl.setPitchInvalid(false);
                        absAngleControl.setControlMode(true);
                    } else if (callback != null) {
                        callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
                        return;
                    } else {
                        return;
                    }
                } else if (rotation.getMode() == RotationMode.RELATIVE_ANGLE) {
                    absAngleControl.setPitch((short) ((int) (10.0f * rotation.getPitch())));
                    absAngleControl.setPitchInvalid(false);
                    absAngleControl.setControlMode(false);
                }
                if (rotation.getRoll() == Float.MAX_VALUE) {
                    absAngleControl.setRollInvalid(true);
                } else if (rotation.getMode() == RotationMode.ABSOLUTE_ANGLE) {
                    float angle2 = rotation.getRoll();
                    if (angle2 >= ((float) getMinValueFromCapability(CapabilityKey.ADJUST_ROLL).intValue()) && angle2 <= ((float) getMaxValueFromCapability(CapabilityKey.ADJUST_ROLL).intValue())) {
                        absAngleControl.setRoll((short) ((int) (10.0f * angle2)));
                        absAngleControl.setRollInvalid(false);
                        absAngleControl.setControlMode(true);
                    } else if (callback != null) {
                        callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
                        return;
                    } else {
                        return;
                    }
                } else if (rotation.getMode() == RotationMode.RELATIVE_ANGLE) {
                    absAngleControl.setRoll((short) ((int) (10.0f * rotation.getRoll())));
                    absAngleControl.setRollInvalid(false);
                    absAngleControl.setControlMode(false);
                }
                if (rotation.getYaw() == Float.MAX_VALUE) {
                    absAngleControl.setYawInvalid(true);
                } else if (rotation.getMode() == RotationMode.ABSOLUTE_ANGLE) {
                    float angle3 = rotation.getYaw();
                    if (angle3 >= ((float) getMinValueFromCapability(CapabilityKey.ADJUST_YAW).intValue()) && angle3 <= ((float) getMaxValueFromCapability(CapabilityKey.ADJUST_YAW).intValue())) {
                        absAngleControl.setYaw((short) ((int) (10.0f * angle3)));
                        absAngleControl.setYawInvalid(false);
                        absAngleControl.setControlMode(true);
                    } else if (callback != null) {
                        callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
                        return;
                    } else {
                        return;
                    }
                } else if (rotation.getMode() == RotationMode.RELATIVE_ANGLE) {
                    absAngleControl.setYaw((short) ((int) (10.0f * rotation.getYaw())));
                    absAngleControl.setYawInvalid(false);
                    absAngleControl.setControlMode(false);
                }
                absAngleControl.setOvertime((int) (10.0d * time));
                absAngleControl.setReceiverId(getReceiverIdByIndex());
                absAngleControl.start();
                CallbackUtils.onSuccess(callback, (Object) null);
            } else if (rotation.getMode() == RotationMode.SPEED) {
                justSendPauseData();
                if (rotation.getPitch() != Float.MAX_VALUE) {
                    DataGimbalSpeedControl.getInstance().setPitch((int) (rotation.getPitch() * 10.0f));
                } else {
                    DataGimbalSpeedControl.getInstance().setPitch(0);
                }
                if (rotation.getRoll() != Float.MAX_VALUE) {
                    DataGimbalSpeedControl.getInstance().setRoll((int) (rotation.getRoll() * 10.0f));
                } else {
                    DataGimbalSpeedControl.getInstance().setRoll(0);
                }
                if (rotation.getYaw() != Float.MAX_VALUE) {
                    DataGimbalSpeedControl.getInstance().setYaw((int) (rotation.getYaw() * 10.0f));
                } else {
                    DataGimbalSpeedControl.getInstance().setYaw(0);
                }
                DataGimbalSpeedControl.getInstance().setPermission((rotation.getPitch() == 0.0f && rotation.getRoll() == 0.0f && rotation.getYaw() == 0.0f) ? false : true);
                DataGimbalSpeedControl.getInstance().setReceiverId(getReceiverIdByIndex());
                if ((!DataOnboardGetPushMixInfo.getInstance().isGetted() || !DataOnboardGetPushMixInfo.getInstance().isSimultaneousControlGimbal()) && (!DataOnboardGetPushDualGimbals.getInstance().isGetted() || !DataOnboardGetPushDualGimbals.getInstance().isGimbalBehaviorBoth())) {
                    DataGimbalSpeedControl.getInstance().setMultiControl(false);
                } else {
                    if (DataOnboardGetPushMixInfo.getInstance().isGetted() && DataOnboardGetPushMixInfo.getInstance().isSimultaneousControlGimbal()) {
                        DataGimbalSpeedControl.getInstance().setReceiverId(0);
                    }
                    DataGimbalSpeedControl.getInstance().setMultiControl(true);
                }
                DataGimbalSpeedControl.getInstance().start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass6 */

                    public void onSuccess(Object model) {
                        CallbackUtils.onSuccess(callback, model);
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, ccode);
                    }
                });
            }
        } else {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Action(GimbalKeys.RESET_GIMBAL)
    public void resetGimbal(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (tryToPauseTheStabilizationIfNecessary(callback)) {
            DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
            if (platformType.equals(DJIComponentManager.PlatformType.M200) || platformType.equals(DJIComponentManager.PlatformType.M210) || platformType.equals(DJIComponentManager.PlatformType.M210RTK) || platformType.equals(DJIComponentManager.PlatformType.PM420) || platformType.equals(DJIComponentManager.PlatformType.PM420PRO) || platformType.equals(DJIComponentManager.PlatformType.PM420PRO_RTK)) {
                ((DataGimbalNewResetAndSetMode) new DataGimbalNewResetAndSetMode().setReset(true).setReceiverId(getReceiverIdByIndex(), DataGimbalNewResetAndSetMode.class)).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass7 */

                    public void onSuccess(Object model) {
                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (callback != null) {
                            callback.onFails(DJIError.getDJIError(ccode));
                        }
                    }
                });
                return;
            }
            ((DataSpecialControl) DataSpecialControl.getInstance().resetGimbal().setReceiverId(getReceiverIdByIndex(), DataSpecialControl.class)).start(20);
            callback.onSuccess(null);
        }
    }

    @Action(GimbalKeys.START_CALIBRATION)
    public void startCalibration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalAutoCalibration) DataGimbalAutoCalibration.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalAutoCalibration.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback == null) {
                    return;
                }
                if (DJIGimbalAbstraction.this.isCalibrating) {
                    callback.onSuccess(null);
                } else {
                    callback.onFails(DJIGimbalError.getDJIError(ccode));
                }
            }
        });
    }

    @Action(GimbalKeys.FINE_TUNE_ROLL_IN_DEGREES)
    public void fineTuneGimbalRollInDegrees(DJISDKCacheHWAbstraction.InnerCallback callback, float offset) {
        if (offset > 2.0f || offset < -2.0f) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (this.needPauseStabilization) {
            CallbackUtils.onFailure(callback, DJIError.CANNOT_PAUSE_STABILIZATION);
        } else {
            int fineTune = (int) (10.0f * offset);
            if (DJIProductManager.getInstance().getType().equals(ProductType.PM820)) {
            }
            ((DataGimbalRollFinetune) DataGimbalRollFinetune.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalRollFinetune.class)).setFineTuneValue((byte) fineTune).setFineTuneAxis(DataGimbalRollFinetune.FineTuneAxis.ROLL).start(CallbackUtils.defaultCB(callback));
        }
    }

    @Action(GimbalKeys.FINE_TUNE_PITCH_IN_DEGREES)
    public void fineTuneGimbalPitchInDegrees(DJISDKCacheHWAbstraction.InnerCallback callback, float offset) {
        if (offset > 2.0f || offset < -2.0f) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        ((DataGimbalRollFinetune) DataGimbalRollFinetune.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalRollFinetune.class)).setFineTuneValue((byte) ((int) (10.0f * offset))).setFineTuneAxis(DataGimbalRollFinetune.FineTuneAxis.PITCH).start(CallbackUtils.defaultCB(callback));
    }

    @Action(GimbalKeys.FINE_TUNE_YAW_IN_DEGREES)
    public void fineTuneGimbalYawInDegrees(DJISDKCacheHWAbstraction.InnerCallback callback, float offset) {
        if (offset > 2.0f || offset < -2.0f) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        ((DataGimbalRollFinetune) DataGimbalRollFinetune.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalRollFinetune.class)).setFineTuneValue((byte) ((int) (10.0f * offset))).setFineTuneAxis(DataGimbalRollFinetune.FineTuneAxis.YAW).start(CallbackUtils.defaultCB(callback));
    }

    @Setter(GimbalKeys.PITCH_RANGE_EXTENSION_ENABLED)
    public void setPitchRangeExtensionEnabled(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        DataGimbalSetUserParams dataGimbalSetUserParams = (DataGimbalSetUserParams) DataGimbalSetUserParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalSetUserParams.class);
        String cmdString = SettingParamCmd.PITCH_EXP.getCmdString();
        if (enable) {
            i = 1;
        } else {
            i = 0;
        }
        dataGimbalSetUserParams.setInfo(cmdString, Integer.valueOf(i)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass9 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
        new Thread(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass10 */

            public void run() {
                int i = 0;
                while (i < 15) {
                    try {
                        Thread.sleep(200);
                        if (enable == (DJIGimbalParamInfoManager.read(SettingParamCmd.PITCH_EXP.getCmdString()).value.intValue() == 1)) {
                            callback.onSuccess(Boolean.valueOf(enable));
                            return;
                        } else if (i == 14) {
                            callback.onFails(DJIError.COMMON_TIMEOUT);
                            return;
                        } else {
                            i++;
                        }
                    } catch (InterruptedException e) {
                        DJILog.e(DJIGimbalAbstraction.TAG, DJILog.exceptionToString(e), new Object[0]);
                    }
                }
            }
        }, "gimbalAbs").start();
    }

    @Getter(GimbalKeys.PITCH_RANGE_EXTENSION_ENABLED)
    public void getPitchRangeExtensionEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalGetUserParams) new DataGimbalGetUserParams().setInfos(new String[]{SettingParamCmd.PITCH_EXP.getCmdString()}).setReceiverId(getReceiverIdByIndex(), DataGimbalGetUserParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass11 */

            public void onSuccess(Object model) {
                boolean z = true;
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                if (DJIGimbalParamInfoManager.read(SettingParamCmd.PITCH_EXP.getCmdString()).value.intValue() != 1) {
                    z = false;
                }
                innerCallback.onSuccess(Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }

    @Action(GimbalKeys.RESTORE_FACTORY_SETTINGS)
    public void loadFactorySettings(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalResetUserParams) DataGimbalResetUserParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalResetUserParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction.AnonymousClass12 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_ENABLED)
    public void setSmoothTrackEnabledOnPitch(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_ENABLED)
    public void setSmoothTrackEnabledOnYaw(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_ENABLED)
    public void getSmoothTrackEnabledOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_ENABLED)
    public void getSmoothTrackEnabledOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.MOVEMENT_SETTINGS_PROFILE)
    public void setMovementSettingsProfile(MovementSettingsProfile userConfigType, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.MOVEMENT_SETTINGS_PROFILE)
    public void getMovementSettingsProfile(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_SPEED)
    public void setSmoothTrackSpeedOnPitch(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_SPEED)
    public void setSmoothTrackSpeedOnYaw(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_SPEED)
    public void getSmoothTrackSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_SPEED)
    public void getSmoothTrackSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_DEADBAND)
    public void setSmoothTrackDeadbandOnPitch(int deadband, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_DEADBAND)
    public void setSmoothTrackDeadbandOnYaw(int deadband, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_DEADBAND)
    public void getSmoothTrackDeadbandOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_DEADBAND)
    public void getSmoothTrackDeadbandOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_ACCELERATION)
    public void setSmoothTrackAccelerationOnPitch(int acceleration, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_ACCELERATION)
    public void setSmoothTrackAccelerationOnYaw(int acceleration, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_ACCELERATION)
    public void getSmoothTrackAccelerationOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_ACCELERATION)
    public void getSmoothTrackAccelerationOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_SMOOTHING_FACTOR)
    public void setControllerSmoothingOnPitch(int smoothing, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_SMOOTHING_FACTOR)
    public void setControllerSmoothingOnYaw(int smoothing, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_SMOOTHING_FACTOR)
    public void getControllerSmoothingOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_SMOOTHING_FACTOR)
    public void getControllerSmoothingOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_SPEED_COEFFICIENT)
    public void setControllerSpeedOnPitch(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_SPEED_COEFFICIENT)
    public void setControllerSpeedOnYaw(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_MAX_SPEED)
    public void setControllerMaxSpeedOnPitch(int maxSpeed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_MAX_SPEED)
    public void setControllerMaxSpeedOnYaw(int maxSpeed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_SPEED_COEFFICIENT)
    public void getControllerSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_SPEED_COEFFICIENT)
    public void getControllerSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_MAX_SPEED)
    public void getControllerMaxSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_MAX_SPEED)
    public void getControllerMaxSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Action(GimbalKeys.APPLY_MOTOR_CONTROL_PRESET)
    public void configureMotorControl(DJISDKCacheHWAbstraction.InnerCallback callback, MotorControlPreset cameraPreset) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.MOTOR_ENABLED)
    public void setMotorEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.MOTOR_ENABLED)
    public void getMotorEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Action(GimbalKeys.START_BALANCE_TEST)
    public void startGimbalBalanceTest(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Action(GimbalKeys.TOGGLE_SELFIE)
    public void toggleGimbalSelfie(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Setter(GimbalKeys.CONTROLLER_MODE)
    public void setGimbalControllerMode(ControllerMode stickControlMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Getter(GimbalKeys.CONTROLLER_MODE)
    public void getGimbalControllerMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onFails(DJIGimbalError.COMMON_UNSUPPORTED);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushStabilizationState state) {
        if (!state.getStateIsTurnOn()) {
            this.needPauseStabilization = false;
            for (CountDownLatch latch : this.waitList) {
                latch.countDown();
            }
        } else if (!state.getStateIsPaused()) {
            this.needPauseStabilization = true;
        } else {
            this.needPauseStabilization = false;
            for (CountDownLatch latch2 : this.waitList) {
                latch2.countDown();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushUserParams param) {
        if (param.getSenderId() == getExpectedSenderIdByIndex()) {
            this.pushUserParams = param;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushParams params) {
        if (params != null) {
            int expectedSender = getExpectedSenderIdByIndex();
            if (params.isFpvGimbal()) {
                notifyValueChangeForKeyPath(getAttitude(params, DataGimbalGetPushParams.GimbalRole.FPV_GIMBAL.value()), convertKeyToPath(GimbalKeys.FPV_ATTITUDE_IN_DEGREES));
                notifyValueChangeForKeyPath(Float.valueOf(((float) params.getYawAngle(DataGimbalGetPushParams.GimbalRole.FPV_GIMBAL.value())) * 0.1f), convertKeyToPath(GimbalKeys.FPV_YAW_ANGLE_WITH_AIRCRAFT_IN_DEGREE));
            } else if (params.getSenderId() == expectedSender) {
                this.attitude = getAttitude(params, expectedSender);
                notifyValueChangeForKeyPath(this.attitude, convertKeyToPath(GimbalKeys.ATTITUDE_IN_DEGREES));
                notifyValueChangeForKeyPath(Float.valueOf(((float) params.getRollAdjust(expectedSender)) / 10.0f), convertKeyToPath(GimbalKeys.ROLL_FINE_TUNE_IN_DEGREES));
                notifyValueChangeForKeyPath(Float.valueOf(((float) params.getPitchAdjust(expectedSender)) / 10.0f), convertKeyToPath(GimbalKeys.PITCH_FINE_TUNE_IN_DEGREES));
                notifyValueChangeForKeyPath(Float.valueOf(((float) params.getYawAdjustDegree(expectedSender)) / 10.0f), convertKeyToPath(GimbalKeys.YAW_FINE_TUNE_IN_DEGREES));
                GimbalMode gimbalMode = GimbalMode.UNKNOWN;
                DataGimbalControl.MODE midWareMode = params.getMode(expectedSender);
                if (midWareMode == DataGimbalControl.MODE.YawNoFollow) {
                    gimbalMode = GimbalMode.FREE;
                } else if (midWareMode == DataGimbalControl.MODE.FPV) {
                    gimbalMode = GimbalMode.FPV;
                } else if (midWareMode == DataGimbalControl.MODE.YawFollow) {
                    gimbalMode = GimbalMode.YAW_FOLLOW;
                }
                notifyValueChangeForKeyPath(gimbalMode, convertKeyToPath("Mode"));
                boolean isPitchAtStop = params.isPitchInLimit(expectedSender);
                boolean isRollAtStop = params.isRollInLimit(expectedSender);
                boolean isYawAtStop = params.isYawInLimit(expectedSender);
                boolean isGimbalOnTop = params.isTopPosition(expectedSender);
                notifyValueChangeForKeyPath(Boolean.valueOf(isPitchAtStop), convertKeyToPath(GimbalKeys.IS_PITCH_AT_STOP));
                notifyValueChangeForKeyPath(Boolean.valueOf(isRollAtStop), convertKeyToPath(GimbalKeys.IS_ROLL_AT_STOP));
                notifyValueChangeForKeyPath(Boolean.valueOf(isYawAtStop), convertKeyToPath(GimbalKeys.IS_YAW_AT_STOP));
                notifyValueChangeForKeyPath(Boolean.valueOf(isGimbalOnTop), convertKeyToPath(GimbalKeys.IS_GIMBAL_ON_TOP));
                updateCalibrationState(params);
                notifyValueChangeForKeyPath(Float.valueOf(((float) params.getYawAngle(expectedSender)) / 10.0f), convertKeyToPath(GimbalKeys.YAW_ANGLE_WITH_AIRCRAFT_IN_DEGREE));
                notifyValueChangeForKeyPath(Boolean.valueOf(params.isStuck(expectedSender)), convertKeyToPath(GimbalKeys.IS_STUCK));
                notifyValueChangeForKeyPath(Integer.valueOf(params.getSubMode(expectedSender)), convertKeyToPath(GimbalKeys.FPV_SUBMODE));
            }
        }
    }

    private Attitude getAttitude(DataGimbalGetPushParams params, int expectedSender) {
        return new Attitude((float) (((double) params.getPitch(expectedSender)) * 0.1d), (float) (((double) params.getRoll(expectedSender)) * 0.1d), (float) (((double) params.getYaw(expectedSender)) * 0.1d));
    }

    /* access modifiers changed from: protected */
    public void updateCalibrationState(DataGimbalGetPushParams params) {
        int expectedSender = getExpectedSenderIdByIndex();
        if (!(DJIComponentManager.getInstance().getGimbalComponentType() == DJIComponentManager.GimbalComponentType.Ronin || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.OSMO || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.OSMOMobile || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Inspire2 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.P4 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.FoldingDrone || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.M200 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.M210 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.M210RTK || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.PM420 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.PM420PRO || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.PM420PRO_RTK || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.WM230 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.WM240 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.WM245 || DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.WM160)) {
            this.isCalibrating = params.isAutoCalibration(expectedSender);
            this.isCalibrationSuccessful = !this.isCalibrating;
            if (this.isCalibrating) {
                this.calibrationProgress = DataGimbalGetPushAutoCalibrationStatus.getInstance().getProgress(expectedSender);
            }
        }
        if (params.getSenderId() == expectedSender) {
            notifyValueChangeForKeyPath(Float.valueOf(((float) params.getYawAngle(expectedSender)) / 10.0f), convertKeyToPath(GimbalKeys.YAW_ANGLE_WITH_AIRCRAFT_IN_DEGREE));
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isStuck(expectedSender)), convertKeyToPath(GimbalKeys.IS_STUCK));
        notifyValueChangeForKeyPath(Integer.valueOf(params.getSubMode(expectedSender)), convertKeyToPath(GimbalKeys.FPV_SUBMODE));
        notifyValueChangeForKeyPath(Boolean.valueOf(this.isCalibrating), convertKeyToPath(GimbalKeys.IS_CALIBRATING));
        notifyValueChangeForKeyPath(Boolean.valueOf(this.isCalibrationSuccessful), convertKeyToPath(GimbalKeys.IS_CALIBRATION_SUCCESSFUL));
        notifyValueChangeForKeyPath(Integer.valueOf(this.calibrationProgress), convertKeyToPath(GimbalKeys.CALIBRATION_PROGRESS));
    }

    /* access modifiers changed from: protected */
    public boolean tryToPauseTheStabilizationIfNecessary(DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean timeout = false;
        if (this.needPauseStabilization) {
            CountDownLatch latch = new CountDownLatch(1);
            this.waitList.add(latch);
            try {
                new DataSingleSendAppStateForStabilization().setGimbalState(DataSingleSendAppStateForStabilization.GimbalState.START).start();
                if (!latch.await(3, TimeUnit.SECONDS)) {
                    timeout = true;
                }
            } catch (InterruptedException e) {
                DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
            }
            if (latch != null) {
                this.waitList.remove(latch);
            }
            if (timeout && callback != null) {
                callback.onFails(DJIError.CANNOT_PAUSE_STABILIZATION);
            }
        } else {
            new DataSingleSendAppStateForStabilization().setGimbalState(DataSingleSendAppStateForStabilization.GimbalState.START).start();
        }
        if (!timeout) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void justSendPauseData() {
        new DataSingleSendAppStateForStabilization().setGimbalState(DataSingleSendAppStateForStabilization.GimbalState.START).start();
    }

    /* access modifiers changed from: protected */
    public boolean isCustomAdvancedSettingProfile() {
        return MovementSettingsProfile.find(DataGimbalGetPushUserParams.getInstance().getPresetID(getExpectedSenderIdByIndex())) == MovementSettingsProfile.CUSTOM_1 || MovementSettingsProfile.find(DataGimbalGetPushUserParams.getInstance().getPresetID(getExpectedSenderIdByIndex())) == MovementSettingsProfile.CUSTOM_2;
    }

    /* access modifiers changed from: protected */
    public int getExpectedSenderIdByIndex() {
        return DoubleCameraSupportUtil.getGimbalIdInProtocol(this.index);
    }

    /* access modifiers changed from: protected */
    public int getReceiverIdByIndex() {
        return DoubleCameraSupportUtil.getGimbalIdInProtocol(this.index);
    }
}
