package dji.sdksharedlib.keycatalog;

import dji.common.gimbal.Attitude;
import dji.common.gimbal.BalanceState;
import dji.common.gimbal.BalanceTestResult;
import dji.common.gimbal.GimbalMode;
import dji.common.gimbal.MotorControlPreset;
import dji.common.gimbal.MovementSettingsProfile;
import dji.common.gimbal.Rotation;
import dji.common.handheldcontroller.ControllerMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalMobileHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalWM230Abstraction;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;
import java.util.Map;

@EXClassNullAway
public class GimbalKeys extends DJISDKCacheKeys {
    @Key(accessType = 8, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = MotorControlPreset.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String APPLY_MOTOR_CONTROL_PRESET = "ApplyMotorControlPreset";
    @Key(accessType = 4, type = Attitude.class)
    public static final String ATTITUDE_IN_DEGREES = "AttitudeInDegrees";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalMobileHandheldAbstraction.class}, types = {BalanceState.class})
    public static final String BALANCE_STATE = "BalanceState";
    @Key(accessType = 4, type = Integer.class)
    public static final String CALIBRATION_PROGRESS = "CalibrationProgress";
    @Key(accessType = 4, type = Map.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CAPABILITIES = "Capabilities";
    public static final String COMPONENT_KEY = "Gimbal";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, type = ControllerMode.class)
    public static final String CONTROLLER_MODE = "ControllerMode";
    @Key(accessType = 8, includedAbstractions = {DJIGimbalSparkAbstraction.class}, type = Float.class)
    public static final String FINE_TUNE_PITCH_IN_DEGREES = "FineTunePitchInDegrees";
    @Key(accessType = 8, type = Float.class)
    public static final String FINE_TUNE_ROLL_IN_DEGREES = "FineTuneRollInDegrees";
    @Key(accessType = 8, includedAbstractions = {DJIGimbalWM230Abstraction.class}, type = Float.class)
    public static final String FINE_TUNE_YAW_IN_DEGREES = "FineTuneYawInDegrees";
    @InternalKey
    @Key(accessType = 4, type = Attitude.class)
    public static final String FPV_ATTITUDE_IN_DEGREES = "FpvAttitudeInDegrees";
    @Key(accessType = 4, type = Integer.class)
    public static final String FPV_SUBMODE = "FpvSubMode";
    @InternalKey
    @Key(accessType = 4, type = Float.class)
    public static final String FPV_YAW_ANGLE_WITH_AIRCRAFT_IN_DEGREE = "FpvYawAngleWithAircraftInDegree";
    @Key(accessType = 6, includedAbstractions = {DJIGimbalInspire2Abstraction.class}, type = Boolean.class)
    public static final String GIMBAL_ATTITUDE_SYNCHRONIZATION_ENABLED = "AttitudeSynchronizationEnabled";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_ATTITUDE_RESET = "IsAttitudeReset";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_CALIBRATING = "IsCalibrating";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_CALIBRATION_SUCCESSFUL = "IsCalibrationSuccessful";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_GIMBAL_ON_TOP = "IsGimbalOnTopPosition";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalWM160Abstraction.class}, types = {Boolean.class})
    public static final String IS_LIMITATION_ENABLED = "isLimitationEnabled";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalMobileHandheldAbstraction.class}, types = {Boolean.class})
    public static final String IS_MOBILE_DEVICE_MOUNTED = "isMobileDeviceMounted";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalMobileHandheldAbstraction.class}, types = {Boolean.class})
    public static final String IS_MOTOR_OVER_LOADED = "IsMotorOverloaded";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_PITCH_AT_STOP = "IsPitchAtStop";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_ROLL_AT_STOP = "IsRollAtStop";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_STUCK = "IsStuck";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Boolean.class)
    public static final String IS_TESTING_BALANCE = "IsTestingBalance";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_YAW_AT_STOP = "IsYawAtStop";
    @Key(accessType = 6, type = GimbalMode.class)
    public static final String MODE = "Mode";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Boolean.class)
    public static final String MOTOR_ENABLED = "MotorEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, type = MovementSettingsProfile.class)
    public static final String MOVEMENT_SETTINGS_PROFILE = "MovementSettingsProfile";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_CONTROLLER_DEADBAND = "PitchControllerDeadband";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalInspire2Abstraction.class, DJIGimbalWM230Abstraction.class, DJIGimbalWM160Abstraction.class}, type = Integer.class)
    public static final String PITCH_CONTROLLER_MAX_SPEED = "PitchControllerMaxSpeed";
    @Key(accessType = 3, type = Integer.class)
    public static final String PITCH_CONTROLLER_SMOOTHING_FACTOR = "PitchControllerSmoothingFactor";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalWM160Abstraction.class}, type = Integer.class)
    public static final String PITCH_CONTROLLER_SPEED_COEFFICIENT = "PitchControllerSpeedCoefficient";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_DOWN_ENDPOINT = "PitchDownEndpoint";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalSparkAbstraction.class}, type = Float.class)
    public static final String PITCH_FINE_TUNE_IN_DEGREES = "PitchFineTuneInDegrees";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, types = {Boolean.class})
    public static final String PITCH_INVERTED_CONTROL_ENABLED = "PitchInvertedControlEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_MOTOR_CONTROL_GYRO_FILTERING_FACTOR = "PitchMotorControlGyroFilteringFactor";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_MOTOR_CONTROL_PRE_CONTROL = "PitchMotorControlPreControl";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_MOTOR_CONTROL_STIFFNESS = "PitchMotorControlStiffness";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_MOTOR_CONTROL_STRENGTH = "PitchMotorControlStrength";
    @Key(accessType = 3, type = Boolean.class)
    public static final String PITCH_RANGE_EXTENSION_ENABLED = "PitchRangeExtensionEnabled";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalWM160Abstraction.class}, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, type = Integer.class)
    public static final String PITCH_SMOOTH_TRACK_ACCELERATION = "PitchSmoothTrackAcceleration";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class, DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_SMOOTH_TRACK_DEADBAND = "PitchSmoothTrackDeadband";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalWM160Abstraction.class}, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, type = Boolean.class)
    public static final String PITCH_SMOOTH_TRACK_ENABLED = "PitchSmoothTrackEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class, DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_SMOOTH_TRACK_SPEED = "PitchSmoothTrackSpeed";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = BalanceTestResult.class)
    public static final String PITCH_TEST_RESULT = "PitchTestResult";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String PITCH_UP_ENDPOINT = "PitchUpEndpoint";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESET_GIMBAL = "ResetGimbal";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESTORE_FACTORY_SETTINGS = "RestoreFactorySettings";
    @Key(accessType = 4, type = Float.class)
    public static final String ROLL_FINE_TUNE_IN_DEGREES = "RollFineTuneInDegrees";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String ROLL_MOTOR_CONTROL_GYRO_FILTERING_FACTOR = "RollMotorControlGyroFilteringFactor";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String ROLL_MOTOR_CONTROL_PRE_CONTROL = "RollMotorControlPreControl";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String ROLL_MOTOR_CONTROL_STIFFNESS = "RollMotorControlStiffness";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String ROLL_MOTOR_CONTROL_STRENGTH = "RollMotorControlStrength";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = BalanceTestResult.class)
    public static final String ROLL_TEST_RESULT = "RollTestResult";
    @Key(accessType = 8, types = {Rotation.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ROTATE = "Rotate";
    @Key(accessType = 8, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_BALANCE_TEST = "StartBalanceTest";
    @Key(accessType = 8, excludedAbstractions = {DJIGimbalSparkAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_CALIBRATION = "StartCalibration";
    @Key(accessType = 8, includedAbstractions = {DJIGimbalHandheldAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TOGGLE_SELFIE = "ToggleSelfie";
    @Key(accessType = 4, type = Float.class)
    public static final String YAW_ANGLE_WITH_AIRCRAFT_IN_DEGREE = "YawAngleWithAircraftInDegree";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String YAW_CONTROLLER_DEADBAND = "YawControllerDeadband";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalInspire2Abstraction.class}, type = Integer.class)
    public static final String YAW_CONTROLLER_MAX_SPEED = "YawControllerMaxSpeed";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalFoldingDroneAbstraction.class, DJIGimbalSparkAbstraction.class}, type = Integer.class)
    public static final String YAW_CONTROLLER_SMOOTHING_FACTOR = "YawControllerSmoothingFactor";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalFoldingDroneAbstraction.class, DJIGimbalSparkAbstraction.class}, type = Integer.class)
    public static final String YAW_CONTROLLER_SPEED_COEFFICIENT = "YawControllerSpeedCoefficient";
    @Key(accessType = 4, includedAbstractions = {DJIGimbalWM230Abstraction.class}, type = Float.class)
    public static final String YAW_FINE_TUNE_IN_DEGREES = "YawFineTuneInDegrees";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, types = {Boolean.class})
    public static final String YAW_INVERTED_CONTROL_ENABLED = "YawInvertedControlEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_LEFT_ENDPOINT = "YawLeftEndpoint";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_MOTOR_CONTROL_GYRO_FILTERING_FACTOR = "YawMotorControlGyroFilteringFactor";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_MOTOR_CONTROL_PRE_CONTROL = "YawMotorControlPreControl";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_MOTOR_CONTROL_STIFFNESS = "YawMotorControlStiffness";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_MOTOR_CONTROL_STRENGTH = "YawMotorControlStrength";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_RIGHT_ENDPOINT = "YawRightEndpoint";
    @Key(accessType = 3, excludedAbstractions = {DJIGimbalWM230Abstraction.class}, type = Boolean.class)
    public static final String YAW_SIMULTANEOUS_FOLLOW_ENABLED = "YawSynchronousFollowEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, type = Integer.class)
    public static final String YAW_SMOOTH_TRACK_ACCELERATION = "YawSmoothTrackAcceleration";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class, DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_SMOOTH_TRACK_DEADBAND = "YawSmoothTrackDeadband";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class}, type = Boolean.class)
    public static final String YAW_SMOOTH_TRACK_ENABLED = "YawSmoothTrackEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIGimbalHandheldAbstraction.class, DJIGimbalMobileHandheldAbstraction.class, DJIGimbalRoninMXAbstraction.class}, type = Integer.class)
    public static final String YAW_SMOOTH_TRACK_SPEED = "YawSmoothTrackSpeed";

    public GimbalKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
