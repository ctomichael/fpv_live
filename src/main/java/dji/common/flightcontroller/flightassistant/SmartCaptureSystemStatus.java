package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataEyeGetPushPalmControlNotification;

@EXClassNullAway
public enum SmartCaptureSystemStatus {
    INITIALIZING,
    NOT_FLYING_AND_FACE_AWARE_ACTIVATING,
    NOT_FLYING_AND_FACE_AWARE_ACTIVATED,
    GESTURE_LAUNCH,
    FLYING_AND_FACE_AWARE_ACTIVATING,
    FLYING_AND_FACE_AWARE_ACTIVATED,
    PALM_CONTROL,
    GESTURE_LAND,
    FOLLOWING,
    CAMERA_CAPTURE_ACTION,
    UNKNOWN;

    public static SmartCaptureSystemStatus getActionState(int value) {
        switch (value) {
            case 0:
                return INITIALIZING;
            case 1:
                return NOT_FLYING_AND_FACE_AWARE_ACTIVATING;
            case 2:
                return NOT_FLYING_AND_FACE_AWARE_ACTIVATED;
            case 3:
                return GESTURE_LAUNCH;
            case 4:
                return FLYING_AND_FACE_AWARE_ACTIVATING;
            case 5:
                return FLYING_AND_FACE_AWARE_ACTIVATED;
            case 6:
                return PALM_CONTROL;
            case 7:
                return GESTURE_LAND;
            default:
                return INITIALIZING;
        }
    }

    public static SmartCaptureSystemStatus convertNotificationToSystemStatus(DataEyeGetPushPalmControlNotification notification) {
        switch (notification.getCameraAction()) {
            case 1:
            case 2:
                return CAMERA_CAPTURE_ACTION;
            default:
                if (notification.getPalmControlControlMode() == 3) {
                    return FOLLOWING;
                }
                return getActionState(notification.getPalmControlActionState());
        }
    }
}
