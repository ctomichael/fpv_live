package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SmartCaptureControlMode {
    NO_GESTURE_DETECTED,
    ONE_HAND_GESTURE,
    TWO_HAND_GESTURE,
    FOLLOW_IN_PROGRESS,
    TARGET_SWITCHED;

    public static SmartCaptureControlMode getControlMode(int value) {
        switch (value) {
            case 0:
                return NO_GESTURE_DETECTED;
            case 1:
                return ONE_HAND_GESTURE;
            case 2:
                return TWO_HAND_GESTURE;
            case 3:
                return FOLLOW_IN_PROGRESS;
            case 4:
                return TARGET_SWITCHED;
            default:
                return NO_GESTURE_DETECTED;
        }
    }
}
