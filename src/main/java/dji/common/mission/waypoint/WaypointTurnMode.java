package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WaypointTurnMode {
    CLOCKWISE(0),
    COUNTER_CLOCKWISE(1);
    
    private int value;

    private WaypointTurnMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int value2) {
        return this.value == value2;
    }

    public static WaypointTurnMode find(int value2) {
        WaypointTurnMode result = CLOCKWISE;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
