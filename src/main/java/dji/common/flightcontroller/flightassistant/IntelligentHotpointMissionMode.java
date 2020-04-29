package dji.common.flightcontroller.flightassistant;

public enum IntelligentHotpointMissionMode {
    NONE(0),
    VISION_BASED(1),
    GPS_BASED(2),
    UNKNOWN(255);
    
    private int value;

    private IntelligentHotpointMissionMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int value2) {
        return this.value == value2;
    }

    public static IntelligentHotpointMissionMode find(int value2) {
        IntelligentHotpointMissionMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
