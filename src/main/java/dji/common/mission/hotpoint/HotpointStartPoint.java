package dji.common.mission.hotpoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum HotpointStartPoint {
    NORTH(0),
    SOUTH(1),
    WEST(2),
    EAST(3),
    NEAREST(4);
    
    private int value;

    private HotpointStartPoint(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static HotpointStartPoint find(int value2) {
        HotpointStartPoint result = NORTH;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
