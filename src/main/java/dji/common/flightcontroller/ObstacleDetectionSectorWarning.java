package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ObstacleDetectionSectorWarning {
    LEVEL_1(0),
    LEVEL_2(1),
    LEVEL_3(2),
    LEVEL_4(3),
    LEVEL_5(4),
    LEVEL_6(5),
    INVALID(15),
    UNKNOWN(255);
    
    private int value;

    private ObstacleDetectionSectorWarning(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static ObstacleDetectionSectorWarning find(int value2) {
        ObstacleDetectionSectorWarning result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
