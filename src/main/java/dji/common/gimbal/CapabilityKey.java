package dji.common.gimbal;

import dji.common.util.DJIParamCapability;
import dji.common.util.DJIParamMinMaxCapability;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum CapabilityKey {
    ADJUST_PITCH(DJIParamMinMaxCapability.class),
    ADJUST_YAW(DJIParamMinMaxCapability.class),
    ADJUST_ROLL(DJIParamMinMaxCapability.class),
    PITCH_RANGE_EXTENSION(DJIParamMinMaxCapability.class),
    PITCH_CONTROLLER_SPEED_COEFFICIENT(DJIParamMinMaxCapability.class),
    YAW_CONTROLLER_SPEED_COEFFICIENT(DJIParamMinMaxCapability.class),
    PITCH_CONTROLLER_MAX_SPEED(DJIParamMinMaxCapability.class),
    YAW_CONTROLLER_MAX_SPEED(DJIParamMinMaxCapability.class),
    PITCH_CONTROLLER_SMOOTHING_FACTOR(DJIParamMinMaxCapability.class),
    YAW_CONTROLLER_SMOOTHING_FACTOR(DJIParamMinMaxCapability.class),
    PITCH_CONTROLLER_DEADBAND(DJIParamMinMaxCapability.class),
    YAW_CONTROLLER_DEADBAND(DJIParamMinMaxCapability.class),
    PITCH_SMOOTH_TRACK_ENABLED(DJIParamCapability.class),
    YAW_SMOOTH_TRACK_ENABLED(DJIParamCapability.class),
    PITCH_SMOOTH_TRACK_ACCELERATION(DJIParamMinMaxCapability.class),
    YAW_SMOOTH_TRACK_ACCELERATION(DJIParamMinMaxCapability.class),
    PITCH_SMOOTH_TRACK_SPEED(DJIParamMinMaxCapability.class),
    YAW_SMOOTH_TRACK_SPEED(DJIParamMinMaxCapability.class),
    PITCH_SMOOTH_TRACK_DEADBAND(DJIParamMinMaxCapability.class),
    YAW_SMOOTH_TRACK_DEADBAND(DJIParamMinMaxCapability.class),
    PITCH_UP_ENDPOINT(DJIParamMinMaxCapability.class),
    PITCH_DOWN_ENDPOINT(DJIParamMinMaxCapability.class),
    YAW_LEFT_ENDPOINT(DJIParamMinMaxCapability.class),
    YAW_RIGHT_ENDPOINT(DJIParamMinMaxCapability.class),
    PITCH_MOTOR_CONTROL_STIFFNESS(DJIParamMinMaxCapability.class),
    YAW_MOTOR_CONTROL_STIFFNESS(DJIParamMinMaxCapability.class),
    ROLL_MOTOR_CONTROL_STIFFNESS(DJIParamMinMaxCapability.class),
    PITCH_MOTOR_CONTROL_STRENGTH(DJIParamMinMaxCapability.class),
    YAW_MOTOR_CONTROL_STRENGTH(DJIParamMinMaxCapability.class),
    ROLL_MOTOR_CONTROL_STRENGTH(DJIParamMinMaxCapability.class),
    PITCH_MOTOR_CONTROL_GYRO_FILTERING_FACTOR(DJIParamMinMaxCapability.class),
    YAW_MOTOR_CONTROL_GYRO_FILTERING_FACTOR(DJIParamMinMaxCapability.class),
    ROLL_MOTOR_CONTROL_GYRO_FILTERING_FACTOR(DJIParamMinMaxCapability.class),
    PITCH_MOTOR_CONTROL_PRE_CONTROL(DJIParamMinMaxCapability.class),
    YAW_MOTOR_CONTROL_PRE_CONTROL(DJIParamMinMaxCapability.class),
    ROLL_MOTOR_CONTROL_PRE_CONTROL(DJIParamMinMaxCapability.class),
    MOVEMENT_SETTINGS(DJIParamCapability.class);
    
    private Class<? extends DJIParamCapability> capabilityCls;

    public String value() {
        return name();
    }

    public Class<? extends DJIParamCapability> capabilityClass() {
        return this.capabilityCls;
    }

    private CapabilityKey(Class<? extends DJIParamCapability> capabilityClass) {
        this.capabilityCls = capabilityClass;
    }
}
