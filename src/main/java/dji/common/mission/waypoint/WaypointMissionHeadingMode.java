package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointMissionHeadingMode {
    AUTO(0),
    USING_INITIAL_DIRECTION(1),
    CONTROL_BY_REMOTE_CONTROLLER(2),
    USING_WAYPOINT_HEADING(3),
    TOWARD_POINT_OF_INTEREST(4);
    
    private int value;

    private WaypointMissionHeadingMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static WaypointMissionHeadingMode find(int value2) {
        WaypointMissionHeadingMode result = AUTO;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
