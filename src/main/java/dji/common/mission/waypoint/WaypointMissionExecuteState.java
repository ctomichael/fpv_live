package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointMissionExecuteState {
    INITIALIZING(0),
    MOVING(1),
    CURVE_MODE_MOVING(2),
    CURVE_MODE_TURNING(3),
    BEGIN_ACTION(4),
    DOING_ACTION(5),
    FINISHED_ACTION(6),
    RETURN_TO_FIRST_WAYPOINT(8),
    PAUSED(9);
    
    private int value;

    private WaypointMissionExecuteState(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static WaypointMissionExecuteState find(int value2) {
        WaypointMissionExecuteState result = INITIALIZING;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
