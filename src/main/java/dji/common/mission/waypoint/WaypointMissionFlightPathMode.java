package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointMissionFlightPathMode {
    NORMAL(0),
    CURVED(1);
    
    private int value;

    public int value() {
        return this.value;
    }

    private WaypointMissionFlightPathMode(int value2) {
        this.value = value2;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static WaypointMissionFlightPathMode find(int value2) {
        WaypointMissionFlightPathMode result = NORMAL;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
