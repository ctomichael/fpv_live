package dji.common.flightcontroller.flightassistant;

import dji.midware.data.model.P3.DataEyeGetPushPalmControlNotification;

public enum SmartCaptureAction {
    NONE,
    ADJUSTING_POSITION,
    ADJUSTING_DISTANCE,
    SWITCHING_CONTROL,
    SHOOTING_PHOTO,
    RECORDING_VIDEO,
    UNKNOWN;

    public static SmartCaptureAction convertNotificationToAction(DataEyeGetPushPalmControlNotification notification) {
        if (notification.getPalmControlActionState() != 6) {
            return NONE;
        }
        switch (notification.getCameraAction()) {
            case 1:
                return SHOOTING_PHOTO;
            case 2:
                return RECORDING_VIDEO;
            default:
                switch (notification.getPalmControlControlMode()) {
                    case 0:
                    case 3:
                        return NONE;
                    case 1:
                        return ADJUSTING_POSITION;
                    case 2:
                        return ADJUSTING_DISTANCE;
                    case 4:
                        return SWITCHING_CONTROL;
                    default:
                        return UNKNOWN;
                }
        }
    }
}
