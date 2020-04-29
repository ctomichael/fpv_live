package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointMissionGotoWaypointMode {
    SAFELY(0),
    POINT_TO_POINT(1);
    
    private int value;

    private WaypointMissionGotoWaypointMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static WaypointMissionGotoWaypointMode find(int value2) {
        WaypointMissionGotoWaypointMode result = SAFELY;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
