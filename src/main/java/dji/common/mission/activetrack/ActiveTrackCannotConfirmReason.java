package dji.common.mission.activetrack;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ActiveTrackCannotConfirmReason {
    NONE,
    UNSTABLE_TARGET,
    TARGET_TOO_HIGH,
    BLOCKED_BY_OBSTACLE,
    GIMBAL_ATTITUDE_ERROR,
    TARGET_TOO_FAR,
    TARGET_TOO_CLOSE,
    AIRCRAFT_TOO_HIGH,
    AIRCRAFT_TOO_LOW,
    OBSTACLE_SENSOR_ERROR,
    UNKNOWN
}
