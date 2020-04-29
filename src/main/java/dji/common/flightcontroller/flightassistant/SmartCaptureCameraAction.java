package dji.common.flightcontroller.flightassistant;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SmartCaptureCameraAction {
    NONE,
    START_TO_SHOOT_PHOTO,
    RECORDING;

    public static SmartCaptureCameraAction getCameraAction(int value) {
        switch (value) {
            case 0:
                return NONE;
            case 1:
                return START_TO_SHOOT_PHOTO;
            case 2:
                return RECORDING;
            default:
                return NONE;
        }
    }
}
