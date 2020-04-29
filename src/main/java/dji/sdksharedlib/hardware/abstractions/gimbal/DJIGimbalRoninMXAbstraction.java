package dji.sdksharedlib.hardware.abstractions.gimbal;

import com.mapzen.android.lost.internal.FusionEngine;
import dji.common.error.DJIGimbalError;
import dji.common.gimbal.Axis;
import dji.common.gimbal.BalanceTestResult;
import dji.common.gimbal.CapabilityKey;
import dji.common.gimbal.EndpointDirection;
import dji.common.gimbal.MotorControlPreset;
import dji.common.util.LatchHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.gimbal.CalibrationState;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIGimbalParamInfoManager;
import dji.midware.data.model.P3.DataGimbalGetUserParams;
import dji.midware.data.model.P3.DataGimbalRoninGetUserParams;
import dji.midware.data.model.P3.DataGimbalRoninSetUserParams;
import dji.midware.data.model.P3.DataGimbalSetUserParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIGimbalRoninMXAbstraction extends DJIGimbalHandheldAbstraction {
    /* access modifiers changed from: private */
    public static final String[] RONIN_COMMAND = {"ronin_sensibility_yaw", "ronin_sensibility_pitch", "ronin_sensibility_roll", "ronin_strength_yaw", "ronin_strength_pitch", "ronin_strength_roll", "ronin_filter_yaw", "ronin_filter_pitch", "ronin_filter_roll", "ronin_feedback_yaw", "ronin_feedback_pitch", "ronin_feedback_roll", "ronin_pitch_up", "ronin_pitch_down", "ronin_yaw_left", "ronin_yaw_right", "pitch_dead_zone", "yaw_dead_zone", "pitch_expo", "yaw_expo", "pitch_time_expo", "yaw_time_expo", "system_calc", "balance_test"};
    private final int[][] RONIN_CAMERA_DEFAULT_PARAMS = {new int[]{73, 75, 70, 40, 40, 25, 0, 0, 0, 20, 60, 60}, new int[]{55, 45, 45, 40, 40, 40, 0, 0, 0, 20, 60, 60}, new int[]{50, 45, 45, 20, 40, 40, 0, 0, 0, 0, 60, 60}};
    /* access modifiers changed from: private */
    public LatchHelper latch = LatchHelper.getInstance();
    boolean setSuccessful = false;

    public void init(String parentPath, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(parentPath, index, storeLayer, onValueChangeListener);
        initGimbalCapability();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -135, 45);
        addMinMaxToCapability(CapabilityKey.ADJUST_YAW, -179, 179);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_DEADBAND, 0, 90);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_DEADBAND, 0, 90);
        addMinMaxToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_SPEED, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_SMOOTH_TRACK_SPEED, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_DEADBAND, 0, 90);
        addMinMaxToCapability(CapabilityKey.YAW_SMOOTH_TRACK_DEADBAND, 0, 90);
        addMinMaxToCapability(CapabilityKey.PITCH_UP_ENDPOINT, 0, 45);
        addMinMaxToCapability(CapabilityKey.PITCH_DOWN_ENDPOINT, 0, 135);
        addMinMaxToCapability(CapabilityKey.YAW_LEFT_ENDPOINT, 0, 179);
        addMinMaxToCapability(CapabilityKey.YAW_RIGHT_ENDPOINT, 0, 179);
        addMinMaxToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_STIFFNESS, 0, 100);
        addMinMaxToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_STIFFNESS, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_MOTOR_CONTROL_STIFFNESS, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_STRENGTH, 0, 100);
        addMinMaxToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_STRENGTH, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_MOTOR_CONTROL_STRENGTH, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_GYRO_FILTERING_FACTOR, 0, 99);
        addMinMaxToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_GYRO_FILTERING_FACTOR, 0, 99);
        addMinMaxToCapability(CapabilityKey.YAW_MOTOR_CONTROL_GYRO_FILTERING_FACTOR, 0, 99);
        addMinMaxToCapability(CapabilityKey.PITCH_MOTOR_CONTROL_PRE_CONTROL, 0, 100);
        addMinMaxToCapability(CapabilityKey.ROLL_MOTOR_CONTROL_PRE_CONTROL, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_MOTOR_CONTROL_PRE_CONTROL, 0, 100);
    }

    /* access modifiers changed from: protected */
    public void setRoninMotorParam(int command, int val, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (command >= 0 && command < RONIN_COMMAND.length) {
            switch (command) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    if (!checkValueValid(Integer.valueOf(val), 1, 100, callback)) {
                        return;
                    }
                    break;
                case 12:
                    if (!checkValueValid(Integer.valueOf(val), 0, 45, callback)) {
                        return;
                    }
                    break;
                case 13:
                    if (!checkValueValid(Integer.valueOf(val), 0, 135, callback)) {
                        return;
                    }
                    break;
                case 14:
                    if (!checkValueValid(Integer.valueOf(val), 0, 179, callback)) {
                        return;
                    }
                    break;
                case 15:
                    if (!checkValueValid(Integer.valueOf(val), 0, 179, callback)) {
                        return;
                    }
                    break;
                case 16:
                    if (!checkValueValid(Integer.valueOf(val), 0, 90, callback)) {
                        return;
                    }
                    break;
                case 17:
                    if (!checkValueValid(Integer.valueOf(val), 0, 90, callback)) {
                        return;
                    }
                    break;
                case 18:
                    if (!checkValueValid(Integer.valueOf(val), 0, 100, callback)) {
                        return;
                    }
                    break;
                case 19:
                    if (!checkValueValid(Integer.valueOf(val), 0, 100, callback)) {
                        return;
                    }
                    break;
                case 20:
                    if (!checkValueValid(Integer.valueOf(val), 0, 30, callback)) {
                        return;
                    }
                    break;
                case 21:
                    if (!checkValueValid(Integer.valueOf(val), 0, 30, callback)) {
                        return;
                    }
                    break;
            }
            DataGimbalSetUserParams.getInstance().setTimeOut(3000);
            DataGimbalRoninSetUserParams.getInstance().setInfo(RONIN_COMMAND[command], Integer.valueOf(val)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    callback.onSuccess(model);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIGimbalError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(GimbalKeys.APPLY_MOTOR_CONTROL_PRESET)
    public void configureMotorControl(DJISDKCacheHWAbstraction.InnerCallback callback, MotorControlPreset cameraPreset) {
        this.latch.setUpLatch(12);
        this.setSuccessful = true;
        for (int i = 0; i < 12; i++) {
            DataGimbalRoninSetUserParams.getInstance().setInfo(RONIN_COMMAND[i], Integer.valueOf(this.RONIN_CAMERA_DEFAULT_PARAMS[cameraPreset.ordinal()][i])).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DJIGimbalRoninMXAbstraction.this.latch.countDownLatch();
                }

                public void onFailure(Ccode ccode) {
                    DJIGimbalRoninMXAbstraction.this.setSuccessful = false;
                    DJIGimbalRoninMXAbstraction.this.latch.countDownLatch();
                }
            });
        }
        this.latch.waitForLatch(60);
        if (this.setSuccessful) {
            callback.onSuccess(cameraPreset);
        } else {
            callback.onFails(DJIGimbalError.RESULT_FAILED);
        }
    }

    @Setter(GimbalKeys.PITCH_MOTOR_CONTROL_STIFFNESS)
    public void setMotorControlStiffnessOnPitch(int stiffness, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(stiffness, Axis.PITCH.ordinal(), callback);
    }

    @Setter(GimbalKeys.ROLL_MOTOR_CONTROL_STIFFNESS)
    public void setMotorControlStiffnessOnRoll(int stiffness, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(stiffness, Axis.ROLL.ordinal(), callback);
    }

    @Setter(GimbalKeys.YAW_MOTOR_CONTROL_STIFFNESS)
    public void setMotorControlStiffnessOnYaw(int stiffness, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(stiffness, Axis.YAW.ordinal(), callback);
    }

    private void getRoninMotorParam(final int direction, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataGimbalRoninGetUserParams.getInstance().setInfos(new String[]{RONIN_COMMAND[direction]}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIGimbalParamInfoManager.getInstance();
                callback.onSuccess(Integer.valueOf(DJIGimbalParamInfoManager.read(DJIGimbalRoninMXAbstraction.RONIN_COMMAND[direction]).value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIGimbalError.getDJIError(ccode));
            }
        });
    }

    @Getter(GimbalKeys.PITCH_MOTOR_CONTROL_STIFFNESS)
    public void getMotorControlStiffnessOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal(), callback);
    }

    @Getter(GimbalKeys.ROLL_MOTOR_CONTROL_STIFFNESS)
    public void getMotorControlStiffnessOnRoll(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.ROLL.ordinal(), callback);
    }

    @Getter(GimbalKeys.YAW_MOTOR_CONTROL_STIFFNESS)
    public void getMotorControlStiffnessOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal(), callback);
    }

    @Setter(GimbalKeys.PITCH_MOTOR_CONTROL_STRENGTH)
    public void setMotorControlStrengthOnPitch(int strength, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(strength, Axis.PITCH.ordinal() + 3, callback);
    }

    @Setter(GimbalKeys.ROLL_MOTOR_CONTROL_STRENGTH)
    public void setMotorControlStrengthOnRoll(int strength, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(strength, Axis.ROLL.ordinal() + 3, callback);
    }

    @Setter(GimbalKeys.YAW_MOTOR_CONTROL_STRENGTH)
    public void setMotorControlStrengthOnYaw(int strength, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(strength, Axis.YAW.ordinal() + 3, callback);
    }

    @Getter(GimbalKeys.PITCH_MOTOR_CONTROL_STRENGTH)
    public void getMotorControlStrengthOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal() + 3, callback);
    }

    @Getter(GimbalKeys.ROLL_MOTOR_CONTROL_STRENGTH)
    public void getMotorControlStrengthOnRoll(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.ROLL.ordinal() + 3, callback);
    }

    @Getter(GimbalKeys.YAW_MOTOR_CONTROL_STRENGTH)
    public void getMotorControlStrengthOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal() + 3, callback);
    }

    @Setter(GimbalKeys.PITCH_MOTOR_CONTROL_GYRO_FILTERING_FACTOR)
    public void setMotorControlGyroFilteringOnPitch(int filtering, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(filtering, Axis.PITCH.ordinal() + 6, callback);
    }

    @Setter(GimbalKeys.ROLL_MOTOR_CONTROL_GYRO_FILTERING_FACTOR)
    public void setMotorControlGyroFilteringOnRoll(int filtering, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(filtering, Axis.ROLL.ordinal() + 6, callback);
    }

    @Setter(GimbalKeys.YAW_MOTOR_CONTROL_GYRO_FILTERING_FACTOR)
    public void setMotorControlGyroFilteringOnYaw(int filtering, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(filtering, Axis.YAW.ordinal() + 6, callback);
    }

    @Getter(GimbalKeys.PITCH_MOTOR_CONTROL_GYRO_FILTERING_FACTOR)
    public void getMotorControlGyroFilteringOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal() + 6, callback);
    }

    @Getter(GimbalKeys.ROLL_MOTOR_CONTROL_GYRO_FILTERING_FACTOR)
    public void getMotorControlGyroFilteringOnRoll(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.ROLL.ordinal() + 6, callback);
    }

    @Getter(GimbalKeys.YAW_MOTOR_CONTROL_GYRO_FILTERING_FACTOR)
    public void getMotorControlGyroFilteringOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal() + 6, callback);
    }

    @Setter(GimbalKeys.PITCH_MOTOR_CONTROL_PRE_CONTROL)
    public void setMotorControlPreControlOnPitch(int precontrol, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.PITCH.ordinal() + 9, precontrol, callback);
    }

    @Setter(GimbalKeys.ROLL_MOTOR_CONTROL_PRE_CONTROL)
    public void setMotorControlPreControlOnRoll(int precontrol, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.ROLL.ordinal() + 9, precontrol, callback);
    }

    @Setter(GimbalKeys.YAW_MOTOR_CONTROL_PRE_CONTROL)
    public void setMotorControlPreControlOnYaw(int precontrol, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.YAW.ordinal() + 9, precontrol, callback);
    }

    @Getter(GimbalKeys.PITCH_MOTOR_CONTROL_PRE_CONTROL)
    public void getMotorControlPreControlOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal() + 9, callback);
    }

    @Getter(GimbalKeys.ROLL_MOTOR_CONTROL_PRE_CONTROL)
    public void getMotorControlPreControlOnRoll(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.ROLL.ordinal() + 9, callback);
    }

    @Getter(GimbalKeys.YAW_MOTOR_CONTROL_PRE_CONTROL)
    public void getMotorControlPreControlOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal() + 9, callback);
    }

    @Setter(GimbalKeys.PITCH_UP_ENDPOINT)
    public void setEndpointInPitchUp(int endpoint, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(EndpointDirection.PITCH_UP.ordinal() + 12, endpoint, callback);
    }

    @Setter(GimbalKeys.PITCH_DOWN_ENDPOINT)
    public void setEndpointInPitchDown(int endpoint, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(EndpointDirection.PITCH_DOWN.ordinal() + 12, endpoint, callback);
    }

    @Setter(GimbalKeys.YAW_RIGHT_ENDPOINT)
    public void setEndpointInYawLeft(int endpoint, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(EndpointDirection.YAW_LEFT.ordinal() + 12, endpoint, callback);
    }

    @Setter(GimbalKeys.YAW_LEFT_ENDPOINT)
    public void setEndpointInYawRight(int endpoint, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(EndpointDirection.YAW_RIGHT.ordinal() + 12, endpoint, callback);
    }

    @Getter(GimbalKeys.PITCH_UP_ENDPOINT)
    public void getEndpointInPitchUp(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(EndpointDirection.PITCH_UP.ordinal() + 12, callback);
    }

    @Getter(GimbalKeys.PITCH_DOWN_ENDPOINT)
    public void getEndpointInPitchDown(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(EndpointDirection.PITCH_DOWN.ordinal() + 12, callback);
    }

    @Getter(GimbalKeys.YAW_RIGHT_ENDPOINT)
    public void getEndpointInYawLeft(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(EndpointDirection.YAW_LEFT.ordinal() + 12, callback);
    }

    @Getter(GimbalKeys.YAW_LEFT_ENDPOINT)
    public void getEndpointInYawRight(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(EndpointDirection.YAW_RIGHT.ordinal() + 12, callback);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_DEADBAND)
    public void setControllerDeadbandOnPitch(int deadband, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.PITCH.ordinal() + 16, deadband, callback);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_DEADBAND)
    public void setControllerDeadbandOnYaw(int deadband, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.YAW.ordinal() + 16, deadband, callback);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_DEADBAND)
    public void getControllerDeadbandOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal() + 16, callback);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_DEADBAND)
    public void getControllerDeadbandOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal() + 16, callback);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_SPEED_COEFFICIENT)
    public void setControllerSpeedOnPitch(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.PITCH.ordinal() + 18, speed, callback);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_SPEED_COEFFICIENT)
    public void setControllerSpeedOnYaw(int speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.YAW.ordinal() + 18, speed, callback);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_SPEED_COEFFICIENT)
    public void getControllerSpeedOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal() + 18, callback);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_SPEED_COEFFICIENT)
    public void getControllerSpeedOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal() + 18, callback);
    }

    @Setter(GimbalKeys.PITCH_CONTROLLER_SMOOTHING_FACTOR)
    public void setControllerSmoothingOnPitch(int smoothing, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.PITCH.ordinal() + 20, smoothing, callback);
    }

    @Setter(GimbalKeys.YAW_CONTROLLER_SMOOTHING_FACTOR)
    public void setControllerSmoothingOnYaw(int smoothing, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setRoninMotorParam(Axis.YAW.ordinal() + 20, smoothing, callback);
    }

    @Getter(GimbalKeys.PITCH_CONTROLLER_SMOOTHING_FACTOR)
    public void getControllerSmoothingOnPitch(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.PITCH.ordinal() + 20, callback);
    }

    @Getter(GimbalKeys.YAW_CONTROLLER_SMOOTHING_FACTOR)
    public void getControllerSmoothingOnYaw(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getRoninMotorParam(Axis.YAW.ordinal() + 20, callback);
    }

    @Setter(GimbalKeys.MOTOR_ENABLED)
    public void setMotorEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        DataGimbalSetUserParams instance = DataGimbalSetUserParams.getInstance();
        if (enabled) {
            i = 0;
        } else {
            i = 1;
        }
        instance.setInfo("shut_down_motor", Integer.valueOf(i)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIGimbalError.getDJIError(ccode));
            }
        });
    }

    @Getter(GimbalKeys.MOTOR_ENABLED)
    public void getMotorEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onSuccess(Boolean.valueOf(DJIGimbalParamInfoManager.read("shut_down_motor").value.intValue() == 0));
    }

    private void updateCalibrationPushInfo() {
        notifyValueChangeForKeyPath(Boolean.valueOf(this.isCalibrating), convertKeyToPath(GimbalKeys.IS_CALIBRATING));
        notifyValueChangeForKeyPath(Boolean.valueOf(this.isCalibrationSuccessful), convertKeyToPath(GimbalKeys.IS_CALIBRATION_SUCCESSFUL));
    }

    /* access modifiers changed from: private */
    public void refreshCalibrationState() {
        this.setSuccessful = false;
        this.latch.setUpLatch(1);
        long beginTime = System.currentTimeMillis();
        Runnable updateCalResult = new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass5 */

            public void run() {
                DataGimbalGetUserParams.getInstance().setInfos(new String[]{DJIGimbalRoninMXAbstraction.RONIN_COMMAND[22]}).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass5.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        switch (AnonymousClass9.$SwitchMap$dji$internal$gimbal$CalibrationState[CalibrationState.values()[DJIGimbalParamInfoManager.read(DJIGimbalRoninMXAbstraction.RONIN_COMMAND[22]).value.intValue()].ordinal()]) {
                            case 1:
                                DJIGimbalRoninMXAbstraction.this.isCalibrating = false;
                                DJIGimbalRoninMXAbstraction.this.isCalibrationSuccessful = true;
                                return;
                            case 2:
                                DJIGimbalRoninMXAbstraction.this.isCalibrating = true;
                                return;
                            case 3:
                                DJIGimbalRoninMXAbstraction.this.isCalibrating = false;
                                DJIGimbalRoninMXAbstraction.this.isCalibrationSuccessful = false;
                                DJIGimbalRoninMXAbstraction.this.setSuccessful = true;
                                DJIGimbalRoninMXAbstraction.this.latch.countDownLatch();
                                return;
                            case 4:
                                DJIGimbalRoninMXAbstraction.this.isCalibrating = false;
                                DJIGimbalRoninMXAbstraction.this.isCalibrationSuccessful = true;
                                DJIGimbalRoninMXAbstraction.this.setSuccessful = true;
                                DJIGimbalRoninMXAbstraction.this.latch.countDownLatch();
                                return;
                            default:
                                return;
                        }
                    }

                    public void onFailure(Ccode ccode) {
                    }
                });
            }
        };
        while (System.currentTimeMillis() - beginTime < 30000) {
            try {
                updateCalibrationPushInfo();
                Thread.sleep(2000);
                new Thread(updateCalResult, "ronimMxAbs1").start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.latch.waitForLatch(31);
        if (!this.setSuccessful) {
            this.isCalibrating = false;
            this.isCalibrationSuccessful = false;
        }
        updateCalibrationPushInfo();
    }

    /* renamed from: dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction$9  reason: invalid class name */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$dji$internal$gimbal$CalibrationState = new int[CalibrationState.values().length];

        static {
            try {
                $SwitchMap$dji$internal$gimbal$CalibrationState[CalibrationState.DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dji$internal$gimbal$CalibrationState[CalibrationState.START.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$dji$internal$gimbal$CalibrationState[CalibrationState.FAIL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$dji$internal$gimbal$CalibrationState[CalibrationState.SUCCESS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    @Action(GimbalKeys.START_CALIBRATION)
    public void startGimbalAutoCalibration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataGimbalSetUserParams.getInstance().setInfo(RONIN_COMMAND[22], 1).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
                DJIGimbalRoninMXAbstraction.this.refreshCalibrationState();
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIGimbalError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshBalanceTestState() {
        this.setSuccessful = false;
        this.latch.setUpLatch(1);
        long beginTime = System.currentTimeMillis();
        Runnable updateTestResult = new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass7 */

            public void run() {
                DataGimbalGetUserParams.getInstance().setInfos(new String[]{DJIGimbalRoninMXAbstraction.RONIN_COMMAND[23]}).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass7.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        int testResult = DJIGimbalParamInfoManager.read(DJIGimbalRoninMXAbstraction.RONIN_COMMAND[23]).value.intValue();
                        if (((testResult >> 6) & 3) == 2) {
                            DJIGimbalRoninMXAbstraction.this.setSuccessful = true;
                            DJIGimbalRoninMXAbstraction.this.setIsTestingBalance(false);
                            DJIGimbalRoninMXAbstraction.this.setPitchTestResult(BalanceTestResult.values()[(testResult >> 2) & 3]);
                            DJIGimbalRoninMXAbstraction.this.setRollTestResult(BalanceTestResult.values()[(testResult >> 4) & 3]);
                            DJIGimbalRoninMXAbstraction.this.latch.countDownLatch();
                            return;
                        }
                        DJIGimbalRoninMXAbstraction.this.setIsTestingBalance(true);
                    }

                    public void onFailure(Ccode ccode) {
                    }
                });
            }
        };
        while (System.currentTimeMillis() - beginTime < FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS) {
            try {
                Thread.sleep(2000);
                updateBalanceTestPushInfo();
                new Thread(updateTestResult, "ronimMxAbs1").start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.latch.waitForLatch(61);
        if (!this.setSuccessful) {
            setIsTestingBalance(false);
            setPitchTestResult(BalanceTestResult.UNKNOWN);
            setRollTestResult(BalanceTestResult.UNKNOWN);
        }
        updateBalanceTestPushInfo();
    }

    private void updateBalanceTestPushInfo() {
        notifyValueChangeForKeyPath(Boolean.valueOf(this.isTestingBalance), convertKeyToPath(GimbalKeys.IS_TESTING_BALANCE));
        notifyValueChangeForKeyPath(this.pitchResult, convertKeyToPath(GimbalKeys.PITCH_TEST_RESULT));
        notifyValueChangeForKeyPath(this.rollResult, convertKeyToPath(GimbalKeys.ROLL_TEST_RESULT));
    }

    @Action(GimbalKeys.START_BALANCE_TEST)
    public void startGimbalBalanceTest(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataGimbalSetUserParams.getInstance().setInfo(RONIN_COMMAND[23], 1).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                callback.onSuccess(model);
                DJIGimbalRoninMXAbstraction.this.refreshBalanceTestState();
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIGimbalError.getDJIError(ccode));
            }
        });
    }
}
