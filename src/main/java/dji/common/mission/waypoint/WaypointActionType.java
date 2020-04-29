package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointActionType {
    STAY(0),
    START_TAKE_PHOTO(1),
    START_RECORD(2),
    STOP_RECORD(3),
    ROTATE_AIRCRAFT(4),
    GIMBAL_PITCH(5),
    CAMERA_ZOOM(7),
    CAMERA_FOCUS(8);
    
    private int value;

    private WaypointActionType(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static WaypointActionType find(int value2) {
        WaypointActionType result = STAY;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
