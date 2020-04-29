package dji.common.mission.intelligenthotpoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum IntelligentHotpointTurnMode {
    CLOCKWISE(0),
    COUNTER_CLOCKWISE(1),
    UNKNOWN(255);
    
    private int value;

    private IntelligentHotpointTurnMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int value2) {
        return this.value == value2;
    }

    public static IntelligentHotpointTurnMode find(int value2) {
        IntelligentHotpointTurnMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
