package dji.common.flightcontroller.virtualstick;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Limits {
    public static final float ROLL_PITCH_CONTROL_MAX_ANGLE = 30.0f;
    public static final float ROLL_PITCH_CONTROL_MAX_VELOCITY = 15.0f;
    public static final float ROLL_PITCH_CONTROL_MIN_ANGLE = -30.0f;
    public static final float ROLL_PITCH_CONTROL_MIN_VELOCITY = -15.0f;
    public static final float VERTICAL_CONTROL_MAX_HEIGHT = 500.0f;
    public static final float VERTICAL_CONTROL_MAX_VELOCITY = 4.0f;
    public static final float VERTICAL_CONTROL_MIN_HEIGHT = 0.0f;
    public static final float VERTICAL_CONTROL_MIN_VELOCITY = -4.0f;
    public static final float YAW_CONTROL_MAX_ANGLE = 180.0f;
    public static final float YAW_CONTROL_MAX_ANGULAR_VELOCITY = 100.0f;
    public static final float YAW_CONTROL_MIN_ANGLE = -180.0f;
    public static final float YAW_CONTROL_MIN_ANGULAR_VELOCITY = -100.0f;
}
