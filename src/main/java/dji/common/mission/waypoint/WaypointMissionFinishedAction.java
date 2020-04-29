package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointMissionFinishedAction {
    NO_ACTION(0),
    GO_HOME(1),
    AUTO_LAND(2),
    GO_FIRST_WAYPOINT(3),
    CONTINUE_UNTIL_END(4);
    
    private int value;

    private WaypointMissionFinishedAction(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static WaypointMissionFinishedAction find(int value2) {
        WaypointMissionFinishedAction result = NO_ACTION;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
